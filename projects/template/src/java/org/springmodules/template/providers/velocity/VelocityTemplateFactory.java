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

import java.util.*;

import org.apache.velocity.app.*;
import org.apache.velocity.exception.*;
import org.springmodules.template.*;
import org.springmodules.template.providers.velocity.extended.*;

/**
 * A Velocity implementation of the TemplateFactory interface.
 *
 * @author Uri Boness
 */
public class VelocityTemplateFactory extends AbstractTemplateFactory {

    // the default velocity engine. this engine will be used as long as there's not need for a
    // special engine.
    private VelocityEngine engine;

    // the configuration for the velocity engines.
    private Properties configuration;

    public VelocityTemplateFactory() {
        engine = new VelocityEngine();
    }

    /**
     * @see TemplateFactory#createTemplate(org.springmodules.template.TemplateSource)
     */
    public Template createTemplate(TemplateSource source) throws TemplateCreationException {
        //@todo can be optimized by using velocity compiled template engine.getTemplate(...)
        return new VelocityTemplate(engine, source);
    }

    /**
     * @see TemplateFactory#createTemplate(org.springmodules.template.TemplateSourceResolver, String)
     */
    public Template createTemplate(TemplateSourceResolver templateSourceResolver, String templateName)
        throws TemplateCreationException {

        // creating an extended velocity engine. Each template set will have its own engine. This is because
        // a custom resource loader needs to be set on the engine. The resource loader will only 'know' of
        // the given sources.
        TemplateSourceResolverResourceLoader loader = new TemplateSourceResolverResourceLoader(templateSourceResolver);
        ExtendedResourceManager rm = new ExtendedResourceManager(loader);
        ExtendedRuntimeInstance ri = new ExtendedRuntimeInstance(rm);
        ExtendedVelocityEngine engine = new ExtendedVelocityEngine(ri);
        try {
            if (configuration == null) {
                engine.init();
            } else {
                engine.init(configuration);
            }
        } catch (Exception e) {
            throw new TemplateCreationException("Could create templates for velocity engine could not be initialized", e);
        }
        try {
            return new VelocityTemplateWrapperTemplate(engine.getTemplate(templateName));
        } catch (ParseErrorException pee) {
            throw new TemplateCreationException("Could not parse velocity template from source \"" +
                templateName + "\"", pee);
        } catch (ResourceNotFoundException rnfe) {
            throw new TemplateCreationException("Could create velocity template for source \"" +
                templateName + "\" was not found (might have been removed)", rnfe);
        } catch (Exception e) {
            throw new TemplateCreationException("Could create velocity template from source \"" + templateName + "\"", e);
        }
    }

    /**
     * @see TemplateFactory#createTemplateSet(org.springmodules.template.TemplateSource[])
     */
    public TemplateSet createTemplateSet(TemplateSource[] sources) throws TemplateCreationException {

        // creating an extended velocity engine. Each template set will have its own engine. This is because
        // a custom resource loader needs to be set on the engine. The resource loader will only 'know' of
        // the given sources.
        TemplateSourceResourceLoader loader = new TemplateSourceResourceLoader();
        loader.addTemplateSources(sources);
        ExtendedResourceManager rm = new ExtendedResourceManager(loader);
        ExtendedRuntimeInstance ri = new ExtendedRuntimeInstance(rm);
        ExtendedVelocityEngine engine = new ExtendedVelocityEngine(ri);
        try {
            if (configuration == null) {
                engine.init();
            } else {
                engine.init(configuration);
            }
        } catch (Exception e) {
            throw new TemplateCreationException("Could create templates for velocity engine could not be initialized", e);
        }

        SimpleTemplateSet set = new SimpleTemplateSet();
        for (int i=0; i<sources.length; i++) {
            try {

                org.apache.velocity.Template velocityTemplate = engine.getTemplate(sources[i].getName());
                VelocityTemplateWrapperTemplate template = new VelocityTemplateWrapperTemplate(velocityTemplate);
                set.addTemplate(sources[i].getName(), template);

            } catch (ParseErrorException pee) {
                throw new TemplateCreationException("Could not parse velocity template from source \"" +
                    sources[i].getName() + "\"", pee);
            } catch (ResourceNotFoundException rnfe) {
                throw new TemplateCreationException("Could create velocity template for source \"" +
                    sources[i].getName() + "\" was not found (might have been removed)", rnfe);
            } catch (Exception e) {
                throw new TemplateCreationException("Could create velocity template from source \"" +
                    sources[i].getName() + "\"", e);
            }
        }
        return set;
    }

    /**
     * @see TemplateFactory#createTemplateSet(org.springmodules.template.TemplateSourceResolver)
     */
    public TemplateSet createTemplateSet(TemplateSourceResolver templateSourceResolver) throws TemplateCreationException {

        // creating an extended velocity engine. Each template set will have its own engine. This is because
        // a custom resource loader needs to be set on the engine. The resource loader will only 'know' of
        // the given sources.
        TemplateSourceResolverResourceLoader loader = new TemplateSourceResolverResourceLoader(templateSourceResolver);
        ExtendedResourceManager rm = new ExtendedResourceManager(loader);
        ExtendedRuntimeInstance ri = new ExtendedRuntimeInstance(rm);
        ExtendedVelocityEngine engine = new ExtendedVelocityEngine(ri);
        try {
            if (configuration == null) {
                engine.init();
            } else {
                engine.init(configuration);
            }
        } catch (Exception e) {
            throw new TemplateCreationException("Could create templates for velocity engine could not be initialized", e);
        }

        return new VelocityTemplateSet(engine);
    }

    /**
     * Initializes the factory.
     *
     * @throws TemplateConfigurationException
     */
    public void init() throws TemplateConfigurationException {
        try {
            if (configuration == null) {
                engine.init();
            } else {
                engine.init(configuration);
            }

        } catch (Exception e) {
            throw new TemplateConfigurationException("Could not initialized velocity engine", e);
        }
    }

    /**
     * Sets the configuration properties the velocity engines will be configured with.
     *
     * @param configuration The configuration properties
     */
    public void setConfiguration(Properties configuration) {
        this.configuration = configuration;
    }

}
