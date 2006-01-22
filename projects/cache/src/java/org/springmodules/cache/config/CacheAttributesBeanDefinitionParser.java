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
import org.springframework.metadata.commons.CommonsAttributes;

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
public class CacheAttributesBeanDefinitionParser extends
    AbstractCacheStrategyBeanDefinitionParser {

  private static class AttributeType {

    static final String COMMONS_ATTRIBUTES = "COMMONS_ATTRIBUTES";

    static final String JDK5_ANNOTATIONS = "JDK5_ANNOTATIONS";
  }

  private static class BeanName {

    static final String ATTRIBUTES = CommonsAttributes.class.getName();

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

  private static class PropertyName {

    static final String ATTRIBUTES = "attributes";

  }

  private static class XmlAttribute {

    static final String TYPE = "type";
  }

  protected void doParse(Element element, BeanDefinitionRegistry registry,
      String providerId, Map cachingModels, Map flushingModels,
      List cachingListeners) {
    RootBeanDefinition autoproxy = new RootBeanDefinition(
        DefaultAdvisorAutoProxyCreator.class);
    registry.registerBeanDefinition(BeanName.AUTOPROXY, autoproxy);

    RootBeanDefinition cachingInterceptor = new RootBeanDefinition(
        MetadataCachingInterceptor.class);
    cachingInterceptor.setPropertyValues(new MutablePropertyValues());

    RootBeanDefinition flushingInterceptor = new RootBeanDefinition(
        MetadataFlushingInterceptor.class);
    flushingInterceptor.setPropertyValues(new MutablePropertyValues());

    String type = element.getAttribute(XmlAttribute.TYPE);
    if (AttributeType.COMMONS_ATTRIBUTES.equalsIgnoreCase(type)) {
      RootBeanDefinition attributes = new RootBeanDefinition(
          CommonsAttributes.class);
      registry.registerBeanDefinition(BeanName.ATTRIBUTES, attributes);

      setAttributes(cachingInterceptor);
      setAttributes(flushingInterceptor);

    } else if (AttributeType.JDK5_ANNOTATIONS.equalsIgnoreCase(type)) {

    } else {
      // TODO throw exception
    }

    setCacheProvider(cachingInterceptor, providerId);
    setCachingListeners(cachingInterceptor, cachingListeners);
    setCachingModels(cachingInterceptor, cachingModels);
    registry.registerBeanDefinition(BeanName.CACHING_INTERCEPTOR,
        cachingInterceptor);

    RootBeanDefinition cachingAdvisor = new RootBeanDefinition(
        CachingAttributeSourceAdvisor.class);
    cachingAdvisor.getConstructorArgumentValues().addGenericArgumentValue(
        new RuntimeBeanReference(BeanName.CACHING_INTERCEPTOR));
    registry.registerBeanDefinition(BeanName.CACHING_ADVISOR, cachingAdvisor);

    setCacheProvider(flushingInterceptor, providerId);
    setFlushingModels(flushingInterceptor, flushingModels);
    registry.registerBeanDefinition(BeanName.FLUSHING_INTERCEPTOR,
        flushingInterceptor);

    RootBeanDefinition flushingAdvisor = new RootBeanDefinition(
        FlushingAttributeSourceAdvisor.class);
    flushingAdvisor.getConstructorArgumentValues().addGenericArgumentValue(
        new RuntimeBeanReference(BeanName.FLUSHING_INTERCEPTOR));
    registry.registerBeanDefinition(BeanName.FLUSHING_ADVISOR,
        flushingAdvisor);

  }

  private void setAttributes(AbstractBeanDefinition definition) {
    definition.getPropertyValues().addPropertyValue(PropertyName.ATTRIBUTES,
        new RuntimeBeanReference(BeanName.ATTRIBUTES));
  }

}
