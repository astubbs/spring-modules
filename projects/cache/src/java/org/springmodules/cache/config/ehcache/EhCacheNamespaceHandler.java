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

import org.springmodules.cache.config.AbstractCacheNamespaceHandler;
import org.springmodules.cache.config.AbstractCacheProviderFacadeParser;
import org.springmodules.cache.config.AbstractCacheProxyFactoryBeanParser;
import org.springmodules.cache.config.AbstractCommonsAttributesParser;
import org.springmodules.cache.config.AbstractInterceptorsParser;

/**
 * <p>
 * TODO Document class.
 * </p>
 * 
 * @author Alex Ruiz
 */
public final class EhCacheNamespaceHandler extends AbstractCacheNamespaceHandler {

  /**
   * @see AbstractCacheNamespaceHandler#getAnnotationsParserClassName()
   */
  protected String getAnnotationsParserClassName() {
    return getClass().getPackage().getName() + ".EhCacheAnnotationsParser";
  }

  /**
   * @see AbstractCacheNamespaceHandler#getCacheProviderFacadeParser()
   */
  protected AbstractCacheProviderFacadeParser getCacheProviderFacadeParser() {
    return new EhCacheFacadeParser();
  }

  /**
   * @see AbstractCacheNamespaceHandler#getCacheProxyFactoryBeanParser()
   */
  protected AbstractCacheProxyFactoryBeanParser getCacheProxyFactoryBeanParser() {
    return new EhCacheProxyFactoryBeanParser();
  }

  /**
   * @see AbstractCacheNamespaceHandler#getCommonsAttributeParser()
   */
  protected AbstractCommonsAttributesParser getCommonsAttributeParser() {
    return new EhCacheCommonsAttributesParser();
  }

  /**
   * @see AbstractCacheNamespaceHandler#getInterceptorsParser()
   */
  protected AbstractInterceptorsParser getInterceptorsParser() {
    return new EhCacheInterceptorsParser();
  }

}
