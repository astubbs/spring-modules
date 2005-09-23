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
import org.springmodules.cache.provider.CacheAccessException;
import org.springmodules.cache.provider.CacheProfileEditor;
import org.springmodules.cache.provider.CacheProfileValidator;
import org.springmodules.cache.provider.FatalCacheException;

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

  private static final String CACHE_KEY = "key";

  private static final String CACHE_NODE_NAME = "a/b/c/d";

  private JbossCacheProfile cacheProfile;

  private JbossCacheFacade jbossCacheFacade;

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
    return treeCache.get(cacheProfile.getNodeFqn(), key);
  }

  private void putInTreeCache(Object key, Object value) throws Exception {
    treeCache.put(cacheProfile.getNodeFqn(), key, value);
  }

  protected void setUp() throws Exception {
    super.setUp();

    jbossCacheFacade = new JbossCacheFacade();

    cacheProfile = new JbossCacheProfile(CACHE_NODE_NAME);
  }

  private void setUpTreeCache() throws Exception {
    treeCache = new TreeCache();
    startTreeCache();

    jbossCacheFacade.setCacheManager(treeCache);
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

    jbossCacheFacade.setCacheManager(treeCache);
  }

  private void startTreeCache() throws Exception {
    treeCache.createService();
    treeCache.startService();
  }

  protected void tearDown() throws Exception {
    super.tearDown();

    if (treeCache != null) {
      treeCache.stopService();
      treeCache.destroyService();
    }
  }

  public void testGetCacheProfileEditor() {
    PropertyEditor editor = jbossCacheFacade.getCacheProfileEditor();

    assertNotNull(editor);
    assertEquals(CacheProfileEditor.class, editor.getClass());

    CacheProfileEditor profileEditor = (CacheProfileEditor) editor;
    assertEquals(JbossCacheProfile.class, profileEditor.getCacheProfileClass());
    assertNull(profileEditor.getCacheProfilePropertyEditors());
  }

  /**
   * Verifies that the method
   * <code>{@link JbossCacheFacade#getCacheProfileValidator()}</code> returns
   * an an instance of <code>{@link JbossCacheProfileValidator}</code> not
   * equal to <code>null</code>.
   */
  public void testGetCacheProfileValidator() {
    CacheProfileValidator validator = jbossCacheFacade
        .getCacheProfileValidator();
    assertNotNull(validator);
    assertEquals(JbossCacheProfileValidator.class, validator.getClass());
  }

  public void testOnFlushCache() throws Exception {
    setUpTreeCache();

    jbossCacheFacade.onFlushCache(cacheProfile);

    Node cacheNode = treeCache.get(cacheProfile.getNodeFqn());
    assertNull(cacheNode);
  }

  public void testOnFlushCacheWhenCacheAccessThrowsException() throws Exception {
    Method removeMethod = TreeCache.class.getDeclaredMethod("remove",
        new Class[] { String.class });
    setUpTreeCacheAsMockObject(removeMethod);

    RuntimeException expected = new RuntimeException();

    treeCache.remove(CACHE_NODE_NAME);
    treeCacheControl.setThrowable(expected);

    treeCacheControl.replay();

    try {
      jbossCacheFacade.onFlushCache(cacheProfile);
      fail();

    } catch (CacheAccessException exception) {
      assertSameNestedException(expected, exception);
    }

    treeCacheControl.verify();
  }

  public void testOnGetFromCache() throws Exception {
    setUpTreeCache();

    String objectToCache = "R2-D2";
    putInTreeCache(CACHE_KEY, objectToCache);

    Object cachedObject = jbossCacheFacade.onGetFromCache(CACHE_KEY,
        cacheProfile);

    assertEquals(objectToCache, cachedObject);
  }

  public void testOnGetFromCacheWhenCacheAccessThrowsException()
      throws Exception {
    Method getMethod = TreeCache.class.getDeclaredMethod("get", new Class[] {
        String.class, Object.class });
    setUpTreeCacheAsMockObject(getMethod);

    RuntimeException expected = new RuntimeException();

    treeCache.get(CACHE_NODE_NAME, CACHE_KEY);
    treeCacheControl.setThrowable(expected);

    treeCacheControl.replay();

    try {
      jbossCacheFacade.onGetFromCache(CACHE_KEY, cacheProfile);
      fail();

    } catch (CacheAccessException exception) {
      assertSameNestedException(expected, exception);
    }

    treeCacheControl.verify();
  }

  public void testOnPutInCache() throws Exception {
    setUpTreeCache();

    String objectToCache = "Luke Skywalker";
    jbossCacheFacade.onPutInCache(CACHE_KEY, cacheProfile, objectToCache);

    Object cachedObject = getFromTreeCache(CACHE_KEY);
    assertEquals(objectToCache, cachedObject);
  }

  public void testOnPutInCacheWhenCacheAccessThrowsException() throws Exception {
    Method putMethod = TreeCache.class.getDeclaredMethod("put", new Class[] {
        String.class, Object.class, Object.class });
    setUpTreeCacheAsMockObject(putMethod);

    String objectToCache = "Luke";
    RuntimeException expected = new RuntimeException();

    treeCache.put(CACHE_NODE_NAME, CACHE_KEY, objectToCache);
    treeCacheControl.setThrowable(expected);

    treeCacheControl.replay();

    try {
      jbossCacheFacade.onPutInCache(CACHE_KEY, cacheProfile, objectToCache);
      fail();

    } catch (CacheAccessException exception) {
      assertSameNestedException(expected, exception);
    }

    treeCacheControl.verify();
  }

  public void testOnRemoveFromCache() throws Exception {
    setUpTreeCache();

    String objectToCache = "Falcon Millenium";
    putInTreeCache(CACHE_KEY, objectToCache);

    jbossCacheFacade.onRemoveFromCache(CACHE_KEY, cacheProfile);

    Object cachedObject = getFromTreeCache(CACHE_KEY);
    assertNull(cachedObject);
  }

  public void testOnRemoveFromCacheWhenCacheAccessThrowsException()
      throws Exception {
    Method removeMethod = TreeCache.class.getDeclaredMethod("remove",
        new Class[] { String.class, Object.class });
    setUpTreeCacheAsMockObject(removeMethod);

    RuntimeException expected = new RuntimeException();

    treeCache.remove(CACHE_NODE_NAME, CACHE_KEY);
    treeCacheControl.setThrowable(expected);

    treeCacheControl.replay();

    try {
      jbossCacheFacade.onRemoveFromCache(CACHE_KEY, cacheProfile);
      fail();

    } catch (CacheAccessException exception) {
      assertSameNestedException(expected, exception);
    }

    treeCacheControl.verify();
  }

  public void testValidateCacheManagerWithCacheManagerEqualToNull() {
    jbossCacheFacade.setCacheManager(null);
    try {
      jbossCacheFacade.validateCacheManager();
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

    jbossCacheFacade.validateCacheManager();
  }

}
