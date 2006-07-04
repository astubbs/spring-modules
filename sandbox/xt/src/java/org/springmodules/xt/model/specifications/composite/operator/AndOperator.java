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

package org.springmodules.xt.model.specifications.composite.operator;

import org.springmodules.xt.model.notification.Notification;
import org.springmodules.xt.model.specifications.Specification;

/**
 * Implementation for the <code>AND</code> operator.
 *
 * @author Sergio Bossa
 */
public class AndOperator implements BinaryOperator {
    
    public boolean evaluate(Specification a, Specification b, Object o) {
        return a.evaluate(o) && b.evaluate(o);
    }
    
    public boolean evaluate(Specification a, boolean b, Object o) {
        return a.evaluate(o) && b;
    }
    
    public boolean evaluate(Specification a, Specification b, Object o, Notification notification) {
        return a.evaluate(o, notification) && b.evaluate(o, notification);
    }
    
    public boolean evaluate(Specification a, boolean b, Object o, Notification notification) {
        return a.evaluate(o, notification) && b;
    }
}
