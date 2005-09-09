/* 
 * Created on Jan 19, 2005
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

import java.util.Properties;

import org.springmodules.cache.provider.AbstractCacheProfileEditor;
import org.springmodules.cache.provider.CacheProfile;
import org.springmodules.cache.provider.CacheProfileValidator;
import org.springmodules.cache.provider.InvalidCacheProfileException;

/**
 * <p>
 * Creates a new instance of <code>{@link EhCacheProfile}</code> by parsing a
 * String of the form <code>[cacheName=value]</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.4 $ $Date: 2005/09/09 02:19:05 $
 */
public final class EhCacheProfileEditor extends AbstractCacheProfileEditor {

  private CacheProfileValidator cacheProfileValidator;

  public EhCacheProfileEditor() {
    super();
    cacheProfileValidator = new EhCacheProfileValidator();
  }

  /**
   * Creates a new instance of <code>{@link EhCacheProfile}</code> from the
   * specified set of properties.
   * 
   * @param properties
   *          the specified set of properties.
   * @return a new instance of <code>EhCacheProfile</code>.
   * 
   * @see CacheProfileValidator#validateCacheProfile(Object)
   * @see EhCacheProfileValidator
   */
  protected CacheProfile createCacheProfile(Properties properties)
      throws InvalidCacheProfileException {
    String cacheName = null;

    if (!properties.isEmpty()) {
      cacheName = properties.getProperty("cacheName");
    }
    EhCacheProfile cacheProfile = new EhCacheProfile();
    cacheProfile.setCacheName(cacheName);

    cacheProfileValidator.validateCacheProfile(cacheProfile);

    return cacheProfile;
  }

  protected CacheProfileValidator getCacheProfileValidator() {
    return cacheProfileValidator;
  }

  protected void setCacheProfileValidator(
      CacheProfileValidator newCacheProfileValidator) {
    cacheProfileValidator = newCacheProfileValidator;
  }

}