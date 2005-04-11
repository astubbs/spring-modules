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

import org.springmodules.cache.key.CacheKeyGenerator;


/**
 * <p>
 * Superclass for aspects that perform caching.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/04/11 04:02:14 $
 */
public class CachingAspectSupport {

  /**
   * Canonical value held in cache to indicate that the return value of the
   * method to apply caching to is <code>null</code>.
   */
  public static final Object NULL_ENTRY = new Object();

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

  /**
   * Constructor.
   */
  public CachingAspectSupport() {

    super();
  }

  /**
   * Getter for field <code>{@link #cacheKeyGenerator}</code>.
   * 
   * @return the field <code>cacheKeyGenerator</code>.
   */
  protected final CacheKeyGenerator getCacheKeyGenerator() {
    return this.cacheKeyGenerator;
  }

  /**
   * Getter for field <code>{@link #cachingAttributeSource}</code>.
   * 
   * @return the field <code>cachingAttributeSource</code>.
   */
  public final CachingAttributeSource getCachingAttributeSource() {
    return this.cachingAttributeSource;
  }

  /**
   * Getter for field <code>{@link #entryStoredListener}</code>.
   * 
   * @return the field <code>entryStoredListener</code>.
   */
  public final EntryStoredListener getEntryStoredListener() {
    return this.entryStoredListener;
  }

  /**
   * Setter for the field <code>{@link #cacheKeyGenerator}</code>.
   * 
   * @param cacheKeyGenerator
   *          the new value to set
   */
  public final void setCacheKeyGenerator(CacheKeyGenerator cacheKeyGenerator) {
    this.cacheKeyGenerator = cacheKeyGenerator;
  }

  /**
   * Setter for the field <code>{@link #cachingAttributeSource}</code>.
   * 
   * @param cachingAttributeSource
   *          the new value to set
   */
  public final void setCachingAttributeSource(
      CachingAttributeSource cachingAttributeSource) {
    this.cachingAttributeSource = cachingAttributeSource;
  }

  /**
   * Setter for the field <code>{@link #entryStoredListener}</code>.
   * 
   * @param entryStoredListener
   *          the new value to set
   */
  public final void setEntryStoredListener(
      EntryStoredListener entryStoredListener) {
    this.entryStoredListener = entryStoredListener;
  }

}