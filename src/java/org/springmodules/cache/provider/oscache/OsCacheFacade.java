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

package org.springmodules.cache.provider.oscache;

import java.beans.PropertyEditor;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.springmodules.cache.provider.AbstractCacheProviderFacade;
import org.springmodules.cache.provider.CacheModel;
import org.springmodules.cache.provider.CacheModelEditor;
import org.springmodules.cache.provider.CacheModelValidator;
import org.springmodules.cache.provider.FatalCacheException;

import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;

/**
 * <p>
 * Facade for OSCache.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.13 $ $Date: 2005/09/29 01:21:58 $
 */
public final class OsCacheFacade extends AbstractCacheProviderFacade {

  /**
   * OSCache cache manager.
   */
  private GeneralCacheAdministrator cacheManager;

  public OsCacheFacade() {
    super();
  }

  /**
   * @see AbstractCacheProviderFacade#getCacheModelEditor()
   */
  protected PropertyEditor getCacheModelEditor() {
    Map propertyEditors = new HashMap();
    propertyEditors.put("refreshPeriod", new RefreshPeriodEditor());

    CacheModelEditor editor = new CacheModelEditor();
    editor.setCacheModelClass(OsCacheModel.class);
    editor.setCacheModelPropertyEditors(propertyEditors);
    return editor;
  }

  /**
   * @see AbstractCacheProviderFacade#getCacheModelValidator()
   * @see OsCacheModelValidator#validateCacheModel(Object)
   */
  protected CacheModelValidator getCacheModelValidator() {
    return new OsCacheModelValidator();
  }

  /**
   * Returns the <code>String</code> representation of the given key.
   * 
   * @param cacheKey
   *          the cache key.
   * @return the <code>String</code> representation of <code>cacheKey</code>.
   */
  protected String getEntryKey(Serializable cacheKey) {
    return cacheKey.toString();
  }

  /**
   * @see AbstractCacheProviderFacade#isSerializableCacheElementRequired()
   */
  protected boolean isSerializableCacheElementRequired() {
    return false;
  }

  /**
   * 
   * @see AbstractCacheProviderFacade#onCancelCacheUpdate(Serializable)
   */
  protected void onCancelCacheUpdate(Serializable cacheKey) {
    String key = getEntryKey(cacheKey);
    cacheManager.cancelUpdate(key);
  }

  /**
   * @see AbstractCacheProviderFacade#onFlushCache(CacheModel)
   */
  protected void onFlushCache(CacheModel cacheModel) {
    OsCacheModel osCacheModel = (OsCacheModel) cacheModel;
    String[] groups = osCacheModel.getGroups();

    if (groups == null || groups.length == 0) {
      cacheManager.flushAll();

    } else {
      int groupCount = groups.length;

      for (int i = 0; i < groupCount; i++) {
        String group = groups[i];
        cacheManager.flushGroup(group);
      }
    }
  }

  /**
   * @see AbstractCacheProviderFacade#onGetFromCache(Serializable,
   *      CacheModel)
   */
  protected Object onGetFromCache(Serializable cacheKey,
      CacheModel cacheModel) {
    OsCacheModel osCacheModel = (OsCacheModel) cacheModel;

    Integer refreshPeriod = osCacheModel.getRefreshPeriod();
    String cronExpression = osCacheModel.getCronExpression();

    String key = getEntryKey(cacheKey);
    Object cachedObject = null;

    try {
      if (null == refreshPeriod) {
        cachedObject = cacheManager.getFromCache(key);

      } else if (null == cronExpression) {
        cachedObject = cacheManager.getFromCache(key, refreshPeriod.intValue());

      } else {
        cachedObject = cacheManager.getFromCache(key, refreshPeriod.intValue(),
            cronExpression);
      }
    } catch (NeedsRefreshException needsRefreshException) {
      // the cache does not have that entry.
    }

    return cachedObject;
  }

  /**
   * @see AbstractCacheProviderFacade#onPutInCache(Serializable,
   *      CacheModel, Object)
   */
  protected void onPutInCache(Serializable cacheKey, CacheModel cacheModule,
      Object objectToCache) {

    OsCacheModel osCacheModel = (OsCacheModel) cacheModule;

    String key = getEntryKey(cacheKey);
    String[] groups = osCacheModel.getGroups();

    if (groups == null || groups.length == 0) {
      cacheManager.putInCache(key, objectToCache);

    } else {
      cacheManager.putInCache(key, objectToCache, groups);
    }
  }

  /**
   * 
   * @see AbstractCacheProviderFacade#onRemoveFromCache(Serializable,
   *      CacheModel)
   */
  protected void onRemoveFromCache(Serializable cacheKey,
      CacheModel cacheModel) {

    String key = getEntryKey(cacheKey);
    cacheManager.flushEntry(key);
  }

  public void setCacheManager(GeneralCacheAdministrator newCacheManager) {
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