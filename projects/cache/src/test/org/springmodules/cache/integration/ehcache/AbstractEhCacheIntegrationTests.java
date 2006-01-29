/* 
 * Created on Sep 22, 2004
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
package org.springmodules.cache.integration.ehcache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.springmodules.cache.integration.AbstractCacheIntegrationTests;
import org.springmodules.cache.integration.KeyAndModelListCachingListener.KeyAndModel;
import org.springmodules.cache.provider.PathUtils;
import org.springmodules.cache.provider.ehcache.EhCacheCachingModel;

/**
 * <p>
 * Template for test cases that verify that the caching module works correctly
 * inside a Spring bean context when using EHCache as the cache provider.
 * </p>
 * 
 * @author Alex Ruiz
 */
public abstract class AbstractEhCacheIntegrationTests extends
    AbstractCacheIntegrationTests {

  /**
   * Spring file specifying the configuration of the cache manager.
   */
  protected static final String CACHE_CONFIG = CLASSPATH
      + PathUtils.getPackageNameAsPath(AbstractEhCacheIntegrationTests.class)
      + "/ehCacheContext.xml";

  /**
   * Spring file specifying the configuration of the cache manager (simplified
   * format).
   */
  protected static final String SIMPLE_CACHE_CONFIG = CLASSPATH
      + PathUtils.getPackageNameAsPath(AbstractEhCacheIntegrationTests.class)
      + "/ehCacheSimpleConfigContext.xml";

  private CacheManager cacheManager;

  /**
   * @see AbstractCacheIntegrationTests#assertCacheWasFlushed()
   */
  protected final void assertCacheWasFlushed() throws Exception {
    int index = 0;
    Element element = getCacheElement(index);
    assertCacheEntryFromCacheIsNull(element, getKeyAndModel(index).key);
  }

  /**
   * @see AbstractCacheIntegrationTests#assertObjectWasCached(Object, int)
   */
  protected final void assertObjectWasCached(Object expectedCachedObject,
      int keyAndModelIndex) throws Exception {
    Element element = getCacheElement(keyAndModelIndex);
    assertEquals(expectedCachedObject, element.getValue());
  }

  /**
   * @see org.springframework.test.AbstractDependencyInjectionSpringContextTests#onSetUp()
   */
  protected final void onSetUp() {
    cacheManager = (CacheManager) applicationContext
        .getBean(getCacheManagerBeanId());
  }

  private Element getCacheElement(int keyAndModelIndex) throws Exception {
    KeyAndModel keyAndModel = getKeyAndModel(keyAndModelIndex);
    EhCacheCachingModel model = (EhCacheCachingModel) keyAndModel.model;
    Cache cache = cacheManager.getCache(model.getCacheName());
    return cache.get(keyAndModel.key);
  }
}