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
import org.w3c.dom.Element;

/**
 * An {@link AbstractClassValidationElementHandler} that can handle class elements that represent valang validation rules.
 * This handler creates a valang conditoin out of a &lt;valang&gt; element.
 *
 * @author Uri Boness
 */
public class ExpressionClassValidationElementHandler extends AbstractClassValidationElementHandler {

    /**
     * The default error code for the created valang validation rule.
     */
    public static final String DEFAULT_ERROR_CODE = EXPRESSION_ERROR_CODE;

    private static final String ELEMENT_NAME = "expression";
    private static final String CONDITION_ATTR = "condition";

    /**
     * Constructs a new ExpressionClassValidationElementHandler.
     */
    public ExpressionClassValidationElementHandler(String namespaceUri) {
        super(ExpressionClassValidationElementHandler.ELEMENT_NAME, namespaceUri);
    }

    /**
     * Returns {@link #DEFAULT_ERROR_CODE}.
     *
     * @see AbstractClassValidationElementHandler#getDefaultErrorCode(org.w3c.dom.Element)
     */
    protected String getDefaultErrorCode(Element element) {
        return ExpressionClassValidationElementHandler.DEFAULT_ERROR_CODE;
    }

    /**
     * Creates a valang condition from the given validation rule element.
     *
     * @param element The element that represents the valang validation rule.
     * @return The created valang condition
     * @see AbstractClassValidationElementHandler#extractCondition(org.w3c.dom.Element)
     */
    protected Condition extractCondition(Element element) {
        String expression = element.getAttribute(CONDITION_ATTR);
        if (!StringUtils.hasText(expression)) {
            throw new XmlConfigurationException("Element '" + ELEMENT_NAME + "' must have a '" + CONDITION_ATTR + "' attribute");
        }
        return getConditionExpressionParser().parse(expression);
    }

}
