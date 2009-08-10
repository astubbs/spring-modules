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

import org.springmodules.xt.model.introductor.AbstractDynamicIntroductor;
import org.springframework.aop.framework.ProxyFactory;

/**
 * <p>
 * Introductor implementation for introducing JavaBeans style interfaces via {@link BeanIntroductorAdvisor}
 * and {@link BeanIntroductorInterceptor}.<br>
 * This introductor introduces additional interfaces into a target object, and creates a proxy object that behaves
 * like the following:
 * <ul>
 *      <li>Every call to a getter/setter method declared into the introduced interface is delegated to the target object
 *          if this declares and implements a public method with equal signature, otherwise it is automatically implemented
 *          by the proxy.</li>
 *      <li>Every call to a non getter/setter method declared into the introduced interface throws an exception.</li>
 *      <li>Every call to a method not declared into the introduced interface is directly delegated
 *          to the target object </li>
 * </ul>
 * Moreover, it is possible to change the introductor behaviour by annotating the introduced interfaces with
 * annotations contained in the <i>org.springmodules.xt.model.introductor.annotation</i> package; it is possible to
 * use the following annotations:
 * <ul>
 * <li>{@link org.springmodules.xt.model.introductor.annotation.MapToTargetField}</li>
 * <li>{@link org.springmodules.xt.model.introductor.annotation.OverrideTarget}</li>
 * </ul>
 * </p>
 * <p>
 * <b>Restrictions</b>: The only restriction is that introduced methods cannot return primitive values; so, you
 * have to use wrapper objects instead when you need to return a primitive type.
 * </p>
 * <p>This class is <b>thread-safe</b>.</p>
 *
 * @author Sergio Bossa
 */
public class DynamicBeanIntroductor extends AbstractDynamicIntroductor {
    
    /**
     * @see AbstractDynamicIntroductor#introduceInterfaces(Object , Class[] )
     */
    public Object introduceInterfaces(Object target, Class[] introducedInterfaces) {
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
        ProxyFactory proxyFactory = new ProxyFactory();
        
        proxyFactory.addAdvisor(new BeanIntroductorAdvisor(introducedInterfaces));
        proxyFactory.setTarget(target);
        proxyFactory.setInterfaces(this.merge(introducedInterfaces, targetInterfaces));
        
        return proxyFactory.getProxy();
    }
}
