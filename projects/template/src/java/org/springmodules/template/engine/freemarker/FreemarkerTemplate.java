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

package org.springmodules.template.engine.freemarker;

import org.springmodules.template.AbstractTemplate;
import org.springmodules.template.TemplateGenerationException;

import java.io.Writer;
import java.io.IOException;
import java.util.Map;

import freemarker.template.TemplateException;

/**
 * A {@link org.springmodules.template.Template} implementation that uses freemarker's {@link freemarker.template.Template}
 * under the hood to generate the output.
 *
 * @author Uri Boness
 */
public class FreemarkerTemplate extends AbstractTemplate {

    private freemarker.template.Template template;

    /**
     * Creates a new FreemarkerTemplate.
     *
     * @param template The underlying freemarker template.
     */
    public FreemarkerTemplate(freemarker.template.Template template) {
        this.template = template;
    }

    /**
     * @see org.springmodules.template.Template#generate(java.io.Writer, java.util.Map)
     */
    public void generate(Writer writer, Map model) throws TemplateGenerationException {

        try {
            template.process(model, writer);
        } catch (TemplateException te) {
            throw new TemplateGenerationException("Could not generate freemarker template", te);
        } catch (IOException ioe) {
            throw new TemplateGenerationException("Could not write generated output", ioe);
        }

    }

    /**
     * Returns the underlying freemarker template that is used.
     *
     * @return The underlying freemarker template that is used.
     */
    public freemarker.template.Template getFreemarkerTemplate() {
        return template;
    }

}
