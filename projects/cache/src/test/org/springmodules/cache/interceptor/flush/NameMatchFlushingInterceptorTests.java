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
import java.util.Properties;

import junit.framework.TestCase;

import org.aopalliance.intercept.MethodInvocation;
import org.easymock.MockControl;
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

  private void assertOnAfterPropertiesSetDoesNotCreateModelSource(Map models) {
    interceptor.setFlushingModels(models);
    interceptor.onAfterPropertiesSet();
    assertNull(interceptor.getFlushingModelSource());
  }

  protected void setUp() {
    interceptor = new NameMatchFlushingInterceptor();
  }

  public void testGetModel() throws Exception {
    MockControl sourceControl = MockControl
        .createStrictControl(FlushingModelSource.class);
    FlushingModelSource source = (FlushingModelSource) sourceControl.getMock();

    MockControl invocationControl = MockControl
        .createStrictControl(MethodInvocation.class);
    MethodInvocation invocation = (MethodInvocation) invocationControl
        .getMock();
    interceptor.setFlushingModelSource(source);

    Object thisObject = "Anakin";
    invocation.getThis();
    invocationControl.setReturnValue(thisObject);

    Method method = String.class.getDeclaredMethod("toUpperCase", null);
    invocation.getMethod();
    invocationControl.setReturnValue(method);

    FlushingModel expected = new MockFlushingModel();
    source.getFlushingModel(method, thisObject.getClass());
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
    interceptor.setFlushingModels(models);

    interceptor.onAfterPropertiesSet();
    FlushingModelSource source = interceptor.getFlushingModelSource();

    assertEquals(NameMatchFlushingModelSource.class, source.getClass());
    NameMatchFlushingModelSource nameMatchSource = (NameMatchFlushingModelSource) source;
    assertSame(models, nameMatchSource.getFlushingModels());
  }

  public void testOnAfterPropertiesSetWithEmptyModelMap() {
    assertOnAfterPropertiesSetDoesNotCreateModelSource(new HashMap());
  }

  public void testOnAfterPropertiesSetWithModelMapEqualToNull() {
    assertOnAfterPropertiesSetDoesNotCreateModelSource(new HashMap());
  }
}
