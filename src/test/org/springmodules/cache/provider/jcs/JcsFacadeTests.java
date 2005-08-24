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
import org.easymock.AbstractMatcher;
import org.easymock.classextension.MockClassControl;
import org.springmodules.cache.provider.AbstractCacheProfileEditor;
import org.springmodules.cache.provider.CacheAccessException;
import org.springmodules.cache.provider.CacheNotFoundException;
import org.springmodules.cache.provider.CacheProfileValidator;
import org.springmodules.cache.provider.InvalidConfigurationException;

/**
 * <p>
 * Unit Tests for <code>{@link JcsFacade}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.11 $ $Date: 2005/08/24 01:18:49 $
 */
public final class JcsFacadeTests extends TestCase {

  private class CacheElementMatcher extends AbstractMatcher {
    /**
     * @see AbstractMatcher#argumentMatches(Object, Object)
     */
    protected boolean argumentMatches(Object expected, Object actual) {
      if (!(expected instanceof CacheElement)) {
        throw new IllegalArgumentException(
            "Element matcher only evaluates instances of <"
                + CacheElement.class.getName() + ">");
      }
      if (!(actual instanceof CacheElement)) {
        return false;
      }
      CacheElement expectedElement = (CacheElement) expected;
      CacheElement actualElement = (CacheElement) actual;

      Serializable expectedKey = expectedElement.getKey();
      Object expectedValue = expectedElement.getVal();

      Serializable actualKey = actualElement.getKey();
      Object actualValue = actualElement.getVal();

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

  private CompositeCache cache;

  private MockClassControl cacheControl;

  /**
   * Keys used to store/retrieve entries of the cache.
   */
  private String[] cacheKeys;

  private CompositeCacheManager cacheManager;

  private MockClassControl cacheManagerControl;

  private String cacheName;

  /**
   * Ids used by <code>{@link #jcsFacade}</code> to obtain any of the cache
   * profiles in <code>{@link #cacheProfiles}</code>.
   */
  private String[] cacheProfileIds;

  private JcsProfile[] cacheProfiles;

  /**
   * Name of the groups in <code>{@link #cache}</code> to use.
   */
  private String[] groups;

  /**
   * Primary object that is under test.
   */
  private JcsFacade jcsFacade;

  public JcsFacadeTests(String name) {
    super(name);
  }

  private void assertEqualGeneratedKeys(Serializable expected,
      Serializable actual) {
    assertEquals("<Generated key>", expected, actual);
  }

  private void assertSameNestedException(Exception rootException,
      Exception expectedNestedException) {
    assertSame("<Nested exception>", expectedNestedException, rootException
        .getCause());
  }

  protected ICacheElement createNewCacheElement(Serializable key,
      Object objectToCache) {
    ICacheElement newCacheElement = new CacheElement(this.cache.getCacheName(),
        key, objectToCache);

    IElementAttributes elementAttributes = this.cache.getElementAttributes()
        .copy();

    newCacheElement.setElementAttributes(elementAttributes);
    return newCacheElement;
  }

  private void expectCacheManagerDoesNotHaveCache() {
    this.cacheManager.getCache(this.cacheName);
    this.cacheManagerControl.setReturnValue(null);
  }

  private void failIfCacheAccessExceptionIsNotThrown() {
    fail("Expecting exception <" + CacheAccessException.class.getName() + ">");
  }

  private void failIfCacheNotFoundExceptionIsNotThrown() {
    fail("Expecting exception <" + CacheNotFoundException.class.getName() + ">");
  }

  private String getFirstCacheKey() {
    return this.cacheKeys[0];
  }

  private JcsProfile getFirstCacheProfile() {
    return this.cacheProfiles[0];
  }

  private Method getGetCacheMethodFromCompositeCacheManagerClass()
      throws Exception {
    return CompositeCacheManager.class.getDeclaredMethod("getCache",
        new Class[] { String.class });
  }

  private void setStateOfMockControlsToReplay() {
    if (this.cacheControl != null) {
      this.cacheControl.replay();
    }
    this.cacheManagerControl.replay();
  }

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

  private void setUpCacheAdministratorAsMockObject(Method methodToMock) {
    this.setUpCacheAdministratorAsMockObject(new Method[] { methodToMock });
  }

  private void setUpCacheAdministratorAsMockObject(Method[] methodsToMock) {
    Class targetClass = CompositeCacheManager.class;

    this.cacheManagerControl = MockClassControl.createControl(targetClass,
        null, null, methodsToMock);
    this.cacheManager = (CompositeCacheManager) this.cacheManagerControl
        .getMock();

    this.jcsFacade.setCacheManager(this.cacheManager);
  }

  private void setUpCacheAsMockObject(Method methodToMock) throws Exception {
    this.setUpCacheAsMockObject(new Method[] { methodToMock });
  }

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

  public void testGetCacheWithExistingCache() {
    CompositeCache expected = this.cacheManager.getCache(this.cacheName);
    CompositeCache actual = this.jcsFacade.getCache(this.cacheName);
    assertSame(expected, actual);
  }

  public void testGetCacheWithNotExistingCache() throws Exception {
    Method getCacheMethod = this
        .getGetCacheMethodFromCompositeCacheManagerClass();
    this.setUpCacheAdministratorAsMockObject(getCacheMethod);

    this.expectCacheManagerDoesNotHaveCache();
    this.setStateOfMockControlsToReplay();

    try {
      this.jcsFacade.getCache(this.cacheName);
      this.failIfCacheNotFoundExceptionIsNotThrown();

    } catch (CacheNotFoundException exception) {
      // expecting this exception
    }

    this.verifyExpectationsOfMockControlsWereMet();
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

    Serializable cacheKey = this.getFirstCacheKey();
    GroupId groupId = new GroupId(profile.getCacheName(), profile.getGroup());
    GroupAttrName expectedKey = new GroupAttrName(groupId, cacheKey);

    Serializable actualKey = this.jcsFacade.getKey(cacheKey, profile);

    this.assertEqualGeneratedKeys(expectedKey, actualKey);
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

    Serializable cacheKey = this.getFirstCacheKey();
    Serializable actualKey = this.jcsFacade.getKey(cacheKey, profile);

    this.assertEqualGeneratedKeys(cacheKey, actualKey);
  }

  public void testIsSerializableCacheElementRequired() {
    assertTrue(this.jcsFacade.isSerializableCacheElementRequired());
  }

  public void testOnFlushCacheWhenCacheAccessThrowsException() throws Exception {
    Method getCacheMethod = this
        .getGetCacheMethodFromCompositeCacheManagerClass();
    this.setUpCacheAdministratorAsMockObject(getCacheMethod);

    Method removeAllMethod = CompositeCache.class.getMethod("removeAll", null);
    this.setUpCacheAsMockObject(removeAllMethod);

    // expectation: cache manager finds the cache we are looking for.
    this.cacheManager.getCache(this.cacheName);
    this.cacheManagerControl.setReturnValue(this.cache);

    // expectation: cache manager throws exception when flushing the cache.
    RuntimeException thrownException = new RuntimeException();
    this.cache.removeAll();
    this.cacheControl.setThrowable(thrownException);

    this.setStateOfMockControlsToReplay();

    try {
      JcsProfile profile = new JcsProfile(this.cacheName);
      this.jcsFacade.onFlushCache(profile);
      this.failIfCacheAccessExceptionIsNotThrown();

    } catch (CacheAccessException exception) {
      this.assertSameNestedException(exception, thrownException);
    }

    this.verifyExpectationsOfMockControlsWereMet();
  }

  public void testOnFlushCacheWhenCacheIsNotFound() throws Exception {
    Method getCacheMethod = this
        .getGetCacheMethodFromCompositeCacheManagerClass();
    this.setUpCacheAdministratorAsMockObject(getCacheMethod);

    this.expectCacheManagerDoesNotHaveCache();
    this.setStateOfMockControlsToReplay();

    try {
      this.jcsFacade.onFlushCache(this.getFirstCacheProfile());
      this.failIfCacheNotFoundExceptionIsNotThrown();

    } catch (CacheNotFoundException exception) {
      // expecting this exception
    }

    this.verifyExpectationsOfMockControlsWereMet();
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
    JcsProfile cacheProfile = this.getFirstCacheProfile();
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

    JcsProfile cacheProfile = this.getFirstCacheProfile();
    cacheProfile.setGroup(null);

    // execute the method to test.
    this.jcsFacade.onFlushCache(cacheProfile);

    // the whole cache should be flushed.
    assertEquals("<Cache size>", 0, this.cache.getSize());
  }

  public void testOnGetFromCache() throws Exception {
    String objectToStore = "An Object";
    this.updateCache(new Object[] { objectToStore });

    String cacheKey = this.getFirstCacheKey();
    JcsProfile cacheProfile = this.getFirstCacheProfile();

    // execute the method to test.
    Object cachedObject = this.jcsFacade.onGetFromCache(cacheKey, cacheProfile);

    assertSame("<Cached object>", objectToStore, cachedObject);
  }

  public void testOnGetFromCacheWhenCacheAccessThrowsException()
      throws Exception {
    Method getCacheMethod = this
        .getGetCacheMethodFromCompositeCacheManagerClass();
    this.setUpCacheAdministratorAsMockObject(getCacheMethod);

    Method getMethod = CompositeCache.class.getMethod("get",
        new Class[] { Serializable.class });
    this.setUpCacheAsMockObject(getMethod);

    // expectation: cache manager finds the cache we are looking for.
    this.cacheManager.getCache(this.cacheName);
    this.cacheManagerControl.setReturnValue(this.cache);

    // expectation: cache manager throws exception.
    Serializable cacheKey = this.getFirstCacheKey();
    RuntimeException thrownException = new RuntimeException();
    this.cache.get(cacheKey);
    this.cacheControl.setThrowable(thrownException);

    this.setStateOfMockControlsToReplay();

    JcsProfile profile = this.getFirstCacheProfile();
    profile.setGroup(null);

    try {
      this.jcsFacade.onGetFromCache(cacheKey, profile);
      this.failIfCacheAccessExceptionIsNotThrown();

    } catch (CacheAccessException exception) {
      this.assertSameNestedException(exception, thrownException);
    }

    this.verifyExpectationsOfMockControlsWereMet();

  }

  public void testOnGetFromCacheWhenCacheIsNotFound() throws Exception {
    Method getCacheMethod = this
        .getGetCacheMethodFromCompositeCacheManagerClass();
    this.setUpCacheAdministratorAsMockObject(getCacheMethod);

    this.expectCacheManagerDoesNotHaveCache();
    this.setStateOfMockControlsToReplay();

    try {
      this.jcsFacade.onGetFromCache(this.getFirstCacheKey(), this
          .getFirstCacheProfile());
      this.failIfCacheNotFoundExceptionIsNotThrown();

    } catch (CacheNotFoundException exception) {
      // expecting this exception
    }

    this.verifyExpectationsOfMockControlsWereMet();
  }

  public void testOnGetFromCacheWhenCacheProfileDoesNotHaveCacheName() {
    String cacheKey = this.getFirstCacheKey();
    JcsProfile profile = this.getFirstCacheProfile();
    profile.setCacheName("");

    // execute the method to test.
    Object cachedObject = this.jcsFacade.onGetFromCache(cacheKey, profile);

    assertNull(cachedObject);
  }

  /**
   * Verifies that the method
   * <code>{@link JcsFacade#onGetFromCache(java.io.Serializable, org.springmodules.cache.provider.CacheProfile)}</code>
   * returns <code>null</code> if the specified key does not exist in the
   * cache.
   */
  public void testOnGetFromCacheWhenKeyIsNotFound() throws Exception {
    String cacheKey = "NonExistingKey";
    JcsProfile profile = this.getFirstCacheProfile();

    // execute the method to test.
    Object cachedObject = this.jcsFacade.onGetFromCache(cacheKey, profile);

    assertNull(cachedObject);
  }

  public void testOnPutInCache() throws Exception {
    String cacheKey = this.getFirstCacheKey();
    JcsProfile cacheProfile = this.getFirstCacheProfile();
    String objectToStore = "An Object";

    // execute the method to test.
    this.jcsFacade.onPutInCache(cacheKey, cacheProfile, objectToStore);

    Serializable entryKey = this.jcsFacade.getKey(cacheKey, cacheProfile);
    Object cachedObject = this.cache.get(entryKey).getVal();
    assertSame("<Cached object>", objectToStore, cachedObject);
  }

  public void testOnPutInCacheWhenCacheAccessThrowsException() throws Exception {
    Method getCacheMethod = this
        .getGetCacheMethodFromCompositeCacheManagerClass();
    this.setUpCacheAdministratorAsMockObject(getCacheMethod);

    Method updateMethod = CompositeCache.class.getMethod("update",
        new Class[] { ICacheElement.class });
    this.setUpCacheAsMockObject(updateMethod);

    // expectation: cache manager finds the cache we are looking for.
    this.cacheManager.getCache(this.cacheName);
    this.cacheManagerControl.setReturnValue(this.cache);

    // expectation: cache manager throws exception.
    String cacheKey = this.getFirstCacheKey();
    JcsProfile cacheProfile = this.getFirstCacheProfile();
    cacheProfile.setGroup(null);
    String objToCache = "Obi-Wan";

    CacheElement cacheElement = new CacheElement(cacheProfile.getCacheName(),
        cacheKey, objToCache);
    this.cache.update(cacheElement);
    this.cacheControl.setMatcher(new CacheElementMatcher());

    RuntimeException thrownException = new RuntimeException();
    this.cacheControl.setThrowable(thrownException);

    this.setStateOfMockControlsToReplay();

    try {
      this.jcsFacade.onPutInCache(cacheKey, cacheProfile, objToCache);
      this.failIfCacheAccessExceptionIsNotThrown();

    } catch (CacheAccessException exception) {
      this.assertSameNestedException(exception, thrownException);
    }

    this.verifyExpectationsOfMockControlsWereMet();
  }

  public void testOnPutInCacheWhenCacheIsNotFound() throws Exception {
    Method getCacheMethod = this
        .getGetCacheMethodFromCompositeCacheManagerClass();
    this.setUpCacheAdministratorAsMockObject(getCacheMethod);

    this.expectCacheManagerDoesNotHaveCache();
    this.setStateOfMockControlsToReplay();

    try {
      this.jcsFacade.onPutInCache(this.getFirstCacheKey(), this
          .getFirstCacheProfile(), "Obi-Wan");
      this.failIfCacheNotFoundExceptionIsNotThrown();

    } catch (CacheNotFoundException exception) {
      // expecting this exception
    }

    this.verifyExpectationsOfMockControlsWereMet();
  }

  public void testOnPutInCacheWhenCacheNameIsEmpty() throws Exception {
    String cacheKey = this.getFirstCacheKey();
    JcsProfile cacheProfile = this.getFirstCacheProfile();
    cacheProfile.setCacheName("");

    // execute the method to test.
    this.jcsFacade.onPutInCache(cacheKey, cacheProfile, "An Object");

    Serializable entryKey = this.jcsFacade.getKey(cacheKey, cacheProfile);
    ICacheElement cacheElement = this.cache.get(entryKey);
    assertNull(cacheElement);
  }

  public void testOnRemoveFromCache() throws Exception {
    Serializable[] entryKeys = this.updateCache(new Object[] { "An Object" });

    int i = 0;
    Serializable entryKey = entryKeys[i];
    String cacheKey = this.getFirstCacheKey();
    JcsProfile cacheProfile = this.getFirstCacheProfile();

    // execute the method to test.
    this.jcsFacade.onRemoveFromCache(cacheKey, cacheProfile);

    ICacheElement cacheElement = this.cache.get(entryKey);
    assertNull("The element with key '" + cacheKey
        + "' should have been removed from the cache", cacheElement);
  }

  public void testOnRemoveFromCacheWhenCacheAccessThrowsException()
      throws Exception {
    Method getCacheMethod = this
        .getGetCacheMethodFromCompositeCacheManagerClass();
    this.setUpCacheAdministratorAsMockObject(getCacheMethod);

    Method removeMethod = CompositeCache.class.getMethod("remove",
        new Class[] { Serializable.class });
    this.setUpCacheAsMockObject(removeMethod);

    // expectation: cache manager finds the cache we are looking for.
    this.cacheManager.getCache(this.cacheName);
    this.cacheManagerControl.setReturnValue(this.cache);

    // expectation: cache manager throws exception.
    Serializable cacheKey = this.getFirstCacheKey();
    RuntimeException thrownException = new RuntimeException();
    this.cache.remove(cacheKey);
    this.cacheControl.setThrowable(thrownException);

    this.setStateOfMockControlsToReplay();

    JcsProfile profile = this.getFirstCacheProfile();
    profile.setGroup(null);

    try {
      this.jcsFacade.onRemoveFromCache(cacheKey, profile);
      this.failIfCacheAccessExceptionIsNotThrown();

    } catch (CacheAccessException exception) {
      this.assertSameNestedException(exception, thrownException);
    }

    this.verifyExpectationsOfMockControlsWereMet();

  }

  public void testOnRemoveFromCacheWhenCacheIsNotFound() throws Exception {
    Method getCacheMethod = this
        .getGetCacheMethodFromCompositeCacheManagerClass();
    this.setUpCacheAdministratorAsMockObject(getCacheMethod);

    this.expectCacheManagerDoesNotHaveCache();
    this.setStateOfMockControlsToReplay();

    try {
      this.jcsFacade.onRemoveFromCache(this.getFirstCacheKey(), this
          .getFirstCacheProfile());
      this.failIfCacheNotFoundExceptionIsNotThrown();

    } catch (CacheNotFoundException exception) {
      // expecting this exception
    }

    this.verifyExpectationsOfMockControlsWereMet();
  }

  public void testOnRemoveFromCacheWhenCacheNameIsEmpty() throws Exception {
    Serializable[] entryKeys = this.updateCache(new Object[] { "An Object" });

    int i = 0;
    Serializable entryKey = entryKeys[i];
    String cacheKey = this.getFirstCacheKey();
    JcsProfile cacheProfile = this.getFirstCacheProfile();
    cacheProfile.setCacheName("");

    // execute the method to test.
    this.jcsFacade.onRemoveFromCache(cacheKey, cacheProfile);

    ICacheElement cacheElement = this.cache.get(entryKey);
    assertNotNull("The element with key '" + cacheKey
        + "' should not have been removed from the cache", cacheElement);
  }

  /**
   * Verifies that the method
   * <code>{@link JcsFacade#validateCacheManager()}</code> throws an
   * <code>{@link InvalidConfigurationException}</code> the cache manager is
   * <code>null</code>.
   */
  public void testValidateCacheManagerWithCacheManagerEqualToNull() {
    this.jcsFacade.setCacheManager(null);
    try {
      this.jcsFacade.validateCacheManager();
      fail("Expecting exception <"
          + InvalidConfigurationException.class.getName() + ">");
    } catch (InvalidConfigurationException exception) {
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

  private void verifyExpectationsOfMockControlsWereMet() {
    if (this.cacheControl != null) {
      this.cacheControl.verify();
    }
    this.cacheManagerControl.verify();
  }
}
