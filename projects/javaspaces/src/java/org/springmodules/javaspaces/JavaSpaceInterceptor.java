package org.springmodules.javaspaces;

import java.io.Serializable;
import java.lang.reflect.Method;

import net.jini.core.lease.Lease;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.LazyLoader;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.remoting.RemoteAccessException;
import org.springmodules.javaspaces.entry.AbstractMethodCallEntry;
import org.springmodules.javaspaces.entry.MethodResultEntry;
import org.springmodules.javaspaces.entry.RunnableMethodCallEntry;
import org.springmodules.javaspaces.entry.ServiceSeekingMethodCallEntry;
import org.springmodules.javaspaces.entry.UidFactory;
import org.springmodules.javaspaces.entry.support.DefaultUidFactory;

/**
 * Generic invoker for Java space-implemented methods. It allows method
 * invocations on a given proxy interface to be transparently implemented as
 * write/take operations of a generic entry to a JavaSpace. This is used as the
 * terminal interceptor for a Spring AOP proxy. See the Spring implementation of
 * local and remote EJB proxies, which illustrates the same concepts.
 * <p>
 * This class supports two modes of JavaSpace usage:
 * <li>The endpoint hosts the service, and only the method to be invoked and
 * arguments are passed around the network. This is somewhat like a SLSB
 * approach to remoting, although there is no assumption as to where the
 * endpoint is.
 * <li>The endpoint does not necessarily host the service. The code to execute
 * as well as the method and arguments are shipped to the node that takes from
 * the space. This is a "runnable entry" approach. To enable this, simply set
 * the serializableTarget property on this object to an object that can be
 * serialized to implement the method. Typically this will be dependency
 * injected with Spring, allowing the object to be configured. Make sure this
 * object is serializable. Alternatively, override the serializableTarget()
 * method to return a custom object.
 * <p>
 * This interceptor is generic. One interceptor instance can serve multiple
 * methods.
 *
 * @author Rod Johnson
 * @author Costin Leau
 */
public class JavaSpaceInterceptor implements MethodInterceptor {

	private static final Log log = LogFactory.getLog(JavaSpaceInterceptor.class);

	public class LazyResult {
		AbstractMethodCallEntry call;

		public LazyResult(AbstractMethodCallEntry call) {
			this.call = call;
		}

		public Object getResult() {
			try {
				return handleResultRetrieval(call);
			}
			catch (Throwable throwable) {
				// TODO: handle this properly
				throw new RuntimeException(throwable);
			}
		}

	}

	private JavaSpaceTemplate jsTemplate;

	/**
	 * TODO this parameter causes an object to be serialized Can inject it
	 */
	private Object serializableTarget;

	private long timeoutMillis = 100;

	private boolean synchronous = true;

	private UidFactory uidFactory = new DefaultUidFactory();

	public JavaSpaceInterceptor() {
	}

	public void setTimeoutMillis(long timeoutMillis) {
		this.timeoutMillis = timeoutMillis;
	}

	/**
	 * The target will be set by dependency injection. If it is set (ie is
	 * non-null) a RunnableMethodCallEntry will be created that includes the
	 * serialized target as well as the method and arguments. If this property
	 * is not set, the endpoint will be assumed to have a service on which the
	 * method invocation should be made.
	 *
	 * @param serializableTarget
	 */
	public void setSerializableTarget(Object serializableTarget) {
		this.serializableTarget = serializableTarget;
	}

	public JavaSpaceInterceptor(JavaSpaceTemplate jsTemplate) throws Exception {
		setJavaSpaceTemplate(jsTemplate);
	}

	/**
	 * Set the JavaSpaceTemplate Spring helper used to simplify working with the
	 * JavaSpace API.
	 *
	 * @param jsTemplate
	 */
	public void setJavaSpaceTemplate(JavaSpaceTemplate jsTemplate) {
		this.jsTemplate = jsTemplate;
	}

	/**
	 * Implementation of the AOP Alliance MethodInterceptor interface. This will
	 * be terminal interceptor in the AOP interceptor chain. It creates a
	 * generic entry representing the method call, writes it to the space and
	 * returns a MethodResultEntry in response. The call can be blocking
	 * (synchronous) or non-blocking (asynchronous).
	 */
	public Object invoke(MethodInvocation mi) throws Throwable {

		AbstractMethodCallEntry call = null;

		Object target = getSerializableTarget(mi);
		// TODO Could also decide whether to serializable target
		// based on a method annotation
		if (target == null) {
			call = createServiceSeekingMethodCallEntry(mi.getMethod(), mi.getArguments());
		}
		else {
			call = createRunnableMethodCallEntry(mi.getMethod(), mi.getArguments(), target);
		}

		if (log.isDebugEnabled())
			log.debug("Invoke: Call is " + call);

		jsTemplate.write(call, timeoutMillis);

		// if synch do a blocking waiting
		if (synchronous)
			return handleResultRetrieval(call);
		// else return a lazy result
		return createLazyResult(call);

	}

	/**
	 * Gets the ServiceSeekingMethodCallEntry
	 * @param method the method
	 * @param args the method agrs
	 * @return ServiceSeekingMethodCallEntry
	 */
	protected ServiceSeekingMethodCallEntry createServiceSeekingMethodCallEntry(Method method, Object[] args){
		return new ServiceSeekingMethodCallEntry(method, args, getUidFactory().generateUid());
	}

	/**
	 * Gets RunnableMethodCallEntry
	 * @param method the method
	 * @param args the method arg's
	 * @param target the target
	 * @return RunnableMethodCallEntry
	 */
	protected RunnableMethodCallEntry createRunnableMethodCallEntry(Method method, Object[] args, Object target){
		return new RunnableMethodCallEntry(method, args, target, getUidFactory().generateUid());
	}

	/**
	 * Create a lazy proxy through CGLib.
	 *
	 * @param call
	 * @return
	 */
	protected Object createLazyResult(final AbstractMethodCallEntry call) {
		// CGLib usage
		LazyLoader lazyCallback = new LazyLoader() {
			public Object loadObject() throws Exception {
				if (log.isTraceEnabled())
					log.trace("creating lazy proxy");
				try {
					return handleResultRetrieval(call);

				}
				catch (Throwable e) {
					throw new RuntimeException(e);
				}
			}
		};

		return Enhancer.create(call.getMethod().getReturnType(), lazyCallback);
	}

	protected Object handleResultRetrieval(AbstractMethodCallEntry call) throws Throwable {
		MethodResultEntry result = null;

		result = takeResult(call);
		// If it's an exception, throw it to the caller. The
		// exception will propogate up the call chain.
		if (!result.successful()) {
			throw result.getFailure();
		}
		else {
			return result.getResult();
		}
	}

	protected MethodResultEntry takeResult(AbstractMethodCallEntry call) throws Throwable {
		MethodResultEntry result = null;
		MethodResultEntry template = createMethodResultEntry(call);

		result = (MethodResultEntry) jsTemplate.take(template, timeoutMillis);

		if (log.isDebugEnabled())
			log.debug("Result=" + result);

		// if no result found try to clean up and thrown an exception
		if (result == null) {
			// TODO this is not being tested
			jsTemplate.takeIfExists(call, timeoutMillis);
			throw new RemoteAccessException("Couldn't get result for " + call);
		}
		return result;
	}

	/**
	 * Create the specific method result entry
	 * @param call the call entry method
	 * @return the method result entry.
	 */
	protected MethodResultEntry createMethodResultEntry(AbstractMethodCallEntry call){
		return new MethodResultEntry((Method) null, call.uid, null);
	}

	/**
	 * Subclasses can override this method if desired to obtain a target in a
	 * different way, potentially based on the method to be invoked. This
	 * implementation simply returns the value of the serializableTarget bean
	 * property.
	 *
	 * @return the target to include in a RunnableMethodCallEntry. If the return
	 *         value is null, a ServiceSeekingMethodCallEntry will be created,
	 *         which requires endpoints to host the necessary service.
	 */
	protected Object getSerializableTarget(MethodInvocation mi) {
		return serializableTarget;
	}

	/**
	 * @return Returns the synchronous.
	 */
	public boolean isSynchronous() {
		return synchronous;
	}

	/**
	 * @param synchronous
	 *            The synchronous to set.
	 */
	public void setSynchronous(boolean synchronous) {
		this.synchronous = synchronous;
	}

	/**
	 * @return Returns the uidFactory.
	 */
	public UidFactory getUidFactory() {
		return uidFactory;
	}

	/**
	 * Sets the uid factory to use for generating Entry uids. By default,
	 * DefaultUidFactory is used.
	 *
	 * @param uidFactory
	 *            The uidFactory to set.
	 */
	public void setUidFactory(UidFactory uidFactory) {
		this.uidFactory = uidFactory;
	}

}
