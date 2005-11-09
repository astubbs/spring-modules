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

package org.springmodules.template.providers.webmacro;

import org.springmodules.template.*;
import org.springmodules.template.Template;
import org.webmacro.*;
import org.apache.commons.logging.*;
import org.apache.commons.logging.Log;

/**
 * A {@link TemplateSet} implementation that uses the WebMacro engine to create the templates that will be returned.
 *
 * @author Uri Boness
 */
public class WebMacroTemplateSet implements TemplateSet {

    private final static Log logger = LogFactory.getLog(WebMacroTemplateSet.class);

    private WebMacro webMacro;

    /**
     * Constructs a new WebMacroTemplateSet with a given WebMacro engine.
     *
     * @param webMacro The WebMacro engine that will be used to create the returned templates.
     */
    public WebMacroTemplateSet(WebMacro webMacro) {
        this.webMacro = webMacro;
    }

    /**
     * @see TemplateSet#getTemplate(String)
     */
    public Template getTemplate(String name) {
        try {

            org.webmacro.Template webMacroTemplate = webMacro.getTemplate(name);
            return new WebMacroTemplate(webMacroTemplate, new DefaultContextFactory(webMacro));

        } catch (ResourceException re) {
            logger.error("Could not find template '" + name + "' in template set", re);
            return null;
        }
    }

}
