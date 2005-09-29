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
import org.springmodules.cache.mock.MockCacheModel;

/**
 * <p>
 * Unit Tests for <code>{@link AbstractCacheProviderFacade}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public final class CacheProviderFacadeTests extends TestCase {

  private static final String CACHE_MODEL_ID = "CACHE_MODEL_ID";

  /**
   * Key of the cached object to be retrieved from
   * <code>{@link #cacheProviderFacade}</code>.
   */
  private Serializable cacheKey;

  /**
   * Cache model to be stored in <code>{@link #cacheModelMap}</code> using the
   * key <code>{@link #CACHE_MODEL_ID}</code>.
   */
  private CacheModel cacheModel;

  private PropertyEditor cacheModelEditor;

  private MockControl cacheModelEditorControl;

  private Map cacheModelMap;

  private CacheModelValidator cacheModelValidator;

  private MockControl cacheModelValidatorControl;

  private AbstractCacheProviderFacade cacheProviderFacade;

  private MockClassControl cacheProviderFacadeControl;

  public CacheProviderFacadeTests(String name) {
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

  private void expectGetCacheModelEditor() {
    cacheProviderFacade.getCacheModelEditor();
    cacheProviderFacadeControl.setReturnValue(cacheModelEditor);
  }

  private void expectGetCacheModelValidator() {
    cacheProviderFacade.getCacheModelValidator();
    cacheProviderFacadeControl.setReturnValue(cacheModelValidator);
  }

  private CacheException getNewCacheException() {
    return new CacheNotFoundException("someCache");
  }

  private InvalidCacheModelException getNewInvalidCacheModelException() {
    return new InvalidCacheModelException(
        "This exception should not make the test fail");
  }

  private void setStateOfMockControlsToReplay() {
    cacheModelEditorControl.replay();
    if (cacheModelValidatorControl != null) {
      cacheModelValidatorControl.replay();
    }
    cacheProviderFacadeControl.replay();
  }

  protected void setUp() throws Exception {
    super.setUp();

    setUpCacheProviderFacadeAsMockObject();
    setUpCacheModelEditorAsMockObject();

    cacheKey = "Key";
    cacheModel = new MockCacheModel();
    cacheModelMap = new HashMap();
    cacheModelMap.put(CACHE_MODEL_ID, cacheModel);
    cacheProviderFacade.setCacheModels(cacheModelMap);
  }

  private void setUpCacheModelEditorAsMockObject() {
    cacheModelEditorControl = MockControl.createControl(PropertyEditor.class);
    cacheModelEditor = (PropertyEditor) cacheModelEditorControl.getMock();
  }

  private void setUpCacheModelValidatorAsMockObject() {
    cacheModelValidatorControl = MockControl
        .createControl(CacheModelValidator.class);

    cacheModelValidator = (CacheModelValidator) cacheModelValidatorControl
        .getMock();
  }

  private void setUpCacheProviderFacadeAsMockObject() throws Exception {
    Class classToMock = AbstractCacheProviderFacade.class;

    Method getCacheModelEditorMethod = classToMock.getDeclaredMethod(
        "getCacheModelEditor", null);

    Method getCacheModelValidatorMethod = classToMock.getDeclaredMethod(
        "getCacheModelValidator", null);

    Method isSerializableCacheElementRequiredMethod = classToMock
        .getDeclaredMethod("isSerializableCacheElementRequired", null);

    Method onCancelCacheUpdateMethod = classToMock.getDeclaredMethod(
        "onCancelCacheUpdate", new Class[] { Serializable.class });

    Method onFlushCacheMethod = classToMock.getDeclaredMethod("onFlushCache",
        new Class[] { CacheModel.class });

    Method onGetFromCacheMethod = classToMock.getDeclaredMethod(
        "onGetFromCache", new Class[] { Serializable.class, CacheModel.class });

    Method onPutInCacheMethod = classToMock.getDeclaredMethod("onPutInCache",
        new Class[] { Serializable.class, CacheModel.class, Object.class });

    Method validateCacheManagerMethod = classToMock.getDeclaredMethod(
        "validateCacheManager", null);

    Method[] methodsToMock = new Method[] { getCacheModelEditorMethod,
        getCacheModelValidatorMethod, isSerializableCacheElementRequiredMethod,
        onCancelCacheUpdateMethod, onFlushCacheMethod, onGetFromCacheMethod,
        onPutInCacheMethod, validateCacheManagerMethod };

    cacheProviderFacadeControl = MockClassControl.createControl(classToMock,
        null, null, methodsToMock);

    cacheProviderFacade = (AbstractCacheProviderFacade) cacheProviderFacadeControl
        .getMock();
  }

  public void testAfterPropertiesSetValidatesCacheManager() throws Exception {
    setUpCacheModelValidatorAsMockObject();

    // validate cache manager.
    cacheProviderFacade.validateCacheManager();

    // validate the cache model(s).
    expectGetCacheModelValidator();
    cacheModelValidator.validateCacheModel(cacheModel);

    setStateOfMockControlsToReplay();

    // execute the method to test.
    cacheProviderFacade.afterPropertiesSet();

    verifyExpectationsOfMockControlsWereMet();
  }

  public void testAfterPropertiesSetWhenMapOfCacheModelsIsEmpty() {
    cacheModelMap.clear();
    assertAfterPropertiesSetThrowsException();
  }

  public void testAfterPropertiesSetWhenMapOfCacheModelsIsEqualToNull() {
    cacheProviderFacade.setCacheModels(null);
    assertAfterPropertiesSetThrowsException();
  }

  public void testAfterPropertiesSetWhenMapOfCacheModelsIsProperties()
      throws Exception {
    setUpCacheModelValidatorAsMockObject();

    this.cacheProviderFacade.validateCacheManager();

    String cacheModelId = "main";
    String cacheModelPropertiesAsText = "[firstName=Luke]";

    Properties properties = new Properties();
    properties.setProperty(cacheModelId, cacheModelPropertiesAsText);

    cacheProviderFacade.setCacheModels(properties);

    expectGetCacheModelEditor();
    cacheModelEditor.setAsText(cacheModelPropertiesAsText);
    cacheModelEditor.getValue();
    cacheModelEditorControl.setReturnValue(cacheModel);

    expectGetCacheModelValidator();
    cacheModelValidator.validateCacheModel(cacheModel);

    setStateOfMockControlsToReplay();

    // execute the method to test.
    cacheProviderFacade.afterPropertiesSet();

    assertSame(cacheModel, cacheProviderFacade.getCacheModels().get(
        cacheModelId));

    verifyExpectationsOfMockControlsWereMet();
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractCacheProviderFacade#afterPropertiesSet()}</code>
   * throws a <code>{@link FatalCacheException}</code> wrapping any exception
   * thrown by the <code>{@link CacheModelValidator}</code>.
   */
  public void testAfterPropertiesSetWhenValidationOfCacheModelsThrowsException()
      throws Exception {
    setUpCacheModelValidatorAsMockObject();

    // validate cache manager.
    cacheProviderFacade.validateCacheManager();

    // the cache model is invalid.
    InvalidCacheModelException expectedNestedException = this
        .getNewInvalidCacheModelException();
    expectGetCacheModelValidator();
    cacheModelValidator.validateCacheModel(cacheModel);
    cacheModelValidatorControl.setThrowable(expectedNestedException);

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

  public void testAssertCacheManagerIsNotNullWithCacheManagerEqualToNull() {
    try {
      cacheProviderFacade.assertCacheManagerIsNotNull(null);
      fail();
    } catch (FatalCacheException exception) {
      // we are expecting this exception.
    }
  }

  public void testAssertCacheManagerIsNotNullWithCacheManagerNotEqualToNull() {
    cacheProviderFacade.assertCacheManagerIsNotNull(new Object());
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
    String[] cacheModelIds = new String[] { CACHE_MODEL_ID };
    cacheProviderFacade.setFailQuietlyEnabled(false);

    // facade is unable to flush the cache.
    CacheException expectedException = getNewCacheException();
    cacheProviderFacade.onFlushCache(cacheModel);
    cacheProviderFacadeControl.setThrowable(expectedException);

    setStateOfMockControlsToReplay();

    try {
      // execute the method to test.
      cacheProviderFacade.flushCache(cacheModelIds);
      fail();

    } catch (CacheException exception) {
      assertSame(expectedException, exception);
    }

    verifyExpectationsOfMockControlsWereMet();
  }

  public void testFlushCacheWhenAccessToCacheThrowsExceptionAndFailQuietlyIsTrue()
      throws Exception {
    String[] cacheModelIds = { CACHE_MODEL_ID };
    cacheProviderFacade.setFailQuietlyEnabled(true);

    // facade is unable to flush the cache.
    CacheException cacheException = getNewCacheException();
    cacheProviderFacade.onFlushCache(cacheModel);
    cacheProviderFacadeControl.setThrowable(cacheException);

    setStateOfMockControlsToReplay();

    // execute the method to test.
    cacheProviderFacade.flushCache(cacheModelIds);

    verifyExpectationsOfMockControlsWereMet();
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractCacheProviderFacade#flushCache(String[])}</code>
   * does not flush the cache if the array of cache model ids is equal to
   * <code>null</code>.
   */
  public void testFlushCacheWhenArrayOfModelIdsIsEqualToNull() throws Exception {
    String[] cacheModelIds = null;

    setStateOfMockControlsToReplay();

    // execute the method to test.
    cacheProviderFacade.flushCache(cacheModelIds);

    verifyExpectationsOfMockControlsWereMet();
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractCacheProviderFacade#flushCache(String[])}</code>
   * does not flush the cache if the array of cache model ids is empty.
   */
  public void testFlushCacheWhenModelIdsIsEmpty() throws Exception {
    String[] cacheModelIds = new String[0];

    setStateOfMockControlsToReplay();

    // execute the method to test.
    cacheProviderFacade.flushCache(cacheModelIds);

    verifyExpectationsOfMockControlsWereMet();
  }

  public void testFlushCacheWithNotEmptyArrayOfCacheModelIdsAndExistingCacheModelId()
      throws Exception {
    String[] cacheModelIds = new String[] { CACHE_MODEL_ID };

    // flush the cache.
    cacheProviderFacade.onFlushCache(cacheModel);

    setStateOfMockControlsToReplay();

    // execute the method to test.
    cacheProviderFacade.flushCache(cacheModelIds);

    verifyExpectationsOfMockControlsWereMet();
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractCacheProviderFacade#flushCache(String[])}</code>
   * does not flush the cache if the specified cache models cannot be found.
   */
  public void testFlushCacheWithNotEmptyArrayOfCacheModelIdsAndNotExistingCacheModelId()
      throws Exception {
    String[] cacheModelIds = new String[] { "anotherCacheModelId" };

    setStateOfMockControlsToReplay();

    // execute the method to test.
    cacheProviderFacade.flushCache(cacheModelIds);

    verifyExpectationsOfMockControlsWereMet();
  }

  public void testGetCacheModelsReturnsAnUnmodifiableMap() {
    Map unmodifiableMap = cacheProviderFacade.getCacheModels();

    try {
      unmodifiableMap.clear();
      fail();

    } catch (UnsupportedOperationException exception) {
      // we are expecting this exception.
    }
  }

  public void testGetCacheModelWhenCacheModelIdIsNotEmpty() {
    CacheModel actual = cacheProviderFacade.getCacheModel(CACHE_MODEL_ID);
    assertSame(cacheModel, actual);
  }

  public void testGetCacheModelWhenCacheModelIdIsNull() {
    CacheModel actual = cacheProviderFacade.getCacheModel(null);
    assertNull(actual);
  }

  public void testGetCacheModelWithCacheModelIdIsEmpty() {
    CacheModel actual = cacheProviderFacade.getCacheModel("");
    assertNull(actual);
  }

  public void testGetFromCacheWhenAccessToCacheThrowsExceptionAndFailQuietlyIsFalse()
      throws Exception {
    cacheProviderFacade.setFailQuietlyEnabled(false);

    // facade is unable to get an entry from the cache.
    CacheException expectedException = getNewCacheException();
    cacheProviderFacade.onGetFromCache(cacheKey, cacheModel);
    cacheProviderFacadeControl.setThrowable(expectedException);

    setStateOfMockControlsToReplay();

    try {
      // execute the method to test.
      cacheProviderFacade.getFromCache(cacheKey, CACHE_MODEL_ID);
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
    cacheProviderFacade.onGetFromCache(cacheKey, cacheModel);
    cacheProviderFacadeControl.setThrowable(new CacheNotFoundException(
        "testCache"));

    setStateOfMockControlsToReplay();

    // execute the method to test.
    Object cachedObject = cacheProviderFacade.getFromCache(cacheKey,
        CACHE_MODEL_ID);

    assertNull(cachedObject);

    verifyExpectationsOfMockControlsWereMet();
  }

  public void testGetFromCacheWithExistingCacheModelId() throws Exception {
    String cachedString = "Cached String";

    // get an entry from the cache.
    cacheProviderFacade.onGetFromCache(cacheKey, cacheModel);
    cacheProviderFacadeControl.setReturnValue(cachedString);

    setStateOfMockControlsToReplay();

    // execute the method to test.
    Object cachedObject = cacheProviderFacade.getFromCache(cacheKey,
        CACHE_MODEL_ID);

    assertSame("<Cached object>", cachedString, cachedObject);

    verifyExpectationsOfMockControlsWereMet();
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractCacheProviderFacade#getFromCache(Serializable, String)}</code>
   * does not try to access the cache if the cache facade does not contain a
   * model stored under the given id.
   */
  public void testGetFromCacheWithNotExistingCacheModelId() throws Exception {
    setStateOfMockControlsToReplay();

    // execute the method to test.
    Object cachedObject = cacheProviderFacade.getFromCache(cacheKey,
        "anotherCacheModelId");

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
    cacheProviderFacade.onPutInCache(cacheKey, cacheModel, objectToCache);
    cacheProviderFacadeControl.setThrowable(expectedException);

    setStateOfMockControlsToReplay();

    try {
      // execute the method to test.
      cacheProviderFacade.putInCache(cacheKey, CACHE_MODEL_ID, objectToCache);
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
    cacheProviderFacade.onPutInCache(cacheKey, cacheModel, objectToCache);
    cacheProviderFacadeControl.setThrowable(new CacheNotFoundException(
        "testCache"));

    setStateOfMockControlsToReplay();

    // execute the method to test.
    cacheProviderFacade.putInCache(cacheKey, CACHE_MODEL_ID, objectToCache);

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
      cacheProviderFacade.putInCache(cacheKey, CACHE_MODEL_ID, objectToCache);
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

    cacheProviderFacade.putInCache(cacheKey, CACHE_MODEL_ID, objectToCache);

    verifyExpectationsOfMockControlsWereMet();
  }

  public void testPutInCacheWithExistingCacheModelId() throws Exception {
    expectCacheRequiresSerializableElements(false);

    Object objectToCache = new Object();

    cacheProviderFacade.onPutInCache(cacheKey, cacheModel, objectToCache);

    setStateOfMockControlsToReplay();

    // execute the method to test.
    cacheProviderFacade.putInCache(cacheKey, CACHE_MODEL_ID, objectToCache);

    verifyExpectationsOfMockControlsWereMet();
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractCacheProviderFacade#putInCache(Serializable, String, Object)}</code>.
   * does not try to access the cache if the cache facade does not contain a
   * cache model stored under the given id.
   */
  public void testPutInCacheWithNotExistingCacheModelId() throws Exception {
    expectCacheRequiresSerializableElements(false);

    Object objectToCache = new Object();

    setStateOfMockControlsToReplay();

    // execute the method to test.
    cacheProviderFacade.putInCache(cacheKey, "someCacheModelId", objectToCache);

    verifyExpectationsOfMockControlsWereMet();
  }

  private void verifyExpectationsOfMockControlsWereMet() {
    cacheModelEditorControl.verify();
    if (cacheModelValidatorControl != null) {
      cacheModelValidatorControl.verify();
    }
    cacheProviderFacadeControl.verify();
  }
}