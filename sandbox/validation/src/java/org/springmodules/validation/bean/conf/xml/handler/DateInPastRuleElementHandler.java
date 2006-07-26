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

import java.beans.PropertyDescriptor;
import java.util.Calendar;
import java.util.Date;

import org.springmodules.validation.bean.conf.xml.DefaultXmBeanValidationConfigurationlLoaderConstants;
import org.springmodules.validation.util.condition.Condition;
import org.springmodules.validation.util.condition.date.IsInThePastDateCondition;
import org.w3c.dom.Element;

/**
 * An {@link AbstractPropertyValidationElementHandler} implementation that can handle an element that represents a "date in
 * the past" validation rule - a rule that validates that a given date occured in the past.
 *
 * @author Uri Boness
 */
public class DateInPastRuleElementHandler extends AbstractPropertyValidationElementHandler
    implements DefaultXmBeanValidationConfigurationlLoaderConstants {

    /**
     * The default error code for the parsed validation rule.
     */
    public static final String DEFAULT_ERROR_CODE = "in.past";

    private static final String ELEMENT_NAME = "in-past";

    /**
     * Constructs a new DateInPastRuleElementHandler.
     */
    public DateInPastRuleElementHandler() {
        super(ELEMENT_NAME, DEFAULT_NAMESPACE_URL);
    }

    /**
     * In addition to the element name and namespace check, this handler only support properties of types
     * {@link java.util.Date} and {@link java.util.Calendar}.
     *
     * @see org.springmodules.validation.bean.conf.xml.PropertyValidationElementHandler#supports(org.w3c.dom.Element, Class, java.beans.PropertyDescriptor)
     */
    public boolean supports(Element element, Class clazz, PropertyDescriptor descriptor) {
        return super.supports(element, clazz, descriptor)
               &&
               (
                   Date.class.isAssignableFrom(descriptor.getPropertyType())
                   ||
                   Calendar.class.isAssignableFrom(descriptor.getPropertyType())
               );
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
     * Creates and returns a new {@link IsInThePastDateCondition}.
     *
     * @see AbstractPropertyValidationElementHandler#extractCondition(org.w3c.dom.Element)
     */
    protected Condition extractCondition(Element element) {
        return new IsInThePastDateCondition();
    }

}
