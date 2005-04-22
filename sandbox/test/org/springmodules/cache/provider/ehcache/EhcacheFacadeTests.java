/* 
 * Created on Nov 10, 2004
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
import org.springmodules.cache.CacheWrapperException;
import org.springmodules.cache.EntryRetrievalException;
import org.springmodules.cache.key.CacheKey;
import org.springmodules.cache.mock.FixedValueCacheKey;
import org.springmodules.cache.provider.AbstractCacheProfileEditor;
import org.springmodules.cache.provider.CacheProfile;
import org.springmodules.cache.provider.CacheProfileValidator;

/**
 * <p>
 * Unit Test for <code>{@link EhcacheFacade}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/04/22 02:18:59 $
 */
public final class EhcacheFacadeTests extends TestCase {

  /**
   * The name of the cache (<code>{@link #mockCache}</code>).
   */
  private static final String EXISTING_CACHE_NAME = "CACHE";

  /**
   * Key used to store/retrieve an entry of the cache.
   */
  private CacheKey cacheKey;

  /**
   * EHCache Cache Manager.
   */
  private CacheManager cacheManager;

  /**
   * Configuration options for the caching services.
   */
  private EhcacheCacheProfile cacheProfile;

  /**
   * Primary object (instance of the class to test).
   */
  private EhcacheFacade ehcacheFacade;

  /**
   * Mock object that simulates an EHCache Cache.
   */
  private Cache mockCache;

  /**
   * Controls the behavior of <code>{@link #mockCache}</code>.
   */
  private MockClassControl mockCacheControl;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the Test Case.
   */
  public EhcacheFacadeTests(String name) {
    super(name);
  }

  /**
   * Sets up the test fixture.
   */
  protected void setUp() throws Exception {
    super.setUp();

    this.cacheKey = new FixedValueCacheKey("KEY");

    this.ehcacheFacade = new EhcacheFacade();
  }

  /**
   * Sets up <code>{@link #cacheManager}</code>.
   */
  private void setUpCacheManager() throws Exception {
    this.cacheManager = CacheManager.create();
    this.ehcacheFacade.setCacheManager(this.cacheManager);
  }

  /**
   * Sets up <code>{@link #cacheProfile}</code>.
   */
  private void setUpCacheProfile() {
    this.cacheProfile = new EhcacheCacheProfile();
    this.cacheProfile.setCacheName(EXISTING_CACHE_NAME);
  }

  /**
   * Sets up:
   * <ul>
   * <li><code>{@link #mockCache}</code></li>
   * <li><code>{@link #mockCacheControl}</code></li>
   * </ul>
   */
  private void setUpMockCache() throws Exception {
    // set up the constructor arguments for the class to mock.
    Class[] constructorTypes = new Class[] { String.class, int.class,
        boolean.class, boolean.class, long.class, long.class };

    Object[] constructorArgs = new Object[] { EXISTING_CACHE_NAME,
        new Integer(10), new Boolean(false), new Boolean(false), new Long(300),
        new Long(600) };

    // set up the class to mock.
    Class classToMock = Cache.class;

    // set up the methods to mock
    Method getMethod = classToMock.getMethod("get",
        new Class[] { Serializable.class });
    Method getNameMethod = classToMock.getMethod("getName", null);
    Method putMethod = classToMock.getMethod("put",
        new Class[] { Element.class });
    Method removeMethod = classToMock.getMethod("remove",
        new Class[] { Serializable.class });
    Method removeAllMethod = classToMock.getMethod("removeAll", null);
    Method[] methodsToMock = new Method[] { getMethod, getNameMethod,
        putMethod, removeMethod, removeAllMethod };

    // create the mock control.
    this.mockCacheControl = MockClassControl.createControl(classToMock,
        constructorTypes, constructorArgs, methodsToMock);
    // create the mock object.
    this.mockCache = (Cache) this.mockCacheControl.getMock();
  }

  /**
   * Cleans up the test fixture.
   */
  protected void tearDown() throws Exception {
    super.tearDown();

    try {
      if (this.cacheManager != null) {
        this.cacheManager.removeCache(EXISTING_CACHE_NAME);
        this.cacheManager.shutdown();
      }
    } catch (Exception exception) {
      // ignore exception
    }
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
   * <code>{@link EhcacheFacade#onFlushCache(EhcacheCacheProfile)}</code>
   * flushes the cache.
   */
  public void testOnFlushCache() throws Exception {
    this.setUpCacheManager();
    this.setUpMockCache();
    this.setUpCacheProfile();

    // expectation: get the name of the cache three times.
    this.mockCache.getName();
    this.mockCacheControl.setReturnValue(EXISTING_CACHE_NAME, 3);

    // expectation: remove all elements of the cache.
    this.mockCache.removeAll();

    // set the state of the mock control to 'replay'.
    this.mockCacheControl.replay();

    // the cache manager should have the cache we are going to work with.
    this.cacheManager.addCache(this.mockCache);

    // execute the method to test.
    this.ehcacheFacade.onFlushCache(this.cacheProfile);

    // verify that the expectations of the mock control were met.
    this.mockCacheControl.verify();
  }

  /**
   * Verifies that the method
   * <code>{@link EhcacheFacade#onFlushCache(EhcacheCacheProfile)}</code>
   * throws a <code>{@link CacheWrapperException}</code> wrapping any
   * exception thrown by the cache.
   */
  public void testOnFlushCacheWhenCacheThrowsException() throws Exception {
    this.setUpCacheManager();
    this.setUpMockCache();
    this.setUpCacheProfile();

    // expectation: get the name of the cache three times.
    this.mockCache.getName();
    this.mockCacheControl.setReturnValue(EXISTING_CACHE_NAME, 3);

    // expectation: remove all elements of the cache. The cache should throw
    // an exception.
    this.mockCache.removeAll();
    this.mockCacheControl.setThrowable(new IllegalStateException());

    // set the state of the mock control to 'replay'.
    this.mockCacheControl.replay();

    // the cache manager should have the cache we are going to work with.
    this.cacheManager.addCache(this.mockCache);

    // execute the method to test.
    try {
      this.ehcacheFacade.onFlushCache(this.cacheProfile);
      fail("A CacheWrapperException should have been thrown");
    } catch (CacheWrapperException exception) {
      // we expect this exception.
    }

    // verify that the expectations of the mock control were met.
    this.mockCacheControl.verify();
  }

  /**
   * Verifies that the method
   * <code>{@link EhcacheFacade#onFlushCache(EhcacheCacheProfile)}</code> does
   * not flush the cache if the cache manager does not contain a cache that has
   * the same name as the one specified in the cache profile.
   */
  public void testOnFlushCacheWithCacheProfileHavingNotExistingCacheName()
      throws Exception {

    this.setUpCacheManager();
    this.setUpMockCache();
    this.setUpCacheProfile();

    this.cacheProfile.setCacheName("NOT_EXISTING_CACHE");

    // expectation: get the name of the cache three times.
    this.mockCache.getName();
    this.mockCacheControl.setReturnValue(EXISTING_CACHE_NAME, 3);

    // set the state of the mock control to 'replay'.
    this.mockCacheControl.replay();

    // the cache manager should have the cache we are going to work with.
    this.cacheManager.addCache(this.mockCache);

    // execute the method to test.
    this.ehcacheFacade.onFlushCache(this.cacheProfile);

    // verify that the expectations of the mock control were met.
    this.mockCacheControl.verify();
  }

  /**
   * Verifies that the method
   * <code>{@link EhcacheFacade#onGetFromCache(Serializable, CacheProfile)}</code>
   * throws a <code>{@link CacheWrapperException}</code> wrapping any
   * exception thrown by the cache.
   */
  public void testOnGetFromCacheWhenCacheThrowsException() throws Exception {
    this.setUpCacheManager();
    this.setUpMockCache();
    this.setUpCacheProfile();

    // expectation: get the name of the cache three times.
    this.mockCache.getName();
    this.mockCacheControl.setReturnValue(EXISTING_CACHE_NAME, 3);

    // expectation: try to get an entry from the cache. The cache should throw
    // an exception.
    this.mockCache.get(this.cacheKey);
    this.mockCacheControl.setThrowable(new IllegalStateException());

    // set the state of the mock control to 'replay'.
    this.mockCacheControl.replay();

    // the cache manager should have the cache we are going to work with.
    this.cacheManager.addCache(this.mockCache);

    // execute the method to test.
    try {
      this.ehcacheFacade.onGetFromCache(this.cacheKey, this.cacheProfile);
      fail("A CacheWrapperException should have been thrown");
    } catch (CacheWrapperException exception) {
      // we are expecting this exception.
    }

    // verify that the expectations of the mock control were met.
    this.mockCacheControl.verify();
  }

  /**
   * Verifies that the method
   * <code>{@link EhcacheFacade#onGetFromCache(Serializable, CacheProfile)}</code>
   * returns an entry from the cache stored under the specified key.
   */
  public void testOnGetFromCacheWhenObjectToRetrieveIsCached() throws Exception {
    this.setUpCacheManager();
    this.setUpMockCache();
    this.setUpCacheProfile();

    // entry to be returned by the cache.
    Serializable cachedObject = "A String :)";
    Element cachedElement = new Element(this.cacheKey, cachedObject);

    // expectation: get the name of the cache three times.
    this.mockCache.getName();
    this.mockCacheControl.setReturnValue(EXISTING_CACHE_NAME, 3);

    // expectation: get an entry from the cache.
    this.mockCache.get(this.cacheKey);
    this.mockCacheControl.setReturnValue(cachedElement);

    // set the state of the mock control to 'replay'.
    this.mockCacheControl.replay();

    // the cache manager should have the cache we are going to work with.
    this.cacheManager.addCache(this.mockCache);

    // execute the method to test.
    Object returnedObject = this.ehcacheFacade.onGetFromCache(this.cacheKey,
        this.cacheProfile);
    assertSame("<Cached object>", cachedObject, returnedObject);

    // verify that the expectations of the mock control were met.
    this.mockCacheControl.verify();
  }

  /**
   * Verifies that the method
   * <code>{@link EhcacheFacade#onGetFromCache(Serializable, CacheProfile)}</code>
   * returns <code>null</code> if there is not any entry in the cache stored
   * under the specified key.
   */
  public void testOnGetFromCacheWhenObjectToRetrieveIsNotCached()
      throws Throwable {
    this.setUpCacheManager();
    this.setUpMockCache();
    this.setUpCacheProfile();

    // expectation: get the name of the cache three times.
    this.mockCache.getName();
    this.mockCacheControl.setReturnValue(EXISTING_CACHE_NAME, 3);

    // expectation: try to get an entry from the cache. The cache should not
    // have the entry.
    this.mockCache.get(this.cacheKey);
    this.mockCacheControl.setReturnValue(null);

    // set the state of the mock control to 'replay'.
    this.mockCacheControl.replay();

    // the cache manager should have the cache we are going to work with.
    this.cacheManager.addCache(this.mockCache);

    // execute the method to test.
    Object returnedObject = this.ehcacheFacade.onGetFromCache(this.cacheKey,
        this.cacheProfile);

    assertNull("The returned object should be null", returnedObject);

    // verify that the expectations of the mock control were met.
    this.mockCacheControl.verify();
  }

  /**
   * Verifies that the method
   * <code>{@link EhcacheFacade#onGetFromCache(Serializable, CacheProfile)}</code>
   * throws a <code>{@link EntryRetrievalException}</code> if the name of the
   * cache specified in the cache profile does not match any cache in the cache
   * manager.
   */
  public void testOnGetFromCacheWithCacheProfileHavingNotExistingCacheName()
      throws Throwable {

    this.setUpCacheManager();
    this.setUpMockCache();
    this.setUpCacheProfile();

    this.cacheProfile.setCacheName("NOT_EXISTING_CACHE");

    // expectation: get the name of the cache three times.
    this.mockCache.getName();
    this.mockCacheControl.setReturnValue(EXISTING_CACHE_NAME, 3);

    // set the state of the mock control to 'replay'.
    this.mockCacheControl.replay();

    // execute the method to test.
    this.cacheManager.addCache(this.mockCache);

    try {
      this.ehcacheFacade.onGetFromCache(this.cacheKey, this.cacheProfile);
      fail("A EntryRetrievalException should have been thrown");
    } catch (EntryRetrievalException exception) {
      // we are expecting this exception.
    }

    // verify that the expectations of the mock control were met.
    this.mockCacheControl.verify();
  }

  /**
   * Verifies that the method
   * <code>{@link EhcacheFacade#onPutInCache(Serializable, CacheProfile, Object)}</code>
   * stores entries in the cache.
   */
  public void testOnPutInCache() throws Throwable {
    this.setUpCacheManager();
    this.setUpMockCache();
    this.setUpCacheProfile();

    // object to store in the cache.
    Serializable objectToCache = "A String :)";
    EhcacheElement newCacheElement = new EhcacheElement(this.cacheKey,
        objectToCache);

    // expectation: get the name of the cache three times.
    this.mockCache.getName();
    this.mockCacheControl.setReturnValue(EXISTING_CACHE_NAME, 3);

    // expectation: store the object in the cache.
    this.mockCache.put(newCacheElement);

    // set the state of the mock control to 'replay'.
    this.mockCacheControl.replay();

    // the cache manager should have the cache we are going to work with.
    this.cacheManager.addCache(this.mockCache);

    // execute the method to test.
    this.ehcacheFacade.onPutInCache(this.cacheKey, this.cacheProfile,
        objectToCache);

    // verify that the expectations of the mock control were met.
    this.mockCacheControl.verify();
  }

  /**
   * Verifies that the method
   * <code>{@link EhcacheFacade#onPutInCache(Serializable, CacheProfile, Object)}</code>
   * does not store objects in the cache if the cache manager does not contain a
   * cache that has the same name as the one specified in the cache profile.
   */
  public void testOnPutInCacheWithCacheProfileHavingNotExistingCacheName()
      throws Exception {

    this.setUpCacheManager();
    this.setUpMockCache();
    this.setUpCacheProfile();

    this.cacheProfile.setCacheName("NOT_EXISTING_CACHE");

    // object to store in the cache.
    Serializable objectToCache = "A String :)";

    // expectation: get the name of the cache three times.
    this.mockCache.getName();
    this.mockCacheControl.setReturnValue(EXISTING_CACHE_NAME, 3);

    // set the state of the mock control to 'replay'.
    this.mockCacheControl.replay();

    // the cache manager should have the cache we are going to work with.
    this.cacheManager.addCache(this.mockCache);

    // execute the method to test.
    this.ehcacheFacade.onPutInCache(this.cacheKey, this.cacheProfile,
        objectToCache);

    // verify that the expectations of the mock control were met.
    this.mockCacheControl.verify();
  }

  /**
   * Verifies that the method
   * <code>{@link EhcacheFacade#validateCacheManager()}</code> does not throw
   * any exception if the status of the Cache Manager is "Active".
   */
  public void testValidateCacheManagerWithCacheManagerEqualToActive()
      throws Exception {

    this.setUpCacheManager();
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

    this.setUpCacheManager();
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

    this.setUpCacheManager();
    this.ehcacheFacade.setFailQuietlyEnabled(false);
    this.cacheManager.shutdown();

    try {
      this.ehcacheFacade.validateCacheManager();
      fail("An 'IllegalStateException' should have been thrown");
    } catch (IllegalStateException exception) {
      // we are expecting this exception.
    }
  }

  /**
   * Verifies that the method
   * <code>{@link EhcacheFacade#removeFromCache(Serializable, String)}</code>
   * removes from the cache the object stored under the given key.
   */
  public void testRemoveFromCache() throws Exception {
    this.setUpCacheManager();
    this.setUpMockCache();
    this.setUpCacheProfile();
    String cacheProfileId = "myId";

    Map cacheProfiles = new HashMap();
    cacheProfiles.put(cacheProfileId, this.cacheProfile);
    this.ehcacheFacade.setCacheProfiles(cacheProfiles);

    // expectation: get the name of the cache three times.
    this.mockCache.getName();
    this.mockCacheControl.setReturnValue(EXISTING_CACHE_NAME, 3);

    // expectation: remove the object stored under the given key.
    this.mockCache.remove(this.cacheKey);
    this.mockCacheControl.setReturnValue(true);

    // set the state of the mock controls to 'replay'.
    this.mockCacheControl.replay();

    // the cache manager should have the cache we are going to work with.
    this.cacheManager.addCache(this.mockCache);

    // execute the method to test.
    this.ehcacheFacade.removeFromCache(this.cacheKey, cacheProfileId);

    // verify that the expectations of the mock controls were met.
    this.mockCacheControl.verify();
  }

  /**
   * Verifies that the method
   * <code>{@link EhcacheFacade#removeFromCache(Serializable, String)}</code>
   * does not remove any object from the cache if there is not any cache profile
   * stored under the given id.
   */
  public void testRemoveFromCacheWithCacheProfileIsNull() throws Exception {
    this.setUpCacheManager();
    this.setUpMockCache();
    this.setUpCacheProfile();

    this.ehcacheFacade.setCacheProfiles(new HashMap());

    // set the state of the mock controls to 'replay'.
    this.mockCacheControl.replay();

    // execute the method to test.
    this.ehcacheFacade.removeFromCache(this.cacheKey, "myId");

    // verify that the expectations of the mock controls were met.
    this.mockCacheControl.verify();
  }

  /**
   * Verifies that the method
   * <code>{@link EhcacheFacade#removeFromCache(Serializable, String)}</code>
   * does not remove any object from the cache if the cache profile does not
   * have the name of the cache to use.
   */
  public void testRemoveFromCacheWithEmptyCacheName() throws Exception {
    this.setUpCacheManager();
    this.setUpMockCache();

    this.cacheProfile = new EhcacheCacheProfile();
    String cacheProfileId = "myId";

    Map cacheProfiles = new HashMap();
    cacheProfiles.put(cacheProfileId, this.cacheProfile);
    this.ehcacheFacade.setCacheProfiles(cacheProfiles);

    // set the state of the mock controls to 'replay'.
    this.mockCacheControl.replay();

    // execute the method to test.
    this.ehcacheFacade.removeFromCache(this.cacheKey, cacheProfileId);

    // verify that the expectations of the mock controls were met.
    this.mockCacheControl.verify();
  }
}