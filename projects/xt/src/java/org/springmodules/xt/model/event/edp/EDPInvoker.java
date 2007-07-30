/*
 * Copyright 2007 the original author or authors.
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

package org.springmodules.xt.model.event.edp;

import org.springframework.context.ApplicationListener;

/**
 * {@link org.springframework.context.ApplicationListener} extended interface capable of invoking
 * <b>Event Driven POJOs</b> (<b>EDP</b>): plain old Java objects with methods that have to be invoked upon
 * certain events.
 * <br>
 * EDPInvoker implementations will have to define:
 * <ul>
 * <li>The invoked EDP bean (see {@link #getInvokedBean()}), often defined in the Spring application context.</li>
 * <li>The name of the method to invoke on the bean defined above (see {@link #getInvokedMethodName()});
 * if the bean has more overloaded methods with the same name above, all of these methods will be invoked.</li>
 * </ul>
 * Implementors will have to define the actual invoking strategy, by specifying what {@link org.springframework.context.ApplicationEvent}s
 * cause the bean methods to be invoked, and how to invoke them.
 * 
 * @author Sergio Bossa
 */
public interface EDPInvoker extends ApplicationListener {
    
    /**
     * Get the bean to invoke.
     * @return The bean to invoke
     */
    public Object getInvokedBean();
    
    /**
     * Get the name of the bean method to invoke.
     * @return The bean method name.
     */
     public String getInvokedMethodName();
}
