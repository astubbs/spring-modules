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
 * An {@link AbstractValidationRuleElementHandler} that parses a size validation rules. This handler creates a
 * {@link org.springmodules.validation.util.condition.collection.SizeRangeCollectionCondition},
 * {@link org.springmodules.validation.util.condition.collection.MinSizeCollectionCondition}, or
 * {@link org.springmodules.validation.util.condition.collection.MaxSizeCollectionCondition} from the &lt;size&gt;
 * element.
 *
 * @author Uri Boness
 */
public class SizeRuleElementHandler extends AbstractValidationRuleElementHandler
    implements DefaultXmBeanValidationConfigurationlLoaderConstants {

    /**
     * The default error code for the created size range validation rule.
     */
    public static final String DEFAULT_SIZE_RANGE_ERROR_CODE = "size";

    /**
     * The default error code for the created minimum size validation rule.
     */
    public static final String DEFAULT_MIN_SIZE_ERROR_CODE = "min.size";

    /**
     * The default error code for the created maximum size validation rule.
     */
    public static final String DEFAULT_MAX_SIZE_ERROR_CODE = "max.size";

    private static final String ELEMENT_NAME = "size";
    private static final String MIN_ATTR = "min";
    private static final String MAX_ATTR = "max";

    /**
     * Constructs a new SizeRuleElementHandler.
     */
    public SizeRuleElementHandler() {
        super(ELEMENT_NAME, DEFAULT_NAMESPACE_URL);
    }

    /**
     * Returns one of the followings:
     * <ul>
     *  <li>If the element has both 'min' and 'max' attributes, then {@link #DEFAULT_SIZE_RANGE_ERROR_CODE}</li>
     *  <li>If the element only has the 'min' attribute, then {@link #DEFAULT_MIN_SIZE_ERROR_CODE}</li>
     *  <li>If the element only has the 'max' attribute, then {@link #DEFAULT_MAX_SIZE_ERROR_CODE}</li>
     * </ul>
     *
     * @see AbstractValidationRuleElementHandler#getDefaultErrorCode(org.w3c.dom.Element)
     */
    protected String getDefaultErrorCode(Element element) {
        boolean hasMin = element.hasAttribute(MIN_ATTR);
        boolean hasMax = element.hasAttribute(MAX_ATTR);

        if (hasMin && hasMax) {
            return DEFAULT_SIZE_RANGE_ERROR_CODE;
        }

        if (hasMin) {
            return DEFAULT_MIN_SIZE_ERROR_CODE;
        }

        return DEFAULT_MAX_SIZE_ERROR_CODE;
    }

    /**
     * handles the given &lt;size&gt; element and creates the proper condition according to the following rules;
     * <ol>
     *  <li>If both <code>min</code> and <code>max</code> attributes are specified, then a
     * {@link org.springmodules.validation.util.condition.collection.SizeRangeCollectionCondition} is created</li>
     *  <li>If only a <code>min</code> attribute is specified, then a
     * {@link org.springmodules.validation.util.condition.collection.MinSizeCollectionCondition} is created</li>
     *  <li>If only a <code>max</code> attribute is specified, then a
     * {@link org.springmodules.validation.util.condition.collection.MaxSizeCollectionCondition} is created</li>
     * </ol>
     *
     * @param element The parsed element.
     * @return The created condition.
     * @see AbstractValidationRuleElementHandler#extractCondition(org.w3c.dom.Element)
     */
    protected Condition extractCondition(Element element) {
        String minText = element.getAttribute(MIN_ATTR);
        String maxText = element.getAttribute(MAX_ATTR);

        Integer min = (StringUtils.hasText(minText)) ? new Integer(minText) : null;
        Integer max = (StringUtils.hasText(maxText)) ? new Integer(maxText) : null;

        if (min != null && max != null) {
            return Conditions.sizeRange(min.intValue(), max.intValue());
        }

        if (min != null) {
            return Conditions.minSize(min.intValue());
        }

        if (max != null) {
            return Conditions.maxSize(max.intValue());
        }

        throw new XmlConditionConfigurationException("Element '" + ELEMENT_NAME +
            "' must have either 'min' attribute, 'max' attribute, or both");
    }

}
