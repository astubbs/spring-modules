/* 
 * Created on Feb 19, 2006
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.DomUtils;

import org.springmodules.cache.CachingModel;
import org.springmodules.cache.FlushingModel;
import org.springmodules.cache.interceptor.caching.CachingListener;

/**
 * <p>
 * TODO Document class.
 * </p>
 * 
 * @author Alex Ruiz
 */
public abstract class AbstractCacheSetupStrategyParser implements
    BeanDefinitionParser {

  private static class XmlAttribute {

    static final String TARGET = "target";
  }

  /**
   * Constructor.
   */
  public AbstractCacheSetupStrategyParser() {
    super();
  }

  /**
   * @throws IllegalStateException
   *           if the cache provider facade is <code>null</code>
   * @throws IllegalStateException
   *           if the cache provider facade is not valid
   * @throws IllegalStateException
   *           if any of the caching listeners is not an instance of
   *           <code>CachingListener</code>
   * @see BeanDefinitionParser#parse(Element, BeanDefinitionRegistry)
   * @see CacheProviderFacadeValidator#validate(AbstractBeanDefinition)
   */
  public final void parse(Element element, BeanDefinitionRegistry registry) {
    String cacheProviderFacadeId = element.getAttribute("providerId");

    BeanDefinition cacheProviderFacade = registry
        .getBeanDefinition(cacheProviderFacadeId);
    if (cacheProviderFacade == null) {
      throw new IllegalStateException(
          "The cache provider facade should not be null");
    }
    getCacheProviderFacadeValidator().validate(
        (AbstractBeanDefinition) cacheProviderFacade);

    RuntimeBeanReference cacheProviderFacadeReference = new RuntimeBeanReference(
        cacheProviderFacadeId);

    List cachingListeners = parseCachingListeners(element, registry);
    Map cachingModels = parseCachingModels(element);
    Map flushingModels = parseFlushingModels(element);

    CacheSetupStrategyPropertySource propertySource = new CacheSetupStrategyPropertySource(
        cacheProviderFacadeReference, cachingListeners, cachingModels,
        flushingModels);

    parseCacheSetupStrategy(element, registry, propertySource);
  }

  protected abstract CacheModelParser getCacheModelParser();

  protected abstract CacheProviderFacadeValidator getCacheProviderFacadeValidator();

  /**
   * Parses the strategy for setting up caching services.
   * 
   * @param element
   *          the XML element containing the configuration settings for the
   *          strategy
   * @param registry
   *          the Spring beans registry
   * @param newPropertySource
   *          contains common properties for the different cache setup
   *          strategies
   */
  protected abstract void parseCacheSetupStrategy(Element element,
      BeanDefinitionRegistry registry,
      CacheSetupStrategyPropertySource newPropertySource);

  private RuntimeBeanReference parseCachingListener(Element element,
      BeanDefinitionRegistry registry) throws IllegalStateException {

    String refId = element.getAttribute("refId");
    RootBeanDefinition listenerBeanDefinition = (RootBeanDefinition) registry
        .getBeanDefinition(refId);

    Class listenerClass = CachingListener.class;

    System.out.println("listenerBeanDefinition: "
        + listenerBeanDefinition.getBeanClass());

    if (!listenerClass.isAssignableFrom(listenerBeanDefinition.getBeanClass())) {
      throw new IllegalStateException("The caching listener with id "
          + StringUtils.quote(refId) + " should be an instance of <"
          + listenerClass.getName() + ">");
    }
    return new RuntimeBeanReference(refId);
  }

  private List parseCachingListeners(Element element,
      BeanDefinitionRegistry registry) throws IllegalStateException {

    List listenersElements = DomUtils.getChildElementsByTagName(element,
        "cachingListeners", true);

    if (CollectionUtils.isEmpty(listenersElements)) {
      return null;
    }

    Element listenersElement = (Element) listenersElements.get(0);
    List listenerElements = DomUtils.getChildElementsByTagName(
        listenersElement, "cachingListener", true);

    ManagedList listeners = new ManagedList();
    if (!CollectionUtils.isEmpty(listenerElements)) {
      int listenerCount = listenerElements.size();

      for (int i = 0; i < listenerCount; i++) {
        Element listenerElement = (Element) listenerElements.get(i);
        RuntimeBeanReference listener = parseCachingListener(listenerElement,
            registry);
        listeners.add(listener);
      }
    }

    return listeners;
  }

  private Map parseCachingModels(Element element) {
    List modelElements = DomUtils.getChildElementsByTagName(element, "caching",
        true);
    if (CollectionUtils.isEmpty(modelElements)) {
      return null;
    }

    int modelElementCount = modelElements.size();
    CacheModelParser modelParser = getCacheModelParser();

    Map models = new HashMap();
    for (int i = 0; i < modelElementCount; i++) {
      Element modelElement = (Element) modelElements.get(i);
      String key = modelElement.getAttribute(XmlAttribute.TARGET);

      CachingModel model = modelParser.parseCachingModel(modelElement);
      models.put(key, model);
    }

    return models;
  }

  private Map parseFlushingModels(Element element) {
    List modelElements = DomUtils.getChildElementsByTagName(element,
        "flushing", true);
    if (CollectionUtils.isEmpty(modelElements)) {
      return null;
    }

    int modelElementCount = modelElements.size();
    CacheModelParser modelParser = getCacheModelParser();

    Map models = new HashMap();
    for (int i = 0; i < modelElementCount; i++) {
      Element modelElement = (Element) modelElements.get(i);
      String key = modelElement.getAttribute(XmlAttribute.TARGET);

      FlushingModel model = modelParser.parseFlushingModel(modelElement);
      models.put(key, model);
    }

    return models;
  }
}
