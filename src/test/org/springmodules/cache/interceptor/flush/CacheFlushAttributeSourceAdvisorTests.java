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
 * Unit Tests for <code>{@link CacheFlushAttributeSourceAdvisor}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.3 $ $Date: 2005/08/05 02:45:16 $
 */
public class CacheFlushAttributeSourceAdvisorTests extends TestCase {

  private CacheFlushAttributeSource cacheFlushAttributeSource;

  /**
   * Primary object that is under test.
   */
  private CacheFlushAttributeSourceAdvisor cacheFlushAttributeSourceAdvisor;

  private MockControl cacheFlushAttributeSourceAdvisorControl;

  /**
   * Caching interceptor used only to obtain
   * <code>{@link #cacheFlushAttributeSource}</code>.
   */
  private CacheFlushInterceptor cacheFlushInterceptor;

  /**
   * Method definition containing cache-flush attributes.
   */
  private Method method;

  /**
   * Class declaring <code>{@link #method}</code>.
   */
  private Class targetClass;

  public CacheFlushAttributeSourceAdvisorTests(String name) {
    super(name);
  }

  /**
   * 
   */
  private void setStateOfMockControlsToReplay() {
    this.cacheFlushAttributeSourceAdvisorControl.replay();
  }

  protected void setUp() throws Exception {
    super.setUp();
    this.cacheFlushInterceptor = new CacheFlushInterceptor();
  }

  private void setUpCachingAttributeSourceAdvisorAsMockObject() {

    this.cacheFlushAttributeSourceAdvisorControl = MockControl
        .createControl(CacheFlushAttributeSource.class);
    this.cacheFlushAttributeSource = (CacheFlushAttributeSource) this.cacheFlushAttributeSourceAdvisorControl
        .getMock();

    this.cacheFlushInterceptor
        .setCacheFlushAttributeSource(this.cacheFlushAttributeSource);

    this.cacheFlushAttributeSourceAdvisor = new CacheFlushAttributeSourceAdvisor(
        this.cacheFlushInterceptor);
  }

  private void setUpTargetClassAndMethod() throws Exception {
    this.targetClass = String.class;
    this.method = this.targetClass.getMethod("charAt",
        new Class[] { int.class });
  }

  /**
   * Verifies that the constructor
   * <code>{@link CacheFlushAttributeSourceAdvisor#CacheFlushAttributeSourceAdvisor(CacheFlushInterceptor)}</code>.
   * throws a <code>{@link AopConfigException}</code> when the specified
   * <code>{@link CacheFlushInterceptor}</code> does not contain a
   * <code>{@link CacheFlushAttributeSource}</code>.
   */
  public void testConstructorWithMethodInterceptorNotHavingCacheFlushAttributeSource() {
    this.cacheFlushInterceptor.setCacheFlushAttributeSource(null);

    try {
      this.cacheFlushAttributeSourceAdvisor = new CacheFlushAttributeSourceAdvisor(
          this.cacheFlushInterceptor);
      fail("Expecting a <" + AopConfigException.class.getName() + ">");
    } catch (AopConfigException exception) {
      // we are expecting this exception.
    }
  }

  /**
   * Tests
   * <code>{@link CacheFlushAttributeSourceAdvisor#matches(Method, Class)}</code>.
   * Verifies that <code>false</code> is returned if no instance of
   * <code>{@link FlushCache}</code> is returned by
   * <code>{@link #cacheFlushAttributeSource}</code>.
   */
  public void testMatchesWhenAttributeIsNotFound() throws Exception {
    this.setUpCachingAttributeSourceAdvisorAsMockObject();
    this.setUpTargetClassAndMethod();

    // expectation: a cache-flush attribute should not be found for the
    // specified method and class.
    this.cacheFlushAttributeSource.getCacheFlushAttribute(this.method,
        this.targetClass);
    this.cacheFlushAttributeSourceAdvisorControl.setReturnValue(null);

    this.setStateOfMockControlsToReplay();

    boolean matches = this.cacheFlushAttributeSourceAdvisor.matches(
        this.method, this.targetClass);

    assertFalse("<CacheFlushAttributeSourceAdvisor.matches(Method, Class)>",
        matches);

    this.verifyExpectationsOfMockControlsWereMet();
  }

  /**
   * Tests
   * <code>{@link CacheFlushAttributeSourceAdvisor#matches(Method, Class)}</code>.
   * Verifies that <code>true</code> is returned if there is an instance of
   * <code>{@link FlushCache}</code> returned by
   * <code>{@link #cacheFlushAttributeSource}</code>.
   */
  public void testMatchesWhenNotNullAttributeIsFound() throws Exception {
    this.setUpCachingAttributeSourceAdvisorAsMockObject();
    this.setUpTargetClassAndMethod();

    // expectation: a cache-flush attribute should be found for the specified
    // method and class.
    this.cacheFlushAttributeSource.getCacheFlushAttribute(this.method,
        this.targetClass);
    this.cacheFlushAttributeSourceAdvisorControl
        .setReturnValue(new FlushCache());

    this.setStateOfMockControlsToReplay();

    boolean matches = this.cacheFlushAttributeSourceAdvisor.matches(
        this.method, this.targetClass);

    assertTrue("<CacheFlushAttributeSourceAdvisor.matches(Method, Class)>",
        matches);

    this.verifyExpectationsOfMockControlsWereMet();
  }

  private void verifyExpectationsOfMockControlsWereMet() {
    this.cacheFlushAttributeSourceAdvisorControl.verify();
  }
}