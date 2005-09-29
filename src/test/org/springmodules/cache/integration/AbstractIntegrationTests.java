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
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;
import org.springframework.util.StringUtils;
import org.springmodules.cache.interceptor.caching.CachingAspectSupport;
import org.springmodules.cache.provider.AbstractCacheProviderFacade;

/**
 * <p>
 * Template for test cases that verify that caching module works correctly
 * inside a Spring bean context.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.13 $ $Date: 2005/09/29 01:21:52 $
 */
public abstract class AbstractIntegrationTests extends
    AbstractDependencyInjectionSpringContextTests {

  /**
   * Listener that stores the keys used to store objects in the cache. This
   * listener is used to verify that objects are being cached.
   */
  private KeyCollectionListener entryStoredListener;

  /**
   * Bean managed by the Spring bean context. Should be advised by the
   * interceptors that perform caching and flush the cache.
   */
  private CacheableService target;

  public AbstractIntegrationTests() {
    super();
  }

  protected final void assertCacheEntryFromCacheIsNull(Object cacheEntry,
      Serializable key) {
    assertNull("There should not be any object cached under the key <" + key
        + ">", cacheEntry);
  }

  protected abstract void assertCacheWasFlushed() throws Exception;

  /**
   * Asserts that the cache models were configured correctly in the Spring bean
   * context.
   * 
   * @param cacheModules
   *          the cache modules to verify. It is an unmodifiable
   *          <code>Map</code>.
   */
  protected abstract void assertCorrectCacheModelConfiguration(Map cacheModules);

  protected final void assertEqualCachedObjects(Object expected, Object actual) {
    assertEquals("<Cached object>", expected, actual);
  }

  protected final void assertEqualCacheModules(Object expected, Object actual,
      String cacheModelId) {
    assertEquals("<Cache model with id '" + cacheModelId + "'>", expected,
        actual);
  }

  /**
   * Asserts that the given object was cached.
   * 
   * @param expectedCachedObject
   *          the object that should have been cached.
   * @param keyIndex
   *          the index of the key stored in <code>entryStoredListener</code>.
   */
  protected abstract void assertObjectWasCached(Object expectedCachedObject,
      int keyIndex) throws Exception;

  protected final KeyCollectionListener getEntryStoredListener() {
    return this.entryStoredListener;
  }

  protected final Serializable getGeneratedKey(int index) {
    List generatedKeys = this.entryStoredListener.getGeneratedKeys();

    Serializable key = (Serializable) generatedKeys.get(index);
    return key;
  }

  private void setUpEntryStoredListener() {
    this.entryStoredListener = (KeyCollectionListener) super.applicationContext
        .getBean("listener");
  }

  private void setUpTarget() {
    this.target = (CacheableService) super.applicationContext
        .getBean("cacheableService");
  }

  /**
   * Verifies that the cache models were configured correctly in the Spring bean
   * context.
   */
  public final void testCacheModelConfiguration() {
    // get the cache models.
    AbstractCacheProviderFacade cacheProviderFacade = (AbstractCacheProviderFacade) super.applicationContext
        .getBean("cacheProvider");
    Map cacheModels = cacheProviderFacade.getCacheModels();

    // there should be cache models.
    assertNotNull(cacheModels);
    assertFalse("The map of cache models should not be empty", cacheModels
        .isEmpty());

    Map unmodifiableCacheModelMap = Collections.unmodifiableMap(cacheModels);

    assertCorrectCacheModelConfiguration(unmodifiableCacheModelMap);
  }

  /**
   * Verifies that the caching and the cache-flushing are working correctly.
   */
  public final void testCachingAndCacheFlushing() throws Exception {
    setUpTarget();
    setUpEntryStoredListener();

    if (super.logger.isDebugEnabled()) {
      super.logger.debug("Storing in the cache...");
    }

    int nameIndex = 0;

    String cachedObject = target.getName(nameIndex);
    assertTrue("The retrieved name should not be empty", StringUtils
        .hasText(cachedObject));

    assertObjectWasCached(cachedObject, nameIndex);

    // call the same method again. This time the return value should be read
    // from the cache.
    if (super.logger.isDebugEnabled()) {
      super.logger.debug("Reading from the cache...");
    }
    cachedObject = this.target.getName(nameIndex);

    // verify that the element was cached only once. There should be only
    // one caching event registered in the listener.
    int expectedTimesObjectWasCached = 1;
    int actualTimesObjectWasCached = this.entryStoredListener
        .getGeneratedKeys().size();
    assertEquals("<Number of times the same object was cached>",
        expectedTimesObjectWasCached, actualTimesObjectWasCached);

    // call the method 'updateName(int, String)'. When executed, the cache
    // should be flushed.
    if (super.logger.isDebugEnabled()) {
      super.logger.debug("Flushing the cache ...");
    }
    this.target.updateName(nameIndex, "Rod Johnson");

    assertCacheWasFlushed();

    // NULL_ENTRY should be cached, and null should be returned.
    this.target.updateName(++nameIndex, null);
    cachedObject = this.target.getName(nameIndex);
    assertObjectWasCached(CachingAspectSupport.NULL_ENTRY, nameIndex);

    assertNull(cachedObject);
  }
}