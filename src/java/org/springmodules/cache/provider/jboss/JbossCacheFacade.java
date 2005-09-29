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
import org.springmodules.cache.provider.AbstractCacheProviderFacade;
import org.springmodules.cache.provider.CacheAccessException;
import org.springmodules.cache.provider.CacheModel;
import org.springmodules.cache.provider.CacheModelEditor;
import org.springmodules.cache.provider.CacheModelValidator;
import org.springmodules.cache.provider.FatalCacheException;

/**
 * <p>
 * Facade for JBossCache.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public class JbossCacheFacade extends AbstractCacheProviderFacade {

  private TreeCache cacheManager;

  public JbossCacheFacade() {
    super();
  }

  /**
   * @see AbstractCacheProviderFacade#getCacheModelEditor()
   */
  protected PropertyEditor getCacheModelEditor() {
    CacheModelEditor editor = new CacheModelEditor();
    editor.setCacheModelClass(JbossCacheModel.class);
    return editor;
  }

  /**
   * @see AbstractCacheProviderFacade#getCacheModelValidator()
   */
  protected CacheModelValidator getCacheModelValidator() {
    return new JbossCacheModelValidator();
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
   * @see AbstractCacheProviderFacade#onFlushCache(CacheModel)
   */
  protected void onFlushCache(CacheModel cacheModel) {
    JbossCacheModel jbossCacheModel = (JbossCacheModel) cacheModel;

    try {
      cacheManager.remove(jbossCacheModel.getNodeFqn());
    } catch (Exception exception) {
      throw new CacheAccessException(exception);
    }
  }

  /**
   * @see AbstractCacheProviderFacade#onGetFromCache(Serializable, CacheModel)
   */
  protected Object onGetFromCache(Serializable cacheKey, CacheModel cacheModel) {
    JbossCacheModel jbossCacheModel = (JbossCacheModel) cacheModel;

    Object cachedObject = null;

    try {
      cachedObject = cacheManager.get(jbossCacheModel.getNodeFqn(), cacheKey);
    } catch (Exception exception) {
      throw new CacheAccessException(exception);
    }
    return cachedObject;
  }

  /**
   * @see AbstractCacheProviderFacade#onPutInCache(Serializable, CacheModel,
   *      Object)
   */
  protected void onPutInCache(Serializable cacheKey, CacheModel cacheModel,
      Object objectToCache) {
    JbossCacheModel jbossCacheModel = (JbossCacheModel) cacheModel;

    try {
      cacheManager.put(jbossCacheModel.getNodeFqn(), cacheKey, objectToCache);
    } catch (Exception exception) {
      throw new CacheAccessException(exception);
    }
  }

  /**
   * @see AbstractCacheProviderFacade#onRemoveFromCache(Serializable,
   *      CacheModel)
   */
  protected void onRemoveFromCache(Serializable cacheKey, CacheModel cacheModel) {
    JbossCacheModel jbossCacheModel = (JbossCacheModel) cacheModel;

    try {
      cacheManager.remove(jbossCacheModel.getNodeFqn(), cacheKey);
    } catch (Exception exception) {
      throw new CacheAccessException(exception);
    }
  }

  public final void setCacheManager(TreeCache newCacheManager) {
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
