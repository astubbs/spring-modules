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

package org.springmodules.template.providers.freemarker;

import java.util.*;

import org.springmodules.template.*;

/**
 * A FactoryBean that creates {@link FreemarkerTemplateSet}s.
 *
 * @author Uri Boness
 */
public class FreemarkerTemplateSetFactoryBean extends AbstractTemplateSetFactoryBean {

    // freemarker settings.
    private Properties settings;

    /**
     * Empty constructor (JavaBean support)
     */
    public FreemarkerTemplateSetFactoryBean() {
    }

    /**
     * Sets the freemarker settings from which the freemarker configuration will be created.
     *
     * @param settings The freemarker settings from which the freemarker configuration will be created.
     * @see freemarker.template.Configuration#setSettings(java.util.Properties)
     */
    public void setSettings(Properties settings) {
        this.settings = settings;
    }

    /**
     * Creates a new {@link FreemarkerTemplateFactory}.
     *
     * @see org.springmodules.template.AbstractTemplateSetFactoryBean#createTemplateFactory()
     */
    protected TemplateFactory createTemplateFactory() throws Exception {
        FreemarkerTemplateFactory factory = new FreemarkerTemplateFactory();
        if (settings != null) {
            factory.setSettings(settings);
        }
        return factory;
    }

}
