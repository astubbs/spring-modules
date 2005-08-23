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
 * Implementation of
 * <code>{@link org.springmodules.cache.integration.Cacheable}</code> that
 * uses JDK 1.5+ annotations for setting up caching services.
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.3 $ $Date: 2005/08/23 01:18:11 $
 */
public class CacheableBean implements
    org.springmodules.cache.integration.Cacheable {

  private static final long serialVersionUID = 3258129141781181752L;

  private List<String> names;

  public CacheableBean() {
    super();
  }

  @Cacheable(cacheProfileId = "test")
  public final String getName(int index) {
    return this.names.get(index);
  }

  public final void setNames(List<String> names) {
    this.names = names;
  }

  @CacheFlush(cacheProfileIds = { "test" })
  public final void updateName(int index, String name) {
    this.names.set(index, name);
  }
}
