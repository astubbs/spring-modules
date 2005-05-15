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

import org.springmodules.cache.provider.CacheProfileValidator;

/**
 * <p>
 * Validates the properties of a <code>{@link OsCacheProfile}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/05/15 02:14:02 $
 */
public class OsCacheProfileValidator implements CacheProfileValidator {

  /**
   * Constructor.
   */
  public OsCacheProfileValidator() {
    super();
  }

  /**
   * @see CacheProfileValidator#validateCacheProfile(Object)
   * 
   * @throws IllegalArgumentException
   *           if the cache profile is not an instance of
   *           <code>OsCacheProfile</code>.
   */
  public final void validateCacheProfile(Object cacheProfile) {
    if (!(cacheProfile instanceof OsCacheProfile)) {
      throw new IllegalArgumentException(
          "The cache profile should be an instance of 'OsCacheProfile'");
    }
  }

}