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

import junit.framework.TestCase;

import org.easymock.MockControl;

import org.springframework.aop.framework.AopConfigException;

import org.springmodules.cache.mock.MockFlushingModel;

/**
 * <p>
 * Unit Tests for <code>{@link FlushingModelSourceAdvisor}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class FlushingModelSourceAdvisorTests extends TestCase {

  private FlushingModelSourceAdvisor advisor;

  private NameMatchFlushingInterceptor interceptor;

  private FlushingModelSource modelSource;

  private MockControl modelSourceControl;

  public FlushingModelSourceAdvisorTests(String name) {
    super(name);
  }

  public void testFlushingModelSourceAdvisorWithInterceptorNotHavingModelSource() {
    interceptor.setFlushingModelSource(null);

    try {
      new FlushingModelSourceAdvisor(interceptor);
      fail();
    } catch (AopConfigException exception) {
      // expecting exception.
    }
  }

  public void testMatchesWithModelSourceReturningModel() throws Exception {
    advisor = new FlushingModelSourceAdvisor(interceptor);

    Method method = defaultMethod();
    Class targetClass = method.getDeclaringClass();
    modelSourceControl.expectAndReturn(modelSource.getFlushingModel(method,
        targetClass), new MockFlushingModel());

    modelSourceControl.replay();

    assertTrue(advisor.matches(method, targetClass));

    modelSourceControl.verify();
  }

  public void testMatchesWithModelSourceReturningNull() throws Exception {
    advisor = new FlushingModelSourceAdvisor(interceptor);

    Method method = defaultMethod();
    Class targetClass = method.getDeclaringClass();
    modelSourceControl.expectAndReturn(modelSource.getFlushingModel(method,
        targetClass), null);

    modelSourceControl.replay();

    assertFalse(advisor.matches(method, targetClass));

    modelSourceControl.verify();
  }

  protected void setUp() {
    modelSourceControl = MockControl.createControl(FlushingModelSource.class);
    modelSource = (FlushingModelSource) modelSourceControl.getMock();

    interceptor = new NameMatchFlushingInterceptor();
    interceptor.setFlushingModelSource(modelSource);
  }

  private Method defaultMethod() throws Exception {
    return String.class.getDeclaredMethod("toLowerCase", new Class[0]);
  }
}
