/* 
 * Created on Nov 10, 2004
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
 * Copyright @2004 the original author or authors.
 */

package org.springmodules.cache.provider.oscache;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Properties;

import junit.framework.TestCase;

import org.easymock.classextension.MockClassControl;
import org.springmodules.cache.key.CacheKey;
import org.springmodules.cache.mock.FixedValueCacheKey;
import org.springmodules.cache.provider.AbstractCacheProfileEditor;
import org.springmodules.cache.provider.CacheProfile;
import org.springmodules.cache.provider.CacheProfileValidator;

import com.opensymphony.oscache.general.GeneralCacheAdministrator;

/**
 * <p>
 * Unit Test for <code>{@link OscacheFacade}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/04/22 02:19:25 $
 */
public final class OscacheFacadeTests extends TestCase {

  /**
   * Key used to store/retrieve an entry of the cache.
   */
  private CacheKey cacheKey;

  /**
   * Configuration options for the caching services.
   */
  private OscacheCacheProfile cacheProfile;

  /**
   * Mock object that simulates a OSCache cache manager.
   */
  private GeneralCacheAdministrator mockGeneralCacheAdministrator;

  /**
   * Controls the behavior of
   * <code>{@link #mockGeneralCacheAdministrator}</code>.
   */
  private MockClassControl mockGeneralCacheAdministratorControl;

  /**
   * Primary object (nstance of the class to test).
   */
  private OscacheFacade oscacheFacade;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the Test Case.
   */
  public OscacheFacadeTests(String name) {
    super(name);
  }

  /**
   * Set up the test fixture.
   */
  protected void setUp() throws Exception {
    super.setUp();

    this.oscacheFacade = new OscacheFacade();
  }

  /**
   * Sets up:
   * <ul>
   * <li>{@link #cacheKey}</li>
   * </ul>
   */
  private void setUpCacheKey() {
    this.cacheKey = new FixedValueCacheKey("KEY");
  }

  /**
   * Sets up:
   * <ul>
   * <li>{@link #cacheProfile}</li>
   * </ul>
   */
  private void setUpCacheProfile() {
    this.cacheProfile = new OscacheCacheProfile();
  }

  /**
   * Sets up:
   * <ul>
   * <li>{@link #mockGeneralCacheAdministrator}</li>
   * <li>{@link #mockGeneralCacheAdministratorControl}</li>
   * </ul>
   */
  private void setUpMockGeneralCacheAdministrator() throws Exception {
    Properties properties = new Properties();

    Class generalCacheAdministratorClass = GeneralCacheAdministrator.class;
    Method cancelUpdateMethod = generalCacheAdministratorClass.getMethod(
        "cancelUpdate", new Class[] { String.class });
    Method flushGroupMethod = generalCacheAdministratorClass.getMethod(
        "flushGroup", new Class[] { String.class });
    Method getFromCacheStringMethod = generalCacheAdministratorClass.getMethod(
        "getFromCache", new Class[] { String.class });
    Method getFromCacheStringIntMethod = generalCacheAdministratorClass
        .getMethod("getFromCache", new Class[] { String.class, int.class });
    Method getFromCacheStringIntStringMethod = generalCacheAdministratorClass
        .getMethod("getFromCache", new Class[] { String.class, int.class,
            String.class });
    Method putInCacheStringObjectMethod = generalCacheAdministratorClass
        .getMethod("putInCache", new Class[] { String.class, Object.class });
    Method putInCacheStringObjectStringArrayMethod = generalCacheAdministratorClass
        .getMethod("putInCache", new Class[] { String.class, Object.class,
            String[].class });

    Method[] methodsToMock = new Method[] { cancelUpdateMethod,
        flushGroupMethod, getFromCacheStringMethod,
        getFromCacheStringIntMethod, getFromCacheStringIntStringMethod,
        putInCacheStringObjectMethod, putInCacheStringObjectStringArrayMethod };

    this.mockGeneralCacheAdministratorControl = MockClassControl.createControl(
        generalCacheAdministratorClass, new Class[] { Properties.class },
        new Object[] { properties }, methodsToMock);

    this.mockGeneralCacheAdministrator = (GeneralCacheAdministrator) this.mockGeneralCacheAdministratorControl
        .getMock();
    this.oscacheFacade.setCacheManager(this.mockGeneralCacheAdministrator);
  }

  /**
   * Verifies that the method
   * <code>{@link OscacheFacade#getCacheProfileEditor()}</code> returns an
   * instance of <code>{@link OscacheCacheProfileEditor}</code> not equal to
   * <code>null</code>.
   */
  public void testGetCacheProfileEditor() {
    AbstractCacheProfileEditor cacheProfileEditor = this.oscacheFacade
        .getCacheProfileEditor();

    assertNotNull("The cache profile editor should not be null",
        cacheProfileEditor);

    Class expectedClass = OscacheCacheProfileEditor.class;
    Class actualClass = cacheProfileEditor.getClass();

    assertEquals("<Class of the cache profile editor>", expectedClass,
        actualClass);
  }

  /**
   * Verifies that the method
   * <code>{@link OscacheFacade#getCacheProfileValidator()}</code> returns an
   * an instance of <code>{@link OscacheCacheProfileValidator}</code> not
   * equal to <code>null</code>.
   */
  public void testGetCacheProfileValidator() {
    CacheProfileValidator cacheProfileValidator = this.oscacheFacade
        .getCacheProfileValidator();

    assertNotNull("The cache profile validator should not be null",
        cacheProfileValidator);

    Class expectedClass = OscacheCacheProfileValidator.class;
    Class actualClass = cacheProfileValidator.getClass();

    assertEquals("<Class of the cache profile validator>", expectedClass,
        actualClass);
  }

  /**
   * Tests <code>{@link OscacheFacade#onCancelCacheUpdate(Serializable)}.
   * Verifies that the cache manager cancels the update to the cache.
   */
  public void testOnCancelCacheUpdate() throws Exception {
    this.setUpCacheKey();
    this.setUpMockGeneralCacheAdministrator();

    // set the expectations of the mock objects.
    this.mockGeneralCacheAdministrator.cancelUpdate(this.cacheKey.toString());
    this.mockGeneralCacheAdministratorControl.replay();

    // execute the method to test.
    this.oscacheFacade.onCancelCacheUpdate(this.cacheKey);

    // verify the execution was successful.
    this.mockGeneralCacheAdministratorControl.verify();
  }

  /**
   * Verifies that the method
   * <code>{@link OscacheFacade#onFlushCache(CacheProfile)}</code> flushes the
   * groups specified in the cache profile.
   */
  public void testOnFlushCache() throws Exception {
    this.setUpCacheKey();
    this.setUpCacheProfile();
    this.setUpMockGeneralCacheAdministrator();

    // specify the groups to flush in the cache profile.
    String[] groups = new String[] { "test", "dev" };
    this.cacheProfile.setGroups(groups);

    // expectation: flush each of the groups specified in the cache profile.
    int groupCount = groups.length;
    for (int i = 0; i < groupCount; i++) {
      String group = groups[i];
      this.mockGeneralCacheAdministrator.flushGroup(group);
    }

    // set the state of the mock control to 'replay'.
    this.mockGeneralCacheAdministratorControl.replay();

    // execute the method to test.
    this.oscacheFacade.onFlushCache(this.cacheProfile);

    // verify that the expectations of the mock control were met.
    this.mockGeneralCacheAdministratorControl.verify();
  }

  /**
   * Verifies that the method
   * <code>{@link OscacheFacade#onFlushCache(CacheProfile)}</code> does not
   * flush any group if the given cache profile does specify any group to be
   * flushed.
   */
  public void testOnFlushCacheWithNoGroups() throws Exception {
    this.setUpCacheKey();
    this.setUpCacheProfile();
    this.setUpMockGeneralCacheAdministrator();

    String[] groups = null;
    this.cacheProfile.setGroups(groups);

    // set the state of the mock control to 'replay'.
    this.mockGeneralCacheAdministratorControl.replay();

    // execute the method to test.
    this.oscacheFacade.onFlushCache(this.cacheProfile);

    // verify that the expectations of the mock control were met.
    this.mockGeneralCacheAdministratorControl.verify();
  }

  /**
   * Verifies that the method
   * <code>{@link OscacheFacade#onGetFromCache(Serializable, CacheProfile)}</code>
   * retrieves an object from the cache using the given key and refresh period.
   */
  public void testOnGetFromCacheWithKeyAndRefreshPeriod() throws Throwable {
    this.setUpCacheKey();
    this.setUpCacheProfile();
    this.setUpMockGeneralCacheAdministrator();

    int refreshPeriod = 10;
    this.cacheProfile.setRefreshPeriod(refreshPeriod);

    Object cachedObject = "A String :)";

    // expectation: get an object from the cache using the given key and refresh
    // period.
    this.mockGeneralCacheAdministrator.getFromCache(this.cacheKey.toString(),
        refreshPeriod);
    this.mockGeneralCacheAdministratorControl.setReturnValue(cachedObject);

    // set the state of the mock control to 'replay'.
    this.mockGeneralCacheAdministratorControl.replay();

    // execute the method to test.
    Object returnedObject = this.oscacheFacade.onGetFromCache(this.cacheKey,
        this.cacheProfile);

    assertSame("<Cached object>", cachedObject, returnedObject);

    // verify that the expectations of the mock control were met.
    this.mockGeneralCacheAdministratorControl.verify();
  }

  /**
   * Verifies that the method
   * <code>{@link OscacheFacade#onGetFromCache(Serializable, CacheProfile)}</code>
   * retrieves an object from the cache using the given key, refresh period and
   * cron expression.
   */
  public void testOnGetFromCacheWithKeyAndRefreshPeriodAndCronExpression()
      throws Throwable {

    this.setUpCacheKey();
    this.setUpCacheProfile();
    this.setUpMockGeneralCacheAdministrator();

    int refreshPeriod = 10;
    String cronExpression = "0 0 0 0 0";
    this.cacheProfile.setCronExpression(cronExpression);
    this.cacheProfile.setRefreshPeriod(refreshPeriod);

    Object cachedObject = "A String :)";

    // expectation: get an object from the cache using the given key, refresh
    // period and cron expresion.
    this.mockGeneralCacheAdministrator.getFromCache(this.cacheKey.toString(),
        refreshPeriod, cronExpression);
    this.mockGeneralCacheAdministratorControl.setReturnValue(cachedObject);

    // set the state of the mock control to 'replay'.
    this.mockGeneralCacheAdministratorControl.replay();

    // execute the method to test.
    Object returnedObject = this.oscacheFacade.onGetFromCache(this.cacheKey,
        this.cacheProfile);

    assertSame("<Cached object>", cachedObject, returnedObject);

    // verify that the expectations of the mock control were met.
    this.mockGeneralCacheAdministratorControl.verify();
  }

  /**
   * Verifies that the method
   * <code>{@link OscacheFacade#onGetFromCache(Serializable, CacheProfile)}</code>
   * retrieves an object from the cache using the given key.
   */
  public void testOnGetFromCacheWithKeyOnly() throws Exception {
    this.setUpCacheKey();
    this.setUpCacheProfile();
    this.setUpMockGeneralCacheAdministrator();

    Object cachedObject = "A String :)";

    // expectation: get an object from the cache using the given key.
    this.mockGeneralCacheAdministrator.getFromCache(this.cacheKey.toString());
    this.mockGeneralCacheAdministratorControl.setReturnValue(cachedObject);

    // set the state of the mock control to 'replay'.
    this.mockGeneralCacheAdministratorControl.replay();

    // execute the method to test.
    Object returnedObject = this.oscacheFacade.onGetFromCache(this.cacheKey,
        this.cacheProfile);

    assertSame("<Cached object>", cachedObject, returnedObject);

    // verify that the expectations of the mock control were met.
    this.mockGeneralCacheAdministratorControl.verify();
  }

  /**
   * Verifies that the method
   * <code>{@link OscacheFacade#onPutInCache(Serializable, CacheProfile, Object)}</code>
   * stores the given object in the specified groups.
   */
  public void testOnPutInCacheWithGroups() throws Throwable {
    this.setUpCacheKey();
    this.setUpCacheProfile();
    this.setUpMockGeneralCacheAdministrator();

    Object objectToCache = "A String :)";
    String[] groups = new String[] { "test", "dev" };
    this.cacheProfile.setGroups(groups);

    // expectation: store the object in specified groups of the cache.
    this.mockGeneralCacheAdministrator.putInCache(this.cacheKey.toString(),
        objectToCache, groups);

    // set the state of the mock control to 'replay'.
    this.mockGeneralCacheAdministratorControl.replay();

    // execute the method to test.
    this.oscacheFacade.onPutInCache(this.cacheKey, this.cacheProfile,
        objectToCache);

    // verify that the expectations of the mock control were met.
    this.mockGeneralCacheAdministratorControl.verify();
  }

  /**
   * Verifies that the method
   * <code>{@link OscacheFacade#onPutInCache(Serializable, CacheProfile, Object)}</code>
   * stores the given object in cache.
   */
  public void testOnPutInCacheWithoutGroups() throws Throwable {
    this.setUpCacheKey();
    this.setUpCacheProfile();
    this.setUpMockGeneralCacheAdministrator();

    Object objectToCache = "A String :)";
    String[] groups = null;
    this.cacheProfile.setGroups(groups);

    // expectation: store the object in the cache.
    this.mockGeneralCacheAdministrator.putInCache(this.cacheKey.toString(),
        objectToCache);

    // set the state of the mock control to 'replay'.
    this.mockGeneralCacheAdministratorControl.replay();

    // execute the method to test.
    this.oscacheFacade.onPutInCache(this.cacheKey, this.cacheProfile,
        objectToCache);

    // verify that the expectations of the mock control were met.
    this.mockGeneralCacheAdministratorControl.verify();
  }

  /**
   * Verifies that the method
   * <code>{@link OscacheFacade#removeFromCache(Serializable, String)}</code>
   * removes from the cache the object stored under the given key.
   */
  public void testRemoveFromCache() throws Throwable {
    this.setUpCacheKey();
    this.setUpMockGeneralCacheAdministrator();

    // expectation: remove the object stored under the given key.
    this.mockGeneralCacheAdministrator.flushEntry(this.cacheKey.toString());

    // set the state of the mock control to 'replay'.
    this.mockGeneralCacheAdministratorControl.replay();

    // execute the method to test.
    this.oscacheFacade.removeFromCache(this.cacheKey, null);

    // verify that the expectations of the mock control were met.
    this.mockGeneralCacheAdministratorControl.verify();
  }

  /**
   * Verifies that the method
   * <code>{@link OscacheFacade#validateCacheManager()}</code> throws an
   * <code>IllegalStateException</code> if the cache manager is
   * <code>null</code>.
   */
  public void testValidateCacheManagerWithCacheManagerEqualToNull() {
    try {
      this.oscacheFacade.validateCacheManager();
      fail("An 'IllegalStateException' should have been thrown");
    } catch (IllegalStateException exception) {
      // we are expecting this exception.
    }
  }

  /**
   * Verifies that the method
   * <code>{@link OscacheFacade#validateCacheManager()}</code> does not throw
   * any exception if the cache manager is not <code>null</code>.
   */
  public void testValidateCacheManagerWithCacheManagerNotEqualToNull()
      throws Exception {
    this.setUpMockGeneralCacheAdministrator();
    this.oscacheFacade.validateCacheManager();
  }
}