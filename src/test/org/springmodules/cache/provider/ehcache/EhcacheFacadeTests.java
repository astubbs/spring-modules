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

import org.springmodules.cache.EntryRetrievalException;
import org.springmodules.cache.provider.AbstractCacheProfileEditor;
import org.springmodules.cache.provider.CacheProfileValidator;

/**
 * <p>
 * Unit Test for <code>{@link EhcacheFacade}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.2 $ $Date: 2005/05/04 00:17:44 $
 */
public class EhcacheFacadeTests extends TestCase {

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
  private EhcacheCacheProfile cacheProfile;

  /**
   * Id used by <code>{@link #ehcacheFacade}</code> to get
   * <code>{@link #cacheProfile}</code>.
   */
  private String cacheProfileId;

  /**
   * Primary object that is under test.
   */
  private EhcacheFacade ehcacheFacade;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case to construct.
   */
  public EhcacheFacadeTests(String name) {
    super(name);
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

    this.cacheProfile = new EhcacheCacheProfile();
    this.cacheProfile.setCacheName(this.cacheName);

    this.cacheProfileId = "cacheProfile";

    Map cacheProfiles = new HashMap();
    cacheProfiles.put(this.cacheProfileId, this.cacheProfile);

    this.ehcacheFacade = new EhcacheFacade();
    this.ehcacheFacade.setCacheManager(this.cacheManager);
    this.ehcacheFacade.setCacheProfiles(cacheProfiles);
  }

  /**
   * Tears down the test fixture.
   */
  protected void tearDown() throws Exception {
    super.tearDown();

    this.cacheManager.shutdown();
  }

  /**
   * Verifies that the method
   * <code>{@link EhcacheFacade#getCacheProfileEditor()}</code> returns an
   * instance of <code>{@link EhcacheCacheProfileEditor}</code> not equal to
   * <code>null</code>.
   */
  public void testGetCacheProfileEditor() {
    AbstractCacheProfileEditor cacheProfileEditor = this.ehcacheFacade
        .getCacheProfileEditor();

    assertNotNull("The cache profile editor should not be null",
        cacheProfileEditor);

    Class expectedClass = EhcacheCacheProfileEditor.class;
    Class actualClass = cacheProfileEditor.getClass();

    assertEquals("<Class of the cache profile editor>", expectedClass,
        actualClass);
  }

  /**
   * Verifies that the method
   * <code>{@link EhcacheFacade#getCacheProfileValidator()}</code> returns an
   * an instance of <code>{@link EhcacheCacheProfileValidator}</code> not
   * equal to <code>null</code>.
   */
  public void testGetCacheProfileValidator() {
    CacheProfileValidator cacheProfileValidator = this.ehcacheFacade
        .getCacheProfileValidator();

    assertNotNull("The cache profile validator should not be null",
        cacheProfileValidator);

    Class expectedClass = EhcacheCacheProfileValidator.class;
    Class actualClass = cacheProfileValidator.getClass();

    assertEquals("<Class of the cache profile validator>", expectedClass,
        actualClass);
  }

  /**
   * Verifies that the method
   * <code>{@link EhcacheFacade#onFlushCache(org.springmodules.cache.provider.CacheProfile)}</code>
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
   * <code>{@link EhcacheFacade#onFlushCache(org.springmodules.cache.provider.CacheProfile)}</code>
   * does not flush any cache if the cache specified in the given cache profile
   * does not exist.
   */
  public void testOnFlushCacheWhenCacheIsNotFound() throws Exception {
    this.cache.put(new Element(this.cacheKey, "A Value"));

    // execute the method to test.
    this.cacheProfile.setCacheName("NonExistingCache");
    this.ehcacheFacade.onFlushCache(this.cacheProfile);

    Object cachedValue = this.cache.get(this.cacheKey);
    assertNotNull("The cache '" + this.cacheName + "' should not be empty",
        cachedValue);
  }

  /**
   * Verifies that the method
   * <code>{@link EhcacheFacade#onFlushCache(org.springmodules.cache.provider.CacheProfile)}</code>
   * does not flush any cache if the name of thec cache specified in the given
   * cache profile is empty.
   */
  public void testOnFlushCacheWhenCacheNameIsEmpty() throws Exception {
    this.cache.put(new Element(this.cacheKey, "A Value"));

    // execute the method to test.
    this.cacheProfile.setCacheName("");
    this.ehcacheFacade.onFlushCache(this.cacheProfile);

    Object cachedValue = this.cache.get(this.cacheKey);
    assertNotNull("The cache '" + this.cacheName + "' should not be empty",
        cachedValue);
  }

  /**
   * Verifies that the method
   * <code>{@link EhcacheFacade#onGetFromCache(java.io.Serializable, org.springmodules.cache.provider.CacheProfile)}</code>
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
   * <code>{@link EhcacheFacade#onGetFromCache(java.io.Serializable, org.springmodules.cache.provider.CacheProfile)}</code>
   * throws a <code>{@link EntryRetrievalException}</code> if the specified
   * cache does not exist.
   */
  public void testOnGetFromCacheWhenCacheIsNotFound() {
    // execute the method to test.
    this.cacheProfile.setCacheName("NonExistingCache");

    try {
      this.ehcacheFacade.onGetFromCache(this.cacheKey, this.cacheProfile);
      fail("We are expecting a 'EntryRetrievalException'");

    } catch (EntryRetrievalException exception) {
      // we are expecting this exception.
    }
  }

  /**
   * Verifies that the method
   * <code>{@link EhcacheFacade#onGetFromCache(java.io.Serializable, org.springmodules.cache.provider.CacheProfile)}</code>
   * returns <code>null</code> if the name of the cache, specified in the
   * given cache profile, is empty.
   */
  public void testOnGetFromCacheWhenCacheNameIsEmpty() throws Exception {
    // execute the method to test.
    this.cacheProfile.setCacheName("");

    Object cachedObject = this.ehcacheFacade.onGetFromCache(this.cacheKey,
        this.cacheProfile);

    assertNull("The retrieved object should be null", cachedObject);
  }

  /**
   * Verifies that the method
   * <code>{@link EhcacheFacade#onGetFromCache(java.io.Serializable, org.springmodules.cache.provider.CacheProfile)}</code>
   * returns <code>null</code> if the specified key does not exist in the
   * cache.
   */
  public void testOnGetFromCacheWhenKeyIsNotFound() throws Exception {
    // execute the method to test.
    Object cachedObject = this.ehcacheFacade.onGetFromCache("NonExistingKey",
        this.cacheProfile);

    assertNull("The retrieved object should be null", cachedObject);
  }

  /**
   * Verifies that the method
   * <code>{@link EhcacheFacade#onPutInCache(java.io.Serializable, org.springmodules.cache.provider.CacheProfile, Object)}</code>
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
   * <code>{@link EhcacheFacade#onPutInCache(java.io.Serializable, org.springmodules.cache.provider.CacheProfile, Object)}</code>
   * does not store any entry in any cache if the cache specified in the given
   * cache profile does not exist.
   */
  public void testOnPutInCacheWhenCacheIsNotFound() throws Exception {
    this.cacheProfile.setCacheName("NonExistingCache");
    this.ehcacheFacade.onPutInCache(this.cacheKey, this.cacheProfile,
        "An Object");

    Element cacheElement = this.cache.get(this.cacheKey);
    assertNull("The retrieved object should be null", cacheElement);
  }

  /**
   * Verifies that the method
   * <code>{@link EhcacheFacade#onPutInCache(java.io.Serializable, org.springmodules.cache.provider.CacheProfile, Object)}</code>
   * does not store any entry in any cache if the name of the cache, specified
   * in the given cache profile, is empty. given key.
   */
  public void testOnPutInCacheWhenCacheNameIsEmpty() throws Exception {
    this.cacheProfile.setCacheName("");
    this.ehcacheFacade.onPutInCache(this.cacheKey, this.cacheProfile,
        "An Object");

    Element cacheElement = this.cache.get(this.cacheKey);
    assertNull("The retrieved object should be null", cacheElement);
  }

  /**
   * Verifies that the method
   * <code>{@link EhcacheFacade#removeFromCache(java.io.Serializable, String)}</code>
   * removes the entry stored under the given key from the cache specified in
   * the given cache profile.
   */
  public void testRemoveFromCache() throws Exception {
    this.cache.put(new Element(this.cacheKey, "An Object"));

    this.ehcacheFacade.removeFromCache(this.cacheKey, this.cacheProfileId);

    Element cacheElement = this.cache.get(this.cacheKey);
    assertNull("The element with key '" + this.cacheKey
        + "' should have been removed from the cache", cacheElement);
  }

  /**
   * Verifies that the method
   * <code>{@link EhcacheFacade#removeFromCache(java.io.Serializable, String)}</code>
   * does not remove any entry if the cache, specified in the given cache
   * profile, cannot be found.
   */
  public void testRemoveFromCacheWhenCacheIsNotFound() throws Exception {
    this.cache.put(new Element(this.cacheKey, "An Object"));
    this.cacheProfile.setCacheName("NonExistingCache");

    this.ehcacheFacade.removeFromCache(this.cacheKey, this.cacheProfileId);

    Element cacheElement = this.cache.get(this.cacheKey);
    assertNotNull("The element with key '" + this.cacheKey
        + "' should not have been removed from the cache", cacheElement);
  }

  /**
   * Verifies that the method
   * <code>{@link EhcacheFacade#removeFromCache(java.io.Serializable, String)}</code>
   * does not remove any entry if the name of the cache, specified in the given
   * cache profile, is empty.
   */
  public void testRemoveFromCacheWhenCacheNameIsEmpty() throws Exception {
    this.cache.put(new Element(this.cacheKey, "An Object"));
    this.cacheProfile.setCacheName("");

    this.ehcacheFacade.removeFromCache(this.cacheKey, this.cacheProfileId);

    Element cacheElement = this.cache.get(this.cacheKey);
    assertNotNull("The element with key '" + this.cacheKey
        + "' should not have been removed from the cache", cacheElement);
  }

  /**
   * Verifies that the method
   * <code>{@link EhcacheFacade#validateCacheManager()}</code> does not throw
   * any exception if the status of the Cache Manager is "Active".
   */
  public void testValidateCacheManagerWithCacheManagerEqualToActive()
      throws Exception {
    this.ehcacheFacade.validateCacheManager();
  }

  /**
   * Verifies that the method
   * <code>{@link EhcacheFacade#validateCacheManager()}</code> throws an
   * <code>IllegalStateException</code> if the cache manager is
   * <code>null</code>.
   */
  public void testValidateCacheManagerWithCacheManagerEqualToNull() {
    this.ehcacheFacade.setCacheManager(null);
    try {
      this.ehcacheFacade.validateCacheManager();
      fail("An IllegalStateException should have been thrown");
    } catch (IllegalStateException exception) {
      // we are expecting this exception.
    }
  }

  /**
   * Verifies that the method
   * <code>{@link EhcacheFacade#validateCacheManager()}</code> does not throw
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
   * <code>{@link EhcacheFacade#validateCacheManager()}</code> throws an
   * <code>IllegalStateException</code> if the cache manager is not "alive"
   * and the flag 'failQuietlyEnabled' is <code>false</code>.
   */
  public void testValidateCacheManagerWithCacheManagerNotAliveAndFailQuietlyNotEnabled()
      throws Exception {

    this.ehcacheFacade.setFailQuietlyEnabled(false);
    this.cacheManager.shutdown();

    try {
      this.ehcacheFacade.validateCacheManager();
      fail("An 'IllegalStateException' should have been thrown");
    } catch (IllegalStateException exception) {
      // we are expecting this exception.
    }
  }
}
