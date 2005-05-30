/* 
 * Created on Jan 28, 2005
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

package org.springmodules.cache.key;

import java.io.Serializable;
import java.lang.reflect.Method;

import junit.framework.TestCase;

import org.aopalliance.intercept.MethodInvocation;
import org.easymock.MockControl;

/**
 * <p>
 * Template for Test Cases for implementations
 * <code>{@link CacheKeyGenerator}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.2 $ $Date: 2005/05/30 13:30:36 $
 */
public abstract class AbstractCacheKeyGeneratorTests extends TestCase {

  /**
   * Mock object that simulates the description of an invocation of a method.
   */
  private MethodInvocation mockMethodInvocation;

  /**
   * Controls the behavior of <code>{@link #mockMethodInvocation}</code>.
   */
  private MockControl mockMethodInvocationControl;

  /**
   * Constructor.
   */
  public AbstractCacheKeyGeneratorTests() {
    super();
  }

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the Test Case.
   */
  public AbstractCacheKeyGeneratorTests(String name) {
    super(name);
  }

  /**
   * Gives subclasses the opportunity to set up their own fixture.
   */
  protected void afterSetUp() throws Exception {
    // no implementation.
  }

  /**
   * Executes the method
   * <code>{@link HashCodeCacheKeyGenerator#generateKey(MethodInvocation)}</code>
   * 
   * @param method
   *          the invocated method.
   * @param methodArguments
   *          the arguments of the invocated method.
   * @return the key generated for the specified declaration of the invocated
   *         method.
   */
  protected final Serializable executeGenerateArgumentHashCode(Method method,
      Object[] methodArguments) {

    CacheKeyGenerator cacheKeyGenerator = this.getCacheKeyGenerator();

    // expectation: get the method from the description of the invocation.
    this.mockMethodInvocation.getMethod();
    this.mockMethodInvocationControl.setReturnValue(method);

    // expectation: get the arguments of the invocated method.
    this.mockMethodInvocation.getArguments();
    this.mockMethodInvocationControl.setReturnValue(methodArguments);

    // set the state of the mock control to 'replay'.
    this.mockMethodInvocationControl.replay();

    // get the key for the method.
    Serializable cacheKey = cacheKeyGenerator
        .generateKey(this.mockMethodInvocation);

    // verify that the expectations of the mock control were met.
    this.mockMethodInvocationControl.verify();

    return cacheKey;
  }

  /**
   * Returns the instance of the class to test.
   * 
   * @return the instance of the class to test.
   */
  protected abstract CacheKeyGenerator getCacheKeyGenerator();

  /**
   * Getter for <code>{@link #mockMethodInvocation}</code>.
   * 
   * @return the value of the member variable <code>mockMethodInvocation</code>.
   */
  protected final MethodInvocation getMockMethodInvocation() {
    return this.mockMethodInvocation;
  }

  /**
   * Getter for <code>{@link #mockMethodInvocationControl}</code>.
   * 
   * @return the value of the member variable
   *         <code>mockMethodInvocationControl</code>.
   */
  protected final MockControl getMockMethodInvocationControl() {
    return this.mockMethodInvocationControl;
  }

  /**
   * Sets up the test fixture.
   */
  protected final void setUp() throws Exception {
    super.setUp();

    this.mockMethodInvocationControl = MockControl
        .createControl(MethodInvocation.class);
    this.mockMethodInvocation = (MethodInvocation) this.mockMethodInvocationControl
        .getMock();

    this.afterSetUp();
  }

  /**
   * Verifies that the method
   * <code>{@link CacheKeyGenerator#generateKey(MethodInvocation)}</code>
   * returns different keys for different methods having equal set of arguments.
   */
  public void testGenerateKeyGeneratesDifferentKeysForNotEqualMethodsWithEqualArguments()
      throws Exception {

    Class targetClass = String.class;
    Class[] argumentClasses = new Class[] { int.class, int.class };

    Method indexOfMethod = targetClass.getMethod("indexOf", argumentClasses);
    Object[] indexOfMethodArguments = new Object[] { new Integer(4),
        new Integer(0) };

    Method lastIndexOfMethod = targetClass.getMethod("lastIndexOf",
        argumentClasses);
    Object[] lastIndexOfMethodArguments = new Object[] { new Integer(4),
        new Integer(0) };

    // get the key for the first method.
    Serializable firstCacheKey = this.executeGenerateArgumentHashCode(
        indexOfMethod, indexOfMethodArguments);

    // get the key for the second method.
    this.mockMethodInvocationControl.reset();
    Serializable secondCacheKey = this.executeGenerateArgumentHashCode(
        lastIndexOfMethod, lastIndexOfMethodArguments);

    assertFalse("The keys should not be equal", firstCacheKey
        .equals(secondCacheKey));
  }

  /**
   * Verifies that the method
   * <code>{@link CacheKeyGenerator#generateKey(MethodInvocation)}</code>
   * returns different keys for different methods having different set of
   * arguments.
   */
  public void testGenerateKeyGeneratesDifferentKeysForNotEqualMethodsWithNotEqualArguments()
      throws Exception {

    Class targetClass = String.class;
    Class[] argumentClasses = new Class[] { int.class, int.class };

    Method indexOfMethod = targetClass.getMethod("indexOf", argumentClasses);
    Object[] indexOfMethodArguments = new Object[] { new Integer(4),
        new Integer(0) };

    Method lastIndexOfMethod = targetClass.getMethod("lastIndexOf",
        argumentClasses);
    Object[] lastIndexOfMethodArguments = new Object[] { new Integer(5),
        new Integer(4) };

    // get the key for the first method.
    Serializable firstCacheKey = this.executeGenerateArgumentHashCode(
        indexOfMethod, indexOfMethodArguments);

    // get the key for the second method.
    this.mockMethodInvocationControl.reset();
    Serializable secondCacheKey = this.executeGenerateArgumentHashCode(
        lastIndexOfMethod, lastIndexOfMethodArguments);

    assertFalse("The keys should not be equal", firstCacheKey
        .equals(secondCacheKey));
  }

  /**
   * Verifies that the method
   * <code>{@link CacheKeyGenerator#generateKey(MethodInvocation)}</code>
   * returns different keys for the given method and different sets of
   * arguments.
   */
  public void testGenerateKeyGeneratesDifferentKeysForSameMethodWithNotEqualArguments()
      throws Exception {

    Class targetClass = String.class;

    Method method = targetClass.getMethod("indexOf", new Class[] { int.class,
        int.class });
    Object[] methodArguments = new Object[] { new Integer(4), new Integer(0) };
    Object[] differentMethodArguments = new Object[] { new Integer(5),
        new Integer(2) };

    // get the key for the first method.
    Serializable firstCacheKey = this.executeGenerateArgumentHashCode(method,
        methodArguments);

    // get the key for the second method.
    this.mockMethodInvocationControl.reset();
    Serializable secondCacheKey = this.executeGenerateArgumentHashCode(method,
        differentMethodArguments);

    assertFalse("The keys should not be equal", firstCacheKey
        .equals(secondCacheKey));
  }

  /**
   * Verifies that the method
   * <code>{@link CacheKeyGenerator#generateKey(MethodInvocation)}</code>
   * always returns the same key for the given method and the same set of
   * arguments.
   */
  public void testGenerateKeyGeneratesSameKeyForSameMethodAndEqualArguments()
      throws Exception {

    Class targetClass = String.class;

    Method method = targetClass.getMethod("indexOf", new Class[] { int.class,
        int.class });
    Object[] methodArguments = new Object[] { new Integer(4), new Integer(0) };

    // get the key for the method.
    Serializable expectedCacheKey = this.executeGenerateArgumentHashCode(method,
        methodArguments);

    // get the key for the same method.
    this.mockMethodInvocationControl.reset();
    Serializable actualCacheKey = this.executeGenerateArgumentHashCode(method,
        methodArguments);

    assertEquals("<Cache key>", expectedCacheKey, actualCacheKey);
  }
}