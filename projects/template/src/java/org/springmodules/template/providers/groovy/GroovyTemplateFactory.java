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

import groovy.text.*;
import org.codehaus.groovy.control.*;
import org.springmodules.template.*;
import org.springmodules.template.Template;

/**
 * A TemplateFactory that creates GroovyTemplates.
 *
 * @author Uri Boness
 */
public class GroovyTemplateFactory extends AbstractTemplateFactory {

    // the groovy template engine to be used.
    private TemplateEngine templateEngine;

    public GroovyTemplateFactory() {
        templateEngine = new SimpleTemplateEngine();
    }

    /**
     * @see AbstractTemplateFactory#createTemplate(org.springmodules.template.TemplateSource)
     */
    public Template createTemplate(TemplateSource source) throws TemplateCreationException {
        try {

            groovy.text.Template template = templateEngine.createTemplate(source.getReader());
            return new GroovyTemplate(template);

        } catch (CompilationFailedException e) {
            throw new MalformedTemplateSourceException("Could not create Groovy Template", e);
        } catch (ClassNotFoundException e) {
            throw new TemplateCreationException("Could not create Groovy Template", e);
        } catch (IOException e) {
            throw new TemplateCreationException("Could not create Groovy Template. Encountered " +
                "errors while trying to read template source", e);
        }
    }

    //============================================ Setter/Getter ==================================================

    /**
     * Sets the groovy template engine to use. <code>groovy.text.SimpleTemplateEngine</code> is used
     * by default.
     *
     * @param templateEngine The template engine to be used.
     */
    public void setTemplateEngine(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

}
