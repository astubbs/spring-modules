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

import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
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
public abstract class AbstractAttributesBeanDefinitionParser extends
    AbstractCacheStrategyBeanDefinitionParser {

  private static class CommonBeanName {

    static final String AUTOPROXY = DefaultAdvisorAutoProxyCreator.class
        .getName();

    static final String CACHING_ADVISOR = CachingAttributeSourceAdvisor.class
        .getName();

    static final String CACHING_INTERCEPTOR = MetadataCachingInterceptor.class
        .getName();

    static final String FLUSHING_ADVISOR = FlushingAttributeSourceAdvisor.class
        .getName();

    static final String FLUSHING_INTERCEPTOR = MetadataFlushingInterceptor.class
        .getName();
  }

  /**
   * Constructor.
   */
  public AbstractAttributesBeanDefinitionParser() {
    super();
  }

  /**
   * @see AbstractCacheStrategyBeanDefinitionParser#doParse(Element,
   *      BeanDefinitionRegistry, String, Map, Map, List)
   */
  protected final void doParse(Element element,
      BeanDefinitionRegistry registry, String providerId, Map cachingModels,
      Map flushingModels, List cachingListeners) {

    RootBeanDefinition autoproxy = new RootBeanDefinition(
        DefaultAdvisorAutoProxyCreator.class);
    registry.registerBeanDefinition(CommonBeanName.AUTOPROXY, autoproxy);

    RootBeanDefinition cachingInterceptor = new RootBeanDefinition(
        MetadataCachingInterceptor.class);
    cachingInterceptor.setPropertyValues(new MutablePropertyValues());

    RootBeanDefinition flushingInterceptor = new RootBeanDefinition(
        MetadataFlushingInterceptor.class);
    flushingInterceptor.setPropertyValues(new MutablePropertyValues());

    registerInterceptorProperties(cachingInterceptor, flushingInterceptor,
        registry);

    setCacheProvider(cachingInterceptor, providerId);
    setCachingListeners(cachingInterceptor, cachingListeners);
    setCachingModels(cachingInterceptor, cachingModels);
    registry.registerBeanDefinition(CommonBeanName.CACHING_INTERCEPTOR,
        cachingInterceptor);

    RootBeanDefinition cachingAdvisor = new RootBeanDefinition(
        CachingAttributeSourceAdvisor.class);
    cachingAdvisor.getConstructorArgumentValues().addGenericArgumentValue(
        new RuntimeBeanReference(CommonBeanName.CACHING_INTERCEPTOR));
    registry.registerBeanDefinition(CommonBeanName.CACHING_ADVISOR,
        cachingAdvisor);

    setCacheProvider(flushingInterceptor, providerId);
    setFlushingModels(flushingInterceptor, flushingModels);
    registry.registerBeanDefinition(CommonBeanName.FLUSHING_INTERCEPTOR,
        flushingInterceptor);

    RootBeanDefinition flushingAdvisor = new RootBeanDefinition(
        FlushingAttributeSourceAdvisor.class);
    flushingAdvisor.getConstructorArgumentValues().addGenericArgumentValue(
        new RuntimeBeanReference(CommonBeanName.FLUSHING_INTERCEPTOR));
    registry.registerBeanDefinition(CommonBeanName.FLUSHING_ADVISOR,
        flushingAdvisor);
  }

  protected abstract void registerInterceptorProperties(
      AbstractBeanDefinition cachingInterceptor,
      AbstractBeanDefinition flushingInterceptor,
      BeanDefinitionRegistry registry);
}
