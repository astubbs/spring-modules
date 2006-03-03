/* 
 * Created on Feb 20, 2006
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

import org.springmodules.cache.config.AbstractCacheProxyFactoryBeanParser;
import org.springmodules.cache.config.CacheModelParser;
import org.springmodules.cache.config.CacheProviderFacadeDefinitionValidator;
import org.springmodules.cache.interceptor.proxy.CacheProxyFactoryBean;

/**
 * <p>
 * Parses the XML tag "beanRef" when using the XML namespace "ehcache". Creates,
 * configures and registers and implementation of
 * <code>{@link CacheProxyFactoryBean}</code> in the provided registry of bean
 * definitions.
 * </p>
 * 
 * @author Alex Ruiz
 */
public final class EhCacheProxyFactoryBeanParser extends
    AbstractCacheProxyFactoryBeanParser {

  private CacheModelParser modelParser;

  private CacheProviderFacadeDefinitionValidator validator;

  /**
   * Constructor.
   */
  public EhCacheProxyFactoryBeanParser() {
    super();
    modelParser = new EhCacheModelParser();
    validator = new EhCacheFacadeDefinitionValidator();
  }

  /**
   * @see org.springmodules.cache.config.AbstractCacheSetupStrategyParser#getCacheModelParser()
   */
  protected CacheModelParser getCacheModelParser() {
    return modelParser;
  }

  /**
   * @see org.springmodules.cache.config.AbstractCacheSetupStrategyParser#getCacheProviderFacadeDefinitionValidator()
   */
  protected CacheProviderFacadeDefinitionValidator getCacheProviderFacadeDefinitionValidator() {
    return validator;
  }

}
