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

import org.w3c.dom.Element;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.AbstractBeanDefinition;

/**
 * <p>
 * Unit Tests for <code>{@link MethodMapInterceptorsParser}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class MethodMapInterceptorsParserTests extends
    AbstractCacheSetupStrategyParserImplTestCase {

  protected class InterceptorsConfigStruct {
    String cachingInterceptorId = "";

    String flushingInterceptorId = "";

    Element toXml() {
      Element element = new DomElementStub("methodMapInterceptors");
      element.setAttribute("cachingInterceptorId", cachingInterceptorId);
      element.setAttribute("flushingInterceptorId", flushingInterceptorId);
      return element;
    }
  }

  private InterceptorsConfigStruct config;

  private MethodMapInterceptorsParser parser;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case
   */
  public MethodMapInterceptorsParserTests(String name) {
    super(name);
  }

  public void testParseCacheSetupStrategy() {
    parser.parseCacheSetupStrategy(config.toXml(), parserContext,
        propertySource);

    // verify the properties of the caching interceptor.
    AbstractBeanDefinition cachingInterceptor = (AbstractBeanDefinition) registry
        .getBeanDefinition(config.cachingInterceptorId);
    MutablePropertyValues cachingProperties = cachingInterceptor
        .getPropertyValues();

    assertSame(propertySource.cacheKeyGenerator, cachingProperties
        .getPropertyValue("cacheKeyGenerator").getValue());
    assertSame(propertySource.cacheProviderFacadeReference, cachingProperties
        .getPropertyValue("cacheProviderFacade").getValue());
    assertSame(propertySource.cachingListeners, cachingProperties
        .getPropertyValue("cachingListeners").getValue());
    assertSame(propertySource.cachingModelMap, cachingProperties
        .getPropertyValue("cachingModels").getValue());

    // verify the properties of the flushing interceptor.
    AbstractBeanDefinition flushingInterceptor = (AbstractBeanDefinition) registry
        .getBeanDefinition(config.flushingInterceptorId);
    MutablePropertyValues flushingProperties = flushingInterceptor
        .getPropertyValues();

    assertSame(propertySource.cacheProviderFacadeReference, flushingProperties
        .getPropertyValue("cacheProviderFacade").getValue());
    assertSame(propertySource.flushingModelMap, flushingProperties
        .getPropertyValue("flushingModels").getValue());
  }

  protected void afterSetUp() throws Exception {
    parser = new MethodMapInterceptorsParser();

    config = new InterceptorsConfigStruct();
    config.cachingInterceptorId = "cachingInterceptor";
    config.flushingInterceptorId = "flushingInterceptor";
  }

}
