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

import java.io.InputStreamReader;
import java.io.IOException;

import org.springmodules.template.engine.AbstractTemplateEngine;
import org.springmodules.template.Template;
import org.springmodules.template.TemplateCreationException;
import org.springframework.core.io.Resource;
import groovy.text.TemplateEngine;
import groovy.text.SimpleTemplateEngine;

/**
 *
 * @author Uri Boness
 */
public class GroovyTemplateEngine extends AbstractTemplateEngine {

    private TemplateEngine templateEngine;

    public GroovyTemplateEngine() {
        templateEngine = new SimpleTemplateEngine();
    }

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
