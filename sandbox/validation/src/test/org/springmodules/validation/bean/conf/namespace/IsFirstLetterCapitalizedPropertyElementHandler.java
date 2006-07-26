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

package org.springmodules.validation.bean.conf.namespace;

import java.beans.PropertyDescriptor;

import org.springmodules.validation.bean.conf.MutableBeanValidationConfiguration;
import org.springmodules.validation.bean.conf.xml.PropertyValidationElementHandler;
import org.springmodules.validation.bean.rule.DefaultValidationRule;
import org.springmodules.validation.bean.rule.PropertyValidationRule;
import org.springmodules.validation.util.condition.AbstractCondition;
import org.springmodules.validation.util.condition.Condition;
import org.w3c.dom.Element;

/**
 * Handles &lt;is-first-letter-capitalized/&gt; elements.
 *
 * @author Uri Boness
 */
public class IsFirstLetterCapitalizedPropertyElementHandler implements PropertyValidationElementHandler {

    public boolean supports(Element element, Class clazz, PropertyDescriptor descriptor) {
        return element.getLocalName().equals("is-first-letter-capitalized");
    }

    public void handle(Element element, String propertyName, MutableBeanValidationConfiguration configuration) {
        Condition cond = new AbstractCondition() {
            public boolean doCheck(Object object) {
                String text = (String)object;
                if (text.length() == 0) {
                    return false;
                }
                return Character.isUpperCase(text.charAt(0));
            }
        };
        DefaultValidationRule rule = new DefaultValidationRule(cond, "is.first.letter.capitalized");
        configuration.addPropertyRule(propertyName, new PropertyValidationRule(propertyName, rule));
    }

}
