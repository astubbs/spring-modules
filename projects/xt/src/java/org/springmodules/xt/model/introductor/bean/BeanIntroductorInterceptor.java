/*
 * Copyright 2006 - 2007 the original author or authors.
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

package org.springmodules.xt.model.introductor.bean;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springmodules.xt.model.introductor.AbstractIntroductorInterceptor;
import org.springmodules.xt.model.introductor.support.IllegalReturnTypeException;

/**
 * Spring AOP Introduction Interceptor for dynamically constructing JavaBeans style classes with additional setter and getter methods.<br>
 * 
 * @author Sergio Bossa
 */
public class BeanIntroductorInterceptor extends AbstractIntroductorInterceptor {
    
    private static final Logger logger = Logger.getLogger(BeanIntroductorInterceptor.class);
    
    private Map<String, Object> fields = new ConcurrentHashMap();

    /**
     * Constructor.
     * @param introducedInterfaces The interfaces to introduce.
     */
    public BeanIntroductorInterceptor(Class[] introducedInterfaces) {
        super(introducedInterfaces);
    }
    
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Object result = null;
        Method invokedMethod = methodInvocation.getMethod();
        if (this.shouldOverrideTarget(invokedMethod)) {
            result = this.executeOnProxy(methodInvocation, invokedMethod);
        }
        else if (this.shouldMapToTargetField(invokedMethod)) {
            result = this.executeOnTargetField(methodInvocation, invokedMethod);
        }
        else {
            Method targetMethod = this.getTargetMethod(methodInvocation);
            if (this.isIntroduced(invokedMethod) && targetMethod != null) {
                result = this.executeOnTargetMethod(methodInvocation, targetMethod);
            }
            else if (this.isIntroduced(invokedMethod)) {
                result = this.executeOnProxy(methodInvocation, invokedMethod);
            }
            else {
                result =  methodInvocation.proceed();
            }
        }
        
        return result;
    }
    
    protected Object executeOnTargetMethod(MethodInvocation methodInvocation, Method method) throws Exception {
        logger.debug("Executing on target; method: " + method.getName());
        return method.invoke(methodInvocation.getThis(), methodInvocation.getArguments());
    }
    
    protected Object executeOnTargetField(MethodInvocation methodInvocation, Method method) throws Exception {
        logger.debug("Mapping to target field; method: " + method.getName());
        Object result = null;
        try {
            Object target = methodInvocation.getThis();
            if (method.getName().startsWith("get")) {
                String fieldName = StringUtils.uncapitalize(method.getName().substring(3));
                Field field = target.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                result = field.get(target);
            }
            else if (method.getName().startsWith("is")) {
                String fieldName = StringUtils.uncapitalize(method.getName().substring(2));
                Field field = target.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                result = field.get(target);
            }
            else if (method.getName().startsWith("set")) {
                String fieldName = StringUtils.uncapitalize(method.getName().substring(3));
                Field field = target.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                if (methodInvocation.getArguments().length != 1) {
                    throw new IllegalStateException("The setter method " + method.getName() + " must have only one argument!");
                }
                else {
                    field.set(target, methodInvocation.getArguments()[0]);
                }
            }
            else {
                throw new UnsupportedOperationException("The introduced interface must contain only setter and getter methods.");
            }
        }
        catch(Exception ex) {
            logger.warn("Something wrong happened calling: " + method.getName());
            logger.warn("Exception message: " + ex.getMessage());
            throw ex;
        }
        return result;
    }
    
    protected Object executeOnProxy(MethodInvocation methodInvocation, Method method) throws Exception {
        logger.debug("Introducing method: " + method.getName());
        Object result = null;
        try {
            if (method.getName().startsWith("get")) {
                if (method.getReturnType().isPrimitive()) {
                    throw new IllegalReturnTypeException("Return types of your introduced interfaces cannot be primitives.");
                }
                result = this.fields.get(method.getName().substring(3));
            }
            else if (method.getName().startsWith("is")) {
                if (method.getReturnType().isPrimitive()) {
                    throw new IllegalReturnTypeException("Return types of your introduced interfaces cannot be primitives.");
                }
                result = this.fields.get(method.getName().substring(2));
            }
            else if (method.getName().startsWith("set")) {
                if (methodInvocation.getArguments().length != 1) {
                    throw new IllegalStateException("The setter method " + method.getName() + " must have only one argument!");
                }
                else {
                    String key = method.getName().substring(3);
                    Object value = methodInvocation.getArguments()[0];
                    // ConcurrentHashMap doesn't support null values: so, if the value is not null, proceed with setting:
                    if (value != null) {
                        this.fields.put(key, value);
                    }
                    // Else, remove it (this equals setting it null):
                    else {
                        this.fields.remove(key); 
                    }
                }
            }
            else {
                throw new UnsupportedOperationException("The introduced interface must contain only setter and getter methods.");
            }
        }
        catch(Exception ex) {
            logger.warn("Something wrong happened calling: " + method.getName());
            logger.warn("Exception message: " + ex.getMessage());
            throw ex;
        }
        return result;
    }
}
