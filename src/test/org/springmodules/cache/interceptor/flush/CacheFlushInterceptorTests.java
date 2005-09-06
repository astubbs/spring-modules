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

import org.aopalliance.intercept.MethodInvocation;
import org.easymock.MockControl;
import org.springmodules.cache.provider.CacheProviderFacade;

/**
 * <p>
 * Unit Tests for <code>{@link CacheFlushInterceptor}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.4 $ $Date: 2005/09/06 01:41:26 $
 */
public final class CacheFlushInterceptorTests extends TestCase {

  /**
   * Metadata attribute for the intercepted method.
   */
  private FlushCache cacheFlushAttribute;

  private CacheFlushAttributeSource cacheFlushAttributeSource;

  private MockControl cacheFlushAttributeSourceControl;

  private CacheFlushInterceptor cacheFlushInterceptor;

  private CacheProviderFacade cacheProviderFacade;

  private MockControl cacheProviderFacadeControl;

  /**
   * Method to be intercepted by <code>{@link #cacheFlushInterceptor}</code>.
   */
  private Method interceptedMethod;

  private MethodInvocation methodInvocation;

  private MockControl methodInvocationControl;

  public CacheFlushInterceptorTests(String name) {
    super(name);
  }

  private void assertSameFlushCache(FlushCache actual) {
    assertSame(this.cacheFlushAttribute, actual);
  }

  private void setStateOfMockControlsToReplay() {
    this.cacheFlushAttributeSourceControl.replay();
    this.cacheProviderFacadeControl.replay();
    this.methodInvocationControl.replay();
  }

  protected void setUp() throws Exception {
    super.setUp();

    // Create the definition of the intercepted method.
    Class clazz = String.class;
    this.interceptedMethod = clazz.getMethod("concat",
        new Class[] { String.class });

    // Create the proxy for the interface 'CacheProviderFacadeControl'.
    this.cacheProviderFacadeControl = MockControl
        .createControl(CacheProviderFacade.class);
    this.cacheProviderFacade = (CacheProviderFacade) this.cacheProviderFacadeControl
        .getMock();

    // Create the metadata attribute for the intercepted method.
    this.cacheFlushAttribute = new FlushCache();
    this.cacheFlushAttribute.setCacheProfileIds("CACHE_PROFILE");

    // Create the proxy for the interface 'CacheFlushAttributeSource'.
    this.cacheFlushAttributeSourceControl = MockControl
        .createControl(CacheFlushAttributeSource.class);
    this.cacheFlushAttributeSource = (CacheFlushAttributeSource) this.cacheFlushAttributeSourceControl
        .getMock();

    // Create the proxy for the interface 'MethodInvocation'.
    this.methodInvocationControl = MockControl
        .createControl(MethodInvocation.class);
    this.methodInvocation = (MethodInvocation) this.methodInvocationControl
        .getMock();

    // Create the instance of the class to test.
    this.cacheFlushInterceptor = new CacheFlushInterceptor();
    this.cacheFlushInterceptor.setCacheProviderFacade(this.cacheProviderFacade);
    this.cacheFlushInterceptor
        .setCacheFlushAttributeSource(this.cacheFlushAttributeSource);

    // This expectation is required for all the Test Cases.
    this.methodInvocation.getMethod();
    this.methodInvocationControl.setReturnValue(this.interceptedMethod);
  }

  /**
   * Verifies that the method
   * <code>{@link CacheFlushInterceptor#getCacheFlushAttribute(MethodInvocation)}</code>
   * returns the instance of <code>{@link FlushCache}</code> retrieved from
   * <code>{@link #cacheFlushAttributeSource}</code>.
   */
  public void testGetCacheFlushAttribute() throws Exception {
    Object thisObject = "A String!";
    Class targetClass = thisObject.getClass();

    // get the target object for an invocation.
    this.methodInvocation.getThis();
    this.methodInvocationControl.setReturnValue(thisObject);

    // get the cache-flush attribute of the intercepted method.
    this.cacheFlushAttributeSource.getCacheFlushAttribute(
        this.interceptedMethod, targetClass);
    this.cacheFlushAttributeSourceControl
        .setReturnValue(this.cacheFlushAttribute);

    setStateOfMockControlsToReplay();

    // execute the method to test.
    FlushCache returnedCacheFlushAttribute = this.cacheFlushInterceptor
        .getCacheFlushAttribute(this.methodInvocation);
    assertSameFlushCache(returnedCacheFlushAttribute);

    verifyExpectationsOfMockControlsWereMet();
  }

  /**
   * Verifies that the method
   * <code>{@link CacheFlushInterceptor#getCacheFlushAttribute(MethodInvocation)}</code>
   * returns the instance of <code>{@link FlushCache}</code> retrieved from
   * <code>{@link #cacheFlushAttributeSource}</code> when
   * <code>MethodInvocation.getThis()</code> returns <code>null</code>.
   */
  public void testGetCacheFlushAttributeWithMethodInvocationHavingThisObjectEqualToNull()
      throws Exception {

    Object thisObject = null;
    Class targetClass = null;

    // get the target object for an invocation.
    this.methodInvocation.getThis();
    this.methodInvocationControl.setReturnValue(thisObject);

    // get the cache-flush attribute of the intercepted method.
    this.cacheFlushAttributeSource.getCacheFlushAttribute(
        this.interceptedMethod, targetClass);
    this.cacheFlushAttributeSourceControl
        .setReturnValue(this.cacheFlushAttribute);

    setStateOfMockControlsToReplay();

    // execute the method to test.
    FlushCache returnedCacheFlushAttribute = this.cacheFlushInterceptor
        .getCacheFlushAttribute(this.methodInvocation);
    assertSameFlushCache(returnedCacheFlushAttribute);

    verifyExpectationsOfMockControlsWereMet();
  }

  /**
   * Verifies that the method
   * <code>{@link CacheFlushInterceptor#invoke(MethodInvocation)}</code>
   * flushes the cache after executing the intercepted method if
   * <code>{@link FlushCache#isFlushBeforeExecution()}</code> is
   * <code>false</code>.
   */
  public void testInvokeFlushingAfterProceedIsCalled() throws Throwable {
    String thisObject = "A String!";
    this.cacheFlushAttribute.setFlushBeforeExecution(false);

    // return value expected to be returned when executing the intercepted
    // method.
    Object expectedReturnValue = new Integer(10);

    // get the target object for an invocation.
    this.methodInvocation.getThis();
    this.methodInvocationControl.setReturnValue(thisObject);

    // get the cache-flush attribute for the intercepted method.
    this.cacheFlushAttributeSource.getCacheFlushAttribute(
        this.interceptedMethod, thisObject.getClass());
    this.cacheFlushAttributeSourceControl
        .setReturnValue(this.cacheFlushAttribute);

    // execute the intercepted method.
    this.methodInvocation.proceed();
    this.methodInvocationControl.setReturnValue(expectedReturnValue);

    // flush the cache.
    this.cacheProviderFacade.flushCache(this.cacheFlushAttribute
        .getCacheProfileIds());

    setStateOfMockControlsToReplay();

    // execute the method to test.
    Object actualReturnValue = this.cacheFlushInterceptor
        .invoke(this.methodInvocation);
    assertSame(expectedReturnValue, actualReturnValue);

    verifyExpectationsOfMockControlsWereMet();
  }

  /**
   * Verifies that the method
   * <code>{@link CacheFlushInterceptor#invoke(MethodInvocation)}</code>
   * flushes the cache before executing the intercepted method if
   * <code>{@link FlushCache#isFlushBeforeExecution()}</code> is
   * <code>true</code>.
   */
  public void testInvokeFlushingBeforeProceedIsCalled() throws Throwable {
    String thisObject = "A String!";
    this.cacheFlushAttribute.setFlushBeforeExecution(true);

    // return value expected to be returned when executing the intercepted
    // method.
    Object expectedReturnValue = new Integer(10);

    // get the target object for an invocation.
    this.methodInvocation.getThis();
    this.methodInvocationControl.setReturnValue(thisObject);

    // get the cache-flush attribute for the intercepted method.
    this.cacheFlushAttributeSource.getCacheFlushAttribute(
        this.interceptedMethod, thisObject.getClass());
    this.cacheFlushAttributeSourceControl
        .setReturnValue(this.cacheFlushAttribute);

    // flush the cache.
    this.cacheProviderFacade.flushCache(this.cacheFlushAttribute
        .getCacheProfileIds());

    // execute the intercepted method.
    this.methodInvocation.proceed();
    this.methodInvocationControl.setReturnValue(expectedReturnValue);

    setStateOfMockControlsToReplay();

    // execute the method to test.
    Object actualReturnValue = this.cacheFlushInterceptor
        .invoke(this.methodInvocation);
    assertSame(expectedReturnValue, actualReturnValue);

    verifyExpectationsOfMockControlsWereMet();
  }

  /**
   * Verifies that the method
   * <code>{@link CacheFlushInterceptor#invoke(MethodInvocation)}</code> does
   * not flush the cache if there are not any cache-flush attribute for the
   * intercepted method.
   */
  public void testInvokeWithoutCacheFlushAttribute() throws Throwable {
    String thisObject = "A String!";

    // return value expected to be returned when executing the intercepted
    // method.
    Object expectedReturnValue = new Integer(10);

    // set the expectations of the mock objects.
    this.methodInvocation.getThis();
    this.methodInvocationControl.setReturnValue(thisObject);

    this.cacheFlushAttributeSource.getCacheFlushAttribute(
        this.interceptedMethod, thisObject.getClass());
    this.cacheFlushAttributeSourceControl.setReturnValue(null);

    // no cache flushing is executed. execute the intercepted method.
    this.methodInvocation.proceed();
    this.methodInvocationControl.setReturnValue(expectedReturnValue);

    setStateOfMockControlsToReplay();

    // execute the method to test.
    Object actualReturnValue = this.cacheFlushInterceptor
        .invoke(this.methodInvocation);
    assertSame(expectedReturnValue, actualReturnValue);

    verifyExpectationsOfMockControlsWereMet();
  }

  private void verifyExpectationsOfMockControlsWereMet() {
    this.cacheFlushAttributeSourceControl.verify();
    this.cacheProviderFacadeControl.verify();
    this.methodInvocationControl.verify();
  }
}