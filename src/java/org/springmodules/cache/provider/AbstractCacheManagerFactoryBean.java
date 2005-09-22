/* 
 * Created on Oct 18, 2004
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

package org.springmodules.cache.provider;

import org.springframework.core.io.Resource;

/**
 * <p>
 * Template for factories of cache managers.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public abstract class AbstractCacheManagerFactoryBean implements
    CacheManagerFactoryBean {

  /**
   * Location of the cache manager configuration file.
   */
  private Resource configLocation;

  public AbstractCacheManagerFactoryBean() {
    super();
  }

  public final Resource getConfigLocation() {
    return configLocation;
  }

  /**
   * Notifies the Spring IoC container that this factory is a singleton bean.
   * 
   * @return <code>true</code>.
   * 
   * @see org.springframework.beans.factory.FactoryBean#isSingleton()
   */
  public boolean isSingleton() {
    return true;
  }

  public final void setConfigLocation(Resource newConfigLocation) {
    configLocation = newConfigLocation;
  }

}