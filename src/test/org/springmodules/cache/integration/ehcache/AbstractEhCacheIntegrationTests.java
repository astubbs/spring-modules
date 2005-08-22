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

import java.io.Serializable;
import java.util.Map;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.springmodules.cache.integration.AbstractIntegrationTests;
import org.springmodules.cache.provider.ehcache.EhCacheProfile;

/**
 * <p>
 * Template for test cases that verify that the caching module works correctly
 * inside a Spring bean context when using EHCache as the cache provider.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.5 $ $Date: 2005/08/22 03:27:55 $
 */
public abstract class AbstractEhCacheIntegrationTests extends
    AbstractIntegrationTests {

  /**
   * EHCache cache.
   */
  private Cache cache;

  public AbstractEhCacheIntegrationTests() {
    super();
  }

  /**
   * @see AbstractIntegrationTests#assertCacheWasFlushed()
   */
  protected final void assertCacheWasFlushed() throws Exception {
    Serializable key = super.getGeneratedKey(0);

    Element cacheEntry = this.cache.get(key);

    super.assertCacheEntryFromCacheIsNull(cacheEntry, key);
  }

  /**
   * @see AbstractIntegrationTests#assertCorrectCacheProfileConfiguration(Map)
   */
  protected final void assertCorrectCacheProfileConfiguration(Map cacheProfiles) {
    EhCacheProfile expected = new EhCacheProfile();
    expected.setCacheName("testCache");

    String cacheProfileId = "test";
    Object actual = cacheProfiles.get(cacheProfileId);

    super.assertEqualCacheProfiles(expected, actual, cacheProfileId);
  }

  /**
   * @see AbstractIntegrationTests#assertObjectWasCached(Object, int)
   */
  protected final void assertObjectWasCached(Object expectedCachedObject,
      int keyIndex) throws Exception {
    Serializable key = super.getGeneratedKey(keyIndex);

    // get the cache entry stored under the key we got.
    Element cachedElement = this.cache.get(key);
    Object actualCachedObject = cachedElement.getValue();

    super.assertEqualCachedObjects(expectedCachedObject, actualCachedObject);
  }

  /**
   * @see org.springframework.test.AbstractDependencyInjectionSpringContextTests#onSetUp()
   */
  protected final void onSetUp() throws Exception {
    super.onSetUp();

    CacheManager cacheManager = (CacheManager) super.applicationContext
        .getBean("cacheManager");

    this.cache = cacheManager.getCache("testCache");
  }
}