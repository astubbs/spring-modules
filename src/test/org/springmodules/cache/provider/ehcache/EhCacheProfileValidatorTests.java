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
 * @version $Revision: 1.7 $ $Date: 2005/09/09 02:19:11 $
 */
public final class EhCacheProfileValidatorTests extends TestCase {

  private EhCacheProfile cacheProfile;

  private EhCacheProfileValidator cacheProfileValidator;

  public EhCacheProfileValidatorTests(String name) {
    super(name);
  }

  private void assertValidateCacheProfilePropertiesThrowsException() {
    try {
      cacheProfileValidator.validateCacheProfileProperties(cacheProfile);
      fail();
    } catch (InvalidCacheProfileException exception) {
      // we are expecting this exception.
    }
  }

  protected void setUp() throws Exception {
    super.setUp();
    cacheProfile = new EhCacheProfile();
    cacheProfileValidator = new EhCacheProfileValidator();
  }

  public void testGetTargetClass() {
    assertEquals(EhCacheProfile.class, cacheProfileValidator.getTargetClass());
  }

  public void testValidateCacheProfilePropertiesWithCacheNameEqualToNull() {
    cacheProfile.setCacheName(null);
    assertValidateCacheProfilePropertiesThrowsException();
  }

  public void testValidateCacheProfilePropertiesWithEmptyCacheName() {
    cacheProfile.setCacheName("");
    assertValidateCacheProfilePropertiesThrowsException();
  }

  public void testValidateCacheProfilePropertiesWithNotEmptyCacheName() {
    cacheProfile.setCacheName("mapping");
    cacheProfileValidator.validateCacheProfileProperties(cacheProfile);
  }
}