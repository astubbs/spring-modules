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

  public void testGenerateKeyGeneratesDifferentKeysForNotEqualMethodsWithEqualArguments()
      throws Exception {
    Method indexOf = createStringIndexOfMethod();
    Object[] indexOfArgs = new Object[] { new Integer(4), new Integer(0) };

    Method lastIndexOf = createStringLastIndexOfMethod();
    Object[] lastIndexOfArgs = new Object[] { new Integer(4), new Integer(0) };

    Serializable key1 = executeGenerateArgumentHashCode(indexOf, indexOfArgs);

    methodInvocationControl.reset();
    Serializable key2 = executeGenerateArgumentHashCode(lastIndexOf,
        lastIndexOfArgs);

    assertFalse(key1.equals(key2));
  }

  public void testGenerateKeyGeneratesDifferentKeysForNotEqualMethodsWithNotEqualArguments()
      throws Exception {
    Method indexOf = createStringIndexOfMethod();
    Object[] indexOfArgs = new Object[] { new Integer(4), new Integer(0) };

    Method lastIndexOf = createStringLastIndexOfMethod();
    Object[] lastIndexOfArgs = new Object[] { new Integer(5), new Integer(4) };

    Serializable key1 = executeGenerateArgumentHashCode(indexOf, indexOfArgs);

    methodInvocationControl.reset();
    Serializable key2 = executeGenerateArgumentHashCode(lastIndexOf,
        lastIndexOfArgs);

    assertFalse(key1.equals(key2));
  }

  public void testGenerateKeyGeneratesDifferentKeysForSameMethodWithNotEqualArguments()
      throws Exception {
    Method method = createStringIndexOfMethod();
    Object[] args1 = new Object[] { new Integer(4), new Integer(0) };
    Serializable key1 = executeGenerateArgumentHashCode(method, args1);

    methodInvocationControl.reset();
    Object[] args2 = new Object[] { new Integer(5), new Integer(2) };
    Serializable key2 = executeGenerateArgumentHashCode(method, args2);

    assertFalse(key1.equals(key2));
  }

  public void testGenerateKeyGeneratesSameKeyForSameMethodAndEqualArguments()
      throws Exception {
    Method method = createStringIndexOfMethod();
    Object[] args = new Object[] { new Integer(4), new Integer(0) };

    Serializable expected = executeGenerateArgumentHashCode(method, args);

    methodInvocationControl.reset();
    Serializable actual = executeGenerateArgumentHashCode(method, args);

    assertEquals(expected, actual);
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
    methodInvocationControl.expectAndReturn(methodInvocation.getMethod(),
        method);

    // get the arguments of the invocated method.
    methodInvocationControl.expectAndReturn(methodInvocation.getArguments(),
        methodArguments);

    methodInvocationControl.replay();

    // get the key for the method.
    Serializable cacheKey = cacheKeyGenerator.generateKey(methodInvocation);

    methodInvocationControl.verify();
    methodInvocationControl.reset();
    
    return cacheKey;
  }

  /**
   * @return the instance of the class to test.
   */
  protected abstract CacheKeyGenerator getCacheKeyGenerator();

  protected final MethodInvocation getMethodInvocation() {
    return methodInvocation;
  }

  protected final MockControl getMethodInvocationControl() {
    return methodInvocationControl;
  }

  protected final void setUp() throws Exception {
    methodInvocationControl = MockControl.createControl(MethodInvocation.class);
    methodInvocation = (MethodInvocation) methodInvocationControl.getMock();

    afterSetUp();
  }

  private Method createStringIndexOfMethod() throws Exception {
    return String.class.getMethod("indexOf",
        new Class[] { int.class, int.class });
  }

  private Method createStringLastIndexOfMethod() throws Exception {
    return String.class.getMethod("lastIndexOf", new Class[] { int.class,
        int.class });
  }
}