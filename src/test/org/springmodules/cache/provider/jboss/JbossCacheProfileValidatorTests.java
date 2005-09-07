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

package org.springmodules.cache.provider.jboss;

import junit.framework.TestCase;

import org.springmodules.cache.provider.InvalidCacheProfileException;

/**
 * <p>
 * Unit Tests for <code>{@link JbossCacheProfileValidator}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public final class JbossCacheProfileValidatorTests extends TestCase {

  private JbossCacheProfile cacheProfile;

  private JbossCacheProfileValidator cacheProfileValidator;

  public JbossCacheProfileValidatorTests(String name) {
    super(name);
  }

  private void assertValidateCacheProfilePropertiesThrowsException() {
    try {
      this.cacheProfileValidator
          .validateCacheProfileProperties(this.cacheProfile);
      fail();
    } catch (InvalidCacheProfileException exception) {
      // we are expecting this exception.
    }
  }

  protected void setUp() throws Exception {
    super.setUp();
    this.cacheProfile = new JbossCacheProfile();
    this.cacheProfileValidator = new JbossCacheProfileValidator();
  }

  public void testGetTargetClass() {
    assertEquals(JbossCacheProfile.class, this.cacheProfileValidator
        .getTargetClass());
  }

  public void testValidateCacheProfilePropertiesWithNodeFqnEqualToNull() {
    this.cacheProfile.setNodeFqn(null);
    assertValidateCacheProfilePropertiesThrowsException();
  }

  public void testValidateCacheProfilePropertiesWithEmptyNodeFqn() {
    this.cacheProfile.setNodeFqn("");
    assertValidateCacheProfilePropertiesThrowsException();
  }

  public void testValidateCacheProfilePropertiesWithNotEmptyNodeFqn() {
    this.cacheProfile.setNodeFqn("a/b/c");
    this.cacheProfileValidator
        .validateCacheProfileProperties(this.cacheProfile);
  }
}