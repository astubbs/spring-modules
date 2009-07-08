/* 
 * Created on Mar 6, 2006
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

import junit.framework.Assert;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.config.ConstructorArgumentValues.ValueHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.util.StringUtils;

/**
 * <p>
 * Assert methods related to schema-based configuration of Spring IoC
 * containers.
 * </p>
 * 
 * @author Alex Ruiz
 */
public abstract class ConfigAssert {

  public static void assertBeanDefinitionHasConstructorArgument(
      Object expectedArgument, ConstructorArgumentValues argumentValues,
      int index, Class argumentType) {

    String message = "<Constructor argument with index [" + index + "]";

    ValueHolder argumentValue = argumentValues.getArgumentValue(index,
        argumentType);

    assertEquals(message, expectedArgument, argumentValue.getValue());
  }

  /**
   * Asserts that the given bean definition contains a property value equal to
   * the given one.
   * 
   * @param beanDefinition
   *          the given bean definition
   * @param expectedPropertyValue
   *          the expected property value
   */
  public static void assertBeanDefinitionHasProperty(
      BeanDefinition beanDefinition, PropertyValue expectedPropertyValue) {

    assertPropertyIsPresent(beanDefinition.getPropertyValues(),
        expectedPropertyValue);
  }

  /**
   * Asserts that the given bean definition wraps a bean of the given class.
   * 
   * @param beanDefinition
   *          the given bean definition
   * @param expectedClass
   *          the expected class of the wrapped bean
   */
  public static void assertBeanDefinitionWrapsClass(
      AbstractBeanDefinition beanDefinition, Class expectedClass) {
    Assert.assertEquals("<Bean definition class>", expectedClass,
        beanDefinition.getBeanClass());
  }

  /**
   * Asserts that both <code>{@link RuntimeBeanReference}</code> have the same
   * target bean name.
   * 
   * @param message
   *          message to be displayed if this assertion is <code>false</code>
   * @param expected
   *          the runtime bean reference containing the expected target bean
   *          name
   * @param actual
   *          the runtime bean reference containing the actual target bean name
   */
  public static void assertEqualBeanNames(String message,
      RuntimeBeanReference expected, RuntimeBeanReference actual) {
    Assert.assertEquals(message, expected.getBeanName(), actual.getBeanName());
  }

  /**
   * Asserts the given set of property values contains the expected property
   * value.
   * 
   * @param propertyValues
   *          the given set of property values
   * @param expectedPropertyValue
   *          the expected property value
   */
  public static void assertPropertyIsPresent(
      MutablePropertyValues propertyValues, PropertyValue expectedPropertyValue) {

    String propertyName = expectedPropertyValue.getName();
    PropertyValue actualPropertyValue = propertyValues
        .getPropertyValue(propertyName);

    Assert.assertNotNull("Property " + StringUtils.quote(propertyName)
        + " not found", actualPropertyValue);

    Object expectedValue = expectedPropertyValue.getValue();
    Object actualValue = actualPropertyValue.getValue();
    String message = "<Property " + StringUtils.quote(propertyName) + ">";

    assertEquals(message, expectedValue, actualValue);
  }

  private static void assertEquals(String message, Object expected,
      Object actual) {
    if (expected instanceof RuntimeBeanReference) {
      assertEqualBeanNames(message, (RuntimeBeanReference) expected,
          (RuntimeBeanReference) actual);

    } else {
      Assert.assertEquals(message, expected, actual);
    }

  }

}
