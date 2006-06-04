package org.springmodules.parallel;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * Deals with registered interceptors
 * @author Rod Johnson
 *
 */
public class ParallelTaskInterceptor implements MethodInterceptor {

    public Object invoke(MethodInvocation arg0) throws Throwable {
        throw new UnsupportedOperationException();
    }

}
