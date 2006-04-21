/* 
 * Created on Oct 26, 2005
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

import junit.framework.TestCase;

import org.springmodules.cache.CachingModel;
import org.springmodules.cache.FatalCacheException;
import org.springmodules.cache.mock.MockCachingModel;

/**
 * <p>
 * Unit Tests for <code>{@link MethodMapCachingInterceptor}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class MethodMapCachingInterceptorTests extends TestCase {

  private MethodMapCachingInterceptor interceptor;

  public MethodMapCachingInterceptorTests(String name) {
    super(name);
  }

  public void testOnAfterPropertiesSetWithCachingModelSourceEqualToNull()
      throws Exception {
    interceptor.setCachingModelSource(null);

    CachingModel model1 = new MockCachingModel();
    CachingModel model2 = new MockCachingModel();

    Map cachingModels = new HashMap();
    cachingModels.put("java.lang.String.indexO*", model1);
    cachingModels.put("java.lang.String.toUpperC*", model2);
    interceptor.setCachingModels(cachingModels);

    interceptor.onAfterPropertiesSet();
    CachingModelSource modelSource = interceptor.getCachingModelSource();
    assertNotNull(modelSource);
    assertEquals(MethodMapCachingModelSource.class, modelSource.getClass());

    MethodMapCachingModelSource source = (MethodMapCachingModelSource) modelSource;

    Class clazz = String.class;
    Method indexOf = clazz.getMethod("indexOf", new Class[] { int.class });
    Method toUpperCase = clazz.getMethod("toUpperCase", new Class[0]);

    assertSame(model1, source.model(indexOf, clazz));
    assertSame(model2, source.model(toUpperCase, clazz));
  }

  public void testOnAfterPropertiesSetWithCachingModelSourceEqualToNullAndNewModelSourceThrowingException() {
    interceptor.setCachingModelSource(null);

    Map cachingModels = new HashMap();
    cachingModels.put("indexO*", new MockCachingModel());
    interceptor.setCachingModels(cachingModels);

    try {
      interceptor.onAfterPropertiesSet();
      fail();
    } catch (FatalCacheException exception) {
      assertNotNull(exception.getCause());
    }
  }

  public void testOnAfterPropertiesSetWithCachingModelSourceNotNull() {
    CachingModelSource modelSource = new CachingModelSource() {
      public CachingModel model(Method method, Class targetClass) {
        return null;
      }
    };

    interceptor.setCachingModelSource(modelSource);
    interceptor.onAfterPropertiesSet();
    assertSame(modelSource, interceptor.getCachingModelSource());
  }

  protected void setUp() {
    interceptor = new MethodMapCachingInterceptor();
  }
}
