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

import java.beans.PropertyEditor;
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

  private PropertyEditor cacheProfileEditor;

  private MockControl cacheProfileEditorControl;

  private Map cacheProfileMap;

  private CacheProfileValidator cacheProfileValidator;

  private MockControl cacheProfileValidatorControl;

  private AbstractCacheProviderFacadeImpl cacheProviderFacade;

  private MockClassControl cacheProviderFacadeControl;

  public CacheProviderFacadeImplTests(String name) {
    super(name);
  }

  private void assertAfterPropertiesSetThrowsException() {
    try {
      cacheProviderFacade.afterPropertiesSet();
      fail();

    } catch (FatalCacheException exception) {
      // we are expecting this exception to be thrown.
    }
  }

  private void assertIsNotSerializable(Object obj) {
    assertFalse(obj.getClass().getName() + " should not be serializable",
        obj instanceof Serializable);
  }

  private void expectCacheRequiresSerializableElements(boolean requires) {
    cacheProviderFacade.isSerializableCacheElementRequired();
    cacheProviderFacadeControl.setReturnValue(requires);
  }

  private void expectGetCacheProfileEditor() {
    cacheProviderFacade.getCacheProfileEditor();
    cacheProviderFacadeControl.setReturnValue(cacheProfileEditor);
  }

  private void expectGetCacheProfileValidator() {
    cacheProviderFacade.getCacheProfileValidator();
    cacheProviderFacadeControl.setReturnValue(cacheProfileValidator);
  }

  private CacheException getNewCacheException() {
    return new CacheNotFoundException("someCache");
  }

  private InvalidCacheProfileException getNewInvalidCacheProfileException() {
    return new InvalidCacheProfileException(
        "This exception should not make the test fail");
  }

  private void setStateOfMockControlsToReplay() {
    cacheProfileEditorControl.replay();
    if (cacheProfileValidatorControl != null) {
      cacheProfileValidatorControl.replay();
    }
    cacheProviderFacadeControl.replay();
  }

  protected void setUp() throws Exception {
    super.setUp();

    setUpCacheProviderFacadeAsMockObject();
    setUpCacheProfileEditorAsMockObject();

    cacheKey = "Key";
    cacheProfile = new MockCacheProfile();
    cacheProfileMap = new HashMap();
    cacheProfileMap.put(CACHE_PROFILE_ID, cacheProfile);
    cacheProviderFacade.setCacheProfiles(cacheProfileMap);
  }

  private void setUpCacheProfileEditorAsMockObject() {
    cacheProfileEditorControl = MockControl.createControl(PropertyEditor.class);
    cacheProfileEditor = (PropertyEditor) cacheProfileEditorControl.getMock();
  }

  private void setUpCacheProfileValidatorAsMockObject() {
    cacheProfileValidatorControl = MockControl
        .createControl(CacheProfileValidator.class);

    cacheProfileValidator = (CacheProfileValidator) cacheProfileValidatorControl
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

    cacheProviderFacadeControl = MockClassControl.createControl(classToMock,
        null, null, methodsToMock);

    cacheProviderFacade = (AbstractCacheProviderFacadeImpl) cacheProviderFacadeControl
        .getMock();
  }

  public void testAfterPropertiesSetValidatesCacheManager() throws Exception {
    setUpCacheProfileValidatorAsMockObject();

    // validate cache manager.
    cacheProviderFacade.validateCacheManager();

    // validate the cache profile(s).
    expectGetCacheProfileValidator();
    cacheProfileValidator.validateCacheProfile(cacheProfile);

    setStateOfMockControlsToReplay();

    // execute the method to test.
    cacheProviderFacade.afterPropertiesSet();

    verifyExpectationsOfMockControlsWereMet();
  }

  public void testAfterPropertiesSetWhenMapOfCacheProfilesIsEmpty() {
    cacheProfileMap.clear();
    assertAfterPropertiesSetThrowsException();
  }

  public void testAfterPropertiesSetWhenMapOfCacheProfilesIsEqualToNull() {
    cacheProviderFacade.setCacheProfiles(null);
    assertAfterPropertiesSetThrowsException();
  }

  public void testAfterPropertiesSetWhenMapOfCacheProfilesIsProperties()
      throws Exception {
    setUpCacheProfileValidatorAsMockObject();

    this.cacheProviderFacade.validateCacheManager();

    String cacheProfileId = "main";
    String cacheProfilePropertiesAsText = "[firstName=Luke]";

    Properties properties = new Properties();
    properties.setProperty(cacheProfileId, cacheProfilePropertiesAsText);

    cacheProviderFacade.setCacheProfiles(properties);

    expectGetCacheProfileEditor();
    cacheProfileEditor.setAsText(cacheProfilePropertiesAsText);
    cacheProfileEditor.getValue();
    cacheProfileEditorControl.setReturnValue(cacheProfile);

    expectGetCacheProfileValidator();
    cacheProfileValidator.validateCacheProfile(cacheProfile);

    setStateOfMockControlsToReplay();

    // execute the method to test.
    cacheProviderFacade.afterPropertiesSet();

    assertSame(cacheProfile, cacheProviderFacade.getCacheProfiles().get(
        cacheProfileId));

    verifyExpectationsOfMockControlsWereMet();
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractCacheProviderFacadeImpl#afterPropertiesSet()}</code>
   * throws a <code>{@link FatalCacheException}</code> wrapping
   * any exception thrown by the <code>{@link CacheProfileValidator}</code>.
   */
  public void testAfterPropertiesSetWhenValidationOfCacheProfilesThrowsException()
      throws Exception {
    setUpCacheProfileValidatorAsMockObject();

    // validate cache manager.
    cacheProviderFacade.validateCacheManager();

    // the cache profile is invalid.
    InvalidCacheProfileException expectedNestedException = this
        .getNewInvalidCacheProfileException();
    expectGetCacheProfileValidator();
    cacheProfileValidator.validateCacheProfile(cacheProfile);
    cacheProfileValidatorControl.setThrowable(expectedNestedException);

    setStateOfMockControlsToReplay();

    try {
      cacheProviderFacade.afterPropertiesSet();
      fail();

    } catch (FatalCacheException exception) {
      assertSame("<Nested exception>", expectedNestedException, exception
          .getCause());
    }

    verifyExpectationsOfMockControlsWereMet();
  }

  public void testCancelCacheUpdate() throws Exception {
    cacheProviderFacade.onCancelCacheUpdate(cacheKey);

    setStateOfMockControlsToReplay();

    // execute the method to test.
    cacheProviderFacade.cancelCacheUpdate(cacheKey);

    verifyExpectationsOfMockControlsWereMet();
  }

  public void testCancelCacheUpdateWhenCacheAccessThrowsExceptionAndFailQuietlyIsFalse()
      throws Exception {
    cacheProviderFacade.setFailQuietlyEnabled(false);

    CacheException expectedException = getNewCacheException();
    cacheProviderFacade.onCancelCacheUpdate(cacheKey);
    cacheProviderFacadeControl.setThrowable(expectedException);

    setStateOfMockControlsToReplay();

    try {
      // execute the method to test.
      cacheProviderFacade.cancelCacheUpdate(cacheKey);
      fail();

    } catch (CacheException exception) {
      assertSame(expectedException, exception);
    }

    verifyExpectationsOfMockControlsWereMet();
  }

  public void testCancelCacheUpdateWhenCacheAccessThrowsExceptionAndFailQuietlyIsTrue()
      throws Exception {
    cacheProviderFacade.setFailQuietlyEnabled(true);

    // facade is unable to cancel cache update.
    cacheProviderFacade.onCancelCacheUpdate(cacheKey);
    cacheProviderFacadeControl.setThrowable(new CacheNotFoundException(
        "myCache"));

    setStateOfMockControlsToReplay();

    // execute the method to test.
    cacheProviderFacade.cancelCacheUpdate(cacheKey);

    verifyExpectationsOfMockControlsWereMet();
  }

  public void testFlushCacheWhenAccessToCacheThrowsExceptionAndFailQuietlyIsFalse()
      throws Exception {
    String[] cacheProfileIds = new String[] { CACHE_PROFILE_ID };
    cacheProviderFacade.setFailQuietlyEnabled(false);

    // facade is unable to flush the cache.
    CacheException expectedException = getNewCacheException();
    cacheProviderFacade.onFlushCache(cacheProfile);
    cacheProviderFacadeControl.setThrowable(expectedException);

    setStateOfMockControlsToReplay();

    try {
      // execute the method to test.
      cacheProviderFacade.flushCache(cacheProfileIds);
      fail();

    } catch (CacheException exception) {
      assertSame(expectedException, exception);
    }

    verifyExpectationsOfMockControlsWereMet();
  }

  public void testFlushCacheWhenAccessToCacheThrowsExceptionAndFailQuietlyIsTrue()
      throws Exception {
    String[] cacheProfileIds = { CACHE_PROFILE_ID };
    cacheProviderFacade.setFailQuietlyEnabled(true);

    // facade is unable to flush the cache.
    CacheException cacheException = getNewCacheException();
    cacheProviderFacade.onFlushCache(cacheProfile);
    cacheProviderFacadeControl.setThrowable(cacheException);

    setStateOfMockControlsToReplay();

    // execute the method to test.
    cacheProviderFacade.flushCache(cacheProfileIds);

    verifyExpectationsOfMockControlsWereMet();
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

    setStateOfMockControlsToReplay();

    // execute the method to test.
    cacheProviderFacade.flushCache(cacheProfileIds);

    verifyExpectationsOfMockControlsWereMet();
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractCacheProviderFacadeImpl#flushCache(String[])}</code>
   * does not flush the cache if the array of cache profile ids is empty.
   */
  public void testFlushCacheWhenProfileIdsIsEmpty() throws Exception {
    String[] cacheProfileIds = new String[0];

    setStateOfMockControlsToReplay();

    // execute the method to test.
    cacheProviderFacade.flushCache(cacheProfileIds);

    verifyExpectationsOfMockControlsWereMet();
  }

  public void testFlushCacheWithNotEmptyArrayOfProfileIdsAndExistingProfileId()
      throws Exception {
    String[] cacheProfileIds = new String[] { CACHE_PROFILE_ID };

    // flush the cache.
    cacheProviderFacade.onFlushCache(cacheProfile);

    setStateOfMockControlsToReplay();

    // execute the method to test.
    cacheProviderFacade.flushCache(cacheProfileIds);

    verifyExpectationsOfMockControlsWereMet();
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractCacheProviderFacadeImpl#flushCache(String[])}</code>
   * does not flush the cache if the specified cache profiles cannot be found.
   */
  public void testFlushCacheWithNotEmptyArrayOfProfileIdsAndNotExistingProfileId()
      throws Exception {
    String[] cacheProfileIds = new String[] { "anotherCacheProfileId" };

    setStateOfMockControlsToReplay();

    // execute the method to test.
    cacheProviderFacade.flushCache(cacheProfileIds);

    verifyExpectationsOfMockControlsWereMet();
  }

  public void testGetCacheProfilesReturnsAnUnmodifiableMap() {
    Map unmodifiableMap = cacheProviderFacade.getCacheProfiles();

    try {
      unmodifiableMap.clear();
      fail();

    } catch (UnsupportedOperationException exception) {
      // we are expecting this exception.
    }
  }

  public void testGetCacheProfileWhenProfileIdIsNotEmpty() {
    CacheProfile actualCacheProfile = cacheProviderFacade
        .getCacheProfile(CACHE_PROFILE_ID);

    assertSame("<Cache Profile>", cacheProfile, actualCacheProfile);
  }

  public void testGetCacheProfileWhenProfileIdIsNull() {
    CacheProfile actualCacheProfile = cacheProviderFacade.getCacheProfile(null);

    assertNull(actualCacheProfile);
  }

  public void testGetCacheProfileWithProfileIdIsEmpty() {
    CacheProfile actualCacheProfile = cacheProviderFacade.getCacheProfile("");

    assertNull(actualCacheProfile);
  }

  public void testGetFromCacheWhenAccessToCacheThrowsExceptionAndFailQuietlyIsFalse()
      throws Exception {
    cacheProviderFacade.setFailQuietlyEnabled(false);

    // facade is unable to get an entry from the cache.
    CacheException expectedException = getNewCacheException();
    cacheProviderFacade.onGetFromCache(cacheKey, cacheProfile);
    cacheProviderFacadeControl.setThrowable(expectedException);

    setStateOfMockControlsToReplay();

    try {
      // execute the method to test.
      cacheProviderFacade.getFromCache(cacheKey, CACHE_PROFILE_ID);
      fail();

    } catch (CacheException exception) {
      assertSame(expectedException, exception);
    }

    verifyExpectationsOfMockControlsWereMet();
  }

  public void testGetFromCacheWhenAccessToCacheThrowsExceptionAndFailQuietlyIsTrue()
      throws Exception {
    cacheProviderFacade.setFailQuietlyEnabled(true);

    // facade is unable to get an entry from the cache.
    cacheProviderFacade.onGetFromCache(cacheKey, cacheProfile);
    cacheProviderFacadeControl.setThrowable(new CacheNotFoundException(
        "testCache"));

    setStateOfMockControlsToReplay();

    // execute the method to test.
    Object cachedObject = cacheProviderFacade.getFromCache(cacheKey,
        CACHE_PROFILE_ID);

    assertNull(cachedObject);

    verifyExpectationsOfMockControlsWereMet();
  }

  public void testGetFromCacheWithExistingProfileId() throws Exception {
    String cachedString = "Cached String";

    // get an entry from the cache.
    cacheProviderFacade.onGetFromCache(cacheKey, cacheProfile);
    cacheProviderFacadeControl.setReturnValue(cachedString);

    setStateOfMockControlsToReplay();

    // execute the method to test.
    Object cachedObject = cacheProviderFacade.getFromCache(cacheKey,
        CACHE_PROFILE_ID);

    assertSame("<Cached object>", cachedString, cachedObject);

    verifyExpectationsOfMockControlsWereMet();
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractCacheProviderFacadeImpl#getFromCache(Serializable, String)}</code>
   * does not try to access the cache if the cache facade does not contain a
   * profile stored under the given id.
   */
  public void testGetFromCacheWithNotExistingProfileId() throws Exception {
    setStateOfMockControlsToReplay();

    // execute the method to test.
    Object cachedObject = cacheProviderFacade.getFromCache(cacheKey,
        "anotherCacheProfileId");

    assertNull(cachedObject);

    verifyExpectationsOfMockControlsWereMet();
  }

  public void testHandleCacheAccessExceptionWhenFailedQuietlyIsFalse() {
    cacheProviderFacade.setFailQuietlyEnabled(false);
    CacheException expectedException = getNewCacheException();

    try {
      cacheProviderFacade.handleCatchedException(expectedException);
      fail();

    } catch (CacheException exception) {
      assertSame(expectedException, exception);
    }
  }

  public void testHandleCacheAccessExceptionWhenFailedQuietlyIsTrue() {
    cacheProviderFacade.setFailQuietlyEnabled(true);
    CacheException cacheException = getNewCacheException();

    try {
      cacheProviderFacade.handleCatchedException(cacheException);
    } catch (CacheException exception) {
      fail();
    }
  }

  public void testMakeSerializableIfNecessaryWithCacheNotRequiringSerializableElementsAndNotSerializableObjectToCache() {
    expectCacheRequiresSerializableElements(false);

    Object objectToCache = new Socket();
    assertIsNotSerializable(objectToCache);

    setStateOfMockControlsToReplay();

    assertSame(objectToCache, cacheProviderFacade
        .makeSerializableIfNecessary(objectToCache));

    verifyExpectationsOfMockControlsWereMet();
  }

  public void testMakeSerializableIfNecessaryWithCacheRequiringSerializableElementAndSerializableFactoryIsNullAndCacheElementIsNotSerializable() {
    expectCacheRequiresSerializableElements(true);
    setStateOfMockControlsToReplay();

    Object objectToCache = new Socket();
    assertIsNotSerializable(objectToCache);

    try {
      cacheProviderFacade.makeSerializableIfNecessary(objectToCache);
      fail();

    } catch (ObjectCannotBeCachedException exception) {
      // we are expecting this exception.
    }
  }

  public void testMakeSerializableIfNecessaryWithCacheRequiringSerializableElementAndSerializableFactoryIsNullAndCacheElementIsSerializable() {
    expectCacheRequiresSerializableElements(true);
    setStateOfMockControlsToReplay();

    Object objectToCache = "R2-D2";
    assertSame(objectToCache, cacheProviderFacade
        .makeSerializableIfNecessary(objectToCache));

    verifyExpectationsOfMockControlsWereMet();
  }

  public void testMakeSerializableIfNecessaryWithCacheRequiringSerializableElementsAndSerializableFactoryIsNotNull() {
    expectCacheRequiresSerializableElements(true);

    Object objectToCache = "Luke Skywalker";
    assertSame(objectToCache, cacheProviderFacade
        .makeSerializableIfNecessary(objectToCache));
  }

  public void testPutInCacheWhenAccessToCacheThrowsExceptionAndFailQuietlyIsFalse()
      throws Exception {
    expectCacheRequiresSerializableElements(false);

    Object objectToCache = new Object();
    cacheProviderFacade.setFailQuietlyEnabled(false);

    // facade is unable to store an entry in the cache.
    CacheException expectedException = getNewCacheException();
    cacheProviderFacade.onPutInCache(cacheKey, cacheProfile, objectToCache);
    cacheProviderFacadeControl.setThrowable(expectedException);

    setStateOfMockControlsToReplay();

    try {
      // execute the method to test.
      cacheProviderFacade.putInCache(cacheKey, CACHE_PROFILE_ID, objectToCache);
      fail();

    } catch (CacheException exception) {
      assertSame("<Catched exception>", expectedException, exception);
    }

    verifyExpectationsOfMockControlsWereMet();
  }

  public void testPutInCacheWhenAccessToCacheThrowsExceptionAndFailQuietlyIsTrue()
      throws Exception {
    expectCacheRequiresSerializableElements(false);

    Object objectToCache = new Object();
    cacheProviderFacade.setFailQuietlyEnabled(true);

    // facade is unable to store an entry in the cache.
    cacheProviderFacade.onPutInCache(cacheKey, cacheProfile, objectToCache);
    cacheProviderFacadeControl.setThrowable(new CacheNotFoundException(
        "testCache"));

    setStateOfMockControlsToReplay();

    // execute the method to test.
    cacheProviderFacade.putInCache(cacheKey, CACHE_PROFILE_ID, objectToCache);

    verifyExpectationsOfMockControlsWereMet();
  }

  public void testPutInCacheWhenMakeSerializableThrowsExceptionAndFailQuietlyIsFalse() {
    expectCacheRequiresSerializableElements(true);
    cacheProviderFacade.setFailQuietlyEnabled(false);

    Object objectToCache = new Object();
    assertIsNotSerializable(objectToCache);

    setStateOfMockControlsToReplay();

    try {
      // execute the method to test.
      cacheProviderFacade.putInCache(cacheKey, CACHE_PROFILE_ID, objectToCache);
      fail();

    } catch (ObjectCannotBeCachedException exception) {
      // expecting exception.
    }

    verifyExpectationsOfMockControlsWereMet();
  }

  public void testPutInCacheWhenMakeSerializableThrowsExceptionAndFailQuietlyIsTrue() {
    expectCacheRequiresSerializableElements(true);
    cacheProviderFacade.setFailQuietlyEnabled(true);

    Object objectToCache = new Object();
    assertIsNotSerializable(objectToCache);

    setStateOfMockControlsToReplay();

    cacheProviderFacade.putInCache(cacheKey, CACHE_PROFILE_ID, objectToCache);

    verifyExpectationsOfMockControlsWereMet();
  }

  public void testPutInCacheWithExistingProfileId() throws Exception {
    expectCacheRequiresSerializableElements(false);

    Object objectToCache = new Object();

    cacheProviderFacade.onPutInCache(cacheKey, cacheProfile, objectToCache);

    setStateOfMockControlsToReplay();

    // execute the method to test.
    cacheProviderFacade.putInCache(cacheKey, CACHE_PROFILE_ID, objectToCache);

    verifyExpectationsOfMockControlsWereMet();
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractCacheProviderFacadeImpl#putInCache(Serializable, String, Object)}</code>.
   * does not try to access the cache if the cache facade does not contain a
   * profile stored under the given id.
   */
  public void testPutInCacheWithNotExistingProfileId() throws Exception {
    expectCacheRequiresSerializableElements(false);

    Object objectToCache = new Object();

    setStateOfMockControlsToReplay();

    // execute the method to test.
    cacheProviderFacade.putInCache(cacheKey, "someCacheProfileId",
        objectToCache);

    verifyExpectationsOfMockControlsWereMet();
  }

  private void verifyExpectationsOfMockControlsWereMet() {
    cacheProfileEditorControl.verify();
    if (cacheProfileValidatorControl != null) {
      cacheProfileValidatorControl.verify();
    }
    cacheProviderFacadeControl.verify();
  }
}