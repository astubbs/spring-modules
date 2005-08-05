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
 * 
 * @version $Revision: 1.3 $ $Date: 2005/08/05 02:45:15 $
 */
public final class CachingAttributeSourceAdvisorTests extends TestCase {

  private CachingAttributeSource cachingAttributeSource;

  /**
   * Primary object under test.
   */
  private CachingAttributeSourceAdvisor cachingAttributeSourceAdvisor;

  private MockControl cachingAttributeSourceControl;

  /**
   * Caching interceptor used only to obtain
   * <code>{@link #cachingAttributeSource}</code>.
   */
  private CachingInterceptor cachingInterceptor;

  /**
   * Method definition containing caching metadata attributes.
   */
  private Method method;

  /**
   * Class declaring <code>{@link #method}</code>.
   */
  private Class targetClass;

  public CachingAttributeSourceAdvisorTests(String name) {
    super(name);
  }

  private void setStateOfMockControlsToReplay() {
    this.cachingAttributeSourceControl.replay();
  }

  protected void setUp() throws Exception {
    super.setUp();

    this.cachingInterceptor = new CachingInterceptor();
  }

  private void setUpCachingAttributeSourceAsMockObject() {
    this.cachingAttributeSourceControl = MockControl
        .createControl(CachingAttributeSource.class);
    this.cachingAttributeSource = (CachingAttributeSource) this.cachingAttributeSourceControl
        .getMock();

    this.cachingInterceptor
        .setCachingAttributeSource(this.cachingAttributeSource);

    this.cachingAttributeSourceAdvisor = new CachingAttributeSourceAdvisor(
        this.cachingInterceptor);
  }

  private void setUpTargetClassAndMethod() throws Exception {
    this.targetClass = String.class;
    this.method = this.targetClass.getMethod("charAt",
        new Class[] { int.class });
  }

  /**
   * Verifies that the constructor
   * <code>{@link CachingAttributeSourceAdvisor#CachingAttributeSourceAdvisor(CachingInterceptor)}</code>
   * throws a <code>{@link AopConfigException}</code> when the specified
   * <code>{@link CachingInterceptor}</code> does not contain a
   * <code>{@link CachingAttributeSource}</code>.
   */
  public void testConstructorWithMethodInterceptorNotHavingCachingAttributeSource() {
    this.cachingInterceptor.setCachingAttributeSource(null);

    try {
      this.cachingAttributeSourceAdvisor = new CachingAttributeSourceAdvisor(
          this.cachingInterceptor);
      fail("Expecting a <" + AopConfigException.class.getName() + ">");

    } catch (AopConfigException exception) {
      // we are expecting this exception.
    }
  }

  /**
   * Verifies that the method
   * <code>{@link CachingAttributeSourceAdvisor#matches(Method, Class)}</code>
   * return <code>false</code> if <code>{@link #cachingAttributeSource}</code>
   * does not contain a metadata attribute for a given method.
   */
  public void testMatchesWhenAttributeIsNotFound() throws Exception {
    this.setUpCachingAttributeSourceAsMockObject();
    this.setUpTargetClassAndMethod();

    // expectation: a caching attribute should not be found for the specified
    // method and class.
    this.cachingAttributeSource.getCachingAttribute(this.method,
        this.targetClass);
    this.cachingAttributeSourceControl.setReturnValue(null);

    this.setStateOfMockControlsToReplay();

    // execute the method to test.
    boolean matches = this.cachingAttributeSourceAdvisor.matches(this.method,
        this.targetClass);

    assertFalse("<CachingAttributeSourceAdvisor.matches(Method, Class)>",
        matches);

    this.verifyExpectationsOfMockControlsWereMet();
  }

  /**
   * Tests
   * <code>{@link CachingAttributeSourceAdvisor#matches(Method, Class)}</code>.
   * Verifies that <code>true</code> is returned if there is an instance of
   * <code>{@link Cached}</code> returned by
   * <code>{@link #cachingAttributeSource}</code>.
   */
  public void testMatchesWhenNotNullAttributeIsFound() throws Exception {
    this.setUpCachingAttributeSourceAsMockObject();
    this.setUpTargetClassAndMethod();

    // expectation: a caching attribute should be found for the specified method
    // and class.
    this.cachingAttributeSource.getCachingAttribute(this.method,
        this.targetClass);
    this.cachingAttributeSourceControl.setReturnValue(new Cached());

    this.setStateOfMockControlsToReplay();

    // execute the method to test.
    boolean matches = this.cachingAttributeSourceAdvisor.matches(this.method,
        this.targetClass);

    assertTrue("<CachingAttributeSourceAdvisor.matches(Method, Class)>",
        matches);

    this.verifyExpectationsOfMockControlsWereMet();
  }

  private void verifyExpectationsOfMockControlsWereMet() {
    this.cachingAttributeSourceControl.verify();
  }

}