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
 * TODO Document class.
 * </p>
 * 
 * @author Alex Ruiz
 */
public abstract class AbstractCacheProxyFactoryBeanParser extends
    AbstractCacheSetupStrategyParser {

  /**
   * @see AbstractCacheSetupStrategyParser#parseCacheSetupStrategy(Element,
   *      BeanDefinitionRegistry, CacheSetupStrategyPropertySource)
   */
  protected void parseCacheSetupStrategy(Element element,
      BeanDefinitionRegistry registry,
      CacheSetupStrategyPropertySource propertySource) {

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
