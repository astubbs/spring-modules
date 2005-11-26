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

import java.beans.PropertyEditor;
import java.io.Serializable;

import org.apache.jcs.engine.CacheElement;
import org.apache.jcs.engine.behavior.ICacheElement;
import org.apache.jcs.engine.behavior.IElementAttributes;
import org.apache.jcs.engine.control.CompositeCache;
import org.apache.jcs.engine.control.CompositeCacheManager;
import org.apache.jcs.engine.control.group.GroupAttrName;
import org.apache.jcs.engine.control.group.GroupId;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springmodules.cache.CacheException;
import org.springmodules.cache.CachingModel;
import org.springmodules.cache.FatalCacheException;
import org.springmodules.cache.FlushingModel;
import org.springmodules.cache.provider.AbstractCacheProviderFacade;
import org.springmodules.cache.provider.CacheAccessException;
import org.springmodules.cache.provider.CacheModelValidator;
import org.springmodules.cache.provider.CacheNotFoundException;
import org.springmodules.cache.provider.ReflectionCacheModelEditor;
import org.springmodules.cache.provider.jcs.JcsFlushingModel.CacheStruct;

/**
 * <p>
 * Facade for JCS.
 * </p>
 * 
 * @author Alex Ruiz
 */
public final class JcsFacade extends AbstractCacheProviderFacade {

  /**
   * JCS cache manager.
   */
  private CompositeCacheManager cacheManager;

  private CacheModelValidator cacheModelValidator;

  public JcsFacade() {
    super();
    cacheModelValidator = new JcsModelValidator();
  }

  /**
   * @param name
   *          the name of the cache.
   * @return the cache retrieved from the cache manager.
   * @throws CacheNotFoundException
   *           if the cache does not exist.
   */
  protected CompositeCache getCache(String name) {
    CompositeCache cache = cacheManager.getCache(name);
    if (cache == null) {
      throw new CacheNotFoundException(name);
    }

    return cache;
  }

  public CacheModelValidator getCacheModelValidator() {
    return cacheModelValidator;
  }

  public PropertyEditor getCachingModelEditor() {
    ReflectionCacheModelEditor editor = new ReflectionCacheModelEditor();
    editor.setCacheModelClass(JcsCachingModel.class);
    return editor;
  }

  public PropertyEditor getFlushingModelEditor() {
    return new JcsFlushingModelEditor();
  }

  /**
   * Returns the key of a cache entry.
   * 
   * @param key
   *          the generated key.
   * @param model
   *          the model that specifies how to retrieve or store an entry.
   * @return the key of a cache entry.
   */
  protected Serializable getKey(Serializable key, JcsCachingModel model) {
    Serializable newKey = key;

    String group = model.getGroup();
    if (StringUtils.hasText(group)) {
      GroupId groupId = new GroupId(model.getCacheName(), group);
      GroupAttrName groupAttrName = new GroupAttrName(groupId, key);
      newKey = groupAttrName;
    }

    return newKey;
  }

  /**
   * @see AbstractCacheProviderFacade#isSerializableCacheElementRequired()
   */
  protected boolean isSerializableCacheElementRequired() {
    return true;
  }

  /**
   * @see AbstractCacheProviderFacade#onFlushCache(FlushingModel)
   * 
   * @throws CacheNotFoundException
   *           if the cache specified in the given model cannot be found.
   * @throws CacheAccessException
   *           wrapping any unexpected exception thrown by the cache.
   */
  protected void onFlushCache(FlushingModel model) throws CacheException {
    JcsFlushingModel flushingModel = (JcsFlushingModel) model;

    CacheStruct[] cacheStructs = flushingModel.getCacheStructs();
    if (cacheStructs == null) {
      return;
    }

    try {
      int structCount = cacheStructs.length;

      for (int i = 0; i < structCount; i++) {
        CacheStruct cacheStruct = cacheStructs[i];
        if (cacheStruct == null) {
          continue;
        }

        String cacheName = cacheStruct.getCacheName();
        String[] groups = cacheStruct.getGroups();

        CompositeCache cache = getCache(cacheName);

        if (!ObjectUtils.isEmpty(groups)) {
          int groupCount = groups.length;
          for (int j = 0; j < groupCount; j++) {
            GroupId groupId = new GroupId(cacheName, groups[j]);
            cache.remove(groupId);
          }
        } else {
          cache.removeAll();
        }
      }
    } catch (Exception exception) {
      throw new CacheAccessException(exception);
    }
  }

  /**
   * @see AbstractCacheProviderFacade#onGetFromCache(Serializable, CachingModel)
   * 
   * @throws CacheNotFoundException
   *           if the cache specified in the given model cannot be found.
   * @throws CacheAccessException
   *           wrapping any unexpected exception thrown by the cache.
   */
  protected Object onGetFromCache(Serializable key, CachingModel model)
      throws CacheException {
    JcsCachingModel cachingModel = (JcsCachingModel) model;
    String cacheName = cachingModel.getCacheName();

    CompositeCache cache = getCache(cacheName);

    Serializable newKey = getKey(key, cachingModel);
    Object cachedObject = null;

    try {
      ICacheElement cacheElement = cache.get(newKey);
      if (cacheElement != null) {
        cachedObject = cacheElement.getVal();
      }

    } catch (Exception exception) {
      throw new CacheAccessException(exception);
    }

    return cachedObject;
  }

  /**
   * @see AbstractCacheProviderFacade#onPutInCache(Serializable, CachingModel,
   *      Object)
   * 
   * @throws CacheNotFoundException
   *           if the cache specified in the given model cannot be found.
   * @throws CacheAccessException
   *           wrapping any unexpected exception thrown by the cache.
   */
  protected void onPutInCache(Serializable key, CachingModel model, Object obj)
      throws CacheException {

    JcsCachingModel cachingModel = (JcsCachingModel) model;
    String cacheName = cachingModel.getCacheName();

    CompositeCache cache = getCache(cacheName);

    Serializable newKey = getKey(key, cachingModel);
    ICacheElement newCacheElement = new CacheElement(cache.getCacheName(),
        newKey, obj);

    IElementAttributes elementAttributes = cache.getElementAttributes().copy();
    newCacheElement.setElementAttributes(elementAttributes);

    try {
      cache.update(newCacheElement);

    } catch (Exception exception) {
      throw new CacheAccessException(exception);
    }
  }

  /**
   * @see AbstractCacheProviderFacade#onRemoveFromCache(Serializable,
   *      CachingModel)
   * 
   * @throws CacheNotFoundException
   *           if the cache specified in the given model cannot be found.
   * @throws CacheAccessException
   *           wrapping any unexpected exception thrown by the cache.
   */
  protected void onRemoveFromCache(Serializable key, CachingModel model)
      throws CacheException {
    JcsCachingModel cachingModel = (JcsCachingModel) model;
    String cacheName = cachingModel.getCacheName();

    CompositeCache cache = getCache(cacheName);

    Serializable newKey = getKey(key, cachingModel);

    try {
      cache.remove(newKey);

    } catch (Exception exception) {
      throw new CacheAccessException(exception);
    }
  }

  public void setCacheManager(CompositeCacheManager newCacheManager) {
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