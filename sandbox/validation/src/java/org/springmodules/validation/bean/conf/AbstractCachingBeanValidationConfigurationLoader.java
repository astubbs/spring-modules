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

import java.util.HashMap;
import java.util.Map;

/**
 * A base class for {@link BeanValidationConfigurationLoader} implementations which provides caching functionality.
 * Bean validation configuration that are loaded from loaders that extend this are automatically being cached. The
 * caching is also applied for the {@link #supports(Class)} method.
 *
 * @author Uri Boness
 */
public abstract class AbstractCachingBeanValidationConfigurationLoader implements BeanValidationConfigurationLoader {

    private Map configurationByClass;

    private Map supportedByClass;

    /**
     * Creates a new AbstractCachingBeanValidationConfigurationLoader.
     */
    public AbstractCachingBeanValidationConfigurationLoader() {
        configurationByClass = new HashMap();
        supportedByClass = new HashMap();
    }

    /**
     * Tries to fetch the bean validation configuration for the given class from the cache, if it fails, the call is
     * delegated to {@link #doLoadConfiguration(Class)} and the result is cached and returned.
     *
     * @see BeanValidationConfigurationLoader#loadConfiguration(Class)
     */
    public final BeanValidationConfiguration loadConfiguration(Class clazz) {
        BeanValidationConfiguration configuration = (BeanValidationConfiguration)configurationByClass.get(clazz);
        if (configuration != null) {
            return configuration;
        }
        configuration = doLoadConfiguration(clazz);
        configurationByClass.put(clazz, configuration);
        return configuration;
    }

    /**
     * Tries to determine whether this class is supported from the internal cache. If it fails, the call is delegated
     * to {@link #doSupports(Class)}, and the result is cached and returned.
     *
     * @see BeanValidationConfigurationLoader#supports(Class)
     */
    public final boolean supports(Class clazz) {
        if (supportedByClass.containsKey(clazz)) {
            return ((Boolean)supportedByClass.get(clazz)).booleanValue();
        }
        if (configurationByClass.get(clazz) != null) {
            return true;
        }
        boolean supported = doSupports(clazz);
        supportedByClass.put(clazz, (supported) ? Boolean.TRUE : Boolean.FALSE);
        return supported;
    }

    /**
     * Loads the bean validation configuration for the given class.
     *
     * @param clazz The class for which the bean validation configuration should be loaded.
     * @return The loaded bean validation configuration.
     * @see #loadConfiguration(Class)
     */
    protected abstract BeanValidationConfiguration doLoadConfiguration(Class clazz);

    /**
     * Determines whether this supports the given class, that is, whether it can load a bean validation
     * configuration for it.
     *
     * @param clazz The class to be checked.
     * @return <code>true</code> if this loader can load a bean validation configuration for the given class,
     *         <code>false</code> otherwise.
     * @see #supports(Class)
     */
    protected abstract boolean doSupports(Class clazz);

}
