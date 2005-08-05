/* 
 * Created on May 3, 2005
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
package org.springmodules.cache.provider.ehcache;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.springmodules.cache.provider.AbstractCacheProfileEditor;
import org.springmodules.cache.provider.CacheException;
import org.springmodules.cache.provider.CacheNotFoundException;
import org.springmodules.cache.provider.CacheProfileValidator;
import org.springmodules.cache.provider.InvalidConfigurationException;

/**
 * <p>
 * Unit Tests for <code>{@link EhCacheFacade}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.5 $ $Date: 2005/08/05 02:45:18 $
 */
public class EhCacheFacadeTests extends TestCase {

  /**
   * An EHCache Cache.
   */
  private Cache cache;

  /**
   * Key used to store/retrieve an entry of the cache.
   */
  private String cacheKey;

  /**
   * EHCache Cache Manager.
   */
  private CacheManager cacheManager;

  /**
   * Name of the EHCache to use.
   */
  private String cacheName;

  /**
   * Configuration options for the caching services.
   */
  private EhCacheProfile cacheProfile;

  /**
   * Id used by <code>{@link #ehcacheFacade}</code> to obtain a reference to
   * <code>{@link #cacheProfile}</code>.
   */
  private String cacheProfileId;

  /**
   * Primary object that is under test.
   */
  private EhCacheFacade ehcacheFacade;

  public EhCacheFacadeTests(String name) {
    super(name);
  }

  private void assertCacheExceptionIsCacheNotFoundException(
      CacheException exception) {
    assertEquals("<CacheException class>", CacheNotFoundException.class,
        exception.getClass());
  }

  private void failIfCacheNotFoundExceptionIsNotThrown() {
    fail("Expecting exception <" + CacheNotFoundException.class.getName() + ">");
  }

  private void failIfInvalidConfigurationExceptionIsNotThrown() {
    fail("Expecting exception <"
        + InvalidConfigurationException.class.getName() + ">");
  }

  /**
   * Sets up the test fixture.
   */
  protected void setUp() throws Exception {
    super.setUp();

    this.cacheKey = "key";

    this.cacheManager = CacheManager.create();
    this.cacheName = "testCache";

    this.cache = this.cacheManager.getCache(this.cacheName);

    this.cacheProfile = new EhCacheProfile();
    this.cacheProfile.setCacheName(this.cacheName);

    this.cacheProfileId = "cacheProfile";

    Map cacheProfiles = new HashMap();
    cacheProfiles.put(this.cacheProfileId, this.cacheProfile);

    this.ehcacheFacade = new EhCacheFacade();
    this.ehcacheFacade.setCacheManager(this.cacheManager);
    this.ehcacheFacade.setCacheProfiles(cacheProfiles);
  }

  protected void tearDown() throws Exception {
    super.tearDown();

    this.cacheManager.shutdown();
  }

  /**
   * Verifies that the method
   * <code>{@link EhCacheFacade#getCacheProfileEditor()}</code> returns an
   * instance of <code>{@link EhCacheProfileEditor}</code> not equal to
   * <code>null</code>.
   */
  public void testGetCacheProfileEditor() {
    AbstractCacheProfileEditor cacheProfileEditor = this.ehcacheFacade
        .getCacheProfileEditor();

    assertNotNull(cacheProfileEditor);

    Class expectedClass = EhCacheProfileEditor.class;
    Class actualClass = cacheProfileEditor.getClass();

    assertEquals("<Class of the cache profile editor>", expectedClass,
        actualClass);
  }

  /**
   * Verifies that the method
   * <code>{@link EhCacheFacade#getCacheProfileValidator()}</code> returns an
   * an instance of <code>{@link EhCacheProfileValidator}</code> not equal to
   * <code>null</code>.
   */
  public void testGetCacheProfileValidator() {
    CacheProfileValidator cacheProfileValidator = this.ehcacheFacade
        .getCacheProfileValidator();

    assertNotNull(cacheProfileValidator);

    Class expectedClass = EhCacheProfileValidator.class;
    Class actualClass = cacheProfileValidator.getClass();

    assertEquals("<Class of the cache profile validator>", expectedClass,
        actualClass);
  }

  /**
   * Verifies that the method
   * <code>{@link EhCacheFacade#onFlushCache(org.springmodules.cache.provider.CacheProfile)}</code>
   * flushes the cache specified in the given cache profile.
   */
  public void testOnFlushCache() throws Exception {
    this.cache.put(new Element(this.cacheKey, "A Value"));

    // execute the method to test.
    this.ehcacheFacade.onFlushCache(this.cacheProfile);

    Object cachedValue = this.cache.get(this.cacheKey);
    assertNull("The cache '" + this.cacheName + "' should be empty",
        cachedValue);
  }

  /**
   * Verifies that the method
   * <code>{@link EhCacheFacade#onFlushCache(org.springmodules.cache.provider.CacheProfile)}</code>
   * throws a <code>{@link CacheNotFoundException}</code> if the cache
   * specified in the given cache profile does not exist.
   */
  public void testOnFlushCacheWhenCacheIsNotFound() {
    this.cache.put(new Element(this.cacheKey, "A Value"));
    this.cacheProfile.setCacheName("NonExistingCache");

    try {
      this.ehcacheFacade.onFlushCache(this.cacheProfile);
      fail("A 'CacheNotFoundException' should have been thrown");

    } catch (CacheException exception) {
      assertEquals("<CacheException class>", CacheNotFoundException.class,
          exception.getClass());
    }
  }

  /**
   * Verifies that the method
   * <code>{@link EhCacheFacade#onGetFromCache(java.io.Serializable, org.springmodules.cache.provider.CacheProfile)}</code>
   * retrieves, from the cache specified in the given cache profile, the entry
   * stored under the given key.
   */
  public void testOnGetFromCache() throws Exception {
    String objectToStore = "An Object";
    this.cache.put(new Element(this.cacheKey, objectToStore));

    Object cachedObject = this.ehcacheFacade.onGetFromCache(this.cacheKey,
        this.cacheProfile);

    assertEquals("<Cached object>", objectToStore, cachedObject);
  }

  /**
   * Verifies that the method
   * <code>{@link EhCacheFacade#onGetFromCache(java.io.Serializable, org.springmodules.cache.provider.CacheProfile)}</code>
   * throws a <code>{@link CacheNotFoundException}</code> if the specified
   * cache does not exist.
   */
  public void testOnGetFromCacheWhenCacheIsNotFound() {
    this.cacheProfile.setCacheName("NonExistingCache");

    try {
      this.ehcacheFacade.onGetFromCache(this.cacheKey, this.cacheProfile);
      fail("Expecting exception <" + CacheNotFoundException.class.getName()
          + ">");

    } catch (CacheException exception) {
      assertEquals("<CacheException class>", CacheNotFoundException.class,
          exception.getClass());
    }
  }

  /**
   * Verifies that the method
   * <code>{@link EhCacheFacade#onGetFromCache(java.io.Serializable, org.springmodules.cache.provider.CacheProfile)}</code>
   * returns <code>null</code> if the specified key does not exist in the
   * cache.
   */
  public void testOnGetFromCacheWhenKeyIsNotFound() throws Exception {
    Object cachedObject = this.ehcacheFacade.onGetFromCache("NonExistingKey",
        this.cacheProfile);

    assertNull(cachedObject);
  }

  /**
   * Verifies that the method
   * <code>{@link EhCacheFacade#onPutInCache(java.io.Serializable, org.springmodules.cache.provider.CacheProfile, Object)}</code>
   * stores an entry in the cache specified in the given cache profile using the
   * given key.
   */
  public void testOnPutInCache() throws Exception {
    String objectToCache = "An Object";
    this.ehcacheFacade.onPutInCache(this.cacheKey, this.cacheProfile,
        objectToCache);

    Object cachedObject = this.cache.get(this.cacheKey).getValue();
    assertSame("<Cached object>", objectToCache, cachedObject);
  }

  /**
   * Verifies that the method
   * <code>{@link EhCacheFacade#onPutInCache(java.io.Serializable, org.springmodules.cache.provider.CacheProfile, Object)}</code>
   * does not store any entry in any cache if the cache specified in the given
   * cache profile does not exist.
   */
  public void testOnPutInCacheWhenCacheIsNotFound() throws Exception {
    this.cacheProfile.setCacheName("NonExistingCache");
    try {
      this.ehcacheFacade.onPutInCache(this.cacheKey, this.cacheProfile,
          "An Object");
      this.failIfCacheNotFoundExceptionIsNotThrown();

    } catch (CacheException exception) {
      this.assertCacheExceptionIsCacheNotFoundException(exception);
    }
  }

  /**
   * Verifies that the method
   * <code>{@link EhCacheFacade#onRemoveFromCache(java.io.Serializable, org.springmodules.cache.provider.CacheProfile)}</code>
   * throws a <code>{@link CacheNotFoundException}</code> if the specified
   * cache does not exist.
   */
  public void testOnRemoveFromCache() throws Exception {
    this.cache.put(new Element(this.cacheKey, "An Object"));

    this.ehcacheFacade.onRemoveFromCache(this.cacheKey, this.cacheProfile);

    Element cacheElement = this.cache.get(this.cacheKey);
    assertNull("The element with key '" + this.cacheKey
        + "' should have been removed from the cache", cacheElement);
  }

  /**
   * Verifies that the method
   * <code>{@link EhCacheFacade#onRemoveFromCache(java.io.Serializable, org.springmodules.cache.provider.CacheProfile)}</code>
   * throws a <code>{@link CacheNotFoundException}</code> if the specified
   * cache does not exist.
   */
  public void testOnRemoveFromCacheWhenCacheIsNotFound() throws Exception {
    this.cache.put(new Element(this.cacheKey, "An Object"));
    this.cacheProfile.setCacheName("NonExistingCache");

    try {
      this.ehcacheFacade.removeFromCache(this.cacheKey, this.cacheProfileId);
      this.failIfCacheNotFoundExceptionIsNotThrown();

    } catch (CacheException exception) {
      this.assertCacheExceptionIsCacheNotFoundException(exception);
    }
  }

  /**
   * Verifies that the method
   * <code>{@link EhCacheFacade#validateCacheManager()}</code> does not throw
   * any exception if the status of the Cache Manager is "Active".
   */
  public void testValidateCacheManagerWithCacheManagerEqualToActive()
      throws Exception {
    this.ehcacheFacade.validateCacheManager();
  }

  /**
   * Verifies that the method
   * <code>{@link EhCacheFacade#validateCacheManager()}</code> throws an
   * <code>{@link InvalidConfigurationException}</code> if the cache manager
   * is <code>null</code>.
   */
  public void testValidateCacheManagerWithCacheManagerEqualToNull() {
    this.ehcacheFacade.setCacheManager(null);
    try {
      this.ehcacheFacade.validateCacheManager();
      this.failIfInvalidConfigurationExceptionIsNotThrown();

    } catch (InvalidConfigurationException exception) {
      // we are expecting this exception.
    }
  }

  /**
   * Verifies that the method
   * <code>{@link EhCacheFacade#validateCacheManager()}</code> does not throw
   * any exception if the cache manager is not "alive" and the flag
   * 'failQuietlyEnabled' is <code>true</code>.
   */
  public void testValidateCacheManagerWithCacheManagerNotAliveAndFailQuietlyIsEnabled()
      throws Exception {
    this.ehcacheFacade.setFailQuietlyEnabled(true);
    this.cacheManager.shutdown();

    try {
      this.ehcacheFacade.validateCacheManager();
    } catch (Throwable throwable) {
      fail("No exception should have been thrown");
    }
  }

  /**
   * Verifies that the method
   * <code>{@link EhCacheFacade#validateCacheManager()}</code> throws an
   * <code>{@link InvalidConfigurationException}</code> if the cache manager
   * is not "alive" and the flag 'failQuietlyEnabled' is <code>false</code>.
   */
  public void testValidateCacheManagerWithCacheManagerNotAliveAndFailQuietlyNotEnabled()
      throws Exception {

    this.ehcacheFacade.setFailQuietlyEnabled(false);
    this.cacheManager.shutdown();

    try {
      this.ehcacheFacade.validateCacheManager();
      this.failIfInvalidConfigurationExceptionIsNotThrown();

    } catch (InvalidConfigurationException exception) {
      // we are expecting this exception.
    }
  }
}
