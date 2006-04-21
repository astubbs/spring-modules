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
package org.springmodules.cache.interceptor.caching;

import java.lang.reflect.Method;

import junit.framework.TestCase;

import org.springmodules.cache.CachingModel;

/**
 * <p>
 * Unit Tests for <code>{@link NameMatchCachingInterceptor}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class NameMatchCachingInterceptorTests extends TestCase {

  private NameMatchCachingInterceptor interceptor;

  public NameMatchCachingInterceptorTests(String name) {
    super(name);
  }

  public void testOnAfterPropertiesSetWithCachingModelSourceEqualToNull() {
    interceptor.setCachingModelSource(null);

    interceptor.onAfterPropertiesSet();
    CachingModelSource modelSource = interceptor.getCachingModelSource();
    assertNotNull(modelSource);
    assertEquals(NameMatchCachingModelSource.class, modelSource.getClass());
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
    interceptor = new NameMatchCachingInterceptor();
  }
}
