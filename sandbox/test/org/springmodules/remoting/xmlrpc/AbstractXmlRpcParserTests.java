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
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.TestCase;

import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * <p>
 * Unit Tests for <code>{@link AbstractXmlRpcParser}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.2 $ $Date: 2005/06/06 04:42:47 $
 */
public class AbstractXmlRpcParserTests extends TestCase {

  /**
   * A DOM document used to create elements.
   */
  private Document document;

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
  public AbstractXmlRpcParserTests(String name) {
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
   * Creates a new element having "base64" as its tag name.
   * 
   * @return the created element.
   */
  private Element createBase64Element() {
    return this.document.createElement("base64");
  }

  /**
   * Creates a new element having "boolean" as its tag name.
   * 
   * @return the created element.
   */
  private Element createBooleanElement() {
    return this.document.createElement("boolean");
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
   * Creates a new element having "dateTime.iso8601" as its tag name.
   * 
   * @return the created element.
   */
  private Element createDateTimeElement() {
    return this.document.createElement("dateTime.iso8601");
  }

  /**
   * Creates a new element having "double" as its tag name.
   * 
   * @return the created element.
   */
  private Element createDoubleElement() {
    return this.document.createElement("double");
  }

  /**
   * Creates a new element having "i4" as its tag name.
   * 
   * @return the created element.
   */
  private Element createI4Element() {
    return this.document.createElement("i4");
  }

  /**
   * Creates a new element having "int" as its tag name.
   * 
   * @return the created element.
   */
  private Element createIntElement() {
    return this.document.createElement("int");
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
   * Creates a new element having "name" as its tag name.
   * 
   * @return the created element.
   */
  private Element createNameElement() {
    return this.document.createElement("name");
  }

  /**
   * Creates a new element having "param" as its tag name.
   * 
   * @return the created element.
   */
  private Element createParamElement() {
    return this.document.createElement("param");
  }

  /**
   * Creates a new element having "params" as its tag name.
   * 
   * @return the created element.
   */
  private Element createParamsElement() {
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
   * Sets up the test fixture.
   */
  protected void setUp() throws Exception {
    super.setUp();

    this.parser = new AbstractXmlRpcParser() {
      // no extra implementation.
    };

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    this.document = builder.newDocument();
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractXmlRpcParser#parseBooleanElement(Element)}</code>
   * returns <code>{@link Boolean#FALSE}</code> if the text of the given
   * element is not equal to "1".
   */
  public void testParseBooleanElementWhenValueIsNotEqualToOne() {
    Element booleanElement = this.createBooleanElement();
    booleanElement.setTextContent("0");

    assertEquals("<Boolean>", Boolean.FALSE, this.parser
        .parseBooleanElement(booleanElement));
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractXmlRpcParser#parseDateElement(Element)}</code>
   * throws a <code>{@link XmlRpcParsingException}</code> if the text of the
   * given element cannot be parsed into a date.
   */
  public void testParseDateTimeElementWhenElementTextCannotBeParsedIntoDate() {
    Element dateElement = this.createDateTimeElement();
    dateElement.setTextContent("Some text");

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
    Element doubleElement = this.createDoubleElement();
    doubleElement.setTextContent("eight");

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
    Element integerElement = this.createI4Element();
    integerElement.setTextContent("one");

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
    Element valueElement = this.createValueElement();
    valueElement.setTextContent("Jedi Knight");

    Element paramElement = this.createParamElement();
    paramElement.appendChild(valueElement);

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
    Element paramsElement = this.createParamsElement();

    // create an array of integers.
    List expectedArray = new ArrayList();
    Element dataElement = this.createDataElement();

    for (int i = 0; i < 5; i++) {
      Integer number = new Integer(i);
      expectedArray.add(number);

      Element i4Element = this.createI4Element();
      i4Element.setTextContent(number.toString());

      Element valueElement = this.createValueElement();
      valueElement.appendChild(i4Element);

      dataElement.appendChild(valueElement);
    }

    Element arrayElement = this.createArrayElement();
    arrayElement.appendChild(dataElement);

    Element valueElement = this.createValueElement();
    valueElement.appendChild(arrayElement);

    Element paramElement = this.createParamElement();
    paramElement.appendChild(valueElement);

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
    Element structElement = this.createStructElement();

    int structMemberCount = structMembers.size();
    for (int i = 0; i < structMemberCount; i++) {
      AbstractXmlRpcParser.StructMember member = (AbstractXmlRpcParser.StructMember) structMembers
          .get(i);
      expectedStruct.put(member.name, member.value);

      Element nameElement = this.createNameElement();
      nameElement.setTextContent(member.name);

      valueElement = this.createValueElement();
      valueElement.setTextContent(member.value.toString());

      Element memberElement = this.createMemberElement();
      memberElement.appendChild(nameElement);
      memberElement.appendChild(valueElement);

      structElement.appendChild(memberElement);
    }

    valueElement = this.createValueElement();
    valueElement.appendChild(structElement);

    paramElement = this.createParamElement();
    paramElement.appendChild(valueElement);

    paramsElement.appendChild(paramElement);

    // add a String
    valueElement = this.createValueElement();
    valueElement.setTextContent("R2-D2");

    paramElement = this.createParamElement();
    paramElement.appendChild(valueElement);

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
    Element dataElement = this.createDataElement();

    for (int i = 0; i < 10; i++) {
      Integer number = new Integer(i);
      expected.add(number);

      Element intElement = this.createIntElement();
      intElement.setTextContent(number.toString());

      Element valueElement = this.createValueElement();
      valueElement.appendChild(intElement);

      dataElement.appendChild(valueElement);
    }

    Element arrayElement = this.createArrayElement();
    arrayElement.appendChild(dataElement);

    Element valueElement = this.createValueElement();
    valueElement.appendChild(arrayElement);

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

    Element base64Element = this.createBase64Element();
    base64Element.setTextContent(text);

    Element valueElement = this.createValueElement();
    valueElement.appendChild(base64Element);

    byte[] expected = Base64.decodeBase64(text.getBytes());
    assertTrue("<Byte array>", Arrays.equals(expected, this.parser
        .parseBase64Element(base64Element)));
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractXmlRpcParser#parseValueElement(Element)}</code>
   * returns a <code>Boolean</code> if the child of the specified element is a
   * "boolean" element.
   */
  public void testParseValueElementWhenChildElementIsBoolean() {
    Element booleanElement = this.createBooleanElement();
    booleanElement.setTextContent("1");

    Element valueElement = this.createValueElement();
    valueElement.appendChild(booleanElement);

    assertEquals("<Boolean>", Boolean.TRUE, this.parser
        .parseValueElement(valueElement));
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractXmlRpcParser#parseValueElement(Element)}</code>
   * returns a <code>java.util.Date</code> if the child of the specified
   * element is a "dateTime.iso8601" element.
   */
  public void testParseValueElementWhenChildElementIsDateTime() {
    Element dateElement = this.createDateTimeElement();
    dateElement.setTextContent("19980717T14:08:55");

    Calendar calendar = new GregorianCalendar();
    calendar.set(Calendar.YEAR, 1998);
    calendar.set(Calendar.MONTH, 6);
    calendar.set(Calendar.DATE, 17);
    calendar.set(Calendar.HOUR, 2);
    calendar.set(Calendar.AM_PM, Calendar.PM);
    calendar.set(Calendar.MINUTE, 8);
    calendar.set(Calendar.SECOND, 55);
    calendar.set(Calendar.MILLISECOND, 0);

    Element valueElement = this.createValueElement();
    valueElement.appendChild(dateElement);

    assertEquals("<Date>", calendar.getTime(), this.parser
        .parseValueElement(valueElement));
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractXmlRpcParser#parseValueElement(Element)}</code>
   * returns a <code>Double</code> if the child of the specified element is a
   * "double" element.
   */
  public void testParseValueElementWhenChildElementIsDouble() {
    Element doubleElement = this.createDoubleElement();
    doubleElement.setTextContent("99.88");

    Element valueElement = this.createValueElement();
    valueElement.appendChild(doubleElement);

    assertEquals("<Double>", new Double(99.88), this.parser
        .parseValueElement(valueElement));
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractXmlRpcParser#parseValueElement(Element)}</code>
   * returns an <code>Integer</code> if the child of the specified element is
   * an "i4" element.
   */
  public void testParseValueElementWhenChildElementIsI4() {
    Element integerElement = this.createI4Element();
    integerElement.setTextContent("30");

    Element valueElement = this.createValueElement();
    valueElement.appendChild(integerElement);

    assertEquals("<Integer>", new Integer(30), this.parser
        .parseValueElement(valueElement));
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractXmlRpcParser#parseValueElement(Element)}</code>
   * returns an <code>Integer</code> if the child of the specified element is
   * an "integer" element.
   */
  public void testParseValueElementWhenChildElementIsInt() {
    Element integerElement = this.createIntElement();
    integerElement.setTextContent("30");

    Element valueElement = this.createValueElement();
    valueElement.appendChild(integerElement);

    assertEquals("<Integer>", new Integer(30), this.parser
        .parseValueElement(valueElement));
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractXmlRpcParser#parseValueElement(Element)}</code>
   * returns a <code>String</code> if the child of the specified element is an
   * "string" element.
   */
  public void testParseValueElementWhenChildElementIsString() {
    Element stringElement = this.createStringElement();
    stringElement.setTextContent("Some text");

    Element valueElement = this.createValueElement();
    valueElement.appendChild(stringElement);

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
    Element structElement = this.createStructElement();

    int structMemberCount = structMembers.size();
    for (int i = 0; i < structMemberCount; i++) {
      AbstractXmlRpcParser.StructMember member = (AbstractXmlRpcParser.StructMember) structMembers
          .get(i);
      expected.put(member.name, member.value);

      Element nameElement = this.createNameElement();
      nameElement.setTextContent(member.name);

      Element valueElement = this.createValueElement();
      valueElement.setTextContent(member.value.toString());

      Element memberElement = this.createMemberElement();
      memberElement.appendChild(nameElement);
      memberElement.appendChild(valueElement);

      structElement.appendChild(memberElement);
    }

    Element valueElement = this.createValueElement();
    valueElement.appendChild(structElement);

    assertEquals("<Struct>", expected, this.parser
        .parseValueElement(valueElement));
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractXmlRpcParser#parseValueElement(Element)}</code>
   * returns a <code>String</code> the type of the value is not specified.
   */
  public void testParseValueElementWhenValueTypeIsNotSpecified() {
    Element valueElement = this.createStringElement();
    valueElement.setTextContent("Some text");

    assertEquals("<String>", "Some text", this.parser
        .parseValueElement(valueElement));
  }
}
