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
 * @version $Revision: 1.5 $ $Date: 2005/09/06 01:41:25 $
 */
public class CacheFlushAttributeSourceAdvisorTests extends TestCase {

  private CacheFlushAttributeSource cacheFlushAttributeSource;

  private CacheFlushAttributeSourceAdvisor cacheFlushAttributeSourceAdvisor;

  private MockControl cacheFlushAttributeSourceAdvisorControl;

  /**
   * Caching interceptor used only to obtain
   * <code>{@link #cacheFlushAttributeSource}</code>.
   */
  private CacheFlushInterceptor cacheFlushInterceptor;

  /**
   * Method definition containing the metadata attributes for flushing the
   * cache.
   */
  private Method method;

  /**
   * Class declaring <code>{@link #method}</code>.
   */
  private Class targetClass;

  public CacheFlushAttributeSourceAdvisorTests(String name) {
    super(name);
  }

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

  public void testConstructorWithMethodInterceptorNotHavingCacheFlushAttributeSource() {
    this.cacheFlushInterceptor.setCacheFlushAttributeSource(null);

    try {
      this.cacheFlushAttributeSourceAdvisor = new CacheFlushAttributeSourceAdvisor(
          this.cacheFlushInterceptor);
      fail();
    } catch (AopConfigException exception) {
      // we are expecting this exception.
    }
  }

  public void testMatchesWhenAttributeIsNotFound() throws Exception {
    setUpCachingAttributeSourceAdvisorAsMockObject();
    setUpTargetClassAndMethod();

    // a metadata attribute for flushing the cache should not be found for the
    // specified method and class.
    this.cacheFlushAttributeSource.getCacheFlushAttribute(this.method,
        this.targetClass);
    this.cacheFlushAttributeSourceAdvisorControl.setReturnValue(null);

    setStateOfMockControlsToReplay();

    assertFalse(this.cacheFlushAttributeSourceAdvisor.matches(this.method,
        this.targetClass));

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
    setUpCachingAttributeSourceAdvisorAsMockObject();
    setUpTargetClassAndMethod();

    // a metadata attribute for flushing the cache should be found for the
    // specified method and class.
    this.cacheFlushAttributeSource.getCacheFlushAttribute(this.method,
        this.targetClass);
    this.cacheFlushAttributeSourceAdvisorControl
        .setReturnValue(new FlushCache());

    setStateOfMockControlsToReplay();

    boolean matches = this.cacheFlushAttributeSourceAdvisor.matches(
        this.method, this.targetClass);

    assertTrue(matches);

    this.verifyExpectationsOfMockControlsWereMet();
  }

  private void verifyExpectationsOfMockControlsWereMet() {
    this.cacheFlushAttributeSourceAdvisorControl.verify();
  }
}