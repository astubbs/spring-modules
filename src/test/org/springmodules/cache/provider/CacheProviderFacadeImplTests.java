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
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import junit.framework.TestCase;

import org.easymock.MockControl;
import org.easymock.classextension.MockClassControl;
import org.springmodules.cache.mock.MockCacheProfile;
import org.springmodules.cache.serializable.SerializableFactory;

/**
 * <p>
 * Unit Tests for <code>{@link AbstractCacheProviderFacadeImpl}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public final class CacheProviderFacadeImplTests extends TestCase {

  private static final String CACHE_PROFILE_ID = "CACHE_PROFILE_ID";

  /**
   * Key of the cached object to be retrieved from
   * <code>{@link #cacheProviderFacade}</code>.
   */
  private Serializable cacheKey;

  /**
   * Cache profile to be stored in <code>{@link #cacheProfileMap}</code> using
   * the key <code>{@link #CACHE_PROFILE_ID}</code>.
   */
  private CacheProfile cacheProfile;

  private AbstractCacheProfileEditor cacheProfileEditor;

  private MockClassControl cacheProfileEditorControl;

  private Map cacheProfileMap;

  private CacheProfileValidator cacheProfileValidator;

  private MockControl cacheProfileValidatorControl;

  /**
   * Primary object that is under test.
   */
  private AbstractCacheProviderFacadeImpl cacheProviderFacade;

  private MockClassControl cacheProviderFacadeControl;

  public CacheProviderFacadeImplTests(String name) {
    super(name);
  }

  private void assertAfterPropertiesSetThrowsException() {
    try {
      this.cacheProviderFacade.afterPropertiesSet();
      fail("Expecting exception <"
          + InvalidConfigurationException.class.getName() + ">");

    } catch (InvalidConfigurationException exception) {
      // we are expecting this exception to be thrown.
    }
  }

  private void assertIsNotSerializable(Object obj) {
    assertFalse(obj.getClass().getName() + " should not be serializable",
        obj instanceof Serializable);
  }

  private void expectCacheRequiresSerializableElements(boolean requires) {
    this.cacheProviderFacade.isSerializableCacheElementRequired();
    this.cacheProviderFacadeControl.setReturnValue(requires);
  }

  private void expectGetCacheProfileEditor() {
    this.cacheProviderFacade.getCacheProfileEditor();
    this.cacheProviderFacadeControl.setReturnValue(this.cacheProfileEditor);
  }

  private void expectGetCacheProfileValidator() {
    this.cacheProviderFacade.getCacheProfileValidator();
    this.cacheProviderFacadeControl.setReturnValue(this.cacheProfileValidator);
  }

  private CacheException getNewCacheException() {
    return new CacheNotFoundException("someCache");
  }

  private InvalidCacheProfileException getNewInvalidCacheProfileException() {
    return new InvalidCacheProfileException(
        "This exception should not make the test fail");
  }

  private void setStateOfMockControlsToReplay() {
    this.cacheProfileEditorControl.replay();
    if (this.cacheProfileValidatorControl != null) {
      this.cacheProfileValidatorControl.replay();
    }
    this.cacheProviderFacadeControl.replay();
  }

  protected void setUp() throws Exception {
    super.setUp();

    this.setUpCacheProviderFacadeAsMockObject();
    this.setUpCacheProfileEditorAsMockObject();

    this.cacheKey = "Key";
    this.cacheProfile = new MockCacheProfile();
    this.cacheProfileMap = new HashMap();
    this.cacheProfileMap.put(CACHE_PROFILE_ID, this.cacheProfile);
    this.cacheProviderFacade.setCacheProfiles(this.cacheProfileMap);
  }

  private void setUpCacheProfileEditorAsMockObject() throws Exception {
    Class classToMock = AbstractCacheProfileEditor.class;

    Method createCacheProfileMethod = classToMock.getDeclaredMethod(
        "createCacheProfile", new Class[] { Properties.class });

    Method[] methodsToMock = new Method[] { createCacheProfileMethod };

    this.cacheProfileEditorControl = MockClassControl.createControl(
        classToMock, null, null, methodsToMock);

    this.cacheProfileEditor = (AbstractCacheProfileEditor) this.cacheProfileEditorControl
        .getMock();
  }

  private void setUpCacheProfileValidatorAsMockObject() {
    this.cacheProfileValidatorControl = MockControl
        .createControl(CacheProfileValidator.class);

    this.cacheProfileValidator = (CacheProfileValidator) this.cacheProfileValidatorControl
        .getMock();
  }

  private void setUpCacheProviderFacadeAsMockObject() throws Exception {
    Class classToMock = AbstractCacheProviderFacadeImpl.class;

    Method getCacheProfileEditorMethod = classToMock.getDeclaredMethod(
        "getCacheProfileEditor", null);

    Method getCacheProfileValidatorMethod = classToMock.getDeclaredMethod(
        "getCacheProfileValidator", null);

    Method isSerializableCacheElementRequiredMethod = classToMock
        .getDeclaredMethod("isSerializableCacheElementRequired", null);

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
        getCacheProfileValidatorMethod,
        isSerializableCacheElementRequiredMethod, onCancelCacheUpdateMethod,
        onFlushCacheMethod, onGetFromCacheMethod, onPutInCacheMethod,
        validateCacheManagerMethod };

    this.cacheProviderFacadeControl = MockClassControl.createControl(
        classToMock, null, null, methodsToMock);

    this.cacheProviderFacade = (AbstractCacheProviderFacadeImpl) this.cacheProviderFacadeControl
        .getMock();
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractCacheProviderFacadeImpl#afterPropertiesSet()}</code>
   * does not try to validate the cache profiles if they have been previously
   * validated.
   */
  public void testAfterPropertiesSetWhenCacheProfilesHaveBeenValidated()
      throws Exception {
    // simulate the cache profiles have been validated.
    this.cacheProviderFacade.setCacheProfileMapValidated(true);

    // expectation: validate the cache manager ONLY.
    this.cacheProviderFacade.validateCacheManager();

    this.setStateOfMockControlsToReplay();

    // execute the method to test.
    this.cacheProviderFacade.afterPropertiesSet();

    this.verifyExpectationsOfMockControlsWereMet();
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractCacheProviderFacadeImpl#afterPropertiesSet()}</code>.
   * validates both the cache manager and the cache profiles.
   */
  public void testAfterPropertiesSetWhenCacheProfilesHaveNotBeenValidated()
      throws Exception {
    this.setUpCacheProfileValidatorAsMockObject();

    // expectation: validate cache manager.
    this.cacheProviderFacade.validateCacheManager();

    // expectation: validate the cache profile(s).
    this.expectGetCacheProfileValidator();
    this.cacheProfileValidator.validateCacheProfile(this.cacheProfile);

    this.setStateOfMockControlsToReplay();

    // execute the method to test.
    this.cacheProviderFacade.afterPropertiesSet();

    this.verifyExpectationsOfMockControlsWereMet();
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractCacheProviderFacadeImpl#afterPropertiesSet()}</code>.
   * throws a <code>{@link InvalidConfigurationException}</code> if the map of
   * cache profiles is empty.
   */
  public void testAfterPropertiesSetWhenMapOfCacheProfilesIsEmpty() {
    this.cacheProfileMap.clear();
    this.assertAfterPropertiesSetThrowsException();
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractCacheProviderFacadeImpl#afterPropertiesSet()}</code>.
   * throws a <code>{@link InvalidConfigurationException}</code> if the map of
   * cache profiles is equal to <code>null</code>.
   */
  public void testAfterPropertiesSetWhenMapOfCacheProfilesIsEqualToNull() {
    this.cacheProviderFacade.setCacheProfiles((Map) null);
    this.assertAfterPropertiesSetThrowsException();
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractCacheProviderFacadeImpl#afterPropertiesSet()}</code>
   * throws a <code>{@link InvalidConfigurationException}</code> that wraps
   * any exception thrown by the <code>{@link CacheProfileValidator}</code>.
   */
  public void testAfterPropertiesSetWhenValidationOfCacheProfilesThrowsException()
      throws Exception {
    this.setUpCacheProfileValidatorAsMockObject();

    // expectation: validate cache manager.
    this.cacheProviderFacade.validateCacheManager();

    // expectation: the cache profile is invalid.
    InvalidCacheProfileException expectedNestedException = this
        .getNewInvalidCacheProfileException();
    this.expectGetCacheProfileValidator();
    this.cacheProfileValidator.validateCacheProfile(this.cacheProfile);
    this.cacheProfileValidatorControl.setThrowable(expectedNestedException);

    this.setStateOfMockControlsToReplay();

    try {
      this.cacheProviderFacade.afterPropertiesSet();
      fail("Expecting exception <"
          + InvalidConfigurationException.class.getName() + ">");

    } catch (InvalidConfigurationException exception) {
      assertSame("<Nested exception>", expectedNestedException, exception
          .getCause());
    }

    this.verifyExpectationsOfMockControlsWereMet();
  }

  public void testCancelCacheUpdate() throws Exception {
    // expectation: cancel cache update.
    this.cacheProviderFacade.onCancelCacheUpdate(this.cacheKey);

    this.setStateOfMockControlsToReplay();

    // execute the method to test.
    this.cacheProviderFacade.cancelCacheUpdate(this.cacheKey);

    this.verifyExpectationsOfMockControlsWereMet();
  }

  public void testCancelCacheUpdateWhenCacheAccessThrowsExceptionAndFailQuietlyIsFalse()
      throws Exception {
    this.cacheProviderFacade.setFailQuietlyEnabled(false);

    CacheException expectedException = this.getNewCacheException();
    this.cacheProviderFacade.onCancelCacheUpdate(this.cacheKey);
    this.cacheProviderFacadeControl.setThrowable(expectedException);

    this.setStateOfMockControlsToReplay();

    try {
      // execute the method to test.
      this.cacheProviderFacade.cancelCacheUpdate(this.cacheKey);
      fail("Expecting exception <" + CacheException.class.getName() + ">");

    } catch (CacheException exception) {
      assertSame("<Catched exception>", expectedException, exception);
    }

    this.verifyExpectationsOfMockControlsWereMet();
  }

  public void testCancelCacheUpdateWhenCacheAccessThrowsExceptionAndFailQuietlyIsTrue()
      throws Exception {
    this.cacheProviderFacade.setFailQuietlyEnabled(true);

    // expectation: facade is unable to cancel cache update.
    this.cacheProviderFacade.onCancelCacheUpdate(this.cacheKey);
    this.cacheProviderFacadeControl.setThrowable(new CacheNotFoundException(
        "myCache"));

    this.setStateOfMockControlsToReplay();

    // execute the method to test.
    this.cacheProviderFacade.cancelCacheUpdate(this.cacheKey);

    this.verifyExpectationsOfMockControlsWereMet();
  }

  public void testFlushCacheWhenAccessToCacheThrowsExceptionAndFailQuietlyIsFalse()
      throws Exception {
    String[] cacheProfileIds = new String[] { CACHE_PROFILE_ID };
    this.cacheProviderFacade.setFailQuietlyEnabled(false);

    // expectation: facade is unable to flush the cache.
    CacheException expectedException = this.getNewCacheException();
    this.cacheProviderFacade.onFlushCache(this.cacheProfile);
    this.cacheProviderFacadeControl.setThrowable(expectedException);

    this.setStateOfMockControlsToReplay();

    try {
      // execute the method to test.
      this.cacheProviderFacade.flushCache(cacheProfileIds);
      fail("Expecting exception <" + CacheException.class.getName() + ">");

    } catch (CacheException exception) {
      assertSame("<Catched exception>", expectedException, exception);
    }

    this.verifyExpectationsOfMockControlsWereMet();
  }

  public void testFlushCacheWhenAccessToCacheThrowsExceptionAndFailQuietlyIsTrue()
      throws Exception {
    String[] cacheProfileIds = { CACHE_PROFILE_ID };
    this.cacheProviderFacade.setFailQuietlyEnabled(true);

    // expectation: facade is unable to flush the cache.
    CacheException cacheException = this.getNewCacheException();
    this.cacheProviderFacade.onFlushCache(this.cacheProfile);
    this.cacheProviderFacadeControl.setThrowable(cacheException);

    this.setStateOfMockControlsToReplay();

    // execute the method to test.
    this.cacheProviderFacade.flushCache(cacheProfileIds);

    this.verifyExpectationsOfMockControlsWereMet();
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractCacheProviderFacadeImpl#flushCache(String[])}</code>
   * does not flush the cache if the array of cache profile ids is equal to
   * <code>null</code>.
   */
  public void testFlushCacheWhenArrayOfProfileIdsIsEqualToNull()
      throws Exception {
    String[] cacheProfileIds = null;

    this.setStateOfMockControlsToReplay();

    // execute the method to test.
    this.cacheProviderFacade.flushCache(cacheProfileIds);

    this.verifyExpectationsOfMockControlsWereMet();
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractCacheProviderFacadeImpl#flushCache(String[])}</code>
   * does not flush the cache if the array of cache profile ids is empty.
   */
  public void testFlushCacheWhenProfileIdsIsEmpty() throws Exception {
    String[] cacheProfileIds = new String[0];

    this.setStateOfMockControlsToReplay();

    // execute the method to test.
    this.cacheProviderFacade.flushCache(cacheProfileIds);

    this.verifyExpectationsOfMockControlsWereMet();
  }

  public void testFlushCacheWithNotEmptyArrayOfProfileIdsAndExistingProfileId()
      throws Exception {
    String[] cacheProfileIds = new String[] { CACHE_PROFILE_ID };

    // expectation: flush the cache.
    this.cacheProviderFacade.onFlushCache(this.cacheProfile);

    this.setStateOfMockControlsToReplay();

    // execute the method to test.
    this.cacheProviderFacade.flushCache(cacheProfileIds);

    this.verifyExpectationsOfMockControlsWereMet();
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractCacheProviderFacadeImpl#flushCache(String[])}</code>
   * does not flush the cache if the specified cache profiles cannot be found.
   */
  public void testFlushCacheWithNotEmptyArrayOfProfileIdsAndNotExistingProfileId()
      throws Exception {
    String[] cacheProfileIds = new String[] { "anotherCacheProfileId" };

    this.setStateOfMockControlsToReplay();

    // execute the method to test.
    this.cacheProviderFacade.flushCache(cacheProfileIds);

    this.verifyExpectationsOfMockControlsWereMet();
  }

  public void testGetCacheProfilesReturnsAnUnmodifiableMap() {
    Map unmodifiableMap = this.cacheProviderFacade.getCacheProfiles();

    try {
      unmodifiableMap.clear();
      fail("Expecting exception <"
          + UnsupportedOperationException.class.getName() + ">");

    } catch (UnsupportedOperationException exception) {
      // we are expecting this exception.
    }
  }

  public void testGetCacheProfileWhenProfileIdIsNotEmpty() {
    CacheProfile actualCacheProfile = this.cacheProviderFacade
        .getCacheProfile(CACHE_PROFILE_ID);

    assertSame("<Cache Profile>", this.cacheProfile, actualCacheProfile);
  }

  public void testGetCacheProfileWhenProfileIdIsNull() {
    CacheProfile actualCacheProfile = this.cacheProviderFacade
        .getCacheProfile(null);

    assertNull(actualCacheProfile);
  }

  public void testGetCacheProfileWithProfileIdIsEmpty() {
    CacheProfile actualCacheProfile = this.cacheProviderFacade
        .getCacheProfile("");

    assertNull(actualCacheProfile);
  }

  public void testGetFromCacheWhenAccessToCacheThrowsExceptionAndFailQuietlyIsFalse()
      throws Exception {
    this.cacheProviderFacade.setFailQuietlyEnabled(false);

    // expectation: facade is unable to get an entry from the cache.
    CacheException expectedException = this.getNewCacheException();
    this.cacheProviderFacade.onGetFromCache(this.cacheKey, this.cacheProfile);
    this.cacheProviderFacadeControl.setThrowable(expectedException);

    this.setStateOfMockControlsToReplay();

    try {
      // execute the method to test.
      this.cacheProviderFacade.getFromCache(this.cacheKey, CACHE_PROFILE_ID);
      fail("Expecting exception <" + CacheException.class.getName() + ">");

    } catch (CacheException exception) {
      assertSame("<Catched exception>", expectedException, exception);
    }

    this.verifyExpectationsOfMockControlsWereMet();
  }

  public void testGetFromCacheWhenAccessToCacheThrowsExceptionAndFailQuietlyIsTrue()
      throws Exception {
    this.cacheProviderFacade.setFailQuietlyEnabled(true);

    // expectation: facade is unable to get an entry from the cache.
    this.cacheProviderFacade.onGetFromCache(this.cacheKey, this.cacheProfile);
    this.cacheProviderFacadeControl.setThrowable(new CacheNotFoundException(
        "testCache"));

    this.setStateOfMockControlsToReplay();

    // execute the method to test.
    Object cachedObject = this.cacheProviderFacade.getFromCache(this.cacheKey,
        CACHE_PROFILE_ID);

    assertNull(cachedObject);

    this.verifyExpectationsOfMockControlsWereMet();
  }

  public void testGetFromCacheWithExistingProfileId() throws Exception {
    String cachedString = "Cached String";

    // expectation: get an entry from the cache.
    this.cacheProviderFacade.onGetFromCache(this.cacheKey, this.cacheProfile);
    this.cacheProviderFacadeControl.setReturnValue(cachedString);

    this.setStateOfMockControlsToReplay();

    // execute the method to test.
    Object cachedObject = this.cacheProviderFacade.getFromCache(this.cacheKey,
        CACHE_PROFILE_ID);

    assertSame("<Cached object>", cachedString, cachedObject);

    this.verifyExpectationsOfMockControlsWereMet();
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractCacheProviderFacadeImpl#getFromCache(Serializable, String)}</code>
   * does not try to access the cache if the cache facade does not contain a
   * profile stored under the given id.
   */
  public void testGetFromCacheWithNotExistingProfileId() throws Exception {
    this.setStateOfMockControlsToReplay();

    // execute the method to test.
    Object cachedObject = this.cacheProviderFacade.getFromCache(this.cacheKey,
        "anotherCacheProfileId");

    assertNull(cachedObject);

    this.verifyExpectationsOfMockControlsWereMet();
  }

  public void testHandleCacheAccessExceptionWhenFailedQuietlyIsFalse() {
    this.cacheProviderFacade.setFailQuietlyEnabled(false);
    CacheException expectedException = this.getNewCacheException();

    try {
      this.cacheProviderFacade.handleCacheException(expectedException);
      fail("Expecting exception <" + CacheException.class.getName() + ">");

    } catch (CacheException exception) {
      assertSame(expectedException, exception);
    }
  }

  public void testHandleCacheAccessExceptionWhenFailedQuietlyIsTrue() {
    this.cacheProviderFacade.setFailQuietlyEnabled(true);
    CacheException cacheException = this.getNewCacheException();

    try {
      this.cacheProviderFacade.handleCacheException(cacheException);

    } catch (CacheException exception) {
      fail("Exception should not be rethrown");
    }
  }

  public void testMakeSerializableIfNecessaryWithCacheNotRequiringSerializableElementsAndNotSerializableObjectToCache() {
    this.expectCacheRequiresSerializableElements(false);

    Object objectToCache = new Socket();
    assertIsNotSerializable(objectToCache);

    this.setStateOfMockControlsToReplay();

    assertSame(objectToCache, this.cacheProviderFacade
        .makeSerializableIfNecessary(objectToCache));

    this.verifyExpectationsOfMockControlsWereMet();
  }

  public void testMakeSerializableIfNecessaryWithCacheRequiringSerializableElementAndSerializableFactoryIsNullAndCacheElementIsNotSerializable() {
    this.expectCacheRequiresSerializableElements(true);
    this.setStateOfMockControlsToReplay();

    Object objectToCache = new Socket();
    assertIsNotSerializable(objectToCache);

    try {
      this.cacheProviderFacade.makeSerializableIfNecessary(objectToCache);
      fail("Expecting exception <"
          + InvalidObjectToCacheException.class.getName() + ">");

    } catch (InvalidObjectToCacheException exception) {
      // we are expecting this exception.
    }
  }

  public void testMakeSerializableIfNecessaryWithCacheRequiringSerializableElementAndSerializableFactoryIsNullAndCacheElementIsSerializable() {
    this.expectCacheRequiresSerializableElements(true);
    this.setStateOfMockControlsToReplay();

    Object objectToCache = "R2-D2";
    assertSame(objectToCache, this.cacheProviderFacade
        .makeSerializableIfNecessary(objectToCache));

    this.verifyExpectationsOfMockControlsWereMet();
  }

  public void testMakeSerializableIfNecessaryWithCacheRequiringSerializableElementsAndSerializableFactoryIsNotNull() {
    MockControl serializableFactoryControl = MockControl
        .createControl(SerializableFactory.class);
    SerializableFactory serializableFactory = (SerializableFactory) serializableFactoryControl
        .getMock();
    this.cacheProviderFacade.setSerializableFactory(serializableFactory);

    this.expectCacheRequiresSerializableElements(true);

    Object objectToCache = "Luke Skywalker";
    serializableFactory.makeSerializableIfNecessary(objectToCache);
    serializableFactoryControl.setReturnValue(objectToCache);

    this.setStateOfMockControlsToReplay();
    serializableFactoryControl.replay();

    assertSame(objectToCache, this.cacheProviderFacade
        .makeSerializableIfNecessary(objectToCache));

    this.verifyExpectationsOfMockControlsWereMet();
    serializableFactoryControl.verify();
  }

  public void testPutInCacheWhenAccessToCacheThrowsExceptionAndFailQuietlyIsFalse()
      throws Exception {
    this.expectCacheRequiresSerializableElements(false);

    Object objectToCache = new Object();
    this.cacheProviderFacade.setFailQuietlyEnabled(false);

    // expectation: facade is unable to store an entry in the cache.
    CacheException expectedException = this.getNewCacheException();
    this.cacheProviderFacade.onPutInCache(this.cacheKey, this.cacheProfile,
        objectToCache);
    this.cacheProviderFacadeControl.setThrowable(expectedException);

    this.setStateOfMockControlsToReplay();

    try {
      // execute the method to test.
      this.cacheProviderFacade.putInCache(this.cacheKey, CACHE_PROFILE_ID,
          objectToCache);
      fail("Expecting exception <" + CacheException.class.getName() + ">");

    } catch (CacheException exception) {
      assertSame("<Catched exception>", expectedException, exception);
    }

    this.verifyExpectationsOfMockControlsWereMet();
  }

  public void testPutInCacheWhenAccessToCacheThrowsExceptionAndFailQuietlyIsTrue()
      throws Exception {
    this.expectCacheRequiresSerializableElements(false);

    Object objectToCache = new Object();
    this.cacheProviderFacade.setFailQuietlyEnabled(true);

    // expectation: facade is unable to store an entry in the cache.
    this.cacheProviderFacade.onPutInCache(this.cacheKey, this.cacheProfile,
        objectToCache);
    this.cacheProviderFacadeControl.setThrowable(new CacheNotFoundException(
        "testCache"));

    this.setStateOfMockControlsToReplay();

    // execute the method to test.
    this.cacheProviderFacade.putInCache(this.cacheKey, CACHE_PROFILE_ID,
        objectToCache);

    this.verifyExpectationsOfMockControlsWereMet();
  }

  public void testPutInCacheWhenMakeSerializableThrowsExceptionAndFailQuietlyIsFalse() {
    this.expectCacheRequiresSerializableElements(true);
    this.cacheProviderFacade.setFailQuietlyEnabled(false);

    Object objectToCache = new Object();
    assertIsNotSerializable(objectToCache);

    this.setStateOfMockControlsToReplay();

    try {
      // execute the method to test.
      this.cacheProviderFacade.putInCache(this.cacheKey, CACHE_PROFILE_ID,
          objectToCache);
      fail("Expecting exception <"
          + InvalidObjectToCacheException.class.getName() + ">");

    } catch (InvalidObjectToCacheException exception) {
      // expecting exception.
    }

    this.verifyExpectationsOfMockControlsWereMet();
  }

  public void testPutInCacheWhenMakeSerializableThrowsExceptionAndFailQuietlyIsTrue() {
    this.expectCacheRequiresSerializableElements(true);
    this.cacheProviderFacade.setFailQuietlyEnabled(true);

    Object objectToCache = new Object();
    assertIsNotSerializable(objectToCache);

    this.setStateOfMockControlsToReplay();

    this.cacheProviderFacade.putInCache(this.cacheKey, CACHE_PROFILE_ID,
        objectToCache);

    this.verifyExpectationsOfMockControlsWereMet();
  }

  public void testPutInCacheWithExistingProfileId() throws Exception {
    this.expectCacheRequiresSerializableElements(false);

    Object objectToCache = new Object();

    this.cacheProviderFacade.onPutInCache(this.cacheKey, this.cacheProfile,
        objectToCache);

    this.setStateOfMockControlsToReplay();

    // execute the method to test.
    this.cacheProviderFacade.putInCache(this.cacheKey, CACHE_PROFILE_ID,
        objectToCache);

    this.verifyExpectationsOfMockControlsWereMet();
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractCacheProviderFacadeImpl#putInCache(Serializable, String, Object)}</code>.
   * does not try to access the cache if the cache facade does not contain a
   * profile stored under the given id.
   */
  public void testPutInCacheWithNotExistingProfileId() throws Exception {
    this.expectCacheRequiresSerializableElements(false);

    Object objectToCache = new Object();

    this.setStateOfMockControlsToReplay();

    // execute the method to test.
    this.cacheProviderFacade.putInCache(this.cacheKey, "someCacheProfileId",
        objectToCache);

    this.verifyExpectationsOfMockControlsWereMet();
  }

  /**
   * Tests
   * <code>{@link AbstractCacheProviderFacadeImpl#setCacheProfiles(Properties)}</code>.
   * Verifies that an <code>IllegalArgumentException</code> is thrown if the
   * cache profile editor throws an exception.
   */
  public void testSetCacheProfilesWhenCacheProfileEditorThrowsException() {
    Properties unparsedProperties = new Properties();
    unparsedProperties.setProperty("key", "[role=Admin]");

    Properties parsedProperties = new Properties();
    parsedProperties.setProperty("role", "Admin");

    this.expectGetCacheProfileEditor();

    // expectation: use the cache profile editor to create a new cache profile.
    InvalidCacheProfileException expectedException = this
        .getNewInvalidCacheProfileException();
    this.cacheProfileEditor.createCacheProfile(parsedProperties);
    this.cacheProfileEditorControl.setThrowable(expectedException);

    this.setStateOfMockControlsToReplay();

    try {
      // execute the method to test.
      this.cacheProviderFacade.setCacheProfiles(unparsedProperties);
      fail("Expecting exception <"
          + InvalidCacheProfileException.class.getName() + ">");

    } catch (InvalidCacheProfileException exception) {
      assertSame(expectedException, exception);
    }

    this.verifyExpectationsOfMockControlsWereMet();
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

  public void testSetCacheProfilesWithProperties() {
    Properties unparsedProperties = new Properties();
    unparsedProperties.setProperty("key", "[firstName=Luke]");
    unparsedProperties.setProperty("main", "[lastName=Skywalker]");

    Map map = unparsedProperties;

    Properties firstParsedProperties = new Properties();
    firstParsedProperties.setProperty("firstName", "Luke");

    Properties secondParsedProperties = new Properties();
    secondParsedProperties.setProperty("lastName", "Skywalker");

    this.expectGetCacheProfileEditor();

    // expecation: create a cache profile from the first property.
    this.cacheProfileEditor.createCacheProfile(firstParsedProperties);
    this.cacheProfileEditorControl.setReturnValue(this.cacheProfile);

    // expecation: create a cache profile from the second property.
    this.cacheProfileEditor.createCacheProfile(secondParsedProperties);
    this.cacheProfileEditorControl.setReturnValue(this.cacheProfile);

    this.setStateOfMockControlsToReplay();

    // execute the method to test.
    this.cacheProviderFacade.setCacheProfiles(map);

    Map actualCacheProfiles = this.cacheProviderFacade.getCacheProfiles();
    assertEquals("Number of cache profiles", 2, actualCacheProfiles.size());

    this.verifyExpectationsOfMockControlsWereMet();
  }

  public void testSetCacheProfilesWithSetOfPropertiesEqualToNull() {
    try {
      Properties properties = null;
      this.cacheProviderFacade.setCacheProfiles(properties);
      fail("Expecting exception <" + IllegalArgumentException.class.getName()
          + ">");

    } catch (IllegalArgumentException exception) {
      // we are expecting this exception.
    }
  }

  private void verifyExpectationsOfMockControlsWereMet() {
    this.cacheProfileEditorControl.verify();
    if (this.cacheProfileValidatorControl != null) {
      this.cacheProfileValidatorControl.verify();
    }
    this.cacheProviderFacadeControl.verify();
  }
}