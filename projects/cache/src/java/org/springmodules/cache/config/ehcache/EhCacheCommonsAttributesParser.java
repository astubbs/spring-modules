/* 
 * Created on Feb 26, 2006
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

import org.springmodules.cache.config.AbstractCommonsAttributesParser;
import org.springmodules.cache.config.CacheModelParser;
import org.springmodules.cache.config.CacheProviderFacadeValidator;

/**
 * <p>
 * TODO Document class.
 * </p>
 * 
 * @author Alex Ruiz
 */
public final class EhCacheCommonsAttributesParser extends
    AbstractCommonsAttributesParser {

  private CacheModelParser modelParser;

  private CacheProviderFacadeValidator validator;

  /**
   * Constructor.
   */
  public EhCacheCommonsAttributesParser() {
    super();
    modelParser = new EhCacheModelParser();
    validator = new EhCacheFacadeValidator();
  }

  /**
   * @see org.springmodules.cache.config.AbstractCacheSetupStrategyParser#getCacheModelParser()
   */
  protected CacheModelParser getCacheModelParser() {
    return modelParser;
  }

  /**
   * @see org.springmodules.cache.config.AbstractCacheSetupStrategyParser#getCacheProviderFacadeValidator()
   */
  protected CacheProviderFacadeValidator getCacheProviderFacadeValidator() {
    return validator;
  }

}
