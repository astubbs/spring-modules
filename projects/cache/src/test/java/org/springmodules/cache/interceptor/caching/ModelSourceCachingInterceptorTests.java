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

import junit.framework.TestCase;

import org.aopalliance.intercept.MethodInvocation;
import org.easymock.MockControl;

import org.springmodules.cache.CachingModel;
import org.springmodules.cache.mock.MockCachingModel;

/**
 * <p>
 * Unit Tests for <code>{@link AbstractModelSourceCachingInterceptor}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class ModelSourceCachingInterceptorTests extends TestCase {

  protected class ModelSourceCachingInterceptor extends
      AbstractModelSourceCachingInterceptor {
    // no extra implementation
  }

  private ModelSourceCachingInterceptor interceptor;

  private MethodInvocation invocation;

  private MockControl invocationControl;

  private CachingModelSource source;

  private MockControl sourceControl;

  public ModelSourceCachingInterceptorTests(String name) {
    super(name);
  }

  public void testGetModel() throws Exception {
    Object thisObject = "Anakin";
    invocationControl.expectAndReturn(invocation.getThis(), thisObject);

    Method method = defaultMethod();
    invocationControl.expectAndReturn(invocation.getMethod(), method);

    CachingModel expected = new MockCachingModel();
    sourceControl.expectAndReturn(source.model(method, thisObject
        .getClass()), expected);

    replayMocks();

    assertSame(expected, interceptor.model(invocation));
    verifyMocks();
  }

  public void testGetModelWhenThisObjectIsNull() throws Exception {
    invocationControl.expectAndReturn(invocation.getThis(), null);

    Method method = defaultMethod();
    invocationControl.expectAndReturn(invocation.getMethod(), method);

    CachingModel expected = new MockCachingModel();
    sourceControl.expectAndReturn(source.model(method, null),
        expected);

    replayMocks();

    assertSame(expected, interceptor.model(invocation));
    verifyMocks();
  }

  protected void setUp() {
    sourceControl = MockControl.createControl(CachingModelSource.class);
    source = (CachingModelSource) sourceControl.getMock();

    interceptor = new ModelSourceCachingInterceptor();
    interceptor.setCachingModelSource(source);

    invocationControl = MockControl.createStrictControl(MethodInvocation.class);
    invocation = (MethodInvocation) invocationControl.getMock();
  }

  private Method defaultMethod() throws Exception {
    return String.class.getDeclaredMethod("toLowerCase", new Class[0]);
  }

  private void replayMocks() {
    invocationControl.replay();
    sourceControl.replay();
  }

  private void verifyMocks() {
    invocationControl.verify();
    sourceControl.verify();
  }

}
