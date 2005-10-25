/* 
 * Created on Sep 1, 2005
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
 */
public final class JbossCacheModelValidatorTests extends TestCase {

  private JbossCacheCachingModel cachingModel;

  private JbossCacheFlushingModel flushingModel;

  private JbossCacheModelValidator validator;

  public JbossCacheModelValidatorTests(String name) {
    super(name);
  }

  private void assertValidateCachingModelThrowsException() {
    assertValidateCachingModelThrowsException(cachingModel);
  }

  private void assertValidateCachingModelThrowsException(Object model) {
    try {
      validator.validateCachingModel(model);
      fail();
    } catch (InvalidCacheModelException exception) {
      // we are expecting this exception.
    }
  }

  private void assertValidateFlushingModelThrowsException() {
    assertValidateFlushingModelThrowsException(flushingModel);
  }

  private void assertValidateFlushingModelThrowsException(Object model) {
    try {
      validator.validateFlushingModel(model);
      fail();
    } catch (InvalidCacheModelException exception) {
      // we are expecting this exception.
    }
  }

  protected void setUp() {
    cachingModel = new JbossCacheCachingModel();
    flushingModel = new JbossCacheFlushingModel();
    validator = new JbossCacheModelValidator();
  }

  public void testValidateCachingModel() {
    cachingModel.setNode("a/b");
    validator.validateCachingModel(cachingModel);
  }

  public void testValidateCachingModelWithArgumentNotBeingCachingModel() {
    assertValidateCachingModelThrowsException("Han Solo");
  }

  public void testValidateCachingModelWithEmptyNodeFqn() {
    cachingModel.setNode("");
    assertValidateCachingModelThrowsException();
  }

  public void testValidateCachingModelWithNodeFqnEqualToNull() {
    cachingModel.setNode(null);
    assertValidateCachingModelThrowsException();
  }

  public void testValidateFlushingModel() {
    flushingModel.setNodes("a/b/c,a/b/c/d");
    validator.validateFlushingModel(flushingModel);
  }

  public void testValidateFlushingModelWithArgumentNotBeingFlushingModel() {
    assertValidateFlushingModelThrowsException("Lando");
  }

  public void testValidateFlushingModelWithEmptyNodesCsv() {
    flushingModel.setNodes("");
    assertValidateFlushingModelThrowsException();
  }

  public void testValidateFlushingModelWithNodesCsvEqualToNull() {
    flushingModel.setNodes((String) null);
    assertValidateFlushingModelThrowsException();
  }
}