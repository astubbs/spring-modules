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

/**
 * <p>
 * Builds the checksum and hash code necessary to create a new instance of
 * <code>{@link HashCodeCacheKey}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.4 $ $Date: 2005/08/04 04:40:59 $
 */
public final class HashCodeCalculator {

  /**
   * Prime number used as multiplier.
   */
  private static final int MULTIPLIER = 37;

  /**
   * Checksum to build.
   */
  private long checkSum;

  /**
   * Counts the number of times <code>{@link #append(int)}</code> is executed.
   * It is also used to build <code>{@link #checkSum}</code> and
   * <code>{@link #hashCode}</code>.
   */
  private int count;

  /**
   * Hash code to build;
   */
  private int hashCode;

  public HashCodeCalculator() {
    super();
    this.hashCode = 17;
  }

  /**
   * Recalculates <code>{@link #checkSum}</code> and
   * <code>{@link #hashCode}</code> using the specified value.
   * 
   * @param value
   *          the specified value.
   */
  public void append(int value) {

    int valueToAppend = value;

    this.count++;
    valueToAppend *= this.count;

    this.hashCode = MULTIPLIER * this.hashCode
        + (valueToAppend ^ (valueToAppend >>> 32));

    this.checkSum += valueToAppend;
  }

  public final long getCheckSum() {
    return this.checkSum;
  }

  public final int getHashCode() {
    return this.hashCode;
  }

}