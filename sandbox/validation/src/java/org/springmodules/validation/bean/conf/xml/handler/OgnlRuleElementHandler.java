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
package org.springmodules.validation.bean.conf.xml.handler;

import org.springframework.util.StringUtils;
import org.springmodules.validation.bean.conf.xml.DefaultXmBeanValidationConfigurationlLoaderConstants;
import org.springmodules.validation.util.condition.Condition;
import org.springmodules.validation.util.condition.parser.ognl.OgnlCondition;
import org.w3c.dom.Element;

/**
 * An {@link AbstractValidationRuleElementHandler} that can handle elements that represent OGNL validation rules.
 * This handler creates an OGNL conditoin out of a &lt;ognl&gt; element.
 *
 * @author Uri Boness
 */
public class OgnlRuleElementHandler extends AbstractValidationRuleElementHandler
    implements DefaultXmBeanValidationConfigurationlLoaderConstants {

    /**
     * The default error code for the created ognl validation rule.
     */
    public static final String DEFAULT_ERROR_CODE = "ognl";


    private static final String ELEMENT_NAME = "ognl";
    private static final String EXPRESSION_ATTR = "expression";

    /**
     * Constructs a new OgnlRuleElementHandler.
     */
    public OgnlRuleElementHandler() {
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
     * Creates an OGNL condition from the given validation rule element.
     *
     * @param element The element that represents the OGNL validation rule.
     * @return The created OGNL condition
     * @see AbstractValidationRuleElementHandler#extractCondition(org.w3c.dom.Element)
     */
    protected Condition extractCondition(Element element) {
        String expression = element.getAttribute(OgnlRuleElementHandler.EXPRESSION_ATTR);
        if (!StringUtils.hasText(expression)) {
            throw new XmlConditionConfigurationException("Element '" + ELEMENT_NAME + "' must have an 'expression' attribute");
        }
        return new OgnlCondition(expression);
    }

    /**
     * This validation rule handler should always be associated with the global context.
     *
     * @see org.springmodules.validation.bean.conf.xml.ValidationRuleElementHandler#isAlwaysGlobal() 
     */
    public boolean isAlwaysGlobal() {
        return true;
    }

}
