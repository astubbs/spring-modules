/* 
 * Created on Sep 1, 2005
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
package org.springmodules.cache.provider.jboss;

import java.beans.PropertyEditor;
import java.io.Serializable;

import org.jboss.cache.TreeCache;
import org.springmodules.cache.CachingModel;
import org.springmodules.cache.FatalCacheException;
import org.springmodules.cache.FlushingModel;
import org.springmodules.cache.provider.AbstractCacheProviderFacade;
import org.springmodules.cache.provider.CacheAccessException;
import org.springmodules.cache.provider.ReflectionCacheModelEditor;
import org.springmodules.cache.provider.CacheModelValidator;

/**
 * <p>
 * Facade for JBossCache.
 * </p>
 * 
 * @author Alex Ruiz
 */
public final class JbossCacheFacade extends AbstractCacheProviderFacade {

  private TreeCache cacheManager;

  private CacheModelValidator cacheModelValidator;

  public JbossCacheFacade() {
    super();
    cacheModelValidator = new JbossCacheModelValidator();
  }

  public PropertyEditor getCachingModelEditor() {
    ReflectionCacheModelEditor editor = new ReflectionCacheModelEditor();
    editor.setCacheModelClass(JbossCacheCachingModel.class);
    return editor;
  }

  public PropertyEditor getFlushingModelEditor() {
    ReflectionCacheModelEditor editor = new ReflectionCacheModelEditor();
    editor.setCacheModelClass(JbossCacheFlushingModel.class);
    return editor;
  }

  public CacheModelValidator getCacheModelValidator() {
    return cacheModelValidator;
  }

  /**
   * @see AbstractCacheProviderFacade#isSerializableCacheElementRequired()
   */
  protected boolean isSerializableCacheElementRequired() {
    // serializable entries are not necessary if the cache is local (not
    // replicated). It is recommended to use serializable objects to enable
    // users to change the cache mode at any time.
    return true;
  }

  /**
   * @see AbstractCacheProviderFacade#onFlushCache(FlushingModel)
   */
  protected void onFlushCache(FlushingModel model) {
    JbossCacheFlushingModel flushingModel = (JbossCacheFlushingModel) model;

    String[] nodeFqns = flushingModel.getNodes();

    if (nodeFqns != null) {
      int fqnCount = nodeFqns.length;

      try {
        for (int i = 0; i < fqnCount; i++) {
          cacheManager.remove(nodeFqns[i]);
        }
      } catch (Exception exception) {
        throw new CacheAccessException(exception);
      }
    }
  }

  /**
   * @see AbstractCacheProviderFacade#onGetFromCache(Serializable, CachingModel)
   */
  protected Object onGetFromCache(Serializable key, CachingModel model) {
    JbossCacheCachingModel cachingModel = (JbossCacheCachingModel) model;

    Object cachedObject = null;

    try {
      cachedObject = cacheManager.get(cachingModel.getNode(), key);
    } catch (Exception exception) {
      throw new CacheAccessException(exception);
    }
    return cachedObject;
  }

  /**
   * @see AbstractCacheProviderFacade#onPutInCache(Serializable, CachingModel,
   *      Object)
   */
  protected void onPutInCache(Serializable key, CachingModel model, Object obj) {
    JbossCacheCachingModel cachingModel = (JbossCacheCachingModel) model;

    try {
      cacheManager.put(cachingModel.getNode(), key, obj);
    } catch (Exception exception) {
      throw new CacheAccessException(exception);
    }
  }

  /**
   * @see AbstractCacheProviderFacade#onRemoveFromCache(Serializable,
   *      CachingModel)
   */
  protected void onRemoveFromCache(Serializable key, CachingModel model) {
    JbossCacheCachingModel cachingModel = (JbossCacheCachingModel) model;

    try {
      cacheManager.remove(cachingModel.getNode(), key);
    } catch (Exception exception) {
      throw new CacheAccessException(exception);
    }
  }

  public void setCacheManager(TreeCache newCacheManager) {
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
