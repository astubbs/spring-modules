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

package org.springmodules.cache.provider.oscache;

import java.io.InputStream;
import java.util.Properties;

import junit.framework.TestCase;

import org.springframework.core.io.ClassPathResource;
import org.springmodules.cache.provider.PathUtils;

import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;

/**
 * <p>
 * Unit Tests for <code>{@link OsCacheManagerFactoryBean}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.8 $ $Date: 2005/09/25 05:25:49 $
 */
public final class OsCacheManagerFactoryBeanTests extends TestCase {

  private static final String ALTERNATIVE_CONFIG_RESOURCE_NAME = "oscache-config.properties";

  private static final String CACHE_CAPACITY_PROPERTY_NAME = "cache.capacity";

  private static final String DEFAULT_CONFIG_RESOURCE_NAME = "oscache.properties";

  private OsCacheManagerFactoryBean cacheManagerFactoryBean;

  private ClassPathResource configLocation;

  /**
   * Configuration properties to be loaded from
   * <code>{@link #configLocation}</code>.
   */
  private Properties configProperties;

  public OsCacheManagerFactoryBeanTests(String name) {
    super(name);
  }

  private void assertCacheManagerWasConfigured() {
    GeneralCacheAdministrator cacheAdministrator = getCacheManager();

    assertNotNull(cacheAdministrator);

    String expected = configProperties
        .getProperty(CACHE_CAPACITY_PROPERTY_NAME);

    String actual = cacheAdministrator
        .getProperty(CACHE_CAPACITY_PROPERTY_NAME);

    assertEquals(expected, actual);
  }

  private void assertObjectTypeIsCorrect() {
    Class actualObjectType = cacheManagerFactoryBean.getObjectType();
    assertEquals(GeneralCacheAdministrator.class, actualObjectType);
  }

  private GeneralCacheAdministrator getCacheManager() {
    return (GeneralCacheAdministrator) cacheManagerFactoryBean.getObject();
  }

  protected void setUp() throws Exception {
    super.setUp();
    cacheManagerFactoryBean = new OsCacheManagerFactoryBean();
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

  protected void tearDown() {
    if (cacheManagerFactoryBean != null) {
      try {
        cacheManagerFactoryBean.destroy();
      } catch (Exception exception) {
        // ignore the exception.
      }
    }
  }

  public void testCreateCacheManager() throws Exception {
    setUpAlternativeConfigurationProperties();

    cacheManagerFactoryBean.createCacheManager();

    assertCacheManagerWasConfigured();
  }

  public void testCreateCacheManagerWithConfigLocationEqualToNull()
      throws Exception {
    setUpDefaultConfigurationProperties();

    cacheManagerFactoryBean.setConfigLocation(null);

    cacheManagerFactoryBean.createCacheManager();

    assertCacheManagerWasConfigured();
  }

  public void testDestroyCacheManager() throws Exception {
    setUpAlternativeConfigurationProperties();
    cacheManagerFactoryBean.createCacheManager();

    GeneralCacheAdministrator cacheManager = getCacheManager();

    String key = "jedi";
    String entry = "Anakin";
    cacheManager.putInCache(key, entry);
    assertSame(entry, cacheManager.getFromCache(key));

    cacheManagerFactoryBean.destroyCacheManager();

    try {
      cacheManager.getFromCache(key);
      fail("There should not be any cache elements");

    } catch (NeedsRefreshException needsRefreshException) {
      // we are expecting this exception.
    }
  }

  public void testGetObjectType() throws Exception {
    cacheManagerFactoryBean.createCacheManager();
    assertObjectTypeIsCorrect();
  }

  public void testGetObjectTypeWhenCacheAdministratorIsNull() {
    assertObjectTypeIsCorrect();
  }

  public void testIsSingleton() {
    assertTrue(cacheManagerFactoryBean.isSingleton());
  }
}