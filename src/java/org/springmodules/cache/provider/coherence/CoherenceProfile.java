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

package org.springmodules.cache.provider.coherence;

import org.springmodules.cache.provider.CacheProfile;

/**
 * <p>
 * Configuration options needed to access Coherence.
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public class CoherenceProfile implements CacheProfile {

  private static final long serialVersionUID = -7033280312690333223L;

  /**
   * Name of the Coherence cache.
   */
  private String cacheName;

  public CoherenceProfile() {
    super();
  }

  public CoherenceProfile(String cacheName) {
    this();
    this.setCacheName(cacheName);
  }

  /**
   * @see Object#equals(Object)
   */
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof CoherenceProfile)) {
      return false;
    }

    final CoherenceProfile coherenceProfile = (CoherenceProfile) obj;

    if (this.cacheName != null ? !this.cacheName
        .equals(coherenceProfile.cacheName)
        : coherenceProfile.cacheName != null) {
      return false;
    }

    return true;
  }

  public final String getCacheName() {
    return this.cacheName;
  }

  /**
   * @see Object#hashCode()
   */
  public int hashCode() {
    int multiplier = 31;
    int hash = 7;
    hash = multiplier * hash
        + (this.cacheName != null ? this.cacheName.hashCode() : 0);
    return hash;
  }

  public final void setCacheName(String cacheName) {
    this.cacheName = cacheName;
  }

  /**
   * @see Object#toString()
   */
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append(this.getClass().getName());
    buffer.append("@" + System.identityHashCode(this) + "[");

    buffer.append("cacheName=");
    String formattedCacheName = null;
    if (this.cacheName != null) {
      formattedCacheName = "'" + this.cacheName + "'";
    }
    buffer.append(formattedCacheName + "]");

    return buffer.toString();
  }
}