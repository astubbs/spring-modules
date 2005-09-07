/* 
 * Created on Aug 8, 2005
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

import junit.framework.Assert;

/**
 * <p>
 * Helper methods for implementations of
 * <code>{@link EqualsHashCodeTestCase}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public abstract class EqualsHashCodeAssert extends Assert {

  /**
   * @see EqualsHashCodeTestCase#testEqualsHashCodeRelationship()
   */
  public static void assertEqualsHashCodeRelationshipIsCorrect(Object first,
      Object second) {
    assertEquals(first, second);
    assertEquals(first.hashCode(), second.hashCode());
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsReflexive()
   */
  public static void assertEqualsIsReflexive(Object obj) {
    assertEquals(obj, obj);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsSymmetric()
   */
  public static void assertEqualsIsSymmetric(Object first, Object second) {
    assertEquals(first, second);
    assertEquals(second, first);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsTransitive()
   */
  public static void assertEqualsIsTransitive(Object first, Object second,
      Object third) {
    assertEquals(first, second);
    assertEquals(second, third);
    assertEquals(first, third);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsNullComparison()
   */
  public static void assertEqualsNullComparisonReturnsFalse(Object obj) {
    assertFalse(obj.equals(null));
  }
}
