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

package org.springmodules.validation.bean.conf.loader.xml.handler;

import org.springmodules.validation.util.condition.Condition;
import org.springmodules.validation.util.condition.Conditions;
import org.w3c.dom.Element;

/**
 * An {@link AbstractPropertyValidationElementHandler} implementation that can handle and element that represents a "not blank"
 * validation rule - a rule that validates that a string is not blank.
 *
 * @author Uri Boness
 */
public class NotBlankRuleElementHandler extends AbstractPropertyValidationElementHandler {

    /**
     * The default error code of the created validation rule.
     */
    public static final String DEFAULT_ERROR_CODE = NOT_BLANK_ERROR_CODE;

    private static final String ELEMENT_NAME = "not-blank";

    /**
     * Constructs a new NotBlankRuleElementHandler.
     */
    public NotBlankRuleElementHandler(String namespaceUri) {
        super(ELEMENT_NAME, namespaceUri);
    }

    /**
     * Returns {@link #DEFAULT_ERROR_CODE}.
     *
     * @see AbstractPropertyValidationElementHandler#getDefaultErrorCode(org.w3c.dom.Element)
     */
    protected String getDefaultErrorCode(Element element) {
        return DEFAULT_ERROR_CODE;
    }

    /**
     * Creates and returns a new "not blank" condition - a condition that checks if a given string is blank.
     *
     * @see AbstractPropertyValidationElementHandler#extractCondition(org.w3c.dom.Element)
     * @see org.springmodules.validation.util.condition.Conditions#notBlank()
     */
    protected Condition extractCondition(Element element) {
        return Conditions.notBlank();
    }

}
