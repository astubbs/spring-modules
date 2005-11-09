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

package org.springmodules.template.providers.webmacro;

import java.util.*;

import org.springmodules.template.*;

/**
 * A {@link org.springframework.beans.factory.FactoryBean} that creates {@link WebMacroTemplate}s.
 *
 * @author Uri Boness
 */
public class WebMacroTemplateFactoryBean extends AbstractTemplateFactoryBean {

    private Properties settings;

    /**
     * Default constructor (javabean support).
     */
    public WebMacroTemplateFactoryBean() {
    }

    /**
     * @see org.springmodules.template.AbstractTemplateFactoryBean#createTemplateFactory()
     */
    protected TemplateFactory createTemplateFactory() throws Exception {
        WebMacroTemplateFactory factory = new WebMacroTemplateFactory();
        factory.setProperties(settings);
        return factory;
    }

    //============================================== Setter/Getter =====================================================

    /**
     * Sets the configuration for the WebMacro engine.
     *
     * @param settings The configuration for the WebMacro engine.
     */
    public void setSettings(Properties settings) {
        this.settings = settings;
    }


}
