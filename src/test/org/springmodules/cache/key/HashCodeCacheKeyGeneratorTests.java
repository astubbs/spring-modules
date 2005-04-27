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

import java.lang.reflect.Method;

/**
 * <p>
 * Unit Test for <code>{@link HashCodeCacheKeyGenerator}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/04/27 01:41:10 $
 */
public final class HashCodeCacheKeyGeneratorTests extends
    AbstractCacheKeyGeneratorTests {

  /**
   * <p>
   * Class used to test the method
   * <code>{@link HashCodeCacheKeyGenerator#getMethodArgumentHashCode(Object)}</code>.
   * </p>
   * 
   * @author Alex Ruiz
   * 
   * @see HashCodeCacheKeyGeneratorTests#argument
   * @see HashCodeCacheKeyGeneratorTests#testGetMethodArgumentHashCodeGeneratingHashCode()
   * @see HashCodeCacheKeyGeneratorTests#testGetMethodArgumentHashCodeNotGeneratingHashCode()
   */
  private class Argument {

    /**
     * Name of the argument.
     */
    private String name;

    /**
     * Getter for <code>{@link #name}</code>.
     * 
     * @return the value of the member variable <code>name</code>.
     */
    public String getName() {
      return this.name;
    }

    /**
     * Returns a hash code value for the object. This method is supported for
     * the benefit of hashtables such as those provided by
     * <code>java.util.Hashtable</code>.
     * 
     * @return a hash code value for this object.
     */
    public int hashCode() {
      return 10;
    }

    /**
     * Setter for <code>{@link #name}</code>.
     * 
     * @param name
     *          the new value to assign to the member variable <code>name</code>.
     */
    public void setName(String name) {
      this.name = name;
    }
  }

  /**
   * Object used as argument of the method
   * <code>{@link HashCodeCacheKeyGenerator#getMethodArgumentHashCode(Object)}</code>.
   */
  private Argument argument;

  /**
   * Primary object (instance of the class to test).
   */
  private HashCodeCacheKeyGenerator keyGenerator;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the Test Case.
   */
  public HashCodeCacheKeyGeneratorTests(String name) {
    super(name);
  }

  /**
   * Sets up the test fixture.
   */
  protected void afterSetUp() throws Exception {
    this.keyGenerator = new HashCodeCacheKeyGenerator();
  }

  /**
   * @see AbstractCacheKeyGeneratorTests#getCacheKeyGenerator()
   */
  protected CacheKeyGenerator getCacheKeyGenerator() {
    return this.keyGenerator;
  }

  /**
   * Sets up <code>{@link #argument}</code>.
   */
  private void setUpArgument() {
    this.argument = new Argument();
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

    CacheKey expectedCacheKey = new HashCodeCacheKey(checkSum, hashCode);

    // get the actual key.
    CacheKey actualCacheKey = super.executeGenerateArgumentHashCode(
        toStringMethod, null);

    assertEquals("<Cache key>", expectedCacheKey, actualCacheKey);
  }

  /**
   * Verifies that the method
   * <code>{@link HashCodeCacheKeyGenerator#getMethodArgumentHashCode(Object)}</code>
   * generates the key for the given method argument if the flag
   * 'generateArgumentHashCode' is set to <code>true</code>.
   */
  public void testGetMethodArgumentHashCodeGeneratingHashCode() {
    this.setUpArgument();
    this.keyGenerator.setGenerateArgumentHashCode(true);

    int generatedHashCode = this.keyGenerator
        .getMethodArgumentHashCode(this.argument);
    int argumentHashCode = this.argument.hashCode();

    assertFalse("The generated hash code '" + generatedHashCode
        + "' should not be equal to the argument's hash code '"
        + argumentHashCode + "'", argumentHashCode == generatedHashCode);
  }

  /**
   * Verifies that the method
   * <code>{@link HashCodeCacheKeyGenerator#getMethodArgumentHashCode(Object)}</code>
   * does not generate the key for the given method argument if the flag
   * 'generateArgumentHashCode' is set to <code>false</code>.
   */
  public void testGetMethodArgumentHashCodeNotGeneratingHashCode() {
    this.setUpArgument();
    this.keyGenerator.setGenerateArgumentHashCode(false);

    int generatedHashCode = this.keyGenerator
        .getMethodArgumentHashCode(this.argument);
    int argumentHashCode = this.argument.hashCode();

    assertEquals("<Argument hash code>", argumentHashCode, generatedHashCode);
  }

  /**
   * Verifies that the method
   * <code>{@link HashCodeCacheKeyGenerator#getMethodArgumentHashCode(Object)}</code>
   * returns zero if the given method argument is <code>null</code>.
   */
  public void testGetMethodArgumentHashCodeWithArgumentEqualToNull() {
    int actualArgumentHashCode = this.keyGenerator
        .getMethodArgumentHashCode(null);

    assertEquals("<Argument hash code>", 0, actualArgumentHashCode);
  }
}