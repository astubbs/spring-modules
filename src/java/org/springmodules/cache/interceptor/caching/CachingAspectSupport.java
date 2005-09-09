/* 
 * Created on Sep 21, 2004
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

import java.io.Serializable;

import org.springmodules.cache.key.CacheKeyGenerator;

/**
 * <p>
 * Superclass for aspects that perform caching.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.6 $ $Date: 2005/09/09 02:18:56 $
 */
public class CachingAspectSupport {

  /**
   * Canonical value held in cache to indicate that the return value of the
   * method to apply caching to is <code>null</code>.
   */
  public static final Serializable NULL_ENTRY = new Serializable() {

    private static final long serialVersionUID = 3257007674280522803L;

    /**
     * @see Object#toString()
     */
    public String toString() {
      return "<NULL_ENTRY>";
    }
  };

  /**
   * Generates the key of a cache entry.
   */
  private CacheKeyGenerator cacheKeyGenerator;

  /**
   * Retrieves metadata attributes from class methods.
   */
  private CachingAttributeSource cachingAttributeSource;

  /**
   * Listener being notified each time an entry is stored in the cache.
   */
  private EntryStoredListener entryStoredListener;

  public CachingAspectSupport() {
    super();
  }

  protected final CacheKeyGenerator getCacheKeyGenerator() {
    return cacheKeyGenerator;
  }

  public final CachingAttributeSource getCachingAttributeSource() {
    return cachingAttributeSource;
  }

  public final EntryStoredListener getEntryStoredListener() {
    return entryStoredListener;
  }

  public final void setCacheKeyGenerator(CacheKeyGenerator newCacheKeyGenerator) {
    cacheKeyGenerator = newCacheKeyGenerator;
  }

  public final void setCachingAttributeSource(
      CachingAttributeSource newCachingAttributeSource) {
    cachingAttributeSource = newCachingAttributeSource;
  }

  public final void setEntryStoredListener(
      EntryStoredListener newEntryStoredListener) {
    entryStoredListener = newEntryStoredListener;
  }

}