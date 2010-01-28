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

package org.springmodules.web.servlet;

import java.util.HashMap;
import java.util.Map;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

/**
 * @deprecated Use {@link org.springmodules.xt.ajax.web.servlet.AjaxModelAndView} instead.
 */
@Deprecated
public class XTModelAndView extends ModelAndView {
    
    /**
     * Ajax ModelAndView constructor: it creates a ModelAndView with a view name, a model map merging the model of the 
     * {@link org.springframework.validation.BindException} object, and the model of the given
     * additional map.
     * 
     * @param viewName The view to which redirect to.
     * @param errors The {@link org.springframework.validation.BindException}
     * passed to the Spring Controller processing method.
     * @param model An additional model map to merge with the 
     * {@link org.springframework.validation.BindException} model map.
     */
    public XTModelAndView(String viewName, BindException errors, Map model) {
        super(viewName, new HashMap(errors.getModel()));
        this.getModelInternal().putAll(model);
    }
    
    /**
     * Ajax ModelAndView constructor: it creates a ModelAndView with a view name and the model of the 
     * {@link org.springframework.validation.BindException} object.
     *
     * @param viewName The view to which redirect to.
     * @param errors The {@link org.springframework.validation.BindException}
     * passed to the Spring Controller processing method.
     */
    public XTModelAndView(String viewName, BindException errors) {
        super(viewName, new HashMap(errors.getModel()));
    }
}
