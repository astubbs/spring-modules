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

package org.springmodules.template.providers.freemarker;

import java.io.*;
import java.util.*;

import freemarker.template.TemplateException;
import org.springmodules.template.*;
import org.springmodules.template.Template;

/**
 * A Freemarker implementation of the Template interface.
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
     * @see Template#generate(java.io.Writer, java.util.Map)
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
}
