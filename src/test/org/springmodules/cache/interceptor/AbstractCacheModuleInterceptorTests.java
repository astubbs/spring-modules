/* 
 * Created on Sep 20, 2005
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
package org.springmodules.cache.interceptor;

import java.lang.reflect.Method;

import junit.framework.TestCase;

import org.aopalliance.intercept.MethodInvocation;
import org.easymock.MockControl;
import org.springmodules.cache.provider.CacheProviderFacade;
import org.springmodules.cache.provider.CacheProviderFacadeStatus;

/**
 * <p>
 * Template for Test Cases for the method interceptors of the cache module.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public abstract class AbstractCacheModuleInterceptorTests extends TestCase {

  protected static final Object OBJECT_HOLDING_JOINPOINT_STATIC_PART = new Object();

  protected CacheProviderFacade cacheProviderFacade;

  protected MockControl cacheProviderFacadeControl;

  protected Method interceptedMethod;

  protected MethodInvocation methodInvocation;

  protected MockControl methodInvocationControl;

  public AbstractCacheModuleInterceptorTests(String name) {
    super(name);
  }

  protected final void expectCacheProviderFacadeStatusIsReady() {
    cacheProviderFacade.getStatus();
    cacheProviderFacadeControl.setReturnValue(CacheProviderFacadeStatus.READY);
  }

  protected final void expectGetThisFromJoinpoint(Object expected) {
    methodInvocation.getThis();
    methodInvocationControl.setReturnValue(expected);
  }

  protected final void expectProceedMethodInvocation(Object expected)
      throws Throwable {
    methodInvocation.proceed();
    methodInvocationControl.setReturnValue(expected);
  }

  protected abstract void onSetStateOfMockControlsToReplay();

  protected abstract void onSetUp() throws Exception;

  protected abstract void onVerifyExpectationsOfMockControlsWereMet();

  protected final void setStateOfMockControlsToReplay() {
    cacheProviderFacadeControl.replay();
    methodInvocationControl.replay();

    onSetStateOfMockControlsToReplay();
  }

  protected final void setUp() throws Exception {
    super.setUp();

    cacheProviderFacadeControl = MockControl
        .createControl(CacheProviderFacade.class);
    cacheProviderFacade = (CacheProviderFacade) cacheProviderFacadeControl
        .getMock();

    interceptedMethod = String.class.getMethod("concat",
        new Class[] { String.class });

    methodInvocationControl = MockControl.createControl(MethodInvocation.class);
    methodInvocation = (MethodInvocation) methodInvocationControl.getMock();

    // expectation(s) common to all test cases.
    methodInvocation.getMethod();
    methodInvocationControl.setReturnValue(interceptedMethod);

    onSetUp();
  }

  protected final void verifyExpectationsOfMockControlsWereMet() {
    cacheProviderFacadeControl.verify();
    methodInvocationControl.verify();

    onVerifyExpectationsOfMockControlsWereMet();
  }
}
