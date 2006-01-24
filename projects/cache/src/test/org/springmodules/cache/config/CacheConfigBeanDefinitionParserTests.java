/* 
 * Created on Jan 21, 2006
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

import org.w3c.dom.Element;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

/**
 * <p>
 * Unit Test for <code>{@link CacheConfigBeanDefinitionParser}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class CacheConfigBeanDefinitionParserTests extends TestCase {

  private DomElementStub configElement;

  private CacheConfigBeanDefinitionParser parser;

  private BeanDefinitionRegistry registry;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case.
   */
  public CacheConfigBeanDefinitionParserTests(String name) {
    super(name);
  }

  /**
   * Verifies that the method
   * <code>{@link CacheConfigBeanDefinitionParser#parse(Element, BeanDefinitionRegistry)}</code>
   * throws an IllegalStateException if the XML attribute "provider" contains an
   * invalid value.
   */
  public void testParseWithProviderAttributeHavingInvalidValue() {
    assertParseThrowsExceptionIfAnyAttributeHasInvalidValue("provider",
        "MY_OWN_CACHE");
  }

  /**
   * Verifies that the method
   * <code>{@link CacheConfigBeanDefinitionParser#parse(Element, BeanDefinitionRegistry)}</code>
   * throws an IllegalStateException if the XML attribute "serializableFactory"
   * contains an invalid value.
   */
  public void testParseWithSerializableFactoryAttributeHavingInvalidValue() {
    assertParseThrowsExceptionIfAnyAttributeHasInvalidValue(
        "serializableFactory", "MY_OWN_FACTORY");
  }

  protected void setUp() {
    configElement = new DomElementStub("config");
    configElement.setAttribute("provider", "EHCACHE");
    configElement.setAttribute("id", "cacheProvider");
    configElement.setAttribute("configLocation", "");
    configElement.setAttribute("failQuietly", "true");
    configElement.setAttribute("serializableFactory", "");

    parser = new CacheConfigBeanDefinitionParser();
    registry = new DefaultListableBeanFactory();
  }

  private void assertParseThrowsExceptionIfAnyAttributeHasInvalidValue(
      String attributeName, String attributeValue) {
    configElement.setAttribute(attributeName, attributeValue);

    try {
      parser.parse(configElement, registry);
    } catch (IllegalStateException exception) {
      // we are expecting this exception
    }
  }
}
