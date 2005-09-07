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
import org.springmodules.cache.util.Strings;

/**
 * <p>
 * Source-level metadata attribute that identifies the methods which return
 * value should be stored in the cache.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.10 $ $Date: 2005/09/07 01:58:10 $
 */
public class Cached implements CacheAttribute {

  private static final long serialVersionUID = 3256728394032297785L;

  /**
   * The id of the cache profile to use.
   */
  private String cacheProfileId;

  public Cached() {
    super();
  }

  public Cached(String cacheProfileId) {
    this();
    setCacheProfileId(cacheProfileId);
  }

  /**
   * @see Object#equals(Object)
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

  public final String getCacheProfileId() {
    return this.cacheProfileId;
  }

  /**
   * @see Object#hashCode()
   */
  public int hashCode() {
    int multiplier = 31;
    int hash = 7;
    hash = multiplier * hash
        + (this.cacheProfileId != null ? this.cacheProfileId.hashCode() : 0);
    return hash;
  }

  public final void setCacheProfileId(String cacheProfileId) {
    this.cacheProfileId = cacheProfileId;
  }

  /**
   * @see Object#toString()
   */
  public String toString() {
    StringBuffer buffer = new StringBuffer(getClass().getName());
    buffer.append("@" + System.identityHashCode(this) + "[");
    buffer.append("cacheProfileId=" + Strings.quote(this.cacheProfileId) + "]");

    return buffer.toString();
  }
}