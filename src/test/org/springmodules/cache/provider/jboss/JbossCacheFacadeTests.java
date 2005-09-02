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

import org.jboss.cache.Node;
import org.jboss.cache.TreeCache;

import junit.framework.TestCase;

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

  public JbossCacheFacadeTests(String name) {
    super(name);
  }

  private Object getFromTreeCache(Object key) throws Exception {
    return this.treeCache.get(this.cacheProfile.getNodeFqn(), key);
  }

  private void putInTreeCache(Object key, Object value) throws Exception {
    this.treeCache.put(this.cacheProfile.getNodeFqn(), key, value);
  }

  protected void setUp() throws Exception {
    super.setUp();

    this.treeCache = new TreeCache();
    this.treeCache.createService();
    this.treeCache.startService();

    this.jbossCacheFacade = new JbossCacheFacade();
    this.jbossCacheFacade.setTreeCache(this.treeCache);

    this.cacheProfile = new JbossCacheProfile(CACHE_NODE_NAME);
  }

  protected void tearDown() throws Exception {
    super.tearDown();

    this.treeCache.stopService();
    this.treeCache.destroyService();
  }

  public void testOnFlushCache() throws Exception {
    this.jbossCacheFacade.onFlushCache(this.cacheProfile);

    Node cacheNode = this.treeCache.get(this.cacheProfile.getNodeFqn());
    assertNull(cacheNode);
  }

  public void testOnGetFromCache() throws Exception {
    String objectToCache = "R2-D2";
    String key = CACHE_KEY;
    putInTreeCache(key, objectToCache);

    Object cachedObject = this.jbossCacheFacade.onGetFromCache(key,
        this.cacheProfile);

    assertEquals(objectToCache, cachedObject);
  }

  public void testOnPutInCache() throws Exception {
    String objectToCache = "Luke Skywalker";
    String key = CACHE_KEY;

    this.jbossCacheFacade.onPutInCache(key, this.cacheProfile, objectToCache);

    Object cachedObject = getFromTreeCache(key);
    assertEquals(objectToCache, cachedObject);
  }

  public void testOnRemoveFromCache() throws Exception {
    String objectToCache = "Falcon Millenium";
    String key = CACHE_KEY;
    putInTreeCache(key, objectToCache);

    this.jbossCacheFacade.onRemoveFromCache(key, this.cacheProfile);

    Object cachedObject = getFromTreeCache(key);
    assertNull(cachedObject);
  }
}
