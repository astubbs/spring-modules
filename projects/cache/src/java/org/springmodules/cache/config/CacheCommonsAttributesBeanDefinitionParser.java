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

import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.metadata.commons.CommonsAttributes;

/**
 * <p>
 * TODO Document class.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class CacheCommonsAttributesBeanDefinitionParser extends
    AbstractAttributesBeanDefinitionParser {

  private static class BeanName {

    static final String ATTRIBUTES = CommonsAttributes.class.getName();
  }

  private static class PropertyName {

    static final String ATTRIBUTES = "attributes";
  }

  /**
   * @see AbstractAttributesBeanDefinitionParser#doParseAttributeBeanDefinitions(
   *      BeanDefinitionRegistry, AbstractBeanDefinition,
   *      AbstractBeanDefinition)
   */
  protected void doParseAttributeBeanDefinitions(
      BeanDefinitionRegistry registry,
      AbstractBeanDefinition cachingInterceptor,
      AbstractBeanDefinition flushingInterceptor) {
    RootBeanDefinition attributes = new RootBeanDefinition(
        CommonsAttributes.class);
    registry.registerBeanDefinition(BeanName.ATTRIBUTES, attributes);

    setAttributes(cachingInterceptor);
    setAttributes(flushingInterceptor);
  }

  private void setAttributes(AbstractBeanDefinition definition) {
    definition.getPropertyValues().addPropertyValue(PropertyName.ATTRIBUTES,
        new RuntimeBeanReference(BeanName.ATTRIBUTES));
  }

}
