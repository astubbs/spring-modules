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

  private class BeanRefElementBuilder implements XmlElementBuilder {
    String id = "";

    String refId = "";

    public Element toXml() {
      Element element = new DomElementStub("beanRef");
      element.setAttribute("id", id);
      element.setAttribute("refId", refId);
      return element;
    }
  }

  private BeanRefElementBuilder beanRefElementBuilder;

  private AbstractCacheProxyFactoryBeanParser parser;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case
   */
  public CacheProxyFactoryBeanParserTests(String name) {
    super(name);
  }

  public void testParseCacheSetupStrategy() {
    // register bean to reference to.
    registry.registerBeanDefinition(beanRefElementBuilder.refId,
        new RootBeanDefinition(String.class));

    // method to test.
    parser.parseCacheSetupStrategy(beanRefElementBuilder.toXml(), registry,
        propertySource);

    BeanDefinition cacheProxyFactoryBeanDefinition = registry
        .getBeanDefinition(beanRefElementBuilder.id);

    // verify property "target" is correct.
    PropertyValue expected = new PropertyValue("target",
        new RuntimeBeanReference(beanRefElementBuilder.refId));
    ConfigAssert.assertBeanDefinitionHasProperty(
        cacheProxyFactoryBeanDefinition, expected);

    // verify property "cacheProviderFacade" is correct.
    expected = new PropertyValue("cacheProviderFacade",
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

  public void testParseCacheSetupStrategyWithNonExistingRefBean() {
    try {
      parser.parseCacheSetupStrategy(beanRefElementBuilder.toXml(), registry,
          null);
      fail();
    } catch (IllegalStateException exception) {
      // expecting this exception
    }
  }

  protected void onSetUp() throws Exception {
    Class targetClass = AbstractCacheProxyFactoryBeanParser.class;
    parser = (AbstractCacheProxyFactoryBeanParser) createMockParser(targetClass);

    beanRefElementBuilder = new BeanRefElementBuilder();
    beanRefElementBuilder.id = "myBean";
    beanRefElementBuilder.refId = "myBeanTarget";
  }

}
