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

package org.springmodules.xt.model.introductor.bean;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.springmodules.xt.model.introductor.support.IllegalReturnTypeException;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;
import org.springframework.aop.IntroductionInterceptor;
import org.springframework.beans.BeanUtils;

/**
 * Spring AOP Introduction Interceptor for dynamically constructing JavaBeans style classes with additional setter and getter methods.<br>
 * This causes a target object to implement a set of introduced interfaces.<br>
 * Methods on introduced interfaces will be automatically implemented by this interceptor.<br>
 * Methods not belonging to introduced interfaces will be executed on the target object.<br>
 * It is important to specify <b>all introduced interfaces</b>, even those that are superinterfaces of other ones.
 * 
 * @author Sergio Bossa
 */
public class BeanIntroductorInterceptor implements IntroductionInterceptor {
    
    private static final Logger logger = Logger.getLogger(BeanIntroductorInterceptor.class);
    
    private Set<Class> introducedInterfaces = new HashSet();
    private Map<String, Object> fields = new ConcurrentHashMap();

    /**
     * @param introducedInterfaces The interfaces to introduce.
     */
    public BeanIntroductorInterceptor(Class[] introducedInterfaces) {
        Collections.addAll(this.introducedInterfaces, introducedInterfaces);
    }
    
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Method method = methodInvocation.getMethod();
        Object result = null;
        if (this.isIntroduced(method)) {
            logger.debug("Introducing method: " + method.toString());
            try {
                if (method.getName().startsWith("get")) {
                    result = this.fields.get(method.getName().substring(3));
                    if (result == null) {
                        if (method.getReturnType().isPrimitive()) {
                            throw new IllegalReturnTypeException("Return types of your introduced interfaces cannot be primitives.");
                        }
                    }
                }
                else if (method.getName().startsWith("set")) {
                    String key = method.getName().substring(3);
                    Object value = methodInvocation.getArguments()[0];
                    // ConcurrentHashMap doesn't support null values: so, if the value is not null, proceed with setting:
                    if (value != null)
                        this.fields.put(key, value);
                    // Else, remove it (this equals setting it null):
                    else
                        this.fields.remove(key); 
                }
                else {
                    throw new UnsupportedOperationException("You can only invoke setter and getter methods.");
                }
            }
            catch(Exception ex) {
                logger.warn("Something wrong happened calling: " + method.toString());
                logger.warn("Exception message: " + ex.getMessage());
                throw ex;
            }
        }
        else {
            result =  methodInvocation.proceed();
        }
        
        return result;
    }

    public boolean implementsInterface(Class aClass) {
        return this.isIntroduced(aClass);
    }
    
    protected final boolean isIntroduced(Method aMethod) {
        return this.isIntroduced(aMethod.getDeclaringClass());
    }
    
    protected final boolean isIntroduced(Class aClass) {
        if (aClass.isInterface()) {
            for (Class introduced : this.introducedInterfaces) {
                if (aClass.equals(introduced)) {
                    return true;
                }
            }
        }
        return false;
    }
}
