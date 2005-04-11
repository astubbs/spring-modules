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

package org.springmodules.cache.provider.jcs;

import java.util.Properties;

import org.springmodules.cache.provider.AbstractCacheProfileEditor;
import org.springmodules.cache.provider.CacheProfile;

/**
 * <p>
 * Creates a new instance of <code>{@link JcsCacheProfile}</code> by parsing a
 * String of the form <code>[cacheName=value][group=value]</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/04/11 04:02:13 $
 */
public final class JcsCacheProfileEditor extends AbstractCacheProfileEditor {

  /**
   * Validates the properties of the <code>{@link JcsCacheProfile}</code> to
   * create.
   */
  private JcsCacheProfileValidator cacheProfileValidator;

  /**
   * Constructor.
   */
  public JcsCacheProfileEditor() {
    super();
    this.cacheProfileValidator = new JcsCacheProfileValidator();
  }

  /**
   * Creates a new instance of <code>{@link JcsCacheProfile}</code> from the
   * specified set of properties.
   * 
   * @param properties
   *          the specified set of properties.
   * @return a new instance of <code>JcsCacheProfile</code>.
   * 
   * @see JcsCacheProfileValidator#validateCacheName(String)
   */
  protected CacheProfile createCacheProfile(Properties properties) {
    String cacheName = null;
    String group = null;

    if (!properties.isEmpty()) {
      cacheName = properties.getProperty("cacheName");
      group = properties.getProperty("group");
    }
    this.cacheProfileValidator.validateCacheName(cacheName);

    JcsCacheProfile profile = new JcsCacheProfile();
    profile.setCacheName(cacheName);
    profile.setGroup(group);
    return profile;
  }

}