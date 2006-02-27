/* 
 * Created on Feb 26, 2006
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

import org.springframework.beans.factory.xml.BeanDefinitionParser;

/**
 * <p>
 * Unit Tests for <code>{@link AbstractCacheNamespaceHandler}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class CacheNamespaceHandlerTests extends TestCase {

  private static class CacheNamespaceHandler extends
      AbstractCacheNamespaceHandler {

    static String annotationsParserClassName;

    static BeanDefinitionParser cacheProviderFacadeParser;

    static BeanDefinitionParser cacheProxyFactoryBeanParser;

    static BeanDefinitionParser commonsAttributeParser;

    static BeanDefinitionParser interceptorsParser;

    /**
     * Constructor.
     */
    public CacheNamespaceHandler() {
      super();
    }

    protected String getAnnotationsParserClassName() {
      return annotationsParserClassName;
    }

    protected BeanDefinitionParser getCacheProviderFacadeParser() {
      return cacheProviderFacadeParser;
    }

    protected BeanDefinitionParser getCacheProxyFactoryBeanParser() {
      return cacheProxyFactoryBeanParser;
    }

    protected BeanDefinitionParser getCommonsAttributeParser() {
      return commonsAttributeParser;
    }

    protected BeanDefinitionParser getInterceptorsParser() {
      return interceptorsParser;
    }
  }

  private CacheNamespaceHandler handler;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case.
   */
  public CacheNamespaceHandlerTests(String name) {
    super(name);
  }

  public void testAbstractCacheNamespaceHandler() {
    handler = new CacheNamespaceHandler();

    BeanDefinitionParser annotationsParser = handler
        .findParserForElement(new DomElementStub("annotations"));
    assertTrue(annotationsParser instanceof BeanDefinitionParserStub);

    assertStaticallyDefinedParsersAreCorrect();
  }

  public void testAbstractCacheNamespaceHandlerWithoutAnnotationsParserClass() {
    CacheNamespaceHandler.annotationsParserClassName = null;

    BeanDefinitionParser annotationsParser = handler
    .findParserForElement(new DomElementStub("annotations"));

    assertNull(annotationsParser);
    
    assertStaticallyDefinedParsersAreCorrect();
  }
  
  protected void setUp() {
    CacheNamespaceHandler.annotationsParserClassName = BeanDefinitionParserStub.class
        .getName();
    CacheNamespaceHandler.cacheProviderFacadeParser = new BeanDefinitionParserStub();
    CacheNamespaceHandler.cacheProxyFactoryBeanParser = new BeanDefinitionParserStub();
    CacheNamespaceHandler.commonsAttributeParser = new BeanDefinitionParserStub();
    CacheNamespaceHandler.interceptorsParser = new BeanDefinitionParserStub();
  }

  private void assertStaticallyDefinedParsersAreCorrect() {
    BeanDefinitionParser cacheProviderFacadeParser = handler
        .findParserForElement(new DomElementStub("config"));
    assertSame(CacheNamespaceHandler.cacheProviderFacadeParser,
        cacheProviderFacadeParser);

    BeanDefinitionParser cacheProxyFactoryBeanParser = handler
        .findParserForElement(new DomElementStub("beanRef"));
    assertSame(CacheNamespaceHandler.cacheProxyFactoryBeanParser,
        cacheProxyFactoryBeanParser);

    BeanDefinitionParser commonsAttributeParser = handler
        .findParserForElement(new DomElementStub("commons-attributes"));
    assertSame(CacheNamespaceHandler.commonsAttributeParser,
        commonsAttributeParser);

    BeanDefinitionParser interceptorsParser = handler
        .findParserForElement(new DomElementStub("interceptors"));
    assertSame(CacheNamespaceHandler.interceptorsParser, interceptorsParser);
  }
}
