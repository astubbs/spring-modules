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
 * @version $Revision: 1.14 $ $Date: 2005/09/29 01:21:59 $
 */
public class FlushCache implements CacheAttribute {

  private static final long serialVersionUID = 3689909557149513778L;

  /**
   * Ids of the cache models to be used to flush the cache.
   */
  private String[] cacheModelIds;

  /**
   * Flag that indicates if the cache should be flushed before or after the
   * execution of the intercepted method.
   */
  private boolean flushBeforeExecution;

  public FlushCache() {
    super();
  }

  /**
   * @param cacheModelIdsCsv
   *          ids of the cache models to be used to flush the cache, separated
   *          by commas.
   */
  public FlushCache(String cacheModelIdsCsv) {
    this();
    setCacheModelIds(cacheModelIdsCsv);
  }

  /**
   * @param cacheModelIdsCsv
   *          ids of the cache models to be used to flush the cache, separated
   *          by commas.
   * @param flushedBeforeExecution
   */
  public FlushCache(String cacheModelIdsCsv, boolean flushedBeforeExecution) {
    this(cacheModelIdsCsv);
    setFlushBeforeExecution(flushedBeforeExecution);
  }

  public FlushCache(String[] cacheModelIds) {
    this();
    setCacheModelIds(cacheModelIds);
  }

  public FlushCache(String[] cacheModelIds, boolean flushedBeforeExecution) {
    this(cacheModelIds);
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
    if (!Arrays.equals(cacheModelIds, flushCache.cacheModelIds)) {
      return false;
    }

    return true;
  }

  public final String[] getCacheModelIds() {
    return cacheModelIds;
  }

  /**
   * @see Object#hashCode()
   */
  public int hashCode() {
    int multiplier = 31;
    int hash = 7;
    hash = multiplier * hash + (flushBeforeExecution ? 1 : 0);

    if (cacheModelIds == null) {
      hash = multiplier * hash;
    } else {
      int cacheModelIdCount = cacheModelIds.length;
      for (int i = 0; i < cacheModelIdCount; i++) {
        String cacheModelId = cacheModelIds[i];
        hash = multiplier * hash
            + (cacheModelId != null ? cacheModelId.hashCode() : 0);
      }
    }

    return hash;
  }

  public final boolean isFlushBeforeExecution() {
    return flushBeforeExecution;
  }

  /**
   * Creates <code>{@link #cacheModelIds}</code> from a String containing
   * comma-separated values.
   * 
   * @param cacheModelIdsCsv
   *          the String containing comma-separated values.
   */
  public final void setCacheModelIds(String cacheModelIdsCsv) {
    String[] newModelIds = null;
    if (StringUtils.hasText(cacheModelIdsCsv)) {
      newModelIds = StringUtils
          .commaDelimitedListToStringArray(cacheModelIdsCsv);
    }
    setCacheModelIds(newModelIds);
  }

  public final void setCacheModelIds(String[] newCacheModelIds) {
    cacheModelIds = newCacheModelIds;
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
    buffer.append("cacheModelIds=" + ArrayUtils.toString(cacheModelIds) + ", ");
    buffer.append("flushBeforeExecution=" + flushBeforeExecution + "]");

    return buffer.toString();
  }

}