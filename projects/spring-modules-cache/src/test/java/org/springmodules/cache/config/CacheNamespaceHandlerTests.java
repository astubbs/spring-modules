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
import org.easymock.MockControl;
import org.easymock.classextension.MockClassControl;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.springmodules.AssertExt;
import org.w3c.dom.Element;

import java.lang.reflect.Method;

/**
 * <p>
 * Unit Tests for <code>{@link AbstractCacheNamespaceHandler}</code>.
 * </p>
 *
 * @author Omar Irbouh
 * @author Alex Ruiz
 */
public class CacheNamespaceHandlerTests extends TestCase {

  private AbstractCacheNamespaceHandler handler;

  private MockClassControl handlerControl;

  /**
   * Constructor.
   *
   * @param name
   *          the name of the test case.
   */
  public CacheNamespaceHandlerTests(String name) {
    super(name);
  }

  public void testInit() throws Exception {
    CacheModelParser modelParser = createCacheModelParser();
    BeanDefinitionParser facadeParser = createCacheProviderFacadeParser();

    handler.getCacheModelParser();
    handlerControl.setReturnValue(modelParser);

    handler.getCacheProviderFacadeParser();
    handlerControl.setReturnValue(facadeParser);

    handlerControl.replay();

    handler.init();

    assertSame(facadeParser, findParserForElement("config"));

    assertCacheSetupStrategyParserIsCorrect(CommonsAttributesParser.class,
        "commons-attributes", modelParser);

    assertCacheSetupStrategyParserIsCorrect(MethodMapInterceptorsParser.class,
        "methodMapInterceptors", modelParser);

    assertCacheSetupStrategyParserIsCorrect(CacheProxyFactoryBeanParser.class,
        "proxy", modelParser);

    handlerControl.verify();
  }

  protected void setUp() throws Exception {
    Class target = AbstractCacheNamespaceHandler.class;

    Method getCacheModelParserMethod = target.getDeclaredMethod(
        "getCacheModelParser", new Class[0]);

    Method getCacheProviderFacadeParserMethod = target.getDeclaredMethod(
        "getCacheProviderFacadeParser", new Class[0]);

    Method[] methodsToMock = { getCacheModelParserMethod,
        getCacheProviderFacadeParserMethod };

    handlerControl = MockClassControl.createControl(target, new Class[0],
				new Object[0], methodsToMock);

    handler = (AbstractCacheNamespaceHandler) handlerControl.getMock();
  }

  private void assertCacheSetupStrategyParserIsCorrect(Class expectedClass,
      String elementName, CacheModelParser modelParser) throws Exception {

    BeanDefinitionParser parser = findParserForElement(elementName);
    AssertExt.assertInstanceOf(expectedClass, parser);

    CacheModelParser actual = ((AbstractCacheSetupStrategyParser) parser)
        .getCacheModelParser();
    assertSame(modelParser, actual);
  }

  private CacheModelParser createCacheModelParser() {
    MockControl control = MockControl.createControl(CacheModelParser.class);
    return (CacheModelParser) control.getMock();
  }

  private BeanDefinitionParser createCacheProviderFacadeParser() {
    MockControl control = MockControl.createControl(BeanDefinitionParser.class);
    return (BeanDefinitionParser) control.getMock();
  }

  private BeanDefinitionParser findParserForElement(String elementName) throws Exception {
		Method findParserForElementMethod = NamespaceHandlerSupport.class.getDeclaredMethod(
				"findParserForElement", new Class[] {Element.class});

		findParserForElementMethod.setAccessible(true);
		return (BeanDefinitionParser) findParserForElementMethod.invoke(handler,
				new Object[] {new DomElementStub(elementName)});
  }

}