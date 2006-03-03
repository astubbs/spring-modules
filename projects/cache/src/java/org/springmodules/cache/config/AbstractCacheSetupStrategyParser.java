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

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
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
 * Template that handles the parsing of setup strategy for declarative caching
 * services.
 * </p>
 * 
 * @author Alex Ruiz
 */
public abstract class AbstractCacheSetupStrategyParser implements
    BeanDefinitionParser {

  /**
   * Contains the names of the XML attributes used in this parser.
   */
  private static class XmlAttribute {

    static final String TARGET = "target";
  }

  /**
   * Parses the given XML element containing the properties and/or sub-elements
   * necessary to configure a strategy for setting up declarative caching
   * services.
   * 
   * @param element
   *          the XML element to parse
   * @param registry
   *          the registry of bean definitions
   * @throws NoSuchBeanDefinitionException
   *           if the cache provider facade is <code>null</code>
   * @throws IllegalStateException
   *           if the cache provider facade is in invalid state
   * @throws NoSuchBeanDefinitionException 
   *           if any of the caching listeners does not exist in the registry
   * @throws IllegalStateException
   *           if any of the caching listeners is not an instance of
   *           <code>CachingListener</code>
   * 
   * @see BeanDefinitionParser#parse(Element, BeanDefinitionRegistry)
   * @see CacheProviderFacadeValidator#validate(AbstractBeanDefinition)
   */
  public final void parse(Element element, BeanDefinitionRegistry registry)
      throws NoSuchBeanDefinitionException, IllegalStateException {
    String cacheProviderFacadeId = element.getAttribute("providerId");

    BeanDefinition cacheProviderFacade = registry
        .getBeanDefinition(cacheProviderFacadeId);
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

  /**
   * @return the parser for caching and flushing models
   */
  protected abstract CacheModelParser getCacheModelParser();

  /**
   * @return the validator of the properties of the
   *         <code>CacheProviderFacade</code>
   */
  protected abstract CacheProviderFacadeValidator getCacheProviderFacadeValidator();

  /**
   * Parses the given XML element to create the strategy for setting up
   * declarative caching services.
   * 
   * @param element
   *          the XML element to parse
   * @param registry
   *          the registry of bean definitions
   * @param propertySource
   *          contains common properties for the different cache setup
   *          strategies
   */
  protected abstract void parseCacheSetupStrategy(Element element,
      BeanDefinitionRegistry registry,
      CacheSetupStrategyPropertySource propertySource);

  /**
   * Parses the given XML element containing a caching listener to be added to
   * the caching setup strategy.
   * 
   * @param element
   *          the XML element to parse
   * @param registry
   *          the registry of bean definitions
   * @return a reference to a caching listener already registered in the given
   *         registry
   * @throws NoSuchBeanDefinitionException
   *           if the given id references a caching listener that does not exist
   *           in the registry
   * @throws IllegalStateException
   *           if the given id references a caching listener that is not an
   *           instance of <code>CachingListener</code>
   */
  private RuntimeBeanReference parseCachingListener(Element element,
      BeanDefinitionRegistry registry) throws NoSuchBeanDefinitionException,
      IllegalStateException {

    String refId = element.getAttribute("refId");
    RootBeanDefinition listenerBeanDefinition = (RootBeanDefinition) registry
        .getBeanDefinition(refId);

    Class listenerClass = CachingListener.class;

    if (!listenerClass.isAssignableFrom(listenerBeanDefinition.getBeanClass())) {
      throw new IllegalStateException("The caching listener with id "
          + StringUtils.quote(refId) + " should be an instance of <"
          + listenerClass.getName() + ">");
    }
    return new RuntimeBeanReference(refId);
  }

  /**
   * Parses the given XML element containing references to the caching listeners
   * to be added to the caching setup strategy.
   * 
   * @param element
   *          the XML element to parse
   * @param registry
   *          the registry of bean definitions
   * @return a list containing references to caching listeners already
   *         registered in the given register
   * @throws IllegalStateException
   *           if any of the given ids reference a caching listener that does
   *           not exist in the registry
   * @throws IllegalStateException
   *           if the given id references a caching listener that is not an
   *           instance of <code>CachingListener</code>
   */
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

  /**
   * Parses the given XML element which sub-elements containing the properties
   * of the caching models to create.
   * 
   * @param element
   *          the XML element to parse
   * @return a map containing the parsed caching models.The key of each element
   *         is the value of the XML attribute <code>target</code> (a String)
   *         and the value is the caching model (an instance of
   *         <code>CachingModel</code>)
   */
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

  /**
   * Parses the given XML element which sub-elements containing the properties
   * of the flushing models to create.
   * 
   * @param element
   *          the XML element to parse
   * @return a map containing the parsed flushing models.The key of each element
   *         is the value of the XML attribute <code>target</code> (a String)
   *         and the value is the flushing model (an instance of
   *         <code>FlushingModel</code>)
   */
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
