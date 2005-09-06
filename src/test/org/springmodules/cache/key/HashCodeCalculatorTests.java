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
 * Unit Tests for <code>{@link HashCodeCalculator}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.3 $ $Date: 2005/09/06 01:41:29 $
 */
public final class HashCodeCalculatorTests extends TestCase {

  private HashCodeCalculator hashCodeCalculator;

  public HashCodeCalculatorTests(String name) {
    super(name);
  }

  protected void setUp() throws Exception {
    super.setUp();
    this.hashCodeCalculator = new HashCodeCalculator();
  }

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