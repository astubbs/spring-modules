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

package org.springmodules.xt.utils.mvc.controller;

import java.beans.PropertyEditor;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.mvc.SimpleFormController;

/**
 * Enhanced base  form controller to extend, providing facilities for configuring and setting property editors.<br>
 * Simply configure a map of custom property editors specifying, as <b>key</b>, the class of the property in the form <i>"class:CLASS_NAME"</i> 
 * if you want to edit all properties of the given type, or its path in the form <i>"property:PROPERTY_PATH"</i> if you want to
 * edit only the given property,  and as <b>value</b> the name of a bean in the application context. Please note that the bean
 * should be a {@link java.beans.PropertyEditor} and must be declared as <i>prototype</i>.<br>
 * If the <i>class</i> and <i>property</i> prefixes above are missed, the key is treated as a class name.
 *
 * @author Sergio Bossa
 */
public class EnhancedSimpleFormController extends SimpleFormController {
    
    private static final String CLASS_QUALIFIER = "class";
    private static final String PROPERTY_QUALIFIER = "property";
    private static final String QUALIFIER_SEPARATOR = ":";
    
    private Map<String, String> customEditors = new HashMap();
    
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) 
    throws Exception {
        // Configured editors:
        for (Map.Entry<String, String> entry : this.customEditors.entrySet()) {
            String value = entry.getValue();
            if (this.getApplicationContext().getBean(value) instanceof PropertyEditor && !this.getApplicationContext().isSingleton(value)) {
                PropertyEditor editor = (PropertyEditor) this.getApplicationContext().getBean(value);
                String key = entry.getKey();
                String[] splittedKey = key.split(QUALIFIER_SEPARATOR);
                if (splittedKey.length == 2) {
                    String qualifier = splittedKey[0];
                    if (qualifier.equals(PROPERTY_QUALIFIER)) {
                        binder.registerCustomEditor(null, splittedKey[1], editor);
                    }
                    else if (qualifier.equals(CLASS_QUALIFIER)) {
                        binder.registerCustomEditor(Class.forName(splittedKey[1]), editor);
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
    
    /**
     * Set the map of custom editors, as described above.
     */
    public void setCustomEditors(Map customEditors) {
        this.customEditors = customEditors;
    }
    
    /**
     * Get the map of custom editors.
     */
    public Map<String, String> getCustomEditors() {
        return  this.customEditors;
    }
}
