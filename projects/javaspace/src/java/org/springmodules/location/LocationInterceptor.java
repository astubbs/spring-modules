package org.springmodules.location;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.util.ClassUtils;

/**
 * 
 * Immature, experimental, not necessary to the prototype.
 * This can optionally choose routing between a local object
 * and a space. It does this as an interceptor in the AOP
 * invocation chain, which can simply invoke a target method and
 * prevent progress down the chain towards the terminal interceptor
 * that would write and take from a space.
 * 
 * @author Rod Johnson
 * 
 * TODO local spaces: compare performance
 * 
 * Assumes that calling proceed will invoke remotely
 * 
 * TODO what about taking into account server load!?
 * Frequency of invocations to this service? But what
 * about other services?
 * 
 * TODO pluggable algorithm... round robin etc.
 *
 */
public class LocationInterceptor implements MethodInterceptor, BeanFactoryAware {
    
	private static final Log log = LogFactory.getLog(LocationInterceptor.class);
	
    private Map methodStats = new HashMap();
    
    private long thresholdMillis = 0;
    
    private Map interfaceToObjectMap = new HashMap();
    
    private int localInvokes;
    
    private int remoteInvokes;
    
    // TODO synchronization!?
    
    private String[] beanNames;
    
    public void setBeanNames(String[] beanNames) {
        this.beanNames = beanNames;
    }
    
    public void setThresholdMillis(long thresholdMillis) {
        this.thresholdMillis = thresholdMillis;
    }
    
    public void setBeanFactory(BeanFactory bf) {
        // Get all singletons
        for (int i = 0; i < beanNames.length; i++) {
            if (bf.isSingleton(beanNames[i])) {
                Class type = bf.getType(beanNames[i]);
                if (type != null) {
                    Class[] interfaces = ClassUtils.getAllInterfacesForClass(type);
                    for (int j = 0; j < interfaces.length; j++) {
                        Class intf = interfaces[i];
                        // TODO exclude Spring
                        // TODO ban duplicates
                        // TODO MAPPING FROM INTERFACE TO BEAN!?
                        interfaceToObjectMap.put(intf, bf.getBean(beanNames[i]));
                        // TODO what about classes
                    }
                }
            }
        }
    }
    
    protected Object localBean(MethodInvocation mi) {
        return interfaceToObjectMap.get(mi.getMethod().getDeclaringClass());
    }
    
    protected Object invokeLocally(MethodInvocation mi) throws Throwable {
        return AopUtils.invokeJoinpointUsingReflection(null, mi.getMethod(), mi.getArguments());
    }

    public Object invoke(MethodInvocation mi) throws Throwable {
        
        Object localBean = localBean(mi);
        if (localBean == null) {
            // No option
        	if (log.isDebugEnabled())
        		log.debug("Must invoke space: not available locally");
            ++remoteInvokes;
            return mi.proceed();
        }
        
        // It is available locally and remotely, let's see if it was annotated
        MethodStats ms = null;
        synchronized (methodStats) {
            ms = (MethodStats) methodStats.get(mi.getMethod());
        }
        if (ms != null) {
            // Need to outsource to space
        	if (log.isDebugEnabled())
        		log.debug("Marked as slow: invoke space");
            ++remoteInvokes;
            return mi.proceed();
        }
        else {
            // Available locally and remotely, let's invoke locally and time
        	if (log.isDebugEnabled())
        		log.debug("Invoking locally");
            ++localInvokes;
            long started = System.currentTimeMillis();
            try {
                return AopUtils.invokeJoinpointUsingReflection(localBean, mi.getMethod(), mi.getArguments());
            }
            finally {
                long elapsed = System.currentTimeMillis() - started;
                if (elapsed > this.thresholdMillis) {
                    // Candidate for outsourcing
                    // TODO keep track of average
                    synchronized (methodStats) {
                        this.methodStats.put(mi.getMethod(), new MethodStats(mi.getMethod()));
                    }
                }
            }
        }
    }
    
    public int getLocalInvokes() {
        return localInvokes;
    }
    
    public int getRemoteInvokes() {
        return remoteInvokes;
    }

    
    private class MethodStats {
        private Method m;
        public MethodStats(Method m) {
            this.m = m;
        }
    }
}
