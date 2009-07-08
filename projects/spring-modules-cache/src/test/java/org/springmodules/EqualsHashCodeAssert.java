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
 */
public abstract class EqualsHashCodeAssert {

  /**
   * @see EqualsHashCodeTestCase#testEqualsHashCodeRelationship()
   */
  public static void assertEqualsHashCodeRelationshipIsCorrect(Object obj1,
      Object obj2) {
    Assert.assertEquals(obj1, obj2);
    Assert.assertEquals(obj1.hashCode(), obj2.hashCode());
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsReflexive()
   */
  public static void assertEqualsIsReflexive(Object obj) {
    Assert.assertEquals(obj, obj);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsSymmetric()
   */
  public static void assertEqualsIsSymmetric(Object obj1, Object obj2) {
    Assert.assertEquals(obj1, obj2);
    Assert.assertEquals(obj2, obj1);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsTransitive()
   */
  public static void assertEqualsIsTransitive(Object obj1, Object obj2,
      Object obj3) {
    Assert.assertEquals(obj1, obj2);
    Assert.assertEquals(obj2, obj3);
    Assert.assertEquals(obj1, obj3);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsNullComparison()
   */
  public static void assertEqualsNullComparisonReturnsFalse(Object obj) {
    Assert.assertFalse(obj.equals(null));
  }
}
