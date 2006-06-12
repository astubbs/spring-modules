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
package org.springmodules.validation.bean.conf;

import org.springframework.core.io.Resource;

/**
 * A base class for all bean validation configuration loaders that are resource based (that is, load configuration from
 * files, urls, etc...)
 *
 * @author Uri Boness
 */
public abstract class AbstractResourceBasedBeanValidationConfigurationLoader
    extends AbstractCachingBeanValidationConfigurationLoader {

    private Resource[] resources;

    /**
     * Constructs a new AbstractResourceBasedBeanValidationConfigurationLoader.
     */
    public AbstractResourceBasedBeanValidationConfigurationLoader() {
        this(new Resource[0]);
    }

    /**
     * Constructs a new AbstractResourceBasedBeanValidationConfigurationLoader with the given resources.
     *
     * @param resources The resources from which this loader will load the bean validation configurations.
     */
    public AbstractResourceBasedBeanValidationConfigurationLoader(Resource[] resources) {
        this.resources = resources;
    }

    /**
     * Loads the bean validation configuration for the given class from the configured resources.
     *
     * @see AbstractResourceBasedBeanValidationConfigurationLoader#doLoadConfiguration(Class)
     */
    public final BeanValidationConfiguration doLoadConfiguration(Class clazz) {
        for (int i=0; i<resources.length; i++) {
            BeanValidationConfiguration configuration = loadConfiguration(clazz, resources[i]);
            if (configuration != null) {
                return configuration;
            }
        }
        return null;
    }

    /**
     * Determines whether the given class is supported by this loader.
     *
     * @see AbstractResourceBasedBeanValidationConfigurationLoader#doSupports(Class)
     */
    public final boolean doSupports(Class clazz) {
        for (int i=0; i<resources.length; i++) {
            if (supports(clazz, resources[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * Loads the bean validation configuration for the given class form the given resource. If the configuration was
     * not found in the resource <code>null</code> is returned.
     *
     * @param clazz The class for which the bean validation configuration should be loaded.
     * @param resource The resource from which the bean validation configuration should be loaded.
     * @return The loaded bean validation configuration if found, or <code>null</code> otherwise.
     */
    protected abstract BeanValidationConfiguration loadConfiguration(Class clazz, Resource resource);

    /**
     * Determines whether the bean validation configuration for the given class can be loaded from the given resource.
     *
     * @param clazz The class for which the bean validation configuration should be loaded.
     * @param resource The resource to be checked.
     * @return <code>true</code> if the bean validation configuration for the given class can be loaded from the given
     *         resource, <code>false</code> otherwise.
     */
    protected abstract boolean supports(Class clazz, Resource resource);

    //=============================================== Setter/Getter ====================================================

    /**
     * Returns the resouces this loader uses to load bean validation configurations.
     *
     * @return The resouces this loader uses to load bean validation configurations.
     */
    public Resource[] getResources() {
        return resources;
    }

    /**
     * Sets the resources this loader will use to load bean validation configurations.
     *
     * @param resources The resources this loader will use to load bean validation configurations.
     */
    public void setResources(Resource[] resources) {
        this.resources = resources;
    }

    /**
     * If this loader is configured with only on resource, use this method to retreive it.
     *
     * @return The single resource this loader uses to load bean validation configuration from. If the loader uses
     *         multiple resources, the first one is returned.
     */
    public Resource getResource() {
        return (resources[0] != null) ? resources[0] : null;
    }

    /**
     * Sets a single resource this loader will use to load the bean validation configurations from.
     *
     * @param resource The one resource this loader will use to load the bean validation configurations from.
     */
    public void setResource(Resource resource) {
        setResources(new Resource[] { resource });
    }

}
