/* 
 * Created on Nov 10, 2004
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

package org.springmodules.cache.interceptor.caching;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springmodules.cache.CacheAttribute;

/**
 * <p>
 * Source-level metadata attribute that identifies the methods which return
 * value should be stored in the cache.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/04/11 04:02:14 $
 */
public class Cached implements CacheAttribute {

  /**
   * Version number of this class.
   * 
   * @see java.io.Serializable
   */
  private static final long serialVersionUID = 3256728394032297785L;

  /**
   * The id of the cache profile to use.
   */
  private String cacheProfileId;

  /**
   * Constructor.
   */
  public Cached() {
    super();
  }

  /**
   * Constructor.
   * 
   * @param cacheProfileId
   *          the id of the cache profile to use.
   */
  public Cached(String cacheProfileId) {
    this();
    this.setCacheProfileId(cacheProfileId);
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

    if (null == obj || !(obj instanceof Cached)) {
      equals = false;
    } else if (this != obj) {
      Cached cached = (Cached) obj;

      EqualsBuilder equalsBuilder = new EqualsBuilder();
      equalsBuilder
          .append(this.getCacheProfileId(), cached.getCacheProfileId());

      equals = equalsBuilder.isEquals();
    }

    return equals;
  }

  /**
   * Getter for field <code>{@link #cacheProfileId}</code>.
   * 
   * @return the field <code>cacheProfileId</code>.
   */
  public final String getCacheProfileId() {
    return this.cacheProfileId;
  }

  /**
   * Returns a hash code value for the object. This method is supported for the
   * benefit of hashtables such as those provided by
   * <code>{@link java.util.Hashtable}</code>.
   * 
   * @return a hash code value for this object.
   * 
   * @see Object#hashCode()
   */
  public int hashCode() {
    HashCodeBuilder hashCodeBuilder = new HashCodeBuilder(7, 17);
    hashCodeBuilder.append(this.getCacheProfileId());

    int hashCode = hashCodeBuilder.toHashCode();
    return hashCode;
  }

  /**
   * Setter for the field <code>{@link #cacheProfileId}</code>.
   * 
   * @param cacheProfileId
   *          the new value to set
   */
  public final void setCacheProfileId(String cacheProfileId) {
    this.cacheProfileId = cacheProfileId;
  }

  /**
   * Returns a string representation of the object. In general, the
   * <code>toString</code> method returns a string that "textually represents"
   * this object.
   * 
   * @return a string representation of the object.
   * 
   * @see Object#toString()
   * 
   */
  public String toString() {
    ToStringBuilder toStringBuilder = new ToStringBuilder(this);
    toStringBuilder.append("cacheProfileId", this.getCacheProfileId());

    String toString = toStringBuilder.toString();
    return toString;
  }

}