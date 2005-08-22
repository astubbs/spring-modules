/* 
 * Created on Aug 1, 2005
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
 * Copyright @2005 the original author or authors.
 */
package org.springmodules.cache.provider.coherence;

import java.io.Serializable;
import java.util.ConcurrentModificationException;

import org.springmodules.cache.provider.AbstractCacheProfileEditor;
import org.springmodules.cache.provider.AbstractCacheProviderFacadeImpl;
import org.springmodules.cache.provider.CacheAccessException;
import org.springmodules.cache.provider.CacheException;
import org.springmodules.cache.provider.CacheNotFoundException;
import org.springmodules.cache.provider.CacheProfile;
import org.springmodules.cache.provider.CacheProfileValidator;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;

/**
 * <p>
 * Delegates the caching functionality to Coherence.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public class CoherenceFacade extends AbstractCacheProviderFacadeImpl {

  /**
   * Constructor.
   */
  public CoherenceFacade() {
    super();
  }

  /**
   * @see AbstractCacheProviderFacadeImpl#isSerializableCacheElementRequired()
   */
  protected boolean isSerializableCacheElementRequired() {
    return false;
  }

  /**
   * @see AbstractCacheProviderFacadeImpl#getCacheProfileEditor()
   */
  protected AbstractCacheProfileEditor getCacheProfileEditor() {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * @see AbstractCacheProviderFacadeImpl#getCacheProfileValidator()
   */
  protected CacheProfileValidator getCacheProfileValidator() {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * @see AbstractCacheProviderFacadeImpl#onFlushCache(CacheProfile)
   */
  protected void onFlushCache(CacheProfile cacheProfile) throws CacheException {
    CoherenceProfile profile = (CoherenceProfile) cacheProfile;
    String cacheName = profile.getCacheName();

    NamedCache cache = CacheFactory.getCache(cacheName);
    if (cache == null) {
      throw new CacheNotFoundException(cacheName);
    }

    try {
      cache.clear();

    } catch (Exception exception) {
      throw new CacheAccessException(exception);

    } finally {
      CacheFactory.releaseCache(cache);
    }
  }

  /**
   * @see AbstractCacheProviderFacadeImpl#onGetFromCache(Serializable,
   *      CacheProfile)
   */
  protected Object onGetFromCache(Serializable cacheKey,
      CacheProfile cacheProfile) throws CacheException {

    Object cachedObject = null;
    CoherenceProfile profile = (CoherenceProfile) cacheProfile;

    String cacheName = profile.getCacheName();
    NamedCache cache = CacheFactory.getCache(cacheName);

    if (cache == null) {
      throw new CacheNotFoundException(cacheName);
    }

    // TODO Check if the cache is active and/or empty.
    try {
      cachedObject = cache.get(cacheKey);

    } catch (Exception exception) {
      throw new CacheAccessException(exception);

    } finally {
      CacheFactory.releaseCache(cache);
    }

    return cachedObject;
  }

  /**
   * @see AbstractCacheProviderFacadeImpl#onPutInCache(Serializable,
   *      CacheProfile, Object)
   */
  protected void onPutInCache(Serializable cacheKey, CacheProfile cacheProfile,
      Object objectToCache) throws CacheException {

    CoherenceProfile profile = (CoherenceProfile) cacheProfile;
    String cacheName = profile.getCacheName();
    NamedCache cache = CacheFactory.getCache(cacheName);

    if (cache == null) {
      throw new CacheNotFoundException(cacheName);
    }

    try {
      cache.put(cacheKey, objectToCache);

    } catch (Exception exception) {
      throw new CacheAccessException(exception);

    } finally {
      CacheFactory.releaseCache(cache);
    }
  }

  /**
   * @see AbstractCacheProviderFacadeImpl#onRemoveFromCache(Serializable,
   *      CacheProfile)
   */
  protected void onRemoveFromCache(Serializable cacheKey,
      CacheProfile cacheProfile) throws CacheException {

    CoherenceProfile profile = (CoherenceProfile) cacheProfile;
    String cacheName = profile.getCacheName();
    NamedCache cache = CacheFactory.getCache(cacheName);

    if (cache == null) {
      throw new CacheNotFoundException(cacheName);
    }
    try {
      cache.remove(cacheKey);

    } catch (ConcurrentModificationException exception) {
      throw new CacheAccessException(exception);

    } finally {
      CacheFactory.releaseCache(cache);
    }
  }

  /**
   * @see AbstractCacheProviderFacadeImpl#validateCacheManager()
   */
  protected void validateCacheManager() {
    // TODO Auto-generated method stub

  }
}
