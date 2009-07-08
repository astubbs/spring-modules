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

import org.easymock.classextension.MockClassControl;
import org.w3c.dom.Element;

import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;

import org.springmodules.cache.provider.CacheProviderFacade;
import org.springmodules.cache.serializable.SerializableFactory;
import org.springmodules.cache.serializable.XStreamSerializableFactory;

/**
 * <p>
 * Unit Tests for <code>{@link AbstractCacheProviderFacadeParser}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class CacheProviderFacadeParserTests extends
    AbstractSchemaBasedConfigurationTestCase {

  protected class ConfigElementBuilder implements XmlElementBuilder {

    boolean failQuietlyEnabled = false;

    String id = "";

    String serializableFactory = "";

    public Element toXml() {
      Element element = new DomElementStub("config");
      element.setAttribute("id", id);
      String failQuietlyProperty = failQuietlyEnabled ? "true" : "false";
      element.setAttribute("failQuietly", failQuietlyProperty);
      element.setAttribute("serializableFactory", serializableFactory);
      return element;
    }
  }

  private Class cacheProviderFacadeClass;

  private ConfigElementBuilder configElementBuilder;

  private AbstractCacheProviderFacadeParser parser;

  private MockClassControl parserControl;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case
   */
  public CacheProviderFacadeParserTests(String name) {
    super(name);
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractCacheProviderFacadeParser#parse(Element, org.springframework.beans.factory.xml.ParserContext)}</code>
   * creates sets <code>null</code> as the value of the property
   * "serializableFactory" of the cache provider facade if the value of the XML
   * attribute "serializableFactory" is an empty String.
   */
  public void testParseWithEmptySerializableFactory() {
    configElementBuilder.serializableFactory = "";
    Element element = configElementBuilder.toXml();

    expectGetCacheProviderFacadeClass();
    parser.doParse(configElementBuilder.id, element, registry);
    parserControl.replay();

    parser.parse(element, parserContext);
    ConfigAssert.assertBeanDefinitionWrapsClass(getCacheProviderFacade(),
        cacheProviderFacadeClass);
    assertSerializableFactoryPropertyIsNull();
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractCacheProviderFacadeParser#parse(Element, org.springframework.beans.factory.xml.ParserContext)}</code>
   * creates sets <code>false</code> as the value of the property
   * "failQuietlyEnabled" of the cache provider facade if the value of the XML
   * attribute "failQuietly" is equal to "false".
   */
  public void testParseWithFailQuietlyEqualToFalse() {
    configElementBuilder.failQuietlyEnabled = false;
    Element element = configElementBuilder.toXml();

    expectGetCacheProviderFacadeClass();
    parser.doParse(configElementBuilder.id, element, registry);
    parserControl.replay();

    parser.parse(element, parserContext);
    ConfigAssert.assertBeanDefinitionWrapsClass(getCacheProviderFacade(),
        cacheProviderFacadeClass);
    assertFailQuietlyEnabledIsFalse();
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractCacheProviderFacadeParser#parse(Element, org.springframework.beans.factory.xml.ParserContext)}</code>
   * creates sets <code>true</code> as the value of the property
   * "failQuietlyEnabled" of the cache provider facade if the value of the XML
   * attribute "failQuietly" is equal to "true".
   */
  public void testParseWithFailQuietlyEqualToTrue() {
    configElementBuilder.failQuietlyEnabled = true;
    Element element = configElementBuilder.toXml();

    expectGetCacheProviderFacadeClass();
    parser.doParse(configElementBuilder.id, element, registry);
    parserControl.replay();

    parser.parse(element, parserContext);
    ConfigAssert.assertBeanDefinitionWrapsClass(getCacheProviderFacade(),
        cacheProviderFacadeClass);
    assertFailQuietlyEnabledIsTrue();
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractCacheProviderFacadeParser#parse(Element, org.springframework.beans.factory.xml.ParserContext)}</code>
   * creates sets <code>null</code> as the value of the property
   * "serializableFactory" of the cache provider facade if the value of the XML
   * attribute "serializableFactory" is equal to "none".
   */
  public void testParseWithSerializableFactoryEqualToNone() {
    configElementBuilder.serializableFactory = "none";
    Element element = configElementBuilder.toXml();

    expectGetCacheProviderFacadeClass();
    parser.doParse(configElementBuilder.id, element, registry);
    parserControl.replay();

    parser.parse(element, parserContext);
    ConfigAssert.assertBeanDefinitionWrapsClass(getCacheProviderFacade(),
        cacheProviderFacadeClass);
    assertSerializableFactoryPropertyIsNull();
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractCacheProviderFacadeParser#parse(Element, org.springframework.beans.factory.xml.ParserContext)}</code>
   * creates sets a new instance of
   * <code>{@link XStreamSerializableFactory}</code> as the value of the
   * property "serializableFactory" of the cache provider facade if the value of
   * the XML attribute "serializableFactory" is equal to "XSTREAM".
   */
  public void testParseWithSerializableFactoryEqualToXstream() {
    configElementBuilder.serializableFactory = "XSTREAM";
    Element element = configElementBuilder.toXml();

    expectGetCacheProviderFacadeClass();
    parser.doParse(configElementBuilder.id, element, registry);
    parserControl.replay();

    parser.parse(element, parserContext);
    ConfigAssert.assertBeanDefinitionWrapsClass(getCacheProviderFacade(),
        cacheProviderFacadeClass);
    assertSerializableFactoryPropertyIsCorrect(new XStreamSerializableFactory());
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractCacheProviderFacadeParser#parse(Element, org.springframework.beans.factory.xml.ParserContext)}</code>
   * throws a <code>{@link IllegalStateException}</code> if the XML attribute
   * "serializableFactory" contains an invalid value.
   */
  public void testParseWithSerializableFactoryHavingInvalidValue() {
    configElementBuilder.serializableFactory = "INVALID";
    Element element = configElementBuilder.toXml();

    expectGetCacheProviderFacadeClass();
    parserControl.replay();

    try {
      parser.parse(element, parserContext);
      fail();
    } catch (IllegalStateException exception) {
      // expecting this exception.
    }
  }

  protected void onSetUp() throws Exception {
    cacheProviderFacadeClass = CacheProviderFacade.class;
    configElementBuilder = new ConfigElementBuilder();
    configElementBuilder.id = "cacheProviderFacade";

    setUpParser();
  }

  protected void tearDown() {
    parserControl.verify();
  }

  private void assertFailQuietlyEnabledIsCorrect(Boolean expected) {
    PropertyValue expectedPropertyValue = new PropertyValue(
        "failQuietlyEnabled", expected);
    ConfigAssert.assertBeanDefinitionHasProperty(getCacheProviderFacade(),
        expectedPropertyValue);
  }

  private void assertFailQuietlyEnabledIsFalse() {
    assertFailQuietlyEnabledIsCorrect(Boolean.FALSE);
  }

  private void assertFailQuietlyEnabledIsTrue() {
    assertFailQuietlyEnabledIsCorrect(Boolean.TRUE);
  }

  private void assertSerializableFactoryPropertyIsCorrect(
      SerializableFactory expected) {
    PropertyValue expectedPropertyValue = new PropertyValue(
        "serializableFactory", expected);
    ConfigAssert.assertBeanDefinitionHasProperty(getCacheProviderFacade(),
        expectedPropertyValue);
  }

  private void assertSerializableFactoryPropertyIsNull() {
    assertSerializableFactoryPropertyIsCorrect(null);
  }

  private void expectGetCacheProviderFacadeClass() {
    parser.getCacheProviderFacadeClass();
    parserControl.setReturnValue(cacheProviderFacadeClass);
  }

  private RootBeanDefinition getCacheProviderFacade() {
    RootBeanDefinition cacheProviderFacade = (RootBeanDefinition) registry
        .getBeanDefinition(configElementBuilder.id);
    return cacheProviderFacade;
  }

  private void setUpParser() throws Exception {
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
    parser = (AbstractCacheProviderFacadeParser) parserControl.getMock();
  }

}
