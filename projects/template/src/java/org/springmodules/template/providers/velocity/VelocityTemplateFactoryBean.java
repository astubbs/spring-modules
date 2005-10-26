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

import org.springmodules.template.*;

/**
 * A FactoryBean that creates {@link VelocityTemplate}s.
 *
 * @author Uri Boness
 */
public class VelocityTemplateFactoryBean extends AbstractTemplateFactoryBean {

    private Properties configuration;

    /**
     * Empty contructor (JavaBean support)
     */
    public VelocityTemplateFactoryBean() {
    }

    /**
     * Sets specific configurations for the velocity engine.
     *
     * @param configuration The configuration for the velocity engine.
     */
    public void setConfiguration(Properties configuration) {
        this.configuration = configuration;
    }

    /**
     * Creats a new {@link VelocityTemplateFactory}.
     *
     * @see org.springmodules.template.AbstractTemplateFactoryBean#createTemplateFactory()
     */
    protected TemplateFactory createTemplateFactory() throws Exception {
        VelocityTemplateFactory factory = new VelocityTemplateFactory();
        if (configuration != null) {
            factory.setConfiguration(configuration);
        }
        factory.init();
        return factory;
    }
}
