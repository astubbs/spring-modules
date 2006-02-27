/* 
 * Created on Feb 3, 2006
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
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceEditor;
import org.springframework.util.StringUtils;

/**
 * <p>
 * TODO Document class.
 * </p>
 * 
 * @author Alex Ruiz
 */
public abstract class AbstractCacheManagerAndProviderFacadeParser extends
    AbstractCacheProviderFacadeParser {

  /**
   * Constructor.
   */
  public AbstractCacheManagerAndProviderFacadeParser() {
    super();
  }

  /**
   * @see AbstractCacheProviderFacadeParser#doParse(String, Element,
   *      BeanDefinitionRegistry)
   */
  protected void doParse(String cacheProviderFacadeId, Element element,
      BeanDefinitionRegistry registry) {
    String id = cacheProviderFacadeId + ".cacheManager";
    Class clazz = getCacheManagerClass();
    RootBeanDefinition cacheManager = new RootBeanDefinition(clazz);
    MutablePropertyValues cacheManagerProperties = new MutablePropertyValues();
    cacheManager.setPropertyValues(cacheManagerProperties);

    PropertyValue configLocation = parseConfigLocationProperty(element);
    cacheManagerProperties.addPropertyValue(configLocation);
    registry.registerBeanDefinition(id, cacheManager);

    BeanDefinition cacheProviderFacade = registry
        .getBeanDefinition(cacheProviderFacadeId);
    cacheProviderFacade.getPropertyValues().addPropertyValue("cacheManager",
        new RuntimeBeanReference(id));
  }

  /**
   * @return the class of the cache manager to create
   */
  protected abstract Class getCacheManagerClass();

  /**
   * @param element
   *          the XML element containing the properties for the cache manager
   *          factory bean
   * @returns a map containing the properties for the cache manager factory bean
   */
  private PropertyValue parseConfigLocationProperty(Element element) {
    Resource resource = null;

    String configLocation = element.getAttribute("configLocation");
    if (StringUtils.hasText(configLocation)) {
      ResourceEditor resourceEditor = new ResourceEditor();
      resourceEditor.setAsText(configLocation);
      resource = (Resource) resourceEditor.getValue();
    }

    return new PropertyValue("configLocation", resource);
  }

}
