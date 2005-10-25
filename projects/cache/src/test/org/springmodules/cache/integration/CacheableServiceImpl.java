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
 * Default implementation of <code>{@link CacheableService}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public class CacheableServiceImpl implements CacheableService {

  private List names;

  public CacheableServiceImpl() {
    super();
  }

  /**
   * @see CacheableService#getName(int)
   * @@org.springmodules.cache.interceptor.caching.Cached(modelId="test")
   */
  public final String getName(int index) {
    return (String) this.names.get(index);
  }

  public final void setNames(List names) {
    this.names = names;
  }

  /**
   * @see CacheableService#updateName(int, String)
   * @@org.springmodules.cache.interceptor.flush.FlushCache(modelId="test")
   */
  public final void updateName(int index, String name) {
    this.names.set(index, name);
  }
}