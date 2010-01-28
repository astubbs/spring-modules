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

import org.springframework.aop.framework.Advised;

/**
 * Abstract implementation of {@link DynamicIntroductor}.<br>
 * Subclasses must specify how to introduce interfaces.
 * 
 * @author Sergio Bossa
 */
public abstract class AbstractDynamicIntroductor implements DynamicIntroductor {

    /**
     * @see DynamicIntroductor#introduceInterfaces(Object , Class[] )
     */
    public abstract Object introduceInterfaces(Object target, Class[] introducedInterfaces);
    
    /**
     * @see DynamicIntroductor#introduceInterfaces(Object , Class[] , Class[] )
     */
    public abstract Object introduceInterfaces(Object target, Class[] introducedInterfaces, Class[] targetInterfaces);
    
    /**
     * @see DynamicIntroductor#getTarget(Object )
     */
    public Object getTarget(Object proxy) {
        return this.internalGetTarget(proxy);
    }
    
    protected Class[] merge(Class[] a1, Class[] a2) {
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
    
    private Object internalGetTarget(Object proxy) {
        Object target = null;
        
        try {
            if (proxy instanceof Advised) {
                Advised advised = (Advised) proxy;
                target = advised.getTargetSource().getTarget();
            }
            else {
                throw new IllegalArgumentException("Not a proxy object.");
            }
        }
        catch(Exception ex) {
            throw new UnsupportedOperationException("Cannot obtain the target object from the given proxy.");
        }
        
        if (target == null) {
            throw new UnsupportedOperationException("Cannot obtain the target object from the given proxy.");
        }
        
        return target;
    }
}
