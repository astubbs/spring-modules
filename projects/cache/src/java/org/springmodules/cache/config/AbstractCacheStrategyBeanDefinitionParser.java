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

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.util.xml.DomUtils;

import org.springmodules.cache.provider.ehcache.EhCacheFacade;

/**
 * <p>
 * TODO Document class.
 * </p>
 * 
 * @author Alex Ruiz
 */
public abstract class AbstractCacheStrategyBeanDefinitionParser implements
    BeanDefinitionParser {

  protected static class CommonPropertyName {

    static final String CACHE_PROVIDER_FACADE = "cacheProviderFacade";

    static final String CACHING_LISTENERS = "cachingListeners";

    static final String CACHING_MODELS = "cachingModels";

    static final String FLUSHING_MODELS = "flushingModels";

  }

  private static class XmlAttribute {

    static final String PROVIDER_ID = "providerId";
  }

  private static class XmlElement {

    static final String CACHING_LISTENERS = "cachingListeners";
  }

  private CachingListenerParser cachingListenerParser;

  private CacheModelParser ehCacheModelParser;

  /**
   * Constructor.
   */
  public AbstractCacheStrategyBeanDefinitionParser() {
    super();
    cachingListenerParser = new CachingListenerParser();
    ehCacheModelParser = new EhCacheModelParser();
  }

  /**
   * @see BeanDefinitionParser#parse(Element, BeanDefinitionRegistry)
   */
  public final void parse(Element element, BeanDefinitionRegistry registry) {
    String providerId = element.getAttribute(XmlAttribute.PROVIDER_ID);
    RootBeanDefinition cacheProviderFacade = (RootBeanDefinition) registry
        .getBeanDefinition(providerId);

    Map cachingModels = null;
    Map flushingModels = null;

    if (EhCacheFacade.class
        .isAssignableFrom(cacheProviderFacade.getBeanClass())) {
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

    doParse(element, registry, providerId, cachingModels, flushingModels,
        cachingListeners);
  }

  protected abstract void doParse(Element element,
      BeanDefinitionRegistry registry, String providerId, Map cachingModels,
      Map flushingModels, List cachingListeners);
}
