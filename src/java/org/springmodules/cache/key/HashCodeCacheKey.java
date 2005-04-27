/* 
 * Created on Oct 9, 2004
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

import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * <p>
 * Cache key which value is based on a pre-calculated hash code.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.3 $ $Date: 2005/04/27 01:41:24 $
 */
public final class HashCodeCacheKey implements CacheKey {

  /**
   * Version number of this class.
   * 
   * @see java.io.Serializable
   */
  private static final long serialVersionUID = 3904677167731454262L;

  /**
   * Number that helps keep the uniqueness of this key.
   */
  private long checkSum;

  /**
   * Pre-calculated hash code.
   */
  private int hashCode;

  /**
   * Constructor.
   */
  public HashCodeCacheKey() {
    super();
  }

  /**
   * Constructor.
   * 
   * @param checkSum
   *          the new value to assign to the member variable
   *          <code>checkSum</code>.
   * @param hashCode
   *          the new value to assign to the member variable
   *          <code>hashCode</code>.
   */
  public HashCodeCacheKey(long checkSum, int hashCode) {
    this();
    this.checkSum = checkSum;
    this.hashCode = hashCode;
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   * 
   * @param obj
   *          the reference object with which to compare.
   * @return <code>true</code> if this object is the same as the obj argument;
   *         <code>false</code> otherwise.
   * 
   * @see Object#equals(java.lang.Object)
   */
  public boolean equals(Object obj) {
    boolean equals = true;

    if (null == obj || !(obj instanceof HashCodeCacheKey)) {
      equals = false;
    } else if (this != obj) {
      HashCodeCacheKey cacheKey = (HashCodeCacheKey) obj;

      EqualsBuilder equalsBuilder = new EqualsBuilder();
      equalsBuilder.append(this.getCheckSum(), cacheKey.getCheckSum());
      equalsBuilder.append(this.getHashCode(), cacheKey.getHashCode());

      equals = equalsBuilder.isEquals();
    }

    return equals;
  }

  /**
   * Getter for field <code>{@link #checkSum}</code>.
   * 
   * @return the field <code>checkSum</code>.
   */
  public long getCheckSum() {
    return this.checkSum;
  }

  /**
   * Getter for field <code>{@link #hashCode}</code>.
   * 
   * @return the field <code>hashCode</code>.
   */
  public int getHashCode() {
    return this.hashCode;
  }

  /**
   * Returns a hash code value for the object. This method is supported for the
   * benefit of hashtables such as those provided by
   * <code>java.util.Hashtable</code>.
   * 
   * @return a hash code value for this object.
   * 
   * @see Object#hashCode()
   */
  public int hashCode() {
    return this.getHashCode();
  }

  /**
   * Setter for the field <code>{@link #checkSum}</code>.
   * 
   * @param checkSum
   *          the new value to set
   */
  public void setCheckSum(long checkSum) {
    this.checkSum = checkSum;
  }

  /**
   * Setter for the field <code>{@link #hashCode}</code>.
   * 
   * @param hashCode
   *          the new value to set
   */
  public void setHashCode(int hashCode) {
    this.hashCode = hashCode;
  }

  /**
   * Returns a string representation of the object. In general, the
   * <code>toString</code> method returns a string that "textually represents"
   * this object.
   * 
   * @return a string representation of the object.
   * 
   * @see Object#toString()
   */
  public String toString() {
    return this.getHashCode() + "|" + this.getCheckSum();
  }
}