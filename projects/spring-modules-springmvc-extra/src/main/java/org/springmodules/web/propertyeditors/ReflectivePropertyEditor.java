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

package org.springmodules.web.propertyeditors;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;
import java.lang.reflect.Method;
import org.springframework.beans.BeanWrapperImpl;

/**
 * {@link java.beans.PropertyEditor} implementation for making instances of generic property editors, capable of converting from text any object type using Java reflection.<br>
 * Each ReflectivePropertyEditor instance dynamically converts objects to text and vice-versa thanks to four configurable parameters:
 * <ul>
 * <li><code>dataAccessObject</code>, the object used for converting from text to the actual desired object: it could be a Factory, or a DAO.</li>
 * <li><code>dataAccessMethod</code>, the method of the dataAccessObject object to call for converting from text to object.</li>
 * <li><code>propertyName</code>, the name of the property which will represent the object text value.</li>
 * <li><code>stringConvertor</code>, for converting the string value to be passed to the <code>dataAccessMethod</code>.</li>
 * <li><code>stringConvertorOutput</code>, the <code>Class</code> of the object converted by the stringConvertor; if not specified,
 * the ReflectivePropertyEditor will try to guess it by directly inspecting the converted object.</li>
 * </ul>
 * 
 * @author Sergio Bossa
 */
public class ReflectivePropertyEditor extends PropertyEditorSupport {
    
    private Object dataAccessObject;
    private String dataAccessMethod;
    private String propertyName;
    private PropertyEditor stringConvertor;
    private Class stringConvertorOutput;
    
    public String getAsText() {
        if (this.getValue() == null) {
            return null;
        }
        else {
            String result = null;
            try {
                BeanWrapperImpl wrapper = new BeanWrapperImpl(this.getValue());
                result = wrapper.getPropertyValue(this.propertyName).toString();
            }
            catch(Exception ex) {
                throw new ReflectivePropertyEditorException("An error occurred while getting: " + this.propertyName, ex);
            }
            return result;
        }
    }
    
    public void setAsText(String textValue) {
        try {
            if (this.stringConvertor == null) {
                Method method =  this.dataAccessObject.getClass().getMethod(this.dataAccessMethod, new Class[]{String.class});
                this.setValue(method.invoke(this.dataAccessObject, new Object[]{textValue}));
            }
            else {
                this.stringConvertor.setAsText(textValue);
                Object value = this.stringConvertor.getValue();
                Method method = null;
                if (value == null && this.stringConvertorOutput == null) {
                    throw new ReflectivePropertyEditorException("Cannot convert: " + textValue + ". Unable to determine output class.");
                } else if (this.stringConvertorOutput != null) {
                    method = this.dataAccessObject.getClass().getMethod(this.dataAccessMethod, new Class[]{this.stringConvertorOutput});
                } else {
                    method = this.dataAccessObject.getClass().getMethod(this.dataAccessMethod, new Class[]{value.getClass()});
                }
                this.setValue(method.invoke(this.dataAccessObject, new Object[]{value}));
            }
        }
        catch(Exception ex) {
            throw new ReflectivePropertyEditorException("An error occurred while executing: " + this.dataAccessMethod, ex);
        }
    }

    public String getPropertyName() {
        return this.propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public Object getDataAccessObject() {
        return this.dataAccessObject;
    }

    public void setDataAccessObject(Object dataAccessObject) {
        this.dataAccessObject = dataAccessObject;
    }

    public String getDataAccessMethod() {
        return this.dataAccessMethod;
    }

    public void setDataAccessMethod(String dataAccessMethod) {
        this.dataAccessMethod = dataAccessMethod;
    }

    public PropertyEditor getStringConvertor() {
        return this.stringConvertor;
    }

    public void setStringConvertor(PropertyEditor stringConvertor) {
        this.stringConvertor = stringConvertor;
    }

    public Class getStringConvertorOutput() {
        return this.stringConvertorOutput;
    }

    public void setStringConvertorOutput(Class stringConvertorOutput) {
        this.stringConvertorOutput = stringConvertorOutput;
    }
}
