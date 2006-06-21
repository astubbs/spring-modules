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
 * An {@link AbstractValidationRuleElementHandler} implementation that can handle an element that represent a range
 * validation rule, min validation rule, or max validation rule.
 *
 * @author Uri Boness
 */
public class RangeRuleElementHandler extends AbstractValidationRuleElementHandler
    implements DefaultXmBeanValidationConfigurationlLoaderConstants {

    /**
     * The defult error code for the range validation rule.
     */
    public static final String DEFAULT_RANGE_ERROR_CODE = "range";

    /**
     * The defult error code for the min validation rule.
     */
    public static final String DEFAULT_MIN_ERROR_CODE = "range";

    /**
     * The defult error code for the max validation rule.
     */
    public static final String DEFAULT_MAX_ERROR_CODE = "range";

    private static final String ELEMENT_NAME = "range";
    private static final String MIN_ATTR = "min";
    private static final String MAX_ATTR = "max";

    /**
     * Constructs a new RangeRuleElementHandler.
     */
    public RangeRuleElementHandler() {
        super(ELEMENT_NAME, DEFAULT_NAMESPACE_URL);
    }

    /**
     * Returns one of the followings:
     * <ul>
     *  <li>If the element represents a range validation rule, then {@link #DEFAULT_RANGE_ERROR_CODE}</li>
     *  <li>If the element represents a min validation rule, then {@link #DEFAULT_MIN_ERROR_CODE}</li>
     *  <li>If the element represents a max validation rule, then {@link #DEFAULT_MAX_ERROR_CODE}</li>
     * </ul>
     *
     * @see AbstractValidationRuleElementHandler#getDefaultErrorCode(org.w3c.dom.Element)
     */
    protected String getDefaultErrorCode(Element element) {
        boolean hasMin = element.hasAttribute(MIN_ATTR);
        boolean hasMax = element.hasAttribute(MAX_ATTR);

        if (hasMin && hasMax) {
            return DEFAULT_RANGE_ERROR_CODE;
        }

        if (hasMin) {
            return DEFAULT_MIN_ERROR_CODE;
        }

        return DEFAULT_MAX_ERROR_CODE;
    }

    /**
     * Returns one of the followings:
     * <ul>
     *  <li>If the element has both <code>min</code> and <code>max</code> attributes, then
     *     {@link org.springmodules.validation.util.condition.range.BetweenIncludingCondition}</li>
     *  <li>If the element only has the <code>min</code> attribute, then
     *     {@link org.springmodules.validation.util.condition.range.GteCondition}</li>
     *  <li>If the element only has the <code>max</code> attribute, then
     *     {@link org.springmodules.validation.util.condition.range.LteCondition}</li>
     * </ul>
     *
     * @see AbstractValidationRuleElementHandler#extractCondition(org.w3c.dom.Element)
     */
    protected Condition extractCondition(Element element) {
        String minText = element.getAttribute(MIN_ATTR);
        String maxText = element.getAttribute(MAX_ATTR);

        Integer min = (StringUtils.hasText(minText)) ? new Integer(minText) : null;
        Integer max = (StringUtils.hasText(maxText)) ? new Integer(maxText) : null;

        if (min != null && max != null) {
            return Conditions.isBetweenIncluding(min, max);
        }

        if (min != null) {
            return Conditions.isGte(min);
        }

        if (max != null) {
            return Conditions.isLte(max);
        }

        throw new XmlConditionConfigurationException("Element '" + ELEMENT_NAME +
            "' must have either 'min' attribute, 'max' attribute, or both");
    }

}
