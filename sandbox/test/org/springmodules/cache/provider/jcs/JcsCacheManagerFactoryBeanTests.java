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

import org.apache.jcs.engine.behavior.ICompositeCacheAttributes;
import org.apache.jcs.engine.control.CompositeCache;
import org.apache.jcs.engine.control.CompositeCacheManager;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springmodules.cache.provider.AbstractCacheManagerFactoryBeanTests;

/**
 * <p>
 * Unit Test for <code>{@link JcsCacheManagerFactoryBean}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/04/22 02:19:10 $
 */
public final class JcsCacheManagerFactoryBeanTests extends
    AbstractCacheManagerFactoryBeanTests {

  /**
   * Path of an alternative configuration file for JCS.
   */
  private String alternativeCacheConfigurationFilePath;

  /**
   * Primary object (instance of the class to test).
   */
  private JcsCacheManagerFactoryBean cacheManagerFactoryBean;

  /**
   * Contains the location of the configuration file for
   * <code>{@link #cacheManagerFactoryBean}</code>.
   */
  private Resource configLocation;

  /**
   * Configuration properties read from <code>{@link #configLocation}</code>.
   */
  private Properties configProperties;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the Test Case.
   */
  public JcsCacheManagerFactoryBeanTests(String name) {
    super(name);
  }

  /**
   * Sets up the test fixture.
   */
  protected void setUp() throws Exception {
    super.setUp();
    this.alternativeCacheConfigurationFilePath = super.getPackageNameAsPath()
        + "/jcs-config.ccf";
  }

  /**
   * Sets up <code>{@link #cacheManagerFactoryBean}</code>.
   */
  private void setUpCacheManagerFactoryBean() {
    this.cacheManagerFactoryBean = new JcsCacheManagerFactoryBean();
  }

  /**
   * Sets up:
   * <ul>
   * <li><code>{@link #configLocation}</code></li>
   * <li><code>{@link #configProperties}</code></li>
   * </ul>
   * 
   * @param configurationFileName
   *          the name of the configuration file to use.
   */
  private void setUpConfigurationProperties(String configurationFileName)
      throws Exception {

    this.configLocation = new ClassPathResource(configurationFileName);
    this.cacheManagerFactoryBean.setConfigLocation(this.configLocation);

    InputStream inputStream = this.configLocation.getInputStream();
    this.configProperties = new Properties();
    this.configProperties.load(inputStream);
  }

  /**
   * Cleans up resources.
   */
  protected void tearDown() {
    if (this.cacheManagerFactoryBean != null) {
      try {
        this.cacheManagerFactoryBean.destroy();
      } catch (Exception exception) {
        // ignore the exception.
      } // end 'catch'
    } // end 'if'
  }

  /**
   * Verifies that the method
   * <code>{@link JcsCacheManagerFactoryBean#afterPropertiesSet()}</code>
   * creates a new cache manager which properties are loaded from a
   * configuration file.
   */
  public void testAfterPropertiesSet() throws Exception {
    this.setUpCacheManagerFactoryBean();
    this
        .setUpConfigurationProperties(this.alternativeCacheConfigurationFilePath);

    // execute the method to test.
    this.cacheManagerFactoryBean.afterPropertiesSet();

    // verify that the cache manager was configured.
    CompositeCacheManager cacheManager = (CompositeCacheManager) this.cacheManagerFactoryBean
        .getObject();

    assertNotNull("The cache manager should not be null", cacheManager);

    // verify that the properties of the configuration file are the same as the
    // ones of the cache manager.
    String maxObjects = this.configProperties
        .getProperty("jcs.region.altTestCache.cacheattributes.MaxObjects");
    int expectedMaxObjects = Integer.parseInt(maxObjects);

    CompositeCache cache = cacheManager.getCache("altTestCache");
    ICompositeCacheAttributes cacheAttributes = cache.getCacheAttributes();
    int actualMaxObjects = cacheAttributes.getMaxObjects();

    assertEquals("<Property 'maxObjects'>", expectedMaxObjects,
        actualMaxObjects);
  }

  /**
   * Verifies that the method
   * <code>{@link JcsCacheManagerFactoryBean#afterPropertiesSet()}</code>
   * creates a new cache manager using the default configuration file,
   * "cache.ccf", if there is not any configuration file specified.
   */
  public void testAfterPropertiesSetWithConfigLocationEqualToNull()
      throws Exception {

    this.setUpCacheManagerFactoryBean();
    this.setUpConfigurationProperties("cache.ccf");
    this.cacheManagerFactoryBean.setConfigLocation(null);

    // execute the method to test.
    this.cacheManagerFactoryBean.afterPropertiesSet();

    // verify that the cache manager was configured.
    CompositeCacheManager cacheManager = (CompositeCacheManager) this.cacheManagerFactoryBean
        .getObject();

    assertNotNull("The cache manager should not be null", cacheManager);

    // verify that the properties of the configuration file are the same as the
    // ones of the cache manager.
    String maxObjects = this.configProperties
        .getProperty("jcs.region.testCache.cacheattributes.MaxObjects");
    int expectedMaxObjects = Integer.parseInt(maxObjects);

    CompositeCache cache = cacheManager.getCache("testCache");
    ICompositeCacheAttributes cacheAttributes = cache.getCacheAttributes();
    int actualMaxObjects = cacheAttributes.getMaxObjects();

    assertEquals("<Property 'maxObjects'>", expectedMaxObjects,
        actualMaxObjects);
  }

  /**
   * Verifies that the method
   * <code>{@link JcsCacheManagerFactoryBean#destroy()}</code> frees the
   * caches of the cache manager.
   */
  public void testDestroy() throws Exception {
    this.setUpCacheManagerFactoryBean();
    this
        .setUpConfigurationProperties(this.alternativeCacheConfigurationFilePath);

    // create the cache manager.
    this.cacheManagerFactoryBean.afterPropertiesSet();

    // verify that there are caches in the cache manager.
    CompositeCacheManager cacheManager = (CompositeCacheManager) this.cacheManagerFactoryBean
        .getObject();

    assertTrue("There should be at least one cache in the cache manager",
        cacheManager.getCacheNames().length > 0);

    // execute the method to test.
    this.cacheManagerFactoryBean.destroy();

    // verify that the caches were flushed.
    assertTrue("There should not be any cache in the cache manager",
        cacheManager.getCacheNames().length == 0);
  }

  /**
   * Verifies that the method
   * <code>{@link JcsCacheManagerFactoryBean#destroy()}</code> does not throw
   * any exception if the cache manager is <code>null</code>.
   */
  public void testDestroyWithCacheManagerEqualToNull() throws Exception {
    this.setUpCacheManagerFactoryBean();

    // verify that the cache manager is null before calling 'destroy()'
    assertNull("The cache manager should be null", this.cacheManagerFactoryBean
        .getObject());

    try {
      this.cacheManagerFactoryBean.destroy();
    } catch (Throwable throwable) {
      fail("No exception should have been thrown");
    }

    // verify that the cache manager is null after calling 'destroy()'
    assertNull("The cache manager should be null", this.cacheManagerFactoryBean
        .getObject());
  }

  /**
   * Verifies that the method
   * <code>{@link JcsCacheManagerFactoryBean#getObjectType()}</code> returns
   * the class of the created cache manager.
   */
  public void testGetObjectType() throws Exception {
    this.setUpCacheManagerFactoryBean();

    Class expectedObjectType = CompositeCacheManager.class;

    // test when the cache manager has been created.
    this.cacheManagerFactoryBean.afterPropertiesSet();

    Class actualObjectType = this.cacheManagerFactoryBean.getObjectType();
    assertEquals("<Object type>", expectedObjectType, actualObjectType);

  }

  /**
   * Verifies that the method
   * <code>{@link JcsCacheManagerFactoryBean#getObjectType()}</code> returns
   * the class 'org.apache.jcs.engine.control.CompositeCacheManager' if the
   * cache manager has not been created yet.
   */
  public void testGetObjectTypeWhenCacheManagerIsNull() {
    this.setUpCacheManagerFactoryBean();

    Class expectedObjectType = CompositeCacheManager.class;

    // test when the cache manager has not been created yet.
    Class actualObjectType = this.cacheManagerFactoryBean.getObjectType();

    assertEquals("<Object type>", expectedObjectType, actualObjectType);
  }

  /**
   * Verifies that <code>{@link JcsCacheManagerFactoryBean}</code> notifies
   * the Spring IoC container that is a singleton.
   */
  public void testIsSingleton() {
    this.setUpCacheManagerFactoryBean();

    assertTrue("The factory of cache managers should be a singleton",
        this.cacheManagerFactoryBean.isSingleton());
  }

}