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

package org.springmodules.cache.integration.oscache;

import java.io.Serializable;

import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;

import org.springframework.util.StringUtils;

import org.springmodules.cache.integration.AbstractCacheIntegrationTests;
import org.springmodules.cache.integration.KeyAndModelListCachingListener.KeyAndModel;

/**
 * <p>
 * Template for test cases that verify that the caching module works correctly
 * inside a Spring bean context when using OSCache as the cache provider.
 * </p>
 * 
 * @author Alex Ruiz
 */
public abstract class AbstractOsCacheIntegrationTestCases extends
    AbstractCacheIntegrationTests {

  protected static final String CACHE_CONFIG = "classpath:org/springmodules/cache/integration/oscache/osCacheContext.xml";

  /**
   * OSCache cache administrator.
   */
  private GeneralCacheAdministrator cacheAdministrator;

  public AbstractOsCacheIntegrationTestCases() {
    super();
  }

  /**
   * @see AbstractCacheIntegrationTests#assertCacheWasFlushed()
   */
  protected void assertCacheWasFlushed() {
    KeyAndModel keyAndModel = getKeyAndModel(0);
    Serializable key = keyAndModel.key;

    try {
      cacheAdministrator.getFromCache(key.toString());
      fail("There should not be any object cached under the key <"
          + StringUtils.quoteIfString(key) + ">");

    } catch (NeedsRefreshException needsRefreshException) {
      // expecting this exception.
    }
  }

  /**
   * @see AbstractCacheIntegrationTests#assertObjectWasCached(Object, int)
   */
  protected void assertObjectWasCached(Object expectedCachedObject,
      int keyAndModelIndex) throws Exception {
    KeyAndModel keyAndModel = getKeyAndModel(keyAndModelIndex);
    Object actual = cacheAdministrator.getFromCache(keyAndModel.key.toString());
    assertEquals(expectedCachedObject, actual);
  }

  /**
   * @see org.springframework.test.AbstractDependencyInjectionSpringContextTests#onSetUp()
   */
  protected final void onSetUp() throws Exception {
    cacheAdministrator = (GeneralCacheAdministrator) applicationContext
        .getBean(CACHE_MANAGER_BEAN_ID);
  }
}