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

import org.springmodules.template.*;
import groovy.text.*;

/**
 * A FactoryBean that creates GroovyTemplate's.
 *
 * @author Uri Boness
 */
public class GroovyTemplateFactoryBean extends AbstractTemplateFactoryBean {

    private TemplateEngine engine;

    /**
     * Empty constructor (JavaBean support)
     */
    public GroovyTemplateFactoryBean() {
        engine = new SimpleTemplateEngine();
    }

    /**
     * Sets a custom groovy template engine to be used.
     *
     * @param engine The custom groovy template engine.
     */
    public void setEngine(TemplateEngine engine) {
        this.engine = engine;
    }

    /**
     * Creates a new GroovyTemplateFactory.
     *
     * @see GroovyTemplateFactory
     * @see org.springmodules.template.AbstractTemplateFactoryBean#createTemplateFactory()
     */
    protected TemplateFactory createTemplateFactory() throws Exception {
        GroovyTemplateFactory factory = new GroovyTemplateFactory();
        factory.setTemplateEngine(engine);
        return factory;
    }

}
