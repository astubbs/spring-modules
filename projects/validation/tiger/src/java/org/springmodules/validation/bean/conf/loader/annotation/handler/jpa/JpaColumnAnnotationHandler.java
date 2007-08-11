/*
 * Copyright 2002-2007 the original author or authors.
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

package org.springmodules.validation.bean.conf.loader.annotation.handler.jpa;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.util.Set;

import javax.persistence.Column;
import org.springmodules.validation.bean.conf.MutableBeanValidationConfiguration;
import org.springmodules.validation.bean.conf.loader.annotation.handler.Length;
import org.springmodules.validation.bean.conf.loader.annotation.handler.NotNull;
import org.springmodules.validation.bean.rule.MaxLengthValidationRule;
import org.springmodules.validation.bean.rule.NotNullValidationRule;
import org.springmodules.validation.bean.rule.PropertyValidationRule;
import org.springmodules.validation.util.LibraryUtils;

/**
 * @author Uri Boness
 */
public class JpaColumnAnnotationHandler extends AbstractJpaAnnotationHandler {

    public boolean supports(Annotation annotation, Class clazz, PropertyDescriptor descriptor) {
        return Column.class.isInstance(annotation);
    }

    public void handleAnnotation(Annotation annotation, Class clazz, PropertyDescriptor descriptor, MutableBeanValidationConfiguration configuration) {
        Column column = (Column)annotation;

        Set<Annotation> annotations = getAllPropertyAnnotations(clazz, descriptor);

        // handling max length only if other explicit validation annotations do not exist.
        if (!contains(annotations, Length.class) &&
            (!LibraryUtils.HIBERNATE_VALIDATOR_IN_CLASSPATH || !contains(annotations, org.hibernate.validator.Length.class))) {
            int length = column.length();
            PropertyValidationRule propertyRule = new PropertyValidationRule(descriptor.getName(), new MaxLengthValidationRule(length));
            configuration.addPropertyRule(descriptor.getName(), propertyRule);
        }

        // handling nullability only if other explicit validation annotations do not exist.
        if (!contains(annotations, NotNull.class) &&
            (!LibraryUtils.HIBERNATE_VALIDATOR_IN_CLASSPATH || !contains(annotations, org.hibernate.validator.NotNull.class))) {
            if (!column.nullable()) {
                PropertyValidationRule propertyRule = new PropertyValidationRule(descriptor.getName(), new NotNullValidationRule());
                configuration.addPropertyRule(descriptor.getName(), propertyRule);
            }
        }

    }


}
