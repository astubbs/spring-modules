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
 * Template for factories of cache managers configured through a file.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.5 $ $Date: 2005/09/09 02:19:26 $
 */
public abstract class AbstractConfigurationResourceCacheManagerFactoryBean
    extends AbstractSingletonCacheManagerFactoryBean {

  /**
   * Location of the configuration file for the cache manager to build.
   */
  private Resource configLocation;

  public AbstractConfigurationResourceCacheManagerFactoryBean() {
    super();
  }

  public final Resource getConfigLocation() {
    return configLocation;
  }

  public final void setConfigLocation(Resource newConfigLocation) {
    configLocation = newConfigLocation;
  }

}