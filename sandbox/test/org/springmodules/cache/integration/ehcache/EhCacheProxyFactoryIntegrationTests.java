/* 
 * Created on Oct 31, 2004
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

package org.springmodules.cache.integration.ehcache;


/**
 * <p>
 * Integration test that verifies that caching and flushing work correctly.
 * </p>
 * <p>
 * Test settings:
 * <ul>
 * <li>EHCache as cache provider</li>
 * <li>The targets of the caching services are configured using a
 * <code>{@link org.springmodules.cache.interceptor.proxy.CacheProxyFactoryBean}</code></li>
 * </ul>
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/05/19 02:20:06 $
 */
public final class EhCacheProxyFactoryIntegrationTests extends
    AbstractEhCacheIntegrationTests {

  /**
   * Constructor.
   */
  public EhCacheProxyFactoryIntegrationTests() {
    super();
  }

  /**
   * @see org.springframework.test.AbstractDependencyInjectionSpringContextTests#getConfigLocations()
   */
  protected String[] getConfigLocations() {
    String[] configFileNames = new String[] {
        "**/ehcacheApplicationContext.xml",
        "**/proxyFactoryApplicationContext.xml" };
    
    return configFileNames;
  }
}