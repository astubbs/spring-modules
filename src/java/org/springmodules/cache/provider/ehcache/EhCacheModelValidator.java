/* 
 * Created on Jan 14, 2005
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

package org.springmodules.cache.provider.ehcache;

import org.springframework.util.StringUtils;
import org.springmodules.cache.provider.CacheModelValidator;
import org.springmodules.cache.provider.InvalidCacheModelException;
import org.springmodules.util.ArrayUtils;

/**
 * <p>
 * Validates the properties of a <code>{@link EhCacheCachingModel}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public final class EhCacheModelValidator implements CacheModelValidator {

  public EhCacheModelValidator() {
    super();
  }

  /**
   * @throws InvalidCacheModelException
   *           if the model is not an instance of
   *           <code>EhCacheCachingModel</code>.
   * @throws InvalidCacheModelException
   *           if the model does not have a cache name.
   * @see CacheModelValidator#validateCachingModel(Object)
   */
  public void validateCachingModel(Object model)
      throws InvalidCacheModelException {
    if (!(model instanceof EhCacheCachingModel)) {
      throw new InvalidCacheModelException(
          "The caching model should be an instance of <"
              + EhCacheCachingModel.class.getName() + ">");
    }

    EhCacheCachingModel cachingModel = (EhCacheCachingModel) model;
    if (!StringUtils.hasText(cachingModel.getCacheName())) {
      throw new InvalidCacheModelException("Cache name should not be empty");
    }
  }

  /**
   * @throws InvalidCacheModelException
   *           if the model is not an instance of
   *           <code>EhCacheFlushingModel</code>.
   * @throws InvalidCacheModelException
   *           if the model does not have at least one cache name.
   * @see CacheModelValidator#validateFlushingModel(Object)
   */
  public void validateFlushingModel(Object model)
      throws InvalidCacheModelException {
    if (!(model instanceof EhCacheFlushingModel)) {
      throw new InvalidCacheModelException(
          "The flushing model should be an instance of <"
              + EhCacheFlushingModel.class.getName() + ">");
    }

    EhCacheFlushingModel flushingModel = (EhCacheFlushingModel) model;
    String[] cacheNames = flushingModel.getCacheNames();
    if (!ArrayUtils.hasElements(cacheNames)) {
      throw new InvalidCacheModelException(
          "There should be at least one cache name");
    }
  }
}