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

import org.easymock.MockControl;
import org.easymock.classextension.MockClassControl;

import org.springframework.beans.factory.xml.BeanDefinitionParser;

import org.springmodules.AssertExt;

/**
 * <p>
 * Unit Tests for <code>{@link AbstractCacheNamespaceHandler}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class CacheNamespaceHandlerTests extends TestCase {

  private static class Handler extends AbstractCacheNamespaceHandler {

    static CacheModelParser cacheModelParser;

    static AbstractCacheProviderFacadeParser cacheProviderFacadeParser;

    /**
     * @see AbstractCacheNamespaceHandler#getCacheModelParser()
     */
    protected CacheModelParser getCacheModelParser() {
      return cacheModelParser;
    }

    /**
     * @see AbstractCacheNamespaceHandler#getCacheProviderFacadeParser()
     */
    protected AbstractCacheProviderFacadeParser getCacheProviderFacadeParser() {
      return cacheProviderFacadeParser;
    }
  }

  private Handler handler;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case.
   */
  public CacheNamespaceHandlerTests(String name) {
    super(name);
  }

  public void testContructor() throws Exception {
    Handler.cacheModelParser = createCacheModelParser();
    Handler.cacheProviderFacadeParser = createCacheProviderFacadeParser();

    handler = new Handler();

    assertSame(Handler.cacheProviderFacadeParser,
        findParserForElement("config"));
    assertCacheSetupStrategyParserIsCorrect(CommonsAttributesParser.class,
        "commons-attributes");
    assertCacheSetupStrategyParserIsCorrect(InterceptorsParser.class,
        "interceptors");
    assertCacheSetupStrategyParserIsCorrect(CacheProxyFactoryBeanParser.class,
        "proxy");
  }

  private void assertCacheSetupStrategyParserIsCorrect(Class expectedClass,
      String elementName) {
    AbstractCacheSetupStrategyParser parser = (AbstractCacheSetupStrategyParser) findParserForElement(elementName);
    AssertExt.assertInstanceOf(expectedClass, parser);
    assertSame(parser.getCacheModelParser(), Handler.cacheModelParser);
  }

  private CacheModelParser createCacheModelParser() {
    MockControl control = MockControl.createControl(CacheModelParser.class);
    return (CacheModelParser) control.getMock();
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

  private BeanDefinitionParser findParserForElement(String elementName) {
    return handler.findParserForElement(new DomElementStub(elementName));
  }
}
