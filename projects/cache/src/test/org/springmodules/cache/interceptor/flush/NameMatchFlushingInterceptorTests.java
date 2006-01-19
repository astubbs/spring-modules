/* 
 * Created on Oct 9, 2005
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

import org.springmodules.cache.FlushingModel;
import org.springmodules.cache.mock.MockFlushingModel;

/**
 * <p>
 * Unit Tests for <code>{@link NameMatchFlushingInterceptor}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class NameMatchFlushingInterceptorTests extends TestCase {

  private NameMatchFlushingInterceptor interceptor;

  public NameMatchFlushingInterceptorTests(String name) {
    super(name);
  }

  public void testOnAfterPropertiesSetWithFlushingModelSourceEqualToNullAndEmptyFlushingModelMap() {
    interceptor.setFlushingModels(new HashMap());
    interceptor.setFlushingModelSource(null);

    interceptor.onAfterPropertiesSet();
    assertNull(interceptor.getFlushingModelSource());
  }

  public void testOnAfterPropertiesSetWithFlushingModelSourceEqualToNullAndFlushingModelMapEqualToNull() {
    interceptor.setFlushingModels(null);
    interceptor.setFlushingModelSource(null);

    interceptor.onAfterPropertiesSet();
    assertNull(interceptor.getFlushingModelSource());
  }

  public void testOnAfterPropertiesSetWithFlushingModelSourceEqualToNullAndFlushingModels() {
    Map models = createNotEmptyModelMap();
    interceptor.setFlushingModels(models);
    interceptor.setFlushingModelSource(null);

    interceptor.onAfterPropertiesSet();
    FlushingModelSource modelSource = interceptor.getFlushingModelSource();
    assertNotNull(modelSource);
    assertEquals(NameMatchFlushingModelSource.class, modelSource.getClass());

  }

  public void testOnAfterPropertiesSetWithFlushingModelSourceNotNullAndFlushingModels() {
    Map models = createNotEmptyModelMap();
    interceptor.setFlushingModels(models);

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
    interceptor = new NameMatchFlushingInterceptor();
  }

  private Map createNotEmptyModelMap() {
    FlushingModel model = new MockFlushingModel();
    Map models = new HashMap();
    models.put("get*", model);
    return models;
  }
}
