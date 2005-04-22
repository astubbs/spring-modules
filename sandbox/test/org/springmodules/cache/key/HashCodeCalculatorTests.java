/* 
 * Created on Nov 19, 2004
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

/**
 * <p>
 * Unit Test for <code>{@link HashCodeCalculator}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/04/22 02:19:22 $
 */
public final class HashCodeCalculatorTests extends TestCase {

  /**
   * Instance of the class to test.
   */
  private HashCodeCalculator hashCodeCalculator;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the Test Case.
   */
  public HashCodeCalculatorTests(String name) {
    super(name);
  }

  /**
   * Sets up the test fixture.
   */
  protected void setUp() throws Exception {
    super.setUp();

    this.hashCodeCalculator = new HashCodeCalculator();
  }

  /**
   * Cleans up the test fixture.
   */
  protected void tearDown() throws Exception {
    super.tearDown();

    this.hashCodeCalculator = null;
  }

  /**
   * Tests <code>{@link HashCodeCalculator#append(int)}</code>. Verifies that
   * the calculated checksum and hash code are correct.
   */
  public void testAppend() {

    int count = 0;
    long expectedCheckSum = 0;
    int expectedHashCode = 17;

    int[] values = new int[] { 767867, 454564, 0, 50099, 0 };
    int valueCount = values.length;

    for (int i = 0; i < valueCount; i++) {
      int value = values[i];
      this.hashCodeCalculator.append(value);

      count++;
      value *= count;
      expectedHashCode = 37 * expectedHashCode + (value ^ (value >>> 32));
      expectedCheckSum += value;
    }

    long actualCheckSum = this.hashCodeCalculator.getCheckSum();
    int actualHashCode = this.hashCodeCalculator.getHashCode();

    assertEquals("<checkSum>", expectedCheckSum, actualCheckSum);
    assertEquals("<hashCode>", expectedHashCode, actualHashCode);
  }

}