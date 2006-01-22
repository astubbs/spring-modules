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
package org.springmodules.cache.config.integration.jcs;

import org.apache.jcs.engine.behavior.ICompositeCacheAttributes;
import org.apache.jcs.engine.control.CompositeCache;
import org.apache.jcs.engine.control.CompositeCacheManager;

import org.springmodules.AssertExt;
import org.springmodules.cache.config.integration.AbstractConfigTagParsingIntegrationTests;
import org.springmodules.cache.provider.jcs.JcsFacade;
import org.springmodules.cache.provider.jcs.JcsManagerFactoryBean;

/**
 * <p>
 * TODO Document class.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class JcsCacheConfigTagParsingIntegrationTests extends
    AbstractConfigTagParsingIntegrationTests {

  /**
   * Constructor.
   */
  public JcsCacheConfigTagParsingIntegrationTests() {
    super();
  }

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case
   */
  public JcsCacheConfigTagParsingIntegrationTests(String name) {
    super(name);
  }

  public void testConfigTagParsingWithCustomValues() {
    String[] configLocations = new String[] { getConfigLocationPath("jcsCustomConfigContext.xml") };

    TestFixture fixture = assertCacheConfigTagParsingRegisteredCustomValues(configLocations);
    assertTestFixtureIsCorrect(fixture);

    CompositeCacheManager cacheManager = (CompositeCacheManager) fixture.cacheManager;

    String cacheName = "testCache";
    CompositeCache cache = cacheManager.getCache(cacheName);
    assertNotNull("The cache manager should have a cache name '" + cacheName
        + "'", cache);
    ICompositeCacheAttributes cacheAttributes = cache.getCacheAttributes();
    assertEquals("<Max. objects>", 20, cacheAttributes.getMaxObjects());

    shutdownCacheManager(fixture.cacheManagerFactoryBean);
  }

  public void testConfigTagParsingWithDefaultValues() {
    String[] configLocations = new String[] { getConfigLocationPath("jcsConfigContext.xml") };

    TestFixture fixture = assertCacheConfigTagParsingRegisteredDefaultValues(configLocations);
    assertTestFixtureIsCorrect(fixture);

    shutdownCacheManager(fixture.cacheManagerFactoryBean);
  }

  private void assertTestFixtureIsCorrect(TestFixture fixture) {
    AssertExt.assertInstanceOf(JcsManagerFactoryBean.class,
        fixture.cacheManagerFactoryBean);
    AssertExt.assertInstanceOf(JcsFacade.class, fixture.cacheProviderFacade);
  }
}
