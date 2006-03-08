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

import java.lang.reflect.Method;

import junit.framework.TestCase;

import org.easymock.classextension.MockClassControl;
import org.w3c.dom.Element;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.BeanDefinitionParser;

import org.springmodules.cache.FatalCacheException;
import org.springmodules.cache.mock.MockCachingModel;

/**
 * <p>
 * Unit Tests for <code>{@link AbstractCacheNamespaceHandler}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class CacheNamespaceHandlerTests extends TestCase {

  private class CacheNamespaceHandler extends AbstractCacheNamespaceHandler {

    protected String getAnnotationsParserClassName() {
      return annotationsParserClassName;
    }

    protected AbstractCacheProviderFacadeParser getCacheProviderFacadeParser() {
      return cacheProviderFacadeParser;
    }

    protected AbstractCacheProxyFactoryBeanParser getCacheProxyFactoryBeanParser() {
      return cacheProxyFactoryBeanParser;
    }

    protected AbstractCommonsAttributesParser getCommonsAttributesParser() {
      return commonsAttributesParser;
    }

    protected AbstractInterceptorsParser getInterceptorsParser() {
      return interceptorsParser;
    }
  }

  protected String annotationsParserClassName;

  protected AbstractCacheProviderFacadeParser cacheProviderFacadeParser;

  protected AbstractCacheProxyFactoryBeanParser cacheProxyFactoryBeanParser;

  protected AbstractCommonsAttributesParser commonsAttributesParser;

  protected AbstractInterceptorsParser interceptorsParser;

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

  public void testConstructorWithAnnotationsParserClassNameEqualToNull() {
    annotationsParserClassName = null;
    executeConstructorWithInvalidAnnotationsParserClassName();
  }

  public void testConstructorWithAnnotationsParserClassNameNotBelongingToClassHierarchy() {
    annotationsParserClassName = MockCachingModel.class.getName();
    executeConstructorWithInvalidAnnotationsParserClassName();
  }

  public void testConstructorWithEmptyAnnotionsParserClassName() {
    annotationsParserClassName = "";
    executeConstructorWithInvalidAnnotationsParserClassName();
  }

  public void testContructor() throws Exception {
    Class annotationsParserClass = BeanDefinitionParserStub.class;
    annotationsParserClassName = annotationsParserClass.getName();

    cacheProviderFacadeParser = createCacheProviderFacadeParser();

    Class parserClass = AbstractCacheProxyFactoryBeanParser.class;
    cacheProxyFactoryBeanParser = (AbstractCacheProxyFactoryBeanParser) createCacheSetupStrategyParser(parserClass);

    parserClass = AbstractCommonsAttributesParser.class;
    commonsAttributesParser = (AbstractCommonsAttributesParser) createCacheSetupStrategyParser(parserClass);

    parserClass = AbstractInterceptorsParser.class;
    interceptorsParser = (AbstractInterceptorsParser) createCacheSetupStrategyParser(parserClass);

    handler = new CacheNamespaceHandler();

    BeanDefinitionParser annotationsParser = findParserForElement("annotations");
    assertEquals(annotationsParserClass, annotationsParser.getClass());

    assertIsCorrectParser(cacheProviderFacadeParser, "config");
    assertIsCorrectParser(cacheProxyFactoryBeanParser, "beanRef");
    assertIsCorrectParser(commonsAttributesParser, "commons-attributes");
    assertIsCorrectParser(interceptorsParser, "interceptors");
  }

  private void assertIsCorrectParser(Object parser, String elementName) {
    assertSame(parser, findParserForElement(elementName));
  }

  private AbstractCacheProviderFacadeParser createCacheProviderFacadeParser()
      throws Exception {
    Class target = AbstractCacheProviderFacadeParser.class;

    Method getCacheProviderFacadeClassMethod = target.getDeclaredMethod(
        "getCacheProviderFacadeClass", new Class[0]);

    Method[] methodsToMock = { getCacheProviderFacadeClassMethod };

    MockClassControl control = MockClassControl.createControl(target, null,
        null, methodsToMock);

    return (AbstractCacheProviderFacadeParser) control.getMock();
  }

  private Object createCacheSetupStrategyParser(Class parserClass)
      throws Exception {

    Class target = AbstractCacheSetupStrategyParser.class;

    Method getCacheModelParserMethod = target.getDeclaredMethod(
        "getCacheModelParser", new Class[0]);

    Method getCacheProviderFacadeDefinitionValidatorMethod = target
        .getDeclaredMethod("getCacheProviderFacadeDefinitionValidator",
            new Class[0]);

    Method parseCacheSetupStrategyMethod = target.getDeclaredMethod(
        "parseCacheSetupStrategy", new Class[] { Element.class,
            BeanDefinitionRegistry.class,
            CacheSetupStrategyPropertySource.class });

    Method[] methodsToMock = new Method[] { getCacheModelParserMethod,
        getCacheProviderFacadeDefinitionValidatorMethod,
        parseCacheSetupStrategyMethod };

    MockClassControl control = MockClassControl.createControl(parserClass,
        null, null, methodsToMock);

    return control.getMock();
  }

  private void executeConstructorWithInvalidAnnotationsParserClassName() {
    try {
      handler = new CacheNamespaceHandler();
      fail();
    } catch (FatalCacheException exception) {
      // expecting this exception.
    }
  }

  private BeanDefinitionParser findParserForElement(String elementName) {
    return handler.findParserForElement(new DomElementStub(elementName));
  }
}
