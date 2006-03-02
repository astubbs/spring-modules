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

import org.w3c.dom.Element;

import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.util.StringUtils;

import org.springmodules.cache.interceptor.proxy.CacheProxyFactoryBean;

/**
 * <p>
 * Template that handles the parsing of the XML tag "beanRef". Creates,
 * configures and registers and implementation of
 * <code>{@link CacheProxyFactoryBean}</code> in the provided registry of bean
 * definitions.
 * </p>
 * 
 * @author Alex Ruiz
 */
public abstract class AbstractCacheProxyFactoryBeanParser extends
    AbstractCacheSetupStrategyParser {

  /**
   * Creates and registers a <code>{@link CacheProxyFactoryBean}</code> by
   * parsing the given XML element.
   * 
   * @param element
   *          the XML element to parse
   * @param registry
   *          the registry of bean definitions
   * @param propertySource
   *          contains common properties for the different cache setup
   *          strategies
   * @throws IllegalStateException
   *           if the bean to be referenced by the
   *           <code>CacheProxyFactoryBean</code> does not exist in the
   *           registry
   *           
   * @see AbstractCacheSetupStrategyParser#parseCacheSetupStrategy(Element,
   *      BeanDefinitionRegistry, CacheSetupStrategyPropertySource)
   */
  protected final void parseCacheSetupStrategy(Element element,
      BeanDefinitionRegistry registry,
      CacheSetupStrategyPropertySource propertySource)
      throws IllegalStateException {

    String beanRefId = element.getAttribute("refId");
    if (!registry.containsBeanDefinition(beanRefId)) {
      throw new IllegalStateException("Unable to find bean definition with id "
          + StringUtils.quote(beanRefId));
    }

    RootBeanDefinition cacheProxyFactoryBean = new RootBeanDefinition(
        CacheProxyFactoryBean.class, propertySource.getAllProperties());

    cacheProxyFactoryBean.getPropertyValues().addPropertyValue("target",
        new RuntimeBeanReference(beanRefId));

    String id = element.getAttribute("id");
    registry.registerBeanDefinition(id, cacheProxyFactoryBean);
  }
}
