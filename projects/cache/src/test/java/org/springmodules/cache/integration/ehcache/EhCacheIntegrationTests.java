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

import org.springmodules.cache.integration.AbstractIntegrationTests;
import org.springmodules.cache.integration.KeyAndModelListCachingListener.KeyAndModel;
import org.springmodules.cache.provider.ehcache.EhCacheCachingModel;

/**
 * <p>
 * Template for test cases that verify that the caching module works correctly
 * inside a Spring bean context when using EHCache as the cache provider.
 * </p>
 * 
 * @author Alex Ruiz
 */
public final class EhCacheIntegrationTests extends AbstractIntegrationTests {

  private CacheManager cacheManager;

  /**
   * @see AbstractIntegrationTests#assertCacheWasFlushed()
   */
  protected void assertCacheWasFlushed() throws Exception {
    setUpCacheManager();
    int index = 0;
    Element element = getCacheElement(index);
    assertCacheEntryFromCacheIsNull(element, getKeyAndModel(index).key);
  }

  /**
   * @see AbstractIntegrationTests#assertObjectWasCached(Object, int)
   */
  protected void assertObjectWasCached(Object expectedCachedObject,
      int keyAndModelIndex) throws Exception {
    setUpCacheManager();
    Element element = getCacheElement(keyAndModelIndex);
    assertEquals(expectedCachedObject, element.getValue());
  }

  protected void tearDown() {
    //if (cacheManager == null) setUpCacheManager();
    if (cacheManager != null) cacheManager.shutdown();
  }

  private Element getCacheElement(int keyAndModelIndex) throws Exception {
    KeyAndModel keyAndModel = getKeyAndModel(keyAndModelIndex);
    EhCacheCachingModel model = (EhCacheCachingModel)keyAndModel.model;
    Cache cache = cacheManager.getCache(model.getCacheName());
    return cache.get(keyAndModel.key);
  }

  private void setUpCacheManager() {
    cacheManager = (CacheManager)cacheManager();
  }
}