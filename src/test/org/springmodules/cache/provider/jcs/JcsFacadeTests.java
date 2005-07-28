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
package org.springmodules.cache.provider.jcs;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import org.springmodules.cache.provider.AbstractCacheProfileEditor;
import org.springmodules.cache.provider.CacheProfileValidator;

/**
 * <p>
 * Unit Test for <code>{@link JcsFacade}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.6 $ $Date: 2005/07/28 03:43:29 $
 */
public final class JcsFacadeTests extends TestCase {

  /**
   * A JCS Cache.
   */
  private CompositeCache cache;

  /**
   * Controls the behavior of <code>{@link #cache}</code> if initialized as a
   * mock object.
   */
  private MockClassControl cacheControl;

  /**
   * Keys used to store/retrieve entries of the cache.
   */
  private String[] cacheKeys;

  /**
   * JCS Cache Manager.
   */
  private CompositeCacheManager cacheManager;

  /**
   * Controls the behavior of <code>{@link #cacheManager}</code> if
   * initialized as a mock object.
   */
  private MockClassControl cacheManagerControl;

  /**
   * Name of the JCS to use.
   */
  private String cacheName;

  /**
   * Ids used by <code>{@link #jcsFacade}</code> to obtain any of the cache
   * profiles in <code>{@link #cacheProfiles}</code>.
   */
  private String[] cacheProfileIds;

  /**
   * Configuration options for the caching services.
   */
  private JcsProfile[] cacheProfiles;

  /**
   * Name of the groups in <code>{@link #cache}</code> to use.
   */
  private String[] groups;

  /**
   * Primary object that is under test.
   */
  private JcsFacade jcsFacade;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case to construct.
   */
  public JcsFacadeTests(String name) {
    super(name);
  }

  /**
   * Creates a new element that can be added to <code>{@link #cache}</code>.
   * 
   * @param key
   *          the key of the cache entry.
   * @param objectToCache
   *          the object to store in the cache.
   * @return a new element that can be stored in the cache.
   */
  protected ICacheElement createNewCacheElement(Serializable key,
      Object objectToCache) {
    ICacheElement newCacheElement = new CacheElement(this.cache.getCacheName(),
        key, objectToCache);

    IElementAttributes elementAttributes = this.cache.getElementAttributes()
        .copy();

    newCacheElement.setElementAttributes(elementAttributes);
    return newCacheElement;
  }

  /**
   * Sets up the test fixture.
   */
  protected void setUp() throws Exception {
    super.setUp();

    this.cacheKeys = new String[] { "firstKey", "secondKey" };

    this.cacheManager = CompositeCacheManager.getInstance();
    this.cacheName = "testCache";

    this.cache = this.cacheManager.getCache(this.cacheName);

    this.cacheProfiles = new JcsProfile[] { new JcsProfile(), new JcsProfile() };
    this.cacheProfileIds = new String[] { "firstProfile", "secondProfile" };
    this.groups = new String[] { "firstGroup", "secondGroup" };

    Map cacheProfileMap = new HashMap();

    // initialize the cache profiles.
    int cacheProfileCount = this.cacheProfiles.length;
    for (int i = 0; i < cacheProfileCount; i++) {
      JcsProfile cacheProfile = this.cacheProfiles[i];
      cacheProfile.setCacheName(this.cacheName);
      cacheProfile.setGroup(this.groups[i]);

      cacheProfileMap.put(this.cacheProfileIds[i], cacheProfile);
    }

    this.jcsFacade = new JcsFacade();
    this.jcsFacade.setCacheManager(this.cacheManager);
    this.jcsFacade.setCacheProfiles(cacheProfileMap);

    int expectedElementCount = 2;
    assertEquals("<Key count>", expectedElementCount, this.cacheKeys.length);
    assertEquals("<Cache profile count>", expectedElementCount,
        this.cacheProfiles.length);
    assertEquals("<Cache profile id count>", expectedElementCount,
        this.cacheProfileIds.length);
    assertEquals("<Group count>", expectedElementCount, this.groups.length);
    assertEquals("<Stored cache profile count>", expectedElementCount,
        cacheProfileMap.size());
  }

  /**
   * Sets up <code>{@link #cacheManager}</code> as a mock object.
   * 
   * @param methodToMock
   *          the method to mock.
   */
  private void setUpCacheAdministratorAsMockObject(Method methodToMock) {
    this.setUpCacheAdministratorAsMockObject(new Method[] { methodToMock });
  }

  /**
   * Sets up <code>{@link #cacheManager}</code> as a mock object.
   * 
   * @param methodsToMock
   *          the methods to mock.
   */
  private void setUpCacheAdministratorAsMockObject(Method[] methodsToMock) {
    Class targetClass = CompositeCacheManager.class;

    this.cacheManagerControl = MockClassControl.createControl(targetClass,
        null, null, methodsToMock);
    this.cacheManager = (CompositeCacheManager) this.cacheManagerControl
        .getMock();

    this.jcsFacade.setCacheManager(this.cacheManager);
  }

  /**
   * Sets up <code>{@link #cache}</code> as a mock object
   * 
   * @param methodToMock
   *          the methods to mock.
   */
  private void setUpCacheAsMockObject(Method methodToMock) throws Exception {
    this.setUpCacheAsMockObject(new Method[] { methodToMock });
  }

  /**
   * Sets up <code>{@link #cache}</code> as a mock object
   * 
   * @param methodsToMock
   *          the methods to mock.
   */
  private void setUpCacheAsMockObject(Method[] methodsToMock) throws Exception {
    // Create the proxy for the class 'CompositeManager'.
    Class[] constructorTypes = new Class[] { String.class,
        ICompositeCacheAttributes.class, IElementAttributes.class };

    ICompositeCacheAttributes cacheAttributes = new CompositeCacheAttributes();
    cacheAttributes.setCacheName(this.cacheName);
    cacheAttributes.setMaxObjects(10);
    cacheAttributes
        .setMemoryCacheName("org.apache.jcs.engine.memory.lru.LRUMemoryCache");
    ElementAttributes elementAttributes = new ElementAttributes();
    Object[] constructorArgs = new Object[] { this.cacheName, cacheAttributes,
        elementAttributes };

    // set up the methods to mock.
    Class targetClass = CompositeCache.class;

    this.cacheControl = MockClassControl.createControl(targetClass,
        constructorTypes, constructorArgs, methodsToMock);
    this.cache = (CompositeCache) this.cacheControl.getMock();
  }

  /**
   * Tears down the test fixture.
   */
  protected void tearDown() throws Exception {
    super.tearDown();

    if (this.cacheManagerControl == null) {
      this.cacheManager.shutDown();
    }
  }

  /**
   * Verifies that the method
   * <code>{@link JcsFacade#getCacheProfileEditor()}</code> returns an
   * instance of <code>{@link JcsProfileEditor}</code> not equal to
   * <code>null</code>.
   */
  public void testGetCacheProfileEditor() {
    AbstractCacheProfileEditor cacheProfileEditor = this.jcsFacade
        .getCacheProfileEditor();

    assertNotNull("The cache profile editor should not be null",
        cacheProfileEditor);

    Class expectedClass = JcsProfileEditor.class;
    Class actualClass = cacheProfileEditor.getClass();

    assertEquals("<Class of the cache profile editor>", expectedClass,
        actualClass);
  }

  /**
   * Verifies that the method
   * <code>{@link JcsFacade#getCacheProfileValidator()}</code> returns an an
   * instance of <code>{@link JcsProfileValidator}</code> not equal to
   * <code>null</code>.
   */
  public void testGetCacheProfileValidator() {
    CacheProfileValidator cacheProfileValidator = this.jcsFacade
        .getCacheProfileValidator();

    assertNotNull("The cache profile validator should not be null",
        cacheProfileValidator);

    Class expectedClass = JcsProfileValidator.class;
    Class actualClass = cacheProfileValidator.getClass();

    assertEquals("<Class of the cache profile validator>", expectedClass,
        actualClass);
  }

  /**
   * Verifies that the method
   * <code>{@link JcsFacade#getKey(Serializable, JcsProfile)}</code> creates a
   * key containing the group specified in the given cache profile.
   */
  public void testGetKeyWithGroupName() {
    JcsProfile profile = new JcsProfile();
    profile.setCacheName("main");
    profile.setGroup("dev");

    int i = 0;
    GroupId groupId = new GroupId(profile.getCacheName(), profile.getGroup());
    GroupAttrName expectedKey = new GroupAttrName(groupId, this.cacheKeys[i]);

    Serializable actualKey = this.jcsFacade.getKey(this.cacheKeys[i], profile);

    assertEquals("<Generated key>", expectedKey, actualKey);
  }

  /**
   * Verifies that the method
   * <code>{@link JcsFacade#getKey(Serializable, JcsProfile)}</code> creates a
   * key that does not contain the group if the given cache profile does not
   * specify any group.
   */
  public void testGetKeyWithoutGroupName() {
    JcsProfile profile = new JcsProfile();
    profile.setCacheName("main");

    int i = 0;
    Serializable actualKey = this.jcsFacade.getKey(this.cacheKeys[i], profile);

    assertEquals("<Generated key>", this.cacheKeys[i], actualKey);
  }

  /**
   * Verifies that the method
   * <code>{@link JcsFacade#onFlushCache(org.springmodules.cache.provider.CacheProfile)}</code>
   * does not remove any entries if the cache manager cannot find a cache under
   * the given name.
   */
  public void testOnFlushCacheWhenCacheManagerReturnsCacheEqualToNull()
      throws Exception {

    Method getCacheMethod = CompositeCacheManager.class.getDeclaredMethod(
        "getCache", new Class[] { String.class });

    this.setUpCacheAdministratorAsMockObject(getCacheMethod);

    // expectation: cache manager returns a cache equal to null.
    this.cacheManager.getCache(this.cacheName);
    this.cacheManagerControl.setReturnValue(null);

    // set the state of the mock control to "replay".
    this.cacheManagerControl.replay();

    // execute the method to test.
    this.jcsFacade.onFlushCache(this.cacheProfiles[0]);

    // verify that the expectations of the mock control were met.
    this.cacheManagerControl.verify();
  }

  /**
   * Verifies that the method
   * <code>{@link JcsFacade#onFlushCache(org.springmodules.cache.provider.CacheProfile)}</code>
   * throws a <code>{@link CacheWrapperException}</code> wraps the exception
   * thrown by JCS when flushing the cache.
   */
  public void testOnFlushCacheWhenCacheManagerThrowsExceptionWhenRemovingGroup()
      throws Exception {
    Class targetClass = CompositeCacheManager.class;
    Method getCacheMethod = targetClass.getDeclaredMethod("getCache",
        new Class[] { String.class });
    this.setUpCacheAdministratorAsMockObject(getCacheMethod);

    targetClass = CompositeCache.class;
    Method removeAllMethod = targetClass.getMethod("removeAll", null);
    this.setUpCacheAsMockObject(removeAllMethod);

    // expectation: cache manager finds the cache we are looking for.
    this.cacheManager.getCache(this.cacheName);
    this.cacheManagerControl.setReturnValue(this.cache);

    // expectation: cache manager throws exception when flushing the cache.
    RuntimeException thrownException = new RuntimeException();
    this.cache.removeAll();
    this.cacheControl.setThrowable(thrownException);

    // set the state of the mock control to "replay".
    this.cacheManagerControl.replay();
    this.cacheControl.replay();

    // execute the method to test.
    try {
      JcsProfile profile = new JcsProfile(this.cacheName);
      this.jcsFacade.onFlushCache(profile);
      fail("A 'CacheWrapperException' should have been thrown");
    } catch (CacheWrapperException exception) {
      Throwable rootException = exception.getCause();

      logger.debug("Root exception: ", rootException);

      assertSame("<Nested exception>", thrownException, rootException);
    }

    // verify that the expectations of the mock control were met.
    this.cacheManagerControl.verify();
    this.cacheControl.verify();
  }

  /**
   * Message logger.
   */
  private static Log logger = LogFactory.getLog(JcsFacadeTests.class);

  /**
   * Verifies that the method
   * <code>{@link JcsFacade#onFlushCache(org.springmodules.cache.provider.CacheProfile)}</code>
   * does not flush any cache or group if the name of thec cache specified in
   * the given cache profile is empty.
   */
  public void testOnFlushCacheWhenCacheNameIsEmpty() throws Exception {
    this.updateCache(new Object[] { "firstObject", "secondObject" });

    // execute the method to test.
    int i = 0;
    this.cacheProfiles[i].setCacheName("");
    this.jcsFacade.onFlushCache(this.cacheProfiles[i]);

    assertEquals("<Cache size>", this.cacheKeys.length, this.cache.getSize());
  }

  /**
   * Verifies that the method
   * <code>{@link JcsFacade#onFlushCache(org.springmodules.cache.provider.CacheProfile)}</code>
   * flushes only the specified groups of the specified cache.
   */
  public void testOnFlushCacheWithGroups() throws Exception {
    Serializable[] entryKeys = this.updateCache(new Object[] { "firstObject",
        "secondObject" });

    int i = 0;
    JcsProfile cacheProfile = this.cacheProfiles[i];
    Serializable entryKey = entryKeys[i];
    String group = this.groups[i];

    // execute the method to test.
    this.jcsFacade.onFlushCache(cacheProfile);

    // only one group should have been flushed.
    ICacheElement cachedElement = this.cache.get(entryKey);
    assertNull("The group '" + group + "' should be flushed", cachedElement);

    entryKey = entryKeys[++i];
    group = this.groups[i];

    cachedElement = this.cache.get(entryKey);
    assertNotNull("The group '" + group + "' should not be flushed",
        cachedElement);
  }

  /**
   * Verifies that the method
   * <code>{@link JcsFacade#onFlushCache(org.springmodules.cache.provider.CacheProfile)}</code>
   * flushes the whole cache if there are not any specified groups.
   */
  public void testOnFlushCacheWithoutGroups() throws Exception {
    this.updateCache(new Object[] { "firstObject", "secondObject" });
    assertTrue("The size of the cache should be greater than zero", this.cache
        .getSize() > 0);

    JcsProfile cacheProfile = this.cacheProfiles[0];
    cacheProfile.setGroup(null);

    // execute the method to test.
    this.jcsFacade.onFlushCache(cacheProfile);

    // the whole cache should be flushed.
    assertEquals("<Cache size>", 0, this.cache.getSize());
  }

  /**
   * Verifies that the method
   * <code>{@link JcsFacade#onGetFromCache(java.io.Serializable, org.springmodules.cache.provider.CacheProfile)}</code>
   * retrieves, from the cache specified in the given cache profile, the entry
   * stored under the given key.
   */
  public void testOnGetFromCache() throws Exception {
    String objectToStore = "An Object";
    this.updateCache(new Object[] { objectToStore });

    int i = 0;
    String cacheKey = this.cacheKeys[i];
    JcsProfile cacheProfile = this.cacheProfiles[i];

    // execute the method to test.
    Object cachedObject = this.jcsFacade.onGetFromCache(cacheKey, cacheProfile);

    assertSame("<Cached object>", objectToStore, cachedObject);
  }

  /**
   * Verifies that the method
   * <code>{@link JcsFacade#onGetFromCache(java.io.Serializable, org.springmodules.cache.provider.CacheProfile)}</code>
   * returns <code>null</code> if the name of the cache, specified in the
   * given cache profile, is empty.
   */
  public void testOnGetFromCacheWhenCacheNameIsEmpty() throws Exception {
    int i = 0;
    String cacheKey = this.cacheKeys[i];
    JcsProfile profile = this.cacheProfiles[i];
    profile.setCacheName("");

    // execute the method to test.
    Object cachedObject = this.jcsFacade.onGetFromCache(cacheKey, profile);

    assertNull("The retrieved object should be null", cachedObject);
  }

  /**
   * Verifies that the method
   * <code>{@link JcsFacade#onGetFromCache(java.io.Serializable, org.springmodules.cache.provider.CacheProfile)}</code>
   * returns <code>null</code> if the specified key does not exist in the
   * cache.
   */
  public void testOnGetFromCacheWhenKeyIsNotFound() throws Exception {
    int i = 0;
    String cacheKey = "NonExistingKey";
    JcsProfile profile = this.cacheProfiles[i];

    // execute the method to test.
    Object cachedObject = this.jcsFacade.onGetFromCache(cacheKey, profile);

    assertNull("The retrieved object should be null", cachedObject);
  }

  /**
   * Verifies that the method
   * <code>{@link JcsFacade#onPutInCache(java.io.Serializable, org.springmodules.cache.provider.CacheProfile, Object)}</code>
   * stores an entry in the cache specified in the given cache profile using the
   * given key.
   */
  public void testOnPutInCache() throws Exception {
    int i = 0;
    String cacheKey = this.cacheKeys[i];
    JcsProfile cacheProfile = this.cacheProfiles[i];
    String objectToStore = "An Object";

    // execute the method to test.
    this.jcsFacade.onPutInCache(cacheKey, cacheProfile, objectToStore);

    Serializable entryKey = this.jcsFacade.getKey(cacheKey, cacheProfile);
    Object cachedObject = this.cache.get(entryKey).getVal();
    assertSame("<Cached object>", objectToStore, cachedObject);
  }

  /**
   * Verifies that the method
   * <code>{@link JcsFacade#onPutInCache(java.io.Serializable, org.springmodules.cache.provider.CacheProfile, Object)}</code>
   * does not store any entry in any cache if the name of the cache, specified
   * in the given cache profile, is empty. given key.
   */
  public void testOnPutInCacheWhenCacheNameIsEmpty() throws Exception {
    int i = 0;
    String cacheKey = this.cacheKeys[i];
    JcsProfile cacheProfile = this.cacheProfiles[i];
    cacheProfile.setCacheName("");

    // execute the method to test.
    this.jcsFacade.onPutInCache(cacheKey, cacheProfile, "An Object");

    Serializable entryKey = this.jcsFacade.getKey(cacheKey, cacheProfile);
    ICacheElement cacheElement = this.cache.get(entryKey);
    assertNull("The retrieved object should be null", cacheElement);
  }

  /**
   * Verifies that the method
   * <code>{@link JcsFacade#removeFromCache(java.io.Serializable, String)}</code>
   * removes the entry stored under the given key from the cache specified in
   * the given cache profile.
   */
  public void testRemoveFromCache() throws Exception {
    Serializable[] entryKeys = this.updateCache(new Object[] { "An Object" });

    int i = 0;
    Serializable entryKey = entryKeys[i];
    String cacheKey = this.cacheKeys[i];
    String cacheProfileId = this.cacheProfileIds[i];

    // execute the method to test.
    this.jcsFacade.removeFromCache(cacheKey, cacheProfileId);

    ICacheElement cacheElement = this.cache.get(entryKey);
    assertNull("The element with key '" + cacheKey
        + "' should have been removed from the cache", cacheElement);
  }

  /**
   * Verifies that the method
   * <code>{@link JcsFacade#removeFromCache(java.io.Serializable, String)}</code>
   * does not remove any entry if the name of the cache, specified in the given
   * cache profile, is empty.
   */
  public void testRemoveFromCacheWhenCacheNameIsEmpty() throws Exception {
    Serializable[] entryKeys = this.updateCache(new Object[] { "An Object" });

    int i = 0;
    Serializable entryKey = entryKeys[i];
    String cacheKey = this.cacheKeys[i];
    String cacheProfileId = this.cacheProfileIds[i];
    JcsProfile cacheProfile = this.cacheProfiles[i];
    cacheProfile.setCacheName("");

    // execute the method to test.
    this.jcsFacade.removeFromCache(cacheKey, cacheProfileId);

    ICacheElement cacheElement = this.cache.get(entryKey);
    assertNotNull("The element with key '" + cacheKey
        + "' should not have been removed from the cache", cacheElement);
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
    this.jcsFacade.validateCacheManager();
  }

  /**
   * Stores in the cache each element of the given array.
   * 
   * @param objectsToStore
   *          the array containing the objects to store in the cache.
   * @return the keys used to store the objects in the cache.
   */
  protected Serializable[] updateCache(Object[] objectsToStore)
      throws Exception {
    int objectsToStoreCount = objectsToStore.length;
    int cacheKeyCount = this.cacheKeys.length;

    assertTrue("There should be an object to store in the cache",
        objectsToStoreCount > 0);
    assertTrue("There should be no more than " + cacheKeyCount
        + "objects to store in the cache", objectsToStoreCount <= cacheKeyCount);

    Serializable[] entryKeys = new Serializable[objectsToStoreCount];

    for (int i = 0; i < objectsToStoreCount; i++) {
      String cacheKey = this.cacheKeys[i];
      JcsProfile cacheProfile = this.cacheProfiles[i];
      Serializable entryKey = this.jcsFacade.getKey(cacheKey, cacheProfile);
      Object objectToStore = objectsToStore[i];

      ICacheElement cacheElement = this.createNewCacheElement(entryKey,
          objectToStore);
      this.cache.update(cacheElement);
      entryKeys[i] = entryKey;
    }

    return entryKeys;
  }
}
