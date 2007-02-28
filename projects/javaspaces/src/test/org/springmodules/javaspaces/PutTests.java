package org.springmodules.javaspaces;

import java.io.Serializable;

import net.jini.core.entry.Entry;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;
import org.springframework.util.StopWatch;
import org.springmodules.beans.ITestBean;
import org.springmodules.beans.TestBean;
import org.springmodules.javaspaces.DelegatingWorker;
import org.springmodules.javaspaces.JavaSpaceInterceptor;
import org.springmodules.javaspaces.JavaSpaceTemplate;
import org.springmodules.javaspaces.entry.MethodResultEntry;


public class PutTests extends AbstractDependencyInjectionSpringContextTests {

	private JavaSpaceTemplate template;

	private DelegatingWorker worker;

	protected void createWorkerThreads() {
		// iTestBeanWorker = new DelegatingWorker();
		// iTestBeanWorker.setBusinessInterface(ITestBean.class);
		// iTestBeanWorker.setDelegate(new TestBean());
		// iTestBeanWorker.setJavaSpaceTemplate(template);
		worker = (DelegatingWorker) applicationContext.getBean("testBeanWorker");
		Thread testBeanWorkerThread = new Thread(worker);
		testBeanWorkerThread.start();

		// itbThread = new Thread(iTestBeanWorker);
		// itbThread.start();
	}

	protected void killWorkerThreads() {
		worker.stop();
	}

	public void testSuccessfulInvocations() throws Exception {

		TestBean target = new TestBean();

		JavaSpaceInterceptor si = new JavaSpaceInterceptor(template);
		ProxyFactory pf = new ProxyFactory(new Class[] { ITestBean.class });
		pf.addAdvice(new PerformanceMonitorInterceptor());
		pf.addAdvice(si);

		ITestBean proxy = (ITestBean) pf.getProxy();

		String oldName = proxy.getName();

		for (int i = 0; i < 2; i++) {
			String name = "john" + i;
			proxy.setName(name);
			assertEquals(name, proxy.getName());
		}
		proxy.setName(oldName);
	}

	public void testCheckedException() throws Throwable {
		testThrowable(new NumberFormatException());
	}

	public void testUncheckedException() throws Throwable {
		// TODO why does this get no such class exception?
		// testThrowable(new NoSuchBeanDefinitionException("", ""));
		testThrowable(new RuntimeException(""));
	}

	private void testThrowable(Throwable t) throws Throwable {

		JavaSpaceInterceptor si = new JavaSpaceInterceptor(template);
		ProxyFactory pf = new ProxyFactory(new Class[] { ITestBean.class });
		pf.addAdvice(new PerformanceMonitorInterceptor());
		pf.addAdvice(si);

		ITestBean proxy = (ITestBean) pf.getProxy();

		try {
			proxy.exceptional(t);
			fail("Should have aborted with exception");
		}
		catch (Throwable got) {
			if (got.getClass().equals(t.getClass())) {
				// Ok
				System.out.println("Expected exception, " + got);
			}
			else {
				throw got;
			}
		}
	}

	public void testAsynchInterceptor() throws Throwable {
		JavaSpaceInterceptor si = new JavaSpaceInterceptor(template);
		si.setSynchronous(false);
		ProxyFactory pf = new ProxyFactory(new Class[] { ITestBean.class });
		pf.addAdvice(new PerformanceMonitorInterceptor());
		pf.addAdvice(si);

		ITestBean proxy = (ITestBean) pf.getProxy();
		Object result;
		try {
			result = proxy.getName();
			fail("cglib can't proxy final classes");
		}
		catch (IllegalArgumentException e) {
			// can't proxy strings
		}
	}

	public void testLazyTest() throws Throwable {
		JavaSpaceInterceptor si = new JavaSpaceInterceptor(template);
		si.setSynchronous(false);
		ProxyFactory pf = new ProxyFactory(new Class[] { ITestBean.class });
		pf.addAdvice(new PerformanceMonitorInterceptor());
		pf.addAdvice(si);
		ITestBean proxy = (ITestBean) pf.getProxy();

		// nothing before the call
		assertNull(template.getSpace().readIfExists(new MethodResultEntry(), template.getCurrentTransaction(), 100));
		ITestBean lazyResult = proxy.getSpouse();
		System.out.println("spouse name is " + lazyResult.getClass());
		assertTrue(AopUtils.isCglibProxyClass(lazyResult.getClass()));
		System.out.println("should not be initialized");
		System.out.println(lazyResult.getClass());
		System.out.println(lazyResult.hashCode());
		System.out.println("should not be initialized");
		System.out.println(lazyResult);
		System.out.println("should be initialized");
		assertEquals("kerry", lazyResult.getName());
	}

	public class PerformanceMonitorInterceptor implements MethodInterceptor, Serializable {

		public Object invoke(MethodInvocation invocation) throws Throwable {
			String name = invocation.getMethod().getDeclaringClass().getName() + "." + invocation.getMethod().getName();
			StopWatch sw = new StopWatch(name);
			sw.start(name);
			try {
				return invocation.proceed();
			}
			finally {
				sw.stop();

				System.out.println(sw.shortSummary());
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springmodules.test.AbstractDependencyInjectionSpringContextTests#getConfigLocations()
	 */
	protected String[] getConfigLocations() {
		return new String[] { "/org/springmodules/javaspaces/space-context.xml" };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springmodules.test.AbstractDependencyInjectionSpringContextTests#onSetUp()
	 */
	protected void onSetUp() throws Exception {
		createWorkerThreads();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springmodules.test.AbstractDependencyInjectionSpringContextTests#onTearDown()
	 */
	protected void onTearDown() throws Exception {
		killWorkerThreads();

		Entry entry = new Entry() {
		};

		Entry e = null;
		do {
			e = template.takeIfExists(e, 5);
			System.out.println("Cleanup: " + e);
		} while (e != null);
	}

	/**
	 * @param template
	 *            The template to set.
	 */
	public void setTemplate(JavaSpaceTemplate template) {
		this.template = template;
	}

}
