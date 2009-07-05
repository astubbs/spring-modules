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

package org.springmodules.cache.provider.jcs;

import junit.framework.TestCase;

import org.springmodules.cache.provider.InvalidCacheModelException;
import org.springmodules.cache.provider.jcs.JcsFlushingModel.CacheStruct;

/**
 * <p>
 * Unit Tests for <code>{@link JcsModelValidator}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public final class JcsModelValidatorTests extends TestCase {

  private JcsCachingModel cachingModel;

  private JcsFlushingModel flushingModel;

  private JcsModelValidator validator;

  public JcsModelValidatorTests(String name) {
    super(name);
  }

  public void testValidateCachingModel() {
    cachingModel.setCacheName("main");
    validator.validateCachingModel(cachingModel);
  }

  public void testValidateCachingModelWithArgumentNotBeingCachingModel() {
    assertValidateCachingModelThrowsException("Endor");
  }

  public void testValidateCachingModelWithCacheNameEqualToNull() {
    cachingModel.setCacheName(null);
    assertValidateCachingModelThrowsException();
  }

  public void testValidateCachingModelWithEmptyCacheName() {
    cachingModel.setCacheName("");
    assertValidateCachingModelThrowsException();
  }

  public void testValidateFlushingModel() {
    flushingModel.setCacheStruct(new CacheStruct("test"));
    validator.validateFlushingModel(flushingModel);
  }

  public void testValidateFlushingModelWithArgumentNotBeingFlushingModel() {
    assertValidateFlushingModelThrowsException("Ewok");
  }

  public void testValidateFlushingModelWithCacheStructArrayEqualToNull() {
    flushingModel.setCacheStructs((CacheStruct[]) null);
    assertValidateFlushingModelThrowsException();
  }

  public void testValidateFlushingModelWithCacheStructArrayNotHavingCacheName() {
    flushingModel.setCacheStruct(new CacheStruct());
    assertValidateFlushingModelThrowsException();
  }

  public void testValidateFlushingModelWithEmptyCacheStructArray() {
    flushingModel.setCacheStructs(new CacheStruct[0]);
    assertValidateFlushingModelThrowsException();
  }

  protected void setUp() {
    cachingModel = new JcsCachingModel();
    flushingModel = new JcsFlushingModel();
    validator = new JcsModelValidator();
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
}