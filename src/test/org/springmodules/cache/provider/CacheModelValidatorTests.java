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
 * Unit Tests for <code>{@link AbstractCacheModelValidator}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public final class CacheModelValidatorTests extends TestCase {

  private class MockCacheModelValidator extends AbstractCacheModelValidator {

    InvalidCacheModelException exceptionToThrow;

    Class targetClass;

    /**
     * @see AbstractCacheModelValidator#getTargetClass()
     */
    protected Class getTargetClass() {
      return targetClass;
    }

    /**
     * @see AbstractCacheModelValidator#validateCacheModelProperties(java.lang.Object)
     */
    protected void validateCacheModelProperties(Object cacheModel)
        throws InvalidCacheModelException {
      if (exceptionToThrow != null) {
        // this exception is used to verify that this method was called.
        throw exceptionToThrow;
      }
    }
  }

  private MockCacheModelValidator validator;

  public CacheModelValidatorTests(String name) {
    super(name);
  }

  private void assertValidateCacheModelThrowsInvalidCacheModelException(
      Object cacheModel) {
    try {
      validator.validateCacheModel(cacheModel);
      fail();

    } catch (InvalidCacheModelException exception) {
      // we are expecting this exception.
    }
  }

  protected void setUp() throws Exception {
    super.setUp();

    validator = new MockCacheModelValidator();
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractCacheModelValidator#validateCacheModel(Object)}</code>
   * validates that the type and the properties of the given cache model.
   */
  public void testValidateCacheModel() {
    Object cacheModel = "A fake cache model";
    InvalidCacheModelException expectedException = new InvalidCacheModelException(
        "A fake exception");

    validator.targetClass = cacheModel.getClass();
    validator.exceptionToThrow = expectedException;

    try {
      validator.validateCacheModel(cacheModel);

    } catch (InvalidCacheModelException exception) {
      assertSame("The properties of the cache model were not validated",
          expectedException, exception);
    }
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractCacheModelValidator#validateCacheModel(Object)}</code>
   * throws a <code>{@link InvalidCacheModelException}</code> if the given
   * cache model is equal to <code>null</code>.
   */
  public void testValidateCacheModelWithCacheModelEqualToNull() {
    assertValidateCacheModelThrowsInvalidCacheModelException(null);
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractCacheModelValidator#validateCacheModel(Object)}</code>
   * throws a <code>{@link InvalidCacheModelException}</code> if the class
   * of the given cache model is not equal to the target class.
   */
  public void testValidateCacheModelWithCacheModelHavingClassNotEqualToTargetClass() {
    Object cacheModel = "Another fake cache model";
    validator.targetClass = Integer.class;
    assertValidateCacheModelThrowsInvalidCacheModelException(cacheModel);
  }
}
