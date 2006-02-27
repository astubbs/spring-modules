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

import org.springmodules.cache.annotations.AnnotationCachingAttributeSource;
import org.springmodules.cache.annotations.AnnotationFlushingAttributeSource;

/**
 * <p>
 * TODO Document class.
 * </p>
 * 
 * @author Alex Ruiz
 */
public abstract class AbstractAnnotationsParser extends
    AbstractMetadataAttributesParser {

  private static class BeanName {

    static final String CACHING_ATTRIBUTE_SOURCE = AnnotationCachingAttributeSource.class
        .getName();

    static final String FLUSHING_ATTRIBUTE_SOURCE = AnnotationFlushingAttributeSource.class
        .getName();
  }

  /**
   * @see AbstractMetadataAttributesParser#configureInterceptors(MutablePropertyValues,
   *      MutablePropertyValues, BeanDefinitionRegistry)
   */
  @Override
  protected void configureInterceptors(
      MutablePropertyValues cachingInterceptorPropertyValues,
      MutablePropertyValues flushingInterceptorPropertyValues,
      BeanDefinitionRegistry registry) {

    registry.registerBeanDefinition(BeanName.CACHING_ATTRIBUTE_SOURCE,
        new RootBeanDefinition(AnnotationCachingAttributeSource.class));

    registry.registerBeanDefinition(BeanName.FLUSHING_ATTRIBUTE_SOURCE,
        new RootBeanDefinition(AnnotationFlushingAttributeSource.class));

    cachingInterceptorPropertyValues.addPropertyValue("cachingAttributeSource",
        new RuntimeBeanReference(BeanName.CACHING_ATTRIBUTE_SOURCE));

    flushingInterceptorPropertyValues.addPropertyValue(
        "flushingAttributeSource", new RuntimeBeanReference(
            BeanName.FLUSHING_ATTRIBUTE_SOURCE));
  }

}
