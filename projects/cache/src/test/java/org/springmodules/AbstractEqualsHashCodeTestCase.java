/* 
 * Created on Oct 14, 2005
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

import junit.framework.TestCase;

/**
 * <p>
 * Default implementation of
 * <code>{@link org.springmodules.EqualsHashCodeTestCase}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public abstract class AbstractEqualsHashCodeTestCase extends TestCase implements
    EqualsHashCodeTestCase {

  /**
   * @see EqualsHashCodeTestCase#testEqualsHashCodeRelationship()
   */
  public static void assertEqualsHashCodeRelationshipIsCorrect(Object obj1,
      Object obj2) {
    EqualsHashCodeAssert.assertEqualsHashCodeRelationshipIsCorrect(obj1, obj2);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsReflexive()
   */
  public static void assertEqualsIsReflexive(Object obj) {
    EqualsHashCodeAssert.assertEqualsIsReflexive(obj);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsSymmetric()
   */
  public static void assertEqualsIsSymmetric(Object obj1, Object obj2) {
    EqualsHashCodeAssert.assertEqualsIsSymmetric(obj1, obj2);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsTransitive()
   */
  public static void assertEqualsIsTransitive(Object obj1, Object obj2,
      Object obj3) {
    EqualsHashCodeAssert.assertEqualsIsTransitive(obj1, obj2, obj3);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsNullComparison()
   */
  public static void assertEqualsNullComparisonReturnsFalse(Object obj) {
    EqualsHashCodeAssert.assertEqualsNullComparisonReturnsFalse(obj);
  }

  public AbstractEqualsHashCodeTestCase() {
    super();
  }

  public AbstractEqualsHashCodeTestCase(String newName) {
    super(newName);
  }

}
