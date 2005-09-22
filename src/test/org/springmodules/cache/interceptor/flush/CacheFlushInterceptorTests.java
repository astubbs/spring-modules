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

import org.aopalliance.intercept.MethodInvocation;
import org.easymock.MockControl;
import org.springmodules.cache.interceptor.AbstractCacheModuleInterceptorTests;
import org.springmodules.cache.provider.CacheProviderFacadeStatus;

/**
 * <p>
 * Unit Tests for <code>{@link CacheFlushInterceptor}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.6 $ $Date: 2005/09/22 00:42:41 $
 */
public final class CacheFlushInterceptorTests extends
    AbstractCacheModuleInterceptorTests {

  /**
   * Metadata attribute for the intercepted method.
   */
  private FlushCache cacheFlushAttribute;

  private CacheFlushAttributeSource cacheFlushAttributeSource;

  private MockControl cacheFlushAttributeSourceControl;

  private CacheFlushInterceptor cacheFlushInterceptor;

  public CacheFlushInterceptorTests(String name) {
    super(name);
  }

  private void assertInterceptorInvocationIsCorrect(
      Object expectedInvocationReturnValue) throws Throwable {
    setStateOfMockControlsToReplay();

    Object actualReturnValue = cacheFlushInterceptor.invoke(methodInvocation);
    assertSame(expectedInvocationReturnValue, actualReturnValue);

    verifyExpectationsOfMockControlsWereMet();
  }

  private void assertInvokeWithStatusOfCacheProviderNotReadyDoesNotAccessCache(
      CacheProviderFacadeStatus status) throws Throwable {
    cacheProviderFacade.getStatus();
    cacheProviderFacadeControl.setReturnValue(status);

    Object proceedReturnValue = "Luke Skywalker";
    expectProceedMethodInvocation(proceedReturnValue);

    assertInterceptorInvocationIsCorrect(proceedReturnValue);
  }

  private void expectGetCacheFlushAttributeForInterceptedMethod(
      Object thisObject, FlushCache returnValue) {
    Class targetClass = (thisObject != null) ? thisObject.getClass() : null;

    expectGetMethodFromMethodInvocation();
    cacheFlushAttributeSource.getCacheFlushAttribute(interceptedMethod,
        targetClass);
    cacheFlushAttributeSourceControl.setReturnValue(returnValue);
  }

  protected void onSetStateOfMockControlsToReplay() {
    cacheFlushAttributeSourceControl.replay();
  }

  protected void onSetUp() throws Exception {
    // Create the metadata attribute for the intercepted method.
    cacheFlushAttribute = new FlushCache();
    cacheFlushAttribute.setCacheProfileIds("CACHE_PROFILE");

    // Create the proxy for the interface 'CacheFlushAttributeSource'.
    cacheFlushAttributeSourceControl = MockControl
        .createControl(CacheFlushAttributeSource.class);
    cacheFlushAttributeSource = (CacheFlushAttributeSource) cacheFlushAttributeSourceControl
        .getMock();

    // Create the instance of the class to test.
    cacheFlushInterceptor = new CacheFlushInterceptor();
    cacheFlushInterceptor.setCacheProviderFacade(cacheProviderFacade);
    cacheFlushInterceptor
        .setCacheFlushAttributeSource(cacheFlushAttributeSource);
  }

  protected void onVerifyExpectationsOfMockControlsWereMet() {
    cacheFlushAttributeSourceControl.verify();
  }

  /**
   * Verifies that the method
   * <code>{@link CacheFlushInterceptor#getCacheFlushAttribute(MethodInvocation)}</code>
   * returns the instance of <code>{@link FlushCache}</code> retrieved from
   * <code>{@link #cacheFlushAttributeSource}</code>.
   */
  public void testGetCacheFlushAttribute() throws Exception {
    Object thisObject = OBJECT_HOLDING_JOINPOINT_STATIC_PART;
    expectGetThisFromJoinpoint(thisObject);
    expectGetCacheFlushAttributeForInterceptedMethod(thisObject,
        cacheFlushAttribute);

    setStateOfMockControlsToReplay();

    // execute the method to test.
    FlushCache returnedCacheFlushAttribute = cacheFlushInterceptor
        .getCacheFlushAttribute(methodInvocation);

    assertSame(cacheFlushAttribute, returnedCacheFlushAttribute);

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
    expectGetThisFromJoinpoint(thisObject);

    expectGetCacheFlushAttributeForInterceptedMethod(thisObject,
        cacheFlushAttribute);

    setStateOfMockControlsToReplay();

    // execute the method to test.
    FlushCache returnedCacheFlushAttribute = cacheFlushInterceptor
        .getCacheFlushAttribute(methodInvocation);

    assertSame(cacheFlushAttribute, returnedCacheFlushAttribute);

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
    cacheFlushAttribute.setFlushBeforeExecution(false);

    expectCacheProviderFacadeStatusIsReady();

    Object thisObject = OBJECT_HOLDING_JOINPOINT_STATIC_PART;
    expectGetThisFromJoinpoint(thisObject);

    expectGetCacheFlushAttributeForInterceptedMethod(thisObject,
        cacheFlushAttribute);

    Object proceedReturnValue = new Integer(10);
    expectProceedMethodInvocation(proceedReturnValue);

    // flush the cache.
    cacheProviderFacade.flushCache(cacheFlushAttribute.getCacheProfileIds());

    assertInterceptorInvocationIsCorrect(proceedReturnValue);
  }

  /**
   * Verifies that the method
   * <code>{@link CacheFlushInterceptor#invoke(MethodInvocation)}</code>
   * flushes the cache before executing the intercepted method if
   * <code>{@link FlushCache#isFlushBeforeExecution()}</code> is
   * <code>true</code>.
   */
  public void testInvokeFlushingBeforeProceedIsCalled() throws Throwable {
    cacheFlushAttribute.setFlushBeforeExecution(true);

    expectCacheProviderFacadeStatusIsReady();

    Object thisObject = OBJECT_HOLDING_JOINPOINT_STATIC_PART;
    expectGetThisFromJoinpoint(thisObject);

    expectGetCacheFlushAttributeForInterceptedMethod(thisObject,
        cacheFlushAttribute);

    // flush the cache.
    cacheProviderFacade.flushCache(cacheFlushAttribute.getCacheProfileIds());

    Object proceedReturnValue = new Integer(10);
    expectProceedMethodInvocation(proceedReturnValue);

    assertInterceptorInvocationIsCorrect(proceedReturnValue);
  }

  public void testInvokeWithCacheProviderUninitialized() throws Throwable {
    assertInvokeWithStatusOfCacheProviderNotReadyDoesNotAccessCache(CacheProviderFacadeStatus.UNINITIALIZED);
  }

  public void testInvokeWithIllegalStatusOfCacheProvider() throws Throwable {
    assertInvokeWithStatusOfCacheProviderNotReadyDoesNotAccessCache(CacheProviderFacadeStatus.INVALID);
  }

  /**
   * Verifies that the method
   * <code>{@link CacheFlushInterceptor#invoke(MethodInvocation)}</code> does
   * not flush the cache if there are not any cache-flush attribute for the
   * intercepted method.
   */
  public void testInvokeWithoutCacheFlushAttribute() throws Throwable {
    expectCacheProviderFacadeStatusIsReady();

    Object thisObject = OBJECT_HOLDING_JOINPOINT_STATIC_PART;
    expectGetThisFromJoinpoint(thisObject);

    expectGetCacheFlushAttributeForInterceptedMethod(thisObject, null);

    Object proceedReturnValue = new Integer(10);
    expectProceedMethodInvocation(proceedReturnValue);

    assertInterceptorInvocationIsCorrect(proceedReturnValue);
  }
}