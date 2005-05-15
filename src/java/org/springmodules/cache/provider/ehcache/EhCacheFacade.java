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

import org.apache.commons.lang.StringUtils;
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
 * @version $Revision: 1.1 $ $Date: 2005/05/15 02:14:15 $
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

    if (StringUtils.isNotEmpty(cacheName)) {
      Cache cache = this.cacheManager.getCache(cacheName);
      if (cache == null) {
        if (logger.isInfoEnabled()) {
          String logMessage = "Method 'onFlushCache(CacheProfile)'. Could not find EHCache cache: "
              + cacheName;
          logger.info(logMessage);
        }
      } else {
        try {
          cache.removeAll();
        } catch (Exception exception) {
          StringBuffer messageBuffer = new StringBuffer(64);
          messageBuffer
              .append("Exception thrown when flushing cache. Variable 'cacheProfile': ");
          messageBuffer.append(cacheProfile);
          String errorMessage = messageBuffer.toString();

          logger.error(errorMessage, exception);
          throw new CacheWrapperException(errorMessage, exception);
        } // end 'catch'
      } // end 'if (cache != null)'
    }
  }

  /**
   * @see AbstractCacheProviderFacadeImpl#onGetFromCache(Serializable,
   *      CacheProfile)
   */
  protected Object onGetFromCache(Serializable cacheKey,
      CacheProfile cacheProfile) throws EntryRetrievalException {

    Object cachedObject = null;
    EhCacheProfile profile = (EhCacheProfile) cacheProfile;

    String cacheName = profile.getCacheName();

    if (StringUtils.isNotEmpty(cacheName)) {
      if (!this.cacheManager.cacheExists(cacheName)) {
        String logMessage = "Method 'onGetFromCache(CacheKey, CacheProfile)'. Could not find EHCache cache: "
            + cacheName;

        logger.info(logMessage);
        throw new EntryRetrievalException("Could not find EHCache cache: "
            + cacheName);
      }

      Cache cache = this.cacheManager.getCache(cacheName);

      Element cacheElement = null;
      try {
        cacheElement = cache.get(cacheKey);
      } catch (Exception exception) {
        StringBuffer messageBuffer = new StringBuffer(64);
        messageBuffer
            .append("Exception thrown when retrieving an entry from the cache. ");
        messageBuffer.append("Variable 'cacheKey': ");
        messageBuffer.append(cacheKey);
        messageBuffer.append(". Variable 'cacheProfile': ");
        messageBuffer.append(cacheProfile);
        String errorMessage = messageBuffer.toString();

        logger.error(errorMessage, exception);
        throw new CacheWrapperException(errorMessage, exception);
      } // end 'catch'

      if (null != cacheElement) {
        cachedObject = cacheElement.getValue();
      }
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
    if (StringUtils.isNotEmpty(cacheName)) {
      Cache cache = this.cacheManager.getCache(cacheName);

      if (cache == null) {
        if (logger.isInfoEnabled()) {
          String logMessage = "Method 'onPutInCache(CacheKey, CacheProfile, Object)'. Could not find EHCache cache: "
              + cacheName;
          logger.info(logMessage);
        }
      } else {
        Element newCacheElement = new Element(cacheKey,
            (Serializable) objectToCache);

        cache.put(newCacheElement);
      }
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
    if (StringUtils.isNotEmpty(cacheName)) {
      Cache cache = this.cacheManager.getCache(cacheName);

      if (cache == null) {
        if (logger.isInfoEnabled()) {
          String logMessage = "Method 'removeFromCache(CacheKey, String)'. Could not find EHCache cache: "
              + cacheName;
          logger.info(logMessage);
        }
      } else {
        cache.remove(cacheKey);
      }
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
      } // end 'if (cacheManagerStatus != CacheManager.STATUS_ALIVE)'
    } // end 'if (!super.isFailQuietlyEnabled())'
  }

}