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
import org.apache.velocity.exception.*;
import org.springmodules.template.*;

/**
 * A Template implementation that wraps a velocity template.
 *
 * @author Uri Boness
 */
public class VelocityTemplateWrapperTemplate extends AbstractTemplate {

    private org.apache.velocity.Template template;

    /**
     * Constructs a new VelocityTemplateWrapperTemplate with a given velocity template
     *
     * @param template The velocity template to wrap.
     */
    public VelocityTemplateWrapperTemplate(org.apache.velocity.Template template) {
        this.template = template;
    }

    /**
     * @see org.springmodules.template.Template#generate(java.io.Writer, java.util.Map)
     */
    public void generate(Writer writer, Map model) throws TemplateGenerationException {
        VelocityContext context = new VelocityContext(model);
        try {
            template.merge(context, writer);
        } catch (ParseErrorException pee) {
            throw new TemplateGenerationException("Could not generate velocity template output due to syntax error", pee);
        } catch (MethodInvocationException mie) {
            throw new TemplateGenerationException("Could not generate velocity template output due invalid method call", mie);
        } catch (ResourceNotFoundException rnfe) {
            throw new TemplateGenerationException("Template source could not be found (might have been removed)", rnfe);
        } catch (Exception e) {
            throw new TemplateGenerationException("Could not generate template output", e);
        }
    }

}
