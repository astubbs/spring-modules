/* 
 * Created on Feb 3, 2006
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

import java.lang.reflect.Method;

import junit.framework.TestCase;

import org.easymock.classextension.MockClassControl;
import org.w3c.dom.Element;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;

import org.springmodules.cache.provider.CacheProviderFacade;
import org.springmodules.cache.serializable.XStreamSerializableFactory;

/**
 * <p>
 * Unit Tests for
 * <code>{@link AbstractCacheProviderFacadeParser}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class CacheProviderFacadeParserTests extends
    TestCase {

  private class ConfigStruct {

    boolean failQuietlyEnabled = false;

    String id = "";

    String serializableFactory = "";

    Element toXml() {
      Element element = new DomElementStub("config");
      element.setAttribute("id", id);
      String failQuietlyProperty = failQuietlyEnabled ? "true" : "false";
      element.setAttribute("failQuietly", failQuietlyProperty);
      element.setAttribute("serializableFactory", serializableFactory);
      return element;
    }
  }

  private static class PropertyName {

    static final String FAIL_QUIETLY = "failQuietlyEnabled";

    static final String SERIALIZABLE_FACTORY = "serializableFactory";
  }

  private static class SerializableFactory {

    static final String INVALID = "INVALID";

    static final String NONE = "NONE";

    static final String XSTREAM = "XSTREAM";
  }

  private Class cacheProviderFacadeClass;

  private ConfigStruct config;

  private AbstractCacheProviderFacadeParser parser;

  private MockClassControl parserControl;

  private BeanDefinitionRegistry registry;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case
   */
  public CacheProviderFacadeParserTests(String name) {
    super(name);
  }

  public void testParseWithEmptySerializableFactory() {
    config.serializableFactory = "";
    Element element = config.toXml();

    expectGetCacheProviderFacadeClass();
    expectDoParse(element);
    parserControl.replay();

    parser.parse(element, registry);
    assertCacheProviderFacadeIsCorrect();
    assertSerializableFactoryIsNull();
  }

  public void testParseWithFailQuietlyEqualToFalse() {
    config.failQuietlyEnabled = false;
    Element element = config.toXml();

    expectGetCacheProviderFacadeClass();
    expectDoParse(element);
    parserControl.replay();

    parser.parse(element, registry);
    assertCacheProviderFacadeIsCorrect();
    assertFailQuietlyEnabledIsFalse();
  }

  public void testParseWithFailQuietlyEqualToTrue() {
    config.failQuietlyEnabled = true;
    Element element = config.toXml();

    expectGetCacheProviderFacadeClass();
    expectDoParse(element);
    parserControl.replay();

    parser.parse(element, registry);
    assertCacheProviderFacadeIsCorrect();
    assertFailQuietlyEnabledIsTrue();
  }

  public void testParseWithSerializableFactoryEqualToNone() {
    config.serializableFactory = SerializableFactory.NONE;
    Element element = config.toXml();

    expectGetCacheProviderFacadeClass();
    expectDoParse(element);
    parserControl.replay();

    parser.parse(element, registry);
    assertCacheProviderFacadeIsCorrect();
    assertSerializableFactoryIsNull();
  }

  public void testParseWithSerializableFactoryEqualToXstream() {
    config.serializableFactory = SerializableFactory.XSTREAM;
    Element element = config.toXml();

    expectGetCacheProviderFacadeClass();
    expectDoParse(element);
    parserControl.replay();

    parser.parse(element, registry);
    assertCacheProviderFacadeIsCorrect();
    assertSerializableFactoryIsCorrect(new XStreamSerializableFactory());
  }

  public void testParseWithSerializableFactoryHavingInvalidValue() {
    config.serializableFactory = SerializableFactory.INVALID;
    Element element = config.toXml();

    expectGetCacheProviderFacadeClass();
    parserControl.replay();

    try {
      parser.parse(element, registry);
      fail();
    } catch (IllegalStateException exception) {
      // expecting this exception.
    }
  }

  protected void setUp() throws Exception {
    cacheProviderFacadeClass = CacheProviderFacade.class;
    config = new ConfigStruct();
    config.id = "cacheProviderFacade";

    Class targetClass = AbstractCacheProviderFacadeParser.class;

    Method doParseMethod = targetClass
        .getDeclaredMethod("doParse", new Class[] { String.class,
            Element.class, BeanDefinitionRegistry.class });

    Method getCacheProviderFacadeClassMethod = targetClass.getDeclaredMethod(
        "getCacheProviderFacadeClass", new Class[0]);

    Method[] methodsToMock = new Method[] { doParseMethod,
        getCacheProviderFacadeClassMethod };

    parserControl = MockClassControl.createControl(targetClass, null, null,
        methodsToMock);
    parser = (AbstractCacheProviderFacadeParser) parserControl
        .getMock();
    registry = new DefaultListableBeanFactory();
  }

  protected void tearDown() {
    parserControl.verify();
  }

  private void assertCacheProviderFacadeIsCorrect() {
    RootBeanDefinition cacheProviderFacade = getCacheProviderFacade();
    assertEquals("<Bean definition class>", cacheProviderFacadeClass,
        cacheProviderFacade.getBeanClass());
  }

  private void assertFailQuietlyEnabledIsCorrect(Boolean expected) {
    PropertyValue propertyValue = new PropertyValue(PropertyName.FAIL_QUIETLY,
        expected);
    assertPropertyValueIsCorrect(propertyValue);
  }

  private void assertFailQuietlyEnabledIsFalse() {
    assertFailQuietlyEnabledIsCorrect(Boolean.FALSE);
  }

  private void assertFailQuietlyEnabledIsTrue() {
    assertFailQuietlyEnabledIsCorrect(Boolean.TRUE);
  }

  private void assertPropertyValueIsCorrect(PropertyValue expected) {
    RootBeanDefinition cacheProviderFacade = getCacheProviderFacade();
    MutablePropertyValues propertyValues = cacheProviderFacade
        .getPropertyValues();
    String propertyName = expected.getName();
    PropertyValue actual = propertyValues.getPropertyValue(propertyName);
    assertNotNull("Property '" + propertyName + "' not found", actual);
    assertEquals("<Property '" + propertyName + "'>", expected.getValue(),
        actual.getValue());
  }

  private void assertSerializableFactoryIsCorrect(
      org.springmodules.cache.serializable.SerializableFactory expected) {
    PropertyValue propertyValue = new PropertyValue(
        PropertyName.SERIALIZABLE_FACTORY, expected);
    assertPropertyValueIsCorrect(propertyValue);
  }

  private void assertSerializableFactoryIsNull() {
    assertSerializableFactoryIsCorrect(null);
  }

  private void expectDoParse(Element element) {
    parser.doParse(config.id, element, registry);
  }

  private void expectGetCacheProviderFacadeClass() {
    parser.getCacheProviderFacadeClass();
    parserControl.setReturnValue(cacheProviderFacadeClass);
  }

  private RootBeanDefinition getCacheProviderFacade() {
    RootBeanDefinition cacheProviderFacade = (RootBeanDefinition) registry
        .getBeanDefinition(config.id);
    return cacheProviderFacade;
  }

}
