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
import org.springframework.util.StringUtils;
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
 * @version $Revision: 1.12 $ $Date: 2005/09/04 01:33:53 $
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

  private static class JcsCacheElementStruct {

    final Serializable key;

    final JcsProfile profile;

    final String profileId;

    final Object value;

    public JcsCacheElementStruct(JcsProfile profile, String profileId,
        Serializable key, Object value) {
      super();
      this.key = key;
      this.profile = profile;
      this.profileId = profileId;
      this.value = value;
    }

  }

  private static final String CACHE_NAME = "testCache";

  private CompositeCache cache;

  private MockClassControl cacheControl;

  private JcsCacheElementStruct cacheElementStruct;

  private JcsCacheElementStruct[] cacheElementStructs;

  private CompositeCacheManager cacheManager;

  private MockClassControl cacheManagerControl;

  /**
   * Primary object that is under test.
   */
  private JcsFacade jcsFacade;

  public JcsFacadeTests(String name) {
    super(name);
  }

  private void assertSameNestedException(Exception rootException,
      Exception expectedNestedException) {
    assertSame("<Nested exception>", expectedNestedException, rootException
        .getCause());
  }

  private void expectCacheManagerDoesNotHaveCache() {
    this.cacheManager.getCache(CACHE_NAME);
    this.cacheManagerControl.setReturnValue(null);
  }

  private void failIfCacheAccessExceptionIsNotThrown() {
    fail("Expecting exception <" + CacheAccessException.class.getName() + ">");
  }

  private void failIfCacheNotFoundExceptionIsNotThrown() {
    fail("Expecting exception <" + CacheNotFoundException.class.getName() + ">");
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

    this.cacheElementStruct = new JcsCacheElementStruct(new JcsProfile(
        CACHE_NAME, "Empire"), "empire", "sith", "Darth Vader");

    this.cacheElementStructs = new JcsCacheElementStruct[] {
        this.cacheElementStruct,
        new JcsCacheElementStruct(new JcsProfile(CACHE_NAME, "Rebels"),
            "rebels", "general", "Han Solo") };

    Map cacheProfileMap = new HashMap();

    int cacheProfileCount = this.cacheElementStructs.length;
    for (int i = 0; i < cacheProfileCount; i++) {
      JcsCacheElementStruct cacheElement = this.cacheElementStructs[i];
      cacheProfileMap.put(cacheElement.profileId, cacheElement.profile);
    }

    this.jcsFacade = new JcsFacade();
    this.jcsFacade.setCacheProfiles(cacheProfileMap);
  }

  private void setUpCacheAdministratorAndCache() {
    this.cacheManager = CompositeCacheManager.getInstance();
    this.cache = this.cacheManager.getCache(CACHE_NAME);

    this.jcsFacade.setCacheManager(this.cacheManager);
  }

  private void setUpCacheAdministratorAsMockObject(Method methodToMock) {
    setUpCacheAdministratorAsMockObject(new Method[] { methodToMock });
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
    setUpCacheAsMockObject(new Method[] { methodToMock });
  }

  private void setUpCacheAsMockObject(Method[] methodsToMock) throws Exception {
    // Create the proxy for the class 'CompositeManager'.
    Class[] constructorTypes = new Class[] { String.class,
        ICompositeCacheAttributes.class, IElementAttributes.class };

    ICompositeCacheAttributes cacheAttributes = new CompositeCacheAttributes();
    cacheAttributes.setCacheName(CACHE_NAME);
    cacheAttributes.setMaxObjects(10);
    cacheAttributes
        .setMemoryCacheName("org.apache.jcs.engine.memory.lru.LRUMemoryCache");
    ElementAttributes elementAttributes = new ElementAttributes();
    Object[] constructorArgs = new Object[] { CACHE_NAME, cacheAttributes,
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
    setUpCacheAdministratorAndCache();

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
    setUpCacheAdministratorAndCache();

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
    setUpCacheAdministratorAndCache();

    CompositeCache expected = this.cacheManager.getCache(CACHE_NAME);
    CompositeCache actual = this.jcsFacade.getCache(CACHE_NAME);
    assertSame(expected, actual);
  }

  public void testGetCacheWithNotExistingCache() throws Exception {
    Method getCacheMethod = this
        .getGetCacheMethodFromCompositeCacheManagerClass();
    setUpCacheAdministratorAsMockObject(getCacheMethod);

    expectCacheManagerDoesNotHaveCache();
    setStateOfMockControlsToReplay();

    try {
      this.jcsFacade.getCache(CACHE_NAME);
      failIfCacheNotFoundExceptionIsNotThrown();

    } catch (CacheNotFoundException exception) {
      // expecting this exception
    }

    verifyExpectationsOfMockControlsWereMet();
  }

  public void testGetKeyWithGroupName() {
    setUpCacheAdministratorAndCache();

    JcsProfile profile = this.cacheElementStruct.profile;
    Serializable key = this.cacheElementStruct.key;

    GroupId groupId = new GroupId(profile.getCacheName(), profile.getGroup());
    GroupAttrName expected = new GroupAttrName(groupId, key);

    Serializable actual = this.jcsFacade.getKey(key, profile);
    assertEquals(expected, actual);
  }

  /**
   * Verifies that the method
   * <code>{@link JcsFacade#getKey(Serializable, JcsProfile)}</code> creates a
   * key that does not contain the group if the given cache profile does not
   * specify any group.
   */
  public void testGetKeyWithoutGroupName() {
    setUpCacheAdministratorAndCache();

    JcsProfile profile = this.cacheElementStruct.profile;
    profile.setGroup(null);
    Serializable expected = this.cacheElementStruct.key;

    Serializable actual = this.jcsFacade.getKey(expected, profile);
    assertEquals(expected, actual);
  }

  public void testIsSerializableCacheElementRequired() {
    setUpCacheAdministratorAndCache();

    assertTrue(this.jcsFacade.isSerializableCacheElementRequired());
  }

  public void testOnFlushCacheWhenCacheAccessThrowsException() throws Exception {
    Method getCacheMethod = this
        .getGetCacheMethodFromCompositeCacheManagerClass();
    setUpCacheAdministratorAsMockObject(getCacheMethod);

    Method removeAllMethod = CompositeCache.class.getMethod("removeAll", null);
    setUpCacheAsMockObject(removeAllMethod);

    // expectation: cache manager finds the cache we are looking for.
    this.cacheManager.getCache(CACHE_NAME);
    this.cacheManagerControl.setReturnValue(this.cache);

    // expectation: cache manager throws exception when flushing the cache.
    RuntimeException thrownException = new RuntimeException();
    this.cache.removeAll();
    this.cacheControl.setThrowable(thrownException);

    setStateOfMockControlsToReplay();

    try {
      JcsProfile profile = new JcsProfile(CACHE_NAME);
      this.jcsFacade.onFlushCache(profile);
      failIfCacheAccessExceptionIsNotThrown();

    } catch (CacheAccessException exception) {
      assertSameNestedException(exception, thrownException);
    }

    verifyExpectationsOfMockControlsWereMet();
  }

  public void testOnFlushCacheWhenCacheIsNotFound() throws Exception {
    Method getCacheMethod = this
        .getGetCacheMethodFromCompositeCacheManagerClass();
    setUpCacheAdministratorAsMockObject(getCacheMethod);

    expectCacheManagerDoesNotHaveCache();
    setStateOfMockControlsToReplay();

    try {
      this.jcsFacade.onFlushCache(this.cacheElementStruct.profile);
      failIfCacheNotFoundExceptionIsNotThrown();

    } catch (CacheNotFoundException exception) {
      // expecting this exception
    }

    verifyExpectationsOfMockControlsWereMet();
  }

  /**
   * Verifies that the method
   * <code>{@link JcsFacade#onFlushCache(org.springmodules.cache.provider.CacheProfile)}</code>
   * flushes only the specified groups of the specified cache.
   */
  public void testOnFlushCacheWithGroups() throws Exception {
    setUpCacheAdministratorAndCache();

    Serializable[] keys = updateCache(this.cacheElementStructs);

    int elementToRemoveIndex = 0;

    // execute the method to test.
    this.jcsFacade
        .onFlushCache(this.cacheElementStructs[elementToRemoveIndex].profile);

    int cacheElementCount = keys.length;
    for (int i = 0; i < cacheElementCount; i++) {
      if (i == elementToRemoveIndex) {
        // the group of this element should have been removed.
        assertNull(this.cache.get(keys[i]));

      } else {
        // the group of this element should exist.
        assertNotNull(this.cache.get(keys[i]));
      }
    }
  }

  /**
   * Verifies that the method
   * <code>{@link JcsFacade#onFlushCache(org.springmodules.cache.provider.CacheProfile)}</code>
   * flushes the whole cache if there are not any specified groups.
   */
  public void testOnFlushCacheWithoutGroups() throws Exception {
    setUpCacheAdministratorAndCache();

    int cacheElementCount = this.cacheElementStructs.length;
    for (int i = 0; i < cacheElementCount; i++) {
      this.cacheElementStructs[i].profile.setGroup(null);
    }

    this.updateCache(this.cacheElementStructs);
    assertTrue("The size of the cache should be greater than zero", this.cache
        .getSize() > 0);

    // execute the method to test.
    this.jcsFacade.onFlushCache(this.cacheElementStruct.profile);

    // the whole cache should be flushed.
    assertEquals("<Cache size>", 0, this.cache.getSize());
  }

  public void testOnGetFromCache() throws Exception {
    setUpCacheAdministratorAndCache();

    updateCache(this.cacheElementStruct);

    // execute the method to test.
    Object actual = this.jcsFacade.onGetFromCache(this.cacheElementStruct.key,
        this.cacheElementStruct.profile);

    assertSame(this.cacheElementStruct.value, actual);
  }

  public void testOnGetFromCacheWhenCacheAccessThrowsException()
      throws Exception {
    Method getCacheMethod = this
        .getGetCacheMethodFromCompositeCacheManagerClass();
    setUpCacheAdministratorAsMockObject(getCacheMethod);

    Method getMethod = CompositeCache.class.getMethod("get",
        new Class[] { Serializable.class });
    setUpCacheAsMockObject(getMethod);

    // expectation: cache manager finds the cache we are looking for.
    this.cacheManager.getCache(CACHE_NAME);
    this.cacheManagerControl.setReturnValue(this.cache);

    // expectation: cache manager throws exception.
    Serializable cacheKey = this.cacheElementStruct.key;
    RuntimeException thrownException = new RuntimeException();
    this.cache.get(cacheKey);
    this.cacheControl.setThrowable(thrownException);

    setStateOfMockControlsToReplay();

    JcsProfile profile = this.cacheElementStruct.profile;
    profile.setGroup(null);

    try {
      this.jcsFacade.onGetFromCache(cacheKey, profile);
      failIfCacheAccessExceptionIsNotThrown();

    } catch (CacheAccessException exception) {
      assertSameNestedException(exception, thrownException);
    }

    verifyExpectationsOfMockControlsWereMet();

  }

  public void testOnGetFromCacheWhenCacheIsNotFound() throws Exception {
    Method getCacheMethod = this
        .getGetCacheMethodFromCompositeCacheManagerClass();
    setUpCacheAdministratorAsMockObject(getCacheMethod);

    expectCacheManagerDoesNotHaveCache();
    setStateOfMockControlsToReplay();

    try {
      this.jcsFacade.onGetFromCache(this.cacheElementStruct.key,
          this.cacheElementStruct.profile);
      failIfCacheNotFoundExceptionIsNotThrown();

    } catch (CacheNotFoundException exception) {
      // expecting this exception
    }

    verifyExpectationsOfMockControlsWereMet();
  }

  public void testOnGetFromCacheWhenCacheProfileDoesNotHaveCacheName() {
    setUpCacheAdministratorAndCache();

    JcsProfile profile = this.cacheElementStruct.profile;
    profile.setCacheName("");

    // execute the method to test.
    Object cachedObject = this.jcsFacade.onGetFromCache(
        this.cacheElementStruct.key, profile);

    assertNull(cachedObject);
  }

  /**
   * Verifies that the method
   * <code>{@link JcsFacade#onGetFromCache(java.io.Serializable, org.springmodules.cache.provider.CacheProfile)}</code>
   * returns <code>null</code> if the specified key does not exist in the
   * cache.
   */
  public void testOnGetFromCacheWhenKeyIsNotFound() throws Exception {
    setUpCacheAdministratorAndCache();

    Object cachedObject = this.jcsFacade.onGetFromCache(
        this.cacheElementStruct.key, this.cacheElementStruct.profile);
    assertNull(cachedObject);
  }

  public void testOnPutInCache() throws Exception {
    setUpCacheAdministratorAndCache();

    Serializable cacheKey = this.cacheElementStruct.key;
    JcsProfile cacheProfile = this.cacheElementStruct.profile;
    Object expected = this.cacheElementStruct.value;

    // execute the method to test.
    this.jcsFacade.onPutInCache(cacheKey, cacheProfile, expected);

    Serializable entryKey = this.jcsFacade.getKey(cacheKey, cacheProfile);
    Object actual = this.cache.get(entryKey).getVal();
    assertSame(expected, actual);
  }

  public void testOnPutInCacheWhenCacheAccessThrowsException() throws Exception {
    Method getCacheMethod = this
        .getGetCacheMethodFromCompositeCacheManagerClass();
    setUpCacheAdministratorAsMockObject(getCacheMethod);

    Method updateMethod = CompositeCache.class.getMethod("update",
        new Class[] { ICacheElement.class });
    setUpCacheAsMockObject(updateMethod);

    // expectation: cache manager finds the cache we are looking for.
    this.cacheManager.getCache(CACHE_NAME);
    this.cacheManagerControl.setReturnValue(this.cache);

    // expectation: cache manager throws exception.
    Serializable cacheKey = this.cacheElementStruct.key;
    JcsProfile cacheProfile = this.cacheElementStruct.profile;
    cacheProfile.setGroup(null);
    Object objToCache = this.cacheElementStruct.value;

    CacheElement cacheElement = new CacheElement(cacheProfile.getCacheName(),
        cacheKey, objToCache);
    this.cache.update(cacheElement);
    this.cacheControl.setMatcher(new CacheElementMatcher());

    RuntimeException thrownException = new RuntimeException();
    this.cacheControl.setThrowable(thrownException);

    setStateOfMockControlsToReplay();

    try {
      this.jcsFacade.onPutInCache(cacheKey, cacheProfile, objToCache);
      failIfCacheAccessExceptionIsNotThrown();

    } catch (CacheAccessException exception) {
      assertSameNestedException(exception, thrownException);
    }

    verifyExpectationsOfMockControlsWereMet();
  }

  public void testOnPutInCacheWhenCacheIsNotFound() throws Exception {
    Method getCacheMethod = this
        .getGetCacheMethodFromCompositeCacheManagerClass();
    setUpCacheAdministratorAsMockObject(getCacheMethod);

    expectCacheManagerDoesNotHaveCache();
    setStateOfMockControlsToReplay();

    try {
      this.jcsFacade.onPutInCache(this.cacheElementStruct.key,
          this.cacheElementStruct.profile, this.cacheElementStruct.value);
      failIfCacheNotFoundExceptionIsNotThrown();

    } catch (CacheNotFoundException exception) {
      // expecting this exception
    }

    verifyExpectationsOfMockControlsWereMet();
  }

  public void testOnPutInCacheWhenCacheNameIsEmpty() throws Exception {
    setUpCacheAdministratorAndCache();

    Serializable cacheKey = this.cacheElementStruct.key;
    JcsProfile cacheProfile = this.cacheElementStruct.profile;
    cacheProfile.setCacheName("");

    // execute the method to test.
    this.jcsFacade.onPutInCache(cacheKey, cacheProfile,
        this.cacheElementStruct.value);

    Serializable entryKey = this.jcsFacade.getKey(cacheKey, cacheProfile);
    assertNull(this.cache.get(entryKey));
  }

  public void testOnRemoveFromCache() throws Exception {
    setUpCacheAdministratorAndCache();

    Serializable key = updateCache(this.cacheElementStruct);

    // execute the method to test.
    this.jcsFacade.onRemoveFromCache(this.cacheElementStruct.key,
        this.cacheElementStruct.profile);

    ICacheElement cacheElement = this.cache.get(key);
    assertNull(cacheElement);
  }

  public void testOnRemoveFromCacheWhenCacheAccessThrowsException()
      throws Exception {
    Method getCacheMethod = this
        .getGetCacheMethodFromCompositeCacheManagerClass();
    setUpCacheAdministratorAsMockObject(getCacheMethod);

    Method removeMethod = CompositeCache.class.getMethod("remove",
        new Class[] { Serializable.class });
    setUpCacheAsMockObject(removeMethod);

    // expectation: cache manager finds the cache we are looking for.
    this.cacheManager.getCache(CACHE_NAME);
    this.cacheManagerControl.setReturnValue(this.cache);

    // expectation: cache manager throws exception.
    Serializable cacheKey = this.cacheElementStruct.key;
    RuntimeException thrownException = new RuntimeException();
    this.cache.remove(cacheKey);
    this.cacheControl.setThrowable(thrownException);

    setStateOfMockControlsToReplay();

    JcsProfile profile = this.cacheElementStruct.profile;
    profile.setGroup(null);

    try {
      this.jcsFacade.onRemoveFromCache(cacheKey, profile);
      failIfCacheAccessExceptionIsNotThrown();

    } catch (CacheAccessException exception) {
      assertSameNestedException(exception, thrownException);
    }

    verifyExpectationsOfMockControlsWereMet();

  }

  public void testOnRemoveFromCacheWhenCacheIsNotFound() throws Exception {
    Method getCacheMethod = this
        .getGetCacheMethodFromCompositeCacheManagerClass();
    setUpCacheAdministratorAsMockObject(getCacheMethod);

    expectCacheManagerDoesNotHaveCache();
    setStateOfMockControlsToReplay();

    try {
      this.jcsFacade.onRemoveFromCache(this.cacheElementStruct.key,
          this.cacheElementStruct.profile);
      failIfCacheNotFoundExceptionIsNotThrown();

    } catch (CacheNotFoundException exception) {
      // expecting this exception
    }

    verifyExpectationsOfMockControlsWereMet();
  }

  public void testOnRemoveFromCacheWhenCacheNameIsEmpty() throws Exception {
    setUpCacheAdministratorAndCache();

    Serializable key = updateCache(this.cacheElementStruct);

    // execute the method to test.
    this.jcsFacade.onRemoveFromCache(key, this.cacheElementStruct.profile);

    assertNotNull(this.cache.get(key));
  }

  /**
   * Verifies that the method
   * <code>{@link JcsFacade#validateCacheManager()}</code> throws an
   * <code>{@link InvalidConfigurationException}</code> the cache manager is
   * <code>null</code>.
   */
  public void testValidateCacheManagerWithCacheManagerEqualToNull() {
    setUpCacheAdministratorAndCache();

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
    setUpCacheAdministratorAndCache();

    this.jcsFacade.validateCacheManager();
  }

  protected Serializable updateCache(JcsCacheElementStruct cacheElement)
      throws Exception {
    Serializable key = cacheElement.key;
    JcsProfile profile = cacheElement.profile;
    String group = profile.getGroup();

    if (StringUtils.hasText(group)) {
      GroupId groupId = new GroupId(profile.getCacheName(), group);
      GroupAttrName groupAttrName = new GroupAttrName(groupId, key);
      key = groupAttrName;
    }

    ICacheElement newCacheElement = new CacheElement(CACHE_NAME, key,
        cacheElement.value);
    IElementAttributes elementAttributes = this.cache.getElementAttributes()
        .copy();
    newCacheElement.setElementAttributes(elementAttributes);

    this.cache.update(newCacheElement);
    return key;
  }

  protected Serializable[] updateCache(JcsCacheElementStruct[] cacheElements)
      throws Exception {
    int elementCount = cacheElements.length;
    Serializable[] keys = new Serializable[elementCount];

    for (int i = 0; i < elementCount; i++) {
      keys[i] = updateCache(cacheElements[i]);
    }

    return keys;
  }

  private void verifyExpectationsOfMockControlsWereMet() {
    if (this.cacheControl != null) {
      this.cacheControl.verify();
    }
    this.cacheManagerControl.verify();
  }
}
