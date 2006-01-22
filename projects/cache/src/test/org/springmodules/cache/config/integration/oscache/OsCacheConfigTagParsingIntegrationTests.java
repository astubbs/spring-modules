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
package org.springmodules.cache.config.integration.oscache;

import com.opensymphony.oscache.general.GeneralCacheAdministrator;

import org.springmodules.AssertExt;
import org.springmodules.cache.config.integration.AbstractConfigTagParsingIntegrationTests;
import org.springmodules.cache.provider.oscache.OsCacheFacade;
import org.springmodules.cache.provider.oscache.OsCacheManagerFactoryBean;

/**
 * <p>
 * TODO Document class.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class OsCacheConfigTagParsingIntegrationTests extends
    AbstractConfigTagParsingIntegrationTests {

  /**
   * Constructor.
   */
  public OsCacheConfigTagParsingIntegrationTests() {
    super();
  }

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case
   */
  public OsCacheConfigTagParsingIntegrationTests(String name) {
    super(name);
  }

  public void testConfigTagParsingWithCustomValues() {
    String[] configLocations = new String[] { getConfigLocationPath("osCacheCustomConfigContext.xml") };

    TestFixture fixture = assertCacheConfigTagParsingRegisteredCustomValues(configLocations);
    assertTestFixtureIsCorrect(fixture);

    GeneralCacheAdministrator cacheManager = (GeneralCacheAdministrator) fixture.cacheManager;
    assertEquals("<Cache capacity>", "5", cacheManager
        .getProperty("cache.capacity"));

    shutdownCacheManager(fixture.cacheManagerFactoryBean);
  }

  public void testConfigTagParsingWithDefaultValues() {
    String[] configLocations = new String[] { getConfigLocationPath("OsCacheConfigContext.xml") };

    TestFixture fixture = assertCacheConfigTagParsingRegisteredDefaultValues(configLocations);
    assertTestFixtureIsCorrect(fixture);

    shutdownCacheManager(fixture.cacheManagerFactoryBean);
  }

  private void assertTestFixtureIsCorrect(TestFixture fixture) {
    AssertExt.assertInstanceOf(OsCacheManagerFactoryBean.class,
        fixture.cacheManagerFactoryBean);
    AssertExt
        .assertInstanceOf(OsCacheFacade.class, fixture.cacheProviderFacade);
  }
}
