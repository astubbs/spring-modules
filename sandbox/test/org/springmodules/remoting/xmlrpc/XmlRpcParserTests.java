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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.Element;

/**
 * <p>
 * Unit Tests for <code>{@link AbstractXmlRpcParser}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/06/06 16:22:07 $
 */
public class XmlRpcParserTests extends AbstractXmlRpcParserTestCase {

  /**
   * Primary object that is under test.
   */
  private AbstractXmlRpcParser parser;

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
   * Verifies that the method
   * <code>{@link AbstractXmlRpcParser#parseBooleanElement(Element)}</code>
   * returns <code>{@link Boolean#FALSE}</code> if the text of the given
   * element is not equal to "1".
   */
  public void testParseBooleanElementWhenValueIsNotEqualToOne() {
    Boolean expected = Boolean.FALSE;
    Element booleanElement = super.createBooleanElement(expected);

    assertEquals("<Boolean>", expected, this.parser
        .parseBooleanElement(booleanElement));
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractXmlRpcParser#parseDateElement(Element)}</code>
   * throws a <code>{@link XmlRpcParsingException}</code> if the text of the
   * given element cannot be parsed into a date.
   */
  public void testParseDateTimeElementWhenElementTextCannotBeParsedIntoDate() {
    Element dateElement = super.createDateTimeElement("Some text");

    try {
      this.parser.parseDateElement(dateElement);
      fail("A 'XmlRpcParsingException' should have been thrown");
    } catch (XmlRpcParsingException exception) {
      // we are expecting this exception.
    }
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractXmlRpcParser#parseDoubleElement(Element)}</code>
   * throws a <code>{@link XmlRpcParsingException}</code> if the text of the
   * given element cannot be parsed into a double.
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
   * <code>{@link AbstractXmlRpcParser#parseParametersElement(Element, List, List)}</code>
   * fills the lists with the parameter types and arguments obtain from parsing
   * the given DOM element.
   */
  public void testParseParametersElement() {
    Element paramsElement = super.createParamsElement();

    // create an array of integers.
    List expectedArray = new ArrayList();
    Element dataElement = super.createDataElement();

    for (int i = 0; i < 5; i++) {
      Integer number = new Integer(i);
      expectedArray.add(number);

      Element i4Element = super.createI4Element(number);
      Element valueElement = super.createValueElement(i4Element);

      dataElement.appendChild(valueElement);
    }

    Element arrayElement = super.createArrayElement(dataElement);
    Element valueElement = super.createValueElement(arrayElement);
    Element paramElement = super.createParamElement(valueElement);

    paramsElement.appendChild(paramElement);

    // create a struct.
    List structMembers = new ArrayList();
    structMembers
        .add(new AbstractXmlRpcParser.StructMember("firstName", "Leia"));
    structMembers.add(new AbstractXmlRpcParser.StructMember("lastName",
        "Organa"));
    structMembers
        .add(new AbstractXmlRpcParser.StructMember("role", "Princess"));

    Map expectedStruct = new HashMap();
    Element structElement = super.createStructElement();

    int structMemberCount = structMembers.size();
    for (int i = 0; i < structMemberCount; i++) {
      AbstractXmlRpcParser.StructMember member = (AbstractXmlRpcParser.StructMember) structMembers
          .get(i);
      expectedStruct.put(member.name, member.value);

      Element nameElement = super.createNameElement(member.name);
      valueElement = super.createValueElement(member.value.toString());

      Element memberElement = super.createMemberElement(nameElement,
          valueElement);

      structElement.appendChild(memberElement);
    }

    valueElement = super.createValueElement(structElement);
    paramElement = super.createParamElement(valueElement);

    paramsElement.appendChild(paramElement);

    // add a String
    valueElement = super.createValueElement("R2-D2");
    paramElement = super.createParamElement(valueElement);

    paramsElement.appendChild(paramElement);

    // set up the arguments and parameter types.
    List expectedParameterTypes = new ArrayList();
    expectedParameterTypes.add(List.class);
    expectedParameterTypes.add(Map.class);
    expectedParameterTypes.add(String.class);

    List expectedArguments = new ArrayList();
    expectedArguments.add(expectedArray);
    expectedArguments.add(expectedStruct);
    expectedArguments.add("R2-D2");

    List actualParameterTypes = new ArrayList();
    List actualArguments = new ArrayList();

    // execute the method to test.
    this.parser.parseParametersElement(paramsElement, actualParameterTypes,
        actualArguments);

    assertEquals("<Parameter types>", expectedParameterTypes,
        actualParameterTypes);
    assertEquals("<Arguments>", expectedArguments, actualArguments);
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractXmlRpcParser#parseValueElement(Element)}</code>
   * returns a <code>java.util.List</code> if the child of the specified
   * element is a "array" element.
   */
  public void testParseValueElementWhenChildElementIsArray() {
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
    Element valueElement = super.createValueElement(arrayElement);

    assertEquals("<Array>", expected, this.parser
        .parseValueElement(valueElement));
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractXmlRpcParser#parseValueElement(Element)}</code>
   * returns an array of <code>byte</code>s if the child of the specified
   * element is a "base64" element.
   */
  public void testParseValueElementWhenChildElementIsBase64() {
    String text = "eW91IGNhbid0IHJlYWQgdGhpcyE=";

    Element base64Element = super.createBase64Element(text);
    Element valueElement = super.createValueElement(base64Element);

    byte[] expected = Base64.decodeBase64(text.getBytes());
    assertTrue("<Byte array>", Arrays.equals(expected, (byte[]) this.parser
        .parseValueElement(valueElement)));
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractXmlRpcParser#parseValueElement(Element)}</code>
   * returns a <code>Boolean</code> if the child of the specified element is a
   * "boolean" element.
   */
  public void testParseValueElementWhenChildElementIsBoolean() {
    Boolean expected = Boolean.TRUE;

    Element booleanElement = super.createBooleanElement(expected);
    Element valueElement = super.createValueElement(booleanElement);

    assertEquals("<Boolean>", expected, this.parser
        .parseValueElement(valueElement));
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractXmlRpcParser#parseValueElement(Element)}</code>
   * returns a <code>java.util.Date</code> if the child of the specified
   * element is a "dateTime.iso8601" element.
   */
  public void testParseValueElementWhenChildElementIsDateTime() {
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

    Element dateElement = super.createDateTimeElement(expected);
    Element valueElement = super.createValueElement(dateElement);

    assertEquals("<Date>", expected, this.parser
        .parseValueElement(valueElement));
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractXmlRpcParser#parseValueElement(Element)}</code>
   * returns a <code>Double</code> if the child of the specified element is a
   * "double" element.
   */
  public void testParseValueElementWhenChildElementIsDouble() {
    Double expected = new Double(99.88);

    Element doubleElement = super.createDoubleElement(expected);
    Element valueElement = super.createValueElement(doubleElement);

    assertEquals("<Double>", expected, this.parser
        .parseValueElement(valueElement));
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractXmlRpcParser#parseValueElement(Element)}</code>
   * returns an <code>Integer</code> if the child of the specified element is
   * an "i4" element.
   */
  public void testParseValueElementWhenChildElementIsI4() {
    Integer expected = new Integer(30);

    Element integerElement = super.createI4Element(expected);
    Element valueElement = super.createValueElement(integerElement);

    assertEquals("<Integer>", expected, this.parser
        .parseValueElement(valueElement));
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractXmlRpcParser#parseValueElement(Element)}</code>
   * returns an <code>Integer</code> if the child of the specified element is
   * an "integer" element.
   */
  public void testParseValueElementWhenChildElementIsInt() {
    Integer expected = new Integer(30);

    Element integerElement = super.createIntElement(expected);
    Element valueElement = super.createValueElement(integerElement);

    assertEquals("<Integer>", expected, this.parser
        .parseValueElement(valueElement));
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractXmlRpcParser#parseValueElement(Element)}</code>
   * returns a <code>String</code> if the child of the specified element is an
   * "string" element.
   */
  public void testParseValueElementWhenChildElementIsString() {
    String expected = "Some text";

    Element stringElement = super.createStringElement(expected);
    Element valueElement = super.createValueElement(stringElement);

    assertEquals("<String>", "Some text", this.parser
        .parseValueElement(valueElement));
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractXmlRpcParser#parseValueElement(Element)}</code>
   * returns a <code>java.util.Map</code> if the child of the specified
   * element is an "struct" element.
   */
  public void testParseValueElementWhenChildElementIsStruct() {
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
   * returns a <code>String</code> the type of the value is not specified.
   */
  public void testParseValueElementWhenValueTypeIsNotSpecified() {
    String expected = "Some text";

    Element valueElement = super.createValueElement(expected);

    assertEquals("<String>", "Some text", this.parser
        .parseValueElement(valueElement));
  }
}
