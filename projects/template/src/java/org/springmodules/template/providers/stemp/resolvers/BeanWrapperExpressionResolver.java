/*
 * Copyright 2002-2005 the original author or authors.
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

package org.springmodules.template.providers.stemp.resolvers;

import java.beans.*;
import java.util.*;

import org.springframework.beans.*;
import org.springframework.util.*;
import org.springmodules.template.providers.stemp.*;

/**
 * An implementation of {@link org.springmodules.template.providers.stemp.ExpressionResolver} that uses
 * {@link BeanWrapper}'s bean properties expressions support.
 *
 * @author Uri Boness
 */
public class BeanWrapperExpressionResolver implements ExpressionResolver {

    private final static String POINT = ".";

    private Map customPropertyEditorsByType;

    public BeanWrapperExpressionResolver() {
        customPropertyEditorsByType = new HashMap();
    }

    /**
     * @see ExpressionResolver#resolve(String, java.util.Map)
     */
    public String resolve(String expression, Map model) {
        if (expression.indexOf(POINT) < 0) {
            return String.valueOf(model.get(expression));
        }
        String[] splitExpression = StringUtils.split(expression, POINT);
        Object bean = model.get(splitExpression[0]);
        BeanWrapper wrapper = new BeanWrapperImpl(bean);
        registerCustomEditors(wrapper);
        PropertyEditor editor = wrapper.findCustomEditor(null, splitExpression[1]);
        if (editor != null) {
            editor.setValue(wrapper.getPropertyValue(splitExpression[1]));
            return editor.getAsText();
        }
        return String.valueOf(wrapper.getPropertyValue(splitExpression[1]));
    }

    /**
     * Configures specific property editors for specific types.
     *
     * @param customPropertyEditorsByType The custom property editors associated with the targeted types.
     */
    public void setCustomPropertyEditorsByType(Map customPropertyEditorsByType) {
        this.customPropertyEditorsByType = customPropertyEditorsByType;
    }

    /**
     * Adds a new custom property editor to a specify type.
     *
     * @param type The type for which the custom property editor will be set.
     * @param editor The custom property editor that will be set for the given file.
     */
    public void addCustomPropertyEditor(Class type, PropertyEditor editor) {
        customPropertyEditorsByType.put(type, editor);
    }

    /**
     * Registers the custom property editors configured in this class to the used BeaWrapper.
     *
     * @param wrapper The bean wrapper int which the property editor was set.
     */
    protected void registerCustomEditors(BeanWrapper wrapper) {
        for (Iterator types = customPropertyEditorsByType.keySet().iterator(); types.hasNext();) {
            Class type = (Class)types.next();
            PropertyEditor editor = (PropertyEditor)customPropertyEditorsByType.get(type);
            wrapper.registerCustomEditor(type, editor);
        }
    }

}
