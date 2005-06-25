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
import org.springmodules.cache.provider.CacheProfileValidator;

/**
 * <p>
 * Validates the properties of a <code>{@link EhCacheProfile}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.2 $ $Date: 2005/06/25 06:53:16 $
 */
public class EhCacheProfileValidator implements CacheProfileValidator {

  /**
   * Constructor.
   */
  public EhCacheProfileValidator() {
    super();
  }

  /**
   * Validates the specified cache name.
   * 
   * @param cacheName
   *          the cache name to validate.
   * 
   * @throws IllegalArgumentException
   *           if the cache name is empty or <code>null</code>.
   */
  protected final void validateCacheName(String cacheName) {
    if (!StringUtils.hasText(cacheName)) {
      throw new IllegalArgumentException("Cache name should not be empty");
    }
  }

  /**
   * @see CacheProfileValidator#validateCacheProfile(Object)
   */
  public final void validateCacheProfile(Object cacheProfile) {
    if (cacheProfile instanceof EhCacheProfile) {
      EhCacheProfile ehcacheCacheProfile = (EhCacheProfile) cacheProfile;
      String cacheName = ehcacheCacheProfile.getCacheName();
      this.validateCacheName(cacheName);
    } else {
      throw new IllegalArgumentException(
          "The cache profile should be an instance of 'EhCacheProfile'");
    }
  }
}