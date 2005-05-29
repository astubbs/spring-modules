/* 
 * Created on May 28, 2005
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
package org.springmodules.cache.provider.oscache;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.springmodules.cache.provider.AbstractCacheProfileEditor;
import org.springmodules.cache.provider.CacheProfileValidator;

import com.opensymphony.oscache.base.Cache;
import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.base.events.CacheEntryEventListener;
import com.opensymphony.oscache.extra.CacheEntryEventListenerImpl;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;

/**
 * <p>
 * Unit Tests for <code>{@link OsCacheFacade}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/05/29 05:10:19 $
 */
public class OsCacheFacadeTests extends TestCase {

  /**
   * OSCache cache administrator.
   */
  private GeneralCacheAdministrator cacheAdministrator;

  /**
   * Listens to cache entry events.
   */
  private CacheEntryEventListenerImpl cacheEntryEventListener;

  /**
   * Key used to store/retrieve an entry of the cache.
   */
  private String cacheKey;

  /**
   * Configuration options for the caching services.
   */
  private OsCacheProfile cacheProfile;

  /**
   * Name of the groups in <code>{@link #cacheAdministrator}</code> to use.
   */
  private String[] groups;

  /**
   * Primary object that is under test.
   */
  private OsCacheFacade osCacheFacade;

  /**
   * Id used by <code>{@link #osCacheFacade}</code> to get
   * <code>{@link #cacheProfile}</code>.
   */
  private String cacheProfileId;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case to construct.
   */
  public OsCacheFacadeTests(String name) {
    super(name);
  }

  /**
   * Sets up the test fixture.
   */
  protected void setUp() throws Exception {
    super.setUp();

    this.cacheAdministrator = new GeneralCacheAdministrator();

    Cache cache = this.cacheAdministrator.getCache();

    this.cacheEntryEventListener = new CacheEntryEventListenerImpl();
    cache.addCacheEventListener(this.cacheEntryEventListener,
        CacheEntryEventListener.class);

    this.cacheKey = "KEY";
    this.cacheProfile = new OsCacheProfile();
    this.cacheProfileId = "CacheProfile";

    this.groups = new String[] { "main", "test" };

    Map cacheProfiles = new HashMap();
    cacheProfiles.put(this.cacheProfileId, this.cacheProfile);

    this.osCacheFacade = new OsCacheFacade();
    this.osCacheFacade.setCacheManager(this.cacheAdministrator);
    this.osCacheFacade.setCacheProfiles(cacheProfiles);
  }

  /**
   * Tears down the test fixture.
   */
  protected void tearDown() throws Exception {
    super.tearDown();

    this.cacheAdministrator.destroy();
  }

  /**
   * Verifies that the method
   * <code>{@link OsCacheFacade#getCacheProfileEditor()}</code> returns an
   * instance of <code>{@link OsCacheProfileEditor}</code> not equal to
   * <code>null</code>.
   */
  public void testGetCacheProfileEditor() {
    AbstractCacheProfileEditor cacheProfileEditor = this.osCacheFacade
        .getCacheProfileEditor();

    assertNotNull("The cache profile editor should not be null",
        cacheProfileEditor);

    Class expectedClass = OsCacheProfileEditor.class;
    Class actualClass = cacheProfileEditor.getClass();

    assertEquals("<Class of the cache profile editor>", expectedClass,
        actualClass);
  }

  /**
   * Verifies that the method
   * <code>{@link OsCacheFacade#getCacheProfileValidator()}</code> returns an
   * an instance of <code>{@link OsCacheProfileValidator}</code> not equal to
   * <code>null</code>.
   */
  public void testGetCacheProfileValidator() {
    CacheProfileValidator cacheProfileValidator = this.osCacheFacade
        .getCacheProfileValidator();

    assertNotNull("The cache profile validator should not be null",
        cacheProfileValidator);

    Class expectedClass = OsCacheProfileValidator.class;
    Class actualClass = cacheProfileValidator.getClass();

    assertEquals("<Class of the cache profile validator>", expectedClass,
        actualClass);
  }

  /**
   * Verifies that the method
   * <code>{@link OsCacheFacade#onFlushCache(org.springmodules.cache.provider.CacheProfile)}</code>
   * flushes the group(s) specified in the given cache profile.
   */
  public void testOnFlushCache() {
    Object objectToStore = "An Object";
    this.cacheAdministrator.putInCache(this.cacheKey, objectToStore,
        this.groups);

    String groupToFlush = this.groups[0];
    this.cacheProfile.setGroups(new String[] { groupToFlush });

    // execute the method to test.
    this.osCacheFacade.onFlushCache(this.cacheProfile);

    assertEquals("Number of groups flushed", 1, this.cacheEntryEventListener
        .getGroupFlushedCount());
  }

  /**
   * Verifies that the method
   * <code>{@link OsCacheFacade#onFlushCache(org.springmodules.cache.provider.CacheProfile)}</code>
   * flushes all the groups if none is specified in the given cache profile.
   */
  public void testOnFlushCacheWithoutGroups() {
    Object objectToStore = "An Object";
    this.cacheAdministrator.putInCache(this.cacheKey, objectToStore,
        this.groups);

    this.cacheProfile.setGroups((String[]) null);

    // execute the method to test.
    this.osCacheFacade.onFlushCache(this.cacheProfile);

    String cachedObject = this.cacheAdministrator.getProperty(this.cacheKey);
    assertNull("The entire cache should be empty", cachedObject);
  }

  /**
   * Verifies that the method
   * <code>{@link OsCacheFacade#onGetFromCache(java.io.Serializable, org.springmodules.cache.provider.CacheProfile)}</code>
   * retrieves, from the cache specified in the given cache profile, the entry
   * stored under the given key.
   */
  public void testOnGetFromCache() {
    Object objectToStore = "An Object";
    this.cacheAdministrator.putInCache(this.cacheKey, objectToStore);

    // execute the method to test.
    Object cachedObject = this.osCacheFacade.onGetFromCache(this.cacheKey,
        this.cacheProfile);

    assertSame("<Cached object>", objectToStore, cachedObject);
  }

  /**
   * Verifies that the method
   * <code>{@link OsCacheFacade#onGetFromCache(java.io.Serializable, org.springmodules.cache.provider.CacheProfile)}</code>
   * returns <code>null</code> if the specified key does not exist in the
   * cache.
   */
  public void testOnGetFromCacheWhenKeyIsNotFound() {
    // execute the method to test.
    Object cachedObject = this.osCacheFacade.onGetFromCache("NonExistingKey",
        this.cacheProfile);

    assertNull("The retrieved object should be null", cachedObject);
  }

  /**
   * Verifies that the method
   * <code>{@link OsCacheFacade#onPutInCache(java.io.Serializable, org.springmodules.cache.provider.CacheProfile, Object)}</code>
   * stores an entry in the group(s) specified in the given cache profile using
   * the given key.
   */
  public void testOnPutInCacheWithGroups() throws Exception {
    Object objectToStore = "An Object";

    String group = this.groups[0];
    this.cacheProfile.setGroups(new String[] { group });

    // execute the method to test.
    this.osCacheFacade.onPutInCache(this.cacheKey, this.cacheProfile,
        objectToStore);

    Object cachedObject = this.cacheAdministrator.getFromCache(this.cacheKey);
    assertSame("<Cached object>", objectToStore, cachedObject);

    // if we flush the group used, we should not be able to get the cached
    // object.
    this.cacheAdministrator.flushGroup(group);

    try {
      this.cacheAdministrator.getFromCache(this.cacheKey);
      fail("A 'NeedsRefreshException' should have been thrown");
    } catch (NeedsRefreshException exception) {
      // we are expecting this exception
    }
  }

  /**
   * Verifies that the method
   * <code>{@link OsCacheFacade#onPutInCache(java.io.Serializable, org.springmodules.cache.provider.CacheProfile, Object)}</code>
   * stores an entry using the given key. The entry should not be associated
   * with any group.
   */
  public void testOnPutInCacheWithoutGroups() throws Exception {
    Object objectToStore = "An Object";

    this.cacheProfile.setGroups((String[]) null);

    // execute the method to test.
    this.osCacheFacade.onPutInCache(this.cacheKey, this.cacheProfile,
        objectToStore);

    Object cachedObject = this.cacheAdministrator.getFromCache(this.cacheKey);
    assertSame("<Cached object>", objectToStore, cachedObject);

    // if we flush all the groups, we still should be able to get the cached
    // object.
    int groupCount = this.groups.length;
    for (int i = 0; i < groupCount; i++) {
      String group = this.groups[i];
      this.cacheAdministrator.flushGroup(group);
    }

    cachedObject = this.cacheAdministrator.getFromCache(this.cacheKey);
    assertSame("<Cached object>", objectToStore, cachedObject);
  }

  /**
   * Verifies that the method
   * <code>{@link OsCacheFacade#removeFromCache(java.io.Serializable, String)}</code>
   * removes the entry stored under the given key.
   */
  public void removeFromCache() {
    Object objectToStore = "An Object";
    this.cacheAdministrator.putInCache(this.cacheKey, objectToStore);

    // execute the method to test.
    this.osCacheFacade.removeFromCache(this.cacheKey, this.cacheProfileId);

    try {
      this.cacheAdministrator.getFromCache(this.cacheKey);
      fail("A 'NeedsRefreshException' should have been thrown");
    } catch (NeedsRefreshException exception) {
      // we are expecting this exception
    }
  }

  /**
   * Verifies that the method
   * <code>{@link OsCacheFacade#validateCacheManager()}</code> throws an
   * <code>IllegalStateException</code> if the cache manager is
   * <code>null</code>.
   */
  public void testValidateCacheManagerWithCacheManagerEqualToNull() {
    this.osCacheFacade.setCacheManager(null);
    try {
      this.osCacheFacade.validateCacheManager();
      fail("An 'IllegalStateException' should have been thrown");
    } catch (IllegalStateException exception) {
      // we are expecting this exception.
    }
  }

  /**
   * Verifies that the method
   * <code>{@link OsCacheFacade#validateCacheManager()}</code> does not throw
   * any exception if the cache manager is not <code>null</code>.
   */
  public void testValidateCacheManagerWithCacheManagerNotEqualToNull() {
    this.osCacheFacade.validateCacheManager();
  }
}
