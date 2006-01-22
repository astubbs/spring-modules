/* 
 * Created on Jan 21, 2006
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
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.util.StringUtils;

import org.springmodules.cache.interceptor.proxy.CacheProxyFactoryBean;

/**
 * <p>
 * TODO Document class.
 * </p>
 * 
 * @author Alex Ruiz
 */
public final class CacheBeanRefBeanDefinitionParser extends
    AbstractCacheStrategyBeanDefinitionParser implements BeanDefinitionParser {

  private static class PropertyName {

    static final String TARGET = "target";
  }

  private static class XmlAttribute {

    static final String ID = "id";

    static final String REF = "ref";
  }

  /**
   * Constructor.
   */
  public CacheBeanRefBeanDefinitionParser() {
    super();
  }

  /**
   * @see BeanDefinitionParser#parse(Element, BeanDefinitionRegistry)
   */
  public void doParse(Element element, BeanDefinitionRegistry registry,
      String providerId, Map cachingModels, Map flushingModels,
      List cachingListeners) {
    String ref = element.getAttribute(XmlAttribute.REF);
    if (!registry.containsBeanDefinition(ref)) {
      throw new IllegalStateException("Unable to find bean definition with id "
          + StringUtils.quote(ref));
    }

    RootBeanDefinition cacheProxyFactory = new RootBeanDefinition(
        CacheProxyFactoryBean.class);
    cacheProxyFactory.setPropertyValues(new MutablePropertyValues());
    cacheProxyFactory.getPropertyValues().addPropertyValue(
        CommonPropertyName.CACHE_PROVIDER_FACADE,
        new RuntimeBeanReference(providerId));
    if (cachingListeners != null && !cachingListeners.isEmpty()) {
      cacheProxyFactory.getPropertyValues().addPropertyValue(
          CommonPropertyName.CACHING_LISTENERS, cachingListeners);
    }
    cacheProxyFactory.getPropertyValues().addPropertyValue(
        CommonPropertyName.CACHING_MODELS, cachingModels);
    cacheProxyFactory.getPropertyValues().addPropertyValue(
        CommonPropertyName.FLUSHING_MODELS, flushingModels);
    cacheProxyFactory.getPropertyValues().addPropertyValue(PropertyName.TARGET,
        new RuntimeBeanReference(ref));

    String id = element.getAttribute(XmlAttribute.ID);

    registry.registerBeanDefinition(id, cacheProxyFactory);
  }
}
