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

import java.util.HashSet;
import java.util.Set;
import org.springframework.aop.IntroductionInfo;

/**
 * Simple container of introduced interfaces.
 *
 * @author Sergio Bossa
 */
public class SimpleIntroductionInfo implements IntroductionInfo{
    
    private Set<Class> interfaces = new HashSet();
    
    public SimpleIntroductionInfo(Class[] introducedInterfaces) {
        for(Class aClass : introducedInterfaces) {
            if (aClass.isInterface()) {
                this.interfaces.add(aClass);
            }
        }
    }

    public Class[] getInterfaces() {
        return this.interfaces.toArray(new Class[this.interfaces.size()]);
    }
}
