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

import junit.framework.TestCase;

import org.springmodules.cache.provider.InvalidCacheProfileException;

/**
 * <p>
 * Unit Tests for <code>{@link EhCacheProfileValidator}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.4 $ $Date: 2005/08/11 04:34:34 $
 */
public final class EhCacheProfileValidatorTests extends TestCase {

  private EhCacheProfile cacheProfile;

  /**
   * Primary object that is under test.
   */
  private EhCacheProfileValidator cacheProfileValidator;

  public EhCacheProfileValidatorTests(String name) {
    super(name);
  }

  private void assertValidateCacheProfilePropertiesThrowsException() {
    try {
      this.cacheProfileValidator
          .validateCacheProfileProperties(this.cacheProfile);
      fail("Expecting exception <"
          + InvalidCacheProfileException.class.getName() + ">");
    } catch (InvalidCacheProfileException exception) {
      // we are expecting this exception.
    }
  }

  protected void setUp() throws Exception {
    super.setUp();
    this.cacheProfile = new EhCacheProfile();
    this.cacheProfileValidator = new EhCacheProfileValidator();
  }

  public void testGetTargetClass() {
    Class expected = EhCacheProfile.class;
    Class actual = this.cacheProfileValidator.getTargetClass();

    assertEquals("<Target class>", expected, actual);
  }

  public void testValidateCacheProfilePropertiesWithCacheNameEqualToNull() {
    this.cacheProfile.setCacheName(null);
    this.assertValidateCacheProfilePropertiesThrowsException();
  }

  public void testValidateCacheProfilePropertiesWithEmptyCacheName() {
    this.cacheProfile.setCacheName("");
    this.assertValidateCacheProfilePropertiesThrowsException();
  }

  /**
   * Verifies that the method
   * <code>{@link EhCacheProfileValidator#validateCacheProfileProperties(Object)}</code>
   * does not throw a <code>{@link InvalidCacheProfileException}</code>
   * if the the cache name of the cache profile is not empty.
   */
  public void testValidateCacheProfilePropertiesWithNotEmptyCacheName() {
    this.cacheProfile.setCacheName("mapping");
    this.cacheProfileValidator
        .validateCacheProfileProperties(this.cacheProfile);
  }
}