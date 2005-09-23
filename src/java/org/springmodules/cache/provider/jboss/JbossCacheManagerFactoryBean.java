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

import org.jboss.cache.TreeCache;
import org.springmodules.cache.provider.AbstractCacheManagerFactoryBean;

/**
 * <p>
 * Singleton <code>FactoryBean</code> that constructs and exposes a JBossCache
 * <code>TreeCache</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public class JbossCacheManagerFactoryBean extends
    AbstractCacheManagerFactoryBean {

  private static final String CACHE_PROVIDER_NAME = "JBossCache";

  private TreeCache treeCache;

  public JbossCacheManagerFactoryBean() {
    super();
  }

  /**
   * @see AbstractCacheManagerFactoryBean#createCacheManager()
   */
  protected void createCacheManager() throws Exception {
    treeCache = new TreeCache();
    treeCache.createService();
    treeCache.startService();
  }

  /**
   * @see AbstractCacheManagerFactoryBean#destroyCacheManager()
   */
  protected void destroyCacheManager() throws Exception {
    treeCache.stopService();
    treeCache.destroyService();
  }

  /**
   * @see AbstractCacheManagerFactoryBean#getCacheProviderName()
   */
  protected String getCacheProviderName() {
    return CACHE_PROVIDER_NAME;
  }

  /**
   * @see org.springframework.beans.factory.FactoryBean#getObject()
   */
  public Object getObject() throws Exception {
    return treeCache;
  }

  /**
   * @see org.springframework.beans.factory.FactoryBean#getObjectType()
   */
  public Class getObjectType() {
    return (treeCache != null) ? treeCache.getClass() : TreeCache.class;
  }

}
