/* 
 * Created on Mar 2, 2006
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

import org.w3c.dom.Element;

import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * <p>
 * Unit Tests for <code>{@link AbstractCacheProxyFactoryBeanParser}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class CacheProxyFactoryBeanParserTests extends
    AbstractCacheSetupStrategyParserImplTestCase {

  private class ProxyElementBuilder implements XmlElementBuilder {
    String id = "";

    String refId = "";

    public Element toXml() {
      Element element = new DomElementStub("proxy");
      element.setAttribute("id", id);
      element.setAttribute("refId", refId);
      return element;
    }
  }

  private AbstractCacheProxyFactoryBeanParser parser;

  private ProxyElementBuilder proxyElementBuilder;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case
   */
  public CacheProxyFactoryBeanParserTests(String name) {
    super(name);
  }

  public void testParseCacheSetupStrategyWithEmptyRefBeanAndWithoutBeanElement() {
    helperControl.replay();

    proxyElementBuilder.refId = "";
    try {
      parser.parseCacheSetupStrategy(proxyElementBuilder.toXml(),
          parserContext, null);
      fail();
    } catch (IllegalStateException exception) {
      // expecting this exception
    }

    helperControl.verify();
  }

  public void testParseCacheSetupStrategyWithInnerBeanDefinition() {
    proxyElementBuilder.refId = "";

    Class beanClass = String.class;
    Element beanElement = new DomElementStub("bean");
    beanElement.setAttribute("class", beanClass.getName());

    Element proxyElement = proxyElementBuilder.toXml();
    proxyElement.appendChild(beanElement);

    RootBeanDefinition beanDefinition = new RootBeanDefinition(beanClass);
    BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDefinition, null);
    helper.parseBeanDefinitionElement(beanElement, true);
    helperControl.setReturnValue(holder);

    helperControl.replay();

    // method to test.
    parser.parseCacheSetupStrategy(proxyElement, parserContext, propertySource);

    BeanDefinition proxyDefinition = registry
        .getBeanDefinition(proxyElementBuilder.id);

    // verify property "target" is correct.
    PropertyValue target = proxyDefinition.getPropertyValues()
        .getPropertyValue("target");
    assertEquals(holder, target.getValue());

    assertCacheProxyFactoryBeanDefinitionIsCorrect(proxyDefinition);

    helperControl.verify();
  }

  public void testParseCacheSetupStrategyWithRefIdPointingToExistingBean() {
    // register bean to reference to.
    registry.registerBeanDefinition(proxyElementBuilder.refId,
        new RootBeanDefinition(String.class));

    helperControl.replay();

    // method to test.
    parser.parseCacheSetupStrategy(proxyElementBuilder.toXml(), parserContext,
        propertySource);

    BeanDefinition proxyDefinition = registry
        .getBeanDefinition(proxyElementBuilder.id);

    // verify property "target" is correct.
    PropertyValue expected = new PropertyValue("target",
        new RuntimeBeanReference(proxyElementBuilder.refId));
    ConfigAssert.assertBeanDefinitionHasProperty(proxyDefinition, expected);

    assertCacheProxyFactoryBeanDefinitionIsCorrect(proxyDefinition);

    helperControl.verify();
  }

  protected void afterSetUp() throws Exception {
    Class targetClass = AbstractCacheProxyFactoryBeanParser.class;
    parser = (AbstractCacheProxyFactoryBeanParser) createMockParser(targetClass);

    proxyElementBuilder = new ProxyElementBuilder();
    proxyElementBuilder.id = "myBean";
    proxyElementBuilder.refId = "myBeanTarget";
  }

  private void assertCacheProxyFactoryBeanDefinitionIsCorrect(
      BeanDefinition cacheProxyFactoryBeanDefinition) {

    // verify property "cacheProviderFacade" is correct.
    PropertyValue expected = new PropertyValue("cacheProviderFacade",
        new RuntimeBeanReference("cacheProvider"));
    ConfigAssert.assertBeanDefinitionHasProperty(
        cacheProxyFactoryBeanDefinition, expected);

    // verify rest of properties.
    expected = propertySource.getCachingListenersProperty();
    ConfigAssert.assertBeanDefinitionHasProperty(
        cacheProxyFactoryBeanDefinition, expected);

    expected = propertySource.getCachingModelsProperty();
    ConfigAssert.assertBeanDefinitionHasProperty(
        cacheProxyFactoryBeanDefinition, expected);

    expected = propertySource.getFlushingModelsProperty();
    ConfigAssert.assertBeanDefinitionHasProperty(
        cacheProxyFactoryBeanDefinition, expected);
  }

}
