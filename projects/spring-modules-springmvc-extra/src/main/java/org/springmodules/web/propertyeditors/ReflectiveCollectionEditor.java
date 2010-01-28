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
import java.lang.reflect.Method;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;

/**
 * {@link org.springframework.beans.propertyeditors.CustomCollectionEditor} implementation for converting a collection of strings to a collection of objects and vice-versa,
 * as defined by the <code>convertElement</code> method. For converting you have to define the following:
 * <ul>
 * <li><code>dataAccessObject</code>, the object used for converting from text to the actual desired object: it could be a Factory, or a DAO.</li>
 * <li><code>dataAccessMethod</code>, the method of the dataAccessObject object to call for converting from text to object.</li>
 * <li><code>propertyName</code>, the name of the property which will represent the object string value.</li>
 * <li><code>stringConvertor</code>, for converting the string value to be passed to the <code>dataAccessMethod</code>.</li>
 * <li><code>stringConvertorOutput</code>, the <code>Class</code> of the object converted by the stringConvertor; if not specified,
 * the ReflectivePropertyEditor will try to guess it by directly inspecting the converted object.</li>
 * </ul>
 * This class is useful for binding collections in SpringMVC: i.e., for binding a collection of customers starting from a collection of customer ids, obtained from some kind of
 * selection list; using this class you can automatically convert the collection of customer ids to a collection of actual customer objects.
 *
 * @author Sergio Bossa
 */
public class ReflectiveCollectionEditor extends CustomCollectionEditor {
    
    private Object dataAccessObject;
    private String dataAccessMethod;
    private String propertyName;
    private PropertyEditor stringConvertor;
    private Class stringConvertorOutput;
    
    public ReflectiveCollectionEditor(Class collection) {
        super(collection);
    }
    
    public ReflectiveCollectionEditor(Class collection, boolean nullAsEmptyCollection) {
        super(collection, nullAsEmptyCollection);
    }
    
    protected Object convertElement(Object element) {
        Object result = null;
        
        // Convert from string to object:
        if (element instanceof String) {
            String textValue = (String) element;
            try {
                if (this.stringConvertor == null) {
                    Method method = this.dataAccessObject.getClass().getMethod(this.dataAccessMethod, new Class[]{String.class});
                    result = method.invoke(this.dataAccessObject, new Object[] {textValue});
                } else {
                    this.stringConvertor.setAsText(textValue);
                    Object value = this.stringConvertor.getValue();
                    Method method = null;
                    if (value == null && this.stringConvertorOutput == null) {
                        throw new ReflectiveCollectionEditorException("Cannot convert: " + textValue + ". Unable to determine output class.");
                    } else if (this.stringConvertorOutput != null) {
                        method = this.dataAccessObject.getClass().getMethod(this.dataAccessMethod, new Class[]{this.stringConvertorOutput});
                    } else {
                        method = this.dataAccessObject.getClass().getMethod(this.dataAccessMethod, new Class[]{value.getClass()});
                    }
                    result = method.invoke(this.dataAccessObject, new Object[]{value});
                }
            } catch(Exception ex) {
                throw new ReflectiveCollectionEditorException("An error occurred while executing: " + this.dataAccessMethod, ex);
            }
        }
        // Convert from object to string:
        else {
            try {
                BeanWrapperImpl wrapper = new BeanWrapperImpl(element);
                result = wrapper.getPropertyValue(this.propertyName).toString();
            } catch(Exception ex) {
                throw new ReflectiveCollectionEditorException("An error occurred while getting: " + this.propertyName, ex);
            }
        }
        
        return result;
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
    
    /**
     * Overridden for creating a new collection even if the original one is of the same type, because we are interested in converting
     * collection elements, not collection themselves.
     */
    protected boolean alwaysCreateNewCollection() {
        return true;
    }
}
