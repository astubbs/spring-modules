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
import org.springmodules.cache.provider.AbstractCacheProviderFacadeImpl;
import org.springmodules.cache.provider.CacheAccessException;
import org.springmodules.cache.provider.CacheProfile;
import org.springmodules.cache.provider.CacheProfileEditor;
import org.springmodules.cache.provider.CacheProfileValidator;
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
public class JbossCacheFacade extends AbstractCacheProviderFacadeImpl {

  private TreeCache cacheManager;

  public JbossCacheFacade() {
    super();
  }

  /**
   * @see AbstractCacheProviderFacadeImpl#getCacheProfileEditor()
   */
  protected PropertyEditor getCacheProfileEditor() {
    CacheProfileEditor editor = new CacheProfileEditor();
    editor.setCacheProfileClass(JbossCacheProfile.class);
    return editor;
  }

  /**
   * @see AbstractCacheProviderFacadeImpl#getCacheProfileValidator()
   */
  protected CacheProfileValidator getCacheProfileValidator() {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * @see AbstractCacheProviderFacadeImpl#isSerializableCacheElementRequired()
   */
  protected boolean isSerializableCacheElementRequired() {
    return false;
  }

  /**
   * @see AbstractCacheProviderFacadeImpl#onFlushCache(CacheProfile)
   */
  protected void onFlushCache(CacheProfile cacheProfile) {
    JbossCacheProfile profile = (JbossCacheProfile) cacheProfile;

    try {
      cacheManager.remove(profile.getNodeFqn());
    } catch (Exception exception) {
      throw new CacheAccessException(exception);
    }
  }

  /**
   * @see AbstractCacheProviderFacadeImpl#onGetFromCache(Serializable,
   *      CacheProfile)
   */
  protected Object onGetFromCache(Serializable cacheKey,
      CacheProfile cacheProfile) {
    JbossCacheProfile profile = (JbossCacheProfile) cacheProfile;

    Object cachedObject = null;

    try {
      cachedObject = cacheManager.get(profile.getNodeFqn(), cacheKey);
    } catch (Exception exception) {
      throw new CacheAccessException(exception);
    }
    return cachedObject;
  }

  /**
   * @see AbstractCacheProviderFacadeImpl#onPutInCache(Serializable,
   *      CacheProfile, Object)
   */
  protected void onPutInCache(Serializable cacheKey, CacheProfile cacheProfile,
      Object objectToCache) {
    JbossCacheProfile profile = (JbossCacheProfile) cacheProfile;

    try {
      cacheManager.put(profile.getNodeFqn(), cacheKey, objectToCache);
    } catch (Exception exception) {
      throw new CacheAccessException(exception);
    }
  }

  /**
   * @see AbstractCacheProviderFacadeImpl#onRemoveFromCache(Serializable,
   *      CacheProfile)
   */
  protected void onRemoveFromCache(Serializable cacheKey,
      CacheProfile cacheProfile) {
    JbossCacheProfile profile = (JbossCacheProfile) cacheProfile;

    try {
      cacheManager.remove(profile.getNodeFqn(), cacheKey);
    } catch (Exception exception) {
      throw new CacheAccessException(exception);
    }
  }

  public final void setCacheManager(TreeCache newCacheManager) {
    cacheManager = newCacheManager;
  }

  /**
   * @see AbstractCacheProviderFacadeImpl#validateCacheManager()
   * 
   * @throws FatalCacheException
   *           if the cache manager is <code>null</code>.
   */
  protected void validateCacheManager() throws FatalCacheException {
    if (null == cacheManager) {
      throw new FatalCacheException("The cache manager should not be null");
    }
  }

}
