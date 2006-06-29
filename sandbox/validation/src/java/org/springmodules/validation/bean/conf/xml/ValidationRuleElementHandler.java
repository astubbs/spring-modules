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

import org.springmodules.validation.bean.rule.ValidationRule;
import org.w3c.dom.Element;

/**
 * An handler that can create a validation rule from a given {@link Element}.
 *
 * @author Uri Boness
 */
public interface ValidationRuleElementHandler {

    /**
     * Determines whether this handler can handle the given element.
     *
     * @param element The element to be handled.
     * @return <code>true</code> if this handler can handle the given element, <code>false</code> otherwise.
     */
    boolean supports(Element element);

    /**
     * Handles the given element and creates a {@link ValidationRule} based on it.
     *
     * @param element The element to be handled.
     * @return The created validation rule.
     */
    ValidationRule handle(Element element);

    /**
     * Returns whether the validation rule handled by this hanlder is always a global rule.
     *
     * @return <code>true</code> if the validation rule handled by this handler is always global, <code>false</code> otherwise.
     * @see org.springmodules.validation.bean.conf.xml.handler.ValangRuleElementHandler
     */
    boolean isAlwaysGlobal();

}
