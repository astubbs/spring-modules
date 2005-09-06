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

import java.lang.reflect.Method;

import junit.framework.TestCase;

import org.easymock.classextension.MockClassControl;
import org.jboss.cache.Node;
import org.jboss.cache.TreeCache;
import org.springmodules.cache.provider.CacheAccessException;

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

  private void failIfCacheAccessExceptionIsNotThrown() {
    fail("Expecting exception <" + CacheAccessException.class.getName() + ">");
  }

  private Object getFromTreeCache(Object key) throws Exception {
    return this.treeCache.get(this.cacheProfile.getNodeFqn(), key);
  }

  private void putInTreeCache(Object key, Object value) throws Exception {
    this.treeCache.put(this.cacheProfile.getNodeFqn(), key, value);
  }

  protected void setUp() throws Exception {
    super.setUp();

    this.jbossCacheFacade = new JbossCacheFacade();

    this.cacheProfile = new JbossCacheProfile(CACHE_NODE_NAME);
  }

  private void setUpTreeCache() throws Exception {
    this.treeCache = new TreeCache();
    startTreeCache();

    this.jbossCacheFacade.setTreeCache(this.treeCache);
  }

  private void setUpTreeCacheAsMockObject(Method methodToMock) throws Exception {
    setUpTreeCacheAsMockObject(new Method[] { methodToMock });
  }

  private void setUpTreeCacheAsMockObject(Method[] methodsToMock)
      throws Exception {
    Class targetClass = TreeCache.class;

    this.treeCacheControl = MockClassControl.createControl(targetClass, null,
        null, methodsToMock);
    this.treeCache = (TreeCache) this.treeCacheControl.getMock();
    startTreeCache();

    this.jbossCacheFacade.setTreeCache(this.treeCache);
  }

  private void startTreeCache() throws Exception {
    this.treeCache.createService();
    this.treeCache.startService();

  }

  protected void tearDown() throws Exception {
    super.tearDown();

    if (this.treeCache != null) {
      this.treeCache.stopService();
      this.treeCache.destroyService();
    }
  }

  public void testOnFlushCache() throws Exception {
    setUpTreeCache();

    this.jbossCacheFacade.onFlushCache(this.cacheProfile);

    Node cacheNode = this.treeCache.get(this.cacheProfile.getNodeFqn());
    assertNull(cacheNode);
  }

  public void testOnFlushCacheWhenCacheAccessThrowsException() throws Exception {
    Method removeMethod = TreeCache.class.getDeclaredMethod("remove",
        new Class[] { String.class });
    setUpTreeCacheAsMockObject(removeMethod);

    RuntimeException expected = new RuntimeException();

    this.treeCache.remove(CACHE_NODE_NAME);
    this.treeCacheControl.setThrowable(expected);

    this.treeCacheControl.replay();

    try {
      this.jbossCacheFacade.onFlushCache(this.cacheProfile);
      failIfCacheAccessExceptionIsNotThrown();

    } catch (CacheAccessException exception) {
      assertSameNestedException(expected, exception);
    }

    this.treeCacheControl.verify();
  }

  public void testOnGetFromCache() throws Exception {
    setUpTreeCache();

    String objectToCache = "R2-D2";
    putInTreeCache(CACHE_KEY, objectToCache);

    Object cachedObject = this.jbossCacheFacade.onGetFromCache(CACHE_KEY,
        this.cacheProfile);

    assertEquals(objectToCache, cachedObject);
  }

  public void testOnPutInCache() throws Exception {
    setUpTreeCache();

    String objectToCache = "Luke Skywalker";
    this.jbossCacheFacade.onPutInCache(CACHE_KEY, this.cacheProfile,
        objectToCache);

    Object cachedObject = getFromTreeCache(CACHE_KEY);
    assertEquals(objectToCache, cachedObject);
  }

  public void testOnRemoveFromCache() throws Exception {
    setUpTreeCache();

    String objectToCache = "Falcon Millenium";
    putInTreeCache(CACHE_KEY, objectToCache);

    this.jbossCacheFacade.onRemoveFromCache(CACHE_KEY, this.cacheProfile);

    Object cachedObject = getFromTreeCache(CACHE_KEY);
    assertNull(cachedObject);
  }
}
