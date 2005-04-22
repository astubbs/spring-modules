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

package org.springmodules.cache.provider;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import junit.framework.TestCase;

import org.easymock.MockControl;
import org.easymock.classextension.MockClassControl;
import org.springmodules.cache.key.CacheKey;
import org.springmodules.cache.mock.FixedValueCacheKey;
import org.springmodules.cache.mock.MockCacheProfile;

/**
 * <p>
 * Unit Test for <code>{@link AbstractCacheProviderFacadeImpl}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/04/22 02:19:12 $
 */
public final class AbstractCacheProviderFacadeImplTests extends TestCase {

  /**
   * Id of the cache profile to be retrieved from
   * <code>{@link #mockCacheProfileMap}</code>.
   */
  private static final String CACHE_PROFILE_ID = "CACHE_PROFILE_ID";

  /**
   * Key of the cached object to be retrieved from
   * <code>{@link #cacheProviderFacade}</code>.
   */
  private CacheKey cacheKey;

  /**
   * Cache profile to be stored in <code>{@link #mockCacheProfileMap}</code>
   * using the key <code>{@link #CACHE_PROFILE_ID}</code>.
   */
  private CacheProfile cacheProfile;

  /**
   * Primary object (instance of the class to test).
   */
  private AbstractCacheProviderFacadeImpl cacheProviderFacade;

  /**
   * Mocks the abstract methods and controls the behavior of
   * <code>{@link #cacheProviderFacade}</code>.
   */
  private MockClassControl cacheProviderFacadeControl;

  /**
   * Mock object that simulates a property editor of cache profiles.
   */
  private AbstractCacheProfileEditor mockCacheProfileEditor;

  /**
   * Controls the behavior of <code>{@link #mockCacheProfileEditor}</code>.
   */
  private MockClassControl mockCacheProfileEditorControl;

  /**
   * Mock object that simulates a map of cache profiles stored in
   * <code>{@link #cacheProviderFacade}</code>.
   */
  private Map mockCacheProfileMap;

  /**
   * Controls the behavior of <code>{@link #mockCacheProfileMap}</code>.
   */
  private MockControl mockCacheProfileMapControl;

  /**
   * Mock object that simulates the iterator for the keys stored in
   * <code>{@link #mockCacheProfileMapKeySet}</code>.
   */
  private Iterator mockCacheProfileMapKeyIterator;

  /**
   * Controls the behavior of
   * <code>{@link #mockCacheProfileMapKeyIterator}</code>.
   */
  private MockControl mockCacheProfileMapKeyIteratorControl;

  /**
   * Mock object that simulates the set of keys stored in
   * <code>{@link #mockCacheProfileMap}</code>.
   */
  private Set mockCacheProfileMapKeySet;

  /**
   * Controls the behavior of <code>{@link #mockCacheProfileMapKeySet}</code>.
   */
  private MockControl mockCacheProfileMapKeySetControl;

  /**
   * Mock object that simulates a property validator of cache profiles.
   */
  private CacheProfileValidator mockCacheProfileValidator;

  /**
   * Controls the behavior of <code>{@link #mockCacheProfileValidator}.</code>
   */
  private MockControl mockCacheProfileValidatorControl;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the Test Case.
   */
  public AbstractCacheProviderFacadeImplTests(String name) {
    super(name);
  }

  /**
   * Sets up the test fixture.
   */
  protected void setUp() throws Exception {
    super.setUp();

    this.setUpCacheProviderFacade();
    this.setUpMockCacheProfileEditor();
    this.setUpMockCacheProfileMap();

    this.cacheProviderFacade.setCacheProfiles(this.mockCacheProfileMap);

    this.cacheKey = new FixedValueCacheKey("KEY");

    this.cacheProfile = new MockCacheProfile();
  }

  /**
   * Sets up:
   * <ul>
   * <li><code>{@link #cacheProviderFacade}</code></li>
   * <li><code>{@link #cacheProviderFacadeControl}</code></li>
   * </ul>
   */
  private void setUpCacheProviderFacade() throws Exception {
    // set up the class to mock
    Class classToMock = AbstractCacheProviderFacadeImpl.class;

    // set up the abstract methods to mock
    Method getCacheProfileEditorMethod = classToMock.getDeclaredMethod(
        "getCacheProfileEditor", null);

    Method getCacheProfileValidatorMethod = classToMock.getDeclaredMethod(
        "getCacheProfileValidator", null);

    Method onCancelCacheUpdateMethod = classToMock.getDeclaredMethod(
        "onCancelCacheUpdate", new Class[] { Serializable.class });

    Method onFlushCacheMethod = classToMock.getDeclaredMethod("onFlushCache",
        new Class[] { CacheProfile.class });

    Method onGetFromCacheMethod = classToMock.getDeclaredMethod(
        "onGetFromCache",
        new Class[] { Serializable.class, CacheProfile.class });

    Method onPutInCacheMethod = classToMock.getDeclaredMethod("onPutInCache",
        new Class[] { Serializable.class, CacheProfile.class, Object.class });

    Method validateCacheManagerMethod = classToMock.getDeclaredMethod(
        "validateCacheManager", null);

    Method[] methodsToMock = new Method[] { getCacheProfileEditorMethod,
        getCacheProfileValidatorMethod, onCancelCacheUpdateMethod,
        onFlushCacheMethod, onGetFromCacheMethod, onPutInCacheMethod,
        validateCacheManagerMethod };

    // set up the cache control.
    this.cacheProviderFacadeControl = MockClassControl.createControl(
        classToMock, null, null, methodsToMock);

    // set up the mock object.
    this.cacheProviderFacade = (AbstractCacheProviderFacadeImpl) this.cacheProviderFacadeControl
        .getMock();
  }

  /**
   * Sets up:
   * <ul>
   * <li><code>{@link #mockCacheProfileEditor}</code></li>
   * <li><code>{@link #mockCacheProfileEditorControl}</code></li>
   * </ul>
   */
  private void setUpMockCacheProfileEditor() throws Exception {
    // set up the class to mock.
    Class classToMock = AbstractCacheProfileEditor.class;

    // set up the abstracts methods to mock.
    Method createCacheProfileMethod = classToMock.getDeclaredMethod(
        "createCacheProfile", new Class[] { Properties.class });

    Method[] methodsToMock = new Method[] { createCacheProfileMethod };

    // set up the cache control.
    this.mockCacheProfileEditorControl = MockClassControl.createControl(
        classToMock, null, null, methodsToMock);

    // set up the mock object.
    this.mockCacheProfileEditor = (AbstractCacheProfileEditor) this.mockCacheProfileEditorControl
        .getMock();
  }

  /**
   * Sets up:
   * <ul>
   * <li><code>{@link #mockCacheProfileMap}</code></li>
   * <li><code>{@link #mockCacheProfileMapControl}</code></li>
   * </ul>
   */
  private void setUpMockCacheProfileMap() {
    this.mockCacheProfileMapControl = MockControl.createControl(Map.class);
    this.mockCacheProfileMap = (Map) this.mockCacheProfileMapControl.getMock();
  }

  /**
   * Sets up:
   * <ul>
   * <li>{@link #mockCacheProfileMapKeyIterator}</li>
   * <li>{@link #mockCacheProfileMapKeyIteratorControl}</li>
   * <li>{@link #mockCacheProfileMapKeySet}</li>
   * <li>{@link #mockCacheProfileMapKeySetControl}</li>
   * </ul>
   */
  private void setUpMockCacheProfileMapKeys() {

    this.mockCacheProfileMapKeySetControl = MockControl
        .createControl(Set.class);
    this.mockCacheProfileMapKeySet = (Set) this.mockCacheProfileMapKeySetControl
        .getMock();

    this.mockCacheProfileMapKeyIteratorControl = MockControl
        .createControl(Iterator.class);
    this.mockCacheProfileMapKeyIterator = (Iterator) this.mockCacheProfileMapKeyIteratorControl
        .getMock();
  }

  /**
   * Sets up <code>{@link #mockCacheProfileValidator}</code> and
   * <code>{@link #mockCacheProfileValidatorControl}</code>.
   */
  private void setUpMockCacheProfileValidator() {

    this.mockCacheProfileValidatorControl = MockControl
        .createControl(CacheProfileValidator.class);

    this.mockCacheProfileValidator = (CacheProfileValidator) this.mockCacheProfileValidatorControl
        .getMock();
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractCacheProviderFacadeImpl#afterPropertiesSet()}</code>
   * validates the cache manager only since the cache profiles has been already
   * validated by the method
   * <code>{@link AbstractCacheProviderFacadeImpl#setCacheProfiles(Properties)}</code>.
   */
  public void testAfterPropertiesSetWhenCacheProfilesHaveBeenValidated() {

    Properties unparsedProperties = new Properties();
    unparsedProperties.setProperty("key", "[firstName=Yvonne]");

    Properties parsedProperties = new Properties();
    parsedProperties.setProperty("firstName", "Yvonne");

    // expectation: get a editor of cache profiles.
    this.cacheProviderFacade.getCacheProfileEditor();
    this.cacheProviderFacadeControl.setReturnValue(this.mockCacheProfileEditor);

    // expectation: create a cache profile from parsed properties.
    this.mockCacheProfileEditor.createCacheProfile(parsedProperties);
    this.mockCacheProfileEditorControl.setReturnValue(this.cacheProfile);

    // set the state of the mock controls to "replay".
    this.mockCacheProfileEditorControl.replay();
    this.cacheProviderFacadeControl.replay();

    // set the cache profiles from a java.util.Properties.
    this.cacheProviderFacade.setCacheProfiles(unparsedProperties);

    // verify that the expectations of the mock controls were met.
    this.mockCacheProfileEditorControl.verify();
    this.cacheProviderFacadeControl.verify();

    // reset the control(s) for execution of the method to test.
    this.cacheProviderFacadeControl.reset();

    // expectation: validate the cache manager.
    this.cacheProviderFacade.validateCacheManager();

    // set the control(s) to "replay" state.
    this.cacheProviderFacadeControl.replay();

    // execute the method to test.
    this.cacheProviderFacade.afterPropertiesSet();

    // verify that the expectations were met.
    this.cacheProviderFacadeControl.verify();
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractCacheProviderFacadeImpl#afterPropertiesSet()}</code>.
   * validates both the cache manager and the cache profiles.
   */
  public void testAfterPropertiesSetWhenCacheProfilesHaveNotBeenValidated() {

    this.setUpMockCacheProfileValidator();
    this.setUpMockCacheProfileMapKeys();

    // expectation: validate cache manager.
    this.cacheProviderFacade.validateCacheManager();

    // expectation: verify that the map of cache profiles is not null or empty.
    // in this case, we want the map NOT to be null.
    this.mockCacheProfileMap.isEmpty();
    this.mockCacheProfileMapControl.setReturnValue(false);

    // expectation: get a validator for cache profiles.
    this.cacheProviderFacade.getCacheProfileValidator();
    this.cacheProviderFacadeControl
        .setReturnValue(this.mockCacheProfileValidator);

    // expectation: retrieve the set of keys of the map of cache profiles.
    this.mockCacheProfileMap.keySet();
    this.mockCacheProfileMapControl
        .setReturnValue(this.mockCacheProfileMapKeySet);

    // expectation: retrieve the iterator from the set of keys.
    this.mockCacheProfileMapKeySet.iterator();
    this.mockCacheProfileMapKeySetControl
        .setReturnValue(this.mockCacheProfileMapKeyIterator);

    // expectation: iterate through the key of each element the map of cache
    // profiles.
    String key = "KEY";
    this.mockCacheProfileMapKeyIterator.hasNext();
    this.mockCacheProfileMapKeyIteratorControl.setReturnValue(true);
    this.mockCacheProfileMapKeyIterator.next();
    this.mockCacheProfileMapKeyIteratorControl.setReturnValue(key);

    // expectation: retrieve a cache profile using the key.
    this.mockCacheProfileMap.get(key);
    this.mockCacheProfileMapControl.setReturnValue(this.cacheProfile);

    // expectation: validate the cache profile. We want the cache profile to be
    // valid.
    this.mockCacheProfileValidator.validateCacheProfile(this.cacheProfile);

    // expectation: we want the iterator to have only one element.
    this.mockCacheProfileMapKeyIterator.hasNext();
    this.mockCacheProfileMapKeyIteratorControl.setReturnValue(false);

    // set the state of the mock controls to "replay".
    this.cacheProviderFacadeControl.replay();
    this.mockCacheProfileMapControl.replay();
    this.mockCacheProfileMapKeyIteratorControl.replay();
    this.mockCacheProfileMapKeySetControl.replay();
    this.mockCacheProfileValidatorControl.replay();

    // execute the method to test.
    this.cacheProviderFacade.afterPropertiesSet();

    // verify that the expectations of the mock controls were met.
    this.cacheProviderFacadeControl.verify();
    this.mockCacheProfileMapControl.verify();
    this.mockCacheProfileMapKeyIteratorControl.verify();
    this.mockCacheProfileMapKeySetControl.verify();
    this.mockCacheProfileValidatorControl.verify();
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractCacheProviderFacadeImpl#afterPropertiesSet()}</code>.
   * throws a <code>IllegalStateException</code> if the map of cache profiles
   * is <code>null</code> or empty.
   */
  public void testAfterPropertiesSetWhenMapOfCacheProfilesIsEmpty() {
    this.cacheProviderFacade.setCacheProfiles((Map) null);
    try {
      this.cacheProviderFacade.afterPropertiesSet();
      fail("An IllegalStateException should have been thrown");
    } catch (IllegalStateException exception) {
      // we are expecting this exception to be thrown.
    }
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractCacheProviderFacadeImpl#afterPropertiesSet()}</code>.
   * throws a <code>IllegalStateException</code> wrapping the exception thrown
   * by the method
   * <code>{@link CacheProfileValidator#validateCacheProfile(Object)}</code>.
   */
  public void testAfterPropertiesSetWhenValidationOfCacheProfilesThrowsException() {
    this.setUpMockCacheProfileValidator();
    this.setUpMockCacheProfileMapKeys();

    // expectation: validate cache manager.
    this.cacheProviderFacade.validateCacheManager();

    // expectation: verify that the map of cache profiles is not null or empty.
    // in this case, we want the map NOT to be null.
    this.mockCacheProfileMap.isEmpty();
    this.mockCacheProfileMapControl.setReturnValue(false);

    // expectation: get a validator for cache profiles.
    this.cacheProviderFacade.getCacheProfileValidator();
    this.cacheProviderFacadeControl
        .setReturnValue(this.mockCacheProfileValidator);

    // expectation: retrieve the set of keys of the map of cache profiles.
    this.mockCacheProfileMap.keySet();
    this.mockCacheProfileMapControl
        .setReturnValue(this.mockCacheProfileMapKeySet);

    // expectation: retrieve the iterator from the set of keys.
    this.mockCacheProfileMapKeySet.iterator();
    this.mockCacheProfileMapKeySetControl
        .setReturnValue(this.mockCacheProfileMapKeyIterator);

    // expectation: iterate through the key of each element the map of cache
    // profiles.
    String key = "KEY";
    this.mockCacheProfileMapKeyIterator.hasNext();
    this.mockCacheProfileMapKeyIteratorControl.setReturnValue(true);
    this.mockCacheProfileMapKeyIterator.next();
    this.mockCacheProfileMapKeyIteratorControl.setReturnValue(key);

    // expectation: retrieve a cache profile using the key.
    this.mockCacheProfileMap.get(key);
    this.mockCacheProfileMapControl.setReturnValue(this.cacheProfile);

    // expectation: validate the cache profile. We want the cache profile to be
    // valid.
    this.mockCacheProfileValidator.validateCacheProfile(this.cacheProfile);

    // expectation: throw an exception because the cache profile is not valid.
    this.mockCacheProfileValidatorControl
        .setThrowable(new IllegalArgumentException());

    // set the state of the mock controls to "replay".
    this.cacheProviderFacadeControl.replay();
    this.mockCacheProfileMapControl.replay();
    this.mockCacheProfileMapKeyIteratorControl.replay();
    this.mockCacheProfileMapKeySetControl.replay();
    this.mockCacheProfileValidatorControl.replay();

    // execute the method to test.
    try {
      this.cacheProviderFacade.afterPropertiesSet();
      fail("An IllegalStateException should have been thrown");
    } catch (IllegalStateException exception) {
      // we are expecting this exception to be thrown.
    }

    // verify that the expectations of the mock controls were met.
    this.cacheProviderFacadeControl.verify();
    this.mockCacheProfileMapControl.verify();
    this.mockCacheProfileMapKeyIteratorControl.verify();
    this.mockCacheProfileMapKeySetControl.verify();
    this.mockCacheProfileValidatorControl.verify();
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractCacheProviderFacadeImpl#cancelCacheUpdate(Serializable)}</code>.
   * executes
   * <code>{@link AbstractCacheProviderFacadeImpl#onCancelCacheUpdate(Serializable)}</code>
   * when the update of the cache is cancelled.
   */
  public void testCancelCacheUpdate() {

    // expectation: call 'onCancelCacheUpdate(CacheKey)'.
    this.cacheProviderFacade.onCancelCacheUpdate(this.cacheKey);

    // set the controls to "replay" state.
    this.cacheProviderFacadeControl.replay();

    // execute the method to test.
    this.cacheProviderFacade.cancelCacheUpdate(this.cacheKey);

    // verify the expectations were met.
    this.cacheProviderFacadeControl.verify();
  }

  /**
   * Tests
   * <code>{@link AbstractCacheProviderFacadeImpl#cancelCacheUpdate(Serializable)}</code>.
   * Verifies that no exception is thrown if
   * <code>{@link AbstractCacheProviderFacadeImpl#onCancelCacheUpdate(Serializable)}</code>
   * throws an exception and
   * <code>{@link AbstractCacheProviderFacadeImpl#isFailQuietlyEnabled()}</code>
   * is <code>true</code>.
   */
  public void testCancelCacheUpdateWhenCacheAccessThrowsExceptionAndFailQuietlyIsEnabled() {

    this.cacheProviderFacade.setFailQuietlyEnabled(true);

    // expectation: call 'onCancelCacheUpdate(CacheKey)'. Access to the cache
    // should throw an exception.
    this.cacheProviderFacade.onCancelCacheUpdate(this.cacheKey);
    this.cacheProviderFacadeControl.setThrowable(new RuntimeException());

    // set the controls to "replay" state.
    this.cacheProviderFacadeControl.replay();

    // execute the method to test. No exception should be rethrown since "fail
    // quietly" is enabled.
    this.cacheProviderFacade.cancelCacheUpdate(this.cacheKey);

    // verify the expectations were met.
    this.cacheProviderFacadeControl.verify();
  }

  /**
   * Tests
   * <code>{@link AbstractCacheProviderFacadeImpl#cancelCacheUpdate(Serializable)}</code>.
   * Verifies that the exception thrown by
   * <code>{@link AbstractCacheProviderFacadeImpl#onCancelCacheUpdate(Serializable)}</code>
   * is rethrown if
   * <code>{@link AbstractCacheProviderFacadeImpl#isFailQuietlyEnabled()}</code>
   * is <code>false</code>.
   */
  public void testCancelCacheUpdateWhenCacheAccessThrowsExceptionAndFailQuietlyIsNotEnabled() {

    this.cacheProviderFacade.setFailQuietlyEnabled(false);

    // expectation: call 'onCancelCacheUpdate(CacheKey)'. Access to the cache
    // should throw an exception.
    this.cacheProviderFacade.onCancelCacheUpdate(this.cacheKey);
    RuntimeException exception = new RuntimeException();
    this.cacheProviderFacadeControl.setThrowable(exception);

    // set the controls to "replay" state.
    this.mockCacheProfileMapControl.replay();
    this.cacheProviderFacadeControl.replay();

    // execute the method to test. An exception should be rethrown since "fail
    // quietly" is not enabled.
    RuntimeException catched = null;
    try {
      this.cacheProviderFacade.cancelCacheUpdate(this.cacheKey);
    } catch (RuntimeException runtimeException) {
      catched = runtimeException;
    }

    // verify the expectations were met.
    this.mockCacheProfileMapControl.verify();
    this.cacheProviderFacadeControl.verify();

    assertSame("<Catched exception>", exception, catched);
  }

  /**
   * Tests
   * <code>{@link AbstractCacheProviderFacadeImpl#flushCache(String[])}</code>.
   * Verifies that no exception is thrown if
   * <code>{@link AbstractCacheProviderFacadeImpl#onFlushCache(CacheProfile)}</code>
   * throws an exception and
   * <code>{@link AbstractCacheProviderFacadeImpl#isFailQuietlyEnabled()}</code>
   * is <code>true</code>.
   */
  public void testFlushCacheWhenAccessToCacheThrowsExceptionAndFailQuietlyIsEnabled() {

    String[] cacheProfileIds = new String[] { CACHE_PROFILE_ID };
    this.cacheProviderFacade.setFailQuietlyEnabled(true);

    // expectation: retrieve a cache profile using the id(s) specified in the
    // array of ids.
    this.mockCacheProfileMap.get(CACHE_PROFILE_ID);
    this.mockCacheProfileMapControl.setReturnValue(this.cacheProfile);

    // expectation: call 'onFlushCache(CacheProfile)' sending the retrieved
    // cache profile as argument. Access to the cache should throw an exception.
    this.cacheProviderFacade.onFlushCache(this.cacheProfile);
    this.cacheProviderFacadeControl.setThrowable(new RuntimeException());

    // set the controls to "replay" state.
    this.mockCacheProfileMapControl.replay();
    this.cacheProviderFacadeControl.replay();

    // execute the method to test. No exception should be rethrown since "fail
    // quietly" is enabled.
    this.cacheProviderFacade.flushCache(cacheProfileIds);

    // verify the expectations were met.
    this.mockCacheProfileMapControl.verify();
    this.cacheProviderFacadeControl.verify();
  }

  /**
   * Tests
   * <code>{@link AbstractCacheProviderFacadeImpl#flushCache(String[])}</code>.
   * Verifies that the exception thrown by
   * <code>{@link AbstractCacheProviderFacadeImpl#onFlushCache(CacheProfile)}</code>
   * is rethrown if
   * <code>{@link AbstractCacheProviderFacadeImpl#isFailQuietlyEnabled()}</code>
   * is <code>false</code>.
   */
  public void testFlushCacheWhenAccessToCacheThrowsExceptionAndFailQuietlyIsNotEnabled() {

    String[] cacheProfileIds = new String[] { CACHE_PROFILE_ID };
    this.cacheProviderFacade.setFailQuietlyEnabled(false);

    // expectation: retrieve a cache profile using the id(s) specified in the
    // array of ids.
    this.mockCacheProfileMap.get(CACHE_PROFILE_ID);
    this.mockCacheProfileMapControl.setReturnValue(this.cacheProfile);

    // expectation: call 'onFlushCache(CacheProfile)' sending the retrieved
    // cache profile as argument. Access to the cache should throw an exception.
    this.cacheProviderFacade.onFlushCache(this.cacheProfile);
    RuntimeException exception = new RuntimeException();
    this.cacheProviderFacadeControl.setThrowable(exception);

    // set the controls to "replay" state.
    this.mockCacheProfileMapControl.replay();
    this.cacheProviderFacadeControl.replay();

    // execute the method to test. An exception should be rethrown since "fail
    // quietly" is not enabled.
    RuntimeException catched = null;
    try {
      this.cacheProviderFacade.flushCache(cacheProfileIds);
    } catch (RuntimeException runtimeException) {
      catched = runtimeException;
    }

    // verify the expectations were met.
    this.mockCacheProfileMapControl.verify();
    this.cacheProviderFacadeControl.verify();

    assertSame("<Catched exception>", exception, catched);
  }

  /**
   * Tests
   * <code>{@link AbstractCacheProviderFacadeImpl#flushCache(String[])}</code>.
   * Verifies that the method
   * <code>{@link AbstractCacheProviderFacadeImpl#onFlushCache(CacheProfile)}</code>
   * is not executed when a profile id is not specified.
   * 
   * @see #testFlushCacheWithEmptyArrayOfProfileIds()
   */
  public void testFlushCacheWithArrayOfProfileIdsEqualToNull() {

    // expectation: the map of cache profiles should not be accessed.
    this.mockCacheProfileMapControl.replay();

    String[] cacheProfileIds = null;
    this.cacheProviderFacade.flushCache(cacheProfileIds);

    // verify that the expectations were met.
    this.mockCacheProfileMapControl.verify();
  }

  /**
   * Tests
   * <code>{@link AbstractCacheProviderFacadeImpl#flushCache(String[])}</code>.
   * Verifies that the method
   * <code>{@link AbstractCacheProviderFacadeImpl#onFlushCache(CacheProfile)}</code>
   * is not executed when a profile id is not specified.
   * 
   * @see #testFlushCacheWithArrayOfProfileIdsEqualToNull()
   */
  public void testFlushCacheWithEmptyArrayOfProfileIds() {

    // expectation: the map of cache profiles should not be accessed.
    this.mockCacheProfileMapControl.replay();

    String[] cacheProfileIds = new String[0];
    this.cacheProviderFacade.flushCache(cacheProfileIds);

    // verify that the expectations were met.
    this.mockCacheProfileMapControl.verify();
  }

  /**
   * Tests
   * <code>{@link AbstractCacheProviderFacadeImpl#flushCache(String[])}</code>.
   * Verifies that the method
   * <code>{@link AbstractCacheProviderFacadeImpl#onFlushCache(CacheProfile)}</code>
   * is executed when the cache provider contains a profile with the specified
   * id.
   */
  public void testFlushCacheWithNotEmptyArrayOfProfileIdsAndExistingProfileId() {

    String[] cacheProfileIds = new String[] { CACHE_PROFILE_ID };

    // expectation: retrieve a cache profile using the id(s) specified in the
    // array of ids.
    this.mockCacheProfileMap.get(CACHE_PROFILE_ID);
    this.mockCacheProfileMapControl.setReturnValue(this.cacheProfile);

    // expectation: call 'onFlushCache(CacheProfile)' sending the retrieved
    // cache profile as argument.
    this.cacheProviderFacade.onFlushCache(this.cacheProfile);

    // set the controls to "replay" state.
    this.mockCacheProfileMapControl.replay();
    this.cacheProviderFacadeControl.replay();

    // execute the method to test.
    this.cacheProviderFacade.flushCache(cacheProfileIds);

    // verify the expectations were met.
    this.mockCacheProfileMapControl.verify();
    this.cacheProviderFacadeControl.verify();
  }

  /**
   * Tests
   * <code>{@link AbstractCacheProviderFacadeImpl#flushCache(String[])}</code>.
   * Verifies that the method
   * <code>{@link AbstractCacheProviderFacadeImpl#onFlushCache(CacheProfile)}</code>
   * is not executed when the cache provider does not contain a profile with the
   * specified id.
   */
  public void testFlushCacheWithNotEmptyArrayOfProfileIdsAndNotExistingProfileId() {

    String[] cacheProfileIds = new String[] { CACHE_PROFILE_ID };

    // expectation: retrieve a cache profile using the id(s) specified in the
    // array of ids. A cache profile with the given id should not exist.
    this.mockCacheProfileMap.get(CACHE_PROFILE_ID);
    this.mockCacheProfileMapControl.setReturnValue(null);

    // the method 'onFlushCache(CacheProfile)' should never be called.

    // set the controls to "replay" state.
    this.mockCacheProfileMapControl.replay();
    this.cacheProviderFacadeControl.replay();

    // execute the method to test.
    this.cacheProviderFacade.flushCache(cacheProfileIds);

    // verify the expectations were met.
    this.mockCacheProfileMapControl.verify();
    this.cacheProviderFacadeControl.verify();
  }

  /**
   * Tests
   * <code>{@link AbstractCacheProviderFacadeImpl#getCacheProfiles()}</code>.
   * Verifies that an unmodifiable map is returned.
   */
  public void testGetCacheProfilesReturnsAnUnmodifiableMap() {

    UnsupportedOperationException catched = null;
    Map unmodifiableMap = this.cacheProviderFacade.getCacheProfiles();

    try {
      unmodifiableMap.clear();
    } catch (UnsupportedOperationException exception) {
      catched = exception;
    }

    assertNotNull("The map of cache provider should be unmodifiable", catched);
  }

  /**
   * Tests
   * <code>{@link AbstractCacheProviderFacadeImpl#getCacheProfile(String)}</code>.
   * Verifies that a <code>{@link CacheProfile}</code> equal to
   * <code>null</code> is returned if the specified cache profile id is empty.
   */
  public void testGetCacheProfileWithEmptyProfileId() {

    // set the expectations of the mock objects.
    this.mockCacheProfileMapControl.replay();
    this.cacheProviderFacadeControl.replay();

    // execute the method to test.
    CacheProfile actualCacheProfile = this.cacheProviderFacade
        .getCacheProfile("");

    assertNull("The returned cache profile should be null", actualCacheProfile);

    // verify the expectations were met.
    this.mockCacheProfileMapControl.verify();
    this.cacheProviderFacadeControl.verify();
  }

  /**
   * Tests
   * <code>{@link AbstractCacheProviderFacadeImpl#getCacheProfile(String)}</code>.
   */
  public void testGetCacheProfileWithNotEmptyProfileId() {

    String cacheProfileId = "id";

    // set the expectations of the mock objects.
    this.mockCacheProfileMap.get(cacheProfileId);
    this.mockCacheProfileMapControl.setReturnValue(this.cacheProfile);
    this.mockCacheProfileMapControl.replay();

    this.cacheProviderFacadeControl.replay();

    // execute the method to test.
    CacheProfile actualCacheProfile = this.cacheProviderFacade
        .getCacheProfile(cacheProfileId);

    assertSame("<Cache Profile>", this.cacheProfile, actualCacheProfile);

    // verify the expectations were met.
    this.mockCacheProfileMapControl.verify();
    this.cacheProviderFacadeControl.verify();
  }

  /**
   * Tests
   * <code>{@link AbstractCacheProviderFacadeImpl#getCacheProfile(String)}</code>.
   * Verifies that a <code>{@link CacheProfile}</code> equal to
   * <code>null</code> is returned if the specified cache profile id is
   * <code>null</code>.
   */
  public void testGetCacheProfileWithProfileIdEqualToNull() {

    // set the expectations of the mock objects.
    this.mockCacheProfileMapControl.replay();
    this.cacheProviderFacadeControl.replay();

    // execute the method to test.
    CacheProfile actualCacheProfile = this.cacheProviderFacade
        .getCacheProfile(null);

    assertNull("The returned cache profile should be null", actualCacheProfile);

    // verify the expectations were met.
    this.mockCacheProfileMapControl.verify();
    this.cacheProviderFacadeControl.verify();
  }

  /**
   * Tests
   * <code>{@link AbstractCacheProviderFacadeImpl#getFromCache(Serializable, String)}</code>.
   * Verifies that no exception is thrown if
   * <code>{@link AbstractCacheProviderFacadeImpl#onGetFromCache(Serializable, CacheProfile)}</code>
   * throws an exception and
   * <code>{@link AbstractCacheProviderFacadeImpl#isFailQuietlyEnabled()}</code>
   * is <code>true</code>.
   */
  public void testGetFromCacheCacheKeyWhenAccessToCacheThrowsExceptionAndFailQuietlyIsEnabled()
      throws Exception {

    this.cacheProviderFacade.setFailQuietlyEnabled(true);

    // expectation: get a cache profile from the map.
    this.mockCacheProfileMap.get(CACHE_PROFILE_ID);
    this.mockCacheProfileMapControl.setReturnValue(this.cacheProfile);

    // expectation: call 'onGetFromCache(CacheKey, CacheProfile)' sending the
    // retrieved cache profile as argument. Access to the cache should throw an
    // exception.
    this.cacheProviderFacade.onGetFromCache(this.cacheKey, this.cacheProfile);
    this.cacheProviderFacadeControl.setThrowable(new RuntimeException());

    // set the controls to "replay" state.
    this.mockCacheProfileMapControl.replay();
    this.cacheProviderFacadeControl.replay();

    // execute the method to test.
    Object cachedObject = this.cacheProviderFacade.getFromCache(this.cacheKey,
        CACHE_PROFILE_ID);

    // verify the expectations were met.
    this.mockCacheProfileMapControl.verify();
    this.cacheProviderFacadeControl.verify();

    assertNull("<returned object>", cachedObject);
  }

  /**
   * Tests
   * <code>{@link AbstractCacheProviderFacadeImpl#getFromCache(Serializable, String)}</code>.
   * Verifies that the exception thrown by
   * <code>{@link AbstractCacheProviderFacadeImpl#onGetFromCache(Serializable, CacheProfile)}</code>
   * is rethrown if
   * <code>{@link AbstractCacheProviderFacadeImpl#isFailQuietlyEnabled()}</code>
   * is <code>false</code>.
   */
  public void testGetFromCacheCacheKeyWhenAccessToCacheThrowsExceptionAndFailQuietlyIsNotEnabled()
      throws Exception {

    this.cacheProviderFacade.setFailQuietlyEnabled(false);

    // expectation: get a cache profile from the map.
    this.mockCacheProfileMap.get(CACHE_PROFILE_ID);
    this.mockCacheProfileMapControl.setReturnValue(this.cacheProfile);

    // expectation: call 'onGetFromCache(CacheKey, CacheProfile)' sending the
    // retrieved cache profile as argument. Access to the cache should throw an
    // exception.
    this.cacheProviderFacade.onGetFromCache(this.cacheKey, this.cacheProfile);
    RuntimeException exception = new RuntimeException();
    this.cacheProviderFacadeControl.setThrowable(exception);

    // set the controls to "replay" state.
    this.mockCacheProfileMapControl.replay();
    this.cacheProviderFacadeControl.replay();

    // execute the method to test. An exception should be rethrown since "fail
    // quietly" is not enabled.
    RuntimeException catched = null;
    try {
      this.cacheProviderFacade.getFromCache(this.cacheKey, CACHE_PROFILE_ID);
    } catch (RuntimeException runtimeException) {
      catched = runtimeException;
    }

    // verify the expectations were met.
    this.mockCacheProfileMapControl.verify();
    this.cacheProviderFacadeControl.verify();

    assertSame("<Catched exception>", exception, catched);
  }

  /**
   * Tests
   * <code>{@link AbstractCacheProviderFacadeImpl#getFromCache(Serializable, String)}</code>.
   * Verifies that
   * <code>{@link AbstractCacheProviderFacadeImpl#onGetFromCache(Serializable, CacheProfile)}</code>
   * is executed when the cache provider contains a profile with the specified
   * id.
   */
  public void testGetFromCacheCacheKeyWithExistingProfileId() throws Exception {

    String cachedString = "Cached String";

    // set the expectations of the mock objects.
    this.mockCacheProfileMap.get(CACHE_PROFILE_ID);
    this.mockCacheProfileMapControl.setReturnValue(this.cacheProfile);
    this.mockCacheProfileMapControl.replay();

    this.cacheProviderFacade.onGetFromCache(this.cacheKey, this.cacheProfile);
    this.cacheProviderFacadeControl.setReturnValue(cachedString);
    this.cacheProviderFacadeControl.replay();

    // execute the method to test.
    Object cachedObject = this.cacheProviderFacade.getFromCache(this.cacheKey,
        CACHE_PROFILE_ID);

    // verify the expectations were met.
    this.mockCacheProfileMapControl.verify();
    this.cacheProviderFacadeControl.verify();

    assertSame("<cached object>", cachedString, cachedObject);
  }

  /**
   * Tests
   * <code>{@link AbstractCacheProviderFacadeImpl#getFromCache(Serializable, String)}</code>.
   * Verifies that
   * <code>{@link AbstractCacheProviderFacadeImpl#onGetFromCache(Serializable, CacheProfile)}</code>
   * is not executed when the cache provider does not contain a profile with the
   * specified id.
   */
  public void testGetFromCacheCacheKeyWithNotExistingProfileId()
      throws Exception {

    // set the expectations of the mock objects.
    this.mockCacheProfileMap.get(CACHE_PROFILE_ID);
    this.mockCacheProfileMapControl.setReturnValue(null);
    this.mockCacheProfileMapControl.replay();

    this.cacheProviderFacadeControl.replay();

    // execute the method to test.
    Object cachedObject = this.cacheProviderFacade.getFromCache(this.cacheKey,
        CACHE_PROFILE_ID);

    // verify the expectations were met.
    this.mockCacheProfileMapControl.verify();
    this.cacheProviderFacadeControl.verify();

    assertNull("The returned object should be null", cachedObject);
  }

  /**
   * Tests
   * <code>{@link AbstractCacheProviderFacadeImpl#handleCacheAccessException(String, RuntimeException)}</code>.
   * Verifies that the exception sent as argument is not thrown when
   * <code>{@link AbstractCacheProviderFacadeImpl#isFailQuietlyEnabled()}</code>
   * is <code>true</code>.
   */
  public void testHandleCacheAccessExceptionWhenFailedQuietlyIsEnableAndExceptionIsNotNull() {
    RuntimeException expectedException = new RuntimeException("An Exception :)");
    String message = "A String :)";

    RuntimeException catched = null;

    this.cacheProviderFacade.setFailQuietlyEnabled(true);
    try {
      this.cacheProviderFacade.handleCacheAccessException(message,
          expectedException);
    } catch (RuntimeException runtimeException) {
      catched = runtimeException;
    }

    assertNull("There should not be any thrown exception", catched);
  }

  /**
   * Tests
   * <code>{@link AbstractCacheProviderFacadeImpl#handleCacheAccessException(String, RuntimeException)}</code>.
   * Verifies that the exception sent as argument is thrown when
   * <code>{@link AbstractCacheProviderFacadeImpl#isFailQuietlyEnabled()}</code>
   * is <code>false</code>.
   */
  public void testHandleCacheAccessExceptionWhenFailedQuietlyIsNotEnableAndExceptionIsNotNull() {
    RuntimeException expectedException = new RuntimeException("An Exception :)");
    String message = "A String :)";

    RuntimeException catched = null;

    try {
      this.cacheProviderFacade.handleCacheAccessException(message,
          expectedException);
    } catch (RuntimeException runtimeException) {
      catched = runtimeException;
    }

    assertSame("<RuntimeException>", expectedException, catched);
  }

  /**
   * Tests
   * <code>{@link AbstractCacheProviderFacadeImpl#putInCache(Serializable, String, Object)}</code>.
   * Verifies that no exception is thrown if
   * <code>{@link AbstractCacheProviderFacadeImpl#onPutInCache(Serializable, CacheProfile, Object)}</code>
   * throws an exception and
   * <code>{@link AbstractCacheProviderFacadeImpl#isFailQuietlyEnabled()}</code>
   * is <code>true</code>.
   */
  public void testPutInCacheWhenAccessToCacheThrowsExceptionAndFailQuietlyIsEnabled() {

    String objectToCache = "A String";
    this.cacheProviderFacade.setFailQuietlyEnabled(true);

    // set the expectations of the mock objects.
    this.mockCacheProfileMap.get(CACHE_PROFILE_ID);
    this.mockCacheProfileMapControl.setReturnValue(this.cacheProfile);

    this.cacheProviderFacade.onPutInCache(this.cacheKey, this.cacheProfile,
        objectToCache);
    this.cacheProviderFacadeControl.setThrowable(new RuntimeException());

    // set the controls to "replay" state.
    this.mockCacheProfileMapControl.replay();
    this.cacheProviderFacadeControl.replay();

    // execute the method to test.
    this.cacheProviderFacade.putInCache(this.cacheKey, CACHE_PROFILE_ID,
        objectToCache);

    // verify the expectations were met.
    this.mockCacheProfileMapControl.verify();
    this.cacheProviderFacadeControl.verify();
  }

  /**
   * Tests
   * <code>{@link AbstractCacheProviderFacadeImpl#putInCache(Serializable, String, Object)}</code>.
   * Verifies that the exception thrown by
   * <code>{@link AbstractCacheProviderFacadeImpl#onPutInCache(Serializable, CacheProfile, Object)}</code>
   * is rethrown if
   * <code>{@link AbstractCacheProviderFacadeImpl#isFailQuietlyEnabled()}</code>
   * is <code>false</code>.
   */
  public void testPutInCacheWhenAccessToCacheThrowsExceptionAndFailQuietlyIsNotEnabled() {

    String objectToCache = "A String";
    this.cacheProviderFacade.setFailQuietlyEnabled(false);

    // set the expectations of the mock objects.
    this.mockCacheProfileMap.get(CACHE_PROFILE_ID);
    this.mockCacheProfileMapControl.setReturnValue(this.cacheProfile);

    this.cacheProviderFacade.onPutInCache(this.cacheKey, this.cacheProfile,
        objectToCache);
    RuntimeException exception = new RuntimeException();
    this.cacheProviderFacadeControl.setThrowable(exception);

    // set the controls to "replay" state.
    this.mockCacheProfileMapControl.replay();
    this.cacheProviderFacadeControl.replay();

    // execute the method to test. An exception should be rethrown since "fail
    // quietly" is not enabled.
    RuntimeException catched = null;
    try {
      this.cacheProviderFacade.putInCache(this.cacheKey, CACHE_PROFILE_ID,
          objectToCache);
    } catch (RuntimeException runtimeException) {
      catched = runtimeException;
    }

    // verify the expectations were met.
    this.mockCacheProfileMapControl.verify();
    this.cacheProviderFacadeControl.verify();

    assertSame("<Catched exception>", exception, catched);
  }

  /**
   * Tests
   * <code>{@link AbstractCacheProviderFacadeImpl#putInCache(Serializable, String, Object)}</code>.
   * Verifies that the method
   * <code>{@link AbstractCacheProviderFacadeImpl#onPutInCache(Serializable, CacheProfile, Object)}</code>
   * is executed when the cache provider contains a profile with the specified
   * id.
   */
  public void testPutInCacheWithExistingProfileId() {

    String objectToCache = "A String";

    // set the expectations of the mock objects.
    this.mockCacheProfileMap.get(CACHE_PROFILE_ID);
    this.mockCacheProfileMapControl.setReturnValue(this.cacheProfile);
    this.mockCacheProfileMapControl.replay();

    this.cacheProviderFacade.onPutInCache(this.cacheKey, this.cacheProfile,
        objectToCache);
    this.cacheProviderFacadeControl.replay();

    // execute the method to test.
    this.cacheProviderFacade.putInCache(this.cacheKey, CACHE_PROFILE_ID,
        objectToCache);

    // verify the expectations were met.
    this.mockCacheProfileMapControl.verify();
    this.cacheProviderFacadeControl.verify();
  }

  /**
   * Tests
   * <code>{@link AbstractCacheProviderFacadeImpl#putInCache(Serializable, String, Object)}</code>.
   * Verifies that
   * <code>{@link AbstractCacheProviderFacadeImpl#onPutInCache(Serializable, CacheProfile, Object)}</code>
   * is not executed when the cache provider does not contain a profile with the
   * specified id.
   */
  public void testPutInCacheWithNotExistingProfileId() {

    String objectToCache = "A String";

    // set the expectations of the mock objects.
    this.mockCacheProfileMap.get(CACHE_PROFILE_ID);
    this.mockCacheProfileMapControl.setReturnValue(null);
    this.mockCacheProfileMapControl.replay();

    this.cacheProviderFacadeControl.replay();

    // execute the method to test.
    this.cacheProviderFacade.putInCache(this.cacheKey, CACHE_PROFILE_ID,
        objectToCache);

    // verify the expectations were met.
    this.mockCacheProfileMapControl.verify();
    this.cacheProviderFacadeControl.verify();
  }

  /**
   * Tests
   * <code>{@link AbstractCacheProviderFacadeImpl#setCacheProfiles(Map)}</code>.
   * Verifies that an <code>IllegalArgumentException</code> is thrown if the
   * cache profile editor throws an exception.
   */
  public void testSetCacheProfilesWhenCacheProfileEditorThrowsException() {

    Properties unparsedProperties = new Properties();
    unparsedProperties.setProperty("key", "[firstName=Yvonne]");

    Properties parsedProperties = new Properties();
    parsedProperties.setProperty("firstName", "Yvonne");

    // expectation: get the cache profile editor.
    this.cacheProviderFacade.getCacheProfileEditor();
    this.cacheProviderFacadeControl.setReturnValue(this.mockCacheProfileEditor);

    // expectation: use the cache profile editor to create a new cache profile.
    // the editor should throw an exception.
    this.mockCacheProfileEditor.createCacheProfile(parsedProperties);
    RuntimeException exception = new RuntimeException();
    this.mockCacheProfileEditorControl.setThrowable(exception);

    // set the mock objects to "replay" state.
    this.mockCacheProfileEditorControl.replay();
    this.mockCacheProfileMapControl.replay();
    this.cacheProviderFacadeControl.replay();

    // execute the method to test.
    IllegalArgumentException catched = null;

    try {
      this.cacheProviderFacade.setCacheProfiles(unparsedProperties);
    } catch (IllegalArgumentException illegalArgumentException) {
      catched = illegalArgumentException;
    }

    // verify that the expectations were met.
    this.mockCacheProfileMapControl.verify();
    this.cacheProviderFacadeControl.verify();
    this.mockCacheProfileMapControl.verify();

    assertNotNull("An IllegalArgumentException should have been thrown",
        catched);
  }

  /**
   * Tests
   * <code>{@link AbstractCacheProviderFacadeImpl#setCacheProfiles(Properties)}</code>.
   * Verifies that a <code>IllegalArgumentException</code> is thrown if an
   * empty set of properties is passed as argument.
   */
  public void testSetCacheProfilesWithEmptySetOfProperties() {

    IllegalArgumentException catched = null;

    try {
      Properties properties = new Properties();
      this.cacheProviderFacade.setCacheProfiles(properties);
    } catch (IllegalArgumentException exception) {
      catched = exception;
    }

    assertNotNull("An IllegalArgumentException should have been catched",
        catched);
  }

  /**
   * Tests
   * <code>{@link AbstractCacheProviderFacadeImpl#setCacheProfiles(Map)}</code>.
   * Verifies that
   * <code>{@link AbstractCacheProviderFacadeImpl#setCacheProfiles(Properties)}</code>
   * is called when the map passed as parameter is an instance of
   * <code>java.util.Properties</code>.
   */
  public void testSetCacheProfilesWithMapBeingProperties() {

    Properties unparsedProperties = new Properties();
    unparsedProperties.setProperty("key", "[firstName=James]");
    unparsedProperties.setProperty("main", "[lastName=Gosling]");

    Map map = unparsedProperties;

    Properties firstParsedProperties = new Properties();
    firstParsedProperties.setProperty("firstName", "James");

    Properties secondParsedProperties = new Properties();
    secondParsedProperties.setProperty("lastName", "Gosling");

    // expectation: get the cache profile editor.
    this.cacheProviderFacade.getCacheProfileEditor();
    this.cacheProviderFacadeControl.setReturnValue(this.mockCacheProfileEditor);

    // expecation: create a cache profile from the first property.
    this.mockCacheProfileEditor.createCacheProfile(firstParsedProperties);
    this.mockCacheProfileEditorControl.setReturnValue(this.cacheProfile);

    // expecation: create a cache profile from the second property.
    this.mockCacheProfileEditor.createCacheProfile(secondParsedProperties);
    this.mockCacheProfileEditorControl.setReturnValue(this.cacheProfile);

    // set the state of the mock controls to 'replay'.
    this.mockCacheProfileEditorControl.replay();
    this.mockCacheProfileMapControl.replay();
    this.cacheProviderFacadeControl.replay();

    // execute the method to test.
    this.cacheProviderFacade.setCacheProfiles(map);

    Map actualCacheProfiles = this.cacheProviderFacade.getCacheProfiles();
    assertEquals("Number of cache profiles", 2, actualCacheProfiles.size());

    // verify that the expectations were met.
    this.mockCacheProfileMapControl.verify();
    this.cacheProviderFacadeControl.verify();
    this.mockCacheProfileMapControl.verify();
  }

  /**
   * Tests
   * <code>{@link AbstractCacheProviderFacadeImpl#setCacheProfiles(Properties)}</code>.
   * Verifies that a <code>IllegalArgumentException</code> is thrown if a set
   * of properties equal to <code>null</code> is passed as argument.
   */
  public void testSetCacheProfilesWithSetOfPropertiesEqualToNull() {

    IllegalArgumentException catched = null;

    try {
      Properties properties = null;
      this.cacheProviderFacade.setCacheProfiles(properties);
    } catch (IllegalArgumentException exception) {
      catched = exception;
    }

    assertNotNull("An IllegalArgumentException should have been catched",
        catched);
  }
}