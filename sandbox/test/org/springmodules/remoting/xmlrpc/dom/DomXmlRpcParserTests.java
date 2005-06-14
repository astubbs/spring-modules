/* 
 * Created on Jun 5, 2005
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
 * Copyright @2005 the original author or authors.
 */
package org.springmodules.remoting.xmlrpc.dom;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.easymock.classextension.MockClassControl;
import org.springmodules.remoting.xmlrpc.AbstractDomXmlRpcParserTests;
import org.springmodules.remoting.xmlrpc.XmlRpcRemoteInvocationArguments;
import org.springmodules.remoting.xmlrpc.util.StructMember;
import org.w3c.dom.Element;

/**
 * <p>
 * Unit Tests for <code>{@link AbstractDomXmlRpcParser}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/06/14 00:47:24 $
 */
public class DomXmlRpcParserTests extends AbstractDomXmlRpcParserTests {

  /**
   * Primary object that is under test.
   */
  private AbstractDomXmlRpcParser parser;

  /**
   * Controls the behavior of <code>{@link #parser}</code> (if instantiated as
   * a mock object).
   */
  private MockClassControl parserControl;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case to construct.
   */
  public DomXmlRpcParserTests(String name) {
    super(name);
  }

  /**
   * @see AbstractDomXmlRpcParserTests#onSetUp()
   */
  protected void onSetUp() throws Exception {
    this.parser = new AbstractDomXmlRpcParser() {
      // no extra implementation.
    };
  }

  /**
   * Instantiates <code>{@link #parser}</code> as a mock object.
   * 
   * @param method
   *          the method to mock.
   */
  private void setUpParserAsMockObject(Method method) {
    Method[] methods = { method };
    this.setUpParserAsMockObject(methods);
  }

  /**
   * Instantiates <code>{@link #parser}</code> as a mock object.
   * 
   * @param methods
   *          the methods to mock.
   */
  private void setUpParserAsMockObject(Method[] methods) {
    Class targetClass = AbstractDomXmlRpcParser.class;

    this.parserControl = MockClassControl.createControl(targetClass, null,
        null, methods);
    this.parser = (AbstractDomXmlRpcParser) this.parserControl.getMock();
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractDomXmlRpcParser#parseArrayElement(Element)}</code>
   * delegates the parsing of the "data" element to
   * <code>{@link AbstractDomXmlRpcParser#parseDataElement(Element)}</code>.
   */
  public void testParseArrayElement() throws Exception {
    Method methodToMock = AbstractDomXmlRpcParser.class.getDeclaredMethod(
        "parseDataElement", new Class[] { Element.class });

    this.setUpParserAsMockObject(methodToMock);

    List expected = new ArrayList();
    Element dataElement = super.createDataElement();
    Element arrayElement = super.createArrayElement(dataElement);

    // expectation: parse the "data" element.
    this.parser.parseDataElement(dataElement);
    this.parserControl.setReturnValue(expected);

    // set the state of the mock object to "replay".
    this.parserControl.replay();

    // execute the method to test.
    List actual = this.parser.parseArrayElement(arrayElement);

    assertSame("<Array>", expected, actual);

    // verify the expectations of the mock object were met.
    this.parserControl.verify();
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractDomXmlRpcParser#parseDataElement(Element)}</code>
   * delegates the parsing of the "value" elements to
   * <code>{@link AbstractDomXmlRpcParser#parseValueElement(Element)}</code>.
   */
  public void testParseDataElement() throws Exception {
    Method methodToMock = AbstractDomXmlRpcParser.class.getDeclaredMethod(
        "parseValueElement", new Class[] { Element.class });

    this.setUpParserAsMockObject(methodToMock);

    int expectedSize = 5;
    List expected = new ArrayList(expectedSize);
    Element[] valueElements = new Element[expectedSize];

    Element dataElement = super.createDataElement();

    for (int i = 0; i < expectedSize; i++) {
      Integer value = new Integer(i);
      expected.add(value);

      Element valueElement = super.createValueElement();
      valueElements[i] = valueElement;
      dataElement.appendChild(valueElement);

      // expectation: parse each "value" element.
      this.parser.parseValueElement(valueElement);
      this.parserControl.setReturnValue(value);
    }

    // set the state of the mock object to "replay".
    this.parserControl.replay();

    // execute the method to test.
    List actual = this.parser.parseDataElement(dataElement);

    assertEquals("<Array>", expected, actual);

    // verify the expectations of the mock object were met.
    this.parserControl.verify();
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractDomXmlRpcParser#parseMemberElement(Element)}</code>
   * obtains the name of the method to execute and delegates the parsing of the
   * "value" element to
   * <code>{@link AbstractDomXmlRpcParser#parseValueElement(Element)}</code>.
   */
  public void testParseMemberElement() throws Exception {
    Method methodToMock = AbstractDomXmlRpcParser.class.getDeclaredMethod(
        "parseValueElement", new Class[] { Element.class });

    this.setUpParserAsMockObject(methodToMock);

    String expectedName = "Han";
    String expectedValue = "Falcon Millenium";

    Element nameElement = super.createNameElement(expectedName);
    Element valueElement = super.createValueElement();
    Element memberElement = super
        .createMemberElement(nameElement, valueElement);

    // expectation: parse the "value" element.
    this.parser.parseValueElement(valueElement);
    this.parserControl.setReturnValue(expectedValue);

    // set the state of the mock object to "replay".
    this.parserControl.replay();

    // execute the method to test.
    StructMember member = this.parser.parseMemberElement(memberElement);

    assertEquals("<Name>", expectedName, member.name);
    assertEquals("<Value>", expectedValue, member.value);

    // verify the expectations of the mock object were met.
    this.parserControl.verify();
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractDomXmlRpcParser#parseParameterElement(Element)}</code>
   * returns the parsed value of this "value" element.
   */
  public void testParseParameterElement() throws Exception {
    Method methodToMock = AbstractDomXmlRpcParser.class.getDeclaredMethod(
        "parseValueElement", new Class[] { Element.class });

    this.setUpParserAsMockObject(methodToMock);

    String expected = "Luke";
    Element valueElement = super.createValueElement(expected);
    Element paramElement = super.createParamElement(valueElement);

    // expectation: parse the "value" element.
    this.parser.parseValueElement(valueElement);
    this.parserControl.setReturnValue(expected);

    // set the state of the mock object to "replay".
    this.parserControl.replay();

    // execute the method to test.
    Object actual = this.parser.parseParameterElement(paramElement);

    assertEquals("<Param>", expected, actual);

    // verify the expectations of the mock object were met.
    this.parserControl.verify();
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractDomXmlRpcParser#parseParametersElement(Element)}</code>
   * parses the given DOM element into a
   * <code>{@link XmlRpcRemoteInvocationArguments}</code>.
   */
  public void testParseParametersElement() throws Exception {
    Method parseParameterElementMethod = AbstractDomXmlRpcParser.class
        .getDeclaredMethod("parseParameterElement",
            new Class[] { Element.class });

    Method[] methodsToMock = { parseParameterElementMethod };

    this.setUpParserAsMockObject(methodsToMock);

    Element paramsElement = super.createParamsElement();

    Class[] expectedParameterTypes = { Map.class, List.class, String.class };
    Object[] expectedArguments = { new HashMap(), new ArrayList(), "C3PO" };

    int parameterCount = expectedArguments.length;
    for (int i = 0; i < parameterCount; i++) {
      Element paramElement = super.createParamElement();
      paramsElement.appendChild(paramElement);

      Object parameter = expectedArguments[i];

      // expectation: parse the "param" element.
      this.parser.parseParameterElement(paramElement);
      this.parserControl.setReturnValue(parameter);
    }

    // set the state of the mock object to "replay".
    this.parserControl.replay();

    // execute the method to test.
    XmlRpcRemoteInvocationArguments invocationArguments = this.parser
        .parseParametersElement(paramsElement);

    assertTrue("<Parameter types>", Arrays.equals(expectedParameterTypes,
        invocationArguments.getParameterTypes()));
    assertTrue("<Arguments>", Arrays.equals(expectedArguments,
        invocationArguments.getArguments()));

    // verify that the expectations of the mock object were met.
    this.parserControl.verify();
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractDomXmlRpcParser#parseStructElement(Element)}</code>
   * delegates the parsing of the "member" elements to
   * <code>{@link AbstractDomXmlRpcParser#parseMemberElement(Element)}</code>.
   */
  public void testParseStructElement() throws Exception {
    Method methodToMock = AbstractDomXmlRpcParser.class.getDeclaredMethod(
        "parseMemberElement", new Class[] { Element.class });

    this.setUpParserAsMockObject(methodToMock);

    int expectedSize = 3;
    Element[] memberElements = new Element[expectedSize];
    Map expected = new HashMap(expectedSize);
    Element structElement = super.createStructElement();

    for (int i = 0; i < expectedSize; i++) {
      Integer value = new Integer(i);
      expected.put(value.toString(), value);

      Element memberElement = super.createMemberElement();
      memberElements[i] = memberElement;
      structElement.appendChild(memberElement);

      // expectation: parse the "member" element.
      this.parser.parseMemberElement(memberElement);
      this.parserControl.setReturnValue(new StructMember(value.toString(),
          value));
    }

    // set the state of the mock object to "replay".
    this.parserControl.replay();

    // execute the method to test.
    Map actual = this.parser.parseStructElement(structElement);

    assertEquals("<Struct>", expected, actual);

    // verify that the expectations of the mock object were met.
    this.parserControl.verify();
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractDomXmlRpcParser#parseValueElement(Element)}</code>
   * delegates the parsing to
   * <code>{@link AbstractDomXmlRpcParser#parseArrayElement(Element)}</code>
   * if the given "value" element contains an "array" element.
   */
  public void testParseValueElementWhenChildIsArrayElement() throws Exception {
    Method methodToMock = AbstractDomXmlRpcParser.class.getDeclaredMethod(
        "parseArrayElement", new Class[] { Element.class });

    this.setUpParserAsMockObject(methodToMock);

    Element arrayElement = super.createArrayElement();
    Element valueElement = super.createValueElement(arrayElement);

    List expected = new ArrayList();

    // expectation: parse the "array" element;
    this.parser.parseArrayElement(arrayElement);
    this.parserControl.setReturnValue(expected);

    // set the state of the mock object to "replay".
    this.parserControl.replay();

    // execute the method to test.
    Object actual = this.parser.parseValueElement(valueElement);

    assertSame("<Parsed object>", expected, actual);

    // verify the expectations of the mock object were met.
    this.parserControl.verify();
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractDomXmlRpcParser#parseValueElement(Element)}</code>
   * returns the text of the "string" element as the parsed value.
   */
  public void testParseValueElementWhenChildIsStringElement() throws Exception {
    String text = "Yoda";
    Element stringElement = super.createStringElement(text);
    Element valueElement = super.createValueElement(stringElement);

    // execute the method to test.
    Object actual = this.parser.parseValueElement(valueElement);

    assertEquals("<Parsed object>", text, actual);
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractDomXmlRpcParser#parseValueElement(Element)}</code>
   * delegates the parsing to
   * <code>{@link AbstractDomXmlRpcParser#parseStructElement(Element)}</code>
   * if the given "value" element contains a "struct" element.
   */
  public void testParseValueElementWhenChildIsStructElement() throws Exception {
    Method methodToMock = AbstractDomXmlRpcParser.class.getDeclaredMethod(
        "parseStructElement", new Class[] { Element.class });

    this.setUpParserAsMockObject(methodToMock);

    Element structElement = super.createStructElement();
    Element valueElement = super.createValueElement(structElement);

    Map expected = new HashMap();

    // expectation: parse the "struct" element;
    this.parser.parseStructElement(structElement);
    this.parserControl.setReturnValue(expected);

    // set the state of the mock object to "replay".
    this.parserControl.replay();

    // execute the method to test.
    Object actual = this.parser.parseValueElement(valueElement);

    assertSame("<Parsed object>", expected, actual);

    // verify the expectations of the mock object were met.
    this.parserControl.verify();
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractDomXmlRpcParser#parseValueElement(Element)}</code>
   * returns the text of the given "value" element if it does not contain any
   * element specifying the data type.
   */
  public void testParseValueElementWhenValueTypeIsNotSpecified()
      throws Exception {
    String text = "Yoda";
    Element valueElement = super.createValueElement(text);

    // execute the method to test.
    Object actual = this.parser.parseValueElement(valueElement);

    assertEquals("<Parsed object>", text, actual);
  }
}
