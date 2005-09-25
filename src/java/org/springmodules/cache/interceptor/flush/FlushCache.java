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
import org.springmodules.util.ArrayUtils;

/**
 * <p>
 * Metadata attribute used to identify the methods that flush the cache when
 * executed.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.13 $ $Date: 2005/09/25 05:20:28 $
 */
public class FlushCache implements CacheAttribute {

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

  public FlushCache() {
    super();
  }

  /**
   * @param cacheProfileIdsCsv
   *          ids of the cache profiles to be used to flush the cache, separated
   *          by commas.
   */
  public FlushCache(String cacheProfileIdsCsv) {
    this();
    setCacheProfileIds(cacheProfileIdsCsv);
  }

  /**
   * @param cacheProfileIdsCsv
   *          ids of the cache profiles to be used to flush the cache, separated
   *          by commas.
   * @param flushedBeforeExecution
   */
  public FlushCache(String cacheProfileIdsCsv, boolean flushedBeforeExecution) {
    this(cacheProfileIdsCsv);
    setFlushBeforeExecution(flushedBeforeExecution);
  }

  public FlushCache(String[] cacheProfileIds) {
    this();
    setCacheProfileIds(cacheProfileIds);
  }

  public FlushCache(String[] cacheProfileIds, boolean flushedBeforeExecution) {
    this(cacheProfileIds);
    setFlushBeforeExecution(flushedBeforeExecution);
  }

  /**
   * @see Object#equals(Object)
   */
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof FlushCache)) {
      return false;
    }

    final FlushCache flushCache = (FlushCache) obj;

    if (flushBeforeExecution != flushCache.flushBeforeExecution) {
      return false;
    }
    if (!Arrays.equals(cacheProfileIds, flushCache.cacheProfileIds)) {
      return false;
    }

    return true;
  }

  public final String[] getCacheProfileIds() {
    return cacheProfileIds;
  }

  /**
   * @see Object#hashCode()
   */
  public int hashCode() {
    int multiplier = 31;
    int hash = 7;
    hash = multiplier * hash + (flushBeforeExecution ? 1 : 0);

    if (cacheProfileIds == null) {
      hash = multiplier * hash;
    } else {
      int cacheProfileIdCount = cacheProfileIds.length;
      for (int i = 0; i < cacheProfileIdCount; i++) {
        String cacheProfileId = cacheProfileIds[i];
        hash = multiplier * hash
            + (cacheProfileId != null ? cacheProfileId.hashCode() : 0);
      }
    }

    return hash;
  }

  public final boolean isFlushBeforeExecution() {
    return flushBeforeExecution;
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
    setCacheProfileIds(newProfileIds);
  }

  public final void setCacheProfileIds(String[] newCacheProfileIds) {
    cacheProfileIds = newCacheProfileIds;
  }

  public final void setFlushBeforeExecution(boolean newFlushBeforeExecution) {
    flushBeforeExecution = newFlushBeforeExecution;
  }

  /**
   * @see Object#toString()
   */
  public String toString() {
    StringBuffer buffer = new StringBuffer(getClass().getName());
    buffer.append("@" + System.identityHashCode(this) + "[");
    buffer.append("cacheProfileIds=" + ArrayUtils.toString(cacheProfileIds)
        + ", ");
    buffer.append("flushBeforeExecution=" + flushBeforeExecution + "]");

    return buffer.toString();
  }

}