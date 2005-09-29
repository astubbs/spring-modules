/* 
 * Created on Jan 21, 2005
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

package org.springmodules.cache.provider;

/**
 * <p>
 * Validates the properties of cache models.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public interface CacheModelValidator {

  /**
   * Validates the properties of the specified cache model.
   * 
   * @param cacheModel
   *          the cache model to validate.
   * @throws InvalidCacheModelException
   *           if the given cache model is not valid.
   */
  void validateCacheModel(Object cacheModel) throws InvalidCacheModelException;
}