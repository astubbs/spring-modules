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

import org.springmodules.cache.provider.AbstractCacheProviderFacade;
import org.springmodules.cache.provider.CacheAccessException;
import org.springmodules.cache.provider.CacheException;
import org.springmodules.cache.provider.CacheNotFoundException;
import org.springmodules.cache.provider.CacheModel;
import org.springmodules.cache.provider.CacheModelEditor;
import org.springmodules.cache.provider.CacheModelValidator;
import org.springmodules.cache.provider.FatalCacheException;
import org.springmodules.cache.provider.ObjectCannotBeCachedException;

/**
 * <p>
 * Facade for EHCache.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.16 $ $Date: 2005/09/29 01:21:40 $
 */
public final class EhCacheFacade extends AbstractCacheProviderFacade {

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
   * @see AbstractCacheProviderFacade#getCacheModelEditor()
   */
  protected PropertyEditor getCacheModelEditor() {
    CacheModelEditor editor = new CacheModelEditor();
    editor.setCacheModelClass(EhCacheModel.class);
    return editor;
  }

  /**
   * @see AbstractCacheProviderFacade#getCacheModelValidator()
   * @see EhCacheModelValidator#validateCacheModel(Object)
   */
  protected CacheModelValidator getCacheModelValidator() {
    return new EhCacheModelValidator();
  }

  /**
   * @see AbstractCacheProviderFacade#isSerializableCacheElementRequired()
   */
  protected boolean isSerializableCacheElementRequired() {
    return true;
  }

  /**
   * @see AbstractCacheProviderFacade#onFlushCache(CacheModel)
   * 
   * @throws CacheNotFoundException
   *           if the cache specified in the given model cannot be found.
   * @throws CacheAccessException
   *           wrapping any unexpected exception thrown by the cache.
   */
  protected void onFlushCache(CacheModel cacheModel) throws CacheException {
    EhCacheModel ehCacheModel = (EhCacheModel) cacheModel;
    String cacheName = ehCacheModel.getCacheName();

    Cache cache = getCache(cacheName);

    try {
      cache.removeAll();

    } catch (Exception exception) {
      throw new CacheAccessException(exception);
    }
  }

  /**
   * @see AbstractCacheProviderFacade#onGetFromCache(Serializable,
   *      CacheModel)
   * 
   * @throws CacheNotFoundException
   *           if the cache specified in the given model cannot be found.
   * @throws CacheAccessException
   *           wrapping any unexpected exception thrown by the cache.
   */
  protected Object onGetFromCache(Serializable cacheKey,
      CacheModel cacheModel) throws CacheException {

    EhCacheModel ehCacheModel = (EhCacheModel) cacheModel;
    String cacheName = ehCacheModel.getCacheName();

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
   * @see AbstractCacheProviderFacade#onPutInCache(Serializable,
   *      CacheModel, Object)
   * 
   * @throws ObjectCannotBeCachedException
   *           if the object to store is not an implementation of
   *           <code>java.io.Serializable</code>.
   * @throws CacheNotFoundException
   *           if the cache specified in the given model cannot be found.
   * @throws CacheAccessException
   *           wrapping any unexpected exception thrown by the cache.
   */
  protected void onPutInCache(Serializable cacheKey, CacheModel cacheModel,
      Object objectToCache) throws CacheException {

    EhCacheModel ehCacheModel = (EhCacheModel) cacheModel;
    String cacheName = ehCacheModel.getCacheName();

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
   * @see AbstractCacheProviderFacade#onRemoveFromCache(Serializable,
   *      CacheModel)
   * 
   * @throws CacheNotFoundException
   *           if the cache specified in the given model cannot be found.
   * @throws CacheAccessException
   *           wrapping any unexpected exception thrown by the cache.
   */
  protected void onRemoveFromCache(Serializable cacheKey,
      CacheModel cacheModel) throws CacheException {

    EhCacheModel ehCacheModel = (EhCacheModel) cacheModel;
    String cacheName = ehCacheModel.getCacheName();

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
   * @see AbstractCacheProviderFacade#validateCacheManager()
   * 
   * @throws FatalCacheException
   *           if the cache manager is <code>null</code>.
   */
  protected void validateCacheManager() throws FatalCacheException {
    assertCacheManagerIsNotNull(cacheManager);
  }

}