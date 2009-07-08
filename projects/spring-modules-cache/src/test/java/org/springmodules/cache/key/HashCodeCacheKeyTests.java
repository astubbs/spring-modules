/* 
 * Created on Oct 15, 2004
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springmodules.AbstractEqualsHashCodeTestCase;

/**
 * <p>
 * Unit Tests for <code>{@link HashCodeCacheKey}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public final class HashCodeCacheKeyTests extends AbstractEqualsHashCodeTestCase {

  private static Log logger = LogFactory.getLog(HashCodeCacheKeyTests.class);

  private HashCodeCacheKey key;

  public HashCodeCacheKeyTests(String name) {
    super(name);
  }

  /**
   * @see org.springmodules.EqualsHashCodeTestCase#testEqualsHashCodeRelationship()
   */
  public void testEqualsHashCodeRelationship() {
    HashCodeCacheKey anotherKey = new HashCodeCacheKey(key.getCheckSum(), key
        .getHashCode());
    assertEqualsHashCodeRelationshipIsCorrect(key, anotherKey);
  }

  /**
   * @see org.springmodules.EqualsHashCodeTestCase#testEqualsIsConsistent()
   */
  public void testEqualsIsConsistent() {
    long checkSum = key.getCheckSum();
    int hashCode = key.getHashCode();

    HashCodeCacheKey anotherKey = new HashCodeCacheKey(checkSum, hashCode);
    assertEquals(key, anotherKey);

    anotherKey.setCheckSum(589l);
    assertFalse(key.equals(anotherKey));

    anotherKey.setCheckSum(checkSum);
    anotherKey.setHashCode(33);
    assertFalse(key.equals(anotherKey));
  }

  /**
   * @see org.springmodules.EqualsHashCodeTestCase#testEqualsIsReflexive()
   */
  public void testEqualsIsReflexive() {
    assertEqualsIsReflexive(key);
  }

  /**
   * @see org.springmodules.EqualsHashCodeTestCase#testEqualsIsSymmetric()
   */
  public void testEqualsIsSymmetric() {
    HashCodeCacheKey anotherKey = new HashCodeCacheKey(key.getCheckSum(), key
        .getHashCode());
    assertEqualsIsSymmetric(key, anotherKey);
  }

  /**
   * @see org.springmodules.EqualsHashCodeTestCase#testEqualsIsTransitive()
   */
  public void testEqualsIsTransitive() {
    long checkSum = key.getCheckSum();
    int hashCode = key.getHashCode();

    HashCodeCacheKey secondKey = new HashCodeCacheKey(checkSum, hashCode);
    HashCodeCacheKey thirdKey = new HashCodeCacheKey(checkSum, hashCode);

    assertEqualsIsTransitive(key, secondKey, thirdKey);
  }

  /**
   * @see org.springmodules.EqualsHashCodeTestCase#testEqualsNullComparison()
   */
  public void testEqualsNullComparison() {
    assertEqualsNullComparisonReturnsFalse(key);
  }

  /**
   * Verifies that the method <code>{@link HashCodeCacheKey#toString()}</code>
   * returns a String representation of <code>{@link HashCodeCacheKey}</code>.
   */
  public void testToString() {
    String expected = key.getHashCode() + "|" + key.getCheckSum();
    String actual = key.toString();

    logger.debug("Expected toString: " + expected);
    logger.debug("Actual toString:   " + actual);

    assertEquals(expected, actual);
  }

  protected void setUp() {
    key = new HashCodeCacheKey(44322, 544);
  }
}