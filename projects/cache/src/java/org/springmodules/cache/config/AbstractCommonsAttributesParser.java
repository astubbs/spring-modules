/* 
 * Created on Feb 26, 2006
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

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.RuntimeBeanReference;
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
public abstract class AbstractCommonsAttributesParser extends
    AbstractMetadataAttributesParser {

  private static class BeanName {

    static final String ATTRIBUTES = CommonsAttributes.class.getName();
  }

  /**
   * @see AbstractMetadataAttributesParser#configureInterceptors(MutablePropertyValues,
   *      MutablePropertyValues, BeanDefinitionRegistry)
   */
  protected void configureInterceptors(
      MutablePropertyValues cachingInterceptorPropertyValues,
      MutablePropertyValues flushingInterceptorPropertyValues,
      BeanDefinitionRegistry registry) {

    RootBeanDefinition attributes = new RootBeanDefinition(
        CommonsAttributes.class);
    registry.registerBeanDefinition(BeanName.ATTRIBUTES, attributes);

    addAttributesProperty(cachingInterceptorPropertyValues);
    addAttributesProperty(flushingInterceptorPropertyValues);
  }

  private void addAttributesProperty(MutablePropertyValues propertyValues) {
    propertyValues.addPropertyValue("attributes", new RuntimeBeanReference(
        BeanName.ATTRIBUTES));
  }
}
