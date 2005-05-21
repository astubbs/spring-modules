/* 
 * Created on Apr 29, 2005
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
package org.springmodules.cache.annotations;

import java.util.List;

/**
 * <p>
 * JavaBean used for integration testing. Instances of this class should be:
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
 * @version $Revision: 1.2 $ $Date: 2005/05/21 21:26:06 $
 */
public class CacheableBean implements
    org.springmodules.cache.integration.Cacheable {

  /**
   * Version number of this class.
   * 
   * @see java.io.Serializable
   */
  private static final long serialVersionUID = 3258129141781181752L;

  /**
   * List of names.
   */
  private List<String> names;

  /**
   * Constructor.
   */
  public CacheableBean() {
    super();
  }

  /**
   * Returns a name from the list of names.
   * 
   * @param index
   *          the index of the element in the list.
   * @return a name from the list of names.
   */
  @Cacheable(cacheProfileId = "test")
  public final String getName(int index) {
    return this.names.get(index);
  }

  /**
   * Setter for the field <code>{@link #names}</code>.
   * 
   * @param names
   *          the new value to set
   */
  public final void setNames(List<String> names) {
    this.names = names;
  }

  /**
   * Updates the name in the specified position in the list.
   * 
   * @param index
   *          the index of the element to update.
   * @param name
   *          the new name.
   */
  @CacheFlush(cacheProfileIds = { "test" })
  public final void updateName(int index, String name) {
    this.names.set(index, name);
  }
}
