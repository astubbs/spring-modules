/* 
 * Created on Oct 22, 2004
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
 * Copyright @2004 the original author or authors.
 */

package org.springmodules.cache.integration;

import java.util.List;

/**
 * <p>
 * Implementation of <code>{@link Cacheable}</code> used for integration
 * testing. Instances of this class should be:
 * <ul>
 * <li>Managed by a Spring bean context.</li>
 * <li>Advised by
 * <code>{@link org.springmodules.cache.interceptor.caching.CachingInterceptor}</code>.
 * </li>
 * <li>Advised by
 * <code>{@link org.springmodules.cache.interceptor.flush.CacheFlushInterceptor}</code>.
 * </li>
 * </ul>
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/04/22 02:19:15 $
 */
public class CacheableImpl implements Cacheable {

  /**
   * Version number of this class.
   * 
   * @see java.io.Serializable
   */
  private static final long serialVersionUID = 3761693368530647088L;

  /**
   * List of names.
   */
  private List names;

  /**
   * Constructor.
   */
  public CacheableImpl() {
    super();
  }

  /**
   * @see Cacheable#getName(int)
   * @@org.springmodules.cache.interceptor.caching.Cached(cacheProfileId="test")
   */
  public final String getName(int index) {
    return (String) this.names.get(index);
  }

  /**
   * Setter for the field <code>{@link #names}</code>.
   * 
   * @param names
   *          the new value to set
   */
  public final void setNames(List names) {
    this.names = names;
  }

  /**
   * @see Cacheable#updateName(int, String)
   * @@org.springmodules.cache.interceptor.flush.FlushCache(cacheProfileIds="test")
   */
  public final void updateName(int index, String name) {
    this.names.set(index, name);
  }
}