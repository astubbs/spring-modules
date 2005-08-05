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

import org.apache.jcs.engine.CacheElement;
import org.apache.jcs.engine.behavior.ICacheElement;
import org.apache.jcs.engine.behavior.IElementAttributes;
import org.apache.jcs.engine.control.CompositeCache;
import org.apache.jcs.engine.control.CompositeCacheManager;
import org.apache.jcs.engine.control.group.GroupAttrName;
import org.apache.jcs.engine.control.group.GroupId;
import org.springframework.util.StringUtils;
import org.springmodules.cache.provider.AbstractCacheProfileEditor;
import org.springmodules.cache.provider.AbstractCacheProviderFacadeImpl;
import org.springmodules.cache.provider.CacheAccessException;
import org.springmodules.cache.provider.CacheException;
import org.springmodules.cache.provider.CacheNotFoundException;
import org.springmodules.cache.provider.CacheProfile;
import org.springmodules.cache.provider.CacheProfileValidator;
import org.springmodules.cache.provider.InvalidConfigurationException;

/**
 * <p>
 * Facade for JCS.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.10 $ $Date: 2005/08/05 04:36:56 $
 */
public final class JcsFacade extends AbstractCacheProviderFacadeImpl {

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
   * JCS does not support cancellation of updates.
   * 
   * @see AbstractCacheProviderFacadeImpl#onCancelCacheUpdate(Serializable)
   */
  protected void onCancelCacheUpdate(Serializable cacheKey) {
    if (this.logger.isDebugEnabled()) {
      this.logger
          .debug("JCS does not support cancelation of updates to the cache");
    }
  }

  /**
   * @see AbstractCacheProviderFacadeImpl#onFlushCache(CacheProfile)
   * 
   * @throws CacheNotFoundException
   *           if the cache specified in the given profile cannot be found.
   * @throws CacheAccessException
   *           wrapping any unexpected exception thrown by the cache.
   */
  protected void onFlushCache(CacheProfile cacheProfile) throws CacheException {
    JcsProfile profile = (JcsProfile) cacheProfile;
    String cacheName = profile.getCacheName();

    CompositeCache cache = this.cacheManager.getCache(cacheName);
    if (cache == null) {
      throw new CacheNotFoundException(cacheName);
    }

    String cacheGroup = profile.getGroup();

    try {
      if (StringUtils.hasText(cacheGroup)) {
        GroupId groupId = new GroupId(cacheName, cacheGroup);
        cache.remove(groupId);

      } else {
        cache.removeAll();
      }

    } catch (Exception exception) {
      throw new CacheAccessException(exception);
    }
  }

  /**
   * @see AbstractCacheProviderFacadeImpl#onGetFromCache(Serializable,
   *      CacheProfile)
   * 
   * @throws CacheNotFoundException
   *           if the cache specified in the given profile cannot be found.
   * @throws CacheAccessException
   *           wrapping any unexpected exception thrown by the cache.
   */
  protected Object onGetFromCache(Serializable cacheKey,
      CacheProfile cacheProfile) throws CacheException {

    JcsProfile profile = (JcsProfile) cacheProfile;
    String cacheName = profile.getCacheName();

    CompositeCache cache = this.cacheManager.getCache(cacheName);
    if (cache == null) {
      throw new CacheNotFoundException(cacheName);
    }

    Serializable key = this.getKey(cacheKey, profile);
    Object cachedObject = null;

    try {
      ICacheElement cacheElement = cache.get(key);
      if (cacheElement != null) {
        cachedObject = cacheElement.getVal();
      }

    } catch (Exception exception) {
      throw new CacheAccessException(exception);
    }

    return cachedObject;
  }

  /**
   * @see AbstractCacheProviderFacadeImpl#onPutInCache(Serializable,
   *      CacheProfile, Object)
   * 
   * @throws CacheNotFoundException
   *           if the cache specified in the given profile cannot be found.
   * @throws CacheAccessException
   *           wrapping any unexpected exception thrown by the cache.
   */
  protected void onPutInCache(Serializable cacheKey, CacheProfile cacheProfile,
      Object objectToCache) throws CacheException {

    JcsProfile profile = (JcsProfile) cacheProfile;
    String cacheName = profile.getCacheName();

    CompositeCache cache = this.cacheManager.getCache(cacheName);

    if (cache == null) {
      throw new CacheNotFoundException(cacheName);
    }

    Serializable key = this.getKey(cacheKey, profile);
    ICacheElement newCacheElement = new CacheElement(cache.getCacheName(), key,
        objectToCache);

    IElementAttributes elementAttributes = cache.getElementAttributes().copy();
    newCacheElement.setElementAttributes(elementAttributes);

    try {
      cache.update(newCacheElement);

    } catch (Exception exception) {
      throw new CacheAccessException(exception);
    }
  }

  /**
   * @see AbstractCacheProviderFacadeImpl#onRemoveFromCache(Serializable,
   *      CacheProfile)
   * 
   * @throws CacheNotFoundException
   *           if the cache specified in the given profile cannot be found.
   * @throws CacheAccessException
   *           wrapping any unexpected exception thrown by the cache.
   */
  protected void onRemoveFromCache(Serializable cacheKey,
      CacheProfile cacheProfile) throws CacheException {

    JcsProfile profile = (JcsProfile) cacheProfile;
    String cacheName = profile.getCacheName();

    CompositeCache cache = this.cacheManager.getCache(cacheName);

    if (cache == null) {
      throw new CacheNotFoundException(cacheName);
    }

    Serializable key = this.getKey(cacheKey, profile);

    try {
      cache.remove(key);

    } catch (Exception exception) {
      throw new CacheAccessException(exception);
    }
  }

  public void setCacheManager(CompositeCacheManager cacheManager) {
    this.cacheManager = cacheManager;
  }

  /**
   * @see AbstractCacheProviderFacadeImpl#validateCacheManager()
   * 
   * @throws InvalidConfigurationException
   *           if the Cache Manager is <code>null</code>.
   */
  protected void validateCacheManager() throws InvalidConfigurationException {
    if (null == this.cacheManager) {
      throw new InvalidConfigurationException(
          "The Cache Manager should not be null");
    }
  }
}