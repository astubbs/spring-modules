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
import org.springmodules.validation.util.condition.Conditions;
import org.w3c.dom.Element;

/**
 * An {@link AbstractValidationRuleElementHandler} that can handle an element that represents a length range validation rule,
 * min length validation rule, or max length vaidation rule.
 *
 * @author Uri Boness
 */
public class LengthRuleElementHandler extends AbstractValidationRuleElementHandler
    implements DefaultXmBeanValidationConfigurationlLoaderConstants {

    /**
     * The default error code for a length range validation rule.
     */
    public static final String DEFAULT_LENGTH_ERROR_CODE = "length";

    /**
     * The default error code for a min length validation rule.
     */
    public static final String DEFAULT_MIN_LENGTH_ERROR_CODE = "min.length";

    /**
     * The default error code for a max length validation rule.
     */
    public static final String DEFAULT_MAX_LENGTH_ERROR_CODE = "max.length";


    private static final String ELEMENT_NAME = "length";
    private static final String MIN_ATTR = "min";
    private static final String MAX_ATTR = "max";

    /**
     * Constructs a new LengthRuleElementHandler.
     */
    public LengthRuleElementHandler() {
        super(ELEMENT_NAME, DEFAULT_NAMESPACE_URL);
    }

    /**
     * Returns one of the followings:
     * <ul>
     *  <li>If the element represents a length range validation rule, then {@link #DEFAULT_LENGTH_ERROR_CODE}</li>
     *  <li>If the element represents a min length validation rule, then {@link #DEFAULT_MIN_LENGTH_ERROR_CODE}</li>
     *  <li>If the element represents a max length validation rule, then {@link #DEFAULT_MAX_LENGTH_ERROR_CODE}</li>
     * </ul>
     *
     * @see AbstractValidationRuleElementHandler#getDefaultErrorCode(org.w3c.dom.Element)
     */
    protected String getDefaultErrorCode(Element element) {
        boolean hasMin = element.hasAttribute(MIN_ATTR);
        boolean hasMax = element.hasAttribute(MAX_ATTR);

        if (hasMin && hasMax) {
            return DEFAULT_LENGTH_ERROR_CODE;
        }

        if (hasMin) {
            return DEFAULT_MIN_LENGTH_ERROR_CODE;
        }

        return DEFAULT_MAX_LENGTH_ERROR_CODE;

    }

    /**
     * Parses the given element and returns one of the followings:
     * <ul>
     *  <li>If the element represents a length range validation rule, then a
     *      {@link org.springmodules.validation.util.condition.string.LengthRangeStringCondition}</li>
     *  <li>If the element represents a min length validation rule, then a
     *      {@link org.springmodules.validation.util.condition.string.MinLengthStringCondition}</li>
     *  <li>If the element represents a max length validation rule, then a
     *      {@link org.springmodules.validation.util.condition.string.MaxLengthStringCondition}</li>
     * </ul>
     *
     * @see AbstractValidationRuleElementHandler#extractCondition(org.w3c.dom.Element)
     * @see org.springmodules.validation.util.condition.string.LengthRangeStringCondition
     * @see org.springmodules.validation.util.condition.string.MinLengthStringCondition
     * @see org.springmodules.validation.util.condition.string.MaxLengthStringCondition
     */
    protected Condition extractCondition(Element element) {
        String minText = element.getAttribute(MIN_ATTR);
        String maxText = element.getAttribute(MAX_ATTR);

        Integer min = (StringUtils.hasText(minText)) ? new Integer(minText) : null;
        Integer max = (StringUtils.hasText(maxText)) ? new Integer(maxText) : null;

        if (min != null && max != null) {
            return Conditions.lengthBetween(min.intValue(), max.intValue());
        }

        if (min != null) {
            return Conditions.minLength(min.intValue());
        }

        if (max != null) {
            return Conditions.maxLength(max.intValue());
        }

        throw new XmlConditionConfigurationException("Element '" + ELEMENT_NAME +
            "' must have either 'min' attribute, 'max' attribute, or both");
    }

}
