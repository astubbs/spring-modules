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

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.easymock.classextension.MockClassControl;
import org.springmodules.cache.provider.AbstractCacheProfileEditor;
import org.springmodules.cache.provider.CacheAccessException;
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
 * @version $Revision: 1.7 $ $Date: 2005/08/23 01:17:02 $
 */
public class EhCacheFacadeTests extends TestCase {

  private Cache cache;

  private MockClassControl cacheControl;

  /**
   * Key used to store/retrieve an entry of the cache.
   */
  private String cacheKey;

  private CacheManager cacheManager;

  /**
   * Name of the EHCache to use.
   */
  private String cacheName;

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

  private void assertOnGetFromCacheWrapsCatchedException(
      Exception expectedCatchedException) throws Exception {
    Method getMethod = Cache.class.getDeclaredMethod("get",
        new Class[] { Serializable.class });
    this.setUpCacheAsMockObject(getMethod);

    this.cache.get(this.cacheKey);
    this.cacheControl.setThrowable(expectedCatchedException);

    this.cacheControl.replay();

    try {
      this.ehcacheFacade.onGetFromCache(this.cacheKey, this.cacheProfile);
      this.failIfCacheAccessExceptionIsNotThrown();

    } catch (CacheAccessException cacheAccessException) {
      assertSame(expectedCatchedException, cacheAccessException.getCause());
    }

    this.cacheControl.verify();
  }

  private void failIfCacheAccessExceptionIsNotThrown() {
    fail("Expecting exception <" + CacheAccessException.class.getName() + ">");
  }

  private void failIfCacheNotFoundExceptionIsNotThrown() {
    fail("Expecting exception <" + CacheNotFoundException.class.getName() + ">");
  }

  private void failIfInvalidConfigurationExceptionIsNotThrown() {
    fail("Expecting exception <"
        + InvalidConfigurationException.class.getName() + ">");
  }

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

  private void setUpCacheAsMockObject(Method methodToMock) throws Exception {
    this.setUpCacheAsMockObject(new Method[] { methodToMock });
  }

  private void setUpCacheAsMockObject(Method[] methodsToMock) throws Exception {
    Class[] constructorTypes = new Class[] { String.class, int.class,
        boolean.class, boolean.class, long.class, long.class };

    Object[] constructorArgs = new Object[] { this.cacheName, new Integer(10),
        new Boolean(false), new Boolean(false), new Long(300), new Long(600) };

    Class classToMock = Cache.class;

    this.cacheControl = MockClassControl.createControl(classToMock,
        constructorTypes, constructorArgs, methodsToMock);

    this.cache = (Cache) this.cacheControl.getMock();

    this.cacheManager.removeCache(this.cacheName);
    this.cacheManager.addCache(this.cache);
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

  public void testIsSerializableCacheElementRequired() {
    assertTrue(this.ehcacheFacade.isSerializableCacheElementRequired());
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

  public void testOnFlushCacheWhenCacheAccessThrowsIllegalStateException()
      throws Exception {
    Method removeAllMethod = Cache.class.getMethod("removeAll", null);
    this.setUpCacheAsMockObject(removeAllMethod);

    IllegalStateException expectedCatchedException = new IllegalStateException();

    this.cache.removeAll();
    this.cacheControl.setThrowable(expectedCatchedException);

    this.cacheControl.replay();

    try {
      this.ehcacheFacade.onFlushCache(this.cacheProfile);
      this.failIfCacheAccessExceptionIsNotThrown();

    } catch (CacheAccessException cacheAccessException) {
      assertSame(expectedCatchedException, cacheAccessException.getCause());
    }

    this.cacheControl.verify();
  }

  public void testOnFlushCacheWhenCacheIsNotFound() {
    this.cache.put(new Element(this.cacheKey, "A Value"));
    this.cacheProfile.setCacheName("NonExistingCache");

    try {
      this.ehcacheFacade.onFlushCache(this.cacheProfile);
      fail("Expecting exception <" + CacheNotFoundException.class.getName()
          + ">");

    } catch (CacheNotFoundException exception) {
      // expecting this exception.
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

  public void testOnGetFromCacheWhenCacheAccessThrowsCacheException()
      throws Exception {
    Exception expectedCatchedException = new net.sf.ehcache.CacheException();
    this.assertOnGetFromCacheWrapsCatchedException(expectedCatchedException);
  }

  public void testOnGetFromCacheWhenCacheAccessThrowsIllegalStateException()
      throws Exception {
    Exception expectedCatchedException = new IllegalStateException();
    this.assertOnGetFromCacheWrapsCatchedException(expectedCatchedException);
  }

  public void testOnGetFromCacheWhenCacheIsNotFound() {
    this.cacheProfile.setCacheName("NonExistingCache");

    try {
      this.ehcacheFacade.onGetFromCache(this.cacheKey, this.cacheProfile);
      this.failIfCacheNotFoundExceptionIsNotThrown();

    } catch (CacheNotFoundException exception) {
      // we are expecting this exception.
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

  public void testOnRemoveFromCache() throws Exception {
    this.cache.put(new Element(this.cacheKey, "An Object"));

    this.ehcacheFacade.onRemoveFromCache(this.cacheKey, this.cacheProfile);

    Element cacheElement = this.cache.get(this.cacheKey);
    assertNull("The element with key '" + this.cacheKey
        + "' should have been removed from the cache", cacheElement);
  }

  public void testOnRemoveFromCacheWhenCacheAccessThrowsIllegalStateException()
      throws Exception {
    Method removeMethod = Cache.class.getDeclaredMethod("remove",
        new Class[] { Serializable.class });
    this.setUpCacheAsMockObject(removeMethod);

    IllegalStateException expectedCatchedException = new IllegalStateException();

    this.cache.remove(this.cacheKey);
    this.cacheControl.setThrowable(expectedCatchedException);

    this.cacheControl.replay();

    try {
      this.ehcacheFacade.onRemoveFromCache(this.cacheKey, this.cacheProfile);
      this.failIfCacheAccessExceptionIsNotThrown();

    } catch (CacheAccessException cacheAccessException) {
      assertSame(expectedCatchedException, cacheAccessException.getCause());
    }

    this.cacheControl.verify();
  }

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

  /**
   * Verifies that the method
   * <code>{@link EhCacheFacade#verifyCacheExists(String)}</code> does not
   * throw any exception if there is a cache stored under the given name.
   */
  public void testVerifyCacheExistsWithExistingCache() {
    this.ehcacheFacade.verifyCacheExists(this.cacheName);
  }

  public void testVerifyCacheExistsWithNotExistingCache() {
    try {
      this.ehcacheFacade.verifyCacheExists("AnotherCache");
      this.failIfCacheNotFoundExceptionIsNotThrown();

    } catch (CacheNotFoundException exception) {
      // we are expecting this exception.
    }
  }
}
