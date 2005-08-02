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

package org.springmodules.cache.provider.ehcache;

import java.io.Serializable;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springmodules.cache.CacheWrapperException;
import org.springmodules.cache.EntryRetrievalException;
import org.springmodules.cache.provider.AbstractCacheProfileEditor;
import org.springmodules.cache.provider.AbstractCacheProviderFacadeImpl;
import org.springmodules.cache.provider.CacheProfile;
import org.springmodules.cache.provider.CacheProfileValidator;

/**
 * <p>
 * Delegates the caching functionality to EHCache.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.3 $ $Date: 2005/08/02 10:02:12 $
 */
public final class EhCacheFacade extends AbstractCacheProviderFacadeImpl {

  /**
   * Message logger.
   */
  private static Log logger = LogFactory.getLog(EhCacheFacade.class);

  /**
   * EHCache cache manager.
   */
  private CacheManager cacheManager;

  /**
   * Constructor.
   */
  public EhCacheFacade() {
    super();
  }

  /**
   * @see AbstractCacheProviderFacadeImpl#getCacheProfileEditor()
   */
  protected AbstractCacheProfileEditor getCacheProfileEditor() {
    return new EhCacheProfileEditor();
  }

  /**
   * @see AbstractCacheProviderFacadeImpl#getCacheProfileValidator()
   * @see EhCacheProfileValidator#validateCacheProfile(Object)
   */
  protected CacheProfileValidator getCacheProfileValidator() {
    return new EhCacheProfileValidator();
  }

  /**
   * @see AbstractCacheProviderFacadeImpl#onFlushCache(CacheProfile)
   */
  protected void onFlushCache(CacheProfile cacheProfile) {
    EhCacheProfile profile = (EhCacheProfile) cacheProfile;
    String cacheName = profile.getCacheName();

    if (!this.cacheManager.cacheExists(cacheName)) {
      if (logger.isInfoEnabled()) {
        String logMessage = "onFlushCache: Could not find EHCache cache: "
            + cacheName;
        logger.info(logMessage);
      }

    } else {
      Cache cache = this.cacheManager.getCache(cacheName);

      try {
        cache.removeAll();

      } catch (Exception exception) {
        String errorMessage = "Unable to flush cache. Cache profile: "
            + cacheProfile;

        logger.error(errorMessage, exception);
        throw new CacheWrapperException(errorMessage, exception);
      }
    }
  }

  /**
   * @see AbstractCacheProviderFacadeImpl#onGetFromCache(Serializable,
   *      CacheProfile)
   */
  protected Object onGetFromCache(Serializable cacheKey,
      CacheProfile cacheProfile) throws EntryRetrievalException {

    EhCacheProfile profile = (EhCacheProfile) cacheProfile;
    String cacheName = profile.getCacheName();

    if (!this.cacheManager.cacheExists(cacheName)) {
      String exceptionMessage = "Could not find EHCache cache: " + cacheName;
      String logMessage = "onGetFromCache: " + exceptionMessage;

      logger.info(logMessage);
      throw new EntryRetrievalException("Could not find EHCache cache: "
          + cacheName);
    }

    Cache cache = this.cacheManager.getCache(cacheName);
    Element cacheElement = null;

    try {
      cacheElement = cache.get(cacheKey);

    } catch (Exception exception) {
      String errorMessage = "Unable to retrieve an entry from the cache. Key: "
          + cacheKey + ". Cache Profile: " + cacheProfile;

      logger.error(errorMessage, exception);
      throw new CacheWrapperException(errorMessage, exception);
    }

    Object cachedObject = null;
    if (null != cacheElement) {
      cachedObject = cacheElement.getValue();
    }
    return cachedObject;
  }

  /**
   * @see AbstractCacheProviderFacadeImpl#onPutInCache(Serializable,
   *      CacheProfile, Object)
   */
  protected void onPutInCache(Serializable cacheKey, CacheProfile cacheProfile,
      Object objectToCache) {

    EhCacheProfile profile = (EhCacheProfile) cacheProfile;
    String cacheName = profile.getCacheName();

    if (!this.cacheManager.cacheExists(cacheName)) {
      if (logger.isInfoEnabled()) {
        String logMessage = "onPutInCache: Could not find EHCache cache: "
            + cacheName;
        logger.info(logMessage);
      }

    } else {
      Cache cache = this.cacheManager.getCache(cacheName);
      Element newCacheElement = new Element(cacheKey,
          (Serializable) objectToCache);

      cache.put(newCacheElement);
    }
  }

  /**
   * @see org.springmodules.cache.provider.CacheProviderFacade#removeFromCache(Serializable,
   *      String)
   */
  public void removeFromCache(Serializable cacheKey, String cacheProfileId) {
    CacheProfile cacheProfile = super.getCacheProfile(cacheProfileId);
    EhCacheProfile profile = (EhCacheProfile) cacheProfile;
    String cacheName = profile.getCacheName();

    if (!this.cacheManager.cacheExists(cacheName)) {
      if (logger.isInfoEnabled()) {
        String logMessage = "removeFromCache: Could not find EHCache cache: "
            + cacheName;
        logger.info(logMessage);
      }
    } else {
      Cache cache = this.cacheManager.getCache(cacheName);
      cache.remove(cacheKey);
    }
  }

  /**
   * Setter for the field <code>{@link #cacheManager}</code>.
   * 
   * @param cacheManager
   *          the new value to set
   */
  public void setCacheManager(CacheManager cacheManager) {
    this.cacheManager = cacheManager;
  }

  /**
   * <ul>
   * <li>Validates that <code>{@link #cacheManager}</code> is not
   * <code>null</code></li>
   * <li>Verifies that the state of <code>{@link #cacheManager}</code> is
   * 'active' (only if this facade is not configured to fail quietly in case of
   * an error when accessing the cache.)</li>
   * </ul>
   * 
   * @throws IllegalArgumentException
   *           if the Cache Manager is <code>null</code>.
   * @throws IllegalArgumentException
   *           if the status of the Cache Manager is not "Alive".
   * @see AbstractCacheProviderFacadeImpl#isFailQuietlyEnabled()
   * @see AbstractCacheProviderFacadeImpl#validateCacheManager()
   */
  protected void validateCacheManager() {
    if (null == this.cacheManager) {
      throw new IllegalStateException("The Cache Manager should not be null");
    }

    if (!super.isFailQuietlyEnabled()) {
      int cacheManagerStatus = this.cacheManager.getStatus();

      if (cacheManagerStatus != CacheManager.STATUS_ALIVE) {
        throw new IllegalStateException("Cache Manager is not alive");
      }
    }
  }

}