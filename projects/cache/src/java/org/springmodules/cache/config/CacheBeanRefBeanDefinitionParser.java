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
import org.springframework.util.xml.DomUtils;

import org.springmodules.cache.interceptor.proxy.CacheProxyFactoryBean;
import org.springmodules.cache.provider.ehcache.EhCacheFacade;

/**
 * <p>
 * TODO Document class.
 * </p>
 * 
 * @author Alex Ruiz
 */
public final class CacheBeanRefBeanDefinitionParser implements
    BeanDefinitionParser {

  private static class PropertyName {

    static final String CACHE_PROVIDER_FACADE = "cacheProviderFacade";

    static final String CACHING_LISTENERS = "cachingListeners";

    static final String CACHING_MODELS = "cachingModels";

    static final String FLUSHING_MODELS = "flushingModels";

    static final String TARGET = "target";
  }

  private static class XmlAttribute {

    static final String ID = "id";

    static final String PROVIDER_ID = "providerId";

    static final String REF = "ref";
  }

  private static class XmlElement {

    static final String CACHING_LISTENERS = "cachingListeners";
  }

  private CachingListenerParser cachingListenerParser;

  private CacheModelParser ehCacheModelParser;

  /**
   * Constructor.
   */
  public CacheBeanRefBeanDefinitionParser() {
    super();
    cachingListenerParser = new CachingListenerParser();
    ehCacheModelParser = new EhCacheModelParser();
  }

  /**
   * @see BeanDefinitionParser#parse(Element, BeanDefinitionRegistry)
   */
  public void parse(Element element, BeanDefinitionRegistry registry) {
    String providerId = element.getAttribute(XmlAttribute.PROVIDER_ID);
    RootBeanDefinition cacheProviderFacadeBeanDefinition = (RootBeanDefinition) registry
        .getBeanDefinition(providerId);

    String ref = element.getAttribute(XmlAttribute.REF);
    if (!registry.containsBeanDefinition(ref)) {
      throw new IllegalStateException("Unable to find bean definition with id "
          + StringUtils.quote(ref));
    }

    Map cachingModels = null;
    Map flushingModels = null;
    
    if (EhCacheFacade.class.isAssignableFrom(cacheProviderFacadeBeanDefinition
        .getBeanClass())) {
      cachingModels = ehCacheModelParser.parseCachingModels(element);
      flushingModels = ehCacheModelParser.parseFlushingModels(element);
    }

    List cachingListeners = null;
    List cachingListenersElements = DomUtils.getChildElementsByTagName(element,
        XmlElement.CACHING_LISTENERS, true);
    if (cachingListenersElements != null && !cachingListenersElements.isEmpty()) {
      Element cachingListenersElement = (Element) cachingListenersElements
          .get(0);
      cachingListeners = cachingListenerParser.parseCachingListeners(
          cachingListenersElement, registry);
    }

    RootBeanDefinition cacheProxyFactoryBeanDefinition = new RootBeanDefinition(
        CacheProxyFactoryBean.class);
    cacheProxyFactoryBeanDefinition
        .setPropertyValues(new MutablePropertyValues());
    cacheProxyFactoryBeanDefinition.getPropertyValues().addPropertyValue(
        PropertyName.CACHE_PROVIDER_FACADE,
        new RuntimeBeanReference(providerId));
    if (cachingListeners != null && !cachingListeners.isEmpty()) {
      cacheProxyFactoryBeanDefinition.getPropertyValues().addPropertyValue(
          PropertyName.CACHING_LISTENERS, cachingListeners);
    }
    cacheProxyFactoryBeanDefinition.getPropertyValues().addPropertyValue(
        PropertyName.CACHING_MODELS, cachingModels);
    cacheProxyFactoryBeanDefinition.getPropertyValues().addPropertyValue(
        PropertyName.FLUSHING_MODELS, flushingModels);
    cacheProxyFactoryBeanDefinition.getPropertyValues().addPropertyValue(
        PropertyName.TARGET, new RuntimeBeanReference(ref));

    String id = element.getAttribute(XmlAttribute.ID);

    registry.registerBeanDefinition(id, cacheProxyFactoryBeanDefinition);
  }
}
