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

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.easymock.classextension.MockClassControl;
import org.springmodules.cache.provider.AbstractCacheProfileEditor;
import org.springmodules.cache.provider.CacheProfileValidator;
import org.springmodules.cache.provider.InvalidConfigurationException;

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
 * @version $Revision: 1.6 $ $Date: 2005/09/04 01:33:53 $
 */
public class OsCacheFacadeTests extends TestCase {

  private static final String CACHE_KEY = "key";

  private static final String CACHE_PROFILE_ID = "cacheProfile";

  private GeneralCacheAdministrator cacheAdministrator;

  private MockClassControl cacheAdministratorControl;

  private CacheEntryEventListenerImpl cacheEntryEventListener;

  private OsCacheProfile cacheProfile;

  /**
   * Name of the groups in <code>{@link #cacheAdministrator}</code> to use.
   */
  private String[] groups;

  /**
   * Primary object that is under test.
   */
  private OsCacheFacade osCacheFacade;

  public OsCacheFacadeTests(String name) {
    super(name);
  }

  protected void setUp() throws Exception {
    super.setUp();

    this.cacheProfile = new OsCacheProfile();

    this.groups = new String[] { "Empire", "Rebels" };

    Map cacheProfiles = new HashMap();
    cacheProfiles.put(CACHE_PROFILE_ID, this.cacheProfile);

    this.osCacheFacade = new OsCacheFacade();
    this.osCacheFacade.setCacheProfiles(cacheProfiles);
  }

  private void setUpCacheAdministrator() {
    this.cacheAdministrator = new GeneralCacheAdministrator();
    this.osCacheFacade.setCacheManager(this.cacheAdministrator);

    Cache cache = this.cacheAdministrator.getCache();

    this.cacheEntryEventListener = new CacheEntryEventListenerImpl();
    cache.addCacheEventListener(this.cacheEntryEventListener,
        CacheEntryEventListener.class);
  }

  private void setUpCacheAdministratorAsMockObject(Method methodToMock) {
    setUpCacheAdministratorAsMockObject(new Method[] { methodToMock });
  }

  private void setUpCacheAdministratorAsMockObject(Method[] methodsToMock) {
    Class targetClass = GeneralCacheAdministrator.class;

    this.cacheAdministratorControl = MockClassControl.createControl(
        targetClass, null, null, methodsToMock);
    this.cacheAdministrator = (GeneralCacheAdministrator) this.cacheAdministratorControl
        .getMock();

    this.osCacheFacade.setCacheManager(this.cacheAdministrator);
  }

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
    setUpCacheAdministrator();

    AbstractCacheProfileEditor cacheProfileEditor = this.osCacheFacade
        .getCacheProfileEditor();

    assertNotNull(cacheProfileEditor);
    assertEquals(OsCacheProfileEditor.class, cacheProfileEditor.getClass());
  }

  /**
   * Verifies that the method
   * <code>{@link OsCacheFacade#getCacheProfileValidator()}</code> returns an
   * an instance of <code>{@link OsCacheProfileValidator}</code> not equal to
   * <code>null</code>.
   */
  public void testGetCacheProfileValidator() {
    setUpCacheAdministrator();

    CacheProfileValidator cacheProfileValidator = this.osCacheFacade
        .getCacheProfileValidator();

    assertNotNull(cacheProfileValidator);

    assertEquals(OsCacheProfileValidator.class, cacheProfileValidator
        .getClass());
  }

  public void testIsSerializableCacheElementRequired() {
    setUpCacheAdministrator();

    assertFalse(this.osCacheFacade.isSerializableCacheElementRequired());
  }

  /**
   * Verifies that the method
   * <code>{@link OsCacheFacade#onCancelCacheUpdate(java.io.Serializable)}</code>
   * cancels the update of the entry under the given key.
   */
  public void testOnCancelCacheUpdate() throws Exception {
    Method cancelUpdateMethod = GeneralCacheAdministrator.class.getMethod(
        "cancelUpdate", new Class[] { String.class });

    setUpCacheAdministratorAsMockObject(cancelUpdateMethod);

    String key = "Jedi";

    this.cacheAdministrator.cancelUpdate(key);
    this.cacheAdministratorControl.replay();

    // execute the method to test.
    this.osCacheFacade.cancelCacheUpdate(key);

    this.cacheAdministratorControl.verify();
  }

  public void testOnFlushCache() {
    setUpCacheAdministrator();

    Object objectToStore = "An Object";
    this.cacheAdministrator.putInCache(CACHE_KEY, objectToStore, this.groups);

    String groupToFlush = this.groups[0];
    this.cacheProfile.setGroups(new String[] { groupToFlush });

    // execute the method to test.
    this.osCacheFacade.onFlushCache(this.cacheProfile);

    assertEquals("Number of groups flushed", 1, this.cacheEntryEventListener
        .getGroupFlushedCount());
  }

  public void testOnFlushCacheWithoutGroups() {
    setUpCacheAdministrator();

    Object objectToStore = "An Object";
    this.cacheAdministrator.putInCache(CACHE_KEY, objectToStore, this.groups);

    this.cacheProfile.setGroups((String[]) null);

    // execute the method to test.
    this.osCacheFacade.onFlushCache(this.cacheProfile);

    String cachedObject = this.cacheAdministrator.getProperty(CACHE_KEY);
    assertNull(cachedObject);
  }

  public void testOnGetFromCache() {
    setUpCacheAdministrator();

    Object expected = "An Object";
    this.cacheAdministrator.putInCache(CACHE_KEY, expected);

    // execute the method to test.
    Object actual = this.osCacheFacade.onGetFromCache(CACHE_KEY,
        this.cacheProfile);

    assertSame(expected, actual);
  }

  public void testOnGetFromCacheWhenKeyIsNotFound() {
    setUpCacheAdministrator();

    Object cachedObject = this.osCacheFacade.onGetFromCache("NonExistingKey",
        this.cacheProfile);

    assertNull(cachedObject);
  }

  public void testOnGetFromCacheWhenRefreshPeriodIsNotNullAndCronExpressionIsNotNull()
      throws Exception {
    Method getFromCacheMethod = GeneralCacheAdministrator.class
        .getDeclaredMethod("getFromCache", new Class[] { String.class,
            int.class, String.class });

    this.setUpCacheAdministratorAsMockObject(getFromCacheMethod);
    String cronExpression = "* * * 0 0";
    int refreshPeriod = 45;

    this.cacheProfile.setCronExpression(cronExpression);
    this.cacheProfile.setRefreshPeriod(refreshPeriod);
    Object expected = "Anakin";

    this.cacheAdministrator.getFromCache(CACHE_KEY, refreshPeriod,
        cronExpression);
    this.cacheAdministratorControl.setReturnValue(expected);

    this.cacheAdministratorControl.replay();

    // execute the method to test.
    Object actual = this.osCacheFacade.onGetFromCache(CACHE_KEY,
        this.cacheProfile);

    assertSame(expected, actual);

    this.cacheAdministratorControl.verify();
  }

  public void testOnGetFromCacheWhenRefreshPeriodIsNotNullAndCronExpressionIsNull()
      throws Exception {
    Method getFromCacheMethod = GeneralCacheAdministrator.class
        .getDeclaredMethod("getFromCache", new Class[] { String.class,
            int.class });

    setUpCacheAdministratorAsMockObject(getFromCacheMethod);
    int refreshPeriod = 556;

    this.cacheProfile.setRefreshPeriod(refreshPeriod);
    Object expected = "Anakin";

    this.cacheAdministrator.getFromCache(CACHE_KEY, refreshPeriod);
    this.cacheAdministratorControl.setReturnValue(expected);

    this.cacheAdministratorControl.replay();

    // execute the method to test.
    Object actual = this.osCacheFacade.onGetFromCache(CACHE_KEY,
        this.cacheProfile);

    assertSame(expected, actual);

    this.cacheAdministratorControl.verify();
  }

  public void testOnGetFromCacheWhenRefreshPeriodIsNull() throws Exception {
    Method getFromCacheMethod = GeneralCacheAdministrator.class
        .getDeclaredMethod("getFromCache", new Class[] { String.class });

    setUpCacheAdministratorAsMockObject(getFromCacheMethod);

    this.cacheProfile.setRefreshPeriod(null);
    Object expected = "Anakin";

    // expectation: retrieve an entry using only the provided key.
    this.cacheAdministrator.getFromCache(CACHE_KEY);
    this.cacheAdministratorControl.setReturnValue(expected);

    this.cacheAdministratorControl.replay();

    // execute the method to test.
    Object actual = this.osCacheFacade.onGetFromCache(CACHE_KEY,
        this.cacheProfile);

    assertSame(expected, actual);

    this.cacheAdministratorControl.verify();
  }

  public void testOnPutInCacheWithGroups() throws Exception {
    setUpCacheAdministrator();

    Object objectToStore = "An Object";

    String group = this.groups[0];
    this.cacheProfile.setGroups(new String[] { group });

    // execute the method to test.
    this.osCacheFacade
        .onPutInCache(CACHE_KEY, this.cacheProfile, objectToStore);

    Object cachedObject = this.cacheAdministrator.getFromCache(CACHE_KEY);
    assertSame("<Cached object>", objectToStore, cachedObject);

    // if we flush the group used, we should not be able to get the cached
    // object.
    this.cacheAdministrator.flushGroup(group);

    try {
      this.cacheAdministrator.getFromCache(CACHE_KEY);
      fail("Expecting exception <" + NeedsRefreshException.class.getName()
          + ">");
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
    setUpCacheAdministrator();

    Object objectToStore = "An Object";

    this.cacheProfile.setGroups((String[]) null);

    // execute the method to test.
    this.osCacheFacade
        .onPutInCache(CACHE_KEY, this.cacheProfile, objectToStore);

    Object cachedObject = this.cacheAdministrator.getFromCache(CACHE_KEY);
    assertSame("<Cached object>", objectToStore, cachedObject);

    // if we flush all the groups, we should be able to get the cached object.
    int groupCount = this.groups.length;
    for (int i = 0; i < groupCount; i++) {
      String group = this.groups[i];
      this.cacheAdministrator.flushGroup(group);
    }

    cachedObject = this.cacheAdministrator.getFromCache(CACHE_KEY);
    assertSame(objectToStore, cachedObject);
  }

  /**
   * Verifies that the method
   * <code>{@link OsCacheFacade#onRemoveFromCache(java.io.Serializable, org.springmodules.cache.provider.CacheProfile)}</code>
   * removes from the cache the entry stored under the given key.
   */
  public void testOnRemoveFromCache() throws Exception {
    Method flushEntryMethod = GeneralCacheAdministrator.class
        .getDeclaredMethod("flushEntry", new Class[] { String.class });

    setUpCacheAdministratorAsMockObject(flushEntryMethod);

    String key = "Luke";

    this.cacheAdministrator.flushEntry(key);
    this.cacheAdministratorControl.replay();

    // execute the method to test.
    this.osCacheFacade.onRemoveFromCache(key, null);

    this.cacheAdministratorControl.verify();
  }

  public void testValidateCacheManagerWithCacheManagerEqualToNull() {
    setUpCacheAdministrator();

    this.osCacheFacade.setCacheManager(null);
    try {
      this.osCacheFacade.validateCacheManager();
      fail("Expecting exception <"
          + InvalidConfigurationException.class.getName() + ">");
    } catch (InvalidConfigurationException exception) {
      // we are expecting this exception.
    }
  }

  /**
   * Verifies that the method
   * <code>{@link OsCacheFacade#validateCacheManager()}</code> does not throw
   * any exception if the cache manager is not <code>null</code>.
   */
  public void testValidateCacheManagerWithCacheManagerNotEqualToNull()
      throws Exception {
    setUpCacheAdministrator();

    this.osCacheFacade.validateCacheManager();
  }
}
