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

package org.springmodules.template.web;

import java.util.*;

import javax.servlet.http.*;

import org.springframework.web.servlet.view.*;
import org.springmodules.template.*;

/**
 * An implementation of Spring's {@link org.springframework.web.servlet.View} interface that uses a {@link Template}
 * for rendering.
 *
 * @author Uri Boness
 */
public class TemplateView extends AbstractTemplateView {

    private Template template;

    /**
     * Empty contructor (JavaBean support)
     */
    public TemplateView() {
    }

    /**
     * Constructs a new TemplateView with a given template that will be used for the view rendering.
     *
     * @param template The template that will be used for the view rendering.
     */
    public TemplateView(Template template) {
        this.template = template;
    }

    /**
     * @see AbstractTemplateView#renderMergedTemplateModel(java.util.Map,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected void renderMergedTemplateModel(
        Map map,
        HttpServletRequest httpServletRequest,
        HttpServletResponse httpServletResponse) throws Exception {

        template.generate(httpServletResponse.getWriter(), map);
    }

    /**
     * Sets the template that will be used in this view for the rendering.
     *
     * @param template The template that will be used in this view for the rendering.
     */
    public void setTemplate(Template template) {
        this.template = template;
    }
}
