/* 
 * Created on Oct 13, 2005
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

import org.springmodules.cache.mock.MockCachingModel;
import org.springmodules.cache.mock.MockFlushingModel;

/**
 * <p>
 * Unit Tests for <code>{@link AbstractCacheModelValidator}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class CacheModelValidatorTests extends TestCase {

  protected class CacheModelValidator extends AbstractCacheModelValidator {

    boolean cachingModelPropertiesValidated;

    Class cachingModelTargetClass;

    boolean flushingModelPropertiesValidated;

    Class flushingModelTargetClass;

    protected Class getCachingModelTargetClass() {
      return cachingModelTargetClass;
    }

    protected Class getFlushingModelTargetClass() {
      return flushingModelTargetClass;
    }

    protected void validateCachingModelProperties(Object newCachingModel)
        throws InvalidCacheModelException {
      cachingModelPropertiesValidated = true;
    }

    protected void validateFlushingModelProperties(Object newFlushingModel)
        throws InvalidCacheModelException {
      flushingModelPropertiesValidated = true;
    }

  }

  private CacheModelValidator validator;

  public CacheModelValidatorTests(String name) {
    super(name);
  }

  public void testIsInstaceOfWithObjectNotInstanceOfClass() {
    assertFalse(validator.isInstanceOf("Anakin", Integer.class));
  }

  public void testIsInstanceOf() {
    assertTrue(validator.isInstanceOf("Luke Skywalker", String.class));
  }

  public void testIsInstanceOfWithNull() {
    assertFalse(validator.isInstanceOf(null, Object.class));
  }

  public void testValidateCachingModel() {
    MockCachingModel model = new MockCachingModel();
    validator.cachingModelTargetClass = model.getClass();
    validator.validateCachingModel(model);
    assertTrue(validator.cachingModelPropertiesValidated);
  }

  public void testValidateCachingModelWithObjectNotInstanceOfClass() {
    validator.cachingModelTargetClass = MockCachingModel.class;

    try {
      validator.validateCachingModel("Princess Leia");
      fail();
    } catch (InvalidCacheModelException exception) {
      // expecting exception.
    }
  }

  public void testValidateFlushingModel() {
    MockFlushingModel model = new MockFlushingModel();
    validator.flushingModelTargetClass = model.getClass();
    validator.validateFlushingModel(model);
    assertTrue(validator.flushingModelPropertiesValidated);
  }

  public void testValidateFlushingModelWithObjectNotInstanceOfClass() {
    validator.flushingModelTargetClass = MockFlushingModel.class;

    try {
      validator.validateFlushingModel("Han Solo");
      fail();
    } catch (InvalidCacheModelException exception) {
      // expecting exception.
    }
  }

  protected void setUp() {
    validator = new CacheModelValidator();
  }
}
