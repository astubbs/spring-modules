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
package org.springmodules.cache.config.integration.jboss;

import org.jboss.cache.TreeCache;

import org.springmodules.AssertExt;
import org.springmodules.cache.config.integration.AbstractConfigTagParsingIntegrationTests;
import org.springmodules.cache.provider.jboss.JbossCacheFacade;
import org.springmodules.cache.provider.jboss.JbossCacheManagerFactoryBean;

/**
 * <p>
 * TODO Document class.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class JbossCacheConfigTagParsingIntegrationTests extends
    AbstractConfigTagParsingIntegrationTests {

  /**
   * Constructor.
   */
  public JbossCacheConfigTagParsingIntegrationTests() {
    super();
  }

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case
   */
  public JbossCacheConfigTagParsingIntegrationTests(String name) {
    super(name);
  }

  public void testConfigTagParsingWithCustomValues() {
    String[] configLocations = new String[] { getConfigLocationPath("jbossCacheCustomConfigContext.xml") };

    TestFixture fixture = assertCacheConfigTagParsingRegisteredCustomValues(configLocations);
    assertTestFixtureIsCorrect(fixture);

    TreeCache treeCache = (TreeCache) fixture.cacheManager;
    assertEquals(7000l, treeCache.getInitialStateRetrievalTimeout());
    assertEquals(4554l, treeCache.getSyncReplTimeout());

    shutdownCacheManager(fixture.cacheManagerFactoryBean);
  }

  public void testConfigTagParsingWithDefaultValues() {
    String[] configLocations = new String[] { getConfigLocationPath("jbossCacheConfigContext.xml") };

    TestFixture fixture = assertCacheConfigTagParsingRegisteredDefaultValues(configLocations);
    assertTestFixtureIsCorrect(fixture);

    shutdownCacheManager(fixture.cacheManagerFactoryBean);
  }

  private void assertTestFixtureIsCorrect(TestFixture fixture) {
    AssertExt.assertInstanceOf(JbossCacheManagerFactoryBean.class,
        fixture.cacheManagerFactoryBean);
    AssertExt.assertInstanceOf(JbossCacheFacade.class,
        fixture.cacheProviderFacade);
  }
}
