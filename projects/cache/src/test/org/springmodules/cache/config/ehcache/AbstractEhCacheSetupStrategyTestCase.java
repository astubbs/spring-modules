/* 
 * Created on Mar 15, 2006
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
package org.springmodules.cache.config.ehcache;

import org.springmodules.cache.config.CacheModelParser;
import org.springmodules.cache.config.CacheProviderFacadeDefinitionValidator;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * <p>
 * Test Cases for cache setup strategies for EHCache.
 * </p>
 * 
 * @author Alex Ruiz
 */
public abstract class AbstractEhCacheSetupStrategyTestCase extends TestCase {

  /**
   * Constructor.
   */
  public AbstractEhCacheSetupStrategyTestCase() {
    super();
  }

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case
   */
  public AbstractEhCacheSetupStrategyTestCase(String name) {
    super(name);
  }

  public abstract void testGetCacheModelParser();

  public abstract void testGetCacheProviderFacadeDefinitionValidator();

  protected void assertCacheModelParserIsCorrect(CacheModelParser parser) {
    Assert.assertNotNull(parser);

    String message = "CacheModelParser should be an instance of <"
        + EhCacheModelParser.class.getName() + ">, not an instance of <"
        + parser.getClass().getName() + ">";

    Assert.assertTrue(message, parser instanceof EhCacheModelParser);
  }

  protected void assertCacheProviderFacadeDefinitionValidatorIsCorrect(
      CacheProviderFacadeDefinitionValidator validator) {
    Assert.assertNotNull(validator);

    String message = "CacheProviderFacadeDefinitionValidator should be an "
        + "instance of <" + EhCacheFacadeDefinitionValidator.class.getName()
        + ">, not an instance of <" + validator.getClass().getName() + ">";

    Assert.assertTrue(message,
        validator instanceof EhCacheFacadeDefinitionValidator);
  }

}
