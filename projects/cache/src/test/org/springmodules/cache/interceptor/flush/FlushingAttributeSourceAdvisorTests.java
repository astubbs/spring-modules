/* 
 * Created on Oct 22, 2004
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
 * Copyright @2004 the original author or authors.
 */

package org.springmodules.cache.interceptor.flush;

import java.lang.reflect.Method;

import junit.framework.TestCase;

import org.easymock.MockControl;

import org.springframework.aop.framework.AopConfigException;

/**
 * <p>
 * Unit Tests for <code>{@link FlushingAttributeSourceAdvisor}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class FlushingAttributeSourceAdvisorTests extends TestCase {

  /**
   * Interceptor used only to obtain <code>{@link #source}</code>.
   */
  private MetadataFlushingInterceptor interceptor;

  /**
   * Method definition containing metadata attributes.
   */
  private Method method;

  private FlushingAttributeSource source;

  private FlushingAttributeSourceAdvisor sourceAdvisor;

  private MockControl sourceControl;

  /**
   * Class declaring <code>{@link #method}</code>.
   */
  private Class targetClass;

  public FlushingAttributeSourceAdvisorTests(String name) {
    super(name);
  }

  public void testConstructorWithMethodInterceptorNotHavingCacheFlushAttributeSource() {
    interceptor.setFlushingAttributeSource(null);
    try {
      sourceAdvisor = new FlushingAttributeSourceAdvisor(interceptor);
      fail();
    } catch (AopConfigException exception) {
      // we are expecting this exception.
    }
  }

  public void testMatchesWhenAttributeIsNotFound() throws Exception {
    setUpCachingAttributeSourceAdvisorAsMockObject();
    setUpTargetClassAndMethod();

    // metadata attributes not be found for the specified method and class.
    sourceControl.expectAndReturn(source.attribute(method,
        targetClass), null);
    sourceControl.replay();

    assertFalse(sourceAdvisor.matches(method, targetClass));
    sourceControl.verify();
  }

  /**
   * Tests
   * <code>{@link FlushingAttributeSourceAdvisor#matches(Method, Class)}</code>.
   * Verifies that <code>true</code> is returned if there is an instance of
   * <code>{@link FlushCache}</code> returned by <code>{@link #source}</code>.
   */
  public void testMatchesWhenNotNullAttributeIsFound() throws Exception {
    setUpCachingAttributeSourceAdvisorAsMockObject();
    setUpTargetClassAndMethod();

    // metadata attributes not be found for the specified method and class.
    sourceControl.expectAndReturn(source.attribute(method,
        targetClass), new FlushCache());
    sourceControl.replay();

    assertTrue(sourceAdvisor.matches(method, targetClass));
    sourceControl.verify();
  }

  protected void setUp() {
    interceptor = new MetadataFlushingInterceptor();
  }

  private void setUpCachingAttributeSourceAdvisorAsMockObject() {
    sourceControl = MockControl.createControl(FlushingAttributeSource.class);
    source = (FlushingAttributeSource) sourceControl.getMock();

    interceptor.setFlushingAttributeSource(source);
    sourceAdvisor = new FlushingAttributeSourceAdvisor(interceptor);
  }

  private void setUpTargetClassAndMethod() throws Exception {
    targetClass = String.class;
    method = targetClass.getMethod("toUpperCase", new Class[0]);
  }
}