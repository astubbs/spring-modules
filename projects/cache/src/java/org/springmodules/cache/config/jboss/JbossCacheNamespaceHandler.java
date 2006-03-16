/* 
 * Created on Mar 16, 2006
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
package org.springmodules.cache.config.jboss;

import org.springmodules.cache.config.AbstractCacheNamespaceHandler;
import org.springmodules.cache.config.AbstractCacheProviderFacadeParser;
import org.springmodules.cache.config.CacheModelParser;
import org.springmodules.cache.config.CacheProviderFacadeValidator;

/**
 * <p>
 * Registers the parsers of the XML elements in the namespace "jboss".
 * </p>
 * 
 * @author Alex Ruiz
 */
public final class JbossCacheNamespaceHandler extends
    AbstractCacheNamespaceHandler {

  private final JbossCacheModelParser cacheModelParser;

  private final JbossCacheFacadeParser cacheProviderFacadeParser;

  private final JbossCacheFacadeValidator cacheProviderFacadeValidator;

  /**
   * Constructor.
   */
  public JbossCacheNamespaceHandler() {
    super();
    cacheProviderFacadeParser = new JbossCacheFacadeParser();
    cacheModelParser = new JbossCacheModelParser();
    cacheProviderFacadeValidator = new JbossCacheFacadeValidator();
  }

  protected CacheModelParser getCacheModelParser() {
    return cacheModelParser;
  }

  protected AbstractCacheProviderFacadeParser getCacheProviderFacadeParser() {
    return cacheProviderFacadeParser;
  }

  protected CacheProviderFacadeValidator getCacheProviderFacadeValidator() {
    return cacheProviderFacadeValidator;
  }

}
