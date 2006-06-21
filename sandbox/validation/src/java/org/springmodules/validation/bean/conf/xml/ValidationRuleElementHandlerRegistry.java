/*
 * Copyright 2004-2005 the original author or authors.
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

package org.springmodules.validation.bean.conf.xml;

import org.w3c.dom.Element;

/**
 * A registry of {@link ValidationRuleElementHandler}'s.
 *
 * @author Uri Boness
 */
public interface ValidationRuleElementHandlerRegistry {

    /**
     * Registers the given validation rule element handler with this registry.
     *
     * @param handler The handler to register.
     */
    void registerHandler(ValidationRuleElementHandler handler);

    /**
     * Returns the validation rule element handler that can handle the given element.
     *
     * @param element The element to be handled.
     * @return The validation rule element handler that can handle the given element.
     */
    ValidationRuleElementHandler findHandler(Element element);

}
