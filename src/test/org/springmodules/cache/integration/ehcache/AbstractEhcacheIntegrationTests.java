/* 
 * Created on Jan 18, 2005
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
 * Copyright @2005 the original author or authors.
 */

package org.springmodules.cache.integration.ehcache;

import java.util.List;
import java.util.Map;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.springmodules.cache.integration.AbstractIntegrationTests;
import org.springmodules.cache.integration.KeyCollectionListener;
import org.springmodules.cache.key.CacheKey;
import org.springmodules.cache.provider.ehcache.EhcacheCacheProfile;

/**
 * <p>
 * Template for integration tests that verify that the caching and
 * cache-flushing work correctly with EHCache using a Spring bean context.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.2 $ $Date: 2005/05/15 00:45:11 $
 */
public abstract class AbstractEhcacheIntegrationTests extends
    AbstractIntegrationTests {

  /**
   * EHCache cache managed by the Spring context. Caching and cache-flushing are
   * executed using this cache.
   */
  private Cache cache;

  /**
   * Constructor.
   */
  public AbstractEhcacheIntegrationTests() {
    super();
  }

  /**
   * @see AbstractIntegrationTests#assertCacheWasFlushed()
   */
  protected final void assertCacheWasFlushed() throws Exception {

    KeyCollectionListener entryStoredListener = super.getEntryStoredListener();
    List generatedKeys = entryStoredListener.getGeneratedKeys();

    // get the key that supposedly must have been used to store the entry in the
    // cache.
    CacheKey cacheKey = (CacheKey) generatedKeys.get(0);

    // get the cache entry stored under the key we got.
    Element cachedElement = this.cache.get(cacheKey);

    assertNull("There should not be any object cached under the key '"
        + cacheKey + "'", cachedElement);
  }

  /**
   * @see AbstractIntegrationTests#assertCorrectCacheProfileConfiguration(Map)
   */
  protected final void assertCorrectCacheProfileConfiguration(Map cacheProfiles) {

    // cache profile expected to be in the map of cache profiles.
    EhcacheCacheProfile expectedTestProfile = new EhcacheCacheProfile();
    expectedTestProfile.setCacheName("testCache");

    // verify that the expected cache profile exists in the map.
    String cacheProfileId = "test";
    Object actualTestProfile = cacheProfiles.get(cacheProfileId);
    assertEquals("<Cache profile with id '" + cacheProfileId + "'>",
        expectedTestProfile, actualTestProfile);
  }

  /**
   * @see AbstractIntegrationTests#assertObjectWasCached(Object, int)
   */
  protected final void assertObjectWasCached(Object expectedCachedObject,
      int keyIndex) throws Exception {

    KeyCollectionListener entryStoredListener = super.getEntryStoredListener();
    List generatedKeys = entryStoredListener.getGeneratedKeys();

    // get the key that supposedly must have been used to store the entry in the
    // cache.
    CacheKey cacheKey = (CacheKey) generatedKeys.get(keyIndex);

    // get the cache entry stored under the key we got.
    Element cachedElement = this.cache.get(cacheKey);
    Object actualCachedObject = cachedElement.getValue();

    assertEquals("<Cached object>", expectedCachedObject, actualCachedObject);
  }

  /**
   * @see org.springframework.test.AbstractDependencyInjectionSpringContextTests#onSetUp()
   */
  protected final void onSetUp() throws Exception {
    super.onSetUp();

    // get the cache manager from the Spring bean context.
    CacheManager cacheManager = (CacheManager) super.applicationContext
        .getBean("cacheManager");

    // get the cache from the cache manager.
    this.cache = cacheManager.getCache("testCache");
  }
}