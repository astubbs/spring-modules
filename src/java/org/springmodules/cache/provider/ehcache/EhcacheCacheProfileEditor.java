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

/**
 * <p>
 * Creates a new instance of <code>{@link EhcacheCacheProfile}</code> by
 * parsing a String of the form <code>[cacheName=value]</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/04/11 04:02:17 $
 */
public final class EhcacheCacheProfileEditor extends AbstractCacheProfileEditor {

  /**
   * Validates the properties of the <code>{@link EhcacheCacheProfile}</code>
   * to create.
   */
  private EhcacheCacheProfileValidator cacheProfileValidator;

  /**
   * Constructor.
   */
  public EhcacheCacheProfileEditor() {
    super();
    this.cacheProfileValidator = new EhcacheCacheProfileValidator();
  }

  /**
   * Creates a new instance of <code>{@link EhcacheCacheProfile}</code> from
   * the specified set of properties.
   * 
   * @param properties
   *          the specified set of properties.
   * @return a new instance of <code>EhcacheCacheProfile</code>.
   * 
   * @see EhcacheCacheProfileValidator#validateCacheName(String)
   */
  protected CacheProfile createCacheProfile(Properties properties) {

    String cacheName = null;

    if (!properties.isEmpty()) {
      cacheName = properties.getProperty("cacheName");
    }
    this.cacheProfileValidator.validateCacheName(cacheName);

    EhcacheCacheProfile profile = new EhcacheCacheProfile();
    profile.setCacheName(cacheName);
    return profile;
  }

}