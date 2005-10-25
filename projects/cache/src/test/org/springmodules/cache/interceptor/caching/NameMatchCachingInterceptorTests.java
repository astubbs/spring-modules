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
import java.util.Properties;

import junit.framework.TestCase;

import org.aopalliance.intercept.MethodInvocation;
import org.easymock.MockControl;
import org.springmodules.cache.CachingModel;
import org.springmodules.cache.mock.MockCachingModel;

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

  protected void setUp() {
    interceptor = new NameMatchCachingInterceptor();
  }

  public void testGetModel() throws Exception {
    MockControl sourceControl = MockControl
        .createStrictControl(CachingModelSource.class);
    CachingModelSource source = (CachingModelSource) sourceControl.getMock();
    interceptor.setCachingModelSource(source);

    MockControl invocationControl = MockControl
        .createStrictControl(MethodInvocation.class);
    MethodInvocation invocation = (MethodInvocation) invocationControl
        .getMock();

    Object thisObject = "Anakin";
    invocation.getThis();
    invocationControl.setReturnValue(thisObject);

    Method method = String.class.getDeclaredMethod("toUpperCase", null);
    invocation.getMethod();
    invocationControl.setReturnValue(method);

    CachingModel expected = new MockCachingModel();
    source.getCachingModel(method, thisObject.getClass());
    sourceControl.setReturnValue(expected);

    sourceControl.replay();
    invocationControl.replay();

    assertSame(expected, interceptor.getModel(invocation));
    sourceControl.verify();
    invocationControl.verify();
  }

  public void testOnAfterPropertiesSet() {
    Properties models = new Properties();
    models.setProperty("get*", "cacheName=main");
    interceptor.setCachingModels(models);

    interceptor.onAfterPropertiesSet();
    CachingModelSource source = interceptor.getCachingModelSource();

    assertEquals(NameMatchCachingModelSource.class, source.getClass());
    NameMatchCachingModelSource nameMatchSource = (NameMatchCachingModelSource) source;
    assertSame(models, nameMatchSource.getCachingModels());
  }
}
