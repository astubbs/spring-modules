/* 
 * Created on Jan 22, 2006
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
package org.springmodules.cache.config;

import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;

import org.springmodules.cache.interceptor.caching.MethodMapCachingInterceptor;
import org.springmodules.cache.interceptor.flush.MethodMapFlushingInterceptor;

/**
 * <p>
 * TODO Document class.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class CacheInterceptorsBeanDefinitionParser extends
    AbstractCacheStrategyBeanDefinitionParser {

  private static class XmlAttribute {

    static final String CACHING_INTERCEPTOR_ID = "cachingInterceptorId";

    static final String FLUSHING_INTERCEPTOR_ID = "flushingInterceptorId";
  }

  /**
   * Constructor.
   */
  public CacheInterceptorsBeanDefinitionParser() {
    super();
  }

  /**
   * @see org.springmodules.cache.config.AbstractCacheStrategyBeanDefinitionParser#doParse(Element,
   *      BeanDefinitionRegistry, String, Map, Map, List)
   */
  @Override
  protected void doParse(Element element, BeanDefinitionRegistry registry,
      String providerId, Map cachingModels, Map flushingModels,
      List cachingListeners) {

    RootBeanDefinition cachingInterceptor = new RootBeanDefinition(
        MethodMapCachingInterceptor.class);
    cachingInterceptor.setPropertyValues(new MutablePropertyValues());
    setCacheProvider(cachingInterceptor, providerId);
    setCachingListeners(cachingInterceptor, cachingListeners);
    setCachingModels(cachingInterceptor, cachingModels);

    String cachingInterceptorId = element
        .getAttribute(XmlAttribute.CACHING_INTERCEPTOR_ID);
    registry.registerBeanDefinition(cachingInterceptorId, cachingInterceptor);

    RootBeanDefinition flushingInterceptor = new RootBeanDefinition(
        MethodMapFlushingInterceptor.class);
    flushingInterceptor.setPropertyValues(new MutablePropertyValues());
    setCacheProvider(flushingInterceptor, providerId);
    setFlushingModels(flushingInterceptor, flushingModels);

    String flushingInterceptorId = element
        .getAttribute(XmlAttribute.FLUSHING_INTERCEPTOR_ID);
    registry.registerBeanDefinition(flushingInterceptorId, flushingInterceptor);
  }

}
