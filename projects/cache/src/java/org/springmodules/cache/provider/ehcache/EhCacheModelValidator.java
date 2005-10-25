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
import org.springmodules.cache.provider.AbstractCacheModelValidator;
import org.springmodules.cache.provider.InvalidCacheModelException;
import org.springmodules.util.ArrayUtils;

/**
 * <p>
 * Validates the properties of a <code>{@link EhCacheCachingModel}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public final class EhCacheModelValidator extends AbstractCacheModelValidator {

  public EhCacheModelValidator() {
    super();
  }

  /**
   * @see AbstractCacheModelValidator#getCachingModelTargetClass()
   */
  protected Class getCachingModelTargetClass() {
    return EhCacheCachingModel.class;
  }

  /**
   * @see AbstractCacheModelValidator#getFlushingModelTargetClass()
   */
  protected Class getFlushingModelTargetClass() {
    return EhCacheFlushingModel.class;
  }

  /**
   * @see AbstractCacheModelValidator#validateCachingModelProperties(Object)
   * @throws InvalidCacheModelException
   *           if the given model does not specify a cache.
   */
  protected void validateCachingModelProperties(Object cachingModel)
      throws InvalidCacheModelException {
    EhCacheCachingModel model = (EhCacheCachingModel) cachingModel;
    if (!StringUtils.hasText(model.getCacheName())) {
      throw new InvalidCacheModelException("Cache name should not be empty");
    }
  }

  /**
   * @see AbstractCacheModelValidator#validateFlushingModelProperties(Object)
   * @throws InvalidCacheModelException
   *           if the given model does not specify at least one cache.
   */
  protected void validateFlushingModelProperties(Object flushingModel)
      throws InvalidCacheModelException {
    EhCacheFlushingModel model = (EhCacheFlushingModel) flushingModel;
    String[] cacheNames = model.getCacheNames();
    if (!ArrayUtils.hasElements(cacheNames)) {
      throw new InvalidCacheModelException(
          "There should be at least one cache name");
    }
  }
}