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

import java.beans.PropertyEditor;
import java.lang.reflect.Method;
import java.util.Map;

import junit.framework.TestCase;

import org.easymock.classextension.MockClassControl;
import org.springmodules.cache.FatalCacheException;
import org.springmodules.cache.provider.CacheModelValidator;
import org.springmodules.cache.provider.ReflectionCacheModelEditor;

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
 */
public class OsCacheFacadeTests extends TestCase {

  private static final String CACHE_KEY = "key";

  private GeneralCacheAdministrator cacheAdministrator;

  private MockClassControl cacheAdministratorControl;

  private CacheEntryEventListenerImpl cacheEntryEventListener;

  private OsCacheCachingModel cachingModel;

  private OsCacheFlushingModel flushingModel;

  /**
   * Name of the groups in <code>{@link #cacheAdministrator}</code> to use.
   */
  private String[] groups;

  private OsCacheFacade osCacheFacade;

  public OsCacheFacadeTests(String name) {
    super(name);
  }

  protected void setUp() {
    cachingModel = new OsCacheCachingModel();
    groups = new String[] { "Empire", "Rebels" };
    flushingModel = new OsCacheFlushingModel();

    osCacheFacade = new OsCacheFacade();
  }

  private void setUpCacheAdministrator() {
    cacheAdministrator = new GeneralCacheAdministrator();
    osCacheFacade.setCacheManager(cacheAdministrator);

    Cache cache = cacheAdministrator.getCache();

    cacheEntryEventListener = new CacheEntryEventListenerImpl();
    cache.addCacheEventListener(cacheEntryEventListener,
        CacheEntryEventListener.class);

    osCacheFacade.setCacheManager(cacheAdministrator);
  }

  private void setUpCacheAdministratorAsMockObject(Method methodToMock) {
    setUpCacheAdministratorAsMockObject(new Method[] { methodToMock });
  }

  private void setUpCacheAdministratorAsMockObject(Method[] methodsToMock) {
    Class targetClass = GeneralCacheAdministrator.class;

    cacheAdministratorControl = MockClassControl.createControl(targetClass,
        null, null, methodsToMock);
    cacheAdministrator = (GeneralCacheAdministrator) cacheAdministratorControl
        .getMock();

    osCacheFacade.setCacheManager(cacheAdministrator);
  }

  protected void tearDown() {
    if (cacheAdministrator != null) {
      cacheAdministrator.destroy();
    }
  }

  public void testGetCacheModelEditor() {
    PropertyEditor editor = osCacheFacade.getCachingModelEditor();

    assertNotNull(editor);
    assertEquals(ReflectionCacheModelEditor.class, editor.getClass());

    ReflectionCacheModelEditor modelEditor = (ReflectionCacheModelEditor) editor;
    assertEquals(OsCacheCachingModel.class, modelEditor.getCacheModelClass());

    Map cacheModelPropertyEditors = modelEditor.getCacheModelPropertyEditors();
    assertNotNull(cacheModelPropertyEditors);
    assertEquals(1, cacheModelPropertyEditors.size());

    PropertyEditor refreshPeriodEditor = (PropertyEditor) cacheModelPropertyEditors
        .get("refreshPeriod");
    assertNotNull(refreshPeriodEditor);
    assertEquals(RefreshPeriodEditor.class, refreshPeriodEditor.getClass());
  }

  /**
   * Verifies that the method
   * <code>{@link OsCacheFacade#getCacheModelValidator()}</code> returns an an
   * instance of <code>{@link OsCacheModelValidator}</code> not equal to
   * <code>null</code>.
   */
  public void testGetCacheModelValidator() {
    CacheModelValidator validator = osCacheFacade.getCacheModelValidator();

    assertNotNull(validator);

    assertEquals(OsCacheModelValidator.class, validator.getClass());
  }

  public void testIsSerializableCacheElementRequired() {
    assertFalse(osCacheFacade.isSerializableCacheElementRequired());
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

    cacheAdministrator.cancelUpdate(key);
    cacheAdministratorControl.replay();

    // execute the method to test.
    osCacheFacade.cancelCacheUpdate(key);

    cacheAdministratorControl.verify();
  }

  public void testOnFlushCache() {
    setUpCacheAdministrator();

    Object objectToStore = "An Object";
    cacheAdministrator.putInCache(CACHE_KEY, objectToStore, groups);

    String groupToFlush = groups[0];
    flushingModel.setGroups(new String[] { groupToFlush });

    // execute the method to test.
    osCacheFacade.onFlushCache(flushingModel);

    assertEquals("<Number of groups flushed>", 1, cacheEntryEventListener
        .getGroupFlushedCount());
  }

  public void testOnFlushCacheWithoutGroups() {
    setUpCacheAdministrator();

    Object objectToStore = "An Object";
    cacheAdministrator.putInCache(CACHE_KEY, objectToStore, groups);

    flushingModel.setGroups((String[]) null);

    // execute the method to test.
    osCacheFacade.onFlushCache(flushingModel);

    String cachedObject = cacheAdministrator.getProperty(CACHE_KEY);
    assertNull(cachedObject);
  }

  public void testOnGetFromCache() {
    setUpCacheAdministrator();

    Object expected = "An Object";
    cacheAdministrator.putInCache(CACHE_KEY, expected);

    // execute the method to test.
    Object actual = osCacheFacade.onGetFromCache(CACHE_KEY, cachingModel);

    assertSame(expected, actual);
  }

  public void testOnGetFromCacheWhenKeyIsNotFound() {
    setUpCacheAdministrator();

    Object cachedObject = osCacheFacade.onGetFromCache("NonExistingKey",
        cachingModel);

    assertNull(cachedObject);
  }

  public void testOnGetFromCacheWhenRefreshPeriodIsNotNullAndCronExpressionIsNotNull()
      throws Exception {
    Method getFromCacheMethod = GeneralCacheAdministrator.class
        .getDeclaredMethod("getFromCache", new Class[] { String.class,
            int.class, String.class });

    setUpCacheAdministratorAsMockObject(getFromCacheMethod);
    String cronExpression = "* * * 0 0";
    int refreshPeriod = 45;

    cachingModel.setCronExpression(cronExpression);
    cachingModel.setRefreshPeriod(refreshPeriod);
    Object expected = "Anakin";

    cacheAdministratorControl.expectAndReturn(cacheAdministrator.getFromCache(
        CACHE_KEY, refreshPeriod, cronExpression), expected);

    cacheAdministratorControl.replay();

    // execute the method to test.
    Object actual = osCacheFacade.onGetFromCache(CACHE_KEY, cachingModel);

    assertSame(expected, actual);

    cacheAdministratorControl.verify();
  }

  public void testOnGetFromCacheWhenRefreshPeriodIsNotNullAndCronExpressionIsNull()
      throws Exception {
    Method getFromCacheMethod = GeneralCacheAdministrator.class
        .getDeclaredMethod("getFromCache", new Class[] { String.class,
            int.class });

    setUpCacheAdministratorAsMockObject(getFromCacheMethod);
    int refreshPeriod = 556;

    cachingModel.setRefreshPeriod(refreshPeriod);
    Object expected = "Anakin";

    cacheAdministratorControl.expectAndReturn(cacheAdministrator.getFromCache(
        CACHE_KEY, refreshPeriod), expected);

    cacheAdministratorControl.replay();

    // execute the method to test.
    Object actual = osCacheFacade.onGetFromCache(CACHE_KEY, cachingModel);

    assertSame(expected, actual);

    cacheAdministratorControl.verify();
  }

  public void testOnGetFromCacheWhenRefreshPeriodIsNull() throws Exception {
    Method getFromCacheMethod = GeneralCacheAdministrator.class
        .getDeclaredMethod("getFromCache", new Class[] { String.class });

    setUpCacheAdministratorAsMockObject(getFromCacheMethod);

    cachingModel.setRefreshPeriod(null);
    Object expected = "Anakin";

    // retrieve an entry using only the provided key.
    cacheAdministratorControl.expectAndReturn(cacheAdministrator
        .getFromCache(CACHE_KEY), expected);

    cacheAdministratorControl.replay();

    // execute the method to test.
    Object actual = osCacheFacade.onGetFromCache(CACHE_KEY, cachingModel);

    assertSame(expected, actual);

    cacheAdministratorControl.verify();
  }

  public void testOnPutInCacheWithGroups() throws Exception {
    setUpCacheAdministrator();

    Object objectToStore = "An Object";

    String group = groups[0];
    cachingModel.setGroups(new String[] { group });

    // execute the method to test.
    osCacheFacade.onPutInCache(CACHE_KEY, cachingModel, objectToStore);

    Object cachedObject = cacheAdministrator.getFromCache(CACHE_KEY);
    assertSame(objectToStore, cachedObject);

    // if we flush the group used, we should not be able to get the cached
    // object.
    cacheAdministrator.flushGroup(group);

    try {
      cacheAdministrator.getFromCache(CACHE_KEY);
      fail();
    } catch (NeedsRefreshException exception) {
      // we are expecting this exception
    }
  }

  /**
   * Verifies that the method
   * <code>{@link OsCacheFacade#onPutInCache(java.io.Serializable, org.springmodules.cache.CachingModel, Object)}</code>
   * stores an entry using the given key. The entry should not be associated
   * with any group.
   */
  public void testOnPutInCacheWithoutGroups() throws Exception {
    setUpCacheAdministrator();

    Object objectToStore = "An Object";

    cachingModel.setGroups((String[]) null);

    // execute the method to test.
    osCacheFacade.onPutInCache(CACHE_KEY, cachingModel, objectToStore);

    Object cachedObject = cacheAdministrator.getFromCache(CACHE_KEY);
    assertSame(objectToStore, cachedObject);

    // if we flush all the groups, we should be able to get the cached object.
    int groupCount = groups.length;
    for (int i = 0; i < groupCount; i++) {
      String group = groups[i];
      cacheAdministrator.flushGroup(group);
    }

    cachedObject = cacheAdministrator.getFromCache(CACHE_KEY);
    assertSame(objectToStore, cachedObject);
  }

  /**
   * Verifies that the method
   * <code>{@link OsCacheFacade#onRemoveFromCache(java.io.Serializable, org.springmodules.cache.CachingModel)}</code>
   * removes from the cache the entry stored under the given key.
   */
  public void testOnRemoveFromCache() throws Exception {
    Method flushEntryMethod = GeneralCacheAdministrator.class
        .getDeclaredMethod("flushEntry", new Class[] { String.class });

    setUpCacheAdministratorAsMockObject(flushEntryMethod);

    String key = "Luke";

    cacheAdministrator.flushEntry(key);
    cacheAdministratorControl.replay();

    // execute the method to test.
    osCacheFacade.onRemoveFromCache(key, null);

    cacheAdministratorControl.verify();
  }

  public void testValidateCacheManagerWithCacheManagerEqualToNull() {
    osCacheFacade.setCacheManager(null);
    try {
      osCacheFacade.validateCacheManager();
      fail();
    } catch (FatalCacheException exception) {
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
    osCacheFacade.validateCacheManager();
  }
}
