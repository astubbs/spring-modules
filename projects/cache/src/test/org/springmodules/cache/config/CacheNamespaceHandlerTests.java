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
package org.springmodules.cache.config;

import junit.framework.TestCase;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import org.springmodules.cache.provider.PathUtils;

/**
 * <p>
 * Unit Tests for <code>{@link CacheNamespaceHandler}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class CacheNamespaceHandlerTests extends TestCase {

  private static final String THIS_PATH = PathUtils
      .getPackageNameAsPath(CacheNamespaceHandlerTests.class);

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case
   */
  public CacheNamespaceHandlerTests(String name) {
    super(name);
  }

  /**
   * Verifies that <code>{@link CacheNamespaceHandler}</code> creates a cache
   * manager using the default id "cacheManager".
   */
  public void testCacheManagerWithDefaultBeanId() {
    ApplicationContext applicationContext = getApplicationContext("ehCacheDefaultValues.xml");

    String cacheManagerBeanId = "cacheManager";

    EhCacheManagerFactoryBean factoryBean = (EhCacheManagerFactoryBean) applicationContext
        .getBean("&" + cacheManagerBeanId);
    assertNotNull("The application context should have "
        + "a EhCacheManagerFactoryBean called '" + cacheManagerBeanId + "'",
        factoryBean);

    factoryBean.destroy();
  }

  /**
   * Verifies that <code>{@link CacheNamespaceHandler}</code> creates a
   * EHCache cache manager using a custom configuration file.
   */
  public void testCacheManagerWithEhCacheProviderAndCustomConfigLocation() {
    ApplicationContext applicationContext = getApplicationContext("ehCacheCustomValues.xml");

    String cacheManagerBeanId = "customLocationCacheManager";
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

  private ApplicationContext getApplicationContext(String configLocation) {
    return getApplicationContext(new String[] { THIS_PATH + "/"
        + configLocation });
  }

  private ApplicationContext getApplicationContext(String[] configLocations) {
    return new ClassPathXmlApplicationContext(configLocations);
  }

}
