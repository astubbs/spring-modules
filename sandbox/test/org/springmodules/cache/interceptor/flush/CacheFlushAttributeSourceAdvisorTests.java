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
 * Unit Test for <code>{@link CacheFlushAttributeSourceAdvisor}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/04/22 02:19:18 $
 */
public class CacheFlushAttributeSourceAdvisorTests extends TestCase {

  /**
   * Primary object (instance of the class to test).
   */
  private CacheFlushAttributeSourceAdvisor cacheFlushAttributeSourceAdvisor;

  /**
   * Caching interceptor used only to obtain
   * <code>{@link #mockCacheFlushAttributeSource}</code>.
   */
  private CacheFlushInterceptor cacheFlushInterceptor;

  /**
   * Method definition containing cache-flush-attributes.
   */
  private Method method;

  /**
   * Mock object that simulates a search of cache-flush-attributes for a given
   * class and method.
   */
  private CacheFlushAttributeSource mockCacheFlushAttributeSource;

  /**
   * Controls the behavior of
   * <code>{@link #mockCacheFlushAttributeSource}</code>.
   */
  private MockControl mockCacheFlushAttributeSourceControl;

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
  public CacheFlushAttributeSourceAdvisorTests(String name) {
    super(name);
  }

  /**
   * Sets up the test fixture.
   */
  protected void setUp() throws Exception {
    super.setUp();

    this.cacheFlushInterceptor = new CacheFlushInterceptor();
  }

  /**
   * Sets up:
   * <ul>
   * <li><code>{@link #cacheFlushAttributeSourceAdvisor}</code></li>
   * <li><code>{@link #mockCacheFlushAttributeSource}</code></li>
   * <li><code>{@link #mockCacheFlushAttributeSourceControl}</code></li>
   * </ul>
   */
  private void setUpCachingAttributeSourceAdvisor() {

    this.mockCacheFlushAttributeSourceControl = MockControl
        .createControl(CacheFlushAttributeSource.class);
    this.mockCacheFlushAttributeSource = (CacheFlushAttributeSource) this.mockCacheFlushAttributeSourceControl
        .getMock();

    this.cacheFlushInterceptor
        .setCacheFlushAttributeSource(this.mockCacheFlushAttributeSource);

    this.cacheFlushAttributeSourceAdvisor = new CacheFlushAttributeSourceAdvisor(
        this.cacheFlushInterceptor);
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
   * <code>{@link CacheFlushAttributeSourceAdvisor#CacheFlushAttributeSourceAdvisor(CacheFlushInterceptor)}</code>.
   * throws a <code>AopConfigException</code> when the specified
   * <code>{@link CacheFlushInterceptor}</code> does not contain a
   * <code>{@link CacheFlushAttributeSource}</code>.
   */
  public void testConstructorWithMethodInterceptorNotHavingCacheFlushAttributeSource() {
    this.cacheFlushInterceptor.setCacheFlushAttributeSource(null);

    try {
      this.cacheFlushAttributeSourceAdvisor = new CacheFlushAttributeSourceAdvisor(
          this.cacheFlushInterceptor);
      fail("An 'AopConfigException' should have been thrown.");
    } catch (AopConfigException exception) {
      // we are expecting to catch an 'AopConfigException'.
    }
  }

  /**
   * Tests
   * <code>{@link CacheFlushAttributeSourceAdvisor#matches(Method, Class)}</code>.
   * Verifies that <code>false</code> is returned if no instance of
   * <code>{@link FlushCache}</code> is returned by
   * <code>{@link #mockCacheFlushAttributeSource}</code>.
   */
  public void testMatchesWhenAttributeIsNotFound() throws Exception {
    this.setUpCachingAttributeSourceAdvisor();
    this.setUpTargetClassAndMethod();

    // expectation: a cache-flush-attribute should not be found for the
    // specified method and class.
    this.mockCacheFlushAttributeSource.getCacheFlushAttribute(this.method,
        this.targetClass);
    this.mockCacheFlushAttributeSourceControl.setReturnValue(null);

    // set the state of the mock control to 'replay'.
    this.mockCacheFlushAttributeSourceControl.replay();

    boolean matches = this.cacheFlushAttributeSourceAdvisor.matches(
        this.method, this.targetClass);

    // execute the method to test.
    this.mockCacheFlushAttributeSourceControl.verify();
    assertFalse("<CacheFlushAttributeSourceAdvisor.matches(Method, Class)>",
        matches);

    // verify that the expectations of the mock control were met.
    this.mockCacheFlushAttributeSourceControl.verify();
  }

  /**
   * Tests
   * <code>{@link CacheFlushAttributeSourceAdvisor#matches(Method, Class)}</code>.
   * Verifies that <code>true</code> is returned if there is an instance of
   * <code>{@link FlushCache}</code> returned by
   * <code>{@link #mockCacheFlushAttributeSource}</code>.
   */
  public void testMatchesWhenNotNullAttributeIsFound() throws Exception {
    this.setUpCachingAttributeSourceAdvisor();
    this.setUpTargetClassAndMethod();

    // expectation: a cache-flush-attribute should be found for the specified
    // method and class.
    this.mockCacheFlushAttributeSource.getCacheFlushAttribute(this.method,
        this.targetClass);
    this.mockCacheFlushAttributeSourceControl.setReturnValue(new FlushCache());

    // set the state of the mock control to 'replay'.
    this.mockCacheFlushAttributeSourceControl.replay();

    boolean matches = this.cacheFlushAttributeSourceAdvisor.matches(
        this.method, this.targetClass);

    // execute the method to test.
    this.mockCacheFlushAttributeSourceControl.verify();
    assertTrue("<CacheFlushAttributeSourceAdvisor.matches(Method, Class)>",
        matches);

    // verify that the expectations of the mock control were met.
    this.mockCacheFlushAttributeSourceControl.verify();
  }
}