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

package org.springmodules.web.servlet.mvc;

import java.beans.PropertyEditor;
import java.util.Iterator;
import java.util.Map;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.ServletRequestDataBinder;

/**
 * Helper class for configuring {@link org.springframework.web.bind.ServletRequestDataBinder},
 * providing facilities for configuring and setting property editors.<br>
 * Simply configure a map of custom property editors specifying, as <b>key</b>, the class of the property in the form <i>"class:CLASS_NAME"</i> 
 * if you want to edit all properties of the given type, or its path in the form <i>"property:PROPERTY_PATH"</i> if you want to
 * edit only the given property,  and as <b>value</b> the name of a bean in the application context. Please note that the bean
 * must be a {@link java.beans.PropertyEditor} and must be declared as <i>prototype</i>.<br>
 * If the <i>class</i> and <i>property</i> prefixes above are missed, the key is treated as a class name.
 * <br><br>
 * This class is thread safe.
 *
 * @author Sergio Bossa
 */
public class BinderConfiguration {
    
    private static final String CLASS_QUALIFIER = "class";
    private static final String PROPERTY_QUALIFIER = "property";
    private static final String QUALIFIER_SEPARATOR = ":";
    
    /**
     * Configure the given {@link org.springframework.web.bind.ServletRequestDataBinder}.
     *
     * @param applicationContext The {@link org.springframework.context.ApplicationContext} to use
     * for retrieving configured {@link java.beans.PropertyEditor}s.
     * @param customEditors The map of custom editors to configure (as described in class javadoc).
     * @param binder The {@link org.springframework.web.bind.ServletRequestDataBinder} to configure.
     */
    public void configureBinder(ApplicationContext applicationContext, Map customEditors, ServletRequestDataBinder binder) 
    throws Exception {
        // Configured editors:
        Iterator entries = customEditors.entrySet().iterator();
        while(entries.hasNext()) {
            Map.Entry entry = (Map.Entry) entries.next();
            String value = (String) entry.getValue();
            if (applicationContext.getBean(value) instanceof PropertyEditor && ! applicationContext.isSingleton(value)) {
                PropertyEditor editor = (PropertyEditor) applicationContext.getBean(value);
                String key = (String) entry.getKey();
                String[] splittedKey = key.split(QUALIFIER_SEPARATOR);
                if (splittedKey.length == 2) {
                    String qualifier = splittedKey[0];
                    String keyValue = splittedKey[1];
                    if (qualifier.equals(PROPERTY_QUALIFIER)) {
                        Class propertyClass = this.extractPropertyClass(binder.getTarget(), keyValue);
                        binder.registerCustomEditor(propertyClass, keyValue, editor);
                    }
                    else if (qualifier.equals(CLASS_QUALIFIER)) {
                        binder.registerCustomEditor(Class.forName(keyValue), editor);
                    }
                    else {
                        throw new IllegalStateException("Wrong qualifier.");
                    }
                }
                else if (splittedKey.length == 1) {
                    String clazz = splittedKey[0];
                    binder.registerCustomEditor(Class.forName(clazz), editor);
                }
                else {
                    throw new IllegalStateException("Wrong key format.");
                }
            }
            else {
                throw new IllegalStateException("You must specify a prototype property editor.");
            }
        }
    }

    private Class extractPropertyClass(Object target, String property) {
        BeanWrapperImpl wrapper = new BeanWrapperImpl(target);
        return wrapper.getPropertyType(property);
    }
}
