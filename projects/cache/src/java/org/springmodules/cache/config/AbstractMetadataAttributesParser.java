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
package org.springmodules.cache.config;

import org.w3c.dom.Element;

import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;

import org.springmodules.cache.interceptor.caching.CachingAttributeSourceAdvisor;
import org.springmodules.cache.interceptor.caching.MetadataCachingInterceptor;
import org.springmodules.cache.interceptor.flush.FlushingAttributeSourceAdvisor;
import org.springmodules.cache.interceptor.flush.MetadataFlushingInterceptor;

/**
 * <p>
 * TODO Document class.
 * </p>
 * 
 * @author Alex Ruiz
 */
public abstract class AbstractMetadataAttributesParser extends
    AbstractCacheSetupStrategyParser {

  protected abstract void configureInterceptors(
      MutablePropertyValues cachingInterceptorPropertyValues,
      MutablePropertyValues flushingInterceptorPropertyValues,
      BeanDefinitionRegistry registry);

  /**
   * @see AbstractCacheSetupStrategyParser#parseCacheSetupStrategy(Element,
   *      BeanDefinitionRegistry, CacheSetupStrategyPropertySource)
   */
  protected void parseCacheSetupStrategy(Element element,
      BeanDefinitionRegistry registry,
      CacheSetupStrategyPropertySource propertySource) {

    // register "autoproxy"
    RootBeanDefinition autoproxy = new RootBeanDefinition(
        DefaultAdvisorAutoProxyCreator.class);
    registry.registerBeanDefinition("autoproxy", autoproxy);

    // create "caching method interceptor"
    MutablePropertyValues cachingInterceptorProperties = new MutablePropertyValues();
    cachingInterceptorProperties.addPropertyValue(propertySource
        .getCacheProviderFacade());
    cachingInterceptorProperties.addPropertyValue(propertySource
        .getCachingListeners());
    cachingInterceptorProperties.addPropertyValue(propertySource
        .getCachingModels());

    Class cachingInterceptorClass = MetadataCachingInterceptor.class;
    RootBeanDefinition cachingInterceptor = new RootBeanDefinition(
        cachingInterceptorClass, cachingInterceptorProperties);

    // create "flushing method interceptor"
    MutablePropertyValues flushingInterceptorProperties = new MutablePropertyValues();
    flushingInterceptorProperties.addPropertyValue(propertySource
        .getCacheProviderFacade());
    flushingInterceptorProperties.addPropertyValue(propertySource
        .getFlushingModels());

    Class flushingInterceptorClass = MetadataFlushingInterceptor.class;
    RootBeanDefinition flushingInterceptor = new RootBeanDefinition(
        flushingInterceptorClass, flushingInterceptorProperties);

    // add extra properties to the interceptors.
    configureInterceptors(cachingInterceptorProperties,
        flushingInterceptorProperties, registry);

    // register interceptors
    String cachingInterceptorName = cachingInterceptorClass.getName();
    registry.registerBeanDefinition(cachingInterceptorName, cachingInterceptor);

    String flushingInterceptorName = flushingInterceptorClass.getName();
    registry.registerBeanDefinition(flushingInterceptorName,
        flushingInterceptor);

    // create and register advisors
    Class cachingAdvisorClass = CachingAttributeSourceAdvisor.class;
    RootBeanDefinition cachingAdvisor = new RootBeanDefinition(
        cachingAdvisorClass);
    cachingAdvisor.getConstructorArgumentValues().addGenericArgumentValue(
        new RuntimeBeanReference(cachingInterceptorName));
    registry.registerBeanDefinition(cachingAdvisorClass.getName(),
        cachingAdvisor);

    Class flushingAdvisorClass = FlushingAttributeSourceAdvisor.class;
    RootBeanDefinition flushingAdvisor = new RootBeanDefinition(
        flushingAdvisorClass);
    flushingAdvisor.getConstructorArgumentValues().addGenericArgumentValue(
        new RuntimeBeanReference(flushingInterceptorName));
    registry.registerBeanDefinition(flushingAdvisorClass.getName(),
        flushingAdvisor);
  }
}
