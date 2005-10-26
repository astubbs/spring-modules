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

package org.springmodules.template.providers.velocity;

import java.io.*;
import java.util.*;

import org.apache.velocity.*;
import org.apache.velocity.app.*;
import org.springmodules.template.*;

/**
 * A Velocity implementation of the Template interface.
 *
 * @author Uri Boness
 */
public class VelocityTemplate extends AbstractTemplate {

    private final static String LOG_TAG = "internalVelocityTemplate";

    private VelocityEngine engine;
    private TemplateSource source;

    /**
     * Creates a new VelocityTemplate with a given velocity engine to use and a template source.
     *
     * @param engine The velocity engine that will be used to generate the templates.
     * @param source The source of the template.
     */
    public VelocityTemplate(VelocityEngine engine, TemplateSource source) {
        this.engine = engine;
        this.source = source;
    }

    /**
     * @see org.springmodules.template.Template#generate(java.io.Writer, java.util.Map)
     */
    public void generate(Writer writer, Map model) throws TemplateGenerationException {
        VelocityContext context = new VelocityContext();
        for (Iterator names = model.keySet().iterator(); names.hasNext();) {
            String name = (String)names.next();
            context.put(name, model.get(name));
        }
        try {
            boolean successful = engine.evaluate(context, writer, LOG_TAG, source.getReader());
            if (!successful) {
                throw new TemplateGenerationException("Could not generate output using Velocity template");
            }
        } catch (IOException ioe) {
            throw new TemplateGenerationException("Could read velocity template source", ioe);
        } catch (Exception e) {
            throw new TemplateGenerationException("Could not generate output using Velocity template", e);
        }
    }
}
