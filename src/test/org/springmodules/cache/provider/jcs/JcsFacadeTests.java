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

package org.springmodules.cache.provider.jcs;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.jcs.engine.CacheElement;
import org.apache.jcs.engine.CompositeCacheAttributes;
import org.apache.jcs.engine.ElementAttributes;
import org.apache.jcs.engine.behavior.ICacheElement;
import org.apache.jcs.engine.behavior.ICompositeCacheAttributes;
import org.apache.jcs.engine.behavior.IElementAttributes;
import org.apache.jcs.engine.control.CompositeCache;
import org.apache.jcs.engine.control.CompositeCacheManager;
import org.apache.jcs.engine.control.group.GroupAttrName;
import org.apache.jcs.engine.control.group.GroupId;
import org.easymock.classextension.MockClassControl;
import org.springmodules.cache.CacheWrapperException;
import org.springmodules.cache.EntryRetrievalException;
import org.springmodules.cache.key.CacheKey;
import org.springmodules.cache.mock.FixedValueCacheKey;
import org.springmodules.cache.provider.AbstractCacheProfileEditor;
import org.springmodules.cache.provider.CacheProfile;
import org.springmodules.cache.provider.CacheProfileValidator;
import org.springmodules.cache.provider.ehcache.EhcacheCacheProfile;

/**
 * <p>
 * Unit Test for <code>{@link JcsFacade}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/04/27 01:41:02 $
 */
public final class JcsFacadeTests extends TestCase {

  /**
   * Name of the cache (<code>{@link #mockCompositeCache}</code>).
   */
  private static final String CACHE_NAME = "CACHE";

  /**
   * Key used to store/retrieve an entry of the cache.
   */
  private CacheKey cacheKey;

  /**
   * Configuration options for the caching services.
   */
  private JcsCacheProfile cacheProfile;

  /**
   * Attributes of a JCS cache entry.
   */
  private IElementAttributes elementAttributes;

  /**
   * Primary object (instance of the class to test).
   */
  private JcsFacade jcsFacade;

  /**
   * Mock object that simulates a JCS cache.
   */
  private CompositeCache mockCompositeCache;

  /**
   * Controls the behavior of <code>{@link #mockCompositeCache}</code>.
   */
  private MockClassControl mockCompositeCacheControl;

  /**
   * Mock object that simulates a JCS cache manager.
   */
  private CompositeCacheManager mockCompositeCacheManager;

  /**
   * Controls the behavior of <code>{@link #mockCompositeCacheManager}</code>.
   */
  private MockClassControl mockCompositeCacheManagerControl;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the Test Case.
   */
  public JcsFacadeTests(String name) {
    super(name);
  }

  /**
   * Sets up the test fixture.
   */
  protected void setUp() throws Exception {
    super.setUp();

    this.cacheKey = new FixedValueCacheKey("KEY");
    this.jcsFacade = new JcsFacade();
  }

  /**
   * Sets up <code>{@link #cacheProfile}</code>.
   */
  private void setUpCacheProfile() {
    this.cacheProfile = new JcsCacheProfile();
    this.cacheProfile.setCacheName(CACHE_NAME);
  }

  /**
   * Sets up:
   * <ul>
   * <li><code>{@link #mockCompositeCache}</code></li>
   * <li><code>{@link #mockCompositeCacheControl}</code></li>
   * </ul>
   */
  private void setUpMockCompositeCache() throws Exception {
    // Create the proxy for the class 'CompositeManager'.
    Class[] constructorTypes = new Class[] { String.class,
        ICompositeCacheAttributes.class, IElementAttributes.class };

    ICompositeCacheAttributes cacheAttributes = new CompositeCacheAttributes();
    cacheAttributes.setCacheName(CACHE_NAME);
    cacheAttributes.setMaxObjects(10);
    cacheAttributes
        .setMemoryCacheName("org.apache.jcs.engine.memory.lru.LRUMemoryCache");
    this.elementAttributes = new ElementAttributes();
    Object[] constructorArgs = new Object[] { CACHE_NAME, cacheAttributes,
        this.elementAttributes };

    // set up the methods to mock.
    Class compositeCacheClass = CompositeCache.class;
    Method getElementAttributesMethod = compositeCacheClass.getMethod(
        "getElementAttributes", null);
    Method getMethod = compositeCacheClass.getMethod("get",
        new Class[] { Serializable.class });
    Method removeMethod = compositeCacheClass.getMethod("remove",
        new Class[] { Serializable.class });
    Method updateMethod = compositeCacheClass.getMethod("update",
        new Class[] { ICacheElement.class });
    Method[] mockedMethods = new Method[] { getElementAttributesMethod,
        getMethod, removeMethod, updateMethod };

    this.mockCompositeCacheControl = MockClassControl.createControl(
        compositeCacheClass, constructorTypes, constructorArgs, mockedMethods);
    this.mockCompositeCache = (CompositeCache) this.mockCompositeCacheControl
        .getMock();
  }

  /**
   * Sets up:
   * <ul>
   * <li><code>{@link #mockCompositeCacheManager}</code></li>
   * <li><code>{@link #mockCompositeCacheManagerControl}</code></li>
   * </ul>
   */
  private void setUpMockCompositeCacheManager() {
    // Create the proxy for the class 'CompositeCacheManager'.
    this.mockCompositeCacheManagerControl = MockClassControl.createControl(
        CompositeCacheManager.class, null, null);
    this.mockCompositeCacheManager = (CompositeCacheManager) this.mockCompositeCacheManagerControl
        .getMock();
    this.jcsFacade.setCacheManager(this.mockCompositeCacheManager);
  }

  /**
   * Verifies that the method
   * <code>{@link JcsFacade#getCacheProfileEditor()}</code> returns an
   * instance of <code>{@link JcsCacheProfileEditor}</code> not equal to
   * <code>null</code>.
   */
  public void testGetCacheProfileEditor() {
    AbstractCacheProfileEditor cacheProfileEditor = this.jcsFacade
        .getCacheProfileEditor();

    assertNotNull("The cache profile editor should not be null",
        cacheProfileEditor);

    Class expectedClass = JcsCacheProfileEditor.class;
    Class actualClass = cacheProfileEditor.getClass();

    assertEquals("<Class of the cache profile editor>", expectedClass,
        actualClass);
  }

  /**
   * Verifies that the method
   * <code>{@link JcsFacade#getCacheProfileValidator()}</code> returns an an
   * instance of <code>{@link JcsCacheProfileValidator}</code> not equal to
   * <code>null</code>.
   */
  public void testGetCacheProfileValidator() {
    CacheProfileValidator cacheProfileValidator = this.jcsFacade
        .getCacheProfileValidator();

    assertNotNull("The cache profile validator should not be null",
        cacheProfileValidator);

    Class expectedClass = JcsCacheProfileValidator.class;
    Class actualClass = cacheProfileValidator.getClass();

    assertEquals("<Class of the cache profile validator>", expectedClass,
        actualClass);
  }

  /**
   * Verifies that the method
   * <code>{@link JcsFacade#getKey(CacheKey, JcsCacheProfile)}</code> creates
   * a key containing the group specified in the given cache profile.
   */
  public void testGetKeyWithGroupName() {
    JcsCacheProfile profile = new JcsCacheProfile();
    profile.setCacheName("main");
    profile.setGroup("dev");

    GroupId groupId = new GroupId(profile.getCacheName(), profile.getGroup());
    GroupAttrName expectedKey = new GroupAttrName(groupId, this.cacheKey);

    Serializable actualKey = this.jcsFacade.getKey(this.cacheKey, profile);

    assertEquals("<Generated key>", expectedKey, actualKey);
  }

  /**
   * Verifies that the method
   * <code>{@link JcsFacade#getKey(CacheKey, JcsCacheProfile)}</code> creates
   * a key that does not contain the group if the given cache profile does not
   * specify any group.
   */
  public void testGetKeyWithoutGroupName() {
    JcsCacheProfile profile = new JcsCacheProfile();
    profile.setCacheName("main");

    Serializable actualKey = this.jcsFacade.getKey(this.cacheKey, profile);

    assertEquals("<Generated key>", this.cacheKey, actualKey);
  }

  /**
   * Tests <code>{@link JcsFacade#onFlushCache(JcsCacheProfile)}</code>.
   * Verifies that the cache is flushed if the cache profile contains both the
   * name of an existing cache and the name of an existing cache group.
   */
  public void testOnFlushCache() throws Exception {
    this.setUpMockCompositeCacheManager();
    this.setUpMockCompositeCache();
    this.setUpCacheProfile();

    // specify the name of the cache group.
    String groupName = "GROUP";
    this.cacheProfile.setGroup(groupName);

    // expectation: get the cache from the cache manager.
    this.mockCompositeCacheManager.getCache(CACHE_NAME);
    this.mockCompositeCacheManagerControl
        .setReturnValue(this.mockCompositeCache);

    // expectation: remove an entry from the cache.
    GroupId groupId = new GroupId(CACHE_NAME, groupName);
    this.mockCompositeCache.remove(groupId);
    this.mockCompositeCacheControl.setReturnValue(true);

    // set the state of the mock controls to 'replay'.
    this.mockCompositeCacheControl.replay();
    this.mockCompositeCacheManagerControl.replay();

    // execute the method to test.
    this.jcsFacade.onFlushCache(this.cacheProfile);

    // verify that the expectations of the mock controls were met.
    this.mockCompositeCacheControl.verify();
    this.mockCompositeCacheManagerControl.verify();
  }

  /**
   * Verifies that the method
   * <code>{@link JcsFacade#onFlushCache(JcsCacheProfile)}</code> does not try
   * to flush a cache that does not exist.
   */
  public void testOnFlushCacheWithCacheEqualToNull() throws Exception {
    this.setUpMockCompositeCacheManager();
    this.setUpMockCompositeCache();
    this.setUpCacheProfile();

    // expectation: get a cache from the cache manager. The cache manager does
    // not have a cache with the given name.
    this.mockCompositeCacheManager.getCache(CACHE_NAME);
    this.mockCompositeCacheManagerControl.setReturnValue(null);

    // set the state of the mock controls to 'replay'.
    this.mockCompositeCacheControl.replay();
    this.mockCompositeCacheManagerControl.replay();

    // execute the method to test.
    this.jcsFacade.onFlushCache(this.cacheProfile);

    // verify that the expectations of the mock controls were met.
    this.mockCompositeCacheControl.verify();
    this.mockCompositeCacheManagerControl.verify();
  }

  /**
   * Verifies that the method
   * <code>{@link JcsFacade#onFlushCache(EhcacheCacheProfile)}</code> does not
   * flush the cache if the cache profile does not specify a cache name.
   */
  public void testOnFlushCacheWithCacheProfileHavingEmptyCacheName()
      throws Exception {
    this.setUpMockCompositeCacheManager();
    this.setUpMockCompositeCache();

    // the cache profile does not specify a cache name. No cache should be
    // flushed.
    this.cacheProfile = new JcsCacheProfile();

    // set the state of the mock controls to 'replay'.
    this.mockCompositeCacheControl.replay();
    this.mockCompositeCacheManagerControl.replay();

    // execute the method to test.
    this.jcsFacade.onFlushCache(this.cacheProfile);

    // verify that the expectations of the mock controls were met.
    this.mockCompositeCacheControl.verify();
    this.mockCompositeCacheManagerControl.verify();
  }

  /**
   * Tests <code>{@link JcsFacade#onFlushCache(JcsCacheProfile)}</code>.
   * Verifies that the cache is not flushed if the name of the group specified
   * in the cache profile is empty.
   */
  public void testOnFlushCacheWithEmptyGroupName() throws Exception {
    this.setUpMockCompositeCacheManager();
    this.setUpMockCompositeCache();
    this.setUpCacheProfile();

    // specify the name of the cache group.
    this.cacheProfile.setGroup("");

    // expectation: get the cache from the cache manager.
    this.mockCompositeCacheManager.getCache(CACHE_NAME);
    this.mockCompositeCacheManagerControl
        .setReturnValue(this.mockCompositeCache);

    // set the state of the mock controls to 'replay'.
    this.mockCompositeCacheControl.replay();
    this.mockCompositeCacheManagerControl.replay();

    // execute the method to test.
    this.jcsFacade.onFlushCache(this.cacheProfile);

    // verify that the expectations of the mock controls were met.
    this.mockCompositeCacheControl.verify();
    this.mockCompositeCacheManagerControl.verify();
  }

  /**
   * Tests <code>{@link JcsFacade#onFlushCache(JcsCacheProfile)}</code>.
   * Verifies that the cache is not flushed if the name of the group specified
   * in the cache profile is <code>null</code>.
   */
  public void testOnFlushCacheWithGroupNameEqualToNull() throws Exception {
    this.setUpMockCompositeCacheManager();
    this.setUpMockCompositeCache();
    this.setUpCacheProfile();

    // specify the name of the cache group.
    this.cacheProfile.setGroup(null);

    // expectation: get the cache from the cache manager.
    this.mockCompositeCacheManager.getCache(CACHE_NAME);
    this.mockCompositeCacheManagerControl
        .setReturnValue(this.mockCompositeCache);

    // set the state of the mock controls to 'replay'.
    this.mockCompositeCacheControl.replay();
    this.mockCompositeCacheManagerControl.replay();

    // execute the method to test.
    this.jcsFacade.onFlushCache(this.cacheProfile);

    // verify that the expectations of the mock controls were met.
    this.mockCompositeCacheControl.verify();
    this.mockCompositeCacheManagerControl.verify();
  }

  /**
   * Verifies that the method
   * <code>{@link JcsFacade#onGetFromCache(Serializable, CacheProfile)}</code>
   * returns an entry from the cache if the entry exists under the given key.
   */
  public void testOnGetFromCacheWhenObjectToRetrieveIsCached() throws Exception {
    this.setUpMockCompositeCacheManager();
    this.setUpMockCompositeCache();
    this.setUpCacheProfile();

    // expectation: get a cache from the cache manager.
    this.mockCompositeCacheManager.getCache(CACHE_NAME);
    this.mockCompositeCacheManagerControl
        .setReturnValue(this.mockCompositeCache);

    // expectation: get the cache entry. The mock cache simulates that it has
    // an entry under the given key.
    Object cachedObject = "A String :)";
    ICacheElement cacheElement = new CacheElement(CACHE_NAME, this.cacheKey,
        cachedObject);

    this.mockCompositeCache.get(this.cacheKey);
    this.mockCompositeCacheControl.setReturnValue(cacheElement);

    // set the state of the mock controls to 'replay'.
    this.mockCompositeCacheControl.replay();
    this.mockCompositeCacheManagerControl.replay();

    // execute the method to test.
    Object returnedObjectByCache = this.jcsFacade.onGetFromCache(this.cacheKey,
        this.cacheProfile);

    assertSame("<Cached object>", cachedObject, returnedObjectByCache);

    // verify that the expectations of the mock controls were met.
    this.mockCompositeCacheControl.verify();
    this.mockCompositeCacheManagerControl.verify();

  }

  /**
   * Verifies that the
   * <code>{@link JcsFacade#onGetFromCache(Serializable, CacheProfile)}</code>
   * returns <code>null</code> if the cache does not contain an entry under
   * the given key.
   */
  public void testOnGetFromCacheWhenObjectToRetrieveIsNotCached()
      throws Exception {

    this.setUpMockCompositeCacheManager();
    this.setUpMockCompositeCache();
    this.setUpCacheProfile();

    // expectation: get a cache from the cache manager.
    this.mockCompositeCacheManager.getCache(CACHE_NAME);
    this.mockCompositeCacheManagerControl
        .setReturnValue(this.mockCompositeCache);

    // expectation: the cache does not contain an entry under the given name.
    this.mockCompositeCache.get(this.cacheKey);
    this.mockCompositeCacheControl.setReturnValue(null);

    // set the state of the mock controls to 'replay'.
    this.mockCompositeCacheControl.replay();
    this.mockCompositeCacheManagerControl.replay();

    // execute the method to test.
    Object returnedObjectByCache = this.jcsFacade.onGetFromCache(this.cacheKey,
        this.cacheProfile);

    assertNull("The cache should return a null entry", returnedObjectByCache);

    // verify that the expectations of the mock controls were met.
    this.mockCompositeCacheControl.verify();
    this.mockCompositeCacheManagerControl.verify();
  }

  /**
   * Verifies that the method
   * <code>{@link JcsFacade#onGetFromCache(Serializable, CacheProfile)}</code>
   * throws a <code>{@link EntryRetrievalException}</code> if the cache
   * mananger does not contain a cache with the given name.
   */
  public void testOnGetFromCacheWithCacheEqualToNull() throws Exception {
    this.setUpMockCompositeCacheManager();
    this.setUpMockCompositeCache();
    this.setUpCacheProfile();

    // expectation: get a cache from the cache manager. The cache manager does
    // not have a cache with the given name.
    this.mockCompositeCacheManager.getCache(CACHE_NAME);
    this.mockCompositeCacheManagerControl.setReturnValue(null);

    // set the state of the mock controls to 'replay'.
    this.mockCompositeCacheControl.replay();
    this.mockCompositeCacheManagerControl.replay();

    // execute the method to test.
    try {
      this.jcsFacade.onGetFromCache(this.cacheKey, this.cacheProfile);
      fail("An 'EntryRetrievalException' should have been thrown");
    } catch (EntryRetrievalException exception) {
      // we are expecting this exception.
    }

    // verify that the expectations of the mock controls were met.
    this.mockCompositeCacheControl.verify();
    this.mockCompositeCacheManagerControl.verify();
  }

  /**
   * Verifies that the method
   * <code>{@link JcsFacade#onGetFromCache(Serializable, CacheProfile)}</code>
   * throws a <code>{@link EntryRetrievalException}</code> if the cache
   * profile has a cache name equal to <code>null</code>.
   */
  public void testOnGetFromCacheWithCacheProfileHavingCacheNameEqualToNull() {
    // the cache profile does not have a cache name.
    this.cacheProfile = new JcsCacheProfile();
    this.cacheProfile.setCacheName(null);

    // execute the method to test.
    try {
      this.jcsFacade.onGetFromCache(this.cacheKey, this.cacheProfile);
      fail("An 'EntryRetrievalException' should have been thrown");
    } catch (EntryRetrievalException exception) {
      // we are expecting this exception.
    }
  }

  /**
   * Verifies that the method
   * <code>{@link JcsFacade#onGetFromCache(Serializable, CacheProfile)}</code>
   * throws a <code>{@link EntryRetrievalException}</code> if the cache
   * profile has an empty cache name.
   */
  public void testOnGetFromCacheWithCacheProfileHavingEmptyCacheName() {
    // the cache profile does not have a cache name.
    this.cacheProfile = new JcsCacheProfile();
    this.cacheProfile.setCacheName("");

    // execute the method to test.
    try {
      this.jcsFacade.onGetFromCache(this.cacheKey, this.cacheProfile);
      fail("An 'EntryRetrievalException' should have been thrown");
    } catch (EntryRetrievalException exception) {
      // we are expecting this exception.
    }
  }

  /**
   * Verifies that the method
   * <code>{@link JcsFacade#onPutInCache(Serializable, CacheProfile, Object)}</code>
   * stores the given object in the cache.
   */
  public void testOnPutInCache() throws Exception {
    this.setUpMockCompositeCacheManager();
    this.setUpMockCompositeCache();
    this.setUpCacheProfile();

    // expectation: get a cache from the cache manager.
    this.mockCompositeCacheManager.getCache(CACHE_NAME);
    this.mockCompositeCacheManagerControl
        .setReturnValue(this.mockCompositeCache);

    // expectation: get the default element attribute of the cache
    this.mockCompositeCache.getElementAttributes();
    this.mockCompositeCacheControl.setReturnValue(this.elementAttributes);

    IElementAttributes copyOfElementAttributes = this.elementAttributes.copy();

    // create the element to add from the object to cache.
    Object objectToCache = "A String :)";
    ICacheElement newCacheElement = new JcsCacheElement(CACHE_NAME,
        this.cacheKey, objectToCache);
    newCacheElement.setElementAttributes(copyOfElementAttributes);

    // expectation: add the element to the cache.
    this.mockCompositeCache.update(newCacheElement);

    // set the state of the mock controls to 'replay'.
    this.mockCompositeCacheControl.replay();
    this.mockCompositeCacheManagerControl.replay();

    // execute the method to test.
    this.jcsFacade
        .onPutInCache(this.cacheKey, this.cacheProfile, objectToCache);

    // verify that the expectations of the mock controls were met.
    this.mockCompositeCacheControl.verify();
    this.mockCompositeCacheManagerControl.verify();
  }

  /**
   * Verifies that the method
   * <code>{@link JcsFacade#onPutInCache(Serializable, CacheProfile, Object)}</code>
   * throws a <code>{@link CacheWrapperException}</code> wrapping any
   * exception thrown by the cache.
   */
  public void testOnPutInCacheWhenCacheThrowsException() throws Exception {
    this.setUpMockCompositeCacheManager();
    this.setUpMockCompositeCache();
    this.setUpCacheProfile();

    // expectation: get a cache from the cache manager.
    this.mockCompositeCacheManager.getCache(CACHE_NAME);
    this.mockCompositeCacheManagerControl
        .setReturnValue(this.mockCompositeCache);

    // expectation: get the default element attribute of the cache
    this.mockCompositeCache.getElementAttributes();
    this.mockCompositeCacheControl.setReturnValue(this.elementAttributes);

    IElementAttributes copyOfElementAttributes = this.elementAttributes.copy();

    // create the element to add from the object to cache.
    Object objectToCache = "A String :)";
    ICacheElement newCacheElement = new JcsCacheElement(CACHE_NAME,
        this.cacheKey, objectToCache);
    newCacheElement.setElementAttributes(copyOfElementAttributes);

    // expectation: add the element to the cache. Throw an exception to simulate
    // an I/0 error.
    this.mockCompositeCache.update(newCacheElement);
    this.mockCompositeCacheControl.setThrowable(new IOException(
        "Thrown for testing"));

    // set the state of the mock controls to 'replay'.
    this.mockCompositeCacheControl.replay();
    this.mockCompositeCacheManagerControl.replay();

    // execute the method to test.
    try {
      this.jcsFacade.onPutInCache(this.cacheKey, this.cacheProfile,
          objectToCache);
      fail("An 'CacheWrapperException' should have been thrown");
    } catch (CacheWrapperException cacheWrapperException) {
      // we are expecting this exception
    }

    // verify that the expectations of the mock controls were met.
    this.mockCompositeCacheControl.verify();
    this.mockCompositeCacheManagerControl.verify();
  }

  /**
   * Verifies that the method
   * <code>{@link JcsFacade#onPutInCache(Serializable, CacheProfile, Object)}</code>
   * does not store the given object if the cache manager does not contain a
   * cache with the given name.
   */
  public void testOnPutInCacheWithCacheEqualToNull() throws Exception {
    this.setUpMockCompositeCacheManager();
    this.setUpMockCompositeCache();
    this.setUpCacheProfile();

    Object objectToCache = "A String :)";

    // expectation: get a cache from the cache manager. The cache manager does
    // not have a cache with the given name.
    this.mockCompositeCacheManager.getCache(CACHE_NAME);
    this.mockCompositeCacheManagerControl.setReturnValue(null);

    // set the state of the mock controls to 'replay'.
    this.mockCompositeCacheManagerControl.replay();
    this.mockCompositeCacheControl.replay();

    // execute the method to test.
    this.jcsFacade
        .onPutInCache(this.cacheKey, this.cacheProfile, objectToCache);

    // verify that the expectations of the mock controls were met.
    this.mockCompositeCacheManagerControl.verify();
    this.mockCompositeCacheControl.verify();
  }

  /**
   * Verifies that the method
   * <code>{@link JcsFacade#onPutInCache(Serializable, CacheProfile, Object)}</code>
   * does not store the given object if the cache profile has a cache name equal
   * to <code>null</code>.
   */
  public void testOnPutInCacheWithCacheProfileHavingCacheNameEqualToNull()
      throws Exception {
    this.setUpMockCompositeCacheManager();
    this.setUpMockCompositeCache();
    this.cacheProfile = new JcsCacheProfile();
    this.cacheProfile.setCacheName(null);

    Object objectToCache = "A String :)";

    // set the state of the mock controls to 'replay'.
    this.mockCompositeCacheManagerControl.replay();
    this.mockCompositeCacheControl.replay();

    // execute the method to test.
    this.jcsFacade
        .onPutInCache(this.cacheKey, this.cacheProfile, objectToCache);

    // verify that the expectations of the mock controls were met.
    this.mockCompositeCacheManagerControl.verify();
    this.mockCompositeCacheControl.verify();
  }

  /**
   * Verifies that the method
   * <code>{@link JcsFacade#onPutInCache(Serializable, CacheProfile, Object)}</code>
   * does not store the given object if the cache profile has an empty cache
   * name.
   */
  public void testOnPutInCacheWithCacheProfileHavingEmptyCacheName()
      throws Exception {
    this.setUpMockCompositeCacheManager();
    this.setUpMockCompositeCache();
    this.cacheProfile = new JcsCacheProfile();
    this.cacheProfile.setCacheName("");

    Object objectToCache = "A String :)";

    // set the state of the mock controls to 'replay'.
    this.mockCompositeCacheManagerControl.replay();
    this.mockCompositeCacheControl.replay();

    // execute the method to test.
    this.jcsFacade
        .onPutInCache(this.cacheKey, this.cacheProfile, objectToCache);

    // verify that the expectations of the mock controls were met.
    this.mockCompositeCacheManagerControl.verify();
    this.mockCompositeCacheControl.verify();
  }

  /**
   * Verifies that the method
   * <code>{@link JcsFacade#removeFromCache(Serializable, String)}</code>
   * removes from the cache the object stored under the given key.
   */
  public void testRemoveFromCache() throws Exception {
    this.setUpMockCompositeCacheManager();
    this.setUpMockCompositeCache();
    this.setUpCacheProfile();
    String cacheProfileId = "myId";

    Map cacheProfiles = new HashMap();
    cacheProfiles.put(cacheProfileId, this.cacheProfile);
    this.jcsFacade.setCacheProfiles(cacheProfiles);

    // expectation: get a cache from the cache manager.
    this.mockCompositeCacheManager.getCache(CACHE_NAME);
    this.mockCompositeCacheManagerControl
        .setReturnValue(this.mockCompositeCache);

    // expectation: remove the object stored under the given key.
    this.mockCompositeCache.remove(this.cacheKey);
    this.mockCompositeCacheControl.setReturnValue(true);

    // set the state of the mock controls to 'replay'.
    this.mockCompositeCacheControl.replay();
    this.mockCompositeCacheManagerControl.replay();

    // execute the method to test.
    this.jcsFacade.removeFromCache(this.cacheKey, cacheProfileId);

    // verify that the expectations of the mock controls were met.
    this.mockCompositeCacheManagerControl.verify();
    this.mockCompositeCacheControl.verify();
  }

  /**
   * Verifies that the method
   * <code>{@link JcsFacade#removeFromCache(Serializable, String)}</code> does
   * not remove any object from the cache if there is not any cache profile
   * stored under the given id.
   */
  public void testRemoveFromCacheWithCacheProfileIsNull() throws Exception {
    this.setUpMockCompositeCacheManager();
    this.setUpMockCompositeCache();
    this.setUpCacheProfile();

    this.jcsFacade.setCacheProfiles(new HashMap());

    // set the state of the mock controls to 'replay'.
    this.mockCompositeCacheControl.replay();
    this.mockCompositeCacheManagerControl.replay();

    // execute the method to test.
    this.jcsFacade.removeFromCache(this.cacheKey, "myId");

    // verify that the expectations of the mock controls were met.
    this.mockCompositeCacheManagerControl.verify();
    this.mockCompositeCacheControl.verify();
  }

  /**
   * Verifies that the method
   * <code>{@link JcsFacade#removeFromCache(Serializable, String)}</code> does
   * not remove any object from the cache if the cache profile does not have the
   * name of the cache to use.
   */
  public void testRemoveFromCacheWithEmptyCacheName() throws Exception {
    this.setUpMockCompositeCacheManager();
    this.setUpMockCompositeCache();

    this.cacheProfile = new JcsCacheProfile();
    String cacheProfileId = "myId";

    Map cacheProfiles = new HashMap();
    cacheProfiles.put(cacheProfileId, this.cacheProfile);
    this.jcsFacade.setCacheProfiles(cacheProfiles);

    // set the state of the mock controls to 'replay'.
    this.mockCompositeCacheControl.replay();
    this.mockCompositeCacheManagerControl.replay();

    // execute the method to test.
    this.jcsFacade.removeFromCache(this.cacheKey, cacheProfileId);

    // verify that the expectations of the mock controls were met.
    this.mockCompositeCacheManagerControl.verify();
    this.mockCompositeCacheControl.verify();
  }

  /**
   * Verifies that the method
   * <code>{@link JcsFacade#validateCacheManager()}</code> throws an
   * <code>IllegalStateException</code> the cache manager is <code>null</code>.
   */
  public void testValidateCacheManagerWithCacheManagerEqualToNull() {
    this.jcsFacade.setCacheManager(null);
    try {
      this.jcsFacade.validateCacheManager();
      fail("An 'IllegalStateException' should have been thrown");
    } catch (IllegalStateException exception) {
      // we are expecting this exception.
    }
  }

  /**
   * Verifies that the method
   * <code>{@link JcsFacade#validateCacheManager()}</code> does not throw any
   * exception if the cache manager is not <code>null</code>.
   */
  public void testValidateCacheManagerWithCacheManagerNotEqualToNull()
      throws Exception {

    this.setUpMockCompositeCacheManager();
    this.setUpMockCompositeCache();

    this.jcsFacade.validateCacheManager();
  }
}