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

package org.springmodules.xt.model.introductor;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;
import org.springframework.aop.IntroductionInterceptor;
import org.springmodules.xt.model.introductor.annotation.MapToTargetField;
import org.springmodules.xt.model.introductor.annotation.OverrideTarget;

/**
 * Abstract Spring AOP Introduction Interceptor.<br>
 * 
 * @author Sergio Bossa
 */
public abstract class AbstractIntroductorInterceptor implements IntroductionInterceptor {
    
    private static final Logger logger = Logger.getLogger(AbstractIntroductorInterceptor.class);
    
    private Set<Class> introducedInterfaces = new HashSet<Class>();
    
    private Map<Method, Method> introducedMethods = new HashMap<Method, Method>();
    private Map<Method, Method> targetMethods = new HashMap<Method, Method>();
    
    /**
     * @param introducedInterfaces The interfaces to introduce.
     */
    public AbstractIntroductorInterceptor(Class[] introducedInterfaces) {
        Collections.addAll(this.introducedInterfaces, introducedInterfaces);
    }
    
    public boolean implementsInterface(Class aClass) {
        return this.isIntroduced(aClass);
    }
    
    /**
     * Get the method from the target object corresponding to the given {@link org.aopalliance.intercept.MethodInvocation}.
     *
     * @return The target method, or null if no method is found.
     */
    protected Method getTargetMethod(MethodInvocation methodInvocation) {
        Object target = methodInvocation.getThis();
        Method invokedMethod = methodInvocation.getMethod(); 
        Method result = null;
        if (this.targetMethods.containsKey(invokedMethod)) {
            Method targetMethod = this.targetMethods.get(invokedMethod);
            logger.debug("Found cached target method: " + targetMethod.getName());
            result = targetMethod;
        }
        else {
            List<Method> methods = Arrays.asList(target.getClass().getMethods());
            for(Method targetMethod : methods) {
                if (targetMethod.getName().equals(invokedMethod.getName()) 
                 && Arrays.equals(targetMethod.getParameterTypes(), invokedMethod.getParameterTypes())
                 && targetMethod.getReturnType().equals(invokedMethod.getReturnType())) {
                    this.targetMethods.put(invokedMethod, targetMethod);
                    result = targetMethod;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Return true if the given method is declared into an introduced interface, false otherwise.
     */
    protected boolean isIntroduced(Method invokedMethod) {
        boolean result = false;
        if (this.introducedMethods.containsKey(invokedMethod)) {
            Method introducedMethod = this.introducedMethods.get(invokedMethod);
            logger.debug("Found cached introduced method: " + introducedMethod.getName());
            result = true;
        }
        else {
            List<Method> methods = new LinkedList();
            for (Class intf : this.introducedInterfaces) {
                methods.addAll(Arrays.asList(intf.getMethods()));
            }
            for(Method introducedMethod : methods) {
                if (introducedMethod.getName().equals(invokedMethod.getName()) 
                 && Arrays.equals(introducedMethod.getParameterTypes(), invokedMethod.getParameterTypes())
                 && introducedMethod.getReturnType().equals(invokedMethod.getReturnType())) {
                    this.introducedMethods.put(invokedMethod, introducedMethod);
                    result = true;
                    break;
                }
            }
        }
        return result;
    }
    
    /**
     * Return true if the given class is an introduced interface, false otherwise.
     */
    protected boolean isIntroduced(Class aClass) {
        if (aClass.isInterface()) {
            for(Class introduced : this.introducedInterfaces) {
                if (aClass.isAssignableFrom(introduced)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Return true if the {@link org.springmodules.xt.model.introductor.annotation.MapToTargetField}
     * annotation is applied to the given method, false otherwise.
     */
    protected boolean shouldMapToTargetField(Method aMethod) {
        if (this.isIntroduced(aMethod)) {
            Method introducedMethod = this.introducedMethods.get(aMethod);
            if (introducedMethod.isAnnotationPresent(MapToTargetField.class)) {
                logger.debug("Found annotation: " + MapToTargetField.class);
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }

    /**
     * Return true if the {@link org.springmodules.xt.model.introductor.annotation.OverrideTarget}
     * annotation is applied to the given method, false otherwise.
     */
    protected boolean shouldOverrideTarget(Method aMethod) {
        if (this.isIntroduced(aMethod)) {
            Method introducedMethod = this.introducedMethods.get(aMethod);
            if (introducedMethod.isAnnotationPresent(OverrideTarget.class)) {
                logger.debug("Found annotation: " + OverrideTarget.class);
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }
}
