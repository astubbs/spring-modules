/* 
 * Created on Oct 31, 2004
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
 * Copyright @2004 the original author or authors.
 */

package org.springmodules.cache.integration.annotations;

import org.springmodules.cache.integration.ehcache.AbstractEhCacheIntegrationTests;

/**
 * <p>
 * Verifies that the caching module works correctly when using EHCache as the
 * cache provider and the caching services are declared using a
 * <code>{@link org.springmodules.cache.interceptor.proxy.CacheProxyFactoryBean}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public final class EhCacheSimplifiedAnnotationIntegrationTests extends
    AbstractEhCacheIntegrationTests {

  protected String getCacheManagerBeanId() {
    return SIMPLE_CONFIG_CACHE_MANAGER_BEAN_ID;
  }

  /**
   * @see org.springmodules.cache.integration.AbstractCacheIntegrationTests#getConfigFileNames()
   */
  @Override
  protected String[] getConfigFileNames() {
    return new String[] { SIMPLE_CACHE_CONFIG,
        "simpleConfigAnnotationContext.xml" };
  }
}