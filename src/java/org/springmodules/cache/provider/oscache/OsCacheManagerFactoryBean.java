/* 
 * Created on Oct 15, 2004
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

package org.springmodules.cache.provider.oscache;

import java.util.Properties;

import org.springmodules.cache.provider.AbstractCacheManagerFactoryBean;

import com.opensymphony.oscache.general.GeneralCacheAdministrator;

/**
 * <p>
 * Singleton <code>FactoryBean</code> that constructs and exposes a OSCache
 * <code>GeneralCacheAdministrator</code>.
 * </p>
 * <p>
 * If no config location is specified, a <code>GeneralCacheAdministrator</code>
 * will be configured from "oscache.properties" in the root of the class path.
 * </p>
 * 
 * @author Alex Ruiz
 * @version $Revision: 1.8 $ $Date: 2005/09/23 02:58:35 $
 */
public final class OsCacheManagerFactoryBean extends
    AbstractCacheManagerFactoryBean {

  private static final String CACHE_PROVIDER_NAME = "OSCache";

  /**
   * The cache manager managed by this factory.
   */
  private GeneralCacheAdministrator cacheManager;

  public OsCacheManagerFactoryBean() {
    super();
  }

  /**
   * @see AbstractCacheManagerFactoryBean#createCacheManager()
   */
  protected void createCacheManager() throws Exception {
    Properties configProperties = getConfigProperties();
    if (configProperties == null) {
      cacheManager = new GeneralCacheAdministrator();
    } else {
      cacheManager = new GeneralCacheAdministrator(configProperties);
    }
  }

  /**
   * @see AbstractCacheManagerFactoryBean#destroyCacheManager()
   */
  protected void destroyCacheManager() throws Exception {
    cacheManager.flushAll();
    cacheManager.destroy();
  }

  /**
   * @see AbstractCacheManagerFactoryBean#getCacheProviderName()
   */
  protected String getCacheProviderName() {
    return CACHE_PROVIDER_NAME;
  }

  /**
   * Returns <code>{@link #cacheManager}</code>.
   * 
   * @return the cache manager managed by this factory.
   */
  public Object getObject() {
    return cacheManager;
  }

  /**
   * Returns the type of <code>{@link #cacheManager}</code>.
   * 
   * @return the type of the cache manager managed by this factory.
   */
  public Class getObjectType() {
    return cacheManager != null ? cacheManager.getClass()
        : GeneralCacheAdministrator.class;
  }
}