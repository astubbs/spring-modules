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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.w3c.dom.Element;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

/**
 * <p>
 * Unit Tests for <code>{@link AbstractInterceptorsParser}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class InterceptorsParserTests extends TestCase {

  private class InterceptorsParser extends AbstractInterceptorsParser {
    protected CacheModelParser getCacheModelParser() {
      return null;
    }

    protected CacheProviderFacadeDefinitionValidator getCacheProviderFacadeDefinitionValidator() {
      return null;
    }
  }

  private class InterceptorsConfigStruct {
    String cachingInterceptorId = "";

    String flushingInterceptorId = "";

    Element toXml() {
      Element element = new DomElementStub("interceptors");
      element.setAttribute("cachingInterceptorId", cachingInterceptorId);
      element.setAttribute("flushingInterceptorId", flushingInterceptorId);
      return element;
    }
  }

  private InterceptorsParser parser;

  private BeanDefinitionRegistry registry;

  private InterceptorsConfigStruct config;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case
   */
  public InterceptorsParserTests(String name) {
    super(name);
  }

  protected void setUp() {
    parser = new InterceptorsParser();
    registry = new DefaultListableBeanFactory();

    config = new InterceptorsConfigStruct();
    config.cachingInterceptorId = "cachingInterceptor";
    config.flushingInterceptorId = "flushingInterceptor";
  }

  public void testParseCacheSetupStrategy() {

    RuntimeBeanReference cacheProviderFacadeReference = new RuntimeBeanReference(
        "cacheProvider");
    List cachingListeners = new ArrayList();
    Map cachingModels = new HashMap();
    Map flushingModels = new HashMap();

    CacheSetupStrategyPropertySource propertySource = new CacheSetupStrategyPropertySource(
        cacheProviderFacadeReference, cachingListeners, cachingModels,
        flushingModels);

    parser.parseCacheSetupStrategy(config.toXml(), registry, propertySource);

    // verify the properties of the caching interceptor.
    AbstractBeanDefinition cachingInterceptor = (AbstractBeanDefinition) registry
        .getBeanDefinition(config.cachingInterceptorId);
    MutablePropertyValues cachingProperties = cachingInterceptor
        .getPropertyValues();

    assertSame(cacheProviderFacadeReference, cachingProperties
        .getPropertyValue("cacheProviderFacade").getValue());
    assertSame(cachingListeners, cachingProperties.getPropertyValue(
        "cachingListeners").getValue());
    assertSame(cachingModels, cachingProperties.getPropertyValue(
        "cachingModels").getValue());

    // verify the properties of the flushing interceptor.
    AbstractBeanDefinition flushingInterceptor = (AbstractBeanDefinition) registry
        .getBeanDefinition(config.flushingInterceptorId);
    MutablePropertyValues flushingProperties = flushingInterceptor
        .getPropertyValues();

    assertSame(cacheProviderFacadeReference, flushingProperties
        .getPropertyValue("cacheProviderFacade").getValue());
    assertSame(flushingModels, flushingProperties.getPropertyValue(
        "flushingModels").getValue());
  }

}
