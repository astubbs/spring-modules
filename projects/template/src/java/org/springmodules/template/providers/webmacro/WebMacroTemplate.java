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

import java.io.*;
import java.util.*;

import org.springmodules.template.*;
import org.springmodules.template.Template;
import org.webmacro.*;
import org.apache.commons.logging.*;
import org.apache.commons.logging.Log;

/**
 * A WebMacro implementation of the Template interface.
 *
 * @author Uri Boness
 */
public class WebMacroTemplate implements Template {

    private final static Log logger = LogFactory.getLog(WebMacroTemplate.class);

    private WebMacroContextFactory contextFactory;
    private org.webmacro.Template webMacroTemplate;

    /**
     * Creates a new WebMacroTemplate.
     *
     * @param webMacroTemplate The compiled webmacro template that will be used internally.
     * @param contextFactory The context factory that will be used to create the webmacro contexts.
     */
    public WebMacroTemplate(org.webmacro.Template webMacroTemplate, WebMacroContextFactory contextFactory) {
        this.contextFactory = contextFactory;
        this.webMacroTemplate = webMacroTemplate;
    }

    /**
     * @see Template#generate(java.io.Writer, java.util.Map)
     */
    public void generate(Writer writer, Map model) throws TemplateGenerationException {
        try {

            String output = webMacroTemplate.evaluateAsString(contextFactory.createContext(model));
            writer.write(output);
            writer.flush();

        } catch (PropertyException pe) {
            logger.error("Could not generate WebMacro template", pe);
            throw new TemplateGenerationException("Could not generate WebMacro template", pe);
        } catch (IOException ioe) {
            logger.error("Could not generate WebMacro template", ioe);
            throw new TemplateGenerationException("Could not generate WebMacro template", ioe);
        }
    }

    /**
     * @see Template#generate(java.util.Map)
     */
    public String generate(Map model) throws TemplateGenerationException {
        try {

            return webMacroTemplate.evaluateAsString(contextFactory.createContext(model));

        } catch (PropertyException pe) {
            logger.error("Could not generate WebMacro template", pe);
            throw new TemplateGenerationException("Could not generate WebMacro template", pe);
        }
    }

    /**
     * @see Template#generate(java.io.OutputStream, java.util.Map)
     */
    public void generate(OutputStream out, Map model) throws TemplateGenerationException {
        try {

            webMacroTemplate.write(out, contextFactory.createContext(model));

        } catch (PropertyException pe) {
            logger.error("Could not generate WebMacro template", pe);
            throw new TemplateGenerationException("Could not generate WebMacro template", pe);
        } catch (IOException ioe) {
            logger.error("Could not generate WebMacro template", ioe);
            throw new TemplateGenerationException("Could not generate WebMacro template", ioe);
        }
    }

}
