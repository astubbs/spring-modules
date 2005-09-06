/* 
 * Created on Aug 5, 2005
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
package org.springmodules.cache.provider;

import junit.framework.TestCase;

/**
 * <p>
 * Unit Tests for <code>{@link AbstractCacheProfileValidator}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public final class CacheProfileValidatorTests extends TestCase {

  private class MockCacheProfileValidator extends AbstractCacheProfileValidator {

    InvalidCacheProfileException exceptionToThrow;

    Class targetClass;

    /**
     * @see AbstractCacheProfileValidator#getTargetClass()
     */
    protected Class getTargetClass() {
      return this.targetClass;
    }

    /**
     * @see AbstractCacheProfileValidator#validateCacheProfileProperties(java.lang.Object)
     */
    protected void validateCacheProfileProperties(Object cacheProfile)
        throws InvalidCacheProfileException {
      if (this.exceptionToThrow != null) {
        // this exception is used to verify that this method was called.
        throw this.exceptionToThrow;
      }
    }
  }

  private MockCacheProfileValidator validator;

  public CacheProfileValidatorTests(String name) {
    super(name);
  }

  private void assertValidateCacheProfileThrowsInvalidCacheProfileException(
      Object cacheProfile) {
    try {
      this.validator.validateCacheProfile(cacheProfile);
      fail("Expecting exception <"
          + InvalidCacheProfileException.class.getName() + ">");
    } catch (InvalidCacheProfileException exception) {
      // we are expecting this exception.
    }
  }

  protected void setUp() throws Exception {
    super.setUp();

    this.validator = new MockCacheProfileValidator();
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractCacheProfileValidator#validateCacheProfile(Object)}</code>
   * validates that the type and the properties of the given cache profile.
   */
  public void testValidateCacheProfile() {
    Object cacheProfile = "A fake cache profile";
    InvalidCacheProfileException expectedException = new InvalidCacheProfileException(
        "A fake exception");

    this.validator.targetClass = cacheProfile.getClass();
    this.validator.exceptionToThrow = expectedException;

    try {
      this.validator.validateCacheProfile(cacheProfile);

    } catch (InvalidCacheProfileException exception) {
      assertSame("The properties of the cahe profile were not validated",
          expectedException, exception);
    }
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractCacheProfileValidator#validateCacheProfile(Object)}</code>
   * throws a <code>{@link InvalidCacheProfileException}</code> if the given
   * cache profile is equal to <code>null</code>.
   */
  public void testValidateCacheProfileWithCacheProfileEqualToNull() {
    assertValidateCacheProfileThrowsInvalidCacheProfileException(null);
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractCacheProfileValidator#validateCacheProfile(Object)}</code>
   * throws a <code>{@link InvalidCacheProfileException}</code> if the class
   * of the given cache profile is not equal to the target class.
   */
  public void testValidateCacheProfileWithCacheProfileHavingClassNotEqualToTargetClass() {
    Object cacheProfile = "Another fake cache profile";
    this.validator.targetClass = Integer.class;
    assertValidateCacheProfileThrowsInvalidCacheProfileException(cacheProfile);
  }
}
