/* 
 * Created on Oct 18, 2004
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

package org.springmodules.cache.key;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * <p>
 * Unit Tests for <code>{@link HashCodeCacheKeyGenerator}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.5 $ $Date: 2005/10/13 04:51:57 $
 */
public final class HashCodeCacheKeyGeneratorTests extends
    AbstractCacheKeyGeneratorTests {

  private class Argument {

    private String name;

    public String getName() {
      return name;
    }

    public int hashCode() {
      return 10;
    }

    public void setName(String newName) {
      name = newName;
    }
  }

  /**
   * Object used as argument of the method
   * <code>{@link HashCodeCacheKeyGenerator#getMethodArgumentHashCode(Object)}</code>.
   */
  private Argument argument;

  private HashCodeCacheKeyGenerator keyGenerator;

  public HashCodeCacheKeyGeneratorTests(String name) {
    super(name);
  }

  protected void afterSetUp() {
    keyGenerator = new HashCodeCacheKeyGenerator();
  }

  /**
   * @see AbstractCacheKeyGeneratorTests#getCacheKeyGenerator()
   */
  protected CacheKeyGenerator getCacheKeyGenerator() {
    return keyGenerator;
  }

  private void setUpArgument() {
    argument = new Argument();
  }

  /**
   * Verifies that the method
   * <code>{@link HashCodeCacheKeyGenerator#generateKey(org.aopalliance.intercept.MethodInvocation)}</code>
   * does not try to add the hash codes of the method arguments if there are not
   * any method arguments.
   */
  public void testGenerateKeyWithoutMethodArguments() throws Exception {
    Class targetClass = String.class;
    Method toStringMethod = targetClass.getMethod("toString", null);

    // get the expected key.
    HashCodeCalculator hashCodeCalculator = new HashCodeCalculator();
    int methodHashCode = System.identityHashCode(toStringMethod);
    hashCodeCalculator.append(methodHashCode);

    long checkSum = hashCodeCalculator.getCheckSum();
    int hashCode = hashCodeCalculator.getHashCode();

    Serializable expected = new HashCodeCacheKey(checkSum, hashCode);

    // get the actual key.
    Serializable actual = executeGenerateArgumentHashCode(toStringMethod, null);

    assertEquals(expected, actual);
  }

  /**
   * Verifies that the method
   * <code>{@link HashCodeCacheKeyGenerator#getMethodArgumentHashCode(Object)}</code>
   * generates the key for the given method argument if the flag
   * 'generateArgumentHashCode' is set to <code>true</code>.
   */
  public void testGetMethodArgumentHashCodeGeneratingHashCode() {
    setUpArgument();
    keyGenerator.setGenerateArgumentHashCode(true);

    int generatedHashCode = keyGenerator.getMethodArgumentHashCode(argument);
    int argumentHashCode = argument.hashCode();

    assertTrue("The generated hash code '" + generatedHashCode
        + "' should not be equal to the argument's hash code '"
        + argumentHashCode + "'", argumentHashCode != generatedHashCode);
  }

  /**
   * Verifies that the method
   * <code>{@link HashCodeCacheKeyGenerator#getMethodArgumentHashCode(Object)}</code>
   * does not generate the key for the given method argument if the flag
   * 'generateArgumentHashCode' is set to <code>false</code>.
   */
  public void testGetMethodArgumentHashCodeNotGeneratingHashCode() {
    setUpArgument();
    keyGenerator.setGenerateArgumentHashCode(false);

    int generatedHashCode = keyGenerator.getMethodArgumentHashCode(argument);
    int argumentHashCode = argument.hashCode();

    assertEquals(argumentHashCode, generatedHashCode);
  }

  /**
   * Verifies that the method
   * <code>{@link HashCodeCacheKeyGenerator#getMethodArgumentHashCode(Object)}</code>
   * returns zero if the given method argument is <code>null</code>.
   */
  public void testGetMethodArgumentHashCodeWithArgumentEqualToNull() {
    assertEquals(0, keyGenerator.getMethodArgumentHashCode(null));
  }
}