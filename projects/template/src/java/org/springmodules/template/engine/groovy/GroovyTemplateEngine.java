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

import java.io.IOException;
import java.io.InputStreamReader;

import groovy.text.SimpleTemplateEngine;
import groovy.text.TemplateEngine;
import org.springframework.core.io.Resource;
import org.springmodules.template.Template;
import org.springmodules.template.TemplateCreationException;
import org.springmodules.template.engine.AbstractTemplateEngine;

/**
 * A {@link TemplateEngine template engine} that creates {@link GroovyTemplate}'s.
 *
 * @author Uri Boness
 */
public class GroovyTemplateEngine extends AbstractTemplateEngine {

    private TemplateEngine templateEngine;

    /**
     * Constructs a new GroovyTemplateEngine.
     */
    public GroovyTemplateEngine() {
        templateEngine = new SimpleTemplateEngine();
    }

    /**
     * Creates and returns a new {@link GroovyTemplate groovy template}.
     *
     * @param resource The groovy template (script) resource.
     * @param encoding The encoding of the template.
     * @return The newly created groovy template.
     */
    public Template createTemplate(Resource resource, String encoding) {
        groovy.text.Template template = null;
        try {
            template = templateEngine.createTemplate(new InputStreamReader(resource.getInputStream()));
        } catch (ClassNotFoundException cnfe) {
            throw new TemplateCreationException("Could not create groovy template from resource '" +
                resource.getDescription() + "'", cnfe);
        } catch (IOException ioe) {
            throw new TemplateCreationException("Could not create groovy template from resource '" +
                resource.getDescription() + "'", ioe);
        }
        return new GroovyTemplate(template);
    }

}
