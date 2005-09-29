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

import org.springmodules.cache.provider.InvalidCacheModelException;

/**
 * <p>
 * Unit Tests for <code>{@link JbossCacheModelValidator}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public final class JbossCacheModelValidatorTests extends TestCase {

  private JbossCacheModel cacheModel;

  private JbossCacheModelValidator cacheModelValidator;

  public JbossCacheModelValidatorTests(String name) {
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
    cacheModel = new JbossCacheModel();
    cacheModelValidator = new JbossCacheModelValidator();
  }

  public void testGetTargetClass() {
    assertEquals(JbossCacheModel.class, cacheModelValidator.getTargetClass());
  }

  public void testValidateCacheModelPropertiesWithNodeFqnEqualToNull() {
    cacheModel.setNodeFqn(null);
    assertValidateCacheModelPropertiesThrowsException();
  }

  public void testValidateCacheModelPropertiesWithEmptyNodeFqn() {
    cacheModel.setNodeFqn("");
    assertValidateCacheModelPropertiesThrowsException();
  }

  public void testValidateCacheModelPropertiesWithNotEmptyNodeFqn() {
    cacheModel.setNodeFqn("a/b/c");
    cacheModelValidator.validateCacheModelProperties(cacheModel);
  }
}