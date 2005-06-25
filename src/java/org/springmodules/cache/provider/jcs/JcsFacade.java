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

package org.springmodules.cache.provider.jcs;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jcs.engine.CacheElement;
import org.apache.jcs.engine.behavior.ICacheElement;
import org.apache.jcs.engine.behavior.IElementAttributes;
import org.apache.jcs.engine.control.CompositeCache;
import org.apache.jcs.engine.control.CompositeCacheManager;
import org.apache.jcs.engine.control.group.GroupAttrName;
import org.apache.jcs.engine.control.group.GroupId;
import org.springframework.util.StringUtils;
import org.springmodules.cache.CacheWrapperException;
import org.springmodules.cache.EntryRetrievalException;
import org.springmodules.cache.provider.AbstractCacheProfileEditor;
import org.springmodules.cache.provider.AbstractCacheProviderFacadeImpl;
import org.springmodules.cache.provider.CacheProfile;
import org.springmodules.cache.provider.CacheProfileValidator;

/**
 * <p>
 * Implementation of
 * <code>{@link org.springmodules.cache.provider.CacheProviderFacade}</code>
 * that uses JCS as the cache provider.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.7 $ $Date: 2005/06/25 06:53:20 $
 */
public final class JcsFacade extends AbstractCacheProviderFacadeImpl {

  /**
   * Message logger.
   */
  private static Log logger = LogFactory.getLog(JcsFacade.class);

  /**
   * JCS cache manager.
   */
  private CompositeCacheManager cacheManager;

  /**
   * Constructor.
   */
  public JcsFacade() {
    super();
  }

  /**
   * @see AbstractCacheProviderFacadeImpl#getCacheProfileEditor()
   */
  protected AbstractCacheProfileEditor getCacheProfileEditor() {
    return new JcsProfileEditor();
  }

  /**
   * @see AbstractCacheProviderFacadeImpl#getCacheProfileValidator()
   * @see JcsProfileValidator#validateCacheProfile(Object)
   */
  protected CacheProfileValidator getCacheProfileValidator() {
    return new JcsProfileValidator();
  }

  /**
   * Returns the key of a cache entry.
   * 
   * @param cacheKey
   *          the generated key.
   * @param profile
   *          the the cache profile that specifies how to retrieve or store an
   *          entry.
   * @return the key of a cache entry.
   */
  protected Serializable getKey(Serializable cacheKey, JcsProfile profile) {
    Serializable key = null;

    String group = profile.getGroup();
    if (!StringUtils.hasText(group)) {
      key = cacheKey;
    } else {
      GroupId groupId = new GroupId(profile.getCacheName(), group);
      GroupAttrName groupAttrName = new GroupAttrName(groupId, cacheKey);
      key = groupAttrName;
    }

    return key;
  }

  /**
   * @see AbstractCacheProviderFacadeImpl#onFlushCache(CacheProfile)
   */
  protected void onFlushCache(CacheProfile cacheProfile) {
    JcsProfile profile = (JcsProfile) cacheProfile;

    String cacheName = profile.getCacheName();
    if (StringUtils.hasText(cacheName)) {
      CompositeCache cache = this.cacheManager.getCache(cacheName);
      if (cache == null) {
        if (logger.isInfoEnabled()) {
          String logMessage = "Method 'onFlushCache(CacheProfile)'. Could not find JCS cache: "
              + cacheName;
          logger.info(logMessage);
        }
      } else {
        String cacheGroup = profile.getGroup();

        if (logger.isDebugEnabled()) {
          logger
              .debug("Method 'onFlushCache(CacheProfile)'. Variable 'cacheGroup': "
                  + cacheGroup);
        }

        if (!StringUtils.hasText(cacheGroup)) {
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
          }
        } else {
          GroupId groupId = new GroupId(cacheName, cacheGroup);
          cache.remove(groupId);
        }
      }
    }
  }

  /**
   * @see AbstractCacheProviderFacadeImpl#onGetFromCache(Serializable,
   *      CacheProfile)
   */
  protected Object onGetFromCache(Serializable cacheKey,
      CacheProfile cacheProfile) throws EntryRetrievalException {

    Object cachedObject = null;

    JcsProfile profile = (JcsProfile) cacheProfile;
    String cacheName = profile.getCacheName();
    
    if (StringUtils.hasText(cacheName)) {
      CompositeCache cache = this.cacheManager.getCache(cacheName);

      if (cache == null) {
        String logMessage = "Method 'onGetFromCache(CacheKey, CacheProfile)'. Could not find JCS cache: "
            + cacheName;

        logger.info(logMessage);
        throw new EntryRetrievalException("Could not find JCS cache: "
            + cacheName);
      }

      Serializable key = this.getKey(cacheKey, profile);
      ICacheElement cacheElement = cache.get(key);

      if (cacheElement != null) {
        cachedObject = cacheElement.getVal();
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
    JcsProfile profile = (JcsProfile) cacheProfile;

    String cacheName = profile.getCacheName();

    if (StringUtils.hasText(cacheName)) {
      CompositeCache cache = this.cacheManager.getCache(cacheName);

      if (cache == null) {
        if (logger.isInfoEnabled()) {
          String logMessage = "Method 'onPutInCache(CacheKey, CacheProfile, Object)'. Could not find JCS cache: "
              + cacheName;
          logger.info(logMessage);
        }
      } else {
        Serializable key = this.getKey(cacheKey, profile);

        ICacheElement newCacheElement = new CacheElement(cache
            .getCacheName(), key, objectToCache);

        IElementAttributes elementAttributes = cache.getElementAttributes()
            .copy();

        newCacheElement.setElementAttributes(elementAttributes);
        try {
          cache.update(newCacheElement);
        } catch (Exception exception) {
          String errorMessage = "Exception thrown when storing an object in the cache. Variable 'cacheProfile': "
              + cacheProfile;

          logger.error(errorMessage, exception);
          throw new CacheWrapperException(errorMessage, exception);
        } // end 'catch'
      } // end 'if (cache != null)'
    } // end 'if (StringUtils.isNotEmpty(cacheName))'
  }

  /**
   * @see org.springmodules.cache.provider.CacheProviderFacade#removeFromCache(Serializable,
   *      String)
   */
  public void removeFromCache(Serializable cacheKey, String cacheProfileId) {
    CacheProfile cacheProfile = super.getCacheProfile(cacheProfileId);

    if (cacheProfile != null) {
      JcsProfile profile = (JcsProfile) cacheProfile;

      String cacheName = profile.getCacheName();

      if (StringUtils.hasText(cacheName)) {
        CompositeCache cache = this.cacheManager.getCache(cacheName);

        if (cache == null) {
          if (logger.isInfoEnabled()) {
            String logMessage = "Method 'removeFromCache(CacheKey, String)'. Could not find JCS cache: "
                + cacheName;
            logger.info(logMessage);
          }
        } else {
          Serializable key = this.getKey(cacheKey, profile);
          cache.remove(key);
        } // end 'else'
      } // end 'if (StringUtils.isNotEmpty(cacheName))'
    }
  }

  /**
   * Setter for the field <code>{@link #cacheManager}</code>.
   * 
   * @param cacheManager
   *          the new value to set
   */
  public void setCacheManager(CompositeCacheManager cacheManager) {
    this.cacheManager = cacheManager;
  }

  /**
   * @see AbstractCacheProviderFacadeImpl#validateCacheManager()
   */
  protected void validateCacheManager() {
    if (null == this.cacheManager) {
      throw new IllegalStateException("The Cache Manager should not be null");
    }
  }
}