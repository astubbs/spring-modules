/* 
 * Created on Feb 20, 2006
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 * Copyright @2006 the original author or authors.
 */
package org.springmodules.cache.config;

import java.util.List;
import java.util.Map;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.RuntimeBeanReference;

/**
 * <p>
 * Stores properties that are common to all configuration strategies.
 * </p>
 * 
 * @author Alex Ruiz
 */
public final class CacheSetupStrategyPropertySource {

  private RuntimeBeanReference cacheProviderFacade;

  private List cachingListeners;

  private Map cachingModels;

  private Map flushingModels;

  /**
   * Constructor.
   * 
   * @param newCacheProviderFacade
   *          a reference to the cache provider facade
   * @param newCachingListeners
   *          a list of caching listeners
   * @param newCachingModels
   *          a list of caching models
   * @param newFlushingModels
   *          a list of flushing models
   */
  public CacheSetupStrategyPropertySource(
      RuntimeBeanReference newCacheProviderFacade, List newCachingListeners,
      Map newCachingModels, Map newFlushingModels) {
    super();
    cacheProviderFacade = newCacheProviderFacade;
    cachingListeners = newCachingListeners;
    cachingModels = newCachingModels;
    flushingModels = newFlushingModels;
  }

  /**
   * Returns the properties specified by:
   * <ul>
   * <li><code>{@link #getCacheProviderFacade()}</code></li>
   * <li><code>{@link #getCachingListeners()}</code></li>
   * <li><code>{@link #getCachingModels()}</code></li>
   * <li><code>{@link #getFlushingModels()}</code></li>
   * </ul>
   * 
   * @return all the properties stored in this object.
   */
  public MutablePropertyValues getAllProperties() {
    MutablePropertyValues allPropertyValues = new MutablePropertyValues();
    allPropertyValues.addPropertyValue(getCacheProviderFacade());
    allPropertyValues.addPropertyValue(getCachingListeners());
    allPropertyValues.addPropertyValue(getCachingModels());
    allPropertyValues.addPropertyValue(getFlushingModels());

    return allPropertyValues;
  }

  public PropertyValue getCacheProviderFacade() {
    return new PropertyValue("cacheProviderFacade", cacheProviderFacade);
  }

  public PropertyValue getCachingListeners() {
    return new PropertyValue("cachingListeners", cachingListeners);
  }

  public PropertyValue getCachingModels() {
    return new PropertyValue("cachingModels", cachingModels);
  }

  public PropertyValue getFlushingModels() {
    return new PropertyValue("flushingModels", flushingModels);
  }
}
