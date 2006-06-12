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
package org.springmodules.validation.bean.conf.xml.parser;

import org.springmodules.validation.util.condition.Condition;
import org.springmodules.validation.util.condition.parser.valang.ValangCondition;
import org.springmodules.validation.bean.conf.xml.DefaultXmBeanValidationConfigurationlLoaderConstants;
import org.w3c.dom.Element;
import org.springframework.util.StringUtils;

/**
 * An {@link AbstractValidationRuleElementHandler} that can handle elements that represent valang validation rules.
 * This handler creates a valang conditoin out of a &lt;valang&gt; element.
 *
 * @author Uri Boness
 */
public class ValangRuleElementHandler extends AbstractValidationRuleElementHandler implements DefaultXmBeanValidationConfigurationlLoaderConstants {

    /**
     * The default error code for the created valang validation rule.
     */
    public static final String DEFAULT_ERROR_CODE = "valang";


    private static final String ELEMENT_NAME = "valang";
    private static final String EXPRESSION_ATTR = "expression";

    /**
     * Constructs a new ValangRuleElementHandler.
     */
    public ValangRuleElementHandler() {
        super(ELEMENT_NAME, DEFAULT_NAMESPACE_URL);
    }

    /**
     * Returns {@link #DEFAULT_ERROR_CODE}.
     *
     * @see AbstractValidationRuleElementHandler#getDefaultErrorCode(org.w3c.dom.Element)
     */
    protected String getDefaultErrorCode(Element element) {
        return DEFAULT_ERROR_CODE;
    }

    /**
     * Creates a valang condition from the given validation rule element.
     *
     * @param element The element that represents the valang validation rule.
     * @return The created valang condition
     * @see AbstractValidationRuleElementHandler#extractCondition(org.w3c.dom.Element)
     */
    protected Condition extractCondition(Element element) {
        String expression = element.getAttribute(EXPRESSION_ATTR);
        if (!StringUtils.hasText(expression)) {
            throw new XmlConditionConfigurationException("Element '" + ELEMENT_NAME + "' must have an 'expression' attribute");
        }
        return new ValangCondition(expression);
    }

}
