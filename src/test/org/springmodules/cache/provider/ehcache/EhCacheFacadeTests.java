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

import java.beans.PropertyEditor;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.easymock.AbstractMatcher;
import org.easymock.classextension.MockClassControl;
import org.springmodules.cache.provider.CacheAccessException;
import org.springmodules.cache.provider.CacheException;
import org.springmodules.cache.provider.CacheNotFoundException;
import org.springmodules.cache.provider.CacheProfileEditor;
import org.springmodules.cache.provider.CacheProfileValidator;
import org.springmodules.cache.provider.InvalidConfigurationException;

/**
 * <p>
 * Unit Tests for <code>{@link EhCacheFacade}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.12 $ $Date: 2005/09/20 03:50:27 $
 */
public class EhCacheFacadeTests extends TestCase {

  private class ElementMatcher extends AbstractMatcher {
    /**
     * @see AbstractMatcher#argumentMatches(Object, Object)
     */
    protected boolean argumentMatches(Object expected, Object actual) {
      if (!(expected instanceof Element)) {
        throw new IllegalArgumentException(
            "Element matcher only evaluates instances of <"
                + Element.class.getName() + ">");
      }
      if (!(actual instanceof Element)) {
        return false;
      }
      Element expectedElement = (Element) expected;
      Element actualElement = (Element) actual;

      Serializable expectedKey = expectedElement.getKey();
      Object expectedValue = expectedElement.getValue();

      Serializable actualKey = actualElement.getKey();
      Object actualValue = actualElement.getValue();

      if (expectedKey != null ? !expectedKey.equals(actualKey)
          : actualKey != null) {
        return false;
      }

      if (expectedValue != null ? !expectedValue.equals(actualValue)
          : actualValue != null) {
        return false;
      }

      return true;
    }

  }

  private static final String CACHE_KEY = "key";

  private static final String CACHE_NAME = "testCache";

  private static final String CACHE_PROFILE_ID = "cacheProfile";

  private Cache cache;

  private MockClassControl cacheControl;

  private CacheManager cacheManager;

  private EhCacheProfile cacheProfile;

  private EhCacheFacade ehcacheFacade;

  public EhCacheFacadeTests(String name) {
    super(name);
  }

  private void assertCacheExceptionIsCacheNotFoundException(
      CacheException exception) {
    assertEquals(CacheNotFoundException.class, exception.getClass());
  }

  private void assertOnGetFromCacheWrapsCatchedException(
      Exception expectedCatchedException) throws Exception {
    Method getMethod = Cache.class.getDeclaredMethod("get",
        new Class[] { Serializable.class });
    setUpCacheAsMockObject(getMethod);

    cache.get(CACHE_KEY);
    cacheControl.setThrowable(expectedCatchedException);

    cacheControl.replay();

    try {
      ehcacheFacade.onGetFromCache(CACHE_KEY, cacheProfile);
      fail();

    } catch (CacheAccessException cacheAccessException) {
      assertSame(expectedCatchedException, cacheAccessException.getCause());
    }

    cacheControl.verify();
  }

  private void assertValidateCacheManagerThrowsException() {
    try {
      ehcacheFacade.validateCacheManager();
      fail();

    } catch (InvalidConfigurationException exception) {
      // we are expecting this exception.
    }
  }

  protected void setUp() throws Exception {
    super.setUp();
    cacheManager = CacheManager.create();

    cacheProfile = new EhCacheProfile();
    cacheProfile.setCacheName(CACHE_NAME);

    Map cacheProfiles = new HashMap();
    cacheProfiles.put(CACHE_PROFILE_ID, cacheProfile);

    ehcacheFacade = new EhCacheFacade();
    ehcacheFacade.setCacheProfiles(cacheProfiles);
  }

  private void setUpCache() {
    cache = cacheManager.getCache(CACHE_NAME);
    ehcacheFacade.setCacheManager(cacheManager);
  }

  private void setUpCacheAsMockObject(Method methodToMock) throws Exception {
    setUpCacheAsMockObject(new Method[] { methodToMock });
  }

  private void setUpCacheAsMockObject(Method[] methodsToMock) throws Exception {
    Class[] constructorTypes = new Class[] { String.class, int.class,
        boolean.class, boolean.class, long.class, long.class };

    Object[] constructorArgs = new Object[] { CACHE_NAME, new Integer(10),
        new Boolean(false), new Boolean(false), new Long(300), new Long(600) };

    Class classToMock = Cache.class;

    cacheControl = MockClassControl.createControl(classToMock,
        constructorTypes, constructorArgs, methodsToMock);

    cache = (Cache) cacheControl.getMock();
    ehcacheFacade.setCacheManager(cacheManager);

    cacheManager.removeCache(CACHE_NAME);
    cacheManager.addCache(cache);
  }

  protected void tearDown() throws Exception {
    super.tearDown();

    cacheManager.shutdown();
  }

  public void testGetCacheProfileEditor() {
    PropertyEditor editor = ehcacheFacade.getCacheProfileEditor();

    assertNotNull(editor);
    assertEquals(CacheProfileEditor.class, editor.getClass());

    CacheProfileEditor profileEditor = (CacheProfileEditor) editor;
    assertEquals(EhCacheProfile.class, profileEditor.getCacheProfileClass());
    assertNull(profileEditor.getCacheProfilePropertyEditors());
  }

  /**
   * Verifies that the method
   * <code>{@link EhCacheFacade#getCacheProfileValidator()}</code> returns an
   * an instance of <code>{@link EhCacheProfileValidator}</code> not equal to
   * <code>null</code>.
   */
  public void testGetCacheProfileValidator() {
    CacheProfileValidator validator = ehcacheFacade.getCacheProfileValidator();
    assertNotNull(validator);
    assertEquals(EhCacheProfileValidator.class, validator.getClass());
  }

  public void testGetCacheWhenCacheAccessThrowsException() {
    setUpCache();

    // we can mock the cache manager since it doesn't have a public constructor.
    // force a NullPointerException.
    ehcacheFacade.setCacheManager(null);

    try {
      ehcacheFacade.getCache(CACHE_NAME);
      fail();

    } catch (CacheAccessException exception) {
      Throwable cause = exception.getCause();
      assertNotNull(cause);
      assertTrue(cause instanceof NullPointerException);
    }
  }

  public void testGetCacheWithExistingCache() {
    setUpCache();

    Cache expected = cacheManager.getCache(CACHE_NAME);
    Cache actual = ehcacheFacade.getCache(CACHE_NAME);
    assertSame(expected, actual);
  }

  public void testGetCacheWithNotExistingCache() {
    setUpCache();

    try {
      ehcacheFacade.getCache("AnotherCache");
      fail();

    } catch (CacheNotFoundException exception) {
      // we are expecting this exception.
    }
  }

  public void testIsSerializableCacheElementRequired() {
    assertTrue(ehcacheFacade.isSerializableCacheElementRequired());
  }

  /**
   * Verifies that the method
   * <code>{@link EhCacheFacade#onFlushCache(org.springmodules.cache.provider.CacheProfile)}</code>
   * flushes the cache specified in the given cache profile.
   */
  public void testOnFlushCache() throws Exception {
    setUpCache();

    cache.put(new Element(CACHE_KEY, "A Value"));

    // execute the method to test.
    ehcacheFacade.onFlushCache(cacheProfile);

    Object cachedValue = cache.get(CACHE_KEY);
    assertNull("The cache '" + CACHE_NAME + "' should be empty", cachedValue);
  }

  public void testOnFlushCacheWhenCacheAccessThrowsIllegalStateException()
      throws Exception {
    Method removeAllMethod = Cache.class.getMethod("removeAll", null);
    setUpCacheAsMockObject(removeAllMethod);

    IllegalStateException expectedCatchedException = new IllegalStateException();

    cache.removeAll();
    cacheControl.setThrowable(expectedCatchedException);

    cacheControl.replay();

    try {
      ehcacheFacade.onFlushCache(cacheProfile);
      fail();

    } catch (CacheAccessException cacheAccessException) {
      assertSame(expectedCatchedException, cacheAccessException.getCause());
    }

    cacheControl.verify();
  }

  public void testOnFlushCacheWhenCacheIsNotFound() {
    setUpCache();

    cache.put(new Element(CACHE_KEY, "A Value"));
    cacheProfile.setCacheName("NonExistingCache");

    try {
      ehcacheFacade.onFlushCache(cacheProfile);
      fail();

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
    setUpCache();

    String objectToStore = "An Object";
    cache.put(new Element(CACHE_KEY, objectToStore));

    Object cachedObject = ehcacheFacade.onGetFromCache(CACHE_KEY, cacheProfile);

    assertEquals("<Cached object>", objectToStore, cachedObject);
  }

  public void testOnGetFromCacheWhenCacheAccessThrowsCacheException()
      throws Exception {
    Exception expectedCatchedException = new net.sf.ehcache.CacheException();
    assertOnGetFromCacheWrapsCatchedException(expectedCatchedException);
  }

  public void testOnGetFromCacheWhenCacheAccessThrowsIllegalStateException()
      throws Exception {
    Exception expectedCatchedException = new IllegalStateException();
    assertOnGetFromCacheWrapsCatchedException(expectedCatchedException);
  }

  public void testOnGetFromCacheWhenCacheIsNotFound() {
    setUpCache();

    cacheProfile.setCacheName("NonExistingCache");

    try {
      ehcacheFacade.onGetFromCache(CACHE_KEY, cacheProfile);
      fail();

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
    setUpCache();

    Object cachedObject = ehcacheFacade.onGetFromCache("NonExistingKey",
        cacheProfile);

    assertNull(cachedObject);
  }

  /**
   * Verifies that the method
   * <code>{@link EhCacheFacade#onPutInCache(java.io.Serializable, org.springmodules.cache.provider.CacheProfile, Object)}</code>
   * stores an entry in the cache specified in the given cache profile using the
   * given key.
   */
  public void testOnPutInCache() throws Exception {
    setUpCache();

    String objectToCache = "An Object";
    ehcacheFacade.onPutInCache(CACHE_KEY, cacheProfile, objectToCache);

    Object cachedObject = cache.get(CACHE_KEY).getValue();
    assertSame("<Cached object>", objectToCache, cachedObject);
  }

  public void testOnPutInCacheWhenCacheAccessThrowsIllegalStateException()
      throws Exception {
    Method putMethod = Cache.class.getMethod("put",
        new Class[] { Element.class });
    setUpCacheAsMockObject(putMethod);

    IllegalStateException expectedCatchedException = new IllegalStateException();

    String objectToCache = "Luke";
    Element expectedElement = new Element(CACHE_KEY, objectToCache);

    cache.put(expectedElement);
    cacheControl.setMatcher(new ElementMatcher());
    cacheControl.setThrowable(expectedCatchedException);

    cacheControl.replay();

    try {
      ehcacheFacade.onPutInCache(CACHE_KEY, cacheProfile, objectToCache);
      fail();

    } catch (CacheAccessException cacheAccessException) {
      assertSame(expectedCatchedException, cacheAccessException.getCause());
    }

    cacheControl.verify();
  }

  /**
   * Verifies that the method
   * <code>{@link EhCacheFacade#onPutInCache(java.io.Serializable, org.springmodules.cache.provider.CacheProfile, Object)}</code>
   * does not store any entry in any cache if the cache specified in the given
   * cache profile does not exist.
   */
  public void testOnPutInCacheWhenCacheIsNotFound() throws Exception {
    setUpCache();

    cacheProfile.setCacheName("NonExistingCache");
    try {
      ehcacheFacade.onPutInCache(CACHE_KEY, cacheProfile, "An Object");
      fail();

    } catch (CacheException exception) {
      assertCacheExceptionIsCacheNotFoundException(exception);
    }
  }

  public void testOnRemoveFromCache() throws Exception {
    setUpCache();

    cache.put(new Element(CACHE_KEY, "An Object"));

    ehcacheFacade.onRemoveFromCache(CACHE_KEY, cacheProfile);

    Element cacheElement = cache.get(CACHE_KEY);
    assertNull("The element with key '" + CACHE_KEY
        + "' should have been removed from the cache", cacheElement);
  }

  public void testOnRemoveFromCacheWhenCacheAccessThrowsIllegalStateException()
      throws Exception {
    Method removeMethod = Cache.class.getDeclaredMethod("remove",
        new Class[] { Serializable.class });
    setUpCacheAsMockObject(removeMethod);

    IllegalStateException expectedCatchedException = new IllegalStateException();

    cache.remove(CACHE_KEY);
    cacheControl.setThrowable(expectedCatchedException);

    cacheControl.replay();

    try {
      ehcacheFacade.onRemoveFromCache(CACHE_KEY, cacheProfile);
      fail();

    } catch (CacheAccessException cacheAccessException) {
      assertSame(expectedCatchedException, cacheAccessException.getCause());
    }

    cacheControl.verify();
  }

  public void testOnRemoveFromCacheWhenCacheIsNotFound() throws Exception {
    setUpCache();

    cache.put(new Element(CACHE_KEY, "An Object"));
    cacheProfile.setCacheName("NonExistingCache");

    try {
      ehcacheFacade.removeFromCache(CACHE_KEY, CACHE_PROFILE_ID);
      fail();

    } catch (CacheException exception) {
      assertCacheExceptionIsCacheNotFoundException(exception);
    }
  }

  /**
   * Verifies that the method
   * <code>{@link EhCacheFacade#validateCacheManager()}</code> does not throw
   * any exception if the status of the Cache Manager is "Active".
   */
  public void testValidateCacheManagerWithCacheManagerEqualToActive()
      throws Exception {
    setUpCache();

    ehcacheFacade.validateCacheManager();
  }

  /**
   * Verifies that the method
   * <code>{@link EhCacheFacade#validateCacheManager()}</code> throws an
   * <code>{@link InvalidConfigurationException}</code> if the cache manager
   * is <code>null</code>.
   */
  public void testValidateCacheManagerWithCacheManagerEqualToNull() {
    ehcacheFacade.setCacheManager(null);
    assertValidateCacheManagerThrowsException();
  }

  /**
   * Verifies that the method
   * <code>{@link EhCacheFacade#validateCacheManager()}</code> throws an
   * <code>{@link InvalidConfigurationException}</code> if the cache manager
   * is not "alive" and the flag 'failQuietlyEnabled' is <code>false</code>.
   */
  public void testValidateCacheManagerWithCacheManagerNotAlive()
      throws Exception {
    ehcacheFacade.setFailQuietlyEnabled(false);
    cacheManager.shutdown();

    assertValidateCacheManagerThrowsException();
  }
}
