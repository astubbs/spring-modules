/*
 * Copyright 2002-2007 the original author or authors.
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

package org.springmodules.template.engine.groovy;

import java.io.Writer;
import java.io.IOException;
import java.util.Map;

import org.springmodules.template.AbstractTemplate;
import org.springmodules.template.TemplateGenerationException;
import groovy.text.Template;

/**
 * A {@link Template} implementation that represents a groovy template.
 *
 * @author Uri Boness
 */
public class GroovyTemplate extends AbstractTemplate {

    private Template template;

    /**
     * Creates a new GroovyTemplate with a given {@link groovy.text.Template groovy template}.
     *
     * @param template The underlaying groovy template.
     */
    public GroovyTemplate(Template template) {
        this.template = template;
    }

    /**
     * Generates the template output based on the given model and writes it to the given writer.
     *
     * @param writer The writer to which the generated output will be written.
     * @param model The model based on which the output will be generated.
     * @throws TemplateGenerationException when an error occurs during the output generation
     * @see org.springmodules.template.Template#generate(java.io.Writer, java.util.Map)
     */
    public void generate(Writer writer, Map model) throws TemplateGenerationException {
        try {
            template.make(model).writeTo(writer);
        } catch (IOException ioe) {
            throw new TemplateGenerationException("Could not generate groovy template", ioe);
        }
    }
}
