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

import org.springmodules.cache.provider.InvalidCacheModelException;

/**
 * <p>
 * Unit Tests for <code>{@link EhCacheModelValidator}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public final class EhCacheModelValidatorTests extends TestCase {

  private EhCacheModel cacheModel;

  private EhCacheModelValidator cacheModelValidator;

  public EhCacheModelValidatorTests(String name) {
    super(name);
  }

  private void assertValidateCacheModelPropertiesThrowsException() {
    try {
      cacheModelValidator.validateCacheModelProperties(cacheModel);
      fail();
    } catch (InvalidCacheModelException exception) {
      // we are expecting this exception.
    }
  }

  protected void setUp() throws Exception {
    super.setUp();
    cacheModel = new EhCacheModel();
    cacheModelValidator = new EhCacheModelValidator();
  }

  public void testGetTargetClass() {
    assertEquals(EhCacheModel.class, cacheModelValidator.getTargetClass());
  }

  public void testValidateCacheModelPropertiesWithCacheNameEqualToNull() {
    cacheModel.setCacheName(null);
    assertValidateCacheModelPropertiesThrowsException();
  }

  public void testValidateCacheModelPropertiesWithEmptyCacheName() {
    cacheModel.setCacheName("");
    assertValidateCacheModelPropertiesThrowsException();
  }

  public void testValidateCacheModelPropertiesWithNotEmptyCacheName() {
    cacheModel.setCacheName("mapping");
    cacheModelValidator.validateCacheModelProperties(cacheModel);
  }
}