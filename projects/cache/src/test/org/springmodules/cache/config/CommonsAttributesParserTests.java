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

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.metadata.commons.CommonsAttributes;

/**
 * <p>
 * Unit Tests for <code>{@link CommonsAttributesParser}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class CommonsAttributesParserTests extends
    AbstractCacheSetupStrategyParserImplTestCase {

  private static final String ATTRIBUTES_BEAN_NAME = CommonsAttributes.class
      .getName();

  private CommonsAttributesParser parser;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case
   */
  public CommonsAttributesParserTests(String name) {
    super(name);
  }

  /**
   * Verifies that the method
   * <code>{@link CommonsAttributesParser}</code> adds a new property
   * with name "attributes" to the given set of property values. The
   * "attributes" properties should have a
   * <code>{@link RuntimeBeanReference}</code> to the bean declaration
   * describing an instance of <code>{@link CommonsAttributes}</code>.
   */
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
    ConfigAssert.assertBeanDefinitionWrapsClass(attributesDefinition,
        CommonsAttributes.class);
  }

  protected void afterSetUp() throws Exception {
    parser = new CommonsAttributesParser();
  }

  private void assertAttributesPropertyIsPresent(
      MutablePropertyValues propertyValues) {

    PropertyValue expected = new PropertyValue("attributes",
        new RuntimeBeanReference(ATTRIBUTES_BEAN_NAME));
    ConfigAssert.assertPropertyIsPresent(propertyValues, expected);
  }
}
