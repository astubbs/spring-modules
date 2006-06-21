/*
 * Copyright 2004-2005 the original author or authors.
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

package org.springmodules.validation.bean.conf.xml;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * A registrar that registeres validation rule element handlers with a configured
 * {@link ValidationRuleElementHandlerRegistry}.
 *
 * @author Uri Boness
 */
public class ValidationRuleElementHandlerRegistrar implements InitializingBean {

    private ValidationRuleElementHandlerRegistry registry;

    private ValidationRuleElementHandler[] handlers;

    /**
     * Constructs a new ValidationRuleElementHandlerRegistrar.
     */
    public ValidationRuleElementHandlerRegistrar() {
        this(null, new ValidationRuleElementHandler[0]);
    }

    /**
     * Sets the validation rule element handlers to be registered with the registry.
     *
     * @param handlers The validation rule element handlers to be registered with the registry.
     */
    public ValidationRuleElementHandlerRegistrar(
        ValidationRuleElementHandlerRegistry registry,
        ValidationRuleElementHandler[] handlers) {

        this.handlers = handlers;
        this.registry = registry;
    }

    /**
     * Perfomes the registration.
     */
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(registry, "Required property 'registry' was not set");
        for (int i=0; i<handlers.length; i++) {
            registry.registerHandler(handlers[i]);
        }
    }


    //=============================================== Setter/Getter ====================================================

    /**
     * Sets the validation rule element handlers that will be registred with the configured registry.
     *
     * @param handlers The handlers that will be registered with the configured registry.
     */
    public void setHanlders(ValidationRuleElementHandler[] handlers) {
        this.handlers = handlers;
    }

    /**
     * Sets the registry where the configured validation rule element handlers will be registered.
     *
     * @param registry The registry where the configured validation rule element handlers will be registered.
     */
    public void setRegistry(ValidationRuleElementHandlerRegistry registry) {
        this.registry = registry;
    }

    /**
     * Sets the {@link DefaultXmlBeanValidationConfigurationLoader} of which the validation rule element handler
     * registry will be updated with the configured handlers.
     *
     * @param loader The {@link DefaultXmlBeanValidationConfigurationLoader} of which registry will be updated.
     */
    public void setConfigurationLoader(DefaultXmlBeanValidationConfigurationLoader loader) {
        setRegistry(loader.getElementHandlerRegistry());
    }

}
