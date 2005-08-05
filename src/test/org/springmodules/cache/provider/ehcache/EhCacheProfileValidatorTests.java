/* 
 * Created on Jan 14, 2005
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

package org.springmodules.cache.provider.ehcache;

import java.lang.reflect.Method;

import junit.framework.TestCase;

import org.easymock.classextension.MockClassControl;

/**
 * <p>
 * Unit Tests for <code>{@link EhCacheProfileValidator}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.2 $ $Date: 2005/08/05 02:18:52 $
 */
public final class EhCacheProfileValidatorTests extends TestCase {

  /**
   * Primary object that is under test.
   */
  private EhCacheProfileValidator cacheProfileValidator;

  private MockClassControl cacheProfileValidatorControl;

  public EhCacheProfileValidatorTests(String name) {
    super(name);
  }

  private void assertValidateCacheNameThrowsIllegalArgumentException(
      String cacheName) {
    Class expectedException = IllegalArgumentException.class;

    try {
      this.cacheProfileValidator.validateCacheName(cacheName);
      fail("Expecting exception <" + expectedException + ">");
    } catch (IllegalArgumentException exception) {
      // we are expecting this exception.
    }
  }

  private void assertValidateCacheProfileThrowsIllegalArgumentException(
      Object cacheProfile) {
    Class expectedException = IllegalArgumentException.class;

    try {
      this.cacheProfileValidator.validateCacheProfile(cacheProfile);
      fail("Expecting exception <" + expectedException + ">");
    } catch (IllegalArgumentException exception) {
      // we are expecting this exception.
    }
  }

  private void setUpCacheProfileValidator() {
    this.cacheProfileValidator = new EhCacheProfileValidator();
  }

  private void setUpCacheProfileValidatorAsMockObject(Method[] methodsToMock) {
    Class classToMock = EhCacheProfileValidator.class;

    this.cacheProfileValidatorControl = MockClassControl.createControl(
        classToMock, null, null, methodsToMock);
    this.cacheProfileValidator = (EhCacheProfileValidator) this.cacheProfileValidatorControl
        .getMock();
  }

  /**
   * Verifies that the method
   * <code>{@link EhCacheProfileValidator#validateCacheName(String)}</code>
   * considers an empty String as an invalid cache name.
   */
  public void testValidateCacheNameWithEmptyString() {
    this.setUpCacheProfileValidator();
    this.assertValidateCacheNameThrowsIllegalArgumentException("");
  }

  /**
   * Verifies that the method
   * <code>{@link EhCacheProfileValidator#validateCacheName(String)}</code>
   * considers a String that is not empty as a valid cache name.
   */
  public void testValidateCacheNameWithNotEmptyString() {
    this.setUpCacheProfileValidator();

    String cacheName = "CacheName";
    this.cacheProfileValidator.validateCacheName(cacheName);
  }

  /**
   * Verifies that the method
   * <code>{@link EhCacheProfileValidator#validateCacheName(String)}</code>
   * considers a String equal to <code>null</code> as an invalid cache name.
   */
  public void testValidateCacheNameWithStringEqualToNull() {
    this.setUpCacheProfileValidator();
    this.assertValidateCacheNameThrowsIllegalArgumentException(null);
  }

  /**
   * Verifies that the method
   * <code>{@link EhCacheProfileValidator#validateCacheProfile(EhCacheProfile)}</code>.
   * validates the name of the cache set in the specified cache profile.
   */
  public void testValidateCacheProfile() throws Exception {
    // set up the methods to mock.
    Class classToMock = EhCacheProfileValidator.class;
    Method validateCacheNameMethod = classToMock.getDeclaredMethod(
        "validateCacheName", new Class[] { String.class });
    Method[] methodsToMock = new Method[] { validateCacheNameMethod };

    // create the validator as a mock object. We need to mock
    // 'validateCacheName' to make sure we are executing it.
    this.setUpCacheProfileValidatorAsMockObject(methodsToMock);

    String cacheName = "CacheName";
    EhCacheProfile cacheProfile = new EhCacheProfile();
    cacheProfile.setCacheName(cacheName);

    // expectation: validate the cache name.
    this.cacheProfileValidator.validateCacheName(cacheName);

    // set the state of the mock control to 'replay'.
    this.cacheProfileValidatorControl.replay();

    // execute the method to test.
    this.cacheProfileValidator.validateCacheProfile(cacheProfile);

    // verify that the expectations of the mock control were met.
    this.cacheProfileValidatorControl.verify();
  }

  /**
   * Verifies that the method
   * <code>{@link EhCacheProfileValidator#validateCacheProfile(Object)}</code>
   * throws an <code>IllegalArgumentException</code> if the specified argument
   * is not an instance of <code>{@link EhCacheProfile}</code>.
   */
  public void testValidateCacheProfileObjectWithObjectNotInstanceOfEhcacheCacheProfile() {
    this.setUpCacheProfileValidator();
    this.assertValidateCacheProfileThrowsIllegalArgumentException(new Object());
  }

}