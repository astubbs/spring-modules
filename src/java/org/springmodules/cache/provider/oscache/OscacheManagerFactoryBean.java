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

import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springmodules.cache.provider.AbstractConfigurationResourceCacheManagerFactoryBean;

import com.opensymphony.oscache.general.GeneralCacheAdministrator;

/**
 * <p>
 * FactoryBean that exposes a OSCache <code>GeneralCacheAdministrator</code>
 * singleton, configured from a specified config location.
 * </p>
 * <p>
 * If no config location is specified, a <code>GeneralCacheAdministrator</code>
 * will be configured from "oscache.properties" in the root of the class path.
 * </p>
 * 
 * @author Alex Ruiz
 * @version $Revision: 1.5 $ $Date: 2005/05/01 23:32:36 $
 */
public final class OscacheManagerFactoryBean extends
    AbstractConfigurationResourceCacheManagerFactoryBean {

  /**
   * Message logger.
   */
  private static Log logger = LogFactory
      .getLog(OscacheManagerFactoryBean.class);

  /**
   * The cache manager managed by this factory.
   */
  private GeneralCacheAdministrator cacheManager;

  /**
   * Constructor.
   */
  public OscacheManagerFactoryBean() {
    super();
  }

  /**
   * Builds the cache manager after all the properties of this factory has been
   * set by the BeanFactory.
   */
  public void afterPropertiesSet() throws Exception {
    logger.info("Creating OSCache cache manager");

    Resource configLocation = super.getConfigLocation();
    if (null == configLocation) {
      this.cacheManager = new GeneralCacheAdministrator();
    } else {
      InputStream inputStream = configLocation.getInputStream();
      Properties properties = new Properties();
      properties.load(inputStream);

      this.cacheManager = new GeneralCacheAdministrator(properties);
    }
  }

  /**
   * Shuts down the cache manager before this factory is destroyed by the
   * BeanFactory.
   */
  public void destroy() throws Exception {
    if (this.cacheManager == null) {
      logger
          .info("The OSCache cache manager was not built. No need to shut it down.");
    } else {
      logger.info("Shutting down the OSCache cache manager.");
      this.cacheManager.flushAll();
      this.cacheManager.destroy();
    }
  }

  /**
   * Returns <code>{@link #cacheManager}</code>.
   * 
   * @return the cache manager managed by this factory.
   */
  public Object getObject() throws Exception {
    return this.cacheManager;
  }

  /**
   * Returns the type of <code>{@link #cacheManager}</code>.
   * 
   * @return the type of the cache manager managed by this factory.
   */
  public Class getObjectType() {
    Class objectType = null;

    if (null == this.cacheManager) {
      objectType = GeneralCacheAdministrator.class;
    } else {
      objectType = this.cacheManager.getClass();
    }

    return objectType;
  }
}