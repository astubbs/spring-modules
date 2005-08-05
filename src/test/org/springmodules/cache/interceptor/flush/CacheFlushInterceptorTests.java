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
 * Unit Test for <code>{@link CacheFlushInterceptor}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.2 $ $Date: 2005/08/05 02:45:17 $
 */
public final class CacheFlushInterceptorTests extends TestCase {

  /**
   * Metadata attribute for the intercepted method.
   */
  private FlushCache cacheFlushAttribute;

  /**
   * Primary object (instance of the class to test).
   */
  private CacheFlushInterceptor cacheFlushInterceptor;

  /**
   * Method to be intercepted by <code>{@link #cacheFlushInterceptor}</code>.
   */
  private Method interceptedMethod;

  /**
   * Mock object that simulates a search of cache-flush attributes for a given
   * method.
   */
  private CacheFlushAttributeSource mockCacheFlushAttributeSource;

  /**
   * Controls the behavior of
   * <code>{@link #mockCacheFlushAttributeSource}</code>.
   */
  private MockControl mockCacheFlushAttributeSourceControl;

  /**
   * Mock object that simulates a facade to a cache provider.
   */
  private CacheProviderFacade mockCacheProviderFacade;

  /**
   * Controls the behavior of <code>{@link #mockCacheProviderFacade}</code>.
   */
  private MockControl mockCacheProviderFacadeControl;

  /**
   * Simulates the definition of the method being intercepted by
   * <code>{@link #cacheFlushInterceptor}</code>.
   */
  private MethodInvocation mockMethodInvocation;

  /**
   * Controls the behavior of <code>{@link #mockMethodInvocation}</code>.
   */
  private MockControl mockMethodInvocationControl;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the Test Case.
   */
  public CacheFlushInterceptorTests(String name) {
    super(name);
  }

  /**
   * Sets up the test fixture.
   */
  protected void setUp() throws Exception {
    super.setUp();

    // Create the definition of the intercepted method.
    Class clazz = String.class;
    this.interceptedMethod = clazz.getMethod("concat",
        new Class[] { String.class });

    // Create the proxy for the interface 'CacheProviderFacadeControl'.
    this.mockCacheProviderFacadeControl = MockControl
        .createControl(CacheProviderFacade.class);
    this.mockCacheProviderFacade = (CacheProviderFacade) this.mockCacheProviderFacadeControl
        .getMock();

    // Create the metadata attribute for the intercepted method.
    this.cacheFlushAttribute = new FlushCache();
    this.cacheFlushAttribute.setCacheProfileIds("CACHE_PROFILE");

    // Create the proxy for the interface 'CacheFlushAttributeSource'.
    this.mockCacheFlushAttributeSourceControl = MockControl
        .createControl(CacheFlushAttributeSource.class);
    this.mockCacheFlushAttributeSource = (CacheFlushAttributeSource) this.mockCacheFlushAttributeSourceControl
        .getMock();

    // Create the proxy for the interface 'MethodInvocation'.
    this.mockMethodInvocationControl = MockControl
        .createControl(MethodInvocation.class);
    this.mockMethodInvocation = (MethodInvocation) this.mockMethodInvocationControl
        .getMock();

    // Create the instance of the class to test.
    this.cacheFlushInterceptor = new CacheFlushInterceptor();
    this.cacheFlushInterceptor
        .setCacheProviderFacade(this.mockCacheProviderFacade);
    this.cacheFlushInterceptor
        .setCacheFlushAttributeSource(this.mockCacheFlushAttributeSource);

    // This expectation is required for all the Test Cases.
    this.mockMethodInvocation.getMethod();
    this.mockMethodInvocationControl.setReturnValue(this.interceptedMethod);
  }

  /**
   * Verifies that the method
   * <code>{@link CacheFlushInterceptor#getCacheFlushAttribute(MethodInvocation)}</code>
   * returns the instance of <code>{@link FlushCache}</code> retrieved from
   * <code>{@link #mockCacheFlushAttributeSource}</code>.
   */
  public void testGetCacheFlushAttribute() throws Exception {
    Object thisObject = "A String!";
    Class targetClass = thisObject.getClass();

    // expectation: get the target object for an invocation.
    this.mockMethodInvocation.getThis();
    this.mockMethodInvocationControl.setReturnValue(thisObject);

    // expectation: get the cache-flush attribute of the intercepted method.
    this.mockCacheFlushAttributeSource.getCacheFlushAttribute(
        this.interceptedMethod, targetClass);
    this.mockCacheFlushAttributeSourceControl
        .setReturnValue(this.cacheFlushAttribute);

    // set the state of the mock controls to 'replay'.
    this.mockCacheFlushAttributeSourceControl.replay();
    this.mockMethodInvocationControl.replay();

    // execute the method to test.
    FlushCache returnedCacheFlushAttribute = this.cacheFlushInterceptor
        .getCacheFlushAttribute(this.mockMethodInvocation);

    // verify that the cache-flush attribute is the same we are expecting.
    assertSame("<Cache-flush attribute>", this.cacheFlushAttribute,
        returnedCacheFlushAttribute);

    // verify that the expectations of the mock controls were met.
    this.mockCacheFlushAttributeSourceControl.verify();
    this.mockMethodInvocationControl.verify();
  }

  /**
   * Verifies that the method
   * <code>{@link CacheFlushInterceptor#getCacheFlushAttribute(MethodInvocation)}</code>
   * returns the instance of <code>{@link FlushCache}</code> retrieved from
   * <code>{@link #mockCacheFlushAttributeSource}</code> when
   * <code>MethodInvocation.getThis()</code> returns <code>null</code>.
   */
  public void testGetCacheFlushAttributeWithMethodInvocationHavingThisObjectEqualToNull()
      throws Exception {

    Object thisObject = null;
    Class targetClass = null;

    // expectation: get the target object for an invocation.
    this.mockMethodInvocation.getThis();
    this.mockMethodInvocationControl.setReturnValue(thisObject);

    // expectation: get the cache-flush attribute of the intercepted method.
    this.mockCacheFlushAttributeSource.getCacheFlushAttribute(
        this.interceptedMethod, targetClass);
    this.mockCacheFlushAttributeSourceControl
        .setReturnValue(this.cacheFlushAttribute);

    // set the state of the mock controls to 'replay'.
    this.mockCacheFlushAttributeSourceControl.replay();
    this.mockMethodInvocationControl.replay();

    // execute the method to test.
    FlushCache returnedCacheFlushAttribute = this.cacheFlushInterceptor
        .getCacheFlushAttribute(this.mockMethodInvocation);

    // verify that the cache-flush attribute is the same we are expecting.
    assertSame("<Cache-flush attribute>", this.cacheFlushAttribute,
        returnedCacheFlushAttribute);

    // verify that the expectations of the mock controls were met.
    this.mockCacheFlushAttributeSourceControl.verify();
    this.mockMethodInvocationControl.verify();
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

    // expectation: get the target object for an invocation.
    this.mockMethodInvocation.getThis();
    this.mockMethodInvocationControl.setReturnValue(thisObject);

    // expectation: get the cache-flush attribute for the intercepted method.
    this.mockCacheFlushAttributeSource.getCacheFlushAttribute(
        this.interceptedMethod, thisObject.getClass());
    this.mockCacheFlushAttributeSourceControl
        .setReturnValue(this.cacheFlushAttribute);

    // expectation: execute the intercepted method.
    this.mockMethodInvocation.proceed();
    this.mockMethodInvocationControl.setReturnValue(expectedReturnValue);

    // expectation: flush the cache.
    this.mockCacheProviderFacade.flushCache(this.cacheFlushAttribute
        .getCacheProfileIds());

    // set the state of the mock controls to 'replay'.
    this.mockCacheFlushAttributeSourceControl.replay();
    this.mockCacheProviderFacadeControl.replay();
    this.mockMethodInvocationControl.replay();

    // execute the method to test.
    Object actualReturnValue = this.cacheFlushInterceptor
        .invoke(this.mockMethodInvocation);

    assertSame("<Returned object>", expectedReturnValue, actualReturnValue);

    // verify that the expectations of the mock controls were met.
    this.mockCacheFlushAttributeSourceControl.verify();
    this.mockCacheProviderFacadeControl.verify();
    this.mockMethodInvocationControl.verify();
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

    // expectation: get the target object for an invocation.
    this.mockMethodInvocation.getThis();
    this.mockMethodInvocationControl.setReturnValue(thisObject);

    // expectation: get the cache-flush attribute for the intercepted method.
    this.mockCacheFlushAttributeSource.getCacheFlushAttribute(
        this.interceptedMethod, thisObject.getClass());
    this.mockCacheFlushAttributeSourceControl
        .setReturnValue(this.cacheFlushAttribute);

    // expectation: flush the cache.
    this.mockCacheProviderFacade.flushCache(this.cacheFlushAttribute
        .getCacheProfileIds());

    // expectation: execute the intercepted method.
    this.mockMethodInvocation.proceed();
    this.mockMethodInvocationControl.setReturnValue(expectedReturnValue);

    // set the state of the mock controls to 'replay'.
    this.mockCacheFlushAttributeSourceControl.replay();
    this.mockCacheProviderFacadeControl.replay();
    this.mockMethodInvocationControl.replay();

    // execute the method to test.
    Object actualReturnValue = this.cacheFlushInterceptor
        .invoke(this.mockMethodInvocation);

    assertSame("<Returned object>", expectedReturnValue, actualReturnValue);

    // verify that the expectations of the mock controls were met.
    this.mockCacheFlushAttributeSourceControl.verify();
    this.mockCacheProviderFacadeControl.verify();
    this.mockMethodInvocationControl.verify();
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
    this.mockMethodInvocation.getThis();
    this.mockMethodInvocationControl.setReturnValue(thisObject);

    // expectation: get the cache-flush attribute for the intercepted method.
    // The returned cache-flush attribute should be null.
    this.mockCacheFlushAttributeSource.getCacheFlushAttribute(
        this.interceptedMethod, thisObject.getClass());
    this.mockCacheFlushAttributeSourceControl.setReturnValue(null);

    // expectation: no cache flushing is executed. execute the intercepted
    // method.
    this.mockMethodInvocation.proceed();
    this.mockMethodInvocationControl.setReturnValue(expectedReturnValue);

    // set the state of the mock controls to 'replay'.
    this.mockCacheFlushAttributeSourceControl.replay();
    this.mockCacheProviderFacadeControl.replay();
    this.mockMethodInvocationControl.replay();

    // execute the method to test.
    Object actualReturnValue = this.cacheFlushInterceptor
        .invoke(this.mockMethodInvocation);

    assertSame("<Returned object>", expectedReturnValue, actualReturnValue);

    // verify that the execution was successful.
    this.mockCacheFlushAttributeSourceControl.verify();
    this.mockCacheProviderFacadeControl.verify();
    this.mockMethodInvocationControl.verify();
  }
}