/* 
 * Created on Jul 13, 2005
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
package org.springmodules;

/**
 * <p>
 * Test Cases for verifying the equals/hashcode contract.
 * </p>
 * 
 * @author Alex Ruiz
 */
public interface EqualsHashCodeTestCase {

  /**
   * Verifies the relationship requirement between the methods
   * <code>equals</code> and <code>hashCode</code>. It simply means that if
   * two objects are equal, then they must have the same hash code, however the
   * opposite is NOT true.
   */
  void testEqualsHashCodeRelationship();

  /**
   * <p>
   * Verifies that the implementation of the method <code>equals</code> is
   * transitive.
   * </p>
   * <p>
   * It means that if two objects are equal, they must remain equal as long as
   * they are not modified.
   * </p>
   */
  void testEqualsIsConsistent();

  /**
   * <p>
   * Verifies that the implementation of the method <code>equals</code> is
   * reflexive.
   * </p>
   * <p>
   * It simply means that the object must be equal to itself, which it would be
   * at any given instance; unless you intentionally override the equals method
   * to behave otherwise.
   * </p>
   */
  void testEqualsIsReflexive();

  /**
   * <p>
   * Verifies that the implementation of the method <code>equals</code> is
   * symmetric.
   * </p>
   * <p>
   * It means that if object of one class is equal to another class object, the
   * other class object must be equal to this class object. In other words, one
   * object can not unilaterally decide whether it is equal to another object;
   * two objects, and consequently the classes to which they belong, must
   * bilaterally decide if they are equal or not. They BOTH must agree.
   * </p>
   */
  void testEqualsIsSymmetric();

  /**
   * <p>
   * Verifies that the implementation of the method <code>equals</code> is
   * transitive.
   * </p>
   * <p>
   * It means that if the first object is equal to the second object and the
   * second object is equal to the third object; then the first object is equal
   * to the third object. In other words, if two objects agree that they are
   * equal, and follow the symmetry principle, one of them can not decide to
   * have a similar contract with another object of different class. All three
   * must agree and follow symmetry principle for various permutations of these
   * three classes.
   * </p>
   */
  void testEqualsIsTransitive();

  /**
   * <p>
   * Verifies that the implementation of the method <code>equals</code>
   * returns <code>false</code> if a <code>null</code> is passed as
   * argument.
   * </p>
   * <p>
   * Any instantiable class object is not equal to <code>null</code>, hence
   * the equals method must return false if a <code>null</code> is passed to
   * it as an argument. You have to ensure that your implementation of the
   * equals method returns false if a <code>null</code> is passed to it as an
   * argument.
   * </p>
   */
  void testEqualsNullComparison();
}
