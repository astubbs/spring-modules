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
package org.springmodules.remoting.xmlrpc;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.easymock.classextension.MockClassControl;
import org.w3c.dom.Element;

/**
 * <p>
 * Unit Tests for <code>{@link AbstractXmlRpcParser}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.3 $ $Date: 2005/06/08 01:57:32 $
 */
public class XmlRpcParserTests extends AbstractXmlRpcParserTestCase {

  /**
   * Primary object that is under test.
   */
  private AbstractXmlRpcParser parser;

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
  public XmlRpcParserTests(String name) {
    super(name);
  }

  /**
   * Sets up the test fixture.
   */
  protected void onSetUp() throws Exception {
    super.onSetUp();

    this.parser = new AbstractXmlRpcParser() {
      // no extra implementation.
    };
  }

  /**
   * Instantiates <code>{@link #parser}</code> as a mock object.
   * 
   * @param methodName
   *          the name of the method to mock. It should receive a DOM element as
   *          argument.
   */
  private void setUpParserAsMockObject(String methodName) throws Exception {
    String[] methodNames = { methodName };
    this.setUpParserAsMockObject(methodNames);
  }

  /**
   * Instantiates <code>{@link #parser}</code> as a mock object.
   * 
   * @param methodNames
   *          the names of the methods to mock. Each of the methods should
   *          receive a DOM element as argument.
   */
  private void setUpParserAsMockObject(String[] methodNames) throws Exception {
    Class targetClass = AbstractXmlRpcParser.class;

    int methodNameCount = methodNames.length;
    Method[] methodsToMock = new Method[methodNameCount];

    for (int i = 0; i < methodNameCount; i++) {
      String methodName = methodNames[i];
      Method method = targetClass.getDeclaredMethod(methodName,
          new Class[] { Element.class });
      methodsToMock[i] = method;
    }

    this.parserControl = MockClassControl.createControl(targetClass, null,
        null, methodsToMock);
    this.parser = (AbstractXmlRpcParser) this.parserControl.getMock();
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractXmlRpcParser#parseArrayElement(Element)}</code>
   * returns a <code>java.util.List</code> containing all the parsed children
   * of the given "array" element.
   */
  public void testParseArrayElement() {
    List expected = new ArrayList();
    Element dataElement = super.createDataElement();

    for (int i = 0; i < 10; i++) {
      Integer number = new Integer(i);
      expected.add(number);

      Element intElement = super.createIntElement(number);
      Element valueElement = super.createValueElement(intElement);

      dataElement.appendChild(valueElement);
    }

    Element arrayElement = super.createArrayElement(dataElement);

    assertEquals("<Array>", expected, this.parser
        .parseArrayElement(arrayElement));
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractXmlRpcParser#parseBase64Element(Element)}</code>
   * parses the text of the given element into an array of <code>byte</code>s.
   */
  public void testParseBase64Element() {
    String text = "eW91IGNhbid0IHJlYWQgdGhpcyE=";
    Element base64Element = super.createBase64Element(text);

    byte[] expected = Base64.decodeBase64(text.getBytes());
    assertTrue("<Byte array>", Arrays.equals(expected, (byte[]) this.parser
        .parseBase64Element(base64Element)));
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractXmlRpcParser#parseBooleanElement(Element)}</code>
   * returns <code>{@link Boolean#FALSE}</code> if the text of the given
   * "boolean" element is equal to "1".
   */
  public void testParseBooleanElementWhenValueIsEqualToOne() {
    Boolean expected = Boolean.TRUE;
    Element booleanElement = super.createBooleanElement(expected);

    assertEquals("<Boolean>", expected, this.parser
        .parseBooleanElement(booleanElement));
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractXmlRpcParser#parseBooleanElement(Element)}</code>
   * returns <code>{@link Boolean#FALSE}</code> if the text of the given
   * "boolean" element is not equal to "1".
   */
  public void testParseBooleanElementWhenValueIsNotEqualToOne() {
    Boolean expected = Boolean.FALSE;
    Element booleanElement = super.createBooleanElement(expected);

    assertEquals("<Boolean>", expected, this.parser
        .parseBooleanElement(booleanElement));
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractXmlRpcParser#parseDateTimeElement(Element)}</code>
   * parses the text of the given element into a <code>java.util.Date</code>.
   */
  public void testParseDateTimeElement() {
    Calendar calendar = new GregorianCalendar();
    calendar.set(Calendar.YEAR, 1998);
    calendar.set(Calendar.MONTH, 6);
    calendar.set(Calendar.DATE, 17);
    calendar.set(Calendar.HOUR, 2);
    calendar.set(Calendar.AM_PM, Calendar.PM);
    calendar.set(Calendar.MINUTE, 8);
    calendar.set(Calendar.SECOND, 55);
    calendar.set(Calendar.MILLISECOND, 0);

    Date expected = calendar.getTime();
    Element dateTimeElement = super.createDateTimeElement(expected);

    assertEquals("<Date>", expected, this.parser
        .parseDateTimeElement(dateTimeElement));
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractXmlRpcParser#parseDateTimeElement(Element)}</code>
   * throws a <code>{@link XmlRpcParsingException}</code> if the text of the
   * given "dateTime" element cannot be parsed into a date.
   */
  public void testParseDateTimeElementWhenElementTextCannotBeParsedIntoDate() {
    Element dateElement = super.createDateTimeElement("Some text");

    try {
      this.parser.parseDateTimeElement(dateElement);
      fail("A 'XmlRpcParsingException' should have been thrown");
    } catch (XmlRpcParsingException exception) {
      // we are expecting this exception.
    }
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractXmlRpcParser#parseBooleanElement(Element)}</code>
   * parses the text of the given element into a <code>{@link Double}</code>.
   */
  public void testParseDoubleElement() {
    Double expected = new Double(99.88);

    Element doubleElement = super.createDoubleElement(expected);
    Element valueElement = super.createValueElement(doubleElement);

    assertEquals("<Double>", expected, this.parser
        .parseValueElement(valueElement));
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractXmlRpcParser#parseDoubleElement(Element)}</code>
   * throws a <code>{@link XmlRpcParsingException}</code> if the text of the
   * given "double" element cannot be parsed into a double.
   */
  public void testParseDoubleElementWhenElementTextCannotBeParsedIntoDouble() {
    Element doubleElement = super.createDoubleElement("eight");

    try {
      this.parser.parseDoubleElement(doubleElement);
      fail("A 'XmlRpcParsingException' should have been thrown");
    } catch (XmlRpcParsingException exception) {
      // we are expecting this exception.
    }
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractXmlRpcParser#parseIntegerElement(Element)}</code>
   * parses the text of the given element into an <code>{@link Integer}</code>.
   */
  public void testParseIntegerElement() {
    Integer expected = new Integer(30);
    Element integerElement = super.createI4Element(expected);

    assertEquals("<Integer>", expected, this.parser
        .parseIntegerElement(integerElement));
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractXmlRpcParser#parseIntegerElement(Element)}</code>
   * throws a <code>{@link XmlRpcParsingException}</code> if the text of the
   * given element cannot be parsed into a integer.
   */
  public void testParseIntegerElementWhenElementTextCannotBeParsedIntoInteger() {
    Element integerElement = super.createI4Element("one");

    try {
      this.parser.parseIntegerElement(integerElement);
      fail("A 'XmlRpcParsingException' should have been thrown");
    } catch (XmlRpcParsingException exception) {
      // we are expecting this exception.
    }
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractXmlRpcParser#parseParameterElement(Element)}</code>
   * returns the parsed value of this "value" element.
   */
  public void testParseParameterElement() {
    Element valueElement = super.createValueElement("Jedi Knight");
    Element paramElement = super.createParamElement(valueElement);

    assertEquals("<Param>", "Jedi Knight", this.parser
        .parseParameterElement(paramElement));
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractXmlRpcParser#parseParametersElement(Element)}</code>
   * parses the given DOM element into a
   * <code>{@link XmlRpcRemoteInvocationArguments}</code>.
   */
  public void testParseParametersElement() throws Exception {
    this.setUpParserAsMockObject("parseParameterElement");

    Element paramsElement = super.createParamsElement();

    Class[] expectedParameterTypes = { Map.class, List.class, String.class };
    Object[] expectedArguments = { new HashMap(), new ArrayList(), "C3PO" };

    int parameterCount = expectedArguments.length;
    for (int i = 0; i < parameterCount; i++) {
      Element paramElement = super.createParamElement();
      paramsElement.appendChild(paramElement);

      // expectation: parse the "param" element.
      this.parser.parseParameterElement(paramElement);
      this.parserControl.setReturnValue(expectedArguments[i]);
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
   * <code>{@link AbstractXmlRpcParser#parseStringElement(Element)}</code>
   * parses the text of the given element into a <code>String</code>.
   */
  public void testParseStringElement() {
    String expected = "Some text";

    Element stringElement = super.createStringElement(expected);
    Element valueElement = super.createValueElement(stringElement);

    assertEquals("<String>", "Some text", this.parser
        .parseValueElement(valueElement));
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractXmlRpcParser#parseStructElement(Element)}</code>
   * parses the text of the given element into a <code>java.util.List</code>.
   */
  public void testParseStructElment() {
    List structMembers = new ArrayList();
    structMembers
        .add(new AbstractXmlRpcParser.StructMember("firstName", "Leia"));
    structMembers.add(new AbstractXmlRpcParser.StructMember("lastName",
        "Organa"));
    structMembers
        .add(new AbstractXmlRpcParser.StructMember("role", "Princess"));

    Map expected = new HashMap();
    Element structElement = super.createStructElement();

    int structMemberCount = structMembers.size();
    for (int i = 0; i < structMemberCount; i++) {
      AbstractXmlRpcParser.StructMember member = (AbstractXmlRpcParser.StructMember) structMembers
          .get(i);
      expected.put(member.name, member.value);

      Element nameElement = super.createNameElement(member.name);
      Element valueElement = super.createValueElement(member.value.toString());
      Element memberElement = super.createMemberElement(nameElement,
          valueElement);

      structElement.appendChild(memberElement);
    }

    Element valueElement = super.createValueElement(structElement);

    assertEquals("<Struct>", expected, this.parser
        .parseValueElement(valueElement));
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractXmlRpcParser#parseValueElement(Element)}</code>
   * delegates the parsing to
   * <code>{@link AbstractXmlRpcParser#parseArrayElement(Element)}</code> if
   * the given "value" element contains an "array" element.
   */
  public void testParseValueElementWhenChildIsArrayElement() throws Exception {
    this.setUpParserAsMockObject("parseArrayElement");
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
   * <code>{@link AbstractXmlRpcParser#parseValueElement(Element)}</code>
   * delegates the parsing to
   * <code>{@link AbstractXmlRpcParser#parseBase64Element(Element)}</code> if
   * the given "value" element contains a "base64" element.
   */
  public void testParseValueElementWhenChildIsBase64Element() throws Exception {
    this.setUpParserAsMockObject("parseBase64Element");
    Element base64Element = super.createBase64Element();
    Element valueElement = super.createValueElement(base64Element);

    byte[] expected = new byte[0];

    // expectation: parse the "base64" element;
    this.parser.parseBase64Element(base64Element);
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
   * <code>{@link AbstractXmlRpcParser#parseValueElement(Element)}</code>
   * delegates the parsing to
   * <code>{@link AbstractXmlRpcParser#parseBooleanElement(Element)}</code> if
   * the given "value" element contains a "boolean" element.
   */
  public void testParseValueElementWhenChildIsBooleanElement() throws Exception {
    this.setUpParserAsMockObject("parseBooleanElement");
    Element booleanElement = super.createBooleanElement();
    Element valueElement = super.createValueElement(booleanElement);

    Boolean expected = Boolean.TRUE;

    // expectation: parse the "boolean" element;
    this.parser.parseBooleanElement(booleanElement);
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
   * <code>{@link AbstractXmlRpcParser#parseValueElement(Element)}</code>
   * delegates the parsing to
   * <code>{@link AbstractXmlRpcParser#parseDateTimeElement(Element)}</code>
   * if the given "value" element contains a "dateTime.iso8601" element.
   */
  public void testParseValueElementWhenChildIsDateTimeElement()
      throws Exception {
    this.setUpParserAsMockObject("parseDateTimeElement");
    Element dateTimeElement = super.createDateTimeElement();
    Element valueElement = super.createValueElement(dateTimeElement);

    Date expected = new Date();

    // expectation: parse the "dateTime.iso8601" element;
    this.parser.parseDateTimeElement(dateTimeElement);
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
   * <code>{@link AbstractXmlRpcParser#parseValueElement(Element)}</code>
   * delegates the parsing to
   * <code>{@link AbstractXmlRpcParser#parseDoubleElement(Element)}</code> the
   * given "value" element contains a "double" element.
   */
  public void testParseValueElementWhenChildIsDoubleElement() throws Exception {
    this.setUpParserAsMockObject("parseDoubleElement");
    Element doubleElement = super.createDoubleElement();
    Element valueElement = super.createValueElement(doubleElement);

    Double expected = new Double(6992l);

    // expectation: parse the "double" element;
    this.parser.parseDoubleElement(doubleElement);
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
   * <code>{@link AbstractXmlRpcParser#parseValueElement(Element)}</code>
   * delegates the parsing to
   * <code>{@link AbstractXmlRpcParser#parseIntegerElement(Element)}</code>
   * the given "value" element contains a "i4" element.
   */
  public void testParseValueElementWhenChildIsI4Element() throws Exception {
    this.setUpParserAsMockObject("parseIntegerElement");
    Element i4Element = super.createI4Element();
    Element valueElement = super.createValueElement(i4Element);

    Integer expected = new Integer(88);

    // expectation: parse the "i4" element;
    this.parser.parseIntegerElement(i4Element);
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
   * <code>{@link AbstractXmlRpcParser#parseValueElement(Element)}</code>
   * delegates the parsing to
   * <code>{@link AbstractXmlRpcParser#parseIntegerElement(Element)}</code>
   * the given "value" element contains a "int" element.
   */
  public void testParseValueElementWhenChildIsIntElement() throws Exception {
    this.setUpParserAsMockObject("parseIntegerElement");
    Element intElement = super.createIntElement();
    Element valueElement = super.createValueElement(intElement);

    Integer expected = new Integer(88);

    // expectation: parse the "int" element;
    this.parser.parseIntegerElement(intElement);
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
   * <code>{@link AbstractXmlRpcParser#parseValueElement(Element)}</code>
   * delegates the parsing to
   * <code>{@link AbstractXmlRpcParser#parseStringElement(Element)}</code> the
   * given "value" element contains a "string" element.
   */
  public void testParseValueElementWhenChildIsStringElement() throws Exception {
    this.setUpParserAsMockObject("parseStringElement");
    Element stringElement = super.createStringElement();
    Element valueElement = super.createValueElement(stringElement);

    String expected = "Yoda";

    // expectation: parse the "string" element;
    this.parser.parseStringElement(stringElement);
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
   * <code>{@link AbstractXmlRpcParser#parseValueElement(Element)}</code>
   * delegates the parsing to
   * <code>{@link AbstractXmlRpcParser#parseStructElement(Element)}</code> the
   * given "value" element contains a "struct" element.
   */
  public void testParseValueElementWhenChildIsStructElement() throws Exception {
    this.setUpParserAsMockObject("parseStructElement");
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
   * <code>{@link AbstractXmlRpcParser#parseValueElement(Element)}</code>
   * returns a <code>String</code> the type of the value is not specified.
   */
  public void testParseValueElementWhenValueTypeIsNotSpecified() {
    String expected = "Some text";

    Element valueElement = super.createValueElement(expected);

    assertEquals("<String>", "Some text", this.parser
        .parseValueElement(valueElement));
  }
}
