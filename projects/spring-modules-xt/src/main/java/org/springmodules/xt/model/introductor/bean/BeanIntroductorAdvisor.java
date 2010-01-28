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

import org.springmodules.xt.model.introductor.SimpleIntroductionInfo;
import org.springframework.aop.support.DefaultIntroductionAdvisor;

/**
 * Spring AOP Advisor for dynamically introducing JavaBeans style interfaces using the 
 * {@link BeanIntroductorInterceptor}.
 * 
 * @author Sergio Bossa
 */
public class BeanIntroductorAdvisor extends DefaultIntroductionAdvisor {
    
    /**
     * Constructor.
     * @param introducedInterfaces The interfaces to introduce.
     */
    public BeanIntroductorAdvisor(Class[] introducedInterfaces) {
        super(new BeanIntroductorInterceptor(introducedInterfaces), new SimpleIntroductionInfo(introducedInterfaces));
    }
}
