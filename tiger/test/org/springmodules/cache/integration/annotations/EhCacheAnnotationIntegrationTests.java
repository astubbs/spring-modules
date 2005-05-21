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

package org.springmodules.cache.integration.annotations;

import org.springmodules.cache.integration.ehcache.AbstractEhCacheIntegrationTests;

/**
 * <p>
 * Integration test that verifies that caching and flushing work correctly.
 * </p>
 * <p>
 * Test settings:
 * <ul>
 * <li>EHCache as cache provider</li>
 * <li>Source-level metadata identify the target(s) of the caching services</li>
 * </ul>
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.2 $ $Date: 2005/05/21 21:26:43 $
 */
public final class EhCacheAnnotationIntegrationTests extends
    AbstractEhCacheIntegrationTests {

  /**
   * Constructor.
   */
  public EhCacheAnnotationIntegrationTests() {
    super();
  }

  /**
   * @see org.springframework.test.AbstractDependencyInjectionSpringContextTests#getConfigLocations()
   */
  @Override
  protected String[] getConfigLocations() {
    String[] configFileNames = new String[] {
        "**/ehcacheApplicationContext.xml", "**/annotationApplicationContext.xml" };

    return configFileNames;
  }
}