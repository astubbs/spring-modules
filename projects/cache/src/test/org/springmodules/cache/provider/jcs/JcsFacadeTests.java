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
import org.easymock.MockControl;
import org.easymock.classextension.MockClassControl;

import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import org.springmodules.cache.FatalCacheException;
import org.springmodules.cache.provider.CacheAccessException;
import org.springmodules.cache.provider.CacheModelValidator;
import org.springmodules.cache.provider.CacheNotFoundException;
import org.springmodules.cache.provider.ReflectionCacheModelEditor;
import org.springmodules.cache.provider.jcs.JcsFlushingModel.CacheStruct;

/**
 * <p>
 * Unit Tests for <code>{@link JcsFacade}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public final class JcsFacadeTests extends TestCase {

  protected class CacheElementMatcher extends AbstractMatcher {
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

      return equals(expectedElement, actualElement);
    }

    private boolean equals(CacheElement expected, CacheElement actual) {
      if (expected == actual) {
        return true;
      }
      if (!ObjectUtils.nullSafeEquals(expected.getKey(), actual.getKey())) {
        return false;
      }
      if (!ObjectUtils.nullSafeEquals(expected.getVal(), actual.getVal())) {
        return false;
      }
      return true;
    }
  }

  private class CacheEntry {
    String group;

    Serializable key;

    Object value;

    public CacheEntry(Serializable newKey, String newGroup, Object newValue) {
      super();
      key = newKey;
      group = newGroup;
      value = newValue;
    }
  }

  private static final String CACHE_NAME = "testCache";

  private CompositeCache cache;

  private MockClassControl cacheControl;

  private CacheEntry[] cacheEntries;

  private CompositeCacheManager cacheManager;

  private MockClassControl cacheManagerControl;

  private JcsCachingModel cachingModel;

  private JcsFlushingModel flushingModel;

  private JcsFacade jcsFacade;

  public JcsFacadeTests(String name) {
    super(name);
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
    assertSame(cacheManager.getCache(CACHE_NAME), jcsFacade
        .getCache(CACHE_NAME));
  }

  public void testGetCacheWithNotExistingCache() throws Exception {
    Method getCacheMethod = this
        .getGetCacheMethodFromCompositeCacheManagerClass();
    setUpCacheAdministratorAsMockObject(getCacheMethod);

    expectCacheIsNotFound();
    replay();

    try {
      jcsFacade.getCache(CACHE_NAME);
      fail();
    } catch (CacheNotFoundException exception) {
      // expecting this exception
    }
    verify();
  }

  public void testGetCachingModelEditor() {
    PropertyEditor editor = jcsFacade.getCachingModelEditor();

    assertNotNull(editor);
    assertEquals(ReflectionCacheModelEditor.class, editor.getClass());

    ReflectionCacheModelEditor modelEditor = (ReflectionCacheModelEditor) editor;
    assertEquals(JcsCachingModel.class, modelEditor.getCacheModelClass());
    assertNull(modelEditor.getCacheModelPropertyEditors());
  }

  public void testGetFlushingModelEditor() {
    PropertyEditor editor = jcsFacade.getFlushingModelEditor();
    assertNotNull(editor);
    assertEquals(JcsFlushingModelEditor.class, editor.getClass());
  }

  public void testGetKeyWithGroupName() {
    cachingModel.setGroup("empire");
    Serializable key = "Vader";
    GroupId groupId = new GroupId(CACHE_NAME, cachingModel.getGroup());
    GroupAttrName expected = new GroupAttrName(groupId, key);
    Serializable actual = jcsFacade.getKey(key, cachingModel);
    assertEquals(expected, actual);
  }

  /**
   * Verifies that the method
   * <code>{@link JcsFacade#getKey(Serializable, JcsCachingModel)}</code>
   * creates a key that does not contain the group if the given cache model does
   * not specify any group.
   */
  public void testGetKeyWithoutGroupName() {
    Serializable key = "FalconMillenium";
    assertEquals(key, jcsFacade.getKey(key, cachingModel));
  }

  public void testIsSerializableCacheElementRequired() {
    assertTrue(jcsFacade.isSerializableCacheElementRequired());
  }

  public void testOnFlushCacheCacheStructArrayEqualToNull() throws Exception {
    setUpCacheAdministratorAndCache();
    setUpCacheEntries();
    updateCache();

    int expected = cache.getSize();

    flushingModel.setCacheStructs(null);
    jcsFacade.onFlushCache(flushingModel);

    // the cache should not be flushed.
    assertEquals(expected, cache.getSize());
  }

  public void testOnFlushCacheWhenCacheAccessThrowsException() throws Exception {
    Method getCache = getGetCacheMethodFromCompositeCacheManagerClass();
    setUpCacheAdministratorAsMockObject(getCache);

    Method removeAll = CompositeCache.class
        .getMethod("removeAll", new Class[0]);
    setUpCacheAsMockObject(removeAll);

    expectCacheIsFound();

    // cache manager throws exception when flushing the cache.
    RuntimeException expected = new RuntimeException();
    cache.removeAll();
    cacheControl.setThrowable(expected);

    replay();

    try {
      jcsFacade.onFlushCache(flushingModel);
      fail();

    } catch (CacheAccessException exception) {
      assertSame(expected, exception.getCause());
    }
    verify();
  }

  public void testOnFlushCacheWhenCacheIsNotFound() throws Exception {
    Method getCacheMethod = this
        .getGetCacheMethodFromCompositeCacheManagerClass();
    setUpCacheAdministratorAsMockObject(getCacheMethod);

    expectCacheIsNotFound();
    replay();

    try {
      jcsFacade.onFlushCache(flushingModel);
      fail();
    } catch (CacheAccessException exception) {
      // expecting this exception
    }
    verify();
  }

  /**
   * Verifies that the method
   * <code>{@link JcsFacade#onFlushCache(org.springmodules.cache.FlushingModel)}</code>
   * flushes only the specified groups of the specified cache.
   */
  public void testOnFlushCacheWithGroups() throws Exception {
    setUpCacheAdministratorAndCache();
    setUpCacheEntries();

    Serializable[] keys = updateCache();

    int entryToRemoveIndex = 0;
    CacheEntry entryToRemove = cacheEntries[entryToRemoveIndex];

    flushingModel.setCacheStruct(new CacheStruct(CACHE_NAME,
        entryToRemove.group));
    jcsFacade.onFlushCache(flushingModel);

    int cacheElementCount = keys.length;
    for (int i = 0; i < cacheElementCount; i++) {
      if (i == entryToRemoveIndex) {
        // the group of this element should have been removed.
        assertNull(cache.get(keys[i]));

      } else {
        // the group of this element should exist.
        assertNotNull(cache.get(keys[i]));
      }
    }
  }

  public void testOnFlushCacheWithoutCacheStructs() throws Exception {
    setUpCacheAdministratorAndCache();
    setUpCacheEntries();
    updateCache();

    int expected = cache.getSize();

    flushingModel.setCacheStruct(null);
    jcsFacade.onFlushCache(flushingModel);

    // the cache should not be flushed.
    assertEquals(expected, cache.getSize());
  }

  /**
   * Verifies that the method
   * <code>{@link JcsFacade#onFlushCache(org.springmodules.cache.FlushingModel)}</code>
   * flushes the whole cache if there are not any specified groups.
   */
  public void testOnFlushCacheWithoutGroups() throws Exception {
    setUpCacheAdministratorAndCache();
    setUpCacheEntries();
    updateCache();

    jcsFacade.onFlushCache(flushingModel);

    // the whole cache should be flushed.
    assertEquals(0, cache.getSize());
  }

  public void testOnGetFromCache() throws Exception {
    setUpCacheAdministratorAndCache();
    setUpCacheEntries();
    CacheEntry entryToAdd = cacheEntries[0];
    updateCache(entryToAdd);

    cachingModel.setGroup(entryToAdd.group);
    Object actual = jcsFacade.onGetFromCache(entryToAdd.key, cachingModel);
    assertSame(entryToAdd.value, actual);
  }

  public void testOnGetFromCacheWhenCacheAccessThrowsException()
      throws Exception {
    Method getCache = getGetCacheMethodFromCompositeCacheManagerClass();
    setUpCacheAdministratorAsMockObject(getCache);

    Method get = CompositeCache.class.getMethod("get",
        new Class[] { Serializable.class });
    setUpCacheAsMockObject(get);

    expectCacheIsFound();

    // cache manager throws exception.
    Serializable key = "R2-D2";
    RuntimeException expected = new RuntimeException();
    cacheControl.expectAndThrow(cache.get(key), expected);
    replay();

    try {
      jcsFacade.onGetFromCache(key, cachingModel);
      fail();
    } catch (CacheAccessException exception) {
      assertSame(expected, exception.getCause());
    }
    verify();
  }

  public void testOnGetFromCacheWhenCacheIsNotFound() throws Exception {
    Method getCacheMethod = this
        .getGetCacheMethodFromCompositeCacheManagerClass();
    setUpCacheAdministratorAsMockObject(getCacheMethod);

    expectCacheIsNotFound();
    replay();

    try {
      jcsFacade.onGetFromCache("C-3PO", cachingModel);
      fail();
    } catch (CacheNotFoundException exception) {
      // expecting this exception
    }
    verify();
  }

  /**
   * Verifies that the method
   * <code>{@link JcsFacade#onGetFromCache(Serializable, org.springmodules.cache.CachingModel)}</code>
   * returns <code>null</code> if the specified key does not exist in the
   * cache.
   */
  public void testOnGetFromCacheWhenKeyIsNotFound() throws Exception {
    setUpCacheAdministratorAndCache();
    assertNull(jcsFacade.onGetFromCache("SomeNotExistingKey", cachingModel));
  }

  public void testOnPutInCache() throws Exception {
    setUpCacheAdministratorAndCache();

    Serializable key = "Obi-Wan";
    Object expected = "Jedi Master";

    jcsFacade.onPutInCache(key, cachingModel, expected);
    assertSame(expected, cache.get(key).getVal());
  }

  public void testOnPutInCacheWhenCacheAccessThrowsException() throws Exception {
    Method getCache = getGetCacheMethodFromCompositeCacheManagerClass();
    setUpCacheAdministratorAsMockObject(getCache);

    Method update = CompositeCache.class.getMethod("update",
        new Class[] { ICacheElement.class });
    setUpCacheAsMockObject(update);

    // cache manager finds the cache we are looking for.
    expectCacheIsFound();

    // cache manager throws exception.
    Serializable key = "Jabba";
    Object objToStore = "Bobba Fett";

    CacheElement cacheElement = new CacheElement(cachingModel.getCacheName(),
        key, objToStore);
    cache.update(cacheElement);
    cacheControl.setMatcher(new CacheElementMatcher());

    RuntimeException expected = new RuntimeException();
    cacheControl.setThrowable(expected);

    replay();

    try {
      jcsFacade.onPutInCache(key, cachingModel, objToStore);
      fail();
    } catch (CacheAccessException exception) {
      assertSame(expected, exception.getCause());
    }
    verify();
  }

  public void testOnPutInCacheWhenCacheIsNotFound() throws Exception {
    Method getCache = getGetCacheMethodFromCompositeCacheManagerClass();
    setUpCacheAdministratorAsMockObject(getCache);

    expectCacheIsNotFound();
    replay();

    try {
      jcsFacade.onPutInCache("Skywalker", cachingModel, "Luke");
      fail();
    } catch (CacheNotFoundException exception) {
      // expecting this exception
    }
    verify();
  }

  public void testOnRemoveFromCache() throws Exception {
    setUpCacheAdministratorAndCache();
    setUpCacheEntries();

    CacheEntry entry = cacheEntries[0];
    Serializable key = updateCache(entry);

    cachingModel.setGroup(entry.group);
    jcsFacade.onRemoveFromCache(entry.key, cachingModel);

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

    expectCacheIsFound();

    // cache manager throws exception.
    Serializable key = "FalconMillenium";
    RuntimeException expected = new RuntimeException();
    cache.remove(key);
    cacheControl.setThrowable(expected);

    replay();

    try {
      jcsFacade.onRemoveFromCache(key, cachingModel);
      fail();

    } catch (CacheAccessException exception) {
      assertSame(expected, exception.getCause());
    }
    verify();

  }

  public void testOnRemoveFromCacheWhenCacheIsNotFound() throws Exception {
    Method getCache = getGetCacheMethodFromCompositeCacheManagerClass();
    setUpCacheAdministratorAsMockObject(getCache);

    expectCacheIsNotFound();
    replay();

    try {
      jcsFacade.onRemoveFromCache("Chewbacca", cachingModel);
      fail();
    } catch (CacheNotFoundException exception) {
      // expecting this exception
    }
    verify();
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

  protected void setUp() {
    cachingModel = new JcsCachingModel(CACHE_NAME);
    flushingModel = new JcsFlushingModel(CACHE_NAME);
    jcsFacade = new JcsFacade();
  }

  protected void tearDown() {
    if (cacheManager != null) {
      cacheManager.shutDown();
    }
  }

  private void expectCacheIsFound() {
    cacheManager.getCache(CACHE_NAME);
    cacheManagerControl.setReturnValue(cache);
  }

  private void expectCacheIsNotFound() {
    cacheManager.getCache(CACHE_NAME);
    cacheManagerControl.setReturnValue(null);
  }

  private Method getGetCacheMethodFromCompositeCacheManagerClass()
      throws Exception {
    return CompositeCacheManager.class.getDeclaredMethod("getCache",
        new Class[] { String.class });
  }

  private void replay() {
    replay(cacheControl);
    cacheManagerControl.replay();
  }

  private void replay(MockControl mockControl) {
    if (mockControl == null) {
      return;
    }
    mockControl.replay();
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

  private void setUpCacheEntries() {
    cacheEntries = new CacheEntry[] {
        new CacheEntry("sith", "empire", "Darth Vader"),
        new CacheEntry("jedi", "rebellion", "Luke Skywalker") };
  }

  private Serializable[] updateCache() throws Exception {
    int elementCount = cacheEntries.length;
    Serializable[] keys = new Serializable[elementCount];
    for (int i = 0; i < elementCount; i++) {
      keys[i] = updateCache(cacheEntries[i]);
    }
    assertTrue("The size of the cache should be greater than zero", cache
        .getSize() > 0);
    return keys;
  }

  private Serializable updateCache(CacheEntry cacheEntry) throws Exception {
    Serializable key = cacheEntry.key;
    String group = cacheEntry.group;

    if (StringUtils.hasText(group)) {
      GroupId groupId = new GroupId(CACHE_NAME, group);
      GroupAttrName groupAttrName = new GroupAttrName(groupId, key);
      key = groupAttrName;
    }

    ICacheElement cacheElement = new CacheElement(CACHE_NAME, key,
        cacheEntry.value);
    IElementAttributes attributes = cache.getElementAttributes().copy();
    cacheElement.setElementAttributes(attributes);

    cache.update(cacheElement);
    return key;
  }

  private void verify() {
    verify(cacheControl);
    cacheManagerControl.verify();
  }

  private void verify(MockControl mockControl) {
    if (mockControl == null) {
      return;
    }
    mockControl.verify();
   }
}
