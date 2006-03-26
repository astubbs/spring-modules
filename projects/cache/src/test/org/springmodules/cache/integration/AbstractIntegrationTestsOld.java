/* 
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

package org.springmodules.cache.integration;

import java.io.Serializable;
import java.util.List;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;
import org.springframework.util.StringUtils;

import org.springmodules.cache.integration.KeyAndModelListCachingListener.KeyAndModel;
import org.springmodules.cache.interceptor.caching.AbstractCachingInterceptor;
import org.springmodules.cache.provider.PathUtils;

/**
 * <p>
 * Template for test cases that verify that caching module works correctly
 * inside a Spring bean context.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public abstract class AbstractIntegrationTestsOld extends
    AbstractDependencyInjectionSpringContextTests {

  /**
   * Id used to reference the cache manager in the Spring application context.
   */
  protected static final String CACHE_MANAGER_BEAN_ID = "cacheManager";

  protected static final String CACHING_LISTENER_BEAN_ID = "cachingListener";

  protected static final String SIMPLE_CONFIG_CACHE_MANAGER_BEAN_ID = "cacheProvider.cacheManager";

  protected static final String TARGET_BEAN_ID = "cacheableService";

  /**
   * Listener that stores the keys used to store objects in the cache. This
   * listener is used to verify that objects are being cached.
   */
  private KeyAndModelListCachingListener cachingListener;

  /**
   * Bean managed by the Spring bean context. Should be advised by the
   * interceptors that perform caching and flush the cache.
   */
  private CacheableService target;

  public AbstractIntegrationTestsOld() {
    super();
  }

  /**
   * Verifies that the caching and the cache-flushing are working correctly.
   */
  public final void testCachingAndCacheFlushing() throws Exception {
    cachingListener = (KeyAndModelListCachingListener) applicationContext
        .getBean(CACHING_LISTENER_BEAN_ID);

    target = (CacheableService) applicationContext.getBean(TARGET_BEAN_ID);

    logger.debug("Storing in the cache...");
    int nameIndex = 0;

    String cachedObject = target.getName(nameIndex);
    assertTrue("The retrieved name should not be empty", StringUtils
        .hasText(cachedObject));

    assertObjectWasCached(cachedObject, nameIndex);

    // call the same method again. This time the return value should be read
    // from the cache.
    logger.debug("Reading from the cache...");
    cachedObject = target.getName(nameIndex);

    // verify that the element was cached only once. There should be only
    // one caching event registered in the listener.
    int expectedTimesObjectWasCached = 1;
    int actualTimesObjectWasCached = cachingListener.getKeyAndModelPairs()
        .size();
    assertEquals("<Number of times the same object was cached>",
        expectedTimesObjectWasCached, actualTimesObjectWasCached);

    // call the method 'updateName(int, String)'. When executed, the cache
    // should be flushed.
    logger.debug("Flushing the cache ...");
    target.updateName(nameIndex, "Rod Johnson");

    assertCacheWasFlushed();

    // NULL_ENTRY should be cached, and null should be returned.
    target.updateName(++nameIndex, null);
    cachedObject = target.getName(nameIndex);
    assertObjectWasCached(AbstractCachingInterceptor.NULL_ENTRY, nameIndex);

    assertNull(cachedObject);
  }

  protected final void assertCacheEntryFromCacheIsNull(Object cacheEntry,
      Serializable key) {
    assertNull("There should not be any object stored under the key <"
        + StringUtils.quoteIfString(key) + ">", cacheEntry);
  }

  protected abstract void assertCacheWasFlushed() throws Exception;

  /**
   * Asserts that the given object was cached.
   * 
   * @param expectedCachedObject
   *          the object that should have been cached.
   * @param keyAndModelIndex
   *          the index of the key/model stored in <code>cachingListener</code>.
   * @see KeyAndModelListCachingListener
   */
  protected abstract void assertObjectWasCached(Object expectedCachedObject,
      int keyAndModelIndex) throws Exception;

  protected String getCacheManagerBeanId() {
    return CACHE_MANAGER_BEAN_ID;
  }

  protected final KeyAndModelListCachingListener getCachingListener() {
    return cachingListener;
  }

  protected abstract String[] getConfigFileNames();

  /**
   * @see AbstractDependencyInjectionSpringContextTests#getConfigLocations()
   */
  protected final String[] getConfigLocations() {
    String[] configFileNames = getConfigFileNames();
    int count = configFileNames.length;

    String resourcePath = "classpath:"
        + PathUtils.getPackageNameAsPath(getClass());

    String[] configLocations = new String[count];
    for (int i = 0; i < count; i++) {
      String configLocation = configFileNames[i];
      if (!configLocation.startsWith("classpath:")) {
        configLocation = resourcePath + "/" + configLocation;
      }
      configLocations[i] = configLocation;
    }

    return configLocations;
  }

  protected final KeyAndModel getKeyAndModel(int index) {
    List keyAndModelPairs = cachingListener.getKeyAndModelPairs();
    KeyAndModel keyModel = (KeyAndModel) keyAndModelPairs.get(index);
    return keyModel;
  }

}