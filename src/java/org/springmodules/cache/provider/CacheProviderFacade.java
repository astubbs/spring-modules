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

package org.springmodules.cache.provider;

import java.io.Serializable;

import org.springframework.beans.factory.InitializingBean;
import org.springmodules.cache.EntryRetrievalException;

/**
 * <p>
 * Unified interface for different cache providers.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/04/11 04:02:11 $
 */
public interface CacheProviderFacade extends InitializingBean {

  /**
   * Cancels the update being made to the cache.
   * 
   * @param cacheKey
   *          the key being used in the cache update.
   */
  void cancelCacheUpdate(Serializable cacheKey);

  /**
   * Flushes the cache.
   * 
   * @param cacheProfileIds
   *          the id(s) of the cache profile(s) that specif(y/ies) what and how
   *          to flush.
   */
  void flushCache(String[] cacheProfileIds);

  /**
   * Retrieves an entry from the cache.
   * 
   * @param cacheKey
   *          the key under which the entry is stored.
   * @param cacheProfileId
   *          the id of the cache profile that specifies how to retrieve an
   *          entry.
   * @return the cached entry.
   * @throws EntryRetrievalException
   *           if an unexpected error takes place when retrieving the entry from
   *           the cache.
   */
  Object getFromCache(Serializable cacheKey, String cacheProfileId)
      throws EntryRetrievalException;

  /**
   * Returns the value of the flag that indicates if an exception should thrown
   * or not when an error occurrs when accessing the cache provider.
   * 
   * @return <code>true</code> if no exception should be thrown if an error
   *         occurrs when accessing the cache provider.
   */
  boolean isFailQuietlyEnabled();

  /**
   * Stores an object in the cache.
   * 
   * @param cacheKey
   *          the key under which the object will be stored.
   * @param cacheProfileId
   *          the id of the cache profile that specifies how to store an object.
   * @param objectToCache
   *          the object to store in the cache.
   */
  void putInCache(Serializable cacheKey, String cacheProfileId,
      Object objectToCache);

  /**
   * Removes an object from the cache.
   * 
   * @param cacheKey
   *          the key under which the object is stored.
   * @param cacheProfileId
   *          the id of the cache profile that specifies how to store an object.
   */
  void removeFromCache(Serializable cacheKey, String cacheProfileId);
}