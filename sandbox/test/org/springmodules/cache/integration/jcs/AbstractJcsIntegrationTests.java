/* 
 * JcsIntegrationTest.java
 * 
 * Created on Oct 22, 2004
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

package org.springmodules.cache.integration.jcs;

import java.util.List;
import java.util.Map;

import org.apache.jcs.engine.behavior.ICacheElement;
import org.apache.jcs.engine.control.CompositeCache;
import org.apache.jcs.engine.control.CompositeCacheManager;
import org.apache.jcs.engine.control.group.GroupAttrName;
import org.apache.jcs.engine.control.group.GroupId;
import org.springmodules.cache.integration.AbstractIntegrationTests;
import org.springmodules.cache.integration.KeyCollectionListener;
import org.springmodules.cache.key.CacheKey;
import org.springmodules.cache.provider.jcs.JcsProfile;

/**
 * <p>
 * Template for integration tests that verify that the caching and
 * cache-flushing work correctly with JCS using a Spring bean context.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.3 $ $Date: 2005/05/19 02:20:08 $
 */
public abstract class AbstractJcsIntegrationTests extends
    AbstractIntegrationTests {

  /**
   * JCS cache manager managed by the Spring context. Caching and cache-flushing
   * are executed using this cache.
   */
  private CompositeCacheManager cacheManager;

  /**
   * Constructor.
   */
  public AbstractJcsIntegrationTests() {
    super();
  }

  /**
   * @see org.springframework.test.AbstractDependencyInjectionSpringContextTests#onSetUp()
   */
  protected final void onSetUp() throws Exception {
    super.onSetUp();

    // get the cache manager from the Spring bean context.
    this.cacheManager = (CompositeCacheManager) super.applicationContext
        .getBean("cacheManager");
  }

  /**
   * @see AbstractIntegrationTests#assertCacheWasFlushed()
   */
  protected void assertCacheWasFlushed() throws Exception {

    KeyCollectionListener entryStoredListener = super.getEntryStoredListener();
    List generatedKeys = entryStoredListener.getGeneratedKeys();

    // get the key that supposedly must have been used to store the entry in the
    // cache.
    CacheKey cacheKey = (CacheKey) generatedKeys.get(0);

    // get the cache entry stored under the key we got.
    String cacheName = "testCache";
    String group = "testGroup";
    GroupId groupId = new GroupId(cacheName, group);
    GroupAttrName groupAttrName = new GroupAttrName(groupId, cacheKey);

    CompositeCache cache = this.cacheManager.getCache(cacheName);
    ICacheElement cachedElement = cache.get(groupAttrName);

    assertNull("There should not be any object cached under the key '"
        + cacheKey + "'", cachedElement);
  }

  /**
   * @see AbstractIntegrationTests#assertCorrectCacheProfileConfiguration(Map)
   */
  protected void assertCorrectCacheProfileConfiguration(Map cacheProfiles) {

    // cache profile expected to be in the map of cache profiles.
    JcsProfile expectedTestProfile = new JcsProfile();
    expectedTestProfile.setGroup("testGroup");
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
  protected void assertObjectWasCached(Object expectedCachedObject, int keyIndex)
      throws Exception {

    KeyCollectionListener entryStoredListener = super.getEntryStoredListener();
    List generatedKeys = entryStoredListener.getGeneratedKeys();

    // get the key that supposedly must have been used to store the entry in the
    // cache.
    CacheKey cacheKey = (CacheKey) generatedKeys.get(keyIndex);

    // get the cache entry stored under the key we got.
    String cacheName = "testCache";
    String group = "testGroup";
    GroupId groupId = new GroupId(cacheName, group);
    GroupAttrName groupAttrName = new GroupAttrName(groupId, cacheKey);

    // verify that an entry exists in the cache with the key we got.
    CompositeCache cache = this.cacheManager.getCache(cacheName);
    ICacheElement cachedElement = cache.get(groupAttrName);
    Object actualCachedObject = cachedElement.getVal();

    assertEquals("<Cached object>", expectedCachedObject, actualCachedObject);
  }

}