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
 */
public final class EhCacheModelValidatorTests extends TestCase {

  private EhCacheCachingModel cachingModel;

  private EhCacheFlushingModel flushingModel;

  private EhCacheModelValidator validator;

  public EhCacheModelValidatorTests(String name) {
    super(name);
  }

  public void testValidateCachingModel() {
    cachingModel.setCacheName("mapping");
    validator.validateCachingModel(cachingModel);
  }

  public void testValidateCachingModelWithArgumentNotBeingCachingModel() {
    assertValidateCachingModelThrowsException("Anakin");
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
    flushingModel.setCacheNames("services,pojos");
    validator.validateFlushingModel(flushingModel);
  }

  public void testValidateFlushingModelWithArgumentNotBeingFlushingModel() {
    assertValidateFlushingModelThrowsException("Darth Vader");
  }

  public void testValidateFlushingModelWithCacheNamesCsvEqualToNull() {
    flushingModel.setCacheNames((String) null);
    assertValidateFlushingModelThrowsException();
  }

  public void testValidateFlushingModelWithEmptyCacheNamesCsv() {
    flushingModel.setCacheNames("");
    assertValidateFlushingModelThrowsException();
  }

  protected void setUp() {
    cachingModel = new EhCacheCachingModel();
    flushingModel = new EhCacheFlushingModel();
    validator = new EhCacheModelValidator();
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