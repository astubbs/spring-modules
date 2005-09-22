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
import org.springmodules.cache.provider.CacheProfile;
import org.springmodules.cache.provider.CacheProfileEditor;
import org.springmodules.cache.provider.CacheProfileValidator;
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
 * @version $Revision: 1.12 $ $Date: 2005/09/22 10:03:42 $
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
   * @see AbstractCacheProviderFacade#getCacheProfileEditor()
   */
  protected PropertyEditor getCacheProfileEditor() {
    Map propertyEditors = new HashMap();
    propertyEditors.put("refreshPeriod", new RefreshPeriodEditor());

    CacheProfileEditor editor = new CacheProfileEditor();
    editor.setCacheProfileClass(OsCacheProfile.class);
    editor.setCacheProfilePropertyEditors(propertyEditors);
    return editor;
  }

  /**
   * @see AbstractCacheProviderFacade#getCacheProfileValidator()
   * @see OsCacheProfileValidator#validateCacheProfile(Object)
   */
  protected CacheProfileValidator getCacheProfileValidator() {
    return new OsCacheProfileValidator();
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
   * @see AbstractCacheProviderFacade#onFlushCache(CacheProfile)
   */
  protected void onFlushCache(CacheProfile cacheProfile) {
    OsCacheProfile profile = (OsCacheProfile) cacheProfile;
    String[] groups = profile.getGroups();

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
   *      CacheProfile)
   */
  protected Object onGetFromCache(Serializable cacheKey,
      CacheProfile cacheProfile) {
    OsCacheProfile profile = (OsCacheProfile) cacheProfile;

    Integer refreshPeriod = profile.getRefreshPeriod();
    String cronExpression = profile.getCronExpression();

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
   *      CacheProfile, Object)
   */
  protected void onPutInCache(Serializable cacheKey, CacheProfile cacheProfile,
      Object objectToCache) {

    OsCacheProfile profile = (OsCacheProfile) cacheProfile;

    String key = getEntryKey(cacheKey);
    String[] groups = profile.getGroups();

    if (groups == null || groups.length == 0) {
      cacheManager.putInCache(key, objectToCache);

    } else {
      cacheManager.putInCache(key, objectToCache, groups);
    }
  }

  /**
   * 
   * @see AbstractCacheProviderFacade#onRemoveFromCache(Serializable,
   *      CacheProfile)
   */
  protected void onRemoveFromCache(Serializable cacheKey,
      CacheProfile cacheProfile) {

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