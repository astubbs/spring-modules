/* 
 * Created on Jan 19, 2006
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

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.util.StringUtils;

import org.springmodules.cache.serializable.XStreamSerializableFactory;

/**
 * <p>
 * TODO Document class.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @see #parse(Element, BeanDefinitionRegistry)
 */
public abstract class AbstractCacheProviderFacadeParser implements
    BeanDefinitionParser {

  private static abstract class PropertyName {

    static final String SERIALIZABLE_FACTORY = "serializableFactory";
  }

  private static abstract class SerializableFactory {

    static final String NONE = "NONE";

    static final String XSTREAM = "XSTREAM";
  }

  public final void parse(Element element, BeanDefinitionRegistry registry)
      throws IllegalStateException {
    String id = element.getAttribute("id");

    // create the cache provider facade
    Class clazz = getCacheProviderFacadeClass();
    MutablePropertyValues propertyValues = new MutablePropertyValues();
    RootBeanDefinition cacheProviderFacade = new RootBeanDefinition(clazz,
        propertyValues);
    propertyValues.addPropertyValue(parseFailQuietlyEnabledProperty(element));
    propertyValues.addPropertyValue(parseSerializableFactoryProperty(element));
    registry.registerBeanDefinition(id, cacheProviderFacade);

    doParse(id, element, registry);
  }

  /**
   * Gives subclasses the opportunity to parse their own bean definitions.
   * 
   * @param cacheProviderFacadeId
   *          the id of the already registered <code>CacheProviderFacade</code>
   * @param element
   *          the XML element containing the values needed to parse bean
   *          definitions
   * @param registry
   *          the registry where bean definitions get registered
   */
  protected void doParse(String cacheProviderFacadeId, Element element,
      BeanDefinitionRegistry registry) {
    // no implementation.
  }

  /**
   * @return the class of the cache provider facade to register.
   */
  protected abstract Class getCacheProviderFacadeClass();

  private PropertyValue parseFailQuietlyEnabledProperty(Element element) {
    String failQuietlyAttr = element.getAttribute("failQuietly");
    Boolean value = Boolean.FALSE;

    if (StringUtils.hasText(failQuietlyAttr)) {
      value = "true".equalsIgnoreCase(failQuietlyAttr) ? Boolean.TRUE
          : Boolean.FALSE;
    }
    return new PropertyValue("failQuietlyEnabled", value);
  }

  private PropertyValue parseSerializableFactoryProperty(Element element)
      throws IllegalStateException {
    String serializableFactoryAttr = element
        .getAttribute("serializableFactory");

    if (!StringUtils.hasText(serializableFactoryAttr)
        || SerializableFactory.NONE.equalsIgnoreCase(serializableFactoryAttr)) {
      return new PropertyValue(PropertyName.SERIALIZABLE_FACTORY, null);
    }

    if (SerializableFactory.XSTREAM.equalsIgnoreCase(serializableFactoryAttr)) {
      return new PropertyValue(PropertyName.SERIALIZABLE_FACTORY,
          new XStreamSerializableFactory());
    }

    throw new IllegalStateException(StringUtils.quote(serializableFactoryAttr)
        + " is not a serializableFactory. Valid values include "
        + StringUtils.quote(SerializableFactory.NONE) + " and "
        + StringUtils.quote(SerializableFactory.XSTREAM));
  }
}
