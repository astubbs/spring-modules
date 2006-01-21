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
package org.springmodules.cache.config.integration.ehcache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;

import org.springmodules.AssertExt;
import org.springmodules.cache.config.integration.AbstractCacheNamespaceConfigIntegrationTests;
import org.springmodules.cache.provider.ehcache.EhCacheFacade;

/**
 * <p>
 * TODO Document class.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class EhCacheCacheNamespaceConfigIntegrationTests extends
    AbstractCacheNamespaceConfigIntegrationTests {

  /**
   * Constructor.
   */
  public EhCacheCacheNamespaceConfigIntegrationTests() {
    super();
  }

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case
   */
  public EhCacheCacheNamespaceConfigIntegrationTests(String name) {
    super(name);
  }

  public void testCacheConfigTagParsingWithCustomValues() {
    String[] configLocations = new String[] { getConfigLocationPath("ehCacheCustomConfigContext.xml") };

    TestFixture fixture = assertCacheConfigTagParsingRegisteredCustomValues(configLocations);
    assertTestFixtureIsCorrect(fixture);

    CacheManager cacheManager = (CacheManager) fixture.cacheManager;
    Cache cache = cacheManager.getCache("customCache");
    assertNotNull("The cache manager should have a cache name 'customCache'",
        cache);
    assertEquals("<Max. elements in memory>", 75, cache
        .getMaxElementsInMemory());

    shutdownCacheManager(fixture.cacheManagerFactoryBean);
  }

  public void testCacheConfigTagParsingWithDefaultValues() {
    String[] configLocations = new String[] { getConfigLocationPath("ehCacheConfigContext.xml") };

    TestFixture fixture = assertCacheConfigTagParsingRegisteredDefaultValues(configLocations);
    assertTestFixtureIsCorrect(fixture);

    shutdownCacheManager(fixture.cacheManagerFactoryBean);
  }

  private void assertTestFixtureIsCorrect(TestFixture fixture) {
    AssertExt.assertInstanceOf(EhCacheManagerFactoryBean.class,
        fixture.cacheManagerFactoryBean);
    AssertExt
        .assertInstanceOf(EhCacheFacade.class, fixture.cacheProviderFacade);
  }

}
