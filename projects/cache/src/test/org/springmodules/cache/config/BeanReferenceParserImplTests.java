/* 
 * Created on Mar 14, 2006
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

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.beans.factory.xml.XmlBeanDefinitionParserHelper;
import org.springframework.util.StringUtils;

import org.springmodules.AssertExt;

import junit.framework.TestCase;

/**
 * <p>
 * Unit Tests for <code>{@link BeanReferenceParserImpl}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class BeanReferenceParserImplTests extends TestCase {

  protected class BeanElementBuilder implements XmlElementBuilder {

    String beanName = "";

    public Element toXml() {
      Element element = new DomElementStub("bean");
      if (StringUtils.hasText(beanName)) {
        element.setAttribute("id", beanName);
      }
      return element;
    }
  }

  protected class ReferenceElementBuilder implements XmlElementBuilder {

    String refId = "";

    public Element toXml() {
      Element element = new DomElementStub("reference");
      if (StringUtils.hasText(refId)) {
        element.setAttribute("refId", refId);
      }
      return element;
    }
  }

  protected XmlBeanDefinitionParserHelper helper;

  protected MockClassControl helperControl;

  private ReferenceElementBuilder elementBuilder;

  private BeanReferenceParserImpl parser;

  private ParserContext parserContext;

  private BeanDefinitionRegistry registry;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case
   */
  public BeanReferenceParserImplTests(String name) {
    super(name);
  }

  public void testParseWithAnonymousBean() {
    String beanName = null;
    Element beanElement = createBeanElementBuilder(beanName).toXml();

    Element referenceElement = elementBuilder.toXml();
    referenceElement.appendChild(beanElement);

    RootBeanDefinition beanDefinition = new RootBeanDefinition(String.class);
    BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDefinition,
        beanName);

    helper.parseBeanDefinitionElement(beanElement, true);
    helperControl.setReturnValue(holder);
    helperControl.replay();

    // method to test
    Object actual = parser.parse(referenceElement, parserContext, true);
    assertSame(holder, actual);

    // assert the bean definition was not registered.
    assertEquals(0, registry.getBeanDefinitionCount());
  }

  public void testParseWithEmptyRefIdAndWithoutInnerBean() {
    helperControl.replay();

    // method to test
    try {
      parser.parse(elementBuilder.toXml(), parserContext, true);
      fail();
    } catch (IllegalStateException exception) {
      // expecting exception.
    }
  }

  public void testParseWithNamedBeanNotToBeRegistered() {
    String beanName = "myBean";
    Element beanElement = createBeanElementBuilder(beanName).toXml();

    Element referenceElement = elementBuilder.toXml();
    referenceElement.appendChild(beanElement);

    RootBeanDefinition beanDefinition = new RootBeanDefinition(String.class);
    BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDefinition,
        beanName);

    helper.parseBeanDefinitionElement(beanElement, true);
    helperControl.setReturnValue(holder);
    helperControl.replay();

    // method to test
    Object actual = parser.parse(referenceElement, parserContext, false);
    assertSame(holder, actual);

    // assert the bean definition was not registered.
    assertEquals(0, registry.getBeanDefinitionCount());
  }

  public void testParseWithNamedInnerBeanToBeRegistered() {
    String beanName = "myBean";
    Element beanElement = createBeanElementBuilder(beanName).toXml();

    Element referenceElement = elementBuilder.toXml();
    referenceElement.appendChild(beanElement);

    RootBeanDefinition beanDefinition = new RootBeanDefinition(String.class);
    BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDefinition,
        beanName);

    helper.parseBeanDefinitionElement(beanElement, true);
    helperControl.setReturnValue(holder);
    helperControl.replay();

    // method to test
    Object actual = parser.parse(referenceElement, parserContext, true);
    assertIsRuntimeBeanReference(actual, beanName);

    // assert the bean definition was registered.
    assertSame(beanDefinition, registry.getBeanDefinition(beanName));
  }

  public void testParseWithNotEmptyRefId() {
    elementBuilder.refId = "myBean";
    helperControl.replay();

    // method to test
    Object actual = parser.parse(elementBuilder.toXml(), parserContext);
    assertIsRuntimeBeanReference(actual, elementBuilder.refId);
  }

  protected void setUp() throws Exception {
    setUpHelper();
    elementBuilder = new ReferenceElementBuilder();
    parser = new BeanReferenceParserImpl();
    parserContext = ParserContextFactory.create(helper);
    registry = parserContext.getRegistry();
  }

  protected void tearDown() {
    helperControl.verify();
  }

  private void assertIsRuntimeBeanReference(Object actual, String refId) {
    AssertExt.assertInstanceOf(RuntimeBeanReference.class, actual);

    RuntimeBeanReference actualReference = (RuntimeBeanReference) actual;
    assertEquals("<ref id>", refId, actualReference.getBeanName());
  }

  private BeanElementBuilder createBeanElementBuilder(String beanName) {
    BeanElementBuilder beanElementBuilder = new BeanElementBuilder();
    beanElementBuilder.beanName = beanName;
    return beanElementBuilder;
  }

  private void setUpHelper() throws NoSuchMethodException {
    Class targetClass = XmlBeanDefinitionParserHelper.class;

    Method parseBeanDefinitionElementMethod = targetClass.getDeclaredMethod(
        "parseBeanDefinitionElement", new Class[] { Element.class,
            boolean.class });

    Method[] methodsToMock = { parseBeanDefinitionElementMethod };

    helperControl = MockClassControl.createControl(targetClass, null, null,
        methodsToMock);

    helper = (XmlBeanDefinitionParserHelper) helperControl.getMock();
  }
}
