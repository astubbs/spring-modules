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

import java.io.Serializable;
import java.util.Map;

import org.apache.jcs.engine.behavior.ICacheElement;
import org.apache.jcs.engine.control.CompositeCache;
import org.apache.jcs.engine.control.CompositeCacheManager;
import org.apache.jcs.engine.control.group.GroupAttrName;
import org.apache.jcs.engine.control.group.GroupId;
import org.springmodules.cache.integration.AbstractIntegrationTests;
import org.springmodules.cache.provider.jcs.JcsProfile;

/**
 * <p>
 * Template for test cases that verify that the caching module works correctly
 * inside a Spring bean context when using JCS as the cache provider.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.8 $ $Date: 2005/09/06 01:41:52 $
 */
public abstract class AbstractJcsIntegrationTests extends
    AbstractIntegrationTests {

  private static final String CACHE_GROUP = "testGroup";

  private static final String CACHE_NAME = "testCache";

  /**
   * JCS cache manager.
   */
  private CompositeCacheManager cacheManager;

  public AbstractJcsIntegrationTests() {
    super();
  }

  /**
   * @see AbstractIntegrationTests#assertCacheWasFlushed()
   */
  protected void assertCacheWasFlushed() throws Exception {
    Serializable key = getGeneratedKey(0);
    ICacheElement cacheEntry = getCacheElement(key);

    assertCacheEntryFromCacheIsNull(cacheEntry, key);
  }

  /**
   * @see AbstractIntegrationTests#assertCorrectCacheProfileConfiguration(Map)
   */
  protected void assertCorrectCacheProfileConfiguration(Map cacheProfiles) {
    JcsProfile expected = new JcsProfile();
    expected.setGroup(CACHE_GROUP);
    expected.setCacheName(CACHE_NAME);

    String cacheProfileId = "test";
    Object actual = cacheProfiles.get(cacheProfileId);

    assertEqualCacheProfiles(expected, actual, cacheProfileId);
  }

  /**
   * @see AbstractIntegrationTests#assertObjectWasCached(Object, int)
   */
  protected void assertObjectWasCached(Object expectedCachedObject, int keyIndex)
      throws Exception {
    Serializable key = getGeneratedKey(keyIndex);
    ICacheElement cacheElement = this.getCacheElement(key);

    Object actualCachedObject = cacheElement.getVal();

    assertEqualCachedObjects(expectedCachedObject, actualCachedObject);
  }

  private ICacheElement getCacheElement(Serializable key) {
    CompositeCache cache = this.cacheManager.getCache(CACHE_NAME);
    String group = CACHE_GROUP;
    GroupId groupId = new GroupId(CACHE_NAME, group);
    GroupAttrName groupAttrName = new GroupAttrName(groupId, key);

    ICacheElement cacheElement = cache.get(groupAttrName);
    return cacheElement;
  }

  /**
   * @see org.springframework.test.AbstractDependencyInjectionSpringContextTests#onSetUp()
   */
  protected final void onSetUp() throws Exception {
    this.cacheManager = (CompositeCacheManager) this.applicationContext
        .getBean("cacheManager");
  }

}