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

/**
 * <p>
 * Facade (unified interface) for different cache providers.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.7 $ $Date: 2005/09/29 01:21:45 $
 */
public interface CacheProviderFacade extends InitializingBean {

  /**
   * Cancels the update being made to the cache.
   * 
   * @param cacheKey
   *          the key being used in the cache update.
   * @throws CacheException
   *           if an unexpected error takes place when attempting to cancel the
   *           update.
   */
  void cancelCacheUpdate(Serializable cacheKey) throws CacheException;

  /**
   * Flushes the cache.
   * 
   * @param cacheModuleIds
   *          one or more ids of the cache modules that specify what and how to
   *          flush.
   * @throws CacheException
   *           if an unexpected error takes place when flushing the cache.
   */
  void flushCache(String[] cacheModuleIds) throws CacheException;

  /**
   * Retrieves an entry from the cache.
   * 
   * @param cacheKey
   *          the key under which the entry is stored.
   * @param cacheModelId
   *          the id of the cache model that specifies how to retrieve an entry.
   * @return the cached entry.
   * @throws CacheException
   *           if an unexpected error takes place when retrieving the entry from
   *           the cache.
   */
  Object getFromCache(Serializable cacheKey, String cacheModelId)
      throws CacheException;

  /**
   * @return the state of this cache provider facade.
   */
  CacheProviderFacadeStatus getStatus();

  /**
   * @return <code>true</code> if no exception should be thrown if an error
   *         takes place when the cache provider is being configured or
   *         accessed.
   */
  boolean isFailQuietlyEnabled();

  /**
   * Stores an object in the cache.
   * 
   * @param cacheKey
   *          the key under which the object will be stored.
   * @param cacheModelId
   *          the id of the cache model that specifies how to store an object.
   * @param objectToCache
   *          the object to store in the cache.
   * @throws CacheException
   *           if an unexpected error takes place when storing an object in the
   *           cache.
   */
  void putInCache(Serializable cacheKey, String cacheModelId,
      Object objectToCache) throws CacheException;

  /**
   * Removes an object from the cache.
   * 
   * @param cacheKey
   *          the key under which the object is stored.
   * @param cacheModelId
   *          the id of the cache model that specifies how to store an object.
   */
  void removeFromCache(Serializable cacheKey, String cacheModelId)
      throws CacheException;
}