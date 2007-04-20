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

import org.springmodules.xt.model.introductor.AbstractDynamicIntroductor;
import org.springframework.aop.framework.ProxyFactory;

/**
 * <p>
 * Introductor implementation for introducing new interfaces with new implementations
 * to existent objects, via {@link ImplementorIntroductorAdvisor} and {@link ImplementorIntroductorInterceptor}.<br>
 * This introductor introduces additional interfaces into a target object by creating a proxy object that behaves
 * like the following:
 * <ul>
 *      <li>Every call to a method declared into an introduced interface
 *          will be delegated to the <b>implementor</b> object if it implements the introduced interface; otherwise, an
 *          exception occurs.</li>
 *      <li>Every call to a method not declared into the introduced interface is as always executed
 *          by the target object.</li>
 * </ul>
 * Please nothe that if an introduced method is declared also into the target object interface, it will be anyhow delegated
 * to the <b>implementor</b> object
 * </p>
 * <p>This introductor doesn't support any annotation contained in the <i>org.springmodules.xt.model.introductor.annotation</i> package</p>
 * <p>This class is <b>thread-safe</b>, so you can use it for concurrently introducing multiple targets.</p>
 *
 * @author Sergio Bossa
 */
public class DynamicImplementorIntroductor extends AbstractDynamicIntroductor {
    
    private Object implementor;
    
    /**
     * Create the introductor that uses the given object as implementor.<br>
     * All introduced interfaces must be implemented by the implementor object.
     * @param implementor The object to use as implementor.
     */
    public DynamicImplementorIntroductor(Object implementor) {
        this.implementor = implementor;
    }
    
    /**
     * @see AbstractDynamicIntroductor#introduceInterfaces(Object , Class[] )
     */
    public Object introduceInterfaces(Object target, Class[] introducedInterfaces) {
        ProxyFactory proxyFactory = new ProxyFactory();
        
        proxyFactory.setProxyTargetClass(true);
        proxyFactory.addAdvisor(new ImplementorIntroductorAdvisor(introducedInterfaces, this.implementor));
        proxyFactory.setTarget(target);
        
        return proxyFactory.getProxy();
    }
    
    /**
     * @see AbstractDynamicIntroductor#introduceInterfaces(Object , Class[] , Class[] )
     */
    public Object introduceInterfaces(Object target, Class[] introducedInterfaces, Class[] targetInterfaces) {
        ProxyFactory proxyFactory = new ProxyFactory();
        
        proxyFactory.addAdvisor(new ImplementorIntroductorAdvisor(introducedInterfaces, this.implementor));
        proxyFactory.setTarget(target);
        proxyFactory.setInterfaces(this.merge(introducedInterfaces, targetInterfaces));
        
        return proxyFactory.getProxy();
    }
}
