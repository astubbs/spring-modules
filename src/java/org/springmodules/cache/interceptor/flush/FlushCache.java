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

package org.springmodules.cache.interceptor.flush;

import java.util.Arrays;

import org.springframework.util.StringUtils;
import org.springmodules.cache.CacheAttribute;

/**
 * <p>
 * Metadata attribute used to identify the methods that flush the cache when
 * executed.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.8 $ $Date: 2005/07/15 18:01:31 $
 */
public class FlushCache implements CacheAttribute {

  /**
   * Version number of this class.
   * 
   * @see java.io.Serializable
   */
  private static final long serialVersionUID = 3689909557149513778L;

  /**
   * Ids of the cache profiles to be used to flush the cache.
   */
  private String[] cacheProfileIds;

  /**
   * Flag that indicates if the cache should be flushed before or after the
   * execution of the intercepted method.
   */
  private boolean flushBeforeExecution;

  /**
   * Constructor.
   */
  public FlushCache() {
    super();
  }

  /**
   * Constructor.
   * 
   * @param cacheProfileIdsCsv
   *          ids of the cache profiles to be used to flush the cache, separated
   *          by commas.
   */
  public FlushCache(String cacheProfileIdsCsv) {
    this();
    this.setCacheProfileIds(cacheProfileIdsCsv);
  }

  /**
   * Constructor.
   * 
   * @param cacheProfileIdsCsv
   *          ids of the cache profiles to be used to flush the cache, separated
   *          by commas.
   * @param flushedBeforeExecution
   *          flag that indicates if the cache should be flushed before or after
   *          the execution of the intercepted method.
   */
  public FlushCache(String cacheProfileIdsCsv, boolean flushedBeforeExecution) {
    this(cacheProfileIdsCsv);
    this.setFlushBeforeExecution(flushedBeforeExecution);
  }

  /**
   * Constructor.
   * 
   * @param cacheProfileIds
   *          ids of the cache profiles to be used to flush the cache
   */
  public FlushCache(String[] cacheProfileIds) {
    this();
    this.setCacheProfileIds(cacheProfileIds);
  }

  /**
   * Constructor.
   * 
   * @param cacheProfileIds
   *          ids of the cache profiles to be used to flush the cache
   * @param flushedBeforeExecution
   *          flag that indicates if the cache should be flushed before or after
   *          the execution of the intercepted method.
   */
  public FlushCache(String[] cacheProfileIds, boolean flushedBeforeExecution) {
    this(cacheProfileIds);
    this.setFlushBeforeExecution(flushedBeforeExecution);
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
    if (!(obj instanceof FlushCache)) {
      return false;
    }

    final FlushCache flushCache = (FlushCache) obj;

    if (this.flushBeforeExecution != flushCache.flushBeforeExecution) {
      return false;
    }
    if (!Arrays.equals(this.cacheProfileIds, flushCache.cacheProfileIds)) {
      return false;
    }

    return true;
  }

  /**
   * Getter for field <code>{@link #cacheProfileIds}</code>.
   * 
   * @return the field <code>cacheProfileIds</code>.
   */
  public final String[] getCacheProfileIds() {
    return this.cacheProfileIds;
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
    int multiplier = 31;
    int hash = 7;
    hash = multiplier * hash + (this.flushBeforeExecution ? 1 : 0);

    if (this.cacheProfileIds == null) {
      hash = multiplier * hash;
    } else {
      int cacheProfileIdCount = this.cacheProfileIds.length;
      for (int i = 0; i < cacheProfileIdCount; i++) {
        String cacheProfileId = this.cacheProfileIds[i];
        hash = multiplier * hash
            + (cacheProfileId != null ? cacheProfileId.hashCode() : 0);
      }
    }

    return hash;
  }

  /**
   * Getter for field <code>{@link #flushBeforeExecution}</code>.
   * 
   * @return the field <code>flushBeforeExecution</code>.
   */
  public final boolean isFlushBeforeExecution() {
    return this.flushBeforeExecution;
  }

  /**
   * Creates <code>{@link #cacheProfileIds}</code> from a String containing
   * comma-separated values.
   * 
   * @param cacheProfileIdsCsv
   *          the String containing comma-separated values.
   */
  public final void setCacheProfileIds(String cacheProfileIdsCsv) {
    String[] newProfileIds = null;
    if (StringUtils.hasText(cacheProfileIdsCsv)) {
      newProfileIds = StringUtils
          .commaDelimitedListToStringArray(cacheProfileIdsCsv);
    }
    this.setCacheProfileIds(newProfileIds);
  }

  /**
   * Setter for the field <code>{@link #cacheProfileIds}</code>.
   * 
   * @param cacheProfileIds
   *          the new value to set
   */
  public final void setCacheProfileIds(String[] cacheProfileIds) {
    this.cacheProfileIds = cacheProfileIds;
  }

  /**
   * Setter for the field <code>{@link #flushBeforeExecution}</code>.
   * 
   * @param flushBeforeExecution
   *          the new value to set
   */
  public final void setFlushBeforeExecution(boolean flushBeforeExecution) {
    this.flushBeforeExecution = flushBeforeExecution;
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
    StringBuffer buffer = new StringBuffer();
    buffer.append(this.getClass().getName());
    buffer.append("@" + System.identityHashCode(this) + "[");
    buffer.append("cacheProfileIds=");

    if (this.cacheProfileIds == null) {
      buffer.append("null, ");

    } else {
      int cacheProfileIdCount = this.cacheProfileIds.length;

      if (cacheProfileIdCount == 0) {
        buffer.append("{}, ");

      } else {
        for (int i = 0; i < cacheProfileIdCount; i++) {
          if (i == 0) {
            buffer.append("{");
          } else {
            buffer.append(", ");
          }

          String cacheProfileId = this.cacheProfileIds[i];
          String formattedCacheProfileId = null;
          if (cacheProfileId != null) {
            formattedCacheProfileId = "'" + cacheProfileId + "'";
          }
          buffer.append(formattedCacheProfileId);
        }
        buffer.append("}, ");
      }
    }

    buffer.append("flushBeforeExecution=" + this.flushBeforeExecution + "]");

    return buffer.toString();
  }

}