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

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springmodules.AssertEqualsHashCode;
import org.springmodules.EqualsHashCodeTestCase;

/**
 * <p>
 * Unit Tests for <code>{@link HashCodeCacheKey}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.4 $ $Date: 2005/08/11 04:32:23 $
 */
public final class HashCodeCacheKeyTests extends TestCase implements
    EqualsHashCodeTestCase {

  private static Log logger = LogFactory.getLog(HashCodeCacheKeyTests.class);

  /**
   * Primary object (instance of the class to test).
   */
  private HashCodeCacheKey key;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the Test Case.
   */
  public HashCodeCacheKeyTests(String name) {
    super(name);
  }

  /**
   * Sets up the test fixture.
   */
  protected void setUp() throws Exception {
    super.setUp();
    this.key = new HashCodeCacheKey(44322, 544);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsHashCodeRelationship()
   */
  public void testEqualsHashCodeRelationship() {
    HashCodeCacheKey anotherKey = new HashCodeCacheKey(this.key.getCheckSum(),
        this.key.getHashCode());

    AssertEqualsHashCode.assertEqualsHashCodeRelationshipIsCorrect(this.key,
        anotherKey);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsConsistent()
   */
  public void testEqualsIsConsistent() {
    long checkSum = this.key.getCheckSum();
    int hashCode = this.key.getHashCode();

    HashCodeCacheKey anotherKey = new HashCodeCacheKey(checkSum, hashCode);

    assertEquals(this.key, anotherKey);

    anotherKey.setCheckSum(589l);
    assertFalse(this.key.equals(anotherKey));

    anotherKey.setCheckSum(checkSum);
    anotherKey.setHashCode(33);
    assertFalse(this.key.equals(anotherKey));
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsReflexive()
   */
  public void testEqualsIsReflexive() {
    AssertEqualsHashCode.assertEqualsIsReflexive(this.key);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsSymmetric()
   */
  public void testEqualsIsSymmetric() {
    HashCodeCacheKey anotherKey = new HashCodeCacheKey(this.key.getCheckSum(),
        this.key.getHashCode());

    AssertEqualsHashCode.assertEqualsIsSymmetric(this.key, anotherKey);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsTransitive()
   */
  public void testEqualsIsTransitive() {
    long checkSum = this.key.getCheckSum();
    int hashCode = this.key.getHashCode();

    HashCodeCacheKey secondKey = new HashCodeCacheKey(checkSum, hashCode);
    HashCodeCacheKey thirdKey = new HashCodeCacheKey(checkSum, hashCode);

    AssertEqualsHashCode
        .assertEqualsIsTransitive(this.key, secondKey, thirdKey);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsNullComparison()
   */
  public void testEqualsNullComparison() {
    assertFalse(this.key.equals(null));
  }

  /**
   * Verifies that the method <code>{@link HashCodeCacheKey#toString()}</code>
   * returns a String representation of <code>{@link HashCodeCacheKey}</code>.
   */
  public void testHashCode() {
    String expected = this.key.getHashCode() + "|" + this.key.getCheckSum();
    String actual = this.key.toString();

    logger.debug("Expected toString: " + expected);
    logger.debug("Actual toString:   " + actual);

    assertEquals("<ToString>", expected, actual);
  }
}