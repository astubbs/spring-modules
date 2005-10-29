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

import org.aopalliance.intercept.MethodInvocation;
import org.easymock.MockControl;
import org.springmodules.cache.FlushingModel;
import org.springmodules.cache.mock.MockFlushingModel;

import junit.framework.TestCase;

/**
 * <p>
 * Unit Tests for <code>{@link AbstractModelSourceFlushingInterceptor}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class ModelSourceFlushingInterceptorTests extends TestCase {

  private class ModelSourceFlushingInterceptor extends
      AbstractModelSourceFlushingInterceptor {
    // no extra implementation
  }

  private ModelSourceFlushingInterceptor interceptor;

  private MethodInvocation invocation;

  private MockControl invocationControl;

  private FlushingModelSource source;

  private MockControl sourceControl;

  public ModelSourceFlushingInterceptorTests(String name) {
    super(name);
  }

  private Method defaultMethod() throws Exception {
    return String.class.getDeclaredMethod("toLowerCase", null);
  }

  private void replayMocks() {
    invocationControl.replay();
    sourceControl.replay();
  }

  protected void setUp() {
    sourceControl = MockControl.createControl(FlushingModelSource.class);
    source = (FlushingModelSource) sourceControl.getMock();

    interceptor = new ModelSourceFlushingInterceptor();
    interceptor.setFlushingModelSource(source);

    invocationControl = MockControl.createStrictControl(MethodInvocation.class);
    invocation = (MethodInvocation) invocationControl.getMock();
  }

  public void testGetModel() throws Exception {
    Object thisObject = "Anakin";
    invocationControl.expectAndReturn(invocation.getThis(), thisObject);

    Method method = defaultMethod();
    invocationControl.expectAndReturn(invocation.getMethod(), method);

    FlushingModel expected = new MockFlushingModel();
    sourceControl.expectAndReturn(source.getFlushingModel(method, thisObject
        .getClass()), expected);

    replayMocks();

    assertSame(expected, interceptor.getModel(invocation));
    verifyMocks();
  }

  public void testGetModelWhenThisObjectIsNull() throws Exception {
    invocationControl.expectAndReturn(invocation.getThis(), null);

    Method method = defaultMethod();
    invocationControl.expectAndReturn(invocation.getMethod(), method);

    FlushingModel expected = new MockFlushingModel();
    sourceControl.expectAndReturn(source.getFlushingModel(method, null),
        expected);

    replayMocks();

    assertSame(expected, interceptor.getModel(invocation));
    verifyMocks();
  }

  private void verifyMocks() {
    invocationControl.verify();
    sourceControl.verify();
  }

}
