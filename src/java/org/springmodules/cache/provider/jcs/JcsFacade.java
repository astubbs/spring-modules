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
import org.springframework.util.StringUtils;
import org.springmodules.cache.provider.AbstractCacheProviderFacade;
import org.springmodules.cache.provider.CacheAccessException;
import org.springmodules.cache.provider.CacheException;
import org.springmodules.cache.provider.CacheNotFoundException;
import org.springmodules.cache.provider.CacheModel;
import org.springmodules.cache.provider.CacheModelEditor;
import org.springmodules.cache.provider.CacheModelValidator;
import org.springmodules.cache.provider.FatalCacheException;

/**
 * <p>
 * Facade for JCS.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.22 $ $Date: 2005/09/29 01:22:01 $
 */
public final class JcsFacade extends AbstractCacheProviderFacade {

  /**
   * JCS cache manager.
   */
  private CompositeCacheManager cacheManager;

  public JcsFacade() {
    super();
  }

  /**
   * @param cacheName
   *          the name of the cache.
   * @return the cache retrieved from the cache manager.
   * @throws CacheNotFoundException
   *           if the cache does not exist.
   */
  protected CompositeCache getCache(String cacheName) {
    CompositeCache cache = cacheManager.getCache(cacheName);
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
    editor.setCacheModelClass(JcsModel.class);
    return editor;
  }

  /**
   * @see AbstractCacheProviderFacade#getCacheModelValidator()
   * @see JcsModelValidator#validateCacheModel(Object)
   */
  protected CacheModelValidator getCacheModelValidator() {
    return new JcsModelValidator();
  }

  /**
   * Returns the key of a cache entry.
   * 
   * @param cacheKey
   *          the generated key.
   * @param jcsModel
   *          the the cache model that specifies how to retrieve or store an
   *          entry.
   * @return the key of a cache entry.
   */
  protected Serializable getKey(Serializable cacheKey, JcsModel jcsModel) {
    Serializable key = cacheKey;

    String group = jcsModel.getGroup();
    if (StringUtils.hasText(group)) {
      GroupId groupId = new GroupId(jcsModel.getCacheName(), group);
      GroupAttrName groupAttrName = new GroupAttrName(groupId, cacheKey);
      key = groupAttrName;
    }

    return key;
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
    JcsModel jcsModel = (JcsModel) cacheModel;
    String cacheName = jcsModel.getCacheName();

    CompositeCache cache = getCache(cacheName);

    String cacheGroup = jcsModel.getGroup();

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
   * @see AbstractCacheProviderFacade#onGetFromCache(Serializable, CacheModel)
   * 
   * @throws CacheNotFoundException
   *           if the cache specified in the given model cannot be found.
   * @throws CacheAccessException
   *           wrapping any unexpected exception thrown by the cache.
   */
  protected Object onGetFromCache(Serializable cacheKey, CacheModel cacheModel)
      throws CacheException {

    JcsModel jcsModel = (JcsModel) cacheModel;
    String cacheName = jcsModel.getCacheName();

    CompositeCache cache = getCache(cacheName);

    Serializable key = getKey(cacheKey, jcsModel);
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
   * @see AbstractCacheProviderFacade#onPutInCache(Serializable, CacheModel,
   *      Object)
   * 
   * @throws CacheNotFoundException
   *           if the cache specified in the given model cannot be found.
   * @throws CacheAccessException
   *           wrapping any unexpected exception thrown by the cache.
   */
  protected void onPutInCache(Serializable cacheKey, CacheModel cacheModel,
      Object objectToCache) throws CacheException {

    JcsModel jcsModule = (JcsModel) cacheModel;
    String cacheName = jcsModule.getCacheName();

    CompositeCache cache = getCache(cacheName);

    Serializable key = getKey(cacheKey, jcsModule);
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
   * @see AbstractCacheProviderFacade#onRemoveFromCache(Serializable,
   *      CacheModel)
   * 
   * @throws CacheNotFoundException
   *           if the cache specified in the given model cannot be found.
   * @throws CacheAccessException
   *           wrapping any unexpected exception thrown by the cache.
   */
  protected void onRemoveFromCache(Serializable cacheKey, CacheModel cacheModel)
      throws CacheException {

    JcsModel jcsModel = (JcsModel) cacheModel;
    String cacheName = jcsModel.getCacheName();

    CompositeCache cache = getCache(cacheName);

    Serializable key = getKey(cacheKey, jcsModel);

    try {
      cache.remove(key);

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