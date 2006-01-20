/* 
 * Created on Jan 19, 2006
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
 * Copyright @2006 the original author or authors.
 */
package org.springmodules.cache.config.integration;

import junit.framework.TestCase;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

import org.apache.jcs.engine.behavior.ICompositeCacheAttributes;
import org.apache.jcs.engine.control.CompositeCache;
import org.apache.jcs.engine.control.CompositeCacheManager;
import org.jboss.cache.TreeCache;

import com.opensymphony.oscache.general.GeneralCacheAdministrator;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import org.springmodules.cache.config.CacheNamespaceHandler;
import org.springmodules.cache.provider.PathUtils;
import org.springmodules.cache.provider.jboss.JbossCacheManagerFactoryBean;
import org.springmodules.cache.provider.jcs.JcsManagerFactoryBean;
import org.springmodules.cache.provider.oscache.OsCacheManagerFactoryBean;

/**
 * <p>
 * Unit Tests for <code>{@link CacheNamespaceHandler}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class CacheNamespaceConfigIntegrationTests extends TestCase {

  private static final String CUSTOM_LOCATION_CACHE_MANAGER_ID = "customLocationCacheManager";

  private static final String DEFAULT_CACHE_MANAGER_ID = "cacheManager";

  private static final String THIS_PATH = "classpath:"
      + PathUtils
          .getPackageNameAsPath(CacheNamespaceConfigIntegrationTests.class);

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case
   */
  public CacheNamespaceConfigIntegrationTests(String name) {
    super(name);
  }

  /**
   * Verifies that <code>{@link CacheNamespaceHandler}</code> creates a
   * EHCache cache manager using a custom configuration file.
   */
  public void testCacheConfigWithEhCacheAndCustomConfigLocation() {
    ApplicationContext applicationContext = getApplicationContext("ehCacheConfigCustomValues.xml");

    String cacheManagerBeanId = CUSTOM_LOCATION_CACHE_MANAGER_ID;
    CacheManager cacheManager = (CacheManager) applicationContext
        .getBean(cacheManagerBeanId);
    assertNotNull("The application context should have "
        + "a cache manager called '" + cacheManagerBeanId + "'", cacheManager);

    Cache cache = cacheManager.getCache("customCache");
    assertNotNull("The cache manager should have a cache name 'customCache'",
        cache);
    assertEquals("<Max. elements in memory>", 75, cache
        .getMaxElementsInMemory());

    EhCacheManagerFactoryBean factoryBean = (EhCacheManagerFactoryBean) applicationContext
        .getBean("&" + cacheManagerBeanId);
    factoryBean.destroy();
  }

  /**
   * Verifies that <code>{@link CacheNamespaceHandler}</code> creates a
   * <code>{@link EhCacheManagerFactoryBean}</code> and registers it under the
   * id "cacheManager".
   * 
   * @throws Exception
   *           any exception thrown when shutting down the cache manager
   */
  public void testCacheConfigWithEhCacheAndDefaultBeanId() throws Exception {
    assertCacheConfigRegistersCacheManagerFactoryUsingDefaultId(
        EhCacheManagerFactoryBean.class, "ehCacheConfigDefaultValues.xml");
  }

  /**
   * Verifies that <code>{@link CacheNamespaceHandler}</code> creates a JBoss
   * Cache cache manager using a custom configuration file.
   * 
   * @throws Exception
   *           any exception thrown when shutting down the cache manager
   */
  public void testCacheConfigWithJbossCacheAndCustomConfigLocation()
      throws Exception {
    ApplicationContext applicationContext = getApplicationContext("jbossCacheConfigCustomValues.xml");

    String cacheManagerBeanId = CUSTOM_LOCATION_CACHE_MANAGER_ID;
    TreeCache cacheManager = (TreeCache) applicationContext
        .getBean(cacheManagerBeanId);
    assertNotNull("The application context should have "
        + "a cache manager called '" + cacheManagerBeanId + "'", cacheManager);

    assertEquals(7000l, cacheManager.getInitialStateRetrievalTimeout());
    assertEquals(4554l, cacheManager.getSyncReplTimeout());

    JbossCacheManagerFactoryBean factoryBean = (JbossCacheManagerFactoryBean) applicationContext
        .getBean("&" + cacheManagerBeanId);
    factoryBean.destroy();
  }

  /**
   * Verifies that <code>{@link CacheNamespaceHandler}</code> creates a
   * <code>{@link JbossCacheManagerFactoryBean}</code> and registers it under
   * the id "cacheManager".
   * 
   * @throws Exception
   *           any exception thrown when shutting down the cache manager
   */
  public void testCacheConfigWithJbossCacheAndDefaultBeanId() throws Exception {
    assertCacheConfigRegistersCacheManagerFactoryUsingDefaultId(
        JbossCacheManagerFactoryBean.class, "jbossCacheConfigDefaultValues.xml");
  }

  /**
   * Verifies that <code>{@link CacheNamespaceHandler}</code> creates a JCS
   * cache manager using a custom configuration file.
   * 
   * @throws Exception
   *           any exception thrown when shutting down the cache manager
   */
  public void testCacheConfigWithJcsAndCustomConfigLocation() throws Exception {
    ApplicationContext applicationContext = getApplicationContext("jcsConfigCustomValues.xml");

    String cacheManagerBeanId = CUSTOM_LOCATION_CACHE_MANAGER_ID;
    CompositeCacheManager cacheManager = (CompositeCacheManager) applicationContext
        .getBean(cacheManagerBeanId);
    assertNotNull("The application context should have "
        + "a cache manager called '" + cacheManagerBeanId + "'", cacheManager);

    String cacheName = "testCache";
    CompositeCache cache = cacheManager.getCache(cacheName);
    assertNotNull("The cache manager should have a cache name '" + cacheName
        + "'", cache);
    ICompositeCacheAttributes cacheAttributes = cache.getCacheAttributes();
    assertEquals("<Max. objects>", 20, cacheAttributes.getMaxObjects());

    JcsManagerFactoryBean factoryBean = (JcsManagerFactoryBean) applicationContext
        .getBean("&" + cacheManagerBeanId);
    factoryBean.destroy();
  }

  /**
   * Verifies that <code>{@link CacheNamespaceHandler}</code> creates a
   * <code>{@link JcsManagerFactoryBean}</code> and registers it under the id
   * "cacheManager".
   * 
   * @throws Exception
   *           any exception thrown when shutting down the cache manager
   */
  public void testCacheConfigWithJcsAndDefaultBeanId() throws Exception {
    assertCacheConfigRegistersCacheManagerFactoryUsingDefaultId(
        JcsManagerFactoryBean.class, "jcsConfigDefaultValues.xml");
  }

  /**
   * Verifies that <code>{@link CacheNamespaceHandler}</code> creates a
   * OSCache cache manager using a custom configuration file.
   * 
   * @throws Exception
   *           any exception thrown when shutting down the cache manager
   */
  public void testCacheConfigWithOsCacheAndCustomConfigLocation()
      throws Exception {
    ApplicationContext applicationContext = getApplicationContext("osCacheConfigCustomValues.xml");

    String cacheManagerBeanId = CUSTOM_LOCATION_CACHE_MANAGER_ID;
    GeneralCacheAdministrator cacheManager = (GeneralCacheAdministrator) applicationContext
        .getBean(cacheManagerBeanId);
    assertNotNull("The application context should have "
        + "a cache manager called '" + cacheManagerBeanId + "'", cacheManager);

    assertEquals("<Cache capacity>", "5", cacheManager
        .getProperty("cache.capacity"));

    OsCacheManagerFactoryBean factoryBean = (OsCacheManagerFactoryBean) applicationContext
        .getBean("&" + cacheManagerBeanId);
    factoryBean.destroy();
  }

  /**
   * Verifies that <code>{@link CacheNamespaceHandler}</code> creates a
   * <code>{@link OsCacheManagerFactoryBean}</code> and registers it under the
   * id "cacheManager".
   * 
   * @throws Exception
   *           any exception thrown when shutting down the cache manager
   */
  public void testCacheConfigWithOsCacheAndDefaultBeanId() throws Exception {
    assertCacheConfigRegistersCacheManagerFactoryUsingDefaultId(
        OsCacheManagerFactoryBean.class, "osCacheConfigDefaultValues.xml");
  }

  private void assertCacheConfigRegistersCacheManagerFactoryUsingDefaultId(
      Class targetClass, String configLocation) throws Exception {

    ApplicationContext applicationContext = getApplicationContext(configLocation);
    String cacheManagerFactoryBeanId = "&" + DEFAULT_CACHE_MANAGER_ID;

    DisposableBean factoryBean = (DisposableBean) applicationContext
        .getBean(cacheManagerFactoryBeanId);

    assertNotNull("The application context should have "
        + "a JbossCacheManagerFactoryBean called '" + cacheManagerFactoryBeanId
        + "'", factoryBean);
    assertEquals("<Cache manager factory class>", targetClass, factoryBean
        .getClass());

    factoryBean.destroy();
  }

  private ApplicationContext getApplicationContext(String configLocation) {
    return new ClassPathXmlApplicationContext(new String[] { THIS_PATH + "/"
        + configLocation });
  }

}
