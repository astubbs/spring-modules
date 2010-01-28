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

package org.springmodules.template.engine.velocity;

import java.io.Writer;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Map;

import org.springmodules.template.AbstractTemplate;
import org.springmodules.template.TemplateGenerationException;
import org.springmodules.template.engine.velocity.extended.ExtendedVelocityEngine;
import org.springmodules.template.engine.velocity.extended.SpecialTemplate;
import org.springframework.core.io.Resource;
import org.apache.velocity.VelocityContext;

/**
 * A velocity based {@link org.springmodules.template.Template template} imlementation that uses an underlying
 * {@link SpecialTemplate}.
 *
 * @author Uri Boness
 */
public class SpecialVelocityTemplate extends AbstractTemplate {

    private final SpecialTemplate template;

    private final String templateDescription;

    /**
     * Constructs a new SpecialVelocityTemplate with a given {@link SpecialTemplate} and a template description that
     * will be used for log messages.
     *
     * @param template The given SpecialTemplate.
     * @param templateDescription The description of the template that will be used in log meesages.
     */
    public SpecialVelocityTemplate(SpecialTemplate template, String templateDescription) {
        this.template = template;
        this.templateDescription = templateDescription;
    }

    public void generate(Writer writer, Map model) throws TemplateGenerationException {
        VelocityContext context = new VelocityContext(model);
        try {
            template.merge(context, writer);
        } catch (IOException ioe) {
            throw new TemplateGenerationException("Could not generate velocity template for resource '" +
                templateDescription + "'", ioe);
        }
    }

}
