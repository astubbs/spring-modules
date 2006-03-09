/* 
 * Created on Mar 8, 2006
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

import junit.framework.Assert;

import org.springmodules.cache.config.CacheModelParser;
import org.springmodules.cache.config.CacheProviderFacadeDefinitionValidator;

/**
 * <p>
 * Assert methods related EHCACHE schema-based configuration.
 * </p>
 * 
 * @author Alex Ruiz
 */
public abstract class EhCacheConfigAssert {

  public static void assertCacheModelParserIsCorrect(CacheModelParser parser) {
    Assert.assertNotNull(parser);

    String message = "CacheModelParser should be an instance of <"
        + EhCacheModelParser.class.getName() + ">, not an instance of <"
        + parser.getClass().getName() + ">";

    Assert.assertTrue(message, parser instanceof EhCacheModelParser);
  }

  public static void assertCacheProviderFacadeDefinitionValidatorIsCorrect(
      CacheProviderFacadeDefinitionValidator validator) {
    Assert.assertNotNull(validator);

    String message = "CacheProviderFacadeDefinitionValidator should be an "
        + "instance of <" + EhCacheFacadeDefinitionValidator.class.getName()
        + ">, not an instance of <" + validator.getClass().getName() + ">";

    Assert.assertTrue(message,
        validator instanceof EhCacheFacadeDefinitionValidator);
  }
}
