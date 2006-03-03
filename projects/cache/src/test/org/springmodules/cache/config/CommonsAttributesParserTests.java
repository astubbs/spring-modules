/* 
 * Created on Mar 3, 2006
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

import junit.framework.TestCase;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.metadata.commons.CommonsAttributes;

/**
 * <p>
 * Unit Tests for <code>{@link AbstractCommonsAttributesParser}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class CommonsAttributesParserTests extends TestCase {

  private class CommonsAttributesParser extends AbstractCommonsAttributesParser {

    protected CacheModelParser getCacheModelParser() {
      return null;
    }

    protected CacheProviderFacadeDefinitionValidator getCacheProviderFacadeDefinitionValidator() {
      return null;
    }
  }

  private static final String ATTRIBUTES_BEAN_NAME = CommonsAttributes.class
      .getName();

  private CommonsAttributesParser parser;

  private BeanDefinitionRegistry registry;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case
   */
  public CommonsAttributesParserTests(String name) {
    super(name);
  }

  public void testConfigureCachingInterceptor() {
    MutablePropertyValues propertyValues = new MutablePropertyValues();
    parser.configureCachingInterceptor(propertyValues, registry);
    assertAttributesPropertyIsPresent(propertyValues);
  }

  public void testConfigureFlushingInterceptor() {
    MutablePropertyValues propertyValues = new MutablePropertyValues();
    parser.configureFlushingInterceptor(propertyValues, registry);
    assertAttributesPropertyIsPresent(propertyValues);
  }

  public void testRegisterCustomBeans() {
    parser.registerCustomBeans(registry);
    AbstractBeanDefinition attributesDefinition = (AbstractBeanDefinition) registry
        .getBeanDefinition(ATTRIBUTES_BEAN_NAME);
    assertEquals(CommonsAttributes.class, attributesDefinition.getBeanClass());
  }

  protected void setUp() {
    parser = new CommonsAttributesParser();
    registry = new DefaultListableBeanFactory();
  }

  private void assertAttributesPropertyIsPresent(
      MutablePropertyValues propertyValues) {
    RuntimeBeanReference attributesReference = (RuntimeBeanReference) propertyValues
        .getPropertyValue("attributes").getValue();

    assertEquals(ATTRIBUTES_BEAN_NAME, attributesReference.getBeanName());
  }

}
