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

import org.springframework.beans.factory.xml.BeanDefinitionParser;

import org.springmodules.cache.config.AbstractCacheNamespaceHandler;

/**
 * <p>
 * Registers the parsers of the XML elements in the namespace "ehcache".
 * </p>
 * 
 * @author Alex Ruiz
 */
public final class EhCacheNamespaceHandler extends
    AbstractCacheNamespaceHandler {

  /**
   * @see AbstractCacheNamespaceHandler#getAnnotationsParserClassName()
   */
  protected String getAnnotationsParserClassName() {
    return getClass().getPackage().getName() + ".EhCacheAnnotationsParser";
  }

  protected BeanDefinitionParser getCacheProviderFacadeParser() {
    return new EhCacheFacadeParser();
  }

  protected BeanDefinitionParser getCacheProxyFactoryBeanParser() {
    return new EhCacheProxyFactoryBeanParser();
  }

  protected BeanDefinitionParser getCommonsAttributeParser() {
    return new EhCacheCommonsAttributesParser();
  }

  protected BeanDefinitionParser getInterceptorsParser() {
    return new EhCacheInterceptorsParser();
  }

}
