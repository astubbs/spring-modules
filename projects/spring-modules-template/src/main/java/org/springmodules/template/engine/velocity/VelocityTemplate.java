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

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.springframework.core.io.Resource;
import org.springmodules.template.AbstractTemplate;
import org.springmodules.template.TemplateGenerationException;
import org.springmodules.template.engine.velocity.extended.ExtendedVelocityEngine;

/**
 *
 * @deprecated Using {@link org.springmodules.template.engine.velocity.SpecialVelocityTemplate} now instead.
 * @author Uri Boness
 */
public class VelocityTemplate extends AbstractTemplate {

    private final ExtendedVelocityEngine engine;
    private final Resource resource;
    private final String encoding;

    public VelocityTemplate(Resource resource, String encoding, ExtendedVelocityEngine engine) {
        this.engine = engine;
        this.resource = resource;
        this.encoding = encoding;
    }

    public void generate(Writer writer, Map model) throws TemplateGenerationException {
        VelocityContext context = new VelocityContext(model);
        try {
            engine.evaluate(context, writer, resource.getDescription(), new InputStreamReader(resource.getInputStream(), encoding));
        } catch (IOException ioe) {
            throw new TemplateGenerationException("Could not generate velocity template for resource '" +
                resource.getDescription() + "'", ioe);
        }
    }
}
