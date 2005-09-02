/* 
 * Created on Aug 31, 2005
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
package org.springmodules.cache.provider.jboss;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.cache.TreeCache;
import org.springmodules.cache.provider.AbstractConfigurationResourceCacheManagerFactoryBean;

/**
 * <p>
 * FactoryBean that exposes a OSCache <code>TreeCache</code> singleton,
 * configured from a specified config location.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public class JbossCacheManagerFactoryBean extends
    AbstractConfigurationResourceCacheManagerFactoryBean {

  private static Log logger = LogFactory.getLog(JbossCacheManagerFactoryBean.class);

  private TreeCache treeCache;

  public JbossCacheManagerFactoryBean() {
    super();
  }

  /**
   * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
   */
  public void afterPropertiesSet() throws Exception {
    this.treeCache = new TreeCache();
    this.treeCache.createService();
    this.treeCache.startService();
  }

  /**
   * @see org.springframework.beans.factory.DisposableBean#destroy()
   */
  public void destroy() throws Exception {
    if (this.treeCache == null) {
      logger
          .info("The JBossCache tree cache was not built. No need to shut it down.");

    } else {
      logger.info("Shutting down the JBossCache tree cache.");
      this.treeCache.stopService();
      this.treeCache.destroyService();
    }
  }

  /**
   * @see org.springframework.beans.factory.FactoryBean#getObject()
   */
  public Object getObject() throws Exception {
    return this.treeCache;
  }

  /**
   * @see org.springframework.beans.factory.FactoryBean#getObjectType()
   */
  public Class getObjectType() {
    return (this.treeCache != null) ? this.treeCache.getClass()
        : TreeCache.class;
  }

}
