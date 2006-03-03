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

import java.util.ArrayList;
import java.util.HashMap;

import junit.framework.TestCase;

import org.w3c.dom.Element;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.TestBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * <p>
 * Unit Tests for <code>{@link AbstractCacheProxyFactoryBeanParser}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class CacheProxyFactoryBeanParserTests extends TestCase {

  private class CacheProxyFactoryBeanParser extends
      AbstractCacheProxyFactoryBeanParser {

    protected CacheModelParser getCacheModelParser() {
      return null;
    }

    protected CacheProviderFacadeDefinitionValidator getCacheProviderFacadeValidator() {
      return null;
    }
  }

  private class ConfigStruct {
    String id = "";

    String refId = "";

    Element toXml() {
      Element element = new DomElementStub("beanRef");
      element.setAttribute("id", id);
      element.setAttribute("refId", refId);
      return element;
    }
  }

  private ConfigStruct config;

  private CacheProxyFactoryBeanParser parser;

  private CacheSetupStrategyPropertySource propertySource;

  private BeanDefinitionRegistry registry;

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
    registry.registerBeanDefinition(config.refId, new RootBeanDefinition(
        TestBean.class));

    parser.parseCacheSetupStrategy(config.toXml(), registry, propertySource);

    BeanDefinition proxyCacheFactoryBean = registry
        .getBeanDefinition(config.id);
    MutablePropertyValues actualProperties = proxyCacheFactoryBean
        .getPropertyValues();

    // verify property "target" is correct.
    PropertyValue targetProperty = actualProperties.getPropertyValue("target");
    RuntimeBeanReference targetReference = (RuntimeBeanReference) targetProperty
        .getValue();
    assertEquals(config.refId, targetReference.getBeanName());

    // verify property "cacheProviderFacade" is correct.
    PropertyValue cacheProviderFacade = propertySource.getCacheProviderFacade();
    RuntimeBeanReference expected = (RuntimeBeanReference) cacheProviderFacade
        .getValue();
    String propertyName = cacheProviderFacade.getName();
    RuntimeBeanReference actual = (RuntimeBeanReference) actualProperties
        .getPropertyValue(propertyName).getValue();
    assertEquals("<Property '" + propertyName + "'>", expected.getBeanName(),
        actual.getBeanName());

    // verify rest of properties.
    PropertyValue cachingListeners = propertySource.getCachingListeners();
    assertEquals(cachingListeners, actualProperties
        .getPropertyValue(cachingListeners.getName()));

    PropertyValue cachingModels = propertySource.getCachingModels();
    assertEquals(cachingModels, actualProperties.getPropertyValue(cachingModels
        .getName()));

    PropertyValue flushingModels = propertySource.getFlushingModels();
    assertEquals(flushingModels, actualProperties
        .getPropertyValue(flushingModels.getName()));
  }

  public void testParseCacheSetupStrategyWithNonExistingRefBean() {
    try {
      parser.parseCacheSetupStrategy(config.toXml(), registry, null);
      fail();
    } catch (IllegalStateException exception) {
      // expecting this exception
    }
  }

  protected void setUp() {
    parser = new CacheProxyFactoryBeanParser();
    registry = new DefaultListableBeanFactory();

    config = new ConfigStruct();
    config.id = "myBean";
    config.refId = "myBeanTarget";

    propertySource = new CacheSetupStrategyPropertySource(
        new RuntimeBeanReference("cacheProvider"), new ArrayList(),
        new HashMap(), new HashMap());
  }

  private void assertEquals(PropertyValue expected, PropertyValue actual) {
    assertEquals("<Property '" + expected.getName() + "'>",
        expected.getValue(), actual.getValue());
  }

}
