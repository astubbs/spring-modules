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

package org.springmodules.cache.provider.jcs;

import java.io.InputStream;
import java.util.Properties;

import junit.framework.TestCase;

import org.apache.jcs.engine.behavior.ICompositeCacheAttributes;
import org.apache.jcs.engine.control.CompositeCache;
import org.apache.jcs.engine.control.CompositeCacheManager;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import org.springmodules.cache.provider.PathUtils;

/**
 * <p>
 * Unit Tests for <code>{@link JcsManagerFactoryBean}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public final class JcsManagerFactoryBeanTests extends TestCase {

  private static final String ALTERNATIVE_CONFIG_RESOURCE_NAME = "jcs-config.ccf";

  private static final String DEFAULT_CONFIG_RESOURCE_NAME = "cache.ccf";

  private JcsManagerFactoryBean cacheManagerFactoryBean;

  private Resource configLocation;

  /**
   * Configuration properties to be loaded from
   * <code>{@link #configLocation}</code>.
   */
  private Properties configProperties;

  public JcsManagerFactoryBeanTests(String name) {
    super(name);
  }

  public void testCreateCacheManager() throws Exception {
    setUpAlternativeConfigurationProperties();

    cacheManagerFactoryBean.createCacheManager();

    assertCacheManagerWasConfigured("altTestCache");
  }

  public void testCreateCacheManagerWithConfigLocationEqualToNull()
      throws Exception {
    setUpDefaultConfigurationProperties();
    cacheManagerFactoryBean.setConfigLocation(null);

    cacheManagerFactoryBean.createCacheManager();

    assertCacheManagerWasConfigured("testCache");
  }

  public void testDestroyCacheManager() throws Exception {
    setUpAlternativeConfigurationProperties();
    cacheManagerFactoryBean.createCacheManager();

    CompositeCacheManager cacheManager = getCacheManager();

    assertTrue("There should be at least one cache in the cache manager",
        cacheManager.getCacheNames().length > 0);

    cacheManagerFactoryBean.destroyCacheManager();

    assertTrue("There should not be any cache in the cache manager",
        cacheManager.getCacheNames().length == 0);
  }

  public void testGetObjectType() throws Exception {
    cacheManagerFactoryBean.createCacheManager();
    assertObjectTypeIsCorrect();
  }

  public void testGetObjectTypeWhenCacheManagerIsNull() {
    assertObjectTypeIsCorrect();
  }

  public void testIsSingleton() {
    assertTrue(cacheManagerFactoryBean.isSingleton());
  }

  protected void setUp() {
    cacheManagerFactoryBean = new JcsManagerFactoryBean();
  }

  protected void tearDown() {
    if (cacheManagerFactoryBean != null) {
      try {
        cacheManagerFactoryBean.destroy();
      } catch (Exception exception) {
        // ignore the exception.
      }
    }
  }

  private void assertCacheManagerWasConfigured(String cacheName)
      throws Exception {
    // verify that the cache manager was configured.
    CompositeCacheManager cacheManager = getCacheManager();

    assertNotNull(cacheManager);

    // verify that the properties of the configuration file are the same as the
    // ones of the cache manager.
    String maxObjectsProperty = configProperties.getProperty("jcs.region."
        + cacheName + ".cacheattributes.MaxObjects");

    int expected = Integer.parseInt(maxObjectsProperty);

    CompositeCache cache = cacheManager.getCache(cacheName);
    ICompositeCacheAttributes cacheAttributes = cache.getCacheAttributes();
    int actual = cacheAttributes.getMaxObjects();

    assertEquals(expected, actual);
  }

  private void assertObjectTypeIsCorrect() {
    Class actualObjectType = cacheManagerFactoryBean.getObjectType();
    assertEquals(CompositeCacheManager.class, actualObjectType);
  }

  private CompositeCacheManager getCacheManager() {
    return (CompositeCacheManager) cacheManagerFactoryBean.getObject();
  }

  private void setUpAlternativeConfigurationProperties() throws Exception {
    String configLocationPath = PathUtils.getPackageNameAsPath(getClass())
        + "/" + ALTERNATIVE_CONFIG_RESOURCE_NAME;
    this.setUpConfigurationProperties(configLocationPath);
  }

  private void setUpConfigurationProperties(String configLocationPath)
      throws Exception {
    configLocation = new ClassPathResource(configLocationPath);
    cacheManagerFactoryBean.setConfigLocation(configLocation);

    InputStream inputStream = configLocation.getInputStream();
    configProperties = new Properties();
    configProperties.load(inputStream);
  }

  private void setUpDefaultConfigurationProperties() throws Exception {
    this.setUpConfigurationProperties(DEFAULT_CONFIG_RESOURCE_NAME);
  }

}