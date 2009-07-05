/* 
 * Created on Mar 8, 2006
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
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinition;

import org.springmodules.cache.annotations.AnnotationCachingAttributeSource;
import org.springmodules.cache.annotations.AnnotationFlushingAttributeSource;

/**
 * <p>
 * Unit Tests for <code>{@link AnnotationsParser}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class AnnotationsParserTests extends
    AbstractCacheSetupStrategyParserImplTestCase {

  private AnnotationsParser parser;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case
   */
  public AnnotationsParserTests(String name) {
    super(name);
  }

  public void testConfigureCachingInterceptor() {
    MutablePropertyValues propertyValues = new MutablePropertyValues();
    parser.configureCachingInterceptor(propertyValues, registry);

    Class targetClass = AnnotationCachingAttributeSource.class;
    String beanName = targetClass.getName();
    AbstractBeanDefinition definition = (AbstractBeanDefinition) registry
        .getBeanDefinition(beanName);

    ConfigAssert.assertBeanDefinitionWrapsClass(definition, targetClass);

    PropertyValue expected = new PropertyValue("cachingAttributeSource",
        new RuntimeBeanReference(beanName));

    ConfigAssert.assertPropertyIsPresent(propertyValues, expected);
  }

  public void testConfigureFlushingInterceptor() {
    MutablePropertyValues propertyValues = new MutablePropertyValues();
    parser.configureFlushingInterceptor(propertyValues, registry);

    Class targetClass = AnnotationFlushingAttributeSource.class;
    String beanName = targetClass.getName();
    AbstractBeanDefinition definition = (AbstractBeanDefinition) registry
        .getBeanDefinition(beanName);

    ConfigAssert.assertBeanDefinitionWrapsClass(definition, targetClass);

    PropertyValue expected = new PropertyValue("flushingAttributeSource",
        new RuntimeBeanReference(beanName));

    ConfigAssert.assertPropertyIsPresent(propertyValues, expected);
  }

  @Override
  protected void afterSetUp() throws Exception {
    parser = new AnnotationsParser();
  }
}
