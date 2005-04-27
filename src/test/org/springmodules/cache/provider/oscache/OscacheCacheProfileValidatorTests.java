/* 
 * Created on Jan 12, 2005
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

import junit.framework.TestCase;

/**
 * <p>
 * Unit Test for <code>{@link OscacheCacheProfileValidator}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/04/27 01:41:49 $
 */
public final class OscacheCacheProfileValidatorTests extends TestCase {

  /**
   * Primary object (instance of the class to test).
   */
  private OscacheCacheProfileValidator cacheProfileValidator;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the Test Case.
   */
  public OscacheCacheProfileValidatorTests(String name) {
    super(name);
  }

  /**
   * Sets up the test fixture.
   */
  protected void setUp() {
    this.cacheProfileValidator = new OscacheCacheProfileValidator();
  }

  /**
   * Verifies that the method
   * <code>{@link OscacheCacheProfileValidator#validateCacheProfile(Object)}</code>
   * does not throw any exception if the profile to validate is an instance of
   * <code>{@link OscacheCacheProfile}</code>.
   */
  public void testValidateCacheProfileWithInstanceOfOscacheCacheProfile() {

    OscacheCacheProfile cacheProfile = new OscacheCacheProfile();
    Object object = cacheProfile;

    this.cacheProfileValidator.validateCacheProfile(object);
  }

  /**
   * Verifies that the method
   * <code>{@link OscacheCacheProfileValidator#validateCacheProfile(Object)}</code>
   * throws an <code>IllegalArgumentException</code> if the specified argument
   * is not an instance of <code>{@link OscacheCacheProfile}</code>.
   */
  public void testValidateCacheProfileObjectWithObjectNotInstanceOfOscacheCacheProfile() {
    try {
      this.cacheProfileValidator.validateCacheProfile(new Object());
      fail("An 'IllegalArgumentException' should have been thrown");
    } catch (IllegalArgumentException exception) {
      // we are expecting this exception.
    }
  }
}