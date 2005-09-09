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

package org.springmodules.cache.provider.jcs;

import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jcs.engine.control.CompositeCacheManager;
import org.springframework.core.io.Resource;
import org.springmodules.cache.provider.AbstractConfigurationResourceCacheManagerFactoryBean;

/**
 * <p>
 * FactoryBean that exposes a JCS <code>CompositeCacheManager</code>
 * singleton, configured from a specified config location.
 * </p>
 * <p>
 * If no config location is specified, a <code>CompositeCacheManager</code>
 * will be configured from "cache.ccf" in the root of the class path.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.7 $ $Date: 2005/09/09 02:19:17 $
 */
public final class JcsManagerFactoryBean extends
    AbstractConfigurationResourceCacheManagerFactoryBean {

  private static Log logger = LogFactory.getLog(JcsManagerFactoryBean.class);

  /**
   * The cache manager managed by this factory.
   */
  private CompositeCacheManager cacheManager;

  public JcsManagerFactoryBean() {
    super();
  }

  /**
   * Builds the cache manager after all the properties of this factory has been
   * set by the BeanFactory.
   */
  public void afterPropertiesSet() throws Exception {
    logger.info("Creating JCS cache manager.");

    Resource configLocation = getConfigLocation();

    if (null == configLocation) {
      cacheManager = CompositeCacheManager.getInstance();
    } else {
      InputStream inputStream = configLocation.getInputStream();
      Properties properties = new Properties();
      properties.load(inputStream);

      cacheManager = CompositeCacheManager.getUnconfiguredInstance();
      cacheManager.configure(properties);
    }
  }

  /**
   * Shuts down the cache manager before this factory is destroyed by the
   * BeanFactory.
   */
  public void destroy() throws Exception {
    if (cacheManager == null) {
      logger
          .info("The JCS cache manager was not built. No need to shut it down.");

    } else {
      logger.info("Shutting down the JCS cache manager.");
      cacheManager.shutDown();
    }
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
        : CompositeCacheManager.class;
  }
}