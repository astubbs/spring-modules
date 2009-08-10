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

import java.util.Properties;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springmodules.template.Template;
import org.springmodules.template.engine.AbstractTemplateEngine;
import org.springmodules.template.engine.velocity.extended.ExtendedResourceManager;
import org.springmodules.template.engine.velocity.extended.ExtendedVelocityEngine;
import org.springmodules.template.engine.velocity.extended.SpecialTemplate;

/**
 * @author Uri Boness
 */
public class VelocityTemplateEngine extends AbstractTemplateEngine implements InitializingBean, ResourceLoaderAware {

    private ExtendedVelocityEngine engine;

    private ResourceLoader resourceLoader;

    private Properties configuration;

    public VelocityTemplateEngine() {
        this(new DefaultResourceLoader());
    }

    public VelocityTemplateEngine(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public Template createTemplate(Resource resource, String encoding) {
//        return new VelocityTemplate(resource, encoding, engine);
        SpecialTemplate template = engine.getSpecialTemplate(resource, encoding);
        return new SpecialVelocityTemplate(template, resource.getDescription());
    }

    public void afterPropertiesSet() throws Exception {
        SpringResourceLoaderResourceLoader loader = new SpringResourceLoaderResourceLoader(resourceLoader);
        ExtendedResourceManager resourceManager = new ExtendedResourceManager(loader);
        engine = new ExtendedVelocityEngine(resourceManager);
        if (configuration != null) {
            engine.init(configuration);
        } else {
            engine.init();
        }
    }

    //============================================== Setter/Getter =====================================================

    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public void setConfiguration(Properties configuration) {
        this.configuration = configuration;
    }

}
