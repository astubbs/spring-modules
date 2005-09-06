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
 * @version $Revision: 1.4 $ $Date: 2005/09/06 01:41:30 $
 */
public abstract class AbstractCacheKeyGeneratorTests extends TestCase {

  private MethodInvocation methodInvocation;

  private MockControl methodInvocationControl;

  public AbstractCacheKeyGeneratorTests() {
    super();
  }

  public AbstractCacheKeyGeneratorTests(String name) {
    super(name);
  }

  /**
   * Gives subclasses the opportunity to set up their own fixture.
   */
  protected void afterSetUp() throws Exception {
    // no implementation.
  }

  protected final Serializable executeGenerateArgumentHashCode(Method method,
      Object[] methodArguments) {

    CacheKeyGenerator cacheKeyGenerator = getCacheKeyGenerator();

    // get the method from the description of the invocation.
    this.methodInvocation.getMethod();
    this.methodInvocationControl.setReturnValue(method);

    // get the arguments of the invocated method.
    this.methodInvocation.getArguments();
    this.methodInvocationControl.setReturnValue(methodArguments);

    // set the state of the mock control to 'replay'.
    this.methodInvocationControl.replay();

    // get the key for the method.
    Serializable cacheKey = cacheKeyGenerator
        .generateKey(this.methodInvocation);

    // verify that the expectations of the mock control were met.
    this.methodInvocationControl.verify();

    return cacheKey;
  }

  /**
   * @return the instance of the class to test.
   */
  protected abstract CacheKeyGenerator getCacheKeyGenerator();

  protected final MethodInvocation getMethodInvocation() {
    return this.methodInvocation;
  }

  protected final MockControl getMethodInvocationControl() {
    return this.methodInvocationControl;
  }

  protected final void setUp() throws Exception {
    super.setUp();

    this.methodInvocationControl = MockControl
        .createControl(MethodInvocation.class);
    this.methodInvocation = (MethodInvocation) this.methodInvocationControl
        .getMock();

    afterSetUp();
  }

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
    Serializable firstCacheKey = executeGenerateArgumentHashCode(
        indexOfMethod, indexOfMethodArguments);

    // get the key for the second method.
    this.methodInvocationControl.reset();
    Serializable secondCacheKey = executeGenerateArgumentHashCode(
        lastIndexOfMethod, lastIndexOfMethodArguments);

    assertFalse("The keys should not be equal", firstCacheKey
        .equals(secondCacheKey));
  }

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
    Serializable firstCacheKey = executeGenerateArgumentHashCode(
        indexOfMethod, indexOfMethodArguments);

    // get the key for the second method.
    this.methodInvocationControl.reset();
    Serializable secondCacheKey = executeGenerateArgumentHashCode(
        lastIndexOfMethod, lastIndexOfMethodArguments);

    assertFalse("The keys should not be equal", firstCacheKey
        .equals(secondCacheKey));
  }

  public void testGenerateKeyGeneratesDifferentKeysForSameMethodWithNotEqualArguments()
      throws Exception {

    Class targetClass = String.class;

    Method method = targetClass.getMethod("indexOf", new Class[] { int.class,
        int.class });
    Object[] methodArguments = new Object[] { new Integer(4), new Integer(0) };
    Object[] differentMethodArguments = new Object[] { new Integer(5),
        new Integer(2) };

    // get the key for the first method.
    Serializable firstCacheKey = executeGenerateArgumentHashCode(method,
        methodArguments);

    // get the key for the second method.
    this.methodInvocationControl.reset();
    Serializable secondCacheKey = executeGenerateArgumentHashCode(method,
        differentMethodArguments);

    assertFalse("The keys should not be equal", firstCacheKey
        .equals(secondCacheKey));
  }

  public void testGenerateKeyGeneratesSameKeyForSameMethodAndEqualArguments()
      throws Exception {

    Class targetClass = String.class;

    Method method = targetClass.getMethod("indexOf", new Class[] { int.class,
        int.class });
    Object[] methodArguments = new Object[] { new Integer(4), new Integer(0) };

    // get the key for the method.
    Serializable expectedCacheKey = executeGenerateArgumentHashCode(
        method, methodArguments);

    // get the key for the same method.
    this.methodInvocationControl.reset();
    Serializable actualCacheKey = executeGenerateArgumentHashCode(method,
        methodArguments);

    assertEquals("<Cache key>", expectedCacheKey, actualCacheKey);
  }
}