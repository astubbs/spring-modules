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

package org.springmodules.template.support;

import java.io.*;
import java.util.*;

import org.springframework.beans.factory.*;
import org.springmodules.template.*;

/**
 * Classes that need template support may extends this class.
 *
 * @author Uri Boness
 */
public class TemplateSupport implements InitializingBean {

    private Template template;

    public TemplateSupport() {
    }

    /**
     * Generates the output of the template based on the given model and writes it to the given writer.
     *
     * @param model The model based on which the output will be generated.
     * @param writer The writer to which the output will be written.
     */
    public void generate(Map model, Writer writer) {
        if (template == null) {
            throw new TemplateException("Cannot generate template for template source is not set");
        }
        template.generate(writer, model);
    }

    /**
     * Generates the output of the template based on the given model and writes it to the given output stream.
     *
     * @param model The model based on which the output will be generated.
     * @param out The output stream to which the output will be written.
     */
    public void generate(Map model, OutputStream out) {
        generate(model, new OutputStreamWriter(out));
    }

    /**
     * Generates and returns the output of the template based on the given model.
     *
     * @param model The model based on which the output will be generated.
     * @return The generated output.
     */
    public String generate(Map model) {
        StringWriter writer = new StringWriter();
        generate(model, writer);
        return writer.toString();
    }

    //============================================== Lifecycle Methods =================================================

    public void afterPropertiesSet() throws Exception {
        if (template == null) {
            throw new BeanInitializationException("Property 'template' of " + getClass().getName() + " must be set");
        }
    }


    //================================================ Setter/Getter ===================================================

    /**
     * Returns the template used for output generation.
     *
     * @return The template used for output generation.
     */
    public Template getTemplate() {
        return template;
    }

    /**
     * Sets the template to be used for output generation.
     *
     * @param template The template to be used for output generation.
     */
    public void setTemplate(Template template) {
        this.template = template;
    }

}
