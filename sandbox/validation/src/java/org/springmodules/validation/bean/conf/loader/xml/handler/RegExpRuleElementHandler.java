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

import org.springframework.util.StringUtils;
import org.springmodules.validation.bean.conf.loader.xml.XmlConfigurationException;
import org.springmodules.validation.util.condition.Condition;
import org.springmodules.validation.util.condition.Conditions;
import org.w3c.dom.Element;

/**
 * An {@link AbstractPropertyValidationElementHandler} implementation that knows how to handle elements that represent
 * regular expression validation rules.
 *
 * @author Uri Boness
 */
public class RegExpRuleElementHandler extends AbstractPropertyValidationElementHandler {

    /**
     * The default error code for the parsed regular expression validation rule.
     */
    public static final String DEFAULT_ERROR_CODE = REG_EXP_ERROR_CODE;

    private static final String ELEMENT_NAME = "regexp";
    private static final String EXPRESSION_ATTR = "expression";

    /**
     * Constructs a new RegExpRuleElementHandler.
     */
    public RegExpRuleElementHandler(String namespaceUri) {
        super(ELEMENT_NAME, namespaceUri);
    }

    /**
     * Returns {@link #DEFAULT_ERROR_CODE}
     *
     * @see AbstractPropertyValidationElementHandler#getDefaultErrorCode(org.w3c.dom.Element)
     */
    protected String getDefaultErrorCode(Element element) {
        return DEFAULT_ERROR_CODE;
    }

    /**
     * Parses the given element and creates a {@link org.springmodules.validation.util.condition.string.RegExpStringCondition}.
     *
     * @param element The parsed element.
     * @return The created {@link org.springmodules.validation.util.condition.string.RegExpStringCondition}.
     * @see AbstractPropertyValidationElementHandler#extractCondition(org.w3c.dom.Element)
     */
    protected Condition extractCondition(Element element) {
        String expression = element.getAttribute(EXPRESSION_ATTR);
        if (!StringUtils.hasText(expression)) {
            throw new XmlConfigurationException("Element '" + ELEMENT_NAME + "' must have an 'expression' attribute");
        }
        return Conditions.regexp(expression);
    }

}
