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

import java.io.Serializable;
import java.util.Map;

import org.jboss.cache.TreeCache;
import org.springmodules.cache.integration.AbstractIntegrationTests;
import org.springmodules.cache.provider.jboss.JbossCacheProfile;

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
    AbstractIntegrationTests {

  protected static final String CACHE_APPLICATION_CONTEXT_PATH = "**/jbosscacheApplicationContext.xml";

  private static final String CACHE_NODE_FQN = "a/b/c";

  /**
   * JBossCache cache manager.
   */
  private TreeCache cacheManager;

  public AbstractJbossCacheIntegrationTestCases() {
    super();
  }

  /**
   * @see AbstractIntegrationTests#assertCacheWasFlushed()
   */
  protected void assertCacheWasFlushed() throws Exception {
    Serializable cacheKey = getGeneratedKey(0);
    assertNull(cacheManager.get(CACHE_NODE_FQN, cacheKey));
  }

  /**
   * @see AbstractIntegrationTests#assertCorrectCacheProfileConfiguration(Map)
   */
  protected void assertCorrectCacheProfileConfiguration(Map cacheProfiles) {
    JbossCacheProfile expected = new JbossCacheProfile();
    expected.setNodeFqn(CACHE_NODE_FQN);

    String cacheProfileId = "test";
    Object actual = cacheProfiles.get(cacheProfileId);

    assertEqualCacheProfiles(expected, actual, cacheProfileId);
  }

  /**
   * @see AbstractIntegrationTests#assertObjectWasCached(Object, int)
   */
  protected void assertObjectWasCached(Object expectedCachedObject, int keyIndex)
      throws Exception {
    Serializable cacheKey = getGeneratedKey(keyIndex);
    Object actualCachedObject = cacheManager.get(CACHE_NODE_FQN, cacheKey);
    assertEqualCachedObjects(expectedCachedObject, actualCachedObject);
  }

  /**
   * @see org.springframework.test.AbstractDependencyInjectionSpringContextTests#onSetUp()
   */
  protected final void onSetUp() throws Exception {
    // get the cache administrator from the Spring bean context.
    cacheManager = (TreeCache) applicationContext.getBean("cacheManager");
  }
}