/* 
 * Created on Jan 12, 2005
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

package org.springmodules.cache.provider.oscache;

import org.springmodules.cache.provider.CacheModelValidator;
import org.springmodules.cache.provider.InvalidCacheModelException;
import org.springmodules.util.ArrayUtils;

/**
 * <p>
 * Validates the properties of a <code>{@link OsCacheCachingModel}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public final class OsCacheModelValidator implements CacheModelValidator {

  public OsCacheModelValidator() {
    super();
  }

  /**
   * @throws InvalidCacheModelException
   *           if the model is not an instance of
   *           <code>OSCacheCachingModule</code>.
   * @see CacheModelValidator#validateCachingModel(Object)
   */
  public void validateCachingModel(Object model) {
    if (!(model instanceof OsCacheCachingModel)) {
      throw new InvalidCacheModelException(
          "The caching model should be an instance of <"
              + OsCacheCachingModel.class.getName() + ">");
    }
  }

  /**
   * @throws InvalidCacheModelException
   *           if the model is not an instance of
   *           <code>{@link OsCacheFlushingModel}</code>.
   * @throws InvalidCacheModelException
   *           if the model does not have at least one group name.
   * @see CacheModelValidator#validateFlushingModel(Object)
   */
  public void validateFlushingModel(Object model)
      throws InvalidCacheModelException {
    if (!(model instanceof OsCacheFlushingModel)) {
      throw new InvalidCacheModelException(
          "The flushing model should be an instance of <"
              + OsCacheFlushingModel.class.getName() + ">");
    }

    OsCacheFlushingModel flushingModel = (OsCacheFlushingModel) model;

    if (!ArrayUtils.hasElements(flushingModel.getGroups())) {
      throw new InvalidCacheModelException(
          "The model should have at least one group name");
    }
  }

}