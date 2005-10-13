/* 
 * Created on Jan 13, 2005
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

package org.springmodules.cache.provider.jcs;

import org.springframework.util.StringUtils;
import org.springmodules.cache.provider.CacheModelValidator;
import org.springmodules.cache.provider.InvalidCacheModelException;
import org.springmodules.cache.provider.jcs.JcsFlushingModel.CacheStruct;
import org.springmodules.util.ArrayUtils;

/**
 * <p>
 * Validates the properties of a <code>{@link JcsCachingModel}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public final class JcsModelValidator implements CacheModelValidator {

  public JcsModelValidator() {
    super();
  }

  /**
   * @throws InvalidCacheModelException
   *           if the model is not an instance of <code>JcsCachingModel</code>.
   * @throws InvalidCacheModelException
   *           if the model does not have a cache name.
   * @see CacheModelValidator#validateCachingModel(Object)
   */
  public void validateCachingModel(Object model)
      throws InvalidCacheModelException {
    if (!(model instanceof JcsCachingModel)) {
      throw new InvalidCacheModelException(
          "The caching model should be an instance of <"
              + JcsCachingModel.class.getName() + ">");
    }
    JcsCachingModel cachingModel = (JcsCachingModel) model;
    if (!StringUtils.hasText(cachingModel.getCacheName())) {
      throw new InvalidCacheModelException("Cache name should not be empty");
    }
  }

  /**
   * @throws InvalidCacheModelException
   *           if the model does not contain any cache structs.
   * @throws InvalidCacheModelException
   *           if any of the cache structs does not have a cache name.
   * @see CacheModelValidator#validateFlushingModel(Object)
   */
  public void validateFlushingModel(Object model)
      throws InvalidCacheModelException {
    if (!(model instanceof JcsFlushingModel)) {
      throw new InvalidCacheModelException(
          "The caching model should be an instance of <"
              + JcsFlushingModel.class.getName() + ">");
    }
    JcsFlushingModel flushingModel = (JcsFlushingModel) model;
    CacheStruct[] structs = flushingModel.getCacheStructs();
    if (!ArrayUtils.hasElements(structs)) {
      throw new InvalidCacheModelException(
          "There should be at least one cache to flush");
    }
    int structCount = structs.length;
    for (int i = 0; i < structCount; i++) {
      CacheStruct struct = structs[i];
      if (!StringUtils.hasText(struct.getCacheName())) {
        throw new InvalidCacheModelException(
            "Cache name should not be empty in the struct with index <" + i
                + ">");
      }
    }
  }
}