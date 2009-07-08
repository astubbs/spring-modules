/*
* Copyright 2006 GigaSpaces, Inc.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.springmodules.cache.integration.gigaspaces;


import org.springmodules.cache.integration.AbstractIntegrationTests;
import org.springmodules.cache.integration.KeyAndModelListCachingListener.KeyAndModel;
import org.springmodules.cache.provider.gigaspaces.GigaSpacesCachingModel;

import com.j_spaces.map.CacheFinder;
import com.j_spaces.map.IMap;

/**
 * <p>
 * Template for test cases that verify that the caching module works correctly
 * inside a Spring bean context when using Tangosol GigaSpaces as the cache
 * provider.
 * </p>
 *
 * @author Alex Ruiz
 */
public class GigaSpacesIntegrationTests extends
    AbstractIntegrationTests {

  /**
   * @see AbstractIntegrationTests#assertCacheWasFlushed()
   */
  protected final void assertCacheWasFlushed() throws Exception {
    int index = 0;
    Object entry = getCacheElement(index);
    assertCacheEntryFromCacheIsNull(entry, getKeyAndModel(index).key);
  }

  /**
   * @see AbstractIntegrationTests#assertObjectWasCached(Object, int)
   */
  protected final void assertObjectWasCached(Object expectedCachedObject,
      int keyAndModelIndex) throws Exception {
    Object entry = getCacheElement(keyAndModelIndex);
    assertEquals(expectedCachedObject, entry);
  }

  private Object getCacheElement(int keyAndModelIndex) throws Exception {
    KeyAndModel keyAndModel = getKeyAndModel(keyAndModelIndex);
    GigaSpacesCachingModel model = (GigaSpacesCachingModel) keyAndModel.model;
    IMap cache = (IMap) CacheFinder.find(model.getCacheName());
    return cache.get(keyAndModel.key);
  }
}