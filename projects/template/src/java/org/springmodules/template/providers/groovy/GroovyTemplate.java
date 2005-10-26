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

package org.springmodules.template.providers.groovy;

import java.io.*;
import java.util.*;

import groovy.lang.*;
import org.springmodules.template.*;

/**
 * A groovy Template implementation.
 *
 * @author Uri Boness
 */
class GroovyTemplate extends AbstractTemplate {

    // the internal groovy template.
    private groovy.text.Template template;

    /**
     * Creates a new GroovyTemplate with a given groovy template.
     *
     * @param template The groovy template to be used.
     */
    public GroovyTemplate(groovy.text.Template template) {
        this.template = template;
    }

    /**
     * @see AbstractTemplate#generate(java.io.Writer, java.util.Map)
     */
    public void generate(Writer writer, Map model) throws TemplateGenerationException {
        try {

            Writable writable = template.make(model);
            writable.writeTo(writer);

        } catch (IOException ioe) {
            throw new TemplateGenerationException("Could not write template output", ioe);
        }
    }


}
