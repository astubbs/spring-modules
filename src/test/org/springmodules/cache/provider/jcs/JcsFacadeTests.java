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

import java.beans.PropertyEditor;
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
import org.springmodules.cache.provider.CacheAccessException;
import org.springmodules.cache.provider.CacheNotFoundException;
import org.springmodules.cache.provider.CacheModelEditor;
import org.springmodules.cache.provider.CacheModelValidator;
import org.springmodules.cache.provider.FatalCacheException;

/**
 * <p>
 * Unit Tests for <code>{@link JcsFacade}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.18 $ $Date: 2005/09/29 01:22:02 $
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

    final JcsModel cacheModel;

    final String cacheModelId;

    final Object value;

    public JcsCacheElementStruct(JcsModel newCacheModel,
        String newCacheModelId, Serializable newKey, Object newValue) {
      super();
      key = newKey;
      cacheModel = newCacheModel;
      cacheModelId = newCacheModelId;
      value = newValue;
    }

  }

  private static final String CACHE_NAME = "testCache";

  private CompositeCache cache;

  private MockClassControl cacheControl;

  private JcsCacheElementStruct cacheElementStruct;

  private JcsCacheElementStruct[] cacheElementStructs;

  private CompositeCacheManager cacheManager;

  private MockClassControl cacheManagerControl;

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
    cacheManager.getCache(CACHE_NAME);
    cacheManagerControl.setReturnValue(null);
  }

  private Method getGetCacheMethodFromCompositeCacheManagerClass()
      throws Exception {
    return CompositeCacheManager.class.getDeclaredMethod("getCache",
        new Class[] { String.class });
  }

  private void setStateOfMockControlsToReplay() {
    if (cacheControl != null) {
      cacheControl.replay();
    }
    cacheManagerControl.replay();
  }

  protected void setUp() throws Exception {
    super.setUp();

    cacheElementStruct = new JcsCacheElementStruct(new JcsModel(CACHE_NAME,
        "Empire"), "empire", "sith", "Darth Vader");

    cacheElementStructs = new JcsCacheElementStruct[] {
        cacheElementStruct,
        new JcsCacheElementStruct(new JcsModel(CACHE_NAME, "Rebels"), "rebels",
            "general", "Han Solo") };

    Map cacheModelMap = new HashMap();

    int cacheModelCount = cacheElementStructs.length;
    for (int i = 0; i < cacheModelCount; i++) {
      JcsCacheElementStruct cacheElement = cacheElementStructs[i];
      cacheModelMap.put(cacheElement.cacheModelId, cacheElement.cacheModel);
    }

    jcsFacade = new JcsFacade();
    jcsFacade.setCacheModels(cacheModelMap);
  }

  private void setUpCacheAdministratorAndCache() {
    cacheManager = CompositeCacheManager.getInstance();
    cache = cacheManager.getCache(CACHE_NAME);

    jcsFacade.setCacheManager(cacheManager);
  }

  private void setUpCacheAdministratorAsMockObject(Method methodToMock) {
    setUpCacheAdministratorAsMockObject(new Method[] { methodToMock });
  }

  private void setUpCacheAdministratorAsMockObject(Method[] methodsToMock) {
    Class targetClass = CompositeCacheManager.class;

    cacheManagerControl = MockClassControl.createControl(targetClass, null,
        null, methodsToMock);
    cacheManager = (CompositeCacheManager) cacheManagerControl.getMock();

    jcsFacade.setCacheManager(cacheManager);
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

    cacheControl = MockClassControl.createControl(targetClass,
        constructorTypes, constructorArgs, methodsToMock);
    cache = (CompositeCache) cacheControl.getMock();
  }

  protected void tearDown() throws Exception {
    super.tearDown();

    if (cacheManager != null) {
      cacheManager.shutDown();
    }
  }

  public void testGetCacheModelEditor() {
    PropertyEditor editor = jcsFacade.getCacheModelEditor();

    assertNotNull(editor);
    assertEquals(CacheModelEditor.class, editor.getClass());

    CacheModelEditor modelEditor = (CacheModelEditor) editor;
    assertEquals(JcsModel.class, modelEditor.getCacheModelClass());
    assertNull(modelEditor.getCacheModelPropertyEditors());
  }

  /**
   * Verifies that the method
   * <code>{@link JcsFacade#getCacheModelValidator()}</code> returns an an
   * instance of <code>{@link JcsModelValidator}</code> not equal to
   * <code>null</code>.
   */
  public void testGetCacheModelValidator() {
    CacheModelValidator validator = jcsFacade.getCacheModelValidator();
    assertNotNull(validator);
    assertEquals(JcsModelValidator.class, validator.getClass());
  }

  public void testGetCacheWithExistingCache() {
    setUpCacheAdministratorAndCache();

    CompositeCache expected = cacheManager.getCache(CACHE_NAME);
    CompositeCache actual = jcsFacade.getCache(CACHE_NAME);
    assertSame(expected, actual);
  }

  public void testGetCacheWithNotExistingCache() throws Exception {
    Method getCacheMethod = this
        .getGetCacheMethodFromCompositeCacheManagerClass();
    setUpCacheAdministratorAsMockObject(getCacheMethod);

    expectCacheManagerDoesNotHaveCache();
    setStateOfMockControlsToReplay();

    try {
      jcsFacade.getCache(CACHE_NAME);
      fail();

    } catch (CacheNotFoundException exception) {
      // expecting this exception
    }

    verifyExpectationsOfMockControlsWereMet();
  }

  public void testGetKeyWithGroupName() {
    JcsModel model = cacheElementStruct.cacheModel;
    Serializable key = cacheElementStruct.key;

    GroupId groupId = new GroupId(model.getCacheName(), model.getGroup());
    GroupAttrName expected = new GroupAttrName(groupId, key);

    Serializable actual = jcsFacade.getKey(key, model);
    assertEquals(expected, actual);
  }

  /**
   * Verifies that the method
   * <code>{@link JcsFacade#getKey(Serializable, JcsModel)}</code> creates a
   * key that does not contain the group if the given cache model does not
   * specify any group.
   */
  public void testGetKeyWithoutGroupName() {
    JcsModel model = cacheElementStruct.cacheModel;
    model.setGroup(null);
    Serializable expected = cacheElementStruct.key;

    Serializable actual = jcsFacade.getKey(expected, model);
    assertEquals(expected, actual);
  }

  public void testIsSerializableCacheElementRequired() {
    assertTrue(jcsFacade.isSerializableCacheElementRequired());
  }

  public void testOnFlushCacheWhenCacheAccessThrowsException() throws Exception {
    Method getCacheMethod = this
        .getGetCacheMethodFromCompositeCacheManagerClass();
    setUpCacheAdministratorAsMockObject(getCacheMethod);

    Method removeAllMethod = CompositeCache.class.getMethod("removeAll", null);
    setUpCacheAsMockObject(removeAllMethod);

    // cache manager finds the cache we are looking for.
    cacheManager.getCache(CACHE_NAME);
    cacheManagerControl.setReturnValue(cache);

    // cache manager throws exception when flushing the cache.
    RuntimeException thrownException = new RuntimeException();
    cache.removeAll();
    cacheControl.setThrowable(thrownException);

    setStateOfMockControlsToReplay();

    try {
      JcsModel model = new JcsModel(CACHE_NAME);
      jcsFacade.onFlushCache(model);
      fail();

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
      jcsFacade.onFlushCache(cacheElementStruct.cacheModel);
      fail();

    } catch (CacheNotFoundException exception) {
      // expecting this exception
    }

    verifyExpectationsOfMockControlsWereMet();
  }

  /**
   * Verifies that the method
   * <code>{@link JcsFacade#onFlushCache(org.springmodules.cache.provider.CacheModel)}</code>
   * flushes only the specified groups of the specified cache.
   */
  public void testOnFlushCacheWithGroups() throws Exception {
    setUpCacheAdministratorAndCache();

    Serializable[] keys = updateCache(cacheElementStructs);

    int elementToRemoveIndex = 0;

    // execute the method to test.
    jcsFacade
        .onFlushCache(cacheElementStructs[elementToRemoveIndex].cacheModel);

    int cacheElementCount = keys.length;
    for (int i = 0; i < cacheElementCount; i++) {
      if (i == elementToRemoveIndex) {
        // the group of this element should have been removed.
        assertNull(cache.get(keys[i]));

      } else {
        // the group of this element should exist.
        assertNotNull(cache.get(keys[i]));
      }
    }
  }

  /**
   * Verifies that the method
   * <code>{@link JcsFacade#onFlushCache(org.springmodules.cache.provider.CacheModel)}</code>
   * flushes the whole cache if there are not any specified groups.
   */
  public void testOnFlushCacheWithoutGroups() throws Exception {
    setUpCacheAdministratorAndCache();

    int cacheElementCount = cacheElementStructs.length;
    for (int i = 0; i < cacheElementCount; i++) {
      cacheElementStructs[i].cacheModel.setGroup(null);
    }

    updateCache(cacheElementStructs);
    assertTrue("The size of the cache should be greater than zero", cache
        .getSize() > 0);

    // execute the method to test.
    jcsFacade.onFlushCache(cacheElementStruct.cacheModel);

    // the whole cache should be flushed.
    assertEquals("<Cache size>", 0, cache.getSize());
  }

  public void testOnGetFromCache() throws Exception {
    setUpCacheAdministratorAndCache();

    updateCache(cacheElementStruct);

    // execute the method to test.
    Object actual = jcsFacade.onGetFromCache(cacheElementStruct.key,
        cacheElementStruct.cacheModel);

    assertSame(cacheElementStruct.value, actual);
  }

  public void testOnGetFromCacheWhenCacheAccessThrowsException()
      throws Exception {
    Method getCacheMethod = this
        .getGetCacheMethodFromCompositeCacheManagerClass();
    setUpCacheAdministratorAsMockObject(getCacheMethod);

    Method getMethod = CompositeCache.class.getMethod("get",
        new Class[] { Serializable.class });
    setUpCacheAsMockObject(getMethod);

    // cache manager finds the cache we are looking for.
    cacheManager.getCache(CACHE_NAME);
    cacheManagerControl.setReturnValue(cache);

    // cache manager throws exception.
    Serializable cacheKey = cacheElementStruct.key;
    RuntimeException thrownException = new RuntimeException();
    cache.get(cacheKey);
    cacheControl.setThrowable(thrownException);

    setStateOfMockControlsToReplay();

    JcsModel model = cacheElementStruct.cacheModel;
    model.setGroup(null);

    try {
      jcsFacade.onGetFromCache(cacheKey, model);
      fail();

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
      jcsFacade.onGetFromCache(cacheElementStruct.key,
          cacheElementStruct.cacheModel);
      fail();

    } catch (CacheNotFoundException exception) {
      // expecting this exception
    }

    verifyExpectationsOfMockControlsWereMet();
  }

  public void testOnGetFromCacheWhenCacheModelDoesNotHaveCacheName() {
    setUpCacheAdministratorAndCache();

    JcsModel model = cacheElementStruct.cacheModel;
    model.setCacheName("");

    // execute the method to test.
    Object cachedObject = jcsFacade.onGetFromCache(cacheElementStruct.key,
        model);

    assertNull(cachedObject);
  }

  /**
   * Verifies that the method
   * <code>{@link JcsFacade#onGetFromCache(java.io.Serializable, org.springmodules.cache.provider.CacheModel)}</code>
   * returns <code>null</code> if the specified key does not exist in the
   * cache.
   */
  public void testOnGetFromCacheWhenKeyIsNotFound() throws Exception {
    setUpCacheAdministratorAndCache();

    Object cachedObject = jcsFacade.onGetFromCache(cacheElementStruct.key,
        cacheElementStruct.cacheModel);
    assertNull(cachedObject);
  }

  public void testOnPutInCache() throws Exception {
    setUpCacheAdministratorAndCache();

    Serializable cacheKey = cacheElementStruct.key;
    JcsModel cacheModel = cacheElementStruct.cacheModel;
    Object expected = cacheElementStruct.value;

    // execute the method to test.
    jcsFacade.onPutInCache(cacheKey, cacheModel, expected);

    Serializable entryKey = jcsFacade.getKey(cacheKey, cacheModel);
    Object actual = cache.get(entryKey).getVal();
    assertSame(expected, actual);
  }

  public void testOnPutInCacheWhenCacheAccessThrowsException() throws Exception {
    Method getCacheMethod = this
        .getGetCacheMethodFromCompositeCacheManagerClass();
    setUpCacheAdministratorAsMockObject(getCacheMethod);

    Method updateMethod = CompositeCache.class.getMethod("update",
        new Class[] { ICacheElement.class });
    setUpCacheAsMockObject(updateMethod);

    // cache manager finds the cache we are looking for.
    cacheManager.getCache(CACHE_NAME);
    cacheManagerControl.setReturnValue(cache);

    // cache manager throws exception.
    Serializable cacheKey = cacheElementStruct.key;
    JcsModel cacheModel = cacheElementStruct.cacheModel;
    cacheModel.setGroup(null);
    Object objToCache = cacheElementStruct.value;

    CacheElement cacheElement = new CacheElement(cacheModel.getCacheName(),
        cacheKey, objToCache);
    cache.update(cacheElement);
    cacheControl.setMatcher(new CacheElementMatcher());

    RuntimeException thrownException = new RuntimeException();
    cacheControl.setThrowable(thrownException);

    setStateOfMockControlsToReplay();

    try {
      jcsFacade.onPutInCache(cacheKey, cacheModel, objToCache);
      fail();

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
      jcsFacade.onPutInCache(cacheElementStruct.key,
          cacheElementStruct.cacheModel, cacheElementStruct.value);
      fail();

    } catch (CacheNotFoundException exception) {
      // expecting this exception
    }

    verifyExpectationsOfMockControlsWereMet();
  }

  public void testOnPutInCacheWhenCacheNameIsEmpty() throws Exception {
    setUpCacheAdministratorAndCache();

    Serializable cacheKey = cacheElementStruct.key;
    JcsModel cacheModel = cacheElementStruct.cacheModel;
    cacheModel.setCacheName("");

    // execute the method to test.
    jcsFacade.onPutInCache(cacheKey, cacheModel, cacheElementStruct.value);

    Serializable entryKey = jcsFacade.getKey(cacheKey, cacheModel);
    assertNull(cache.get(entryKey));
  }

  public void testOnRemoveFromCache() throws Exception {
    setUpCacheAdministratorAndCache();

    Serializable key = updateCache(cacheElementStruct);

    // execute the method to test.
    jcsFacade.onRemoveFromCache(cacheElementStruct.key,
        cacheElementStruct.cacheModel);

    ICacheElement cacheElement = cache.get(key);
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

    // cache manager finds the cache we are looking for.
    cacheManager.getCache(CACHE_NAME);
    cacheManagerControl.setReturnValue(cache);

    // cache manager throws exception.
    Serializable cacheKey = cacheElementStruct.key;
    RuntimeException thrownException = new RuntimeException();
    cache.remove(cacheKey);
    cacheControl.setThrowable(thrownException);

    setStateOfMockControlsToReplay();

    JcsModel model = cacheElementStruct.cacheModel;
    model.setGroup(null);

    try {
      jcsFacade.onRemoveFromCache(cacheKey, model);
      fail();

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
      jcsFacade.onRemoveFromCache(cacheElementStruct.key,
          cacheElementStruct.cacheModel);
      fail();

    } catch (CacheNotFoundException exception) {
      // expecting this exception
    }

    verifyExpectationsOfMockControlsWereMet();
  }

  public void testOnRemoveFromCacheWhenCacheNameIsEmpty() throws Exception {
    setUpCacheAdministratorAndCache();

    Serializable key = updateCache(cacheElementStruct);

    // execute the method to test.
    jcsFacade.onRemoveFromCache(key, cacheElementStruct.cacheModel);

    assertNotNull(cache.get(key));
  }

  /**
   * Verifies that the method
   * <code>{@link JcsFacade#validateCacheManager()}</code> throws an
   * <code>{@link FatalCacheException}</code> the cache manager is
   * <code>null</code>.
   */
  public void testValidateCacheManagerWithCacheManagerEqualToNull() {
    jcsFacade.setCacheManager(null);
    try {
      jcsFacade.validateCacheManager();
      fail();
    } catch (FatalCacheException exception) {
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

    jcsFacade.validateCacheManager();
  }

  protected Serializable updateCache(JcsCacheElementStruct cacheElement)
      throws Exception {
    Serializable key = cacheElement.key;
    JcsModel model = cacheElement.cacheModel;
    String group = model.getGroup();

    if (StringUtils.hasText(group)) {
      GroupId groupId = new GroupId(model.getCacheName(), group);
      GroupAttrName groupAttrName = new GroupAttrName(groupId, key);
      key = groupAttrName;
    }

    ICacheElement newCacheElement = new CacheElement(CACHE_NAME, key,
        cacheElement.value);
    IElementAttributes elementAttributes = cache.getElementAttributes().copy();
    newCacheElement.setElementAttributes(elementAttributes);

    cache.update(newCacheElement);
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
    if (cacheControl != null) {
      cacheControl.verify();
    }
    cacheManagerControl.verify();
  }
}
