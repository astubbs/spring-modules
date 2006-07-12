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

import org.springmodules.xt.model.introductor.AbstractDynamicIntroductor;
import org.springframework.aop.framework.ProxyFactory;

/**
 * Introductor implementation for introducing JavaBeans style interfaces via {@link BeanIntroductorAdvisor}.
 * 
 * @author Sergio Bossa
 */
public class DynamicBeanIntroductor extends AbstractDynamicIntroductor {
    
    /**
     * @see AbstractDynamicIntroductor#introduceInterfaces(Object , Class[] )
     */
    public Object introduceInterfaces(Object target, Class[] introducedInterfaces) {
        // FIXME: Do not create a new instance every time ... ?
        ProxyFactory proxyFactory = new ProxyFactory();
        
        proxyFactory.setProxyTargetClass(true);
        proxyFactory.addAdvisor(new BeanIntroductorAdvisor(introducedInterfaces));
        proxyFactory.setTarget(target);

        return proxyFactory.getProxy();
    }
    
    /**
     * @see AbstractDynamicIntroductor#introduceInterfaces(Object , Class[] , Class[] )
     */
    public Object introduceInterfaces(Object target, Class[] introducedInterfaces, Class[] targetInterfaces) {
        // FIXME: Do not create a new instance every time ... ?
        ProxyFactory proxyFactory = new ProxyFactory();
        
        proxyFactory.addAdvisor(new BeanIntroductorAdvisor(introducedInterfaces));
        proxyFactory.setTarget(target);
        proxyFactory.setInterfaces(this.merge(introducedInterfaces, targetInterfaces));
        
        return proxyFactory.getProxy();
    }
    
    private Class[] merge(Class[] a1, Class[] a2) {
        Class[] result = new Class[a1.length + a2.length];
        int i = 0;
        
        for (i = 0; i < a1.length; i++) {
            result[i] = a1[i];
        }
        for (i = 0; i < a2.length; i++) {
            result[i + a1.length] = a2[i];
        }
        
        return result;
    }
}
