/* 
 * Created on Sep 23, 2004
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

package org.springmodules.cache.interceptor.caching;

import java.lang.reflect.Method;

import junit.framework.TestCase;

import org.easymock.MockControl;

import org.springframework.aop.framework.AopConfigException;

/**
 * <p>
 * Unit Tests for <code>{@link CachingAttributeSourceAdvisor}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public final class CachingAttributeSourceAdvisorTests extends TestCase {

  private CachingAttributeSourceAdvisor advisor;

  /**
   * Interceptor used only to obtain <code>{@link #source}</code>.
   */
  private MetadataCachingInterceptor interceptor;

  /**
   * Method definition containing metadata attributes.
   */
  private Method method;

  private CachingAttributeSource source;

  private MockControl sourceControl;

  /**
   * Class declaring <code>{@link #method}</code>.
   */
  private Class targetClass;

  public CachingAttributeSourceAdvisorTests(String name) {
    super(name);
  }

  /**
   * Verifies that the constructor
   * <code>{@link CachingAttributeSourceAdvisor#CachingAttributeSourceAdvisor(MetadataCachingInterceptor)}</code>
   * throws a <code>AopConfigException</code> when the argument
   * <code>{@link MetadataCachingInterceptor}</code> does not contain a
   * <code>{@link CachingAttributeSource}</code>.
   */
  public void testConstructorWithMethodInterceptorNotHavingCachingAttributeSource() {
    interceptor.setCachingAttributeSource(null);

    try {
      advisor = new CachingAttributeSourceAdvisor(interceptor);
      fail();

    } catch (AopConfigException exception) {
      // we are expecting this exception.
    }
  }

  public void testMatchesWhenAttributeIsNotFound() throws Exception {
    setUpCachingAttributeSourceAsMockObject();
    setUpTargetClassAndMethod();

    // a caching attribute should not be found for the specified method and
    // class.
    sourceControl.expectAndReturn(source.attribute(method,
        targetClass), null);

    sourceControl.replay();

    assertFalse(advisor.matches(method, targetClass));

    sourceControl.verify();
  }

  public void testMatchesWhenNotNullAttributeIsFound() throws Exception {
    setUpCachingAttributeSourceAsMockObject();
    setUpTargetClassAndMethod();

    // a caching attribute should be found for the specified method and class.
    sourceControl.expectAndReturn(source.attribute(method,
        targetClass), new Cached());

    sourceControl.replay();

    assertTrue(advisor.matches(method, targetClass));

    sourceControl.verify();
  }

  protected void setUp() {
    interceptor = new MetadataCachingInterceptor();
  }

  private void setUpCachingAttributeSourceAsMockObject() {
    sourceControl = MockControl.createControl(CachingAttributeSource.class);
    source = (CachingAttributeSource) sourceControl.getMock();

    interceptor.setCachingAttributeSource(source);

    advisor = new CachingAttributeSourceAdvisor(interceptor);
  }

  private void setUpTargetClassAndMethod() throws Exception {
    targetClass = String.class;
    method = targetClass.getMethod("charAt", new Class[] { int.class });
  }
}