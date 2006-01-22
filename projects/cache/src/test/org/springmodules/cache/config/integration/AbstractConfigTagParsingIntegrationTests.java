/* 
 * Created on Jan 20, 2006
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import junit.framework.TestCase;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import org.springmodules.AssertExt;
import org.springmodules.cache.provider.CacheProviderFacade;
import org.springmodules.cache.provider.PathUtils;
import org.springmodules.cache.serializable.XStreamSerializableFactory;

/**
 * <p>
 * TODO Document class.
 * </p>
 * 
 * @author Alex Ruiz
 */
public abstract class AbstractConfigTagParsingIntegrationTests extends
    TestCase {

  protected class TestFixture {

    public final Object cacheManager;

    public final Object cacheManagerFactoryBean;

    public final CacheProviderFacade cacheProviderFacade;

    public TestFixture(Object newCacheManager,
        Object newCacheManagerFactoryBean,
        CacheProviderFacade newCacheProviderFacade) {
      super();
      cacheManager = newCacheManager;
      cacheManagerFactoryBean = newCacheManagerFactoryBean;
      cacheProviderFacade = newCacheProviderFacade;
    }
  }

  protected static final String CUSTOM_CACHE_PROVIDER_NAME = "customCacheProvider";

  protected static final String DEFAULT_CACHE_PROVIDER_NAME = "cacheProvider";

  protected Log logger = LogFactory.getLog(getClass());

  /**
   * Constructor.
   */
  public AbstractConfigTagParsingIntegrationTests() {
    super();
  }

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case.
   */
  public AbstractConfigTagParsingIntegrationTests(String name) {
    super(name);
  }

  protected final TestFixture assertCacheConfigTagParsingRegisteredCustomValues(
      String[] configLocations) {
    String cacheProviderFacadeName = CUSTOM_CACHE_PROVIDER_NAME;
    TestFixture fixture = getTestFixture(configLocations,
        cacheProviderFacadeName);

    CacheProviderFacade cacheProviderFacade = fixture.cacheProviderFacade;
    AssertExt.assertInstanceOf(XStreamSerializableFactory.class,
        cacheProviderFacade.getSerializableFactory());
    assertTrue(cacheProviderFacade.isFailQuietlyEnabled());

    return fixture;
  }

  protected final TestFixture assertCacheConfigTagParsingRegisteredDefaultValues(
      String[] configLocations) {
    String cacheProviderFacadeName = DEFAULT_CACHE_PROVIDER_NAME;
    TestFixture fixture = getTestFixture(configLocations,
        cacheProviderFacadeName);

    CacheProviderFacade cacheProviderFacade = fixture.cacheProviderFacade;
    assertNull(cacheProviderFacade.getSerializableFactory());
    assertFalse(cacheProviderFacade.isFailQuietlyEnabled());

    return fixture;
  }

  protected final String getCacheManagerFactoryBeanName(
      String cacheProviderFacadeName) {
    return "&" + getCacheManagerName(cacheProviderFacadeName);
  }

  protected final String getCacheManagerName(String cacheProviderFacadeName) {
    return cacheProviderFacadeName + ".cacheManager";
  }

  protected final String getConfigLocationPath(String fileName) {
    return "classpath:" + PathUtils.getPackageNameAsPath(getClass()) + "/"
        + fileName;
  }

  protected final TestFixture getTestFixture(String[] configLocations,
      String cacheProviderFacadeName) {
    ApplicationContext context = new ClassPathXmlApplicationContext(
        configLocations);

    Object cacheManager = context
        .getBean(getCacheManagerName(cacheProviderFacadeName));

    Object cacheManagerFactoryBean = context
        .getBean(getCacheManagerFactoryBeanName(cacheProviderFacadeName));

    CacheProviderFacade cacheProviderFacade = (CacheProviderFacade) context
        .getBean(cacheProviderFacadeName);

    return new TestFixture(cacheManager, cacheManagerFactoryBean,
        cacheProviderFacade);
  }

  protected final void shutdownCacheManager(Object cacheManagerFactoryBean) {
    if (cacheManagerFactoryBean instanceof DisposableBean) {
      try {
        ((DisposableBean) cacheManagerFactoryBean).destroy();

      } catch (Exception exception) {
        logger.debug("Unable to shut down cache manager", exception);
      }
    } else {
      logger.debug("Cache manager does not have a 'destroy' method");
    }
  }
}