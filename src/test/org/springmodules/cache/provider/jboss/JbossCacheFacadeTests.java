/* 
 * Created on Sep 1, 2005
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
package org.springmodules.cache.provider.jboss;

import java.beans.PropertyEditor;
import java.lang.reflect.Method;

import junit.framework.TestCase;

import org.easymock.classextension.MockClassControl;
import org.jboss.cache.Node;
import org.jboss.cache.TreeCache;
import org.springmodules.cache.FatalCacheException;
import org.springmodules.cache.provider.CacheAccessException;
import org.springmodules.cache.provider.CacheModelValidator;
import org.springmodules.cache.provider.ReflectionCacheModelEditor;

/**
 * <p>
 * Unit Tests for <code>{@link JbossCacheFacade}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public class JbossCacheFacadeTests extends TestCase {

  private static final String KEY = "key";

  private static final String NODE_FQN = "a/b/c/d";

  private JbossCacheFacade cacheFacade;

  private JbossCacheCachingModel cachingModel;

  private JbossCacheFlushingModel flushingModel;

  private TreeCache treeCache;

  private MockClassControl treeCacheControl;

  public JbossCacheFacadeTests(String name) {
    super(name);
  }

  private void assertSameNestedException(Exception expected,
      CacheAccessException root) {
    assertSame(expected, root.getCause());
  }

  private Object getFromTreeCache(Object key) throws Exception {
    return treeCache.get(cachingModel.getNode(), key);
  }

  private void putInTreeCache(Object key, Object value) throws Exception {
    treeCache.put(cachingModel.getNode(), key, value);
  }

  protected void setUp() {
    cacheFacade = new JbossCacheFacade();
    cachingModel = new JbossCacheCachingModel(NODE_FQN);
    flushingModel = new JbossCacheFlushingModel(NODE_FQN);
  }

  private void setUpTreeCache() throws Exception {
    treeCache = new TreeCache();
    startTreeCache();
    cacheFacade.setCacheManager(treeCache);
  }

  private void setUpTreeCacheAsMockObject(Method methodToMock) throws Exception {
    setUpTreeCacheAsMockObject(new Method[] { methodToMock });
  }

  private void setUpTreeCacheAsMockObject(Method[] methodsToMock)
      throws Exception {
    Class targetClass = TreeCache.class;

    treeCacheControl = MockClassControl.createControl(targetClass, null, null,
        methodsToMock);
    treeCache = (TreeCache) treeCacheControl.getMock();
    startTreeCache();

    cacheFacade.setCacheManager(treeCache);
  }

  private void startTreeCache() throws Exception {
    treeCache.createService();
    treeCache.startService();
  }

  protected void tearDown() {
    if (treeCache != null) {
      treeCache.stopService();
      treeCache.destroyService();
    }
  }

  public void testGetCachingModelEditor() {
    PropertyEditor editor = cacheFacade.getCachingModelEditor();

    assertNotNull(editor);
    assertEquals(ReflectionCacheModelEditor.class, editor.getClass());

    ReflectionCacheModelEditor modelEditor = (ReflectionCacheModelEditor) editor;
    assertEquals(JbossCacheCachingModel.class, modelEditor.getCacheModelClass());
    assertNull(modelEditor.getCacheModelPropertyEditors());
  }

  public void testGetFlushingModelEditor() {
    PropertyEditor editor = cacheFacade.getFlushingModelEditor();

    assertNotNull(editor);
    assertEquals(ReflectionCacheModelEditor.class, editor.getClass());

    ReflectionCacheModelEditor modelEditor = (ReflectionCacheModelEditor) editor;
    assertEquals(JbossCacheFlushingModel.class, modelEditor
        .getCacheModelClass());
    assertNull(modelEditor.getCacheModelPropertyEditors());
  }

  /**
   * Verifies that the method
   * <code>{@link JbossCacheFacade#getCacheModelValidator()}</code> returns an
   * an instance of <code>{@link JbossCacheModelValidator}</code> not equal to
   * <code>null</code>.
   */
  public void testGetCacheModelValidator() {
    CacheModelValidator validator = cacheFacade.getCacheModelValidator();
    assertNotNull(validator);
    assertEquals(JbossCacheModelValidator.class, validator.getClass());
  }

  public void testOnFlushCache() throws Exception {
    setUpTreeCache();
    cacheFacade.onFlushCache(flushingModel);
    Node cacheNode = treeCache.get(NODE_FQN);
    assertNull(cacheNode);
  }

  public void testOnFlushCacheWhenCacheAccessThrowsException() throws Exception {
    Method removeMethod = TreeCache.class.getDeclaredMethod("remove",
        new Class[] { String.class });
    setUpTreeCacheAsMockObject(removeMethod);

    RuntimeException expected = new RuntimeException();

    treeCache.remove(NODE_FQN);
    treeCacheControl.setThrowable(expected);
    treeCacheControl.replay();

    try {
      cacheFacade.onFlushCache(flushingModel);
      fail();

    } catch (CacheAccessException exception) {
      assertSameNestedException(expected, exception);
    }

    treeCacheControl.verify();
  }

  public void testOnGetFromCache() throws Exception {
    setUpTreeCache();

    String objectToCache = "R2-D2";
    putInTreeCache(KEY, objectToCache);

    Object cachedObject = cacheFacade.onGetFromCache(KEY, cachingModel);

    assertEquals(objectToCache, cachedObject);
  }

  public void testOnGetFromCacheWhenCacheAccessThrowsException()
      throws Exception {
    Method getMethod = TreeCache.class.getDeclaredMethod("get", new Class[] {
        String.class, Object.class });
    setUpTreeCacheAsMockObject(getMethod);

    RuntimeException expected = new RuntimeException();

    treeCache.get(NODE_FQN, KEY);
    treeCacheControl.setThrowable(expected);

    treeCacheControl.replay();

    try {
      cacheFacade.onGetFromCache(KEY, cachingModel);
      fail();

    } catch (CacheAccessException exception) {
      assertSameNestedException(expected, exception);
    }

    treeCacheControl.verify();
  }

  public void testOnPutInCache() throws Exception {
    setUpTreeCache();

    String objectToCache = "Luke Skywalker";
    cacheFacade.onPutInCache(KEY, cachingModel, objectToCache);

    Object cachedObject = getFromTreeCache(KEY);
    assertEquals(objectToCache, cachedObject);
  }

  public void testOnPutInCacheWhenCacheAccessThrowsException() throws Exception {
    Method putMethod = TreeCache.class.getDeclaredMethod("put", new Class[] {
        String.class, Object.class, Object.class });
    setUpTreeCacheAsMockObject(putMethod);

    String objectToCache = "Luke";
    RuntimeException expected = new RuntimeException();

    treeCache.put(NODE_FQN, KEY, objectToCache);
    treeCacheControl.setThrowable(expected);

    treeCacheControl.replay();

    try {
      cacheFacade.onPutInCache(KEY, cachingModel, objectToCache);
      fail();

    } catch (CacheAccessException exception) {
      assertSameNestedException(expected, exception);
    }

    treeCacheControl.verify();
  }

  public void testOnRemoveFromCache() throws Exception {
    setUpTreeCache();

    String objectToCache = "Falcon Millenium";
    putInTreeCache(KEY, objectToCache);

    cacheFacade.onRemoveFromCache(KEY, cachingModel);

    Object cachedObject = getFromTreeCache(KEY);
    assertNull(cachedObject);
  }

  public void testOnRemoveFromCacheWhenCacheAccessThrowsException()
      throws Exception {
    Method removeMethod = TreeCache.class.getDeclaredMethod("remove",
        new Class[] { String.class, Object.class });
    setUpTreeCacheAsMockObject(removeMethod);

    RuntimeException expected = new RuntimeException();

    treeCache.remove(NODE_FQN, KEY);
    treeCacheControl.setThrowable(expected);

    treeCacheControl.replay();

    try {
      cacheFacade.onRemoveFromCache(KEY, cachingModel);
      fail();

    } catch (CacheAccessException exception) {
      assertSameNestedException(expected, exception);
    }

    treeCacheControl.verify();
  }

  public void testValidateCacheManagerWithCacheManagerEqualToNull() {
    cacheFacade.setCacheManager(null);
    try {
      cacheFacade.validateCacheManager();
      fail();

    } catch (FatalCacheException exception) {
      // we are expecting this exception.
    }
  }

  /**
   * Verifies that the method
   * <code>{@link JbossCacheFacade#validateCacheManager()}</code> does not
   * throw any exception if the cache manager is not <code>null</code>.
   */
  public void testValidateCacheManagerWithCacheManagerNotEqualToNull()
      throws Exception {
    setUpTreeCache();

    cacheFacade.validateCacheManager();
  }

}
