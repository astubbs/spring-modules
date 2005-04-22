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
 * Unit Test for <code>{@link EhcacheCacheProfileValidator}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/04/22 02:18:58 $
 */
public final class EhcacheCacheProfileValidatorTests extends TestCase {

  /**
   * Primary object (instance of the class to test).
   */
  private EhcacheCacheProfileValidator cacheProfileValidator;

  /**
   * Controls the behavior of <code>{@link #cacheProfileValidator}</code>.
   */
  private MockClassControl cacheProfileValidatorControl;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the Test Case.
   */
  public EhcacheCacheProfileValidatorTests(String name) {
    super(name);
  }

  /**
   * Sets up <code>{@link #cacheProfileValidator}</code>.
   */
  private void setUpCacheProfileValidator() {
    this.cacheProfileValidator = new EhcacheCacheProfileValidator();
  }

  /**
   * Sets up:
   * <ul>
   * <li><code>{@link #cacheProfileValidator}</code> (as a mock object)</li>
   * <li><code>{@link #cacheProfileValidatorControl}</code></li>
   * </ul>
   * 
   * @param methodsToMock
   *          the methods to mock.
   */
  private void setUpCacheProfileValidatorAsMockObject(Method[] methodsToMock) {
    Class classToMock = EhcacheCacheProfileValidator.class;

    this.cacheProfileValidatorControl = MockClassControl.createControl(
        classToMock, null, null, methodsToMock);
    this.cacheProfileValidator = (EhcacheCacheProfileValidator) this.cacheProfileValidatorControl
        .getMock();
  }

  /**
   * Verifies that the method
   * <code>{@link EhcacheCacheProfileValidator#validateCacheName(String)}</code>
   * considers an empty String as an invalid cache name.
   */
  public void testValidateCacheNameWithEmptyString() {
    this.setUpCacheProfileValidator();

    try {
      this.cacheProfileValidator.validateCacheName("");
      fail("An IllegalArgumentException should have been thrown");
    } catch (IllegalArgumentException exception) {
      // we are expecting this exception.
    }
  }

  /**
   * Verifies that the method
   * <code>{@link EhcacheCacheProfileValidator#validateCacheName(String)}</code>
   * considers a String that is not empty as a valid cache name.
   */
  public void testValidateCacheNameWithNotEmptyString() {
    this.setUpCacheProfileValidator();

    String cacheName = "CacheName";
    this.cacheProfileValidator.validateCacheName(cacheName);
  }

  /**
   * Verifies that the method
   * <code>{@link EhcacheCacheProfileValidator#validateCacheName(String)}</code>
   * considers a String equal to <code>null</code> as an invalid cache name.
   */
  public void testValidateCacheNameWithStringEqualToNull() {
    this.setUpCacheProfileValidator();

    try {
      this.cacheProfileValidator.validateCacheName(null);
      fail("An IllegalArgumentException should have been thrown");
    } catch (IllegalArgumentException exception) {
      // we are expecting this exception.
    }
  }

  /**
   * Verifies that the method
   * <code>{@link EhcacheCacheProfileValidator#validateCacheProfile(EhcacheCacheProfile)}</code>.
   * validates the name of the cache set in the specified cache profile.
   */
  public void testValidateCacheProfile() throws Exception {
    // set up the methods to mock.
    Class classToMock = EhcacheCacheProfileValidator.class;
    Method validateCacheNameMethod = classToMock.getDeclaredMethod(
        "validateCacheName", new Class[] { String.class });
    Method[] methodsToMock = new Method[] { validateCacheNameMethod };

    // create the validator as a mock object. We need to mock
    // 'validateCacheName' to make sure we are executing it.
    this.setUpCacheProfileValidatorAsMockObject(methodsToMock);

    String cacheName = "CacheName";
    EhcacheCacheProfile cacheProfile = new EhcacheCacheProfile();
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
   * <code>{@link EhcacheCacheProfileValidator#validateCacheProfile(Object)}</code>
   * throws an <code>IllegalArgumentException</code> if the specified argument
   * is not an instance of <code>{@link EhcacheCacheProfile}</code>.
   */
  public void testValidateCacheProfileObjectWithObjectNotInstanceOfEhcacheCacheProfile() {
    this.setUpCacheProfileValidator();

    try {
      this.cacheProfileValidator.validateCacheProfile(new Object());
      fail("An IllegalArgumentException should have been thrown");
    } catch (IllegalArgumentException exception) {
      // we are expecting this exception.
    }
  }

}