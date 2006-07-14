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
import java.lang.annotation.Annotation;

import org.springmodules.validation.bean.conf.MutableBeanValidationConfiguration;
import org.springmodules.validation.bean.conf.annotation.PropertyValidationAnnotationHandler;
import org.springmodules.validation.bean.rule.DefaultValidationRule;
import org.springmodules.validation.bean.rule.PropertyValidationRule;
import org.springmodules.validation.util.condition.AbstractCondition;
import org.springmodules.validation.util.condition.Condition;

/**
 * Handles {@link IsFirstLetterCapitalized} annotations.
 *
 * @author Uri Boness
 */
public class IsFirstLetterCapitalizedPropertyAnnotationHandler implements PropertyValidationAnnotationHandler {

    public boolean supports(Annotation annotation, Class clazz, PropertyDescriptor descriptor) {
        return IsFirstLetterCapitalized.class.isInstance(annotation);
    }

    public void handleAnnotation(Annotation annotation, Class clazz, PropertyDescriptor descriptor, MutableBeanValidationConfiguration configuration) {
        Condition cond = new AbstractCondition() {
            public boolean doCheck(Object object) {
                String text = (String)object;
                if (text.length() == 0) {
                    return false;
                }
                return Character.isUpperCase(text.charAt(0));
            }
        };
        String propertyName = descriptor.getName();
        DefaultValidationRule rule = new DefaultValidationRule(cond, "is.first.letter.capitalized");
        configuration.addPropertyRule(propertyName, new PropertyValidationRule(propertyName, rule));
    }

}
