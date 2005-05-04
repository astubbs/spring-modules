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
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.jcs.engine.CacheElement;
import org.apache.jcs.engine.behavior.ICacheElement;
import org.apache.jcs.engine.behavior.IElementAttributes;
import org.apache.jcs.engine.control.CompositeCache;
import org.apache.jcs.engine.control.CompositeCacheManager;
import org.apache.jcs.engine.control.group.GroupAttrName;
import org.apache.jcs.engine.control.group.GroupId;
import org.springmodules.cache.key.CacheKey;
import org.springmodules.cache.provider.AbstractCacheProfileEditor;
import org.springmodules.cache.provider.CacheProfileValidator;

/**
 * <p>
 * Unit Test for <code>{@link JcsFacade}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.2 $ $Date: 2005/05/04 00:17:44 $
 */
public final class JcsFacadeTests extends TestCase {

  /**
   * A JCS Cache.
   */
  private CompositeCache cache;

  /**
   * Keys used to store/retrieve entries of the cache.
   */
  private String[] cacheKeys;

  /**
   * JCS Cache Manager.
   */
  private CompositeCacheManager cacheManager;

  /**
   * Name of the JCS to use.
   */
  private String cacheName;

  /**
   * Id used by <code>{@link #jcsFacade}</code> to get
   * <code>{@link #cacheProfiles}</code>.
   */
  private String[] cacheProfileIds;

  /**
   * Configuration options for the caching services.
   */
  private JcsCacheProfile[] cacheProfiles;

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

    this.cacheProfiles = new JcsCacheProfile[] { new JcsCacheProfile(),
        new JcsCacheProfile() };
    this.cacheProfileIds = new String[] { "firstProfile", "secondProfile" };
    this.groups = new String[] { "firstGroup", "secondGroup" };

    Map cacheProfileMap = new HashMap();

    int cacheProfileCount = this.cacheProfiles.length;
    for (int i = 0; i < cacheProfileCount; i++) {
      JcsCacheProfile cacheProfile = this.cacheProfiles[i];
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
   * Tears down the test fixture.
   */
  protected void tearDown() throws Exception {
    super.tearDown();

    this.cacheManager.shutDown();
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

    int i = 0;
    GroupId groupId = new GroupId(profile.getCacheName(), profile.getGroup());
    GroupAttrName expectedKey = new GroupAttrName(groupId, this.cacheKeys[i]);

    Serializable actualKey = this.jcsFacade.getKey(this.cacheKeys[i], profile);

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

    int i = 0;
    Serializable actualKey = this.jcsFacade.getKey(this.cacheKeys[i], profile);

    assertEquals("<Generated key>", this.cacheKeys[i], actualKey);
  }

  /**
   * Verifies that the method
   * <code>{@link JcsFacade#onFlushCache(org.springmodules.cache.provider.CacheProfile)}</code>
   * does not flush any cache or group if the cache specified in the given cache
   * profile does not exist.
   */
  public void testOnFlushCacheWhenCacheIsNotFound() throws Exception {
    this.updateCache(new Object[] { "firstObject", "secondObject" });

    // execute the method to test.
    int i = 0;
    this.cacheProfiles[i].setCacheName("NotExistingCache");
    this.jcsFacade.onFlushCache(this.cacheProfiles[i]);

    assertEquals("<Cache size>", this.cacheKeys.length, this.cache.getSize());
  }

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
    Serializable[] keys = this.updateCache(new Object[] { "firstObject",
        "secondObject" });

    int i = 0;
    // execute the method to test.
    this.jcsFacade.onFlushCache(this.cacheProfiles[i]);

    // only one group should have been flushed.
    ICacheElement cachedElement = this.cache.get(keys[i]);
    assertNull("The group '" + this.groups[i] + "' should be flushed",
        cachedElement);

    i++;
    cachedElement = this.cache.get(keys[i]);
    assertNotNull("The group '" + this.groups[i] + "' should not be flushed",
        cachedElement);
  }

  /**
   * Verifies that the method
   * <code>{@link JcsFacade#onFlushCache(org.springmodules.cache.provider.CacheProfile)}</code>
   * flushes the whole cache if there are not any specified groups.
   */
  public void testOnFlushCacheWithoutGroups() throws Exception {
    this.updateCache(new Object[] { "firstObject", "secondObject" });

    // execute the method to test.
    int i = 0;
    this.cacheProfiles[i].setGroup(null);
    this.jcsFacade.onFlushCache(this.cacheProfiles[i]);

    // the whole cache should be flushed.
    assertEquals("<Cache size>", 0, this.cache.getSize());
  }

  /**
   * Stores in the cache each element of the given array.
   * 
   * @param objectsToStore
   *          the array containing the objects to store in the cache.
   * @return the keys used to stored the objects in the cache.
   */
  protected Serializable[] updateCache(Object[] objectsToStore)
      throws Exception {
    int cacheKeyCount = this.cacheKeys.length;
    assertEquals("<Object to store count>", cacheKeyCount,
        objectsToStore.length);

    Serializable[] keys = new Serializable[cacheKeyCount];

    for (int i = 0; i < cacheKeyCount; i++) {
      keys[i] = this.jcsFacade.getKey(this.cacheKeys[i], this.cacheProfiles[i]);
      ICacheElement cacheElement = this.createNewCacheElement(keys[i],
          objectsToStore[i]);
      this.cache.update(cacheElement);
    }

    return keys;
  }
}
