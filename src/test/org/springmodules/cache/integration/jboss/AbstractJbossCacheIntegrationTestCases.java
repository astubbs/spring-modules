/* 
 * OscacheIntegrationTest.java
 * 
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

package org.springmodules.cache.integration.jboss;

import org.jboss.cache.TreeCache;
import org.springmodules.cache.integration.AbstractCacheIntegrationTests;
import org.springmodules.cache.integration.KeyAndModelListCachingListener.KeyAndModel;
import org.springmodules.cache.provider.jboss.JbossCacheCachingModel;

/**
 * <p>
 * Template for test cases that verify that the caching module works correctly
 * inside a Spring bean context when using JbossCache as the cache provider.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public abstract class AbstractJbossCacheIntegrationTestCases extends
    AbstractCacheIntegrationTests {

  protected static final String CACHE_CONFIG = "**/jbossCacheContext.xml";

  /**
   * JBossCache cache manager.
   */
  private TreeCache cacheManager;

  public AbstractJbossCacheIntegrationTestCases() {
    super();
  }

  /**
   * @see AbstractCacheIntegrationTests#assertCacheWasFlushed()
   */
  protected void assertCacheWasFlushed() throws Exception {
    int index = 0;
    Object cachedObject = getCachedObject(index);
    assertCacheEntryFromCacheIsNull(cachedObject, getKeyAndModel(index).key);
  }

  /**
   * @see AbstractCacheIntegrationTests#assertObjectWasCached(Object, int)
   */
  protected void assertObjectWasCached(Object expectedCachedObject,
      int keyAndModelIndex) throws Exception {
    Object actual = getCachedObject(keyAndModelIndex);
    assertEquals(expectedCachedObject, actual);
  }

  private Object getCachedObject(int keyAndModelIndex) throws Exception {
    KeyAndModel keyAndModel = getKeyAndModel(keyAndModelIndex);
    JbossCacheCachingModel model = (JbossCacheCachingModel) keyAndModel.model;
    return cacheManager.get(model.getNode(), keyAndModel.key);
  }

  /**
   * @see org.springframework.test.AbstractDependencyInjectionSpringContextTests#onSetUp()
   */
  protected final void onSetUp() throws Exception {
    // get the cache administrator from the Spring bean context.
    cacheManager = (TreeCache) applicationContext
        .getBean(CACHE_MANAGER_BEAN_ID);
  }
}