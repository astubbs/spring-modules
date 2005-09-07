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

import org.springmodules.cache.provider.InvalidCacheProfileException;

import junit.framework.TestCase;

/**
 * <p>
 * Unit Tests for <code>{@link OsCacheProfileValidator}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.6 $ $Date: 2005/09/07 02:32:11 $
 */
public final class OsCacheProfileValidatorTests extends TestCase {

  private OsCacheProfileValidator cacheProfileValidator;

  public OsCacheProfileValidatorTests(String name) {
    super(name);
  }

  protected void setUp() {
    this.cacheProfileValidator = new OsCacheProfileValidator();
  }

  public void testValidateCacheProfileWithInstanceOfOscacheCacheProfile() {
    OsCacheProfile cacheProfile = new OsCacheProfile();
    Object object = cacheProfile;

    this.cacheProfileValidator.validateCacheProfile(object);
  }

  public void testValidateCacheProfileObjectWithObjectNotInstanceOfOscacheCacheProfile() {
    try {
      this.cacheProfileValidator.validateCacheProfile(new Object());
      fail();
    } catch (InvalidCacheProfileException exception) {
      // we are expecting this exception.
    }
  }
}