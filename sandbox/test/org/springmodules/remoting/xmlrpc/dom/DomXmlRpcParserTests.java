/* 
 * Created on Jun 20, 2005
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
import java.util.Arrays;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.TestCase;

import org.easymock.MockControl;
import org.easymock.classextension.MockClassControl;
import org.springmodules.remoting.xmlrpc.support.XmlRpcArray;
import org.springmodules.remoting.xmlrpc.support.XmlRpcElement;
import org.springmodules.remoting.xmlrpc.support.XmlRpcInteger;
import org.springmodules.remoting.xmlrpc.support.XmlRpcScalarFactory;
import org.springmodules.remoting.xmlrpc.support.XmlRpcString;
import org.springmodules.remoting.xmlrpc.support.XmlRpcStruct;
import org.springmodules.remoting.xmlrpc.support.XmlRpcStruct.XmlRpcMember;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * <p>
 * Unit Tests for <code>{@link AbstractDomXmlRpcParser}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.3 $ $Date: 2005/06/21 01:03:57 $
 */
public class DomXmlRpcParserTests extends TestCase {

  /**
   * A XML document used to create XML elements.
   */
  private Document document;

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
   * Creates a new element having "array" as its tag name.
   * 
   * @return the created element.
   */
  private Element createArrayElement() {
    return this.document.createElement("array");
  }

  /**
   * Creates a new element having "array" as its tag name.
   * 
   * @param dataElement
   *          "data" element to be added as child of the created element.
   * @return the created element.
   */
  private Element createArrayElement(Element dataElement) {
    Element arrayElement = this.createArrayElement();
    arrayElement.appendChild(dataElement);
    return arrayElement;
  }

  /**
   * Creates a new element having "data" as its tag name.
   * 
   * @return the created element.
   */
  private Element createDataElement() {
    return this.document.createElement("data");
  }

  /**
   * Creates a new element having "member" as its tag name.
   * 
   * @return the created element.
   */
  private Element createMemberElement() {
    return this.document.createElement("member");
  }

  /**
   * Creates a new element having "member" as its tag name.
   * 
   * @param nameElement
   *          element to be added as child as the created element.
   * @param valueElement
   *          element to be added as child as the created element.
   * @return the created element.
   */
  private Element createMemberElement(Element nameElement, Element valueElement) {
    Element memberElement = this.createMemberElement();
    memberElement.appendChild(nameElement);
    memberElement.appendChild(valueElement);

    return memberElement;
  }

  /**
   * Creates a new element having "name" as its tag name.
   * 
   * @param text
   *          the text of the created element.
   * @return the created element.
   */
  private Element createNameElement(String text) {
    Element nameElement = this.document.createElement("name");
    nameElement.setTextContent(text);
    return nameElement;
  }

  /**
   * Creates a new element having "param" as its tag name.
   * 
   * @param valueElement
   *          element to be added as child as the created element.
   * @return the created element.
   */
  private Element createParamElement(Element valueElement) {
    Element paramElement = this.createParameterElement();
    paramElement.appendChild(valueElement);
    return paramElement;
  }

  /**
   * Creates a new element having "param" as its tag name.
   * 
   * @return the created element.
   */
  private Element createParameterElement() {
    return this.document.createElement("param");
  }

  /**
   * Creates a new element having "params" as its tag name.
   * 
   * @return the created element.
   */
  private Element createParametersElement() {
    return this.document.createElement("params");
  }

  /**
   * Creates a new element having "string" as its tag name.
   * 
   * @return the created element.
   */
  private Element createStringElement() {
    return this.document.createElement("string");
  }

  /**
   * Creates a new element having "string" as its tag name.
   * 
   * @param text
   *          the text of the created element.
   * @return the created element.
   */
  private Element createStringElement(String text) {
    Element stringElement = this.createStringElement();
    stringElement.setTextContent(text);
    return stringElement;
  }

  /**
   * Creates a new element having "struct" as its tag name.
   * 
   * @return the created element.
   */
  private Element createStructElement() {
    return this.document.createElement("struct");
  }

  /**
   * Creates a new element having "value" as its tag name.
   * 
   * @return the created element.
   */
  private Element createValueElement() {
    return this.document.createElement("value");
  }

  /**
   * Creates a new element having "value" as its tag name.
   * 
   * @param child
   *          element to be added as child as the created element.
   * @return the created element.
   */
  private Element createValueElement(Element child) {
    Element valueElement = this.createValueElement();
    valueElement.appendChild(child);
    return valueElement;
  }

  /**
   * Creates a new element having "value" as its tag name.
   * 
   * @param text
   *          the text of the created element.
   * @return the created element.
   */
  private Element createValueElement(String text) {
    Element valueElement = this.createValueElement();
    valueElement.setTextContent(text);
    return valueElement;
  }

  /*
   * @see TestCase#setUp()
   */
  protected void setUp() throws Exception {
    super.setUp();

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    this.document = builder.newDocument();

    this.parser = new AbstractDomXmlRpcParser() {
      // no extra implementation.
    };
  }

  /**
   * Instantiates <code>{@link #parser}</code> as a mock object.
   * 
   * @param methodToMock
   *          the method to mock.
   */
  private void setUpParserAsMockObject(Method methodToMock) {
    Method[] methodsToMock = { methodToMock };
    this.setUpParserAsMockObject(methodsToMock);
  }

  /**
   * Instantiates <code>{@link #parser}</code> as a mock object.
   * 
   * @param methodsToMock
   *          the methods to mock.
   */
  private void setUpParserAsMockObject(Method[] methodsToMock) {
    Class targetClass = AbstractDomXmlRpcParser.class;

    this.parserControl = MockClassControl.createControl(targetClass, null,
        null, methodsToMock);
    this.parser = (AbstractDomXmlRpcParser) this.parserControl.getMock();
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractDomXmlRpcParser#parseArrayElement(Element)}</code>
   * delegates the parsing of the XML-RPC array to the method
   * <code>{@link AbstractDomXmlRpcParser#parseDataElement(Element)}</code>.
   */
  public void testParseArrayElement() throws Exception {
    Method methodToMock = AbstractDomXmlRpcParser.class.getDeclaredMethod(
        "parseDataElement", new Class[] { Element.class });

    this.setUpParserAsMockObject(methodToMock);

    XmlRpcArray expected = new XmlRpcArray();

    Element dataElement = this.createDataElement();
    Element arrayElement = this.createArrayElement(dataElement);

    // expectation: parse the XML element with name "array".
    this.parser.parseDataElement(dataElement);
    this.parserControl.setReturnValue(expected);

    // set the state of the mock object to "replay".
    this.parserControl.replay();

    // execute the method to test.
    XmlRpcArray actual = this.parser.parseArrayElement(arrayElement);

    assertSame("<XML-RPC array>", expected, actual);

    // verify the expectations of the mock object were met.
    this.parserControl.verify();
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractDomXmlRpcParser#parseDataElement(Element)}</code>
   * delegates the parsing of each of the elements of the XML-RPC array to the
   * method
   * <code>{@link AbstractDomXmlRpcParser#parseValueElement(Element)}</code>.
   */
  public void testParseDataElement() throws Exception {
    Method methodToMock = AbstractDomXmlRpcParser.class.getDeclaredMethod(
        "parseValueElement", new Class[] { Element.class });

    this.setUpParserAsMockObject(methodToMock);

    XmlRpcArray expected = new XmlRpcArray();

    Element dataElement = this.createDataElement();
    int expectedSize = 5;

    for (int i = 0; i < expectedSize; i++) {
      Integer value = new Integer(i);
      XmlRpcInteger xmlRpcInteger = new XmlRpcInteger(value);
      expected.add(xmlRpcInteger);

      Element valueElement = this.createValueElement();
      dataElement.appendChild(valueElement);

      // expectation: parse each XML element with name "value".
      this.parser.parseValueElement(valueElement);
      this.parserControl.setReturnValue(xmlRpcInteger);
    }

    // set the state of the mock object to "replay".
    this.parserControl.replay();

    // execute the method to test.
    XmlRpcArray actual = this.parser.parseDataElement(dataElement);

    assertEquals("<XML-RPC array>", expected, actual);

    // verify the expectations of the mock object were met.
    this.parserControl.verify();
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractDomXmlRpcParser#parseMemberElement(Element)}</code>:
   * <ul>
   * <li>Obtains the name of the struct member from the XML element with name
   * "name", set as child of the XML element with name "member".</li>
   * <li>Delegates the parsing of the value of the struct member to the method
   * <code>{@link AbstractDomXmlRpcParser#parseValueElement(Element)}</code>.</li>
   * </ul>
   */
  public void testParseMemberElement() throws Exception {
    Method methodToMock = AbstractDomXmlRpcParser.class.getDeclaredMethod(
        "parseValueElement", new Class[] { Element.class });

    this.setUpParserAsMockObject(methodToMock);

    XmlRpcMember expected = new XmlRpcMember("Han", new XmlRpcString(
        "Falcon Millenium"));

    Element nameElement = this.createNameElement(expected.name);
    Element valueElement = this.createValueElement();
    Element memberElement = this.createMemberElement(nameElement, valueElement);

    // expectation: parse the XML element with name "value".
    this.parser.parseValueElement(valueElement);
    this.parserControl.setReturnValue(expected.value);

    // set the state of the mock object to "replay".
    this.parserControl.replay();

    // execute the method to test.
    XmlRpcMember actual = this.parser.parseMemberElement(memberElement);

    assertEquals("<XML-RPC struct member>", expected, actual);

    // verify the expectations of the mock object were met.
    this.parserControl.verify();
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractDomXmlRpcParser#parseParameterElement(Element)}</code>
   * delegates the parsing of the XML-RPC parameter to the method
   * <code>{@link AbstractDomXmlRpcParser#parseValueElement(Element)}</code>.
   */
  public void testParseParameterElement() throws Exception {
    Method methodToMock = AbstractDomXmlRpcParser.class.getDeclaredMethod(
        "parseValueElement", new Class[] { Element.class });

    this.setUpParserAsMockObject(methodToMock);

    String expectedValue = "Luke";
    XmlRpcString expected = new XmlRpcString(expectedValue);

    Element valueElement = this.createValueElement(expectedValue);
    Element paramElement = this.createParamElement(valueElement);

    // expectation: parse the XML element with name "value".
    this.parser.parseValueElement(valueElement);
    this.parserControl.setReturnValue(expected);

    // set the state of the mock object to "replay".
    this.parserControl.replay();

    // execute the method to test.
    Object actual = this.parser.parseParameterElement(paramElement);

    assertEquals("<XML-RPC parameter>", expected, actual);

    // verify the expectations of the mock object were met.
    this.parserControl.verify();
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractDomXmlRpcParser#parseParametersElement(Element)}</code>
   * delegates the parsing of each XML-RPC parameter to the method
   * <code>{@link AbstractDomXmlRpcParser#parseParameterElement(Element)}</code>.
   */
  public void testParseParametersElement() throws Exception {
    Method parseParameterElementMethod = AbstractDomXmlRpcParser.class
        .getDeclaredMethod("parseParameterElement",
            new Class[] { Element.class });

    this.setUpParserAsMockObject(parseParameterElementMethod);

    Element parametersElement = this.createParametersElement();
    XmlRpcString[] expected = { new XmlRpcString("Han"),
        new XmlRpcString("Lando") };

    int parameterCount = expected.length;
    for (int i = 0; i < parameterCount; i++) {
      Element parameterElement = this.createParameterElement();
      parametersElement.appendChild(parameterElement);

      // expectation: parse the XML element with name "param".
      this.parser.parseParameterElement(parameterElement);
      this.parserControl.setReturnValue(expected[i]);
    }

    // set the state of the mock object to "replay".
    this.parserControl.replay();

    // execute the method to test.
    XmlRpcElement[] actual = this.parser
        .parseParametersElement(parametersElement);

    assertTrue("<XML-RPC parameters>. Expected: '" + Arrays.toString(expected)
        + "' but was: '" + Arrays.toString(actual) + "'", Arrays.equals(
        expected, actual));

    // verify that the expectations of the mock object were met.
    this.parserControl.verify();
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractDomXmlRpcParser#parseStructElement(Element)}</code>
   * delegates the parsing of each of the members of the XML-RPC struct to
   * <code>{@link AbstractDomXmlRpcParser#parseMemberElement(Element)}</code>.
   */
  public void testParseStructElement() throws Exception {
    Method methodToMock = AbstractDomXmlRpcParser.class.getDeclaredMethod(
        "parseMemberElement", new Class[] { Element.class });

    this.setUpParserAsMockObject(methodToMock);

    XmlRpcStruct expected = new XmlRpcStruct();

    int expectedSize = 3;
    Element structElement = this.createStructElement();
    Element[] memberElements = new Element[expectedSize];

    for (int i = 0; i < expectedSize; i++) {
      Integer value = new Integer(i);
      XmlRpcInteger xmlRpcInteger = new XmlRpcInteger(value);
      XmlRpcMember xmlRpcMember = new XmlRpcMember(value.toString(),
          xmlRpcInteger);
      expected.add(xmlRpcMember);

      Element memberElement = this.createMemberElement();
      memberElements[i] = memberElement;
      structElement.appendChild(memberElement);

      // expectation: parse the XML element with name "member".
      this.parser.parseMemberElement(memberElement);
      this.parserControl.setReturnValue(xmlRpcMember);
    }

    // set the state of the mock object to "replay".
    this.parserControl.replay();

    // execute the method to test.
    XmlRpcStruct actual = this.parser.parseStructElement(structElement);

    assertEquals("<Struct>", expected, actual);

    // verify that the expectations of the mock object were met.
    this.parserControl.verify();
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractDomXmlRpcParser#parseValueElement(Element)}</code>
   * delegates the parsing of the XML element to the method
   * <code>{@link AbstractDomXmlRpcParser#parseArrayElement(Element)}</code>
   * if the XML element represents an XML-RPC array.
   */
  public void testParseValueElementWhenChildContainsArray() throws Exception {
    Method methodToMock = AbstractDomXmlRpcParser.class.getDeclaredMethod(
        "parseArrayElement", new Class[] { Element.class });

    this.setUpParserAsMockObject(methodToMock);

    Element arrayElement = this.createArrayElement();
    Element valueElement = this.createValueElement(arrayElement);

    XmlRpcArray expected = new XmlRpcArray();

    // expectation: parse the "array" element;
    this.parser.parseArrayElement(arrayElement);
    this.parserControl.setReturnValue(expected);

    // set the state of the mock object to "replay".
    this.parserControl.replay();

    // execute the method to test.
    XmlRpcElement actual = this.parser.parseValueElement(valueElement);

    assertSame("<XML-RPC array>", expected, actual);

    // verify the expectations of the mock object were met.
    this.parserControl.verify();
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractDomXmlRpcParser#parseValueElement(Element)}</code>
   * delegates the parsing to of the XML element to
   * <code>{@link XmlRpcScalarFactory#createScalarValue(String, String)}</code>
   * if the XML element represents an XML-RPC scalar value.
   */
  public void testParseValueElementWhenChildContainsScalarValue() {
    MockControl xmlRpcScalarFactoryControl = MockControl
        .createControl(XmlRpcScalarFactory.class);
    XmlRpcScalarFactory xmlRpcScalarFactory = (XmlRpcScalarFactory) xmlRpcScalarFactoryControl
        .getMock();

    this.parser.setXmlRpcScalarFactory(xmlRpcScalarFactory);

    String stringValue = "X-Wing";
    Element scalarElement = this.createStringElement(stringValue);
    Element valueElement = this.createValueElement(scalarElement);
    XmlRpcString expected = new XmlRpcString(stringValue);

    // expectation: create a XmlRpcScalarFactory.
    xmlRpcScalarFactory.createScalarValue(scalarElement.getNodeName(),
        stringValue);
    xmlRpcScalarFactoryControl.setReturnValue(expected);

    // set the state of the mock object to "replay".
    xmlRpcScalarFactoryControl.replay();

    // execute the method to test.
    XmlRpcElement actual = this.parser.parseValueElement(valueElement);

    assertSame("<Parsed object>", expected, actual);

    // verify the expectations of the mock object were met.
    xmlRpcScalarFactoryControl.verify();
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractDomXmlRpcParser#parseValueElement(Element)}</code>
   * delegates the parsing to of the XML element to
   * <code>{@link AbstractDomXmlRpcParser#parseStructElement(Element)}</code>
   * if the XML element represents an XML-RPC struct.
   */
  public void testParseValueElementWhenChildContainsStruct() throws Exception {
    Method methodToMock = AbstractDomXmlRpcParser.class.getDeclaredMethod(
        "parseStructElement", new Class[] { Element.class });

    this.setUpParserAsMockObject(methodToMock);

    Element structElement = this.createStructElement();
    Element valueElement = this.createValueElement(structElement);

    XmlRpcStruct expected = new XmlRpcStruct();

    // expectation: parse the XML element with name "struct".
    this.parser.parseStructElement(structElement);
    this.parserControl.setReturnValue(expected);

    // set the state of the mock object to "replay".
    this.parserControl.replay();

    // execute the method to test.
    XmlRpcElement actual = this.parser.parseValueElement(valueElement);

    assertSame("<Parsed object>", expected, actual);

    // verify the expectations of the mock object were met.
    this.parserControl.verify();
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractDomXmlRpcParser#parseValueElement(Element)}</code>
   * returns the text of the XML element if it does not contain any element
   * specifying the data type.
   */
  public void testParseValueElementWhenTypeIsNotSpecified() throws Exception {
    String text = "Yoda";
    XmlRpcString expected = new XmlRpcString(text);
    Element valueElement = this.createValueElement(text);

    // execute the method to test.
    Object actual = this.parser.parseValueElement(valueElement);

    assertEquals("<Parsed object>", expected, actual);
  }

}
