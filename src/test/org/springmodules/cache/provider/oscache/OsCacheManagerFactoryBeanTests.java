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

import org.springframework.core.io.ClassPathResource;
import org.springmodules.cache.provider.AbstractCacheManagerFactoryBeanTests;

import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;

/**
 * <p>
 * Unit Tests for <code>{@link OsCacheManagerFactoryBean}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.6 $ $Date: 2005/09/09 02:19:21 $
 */
public final class OsCacheManagerFactoryBeanTests extends
    AbstractCacheManagerFactoryBeanTests {

  private static final String CACHE_CAPACITY_PROPERTY_NAME = "cache.capacity";

  private OsCacheManagerFactoryBean cacheManagerFactoryBean;

  /**
   * Contains the location of the configuration file for
   * <code>{@link #cacheManagerFactoryBean}</code>.
   */
  private ClassPathResource configLocation;

  /**
   * Configuration properties read from <code>{@link #configLocation}</code>.
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

    configLocation = new ClassPathResource(getPackageNameAsPath()
        + "/oscache-config.properties");

    cacheManagerFactoryBean = new OsCacheManagerFactoryBean();
    cacheManagerFactoryBean.setConfigLocation(configLocation);

    setUpConfigProperties();
  }

  protected void setUpConfigProperties() throws Exception {
    InputStream inputStream = configLocation.getInputStream();
    configProperties = new Properties();
    configProperties.load(inputStream);
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

  /**
   * Verifies that the method
   * <code>{@link OsCacheManagerFactoryBean#afterPropertiesSet()}</code>
   * creates a new cache manager which properties are loaded from a
   * configuration file.
   */
  public void testAfterPropertiesSet() throws Exception {
    cacheManagerFactoryBean.afterPropertiesSet();
    assertCacheManagerWasConfigured();
  }

  /**
   * Verifies that the method
   * <code>{@link OsCacheManagerFactoryBean#afterPropertiesSet()}</code>
   * creates a new cache manager using the default configuration file,
   * "oscache.properties", if there is not any configuration file specified.
   */
  public void testAfterPropertiesSetWithConfigLocationEqualToNull()
      throws Exception {

    configLocation = new ClassPathResource("oscache.properties");
    setUpConfigProperties();

    cacheManagerFactoryBean.setConfigLocation(null);
    cacheManagerFactoryBean.afterPropertiesSet();

    assertCacheManagerWasConfigured();
  }

  public void testDestroy() throws Exception {
    cacheManagerFactoryBean.afterPropertiesSet();

    GeneralCacheAdministrator cacheManager = getCacheManager();

    String key = "javaGuy";
    String entry = "James Gosling";
    cacheManager.putInCache(key, entry);
    Object cachedObject = cacheManager.getFromCache(key);
    assertSame("<Cached object>", entry, cachedObject);

    cacheManagerFactoryBean.destroy();

    try {
      cacheManager.getFromCache(key);
      fail("There should not be any cache elements");

    } catch (NeedsRefreshException needsRefreshException) {
      // we are expecting this exception.
    }
  }

  public void testDestroyWithCacheManagerEqualToNull() throws Exception {
    // verify that the cache manager is null before calling 'destroy()'
    assertNull(cacheManagerFactoryBean.getObject());

    cacheManagerFactoryBean.destroy();

    // verify that the cache manager is null after calling 'destroy()'
    assertNull(cacheManagerFactoryBean.getObject());
  }

  public void testGetObjectType() throws Exception {
    // test when the cache manager has been created.
    cacheManagerFactoryBean.afterPropertiesSet();
    assertObjectTypeIsCorrect();
  }

  public void testGetObjectTypeWhenCacheAdministratorIsNull() {
    // test when the cache manager has not been created yet.
    assertObjectTypeIsCorrect();
  }

  public void testIsSingleton() {
    assertTrue(cacheManagerFactoryBean.isSingleton());
  }
}