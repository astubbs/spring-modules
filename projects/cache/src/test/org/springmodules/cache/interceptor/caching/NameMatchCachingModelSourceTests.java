/* 
 * Created on Jan 18, 2005
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

package org.springmodules.cache.interceptor.caching;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.springmodules.cache.CachingModel;
import org.springmodules.cache.mock.MockCachingModel;

import junit.framework.TestCase;

/**
 * <p>
 * Unit Tests for <code>{@link NameMatchCachingModelSource}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class NameMatchCachingModelSourceTests extends TestCase {

  private Map models;

  private NameMatchCachingModelSource source;

  private Method method;

  public NameMatchCachingModelSourceTests(String name) {
    super(name);
  }

  protected void setUp() throws Exception {
    method = String.class.getDeclaredMethod("toUpperCase", null);
    models = new HashMap();

    source = new NameMatchCachingModelSource();
    source.setCachingModels(models);
  }

  public void testGetCachingModel() {
    CachingModel expected = new MockCachingModel();
    models.put(method.getName(), expected);

    assertSame(expected, source.getCachingModel(method, null));
  }
}