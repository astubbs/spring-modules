/*
 * Copyright 2006 the original author or authors.
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
package org.springmodules.xt.ajax.component.support;

import java.beans.PropertyEditor;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

/**
 * Helper object used for computing a bind expression and value to assign to Ajax (input) components.
 *
 * @author Sergio Bossa
 */
public class BindStatusHelper {
    
    private String expression;
    private Object target;
    private PropertyEditor propertyEditor;
    
    /**
     * Construct the helper, giving just its binding expression.
     *
     * @param expression The binding expression, i.e.: "customer.address.phone"
     */
    public BindStatusHelper(String expression) {
        this(expression, null);
    }
    
    /**
     * Construct the helper, giving its binding expression and target object which the
     * expression will be evaluated on.
     *
     * @param expression The binding expression, i.e.: "customer.address.phone"
     * @param target The object evaluated for extracting the binding value. 
     */
    public BindStatusHelper(String expression, Object target) {
        int index = expression.indexOf('.');
        if (index == -1) {
            throw new IllegalArgumentException("Wrong expression.");
        }
        else {
            this.expression = expression.substring(index + 1);
        }
        this.target = target;
    }
    
    /**
     * Return the correct bind expression to use. Please note that you have to construct this object with a bind expression like
     * "customer.address.phone", while the correct expression returned by this method is "address.phone".
     * @return The binding expression.
     */
    public String getStatusExpression() {
        return this.expression;
    }
    
    /**
     * Return the binding value, extracted from the target object by evaluating the binding expression.<br>
     * The binding value must be a string, so if the extracted value is not a string, its <code>toString()</code>
     * method will be used.<br>
     * If you want to provide custom behaviour, set an appropriate property editor (see {@link #setPropertyEditor(PropertyEditor propertyEditor)}).
     * @return The binding value, or null if no target object has been set.
     */
    public String getStatusValue() {
        if (this.target != null) {
            BeanWrapper wrapper = new BeanWrapperImpl(this.target);
            Object value = wrapper.getPropertyValue(this.expression);
            if (this.propertyEditor == null) {
                return value.toString();
            }
            else {
                this.propertyEditor.setValue(value);
                return this.propertyEditor.getAsText();
            }
        }
        else {
            return null;
        }
    }
    
    /**
     * Set a property editor to use for converting the originally extracted object value to a custom string value.<br>
     * I.E. : If the value corresponding to the given expression is an object of class "Person", set here a property editor capable
     * of transforming that object in a string representation.
     */
    public void setPropertyEditor(PropertyEditor propertyEditor) {
        this.propertyEditor = propertyEditor;
    }

    public PropertyEditor getPropertyEditor() {
        return this.propertyEditor;
    }
}
