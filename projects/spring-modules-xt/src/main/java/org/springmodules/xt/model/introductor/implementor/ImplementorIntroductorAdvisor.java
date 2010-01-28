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

import org.springmodules.xt.model.introductor.SimpleIntroductionInfo;
import org.springframework.aop.support.DefaultIntroductionAdvisor;

/**
 * Spring AOP Advisor for dynamically introducing and implementing interfaces by using the 
 * {@link ImplementorIntroductorInterceptor}.
 * 
 * @author Sergio Bossa
 */
public class ImplementorIntroductorAdvisor extends DefaultIntroductionAdvisor {
    
    /**
     * Constructor.
     * @param introducedInterfaces The interfaces to introduce, carrying the new behaviour.
     * @param implementor The implementor object, specifying the implementation of the introduced interfaces.
     * Please note that it must implement the given interfaces.
     */
    public ImplementorIntroductorAdvisor(Class[] introducedInterfaces, Object implementor) {
        super(new ImplementorIntroductorInterceptor(introducedInterfaces, implementor), new SimpleIntroductionInfo(introducedInterfaces));
    }
}
