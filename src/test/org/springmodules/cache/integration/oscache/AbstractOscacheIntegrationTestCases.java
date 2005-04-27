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

package org.springmodules.cache.integration.oscache;

import java.util.List;
import java.util.Map;

import org.springmodules.cache.integration.AbstractIntegrationTests;
import org.springmodules.cache.integration.KeyCollectionListener;
import org.springmodules.cache.key.CacheKey;
import org.springmodules.cache.provider.oscache.OscacheCacheProfile;

import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;

/**
 * <p>
 * Template for integration tests that verify that the caching and
 * cache-flushing work correctly with OSCache using a Spring bean context.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/04/27 01:41:41 $
 */
public abstract class AbstractOscacheIntegrationTestCases extends
  AbstractIntegrationTests {

  /**
   * OSCache cache administrator managed by the Spring context. Caching and
   * cache-flushing are executed using this cache.
   */
  private GeneralCacheAdministrator cacheAdministrator;

  /**
   * Constructor.
   */
  public AbstractOscacheIntegrationTestCases() {
    super();
  }

  /**
   * @see org.springframework.test.AbstractDependencyInjectionSpringContextTests#onSetUp()
   */
  protected final void onSetUp() throws Exception {
    super.onSetUp();

    // get the cache administrator from the Spring bean context.
    this.cacheAdministrator = (GeneralCacheAdministrator) super
      .applicationContext.getBean("cacheManager");
  }

  /**
   * @see AbstractIntegrationTests#assertCacheWasFlushed()
   */
  protected void assertCacheWasFlushed() {

    KeyCollectionListener entryStoredListener = super.getEntryStoredListener();
    List generatedKeys = entryStoredListener.getGeneratedKeys();

    // get the key that supposedly must have been used to store the entry in the
    // cache.
    CacheKey cacheKey = (CacheKey) generatedKeys.get(0);
    String key = cacheKey.toString();

    try {
      this.cacheAdministrator.getFromCache(key);
      fail("There should not be any object cached under the key '" + cacheKey
        + "'");
    }
    catch (NeedsRefreshException needsRefreshException) {
      // NeedsRefreshException should be thrown if the cache does not have the
      // object we are looking for.
    }
  }

  /**
   * @see AbstractIntegrationTests#assertCorrectCacheProfileConfiguration(Map)
   */
  protected void assertCorrectCacheProfileConfiguration(Map cacheProfiles) {

    // cache profile expected to be in the map of cache profiles.
    OscacheCacheProfile expectedTestProfile = new OscacheCacheProfile();
    expectedTestProfile.setGroups("testGroup");
    expectedTestProfile.setRefreshPeriod(4);

    // verify that the expected cache profile exists in the map.
    String cacheProfileId = "test";
    Object actualTestProfile = cacheProfiles.get(cacheProfileId);
    assertEquals("<Cache profile with id '" + cacheProfileId + "'>",
                 expectedTestProfile, actualTestProfile);
  }

  /**
   * @see AbstractIntegrationTests#assertObjectWasCached(String)
   */
  protected void assertObjectWasCached(String expectedCachedObject)
    throws Exception {

    KeyCollectionListener entryStoredListener = super.getEntryStoredListener();
    List generatedKeys = entryStoredListener.getGeneratedKeys();

    // get the key that supposedly must have been used to store the entry in the
    // cache.
    CacheKey cacheKey = (CacheKey) generatedKeys.get(0);
    String key = cacheKey.toString();

    // get the cache entry stored under the key we got.
    Object actualCachedObject = this.cacheAdministrator.getFromCache(key);
    assertEquals("<Cached object>", expectedCachedObject, actualCachedObject);
  }

}