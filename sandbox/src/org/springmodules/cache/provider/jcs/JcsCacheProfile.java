/* 
 * Created on Sep 24, 2004
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

package org.springmodules.cache.provider.jcs;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springmodules.cache.provider.CacheProfile;

/**
 * <p>
 * Set of configuration options needed for:
 * <ul>
 * <li>Retrieving an entry from a JCS cache</li>
 * <li>Storing an object in a JCS cache</li>
 * <li>Flushing one or more caches</li>
 * </ul>
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/04/12 08:37:49 $
 */
public class JcsCacheProfile implements CacheProfile {

  /**
   * Version number of this class.
   * 
   * @see java.io.Serializable
   */
  private static final long serialVersionUID = 3257282547976057398L;

  /**
   * Name of the JCS Cache.
   */
  private String cacheName;

  /**
   * The group the object to cache belongs to.
   */
  private String group;

  /**
   * Constructor.
   */
  public JcsCacheProfile() {
    super();
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

    if (null == obj || !(obj instanceof JcsCacheProfile)) {
      equals = false;
    } else if (this != obj) {
      JcsCacheProfile profile = (JcsCacheProfile) obj;

      EqualsBuilder equalsBuilder = new EqualsBuilder();
      equalsBuilder.append(this.getCacheName(), profile.getCacheName());
      equalsBuilder.append(this.getGroup(), profile.getGroup());

      equals = equalsBuilder.isEquals();
    }

    return equals;
  }

  /**
   * Getter for field <code>{@link #cacheName}</code>.
   * 
   * @return the field <code>cacheName</code>.
   */
  public final String getCacheName() {
    return this.cacheName;
  }

  /**
   * Getter for field <code>{@link #group}</code>.
   * 
   * @return the field <code>group</code>.
   */
  public final String getGroup() {
    return this.group;
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
    HashCodeBuilder hashCodeBuilder = new HashCodeBuilder(3, 7);
    hashCodeBuilder.append(this.getCacheName());
    hashCodeBuilder.append(this.getGroup());

    int hashCode = hashCodeBuilder.toHashCode();
    return hashCode;
  }

  /**
   * Setter for the field <code>{@link #cacheName}</code>.
   * 
   * @param cacheName
   *          the new value to set.
   */
  public final void setCacheName(String cacheName) {
    this.cacheName = cacheName;
  }

  /**
   * Setter for the field <code>{@link #group}</code>.
   * 
   * @param group
   *          the new value to set.
   */
  public final void setGroup(String group) {
    this.group = group;
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
    ToStringBuilder toStringBuilder = new ToStringBuilder(this);
    toStringBuilder.append("cacheName", this.getCacheName());
    toStringBuilder.append("group", this.getGroup());

    String toString = toStringBuilder.toString();
    return toString;
  }
}