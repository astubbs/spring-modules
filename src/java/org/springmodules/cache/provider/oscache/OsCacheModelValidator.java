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

/**
 * <p>
 * Validates the properties of a <code>{@link OsCacheModel}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public class OsCacheModelValidator implements CacheModelValidator {

  public OsCacheModelValidator() {
    super();
  }

  /**
   * @see CacheModelValidator#validateCacheModel(Object)
   * 
   * @throws InvalidCacheModelException
   *           if the cache module is not an instance of
   *           <code>OSCacheModule</code>.
   */
  public final void validateCacheModel(Object cacheModel) {
    if (!(cacheModel instanceof OsCacheModel)) {
      throw new InvalidCacheModelException(
          "The cache model should be an instance of <"
              + OsCacheModel.class.getName() + ">");
    }
  }

}