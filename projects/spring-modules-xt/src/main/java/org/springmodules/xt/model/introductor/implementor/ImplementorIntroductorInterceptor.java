/*
 * Copyright 2006 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springmodules.xt.model.introductor.implementor;

import java.lang.reflect.Method;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;
import org.springmodules.xt.model.introductor.AbstractIntroductorInterceptor;
import org.springmodules.xt.model.introductor.support.UnsupportedAnnotationException;

/**
 * Spring AOP Interceptor that introduces to a given <i>target</i> object a set of interfaces with an implementation provided by a
 * given <i>implementor</i> object.<br>
 *
 * @author Sergio Bossa
 */
public class ImplementorIntroductorInterceptor extends AbstractIntroductorInterceptor {
    
    private static final Logger logger = Logger.getLogger(ImplementorIntroductorInterceptor.class);
    
    private Object implementor;
    
    /**
     * Constructor.
     * @param introducedInterfaces The interfaces to introduce, carrying the new behaviour.
     * @param implementor The implementor object, specifying the implementation of the introduced interfaces.
     * Please note that it must implement the given interfaces.
     */
    public ImplementorIntroductorInterceptor(Class[] introducedInterfaces, Object implementor) {
        super(introducedInterfaces);
        for (Class intf : introducedInterfaces) {
            if ((!intf.isInterface()) || (!intf.isAssignableFrom(implementor.getClass()))) {
                throw new IllegalArgumentException("The implementor object must implement all introduced interfaces!");
            }
        }
        this.implementor = implementor;
    }
    
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Object result = null;
        Method invokedMethod = methodInvocation.getMethod();
        if (this.shouldOverrideTarget(invokedMethod) || this.shouldMapToTargetField(invokedMethod)) {
            throw new UnsupportedAnnotationException("Unsupported annotation on method: " + invokedMethod);
        } else {
            if (this.isIntroduced(invokedMethod)) {
                result = this.executeOnProxy(methodInvocation, invokedMethod);
            } else {
                result =  methodInvocation.proceed();
            }
        }
        
        return result;
    }
    
    protected Object executeOnTargetMethod(MethodInvocation methodInvocation, Method method) throws Exception {
        logger.debug(new StringBuilder("Executing on target; method: ").append(method.getName()));
        return method.invoke(methodInvocation.getThis(), methodInvocation.getArguments());
    }
    
    protected Object executeOnProxy(MethodInvocation methodInvocation, Method method) throws Exception {
        logger.debug(new StringBuilder("Introducing method: ") .append(method.getName()));
        return method.invoke(this.implementor, methodInvocation.getArguments());
    }
    
    protected Object executeOnTargetField(MethodInvocation methodInvocation, Method invokedMethod) throws Exception {
        throw new UnsupportedAnnotationException("Unsupported annotation on method: " + invokedMethod);
    }
}
