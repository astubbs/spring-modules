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

import java.beans.PropertyEditor;
import java.io.Serializable;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.springmodules.cache.provider.AbstractCacheProviderFacadeImpl;
import org.springmodules.cache.provider.CacheAccessException;
import org.springmodules.cache.provider.CacheException;
import org.springmodules.cache.provider.CacheNotFoundException;
import org.springmodules.cache.provider.CacheProfile;
import org.springmodules.cache.provider.CacheProfileEditor;
import org.springmodules.cache.provider.CacheProfileValidator;
import org.springmodules.cache.provider.InvalidConfigurationException;
import org.springmodules.cache.provider.InvalidObjectToCacheException;

/**
 * <p>
 * Facade for EHCache.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.12 $ $Date: 2005/09/20 03:50:25 $
 */
public final class EhCacheFacade extends AbstractCacheProviderFacadeImpl {

  /**
   * EHCache cache manager.
   */
  private CacheManager cacheManager;

  public EhCacheFacade() {
    super();
  }

  /**
   * @param cacheName
   *          the name of the cache.
   * @return the cache retrieved from the cache manager.
   * @throws CacheNotFoundException
   *           if the cache does not exist.
   * @throws CacheAccessException
   *           wrapping any unexpected exception thrown by the cache.
   */
  protected Cache getCache(String cacheName) {
    Cache cache = null;

    try {
      if (cacheManager.cacheExists(cacheName)) {
        cache = cacheManager.getCache(cacheName);
      }

    } catch (Exception exception) {
      throw new CacheAccessException(exception);
    }

    if (cache == null) {
      throw new CacheNotFoundException(cacheName);
    }

    return cache;
  }

  /**
   * @see AbstractCacheProviderFacadeImpl#getCacheProfileEditor()
   */
  protected PropertyEditor getCacheProfileEditor() {
    CacheProfileEditor editor = new CacheProfileEditor();
    editor.setCacheProfileClass(EhCacheProfile.class);
    return editor;
  }

  /**
   * @see AbstractCacheProviderFacadeImpl#getCacheProfileValidator()
   * @see EhCacheProfileValidator#validateCacheProfile(Object)
   */
  protected CacheProfileValidator getCacheProfileValidator() {
    return new EhCacheProfileValidator();
  }

  /**
   * @see AbstractCacheProviderFacadeImpl#isSerializableCacheElementRequired()
   */
  protected boolean isSerializableCacheElementRequired() {
    return true;
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
    EhCacheProfile profile = (EhCacheProfile) cacheProfile;
    String cacheName = profile.getCacheName();

    Cache cache = getCache(cacheName);

    try {
      cache.removeAll();

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

    EhCacheProfile profile = (EhCacheProfile) cacheProfile;
    String cacheName = profile.getCacheName();

    Cache cache = getCache(cacheName);
    Object cachedObject = null;

    try {
      Element cacheElement = cache.get(cacheKey);
      if (cacheElement != null) {
        cachedObject = cacheElement.getValue();
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
   * @throws InvalidObjectToCacheException
   *           if the object to store is not an implementation of
   *           <code>java.io.Serializable</code>.
   * @throws CacheNotFoundException
   *           if the cache specified in the given profile cannot be found.
   * @throws CacheAccessException
   *           wrapping any unexpected exception thrown by the cache.
   */
  protected void onPutInCache(Serializable cacheKey, CacheProfile cacheProfile,
      Object objectToCache) throws CacheException {

    EhCacheProfile profile = (EhCacheProfile) cacheProfile;
    String cacheName = profile.getCacheName();

    Cache cache = getCache(cacheName);
    Element newCacheElement = new Element(cacheKey,
        (Serializable) objectToCache);

    try {
      cache.put(newCacheElement);

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

    EhCacheProfile profile = (EhCacheProfile) cacheProfile;
    String cacheName = profile.getCacheName();

    Cache cache = getCache(cacheName);

    try {
      cache.remove(cacheKey);

    } catch (Exception exception) {
      throw new CacheAccessException(exception);
    }
  }

  public void setCacheManager(CacheManager newCacheManager) {
    cacheManager = newCacheManager;
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
   * @throws InvalidConfigurationException
   *           if the Cache Manager is <code>null</code>.
   * @throws InvalidConfigurationException
   *           if the status of the Cache Manager is not "Alive".
   * @see AbstractCacheProviderFacadeImpl#isFailQuietlyEnabled()
   * @see AbstractCacheProviderFacadeImpl#validateCacheManager()
   */
  protected void validateCacheManager() throws InvalidConfigurationException {
    if (null == cacheManager) {
      throw new InvalidConfigurationException(
          "The Cache Manager should not be null");
    }

    if (cacheManager.getStatus() != CacheManager.STATUS_ALIVE) {
      throw new InvalidConfigurationException("Cache Manager is not alive");
    }
  }

}