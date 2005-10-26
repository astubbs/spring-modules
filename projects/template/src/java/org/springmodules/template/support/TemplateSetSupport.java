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
 * Classes that need template set support may extends this class.
 *
 * @author Uri Boness
 */
public class TemplateSetSupport {

    private TemplateSet templateSet;

    /**
     * Creates a new TemplateSetSupport.
     */
    public TemplateSetSupport() {
    }

    /**
     * Generates the output for the given template name using the given model and writes it to the given writer.
     *
     * @param templateName The name of the template to be used to generate the output.
     * @param model The model based on which the output will be generated.
     * @param writer The writer to which the output will be written.
     */
    public void generate(String templateName, Map model, Writer writer) {
        Template template = templateSet.getTemplate(templateName);
        if (template == null) {
            throw new NoSuchTemplateException("Template '" + templateName + "' was not found");
        }
        template.generate(writer, model);
    }

    /**
     * Generates the output for the given template name using the given model and writes it to the given output stream.
     *
     * @param templateName The name of the template to be used to generate the output.
     * @param model The model based on which the output will be generated.
     * @param out The output stream to which the output will be written.
     */
    public void generate(String templateName, Map model, OutputStream out) {
        generate(templateName, model, new OutputStreamWriter(out));
    }

    /**
     * Generates and returns the output for the given template name based on the given model.
     *
     * @param templateName The name of the template to be used to generate the output.
     * @param model The model based on which the output will be generated.
     * @return The generated output.
     */
    public String generate(String templateName, Map model) {
        StringWriter writer = new StringWriter();
        generate(templateName, model, writer);
        return writer.toString();
    }

    //============================================== Lifecycle Methods =================================================

    /**
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {

        if (templateSet == null) {
            throw new BeanInitializationException("Property 'templateSet' of " + getClass().getName() + " must be set");
        }

    }


    //================================================ Setter/Getter ===================================================


    /**
     * Returns the template set used by this class.
     *
     * @return The template set used by this class.
     */
    public TemplateSet getTemplateSet() {
        return templateSet;
    }

    /**
     * Sets the template set to be used by this class.
     *
     * @param templateSet The template set to be used by this class.
     */
    public void setTemplateSet(TemplateSet templateSet) {
        this.templateSet = templateSet;
    }
}
