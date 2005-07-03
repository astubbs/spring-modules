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

import org.springmodules.cache.CacheAttribute;

/**
 * <p>
 * Source-level metadata attribute that identifies the methods which return
 * value should be stored in the cache.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.6 $ $Date: 2005/07/03 04:33:11 $
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
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Cached)) {
      return false;
    }

    final Cached cached = (Cached) obj;

    if (this.cacheProfileId != null ? !this.cacheProfileId
        .equals(cached.cacheProfileId) : cached.cacheProfileId != null) {
      return false;
    }

    return true;
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
    int multiplier = 31;
    int hash = 7;
    hash = multiplier * hash
        + (this.cacheProfileId != null ? this.cacheProfileId.hashCode() : 0);
    return hash;
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
    StringBuffer buffer = new StringBuffer();
    buffer.append(this.getClass().getName() + ": ");
    buffer.append("cacheProfileId='" + this.cacheProfileId + "'; ");
    buffer.append("systemHashCode=" + System.identityHashCode(this));
    
    return buffer.toString();
  }

}