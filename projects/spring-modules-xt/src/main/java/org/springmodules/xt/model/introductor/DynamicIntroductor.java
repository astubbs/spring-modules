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

/**
 * <p>
 * Dynamically introduces additional interfaces to target objects.<br>
 * This will create a proxy object  which will implement the new interfaces and also a set of target interfaces if you specify them.<br>
 * If you do not specify any target interface, the proxy object will automatically subclass the target object class.
 * </p>
 * <p>
 * <b>Please note</b> that the introductor introduces all interfaces with all relative subinterfaces.
 * </p>
 * 
 * @author Sergio Bossa
 */
public interface DynamicIntroductor {
   
    /**
     * Make the target object implementing the given interfaces.<br>
     * Here, you do not specify any target interface, so the introduced (proxy) object will subclass the target object class. 
     * @param target The target object.
     * @param introducedInterfaces The interfaces to introduce.
     * @return A proxy object implementing the given interfaces.
     */
    public Object introduceInterfaces(Object target, Class[] introducedInterfaces);
    
    /**
     * Make the target object implementing the given interfaces.<br>
     * Here you specify a set of target interfaces which will be implemented by the introduced (proxy) object.
     * @param target The target object.
     * @param introducedInterfaces The interfaces to introduce.
     * @param targetInterfaces The interfaces of the target object.
     * @return A proxy object implementing the given interfaces.
     */
    public Object introduceInterfaces(Object target, Class[] introducedInterfaces, Class[] targetInterfaces);
    
    /**
     * Get the original target object.
     * @param proxy The object proxying the original one and implementing additional, introduced, interfaces.
     * @return The original target object.
     */
    public Object getTarget(Object proxy);
}
