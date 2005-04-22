/* 
 * FixedValueCacheKey.java
 * 
 * Created on Oct 12, 2004
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

package org.springmodules.cache.mock;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springmodules.cache.key.CacheKey;

/**
 * <p>
 * Implementation of <code>{@link CacheKey}</code> that has a fixed value as
 * key.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/04/22 02:19:17 $
 */
public class FixedValueCacheKey implements CacheKey {

  /**
   * Version number of this class.
   * 
   * @see java.io.Serializable
   */

  private static final long serialVersionUID = 3905520510195609905L;

  /**
   * Fixed value of the key.
   */
  private String keyFixedValue;

  /**
   * Constructor.
   * 
   * @param keyFixedValue
   *          the value of the key.
   */
  public FixedValueCacheKey(String keyFixedValue) {

    super();
    this.keyFixedValue = keyFixedValue;
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   * 
   * @param obj
   *          the reference object with which to compare.
   * @return <code>true</code> if this object is the same as the obj argument;
   *         <code>false</code> otherwise.
   * @see #hashCode()
   */
  public boolean equals(Object obj) {
    boolean equals = true;

    if (null == obj || !(obj instanceof FixedValueCacheKey)) {
      equals = false;
    } else if (this != obj) {
      FixedValueCacheKey cacheKey = (FixedValueCacheKey) obj;

      EqualsBuilder equalsBuilder = new EqualsBuilder();
      equalsBuilder
          .append(this.getKeyFixedValue(), cacheKey.getKeyFixedValue());

      equals = equalsBuilder.isEquals();
    }

    return equals;
  }

  /**
   * Getter for field <code>{@link #keyFixedValue}</code>.
   * 
   * @return the field <code>keyFixedValue</code>.
   */
  public final String getKeyFixedValue() {
    return this.keyFixedValue;
  }

  /**
   * Returns a hash code value for the object. This method is supported for the
   * benefit of hashtables such as those provided by
   * <code>java.util.Hashtable</code>.
   * 
   * @return a hash code value for this object.
   */
  public int hashCode() {
    HashCodeBuilder hashCodeBuilder = new HashCodeBuilder(3, 11);
    hashCodeBuilder.append(this.getKeyFixedValue());

    int hashCode = hashCodeBuilder.toHashCode();
    return hashCode;
  }

  /**
   * Setter for the field <code>{@link #keyFixedValue}</code>.
   * 
   * @param keyFixedValue
   *          the new value to set
   */
  public final void setKeyFixedValue(String keyFixedValue) {
    this.keyFixedValue = keyFixedValue;
  }

  /**
   * Returns a string representation of the object. In general, the
   * <code>toString</code> method returns a string that "textually represents"
   * this object.
   * 
   * @return a string representation of the object.
   */
  public String toString() {
    ToStringBuilder toStringBuilder = new ToStringBuilder(this);
    toStringBuilder.append("keyFixedValue", this.getKeyFixedValue());

    String toString = toStringBuilder.toString();
    return toString;
  }

}