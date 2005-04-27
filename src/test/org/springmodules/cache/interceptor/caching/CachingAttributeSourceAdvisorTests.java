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
 * Unit Test for <code>{@link CachingAttributeSourceAdvisor}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/04/27 01:41:12 $
 */
public final class CachingAttributeSourceAdvisorTests extends TestCase {

  /**
   * Primary object (instance of the class to test).
   */
  private CachingAttributeSourceAdvisor cachingAttributeSourceAdvisor;

  /**
   * Caching interceptor used only to obtain
   * <code>{@link #mockCachingAttributeSource}</code>.
   */
  private CachingInterceptor cachingInterceptor;

  /**
   * Method definition containing caching-attributes.
   */
  private Method method;

  /**
   * Mock object that simulates a search of caching-attributes for a given class
   * and method.
   */
  private CachingAttributeSource mockCachingAttributeSource;

  /**
   * Controls the behavior of <code>{@link #mockCachingAttributeSource}</code>.
   */
  private MockControl mockCachingAttributeSourceControl;

  /**
   * Class declaring <code>{@link #method}</code>.
   */
  private Class targetClass;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the Test Case.
   */
  public CachingAttributeSourceAdvisorTests(String name) {
    super(name);
  }

  /**
   * Sets up the test fixture.
   */
  protected void setUp() throws Exception {
    super.setUp();

    this.cachingInterceptor = new CachingInterceptor();
  }

  /**
   * Sets up:
   * <ul>
   * <li><code>{@link #cachingAttributeSourceAdvisor}</code></li>
   * <li><code>{@link #mockCachingAttributeSource}</code></li>
   * <li><code>{@link #mockCachingAttributeSourceControl}</code></li>
   * </ul>
   */
  private void setUpCachingAttributeSourceAdvisor() {

    this.mockCachingAttributeSourceControl = MockControl
        .createControl(CachingAttributeSource.class);
    this.mockCachingAttributeSource = (CachingAttributeSource) this.mockCachingAttributeSourceControl
        .getMock();

    this.cachingInterceptor
        .setCachingAttributeSource(this.mockCachingAttributeSource);

    this.cachingAttributeSourceAdvisor = new CachingAttributeSourceAdvisor(
        this.cachingInterceptor);
  }

  /**
   * Sets up:
   * <ul>
   * <li>{@link #targetClass}</li>
   * <li>{@link #method}</li>
   * </ul>
   */
  private void setUpTargetClassAndMethod() throws Exception {
    this.targetClass = String.class;
    this.method = this.targetClass.getMethod("charAt",
        new Class[] { int.class });
  }

  /**
   * Verifies that the constructor
   * <code>{@link CachingAttributeSourceAdvisor#CachingAttributeSourceAdvisor(CachingInterceptor)}</code>
   * throws a <code>AopConfigException</code> when the specified
   * <code>{@link CachingInterceptor}</code> does not contain a
   * <code>{@link CachingAttributeSource}</code>.
   */
  public void testConstructorWithMethodInterceptorNotHavingCachingAttributeSource() {
    this.cachingInterceptor.setCachingAttributeSource(null);

    try {
      this.cachingAttributeSourceAdvisor = new CachingAttributeSourceAdvisor(
          this.cachingInterceptor);
      fail("An 'AopConfigException' should have been thrown.");
    } catch (AopConfigException exception) {
      // we are expecting to catch an 'AopConfigException'.
    }
  }

  /**
   * Tests
   * <code>{@link CachingAttributeSourceAdvisor#matches(Method, Class)}</code>.
   * Verifies that <code>false</code> is returned if no instance of
   * <code>{@link Cached}</code> is returned by
   * <code>{@link #mockCachingAttributeSource}</code>.
   */
  public void testMatchesWhenAttributeIsNotFound() throws Exception {
    this.setUpCachingAttributeSourceAdvisor();
    this.setUpTargetClassAndMethod();

    // expectation: a caching-attribute should not be found for the specified
    // method and class.
    this.mockCachingAttributeSource.getCachingAttribute(this.method,
        this.targetClass);
    this.mockCachingAttributeSourceControl.setReturnValue(null);

    // set the state of the mock control to 'replay'.
    this.mockCachingAttributeSourceControl.replay();

    // execute the method to test.
    boolean matches = this.cachingAttributeSourceAdvisor.matches(this.method,
        this.targetClass);

    assertFalse("<CachingAttributeSourceAdvisor.matches(Method, Class)>",
        matches);

    // verify that the expectations of the mock control were met.
    this.mockCachingAttributeSourceControl.verify();
  }

  /**
   * Tests
   * <code>{@link CachingAttributeSourceAdvisor#matches(Method, Class)}</code>.
   * Verifies that <code>true</code> is returned if there is an instance of
   * <code>{@link Cached}</code> returned by
   * <code>{@link #mockCachingAttributeSource}</code>.
   */
  public void testMatchesWhenNotNullAttributeIsFound() throws Exception {
    this.setUpCachingAttributeSourceAdvisor();
    this.setUpTargetClassAndMethod();

    // expectation: a caching-attribute should be found for the specified method
    // and class.
    this.mockCachingAttributeSource.getCachingAttribute(this.method,
        this.targetClass);
    this.mockCachingAttributeSourceControl.setReturnValue(new Cached());

    // set the state of the mock control to 'replay'.
    this.mockCachingAttributeSourceControl.replay();

    // execute the method to test.
    boolean matches = this.cachingAttributeSourceAdvisor.matches(this.method,
        this.targetClass);

    assertTrue("<CachingAttributeSourceAdvisor.matches(Method, Class)>",
        matches);

    // verify that the expectations of the mock control were met.
    this.mockCachingAttributeSourceControl.verify();
  }
}