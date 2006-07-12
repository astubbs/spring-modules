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
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.mvc.SimpleFormController;

/**
 * Enhanced base  form controller to extend, providing facilities for configuring and setting property editors.
 *
 * @author Sergio Bossa
 */
public class EnhancedSimpleFormController extends SimpleFormController {
    
    private Map<String, PropertyEditor> customEditors = new HashMap();
    
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) 
    throws Exception {
        // Configured editors:
        for (Map.Entry<String, PropertyEditor> entry : this.customEditors.entrySet()) {
            binder.registerCustomEditor(Class.forName(entry.getKey()), entry.getValue());
        }
    }
    
    public void setCustomEditors(Map customEditors) {
        this.customEditors = customEditors;
    }
}
