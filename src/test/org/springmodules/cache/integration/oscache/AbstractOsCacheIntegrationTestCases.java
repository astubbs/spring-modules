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

import java.io.Serializable;
import java.util.Map;

import org.springmodules.cache.integration.AbstractIntegrationTests;
import org.springmodules.cache.provider.oscache.OsCacheProfile;

import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;

/**
 * <p>
 * Template for test cases that verify that the caching module works correctly
 * inside a Spring bean context when using OSCache as the cache provider.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.6 $ $Date: 2005/09/06 01:41:43 $
 */
public abstract class AbstractOsCacheIntegrationTestCases extends
    AbstractIntegrationTests {

  /**
   * OSCache cache administrator.
   */
  private GeneralCacheAdministrator cacheAdministrator;

  public AbstractOsCacheIntegrationTestCases() {
    super();
  }

  /**
   * @see AbstractIntegrationTests#assertCacheWasFlushed()
   */
  protected void assertCacheWasFlushed() {
    Serializable cacheKey = getGeneratedKey(0);
    String key = cacheKey.toString();

    try {
      this.cacheAdministrator.getFromCache(key);
      fail("There should not be any object cached under the key '" + cacheKey
          + "'");
    } catch (NeedsRefreshException needsRefreshException) {
      // expecting this exception.
    }
  }

  /**
   * @see AbstractIntegrationTests#assertCorrectCacheProfileConfiguration(Map)
   */
  protected void assertCorrectCacheProfileConfiguration(Map cacheProfiles) {
    OsCacheProfile expected = new OsCacheProfile();
    expected.setGroups("testGroup");
    expected.setRefreshPeriod(1);

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
    String key = cacheKey.toString();

    Object actualCachedObject = this.cacheAdministrator.getFromCache(key);

    assertEqualCachedObjects(expectedCachedObject, actualCachedObject);
  }

  /**
   * @see org.springframework.test.AbstractDependencyInjectionSpringContextTests#onSetUp()
   */
  protected final void onSetUp() throws Exception {
    // get the cache administrator from the Spring bean context.
    this.cacheAdministrator = (GeneralCacheAdministrator) this.applicationContext
        .getBean("cacheManager");
  }
}