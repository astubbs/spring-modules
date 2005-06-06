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

import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;

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
 * @version $Revision: 1.1 $ $Date: 2005/06/06 02:14:57 $
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
   * Creates a new element having "dateTime.iso8601" as its tag name.
   * 
   * @return the created element.
   */
  private Element createDateElement() {
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
   * Creates a new element having "string" as its tag name.
   * 
   * @return the created element.
   */
  private Element createStringElement() {
    return this.document.createElement("string");
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

  public void testParseArrayElement() {
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractXmlRpcParser#parseBase64Element(Element)}</code>
   * returns an array of <code>byte</code>s.
   */
  public void testParseBase64Element() {
    String text = "eW91IGNhbid0IHJlYWQgdGhpcyE=";

    Element base64Element = this.createBase64Element();
    base64Element.setTextContent(text);

    byte[] expected = Base64.decodeBase64(text.getBytes());
    assertTrue("<Byte array>", Arrays.equals(expected, this.parser
        .parseBase64Element(base64Element)));
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractXmlRpcParser#parseBooleanElement(Element)}</code>
   * returns <code>{@link Boolean#TRUE}</code> if the text of the given
   * element is equal to "1".
   */
  public void testParseBooleanElementWhenValueIsEqualToOne() {
    Element booleanElement = this.createBooleanElement();
    booleanElement.setTextContent("1");

    assertEquals("<Boolean>", Boolean.TRUE, this.parser
        .parseBooleanElement(booleanElement));
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

  public void testParseDataElement() {
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractXmlRpcParser#parseDateElement(Element)}</code>
   * returns a <code>java.util.Date</code>.
   */
  public void testParseDateElement() {
    Element dateElement = this.createDateElement();
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

    assertEquals("<Date>", calendar.getTime(), this.parser
        .parseDateElement(dateElement));
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractXmlRpcParser#parseDateElement(Element)}</code>
   * throws a <code>{@link XmlRpcParsingException}</code> if the text of the
   * given element cannot be parsed into a date.
   */
  public void testParseDateElementWhenElementTextCannotBeParsedIntoDate() {
    Element dateElement = this.createDateElement();
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
   * returns a <code>Double</code>.
   */
  public void testParseDoubleElement() {
    Element doubleElement = this.createDoubleElement();
    doubleElement.setTextContent("99.88");

    assertEquals("<Double>", new Double(99.88), this.parser
        .parseDoubleElement(doubleElement));
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
   * returns an <code>Integer</code>.
   */
  public void testParseIntegerElement() {
    Element integerElement = this.createI4Element();
    integerElement.setTextContent("30");

    assertEquals("<Integer>", new Integer(30), this.parser
        .parseIntegerElement(integerElement));
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

  public void testParseMemberElement() {
  }

  public void testParseParameterElement() {
  }

  public void testParseParametersElement() {
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractXmlRpcParser#parseStringElement(Element)}</code>
   * returns a <code>String</code>.
   */
  public void testParseStringElement() {
    Element stringElement = this.createStringElement();
    stringElement.setTextContent("Some text");

    assertEquals("<String>", "Some text", this.parser
        .parseStringElement(stringElement));
  }

  public void testParseStructElement() {
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
   * <code>{@link AbstractXmlRpcParser#parseBooleanElement(Element)}</code>
   * returns a <code>Boolean</code>.
   */
  public void testParseValueElementWhenChildElementIsBoolean() {
    Element booleanElement = this.createBooleanElement();
    booleanElement.setTextContent("1");

    Element valueElement = this.createValueElement();
    valueElement.appendChild(booleanElement);

    assertEquals("<Boolean>", Boolean.TRUE, this.parser
        .parseValueElement(valueElement));
  }

}
