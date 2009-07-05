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
package org.springmodules.cache.interceptor.flush;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.springmodules.cache.FatalCacheException;
import org.springmodules.cache.FlushingModel;
import org.springmodules.cache.mock.MockFlushingModel;

/**
 * <p>
 * Unit Tests for <code>{@link MethodMapFlushingInterceptor}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class MethodMapFlushingInterceptorTests extends TestCase {

  private MethodMapFlushingInterceptor interceptor;

  public MethodMapFlushingInterceptorTests(String name) {
    super(name);
  }

  public void testOnAfterPropertiesSetWithFlushingModelSourceEqualToNull()
      throws Exception {
    interceptor.setFlushingModelSource(null);

    FlushingModel model1 = new MockFlushingModel();
    FlushingModel model2 = new MockFlushingModel();

    Map flushingModels = new HashMap();
    flushingModels.put("java.lang.String.indexO*", model1);
    flushingModels.put("java.lang.String.toUpperC*", model2);
    interceptor.setFlushingModels(flushingModels);

    interceptor.onAfterPropertiesSet();
    FlushingModelSource modelSource = interceptor.getFlushingModelSource();
    assertNotNull(modelSource);
    assertEquals(MethodMapFlushingModelSource.class, modelSource.getClass());

    MethodMapFlushingModelSource source = (MethodMapFlushingModelSource) modelSource;

    Class clazz = String.class;
    Method indexOf = clazz.getMethod("indexOf", new Class[] { int.class });
    Method toUpperCase = clazz.getMethod("toUpperCase", new Class[0]);

    assertSame(model1, source.getFlushingModel(indexOf, clazz));
    assertSame(model2, source.getFlushingModel(toUpperCase, clazz));
  }

  public void testOnAfterPropertiesSetWithFlushingModelSourceEqualToNullAndNewModelSourceThrowingException() {
    interceptor.setFlushingModelSource(null);

    Map flushingModels = new HashMap();
    flushingModels.put("indexO*", new MockFlushingModel());
    interceptor.setFlushingModels(flushingModels);

    try {
      interceptor.onAfterPropertiesSet();
      fail();
    } catch (FatalCacheException exception) {
      assertNotNull(exception.getCause());
    }
  }

  public void testOnAfterPropertiesSetWithFlushingModelSourceNotNull() {
    FlushingModelSource modelSource = new FlushingModelSource() {
      public FlushingModel getFlushingModel(Method method, Class targetClass) {
        return null;
      }
    };

    interceptor.setFlushingModelSource(modelSource);
    interceptor.onAfterPropertiesSet();
    assertSame(modelSource, interceptor.getFlushingModelSource());
  }

  public void testOnAfterPropertiesSetWithFlushingModelSourceNotNullAndFlushingMapEqualToNull() {
    interceptor.setFlushingModels(null);
    FlushingModelSource modelSource = new FlushingModelSource() {
      public FlushingModel getFlushingModel(Method method, Class targetClass) {
        return null;
      }
    };

    interceptor.setFlushingModelSource(modelSource);
    interceptor.onAfterPropertiesSet();
    assertSame(modelSource, interceptor.getFlushingModelSource());
  }

  protected void setUp() {
    Map models = new HashMap();
    models.put("java.lang.String.toString", new MockFlushingModel());

    interceptor = new MethodMapFlushingInterceptor();
    interceptor.setFlushingModels(models);
  }
}
