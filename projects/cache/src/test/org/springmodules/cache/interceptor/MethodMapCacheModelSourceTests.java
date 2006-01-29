/* 
 * Created on Oct 25, 2005
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
package org.springmodules.cache.interceptor;

import java.lang.reflect.Method;
import java.util.Map;

import junit.framework.TestCase;

import org.springmodules.cache.CacheModel;
import org.springmodules.cache.mock.MockCachingModel;

/**
 * <p>
 * Unit Tests for <code>{@link AbstractMethodMapCacheModelSource}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class MethodMapCacheModelSourceTests extends TestCase {

  private class MethodMapCacheModelSource extends
      AbstractMethodMapCacheModelSource {
    // no extra implementation.
  }

  private MethodMapCacheModelSource source;

  public MethodMapCacheModelSourceTests(String name) {
    super(name);
  }

  public void testAddCacheModel() throws Exception {
    Method length = String.class.getMethod("length", new Class[0]);
    Map modelMap = source.getCacheModels();

    CacheModel model1 = new MockCachingModel();
    CacheModel model2 = new MockCachingModel();

    source.addCacheModel("java.lang.String.len*", model1);
    assertSame(model1, modelMap.get(length));

    source.addCacheModel("java.lang.String.lengt*", model2);
    assertSame(model2, modelMap.get(length));
  }

  public void testAddCacheModelWithFirstMethodNameBeingMoreSpecificThanSecond()
      throws Exception {
    Method toLowerCase = String.class.getMethod("toLowerCase", new Class[0]);
    Map modelMap = source.getCacheModels();

    CacheModel model1 = new MockCachingModel();
    CacheModel model2 = new MockCachingModel();

    source.addCacheModel("java.lang.String.toLowerCa*", model1);
    assertSame(model1, modelMap.get(toLowerCase));

    source.addCacheModel("java.lang.String.toLower*", model2);
    assertSame(model1, modelMap.get(toLowerCase));
  }

  public void testAddCacheModelWithMethodNameNotFullyQualified() {
    try {
      source.addCacheModel("indexOf", new MockCachingModel());
      fail();
    } catch (IllegalArgumentException exception) {
      // expecting exception.
    }
  }

  public void testAddCacheModelWithNotExistingClass() {
    try {
      source.addCacheModel("FakeClass.toString", new MockCachingModel());
      fail();
    } catch (IllegalArgumentException exception) {
      // expecting exception.
    }
  }

  public void testAddCacheModelWithNotMatchingMethod() {
    try {
      source.addCacheModel("java.lang.String.index", new MockCachingModel());
      fail();
    } catch (IllegalArgumentException exception) {
      // expecting exception.
    }
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractMethodMapCacheModelSource#getCacheModels()}</code>
   * returns an unmodifiable <code>Map</code>.
   */
  public void testGetModelMap() {
    try {
      source.getCacheModels().clear();
      fail();
    } catch (UnsupportedOperationException exception) {
      // expecting exception.
    }
  }

  protected void setUp() {
    source = new MethodMapCacheModelSource();
  }
}
