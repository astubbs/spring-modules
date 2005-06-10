/* 
 * Created on Jun 6, 2005
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

import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.TestCase;

import org.springmodules.remoting.xmlrpc.util.Iso8601DateTimeFormat;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * <p>
 * Template for test cases for <code>{@link AbstractDomXmlRpcParser}</code> or
 * any of its subclasses.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/06/10 01:49:28 $
 */
public class AbstractDomXmlRpcParserTests extends TestCase {

  /**
   * Formats/parses dates.
   */
  private Iso8601DateTimeFormat dateTimeFormat;

  /**
   * A DOM document used to create elements.
   */
  protected Document document;

  /**
   * Constructor.
   */
  public AbstractDomXmlRpcParserTests() {
    super();
  }

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case to construct.
   */
  public AbstractDomXmlRpcParserTests(String name) {
    super(name);
  }

  /**
   * Creates a new element having "array" as its tag name.
   * 
   * @return the created element.
   */
  protected final Element createArrayElement() {
    return this.document.createElement("array");
  }

  /**
   * Creates a new element having "array" as its tag name.
   * 
   * @param dataElement
   *          "data" element to be added as child of the created element.
   * @return the created element.
   */
  protected final Element createArrayElement(Element dataElement) {
    Element arrayElement = this.createArrayElement();
    arrayElement.appendChild(dataElement);
    return arrayElement;
  }

  /**
   * Creates a new element having "base64" as its tag name.
   * 
   * @return the created element.
   */
  protected final Element createBase64Element() {
    return this.document.createElement("base64");
  }

  /**
   * Creates a new element having "base64" as its tag name.
   * 
   * @param text
   *          the text of the created element.
   * @return the created element.
   */
  protected final Element createBase64Element(String text) {
    Element base64Element = this.createBase64Element();
    base64Element.setTextContent(text);
    return base64Element;
  }

  /**
   * Creates a new element having "boolean" as its tag name.
   * 
   * @return the created element.
   */
  protected final Element createBooleanElement() {
    return this.document.createElement("boolean");
  }

  /**
   * Creates a new element having "boolean" as its tag name.
   * 
   * @param value
   *          the <code>Boolean</code> value to set as text of the created
   *          element.
   * @return the created element.
   */
  protected final Element createBooleanElement(Boolean value) {
    String text = value.equals(Boolean.TRUE) ? "1" : "0";
    Element booleanElement = this.createBooleanElement(text);
    return booleanElement;
  }

  /**
   * Creates a new element having "boolean" as its tag name.
   * 
   * @param text
   *          the text of the created element.
   * @return the created element.
   */
  protected final Element createBooleanElement(String text) {
    Element booleanElement = this.createBooleanElement();
    booleanElement.setTextContent(text);
    return booleanElement;
  }

  /**
   * Creates a new element having "data" as its tag name.
   * 
   * @return the created element.
   */
  protected final Element createDataElement() {
    return this.document.createElement("data");
  }

  /**
   * Creates a new element having "dateTime.iso8601" as its tag name.
   * 
   * @return the created element.
   */
  protected final Element createDateTimeElement() {
    return this.document.createElement("dateTime.iso8601");
  }

  /**
   * Creates a new element having "dateTime.iso8601" as its tag name.
   * 
   * @param date
   *          the <code>java.util.Date</code> to set as text of the created
   *          element.
   * @return the created element.
   */
  protected final Element createDateTimeElement(Date date) {
    String text = this.dateTimeFormat.format(date);
    return this.createDateTimeElement(text);
  }

  /**
   * Creates a new element having "dateTime.iso8601" as its tag name.
   * 
   * @param text
   *          the text of the created element.
   * @return the created element.
   */
  protected final Element createDateTimeElement(String text) {
    Element dateTimeElement = this.createDateTimeElement();
    dateTimeElement.setTextContent(text);
    return dateTimeElement;
  }

  /**
   * Creates a new element having "double" as its tag name.
   * 
   * @return the created element.
   */
  protected final Element createDoubleElement() {
    return this.document.createElement("double");
  }

  /**
   * Creates a new element having "double" as its tag name.
   * 
   * @param doubleValue
   *          the <code>Double</code> to set as text of the created element.
   * @return the created element.
   */
  protected final Element createDoubleElement(Double doubleValue) {
    return this.createDoubleElement(doubleValue.toString());
  }

  /**
   * Creates a new element having "double" as its tag name.
   * 
   * @param text
   *          the text of the created element.
   * @return the created element.
   */
  protected final Element createDoubleElement(String text) {
    Element doubleElement = this.createDoubleElement();
    doubleElement.setTextContent(text);
    return doubleElement;
  }

  /**
   * Creates a new element having "i4" as its tag name.
   * 
   * @return the created element.
   */
  protected final Element createI4Element() {
    return this.document.createElement("i4");
  }

  /**
   * Creates a new element having "i4" as its tag name.
   * 
   * @param intValue
   *          the <code>Integer</code> to set as text of the created element.
   * @return the created element.
   */
  protected final Element createI4Element(Integer intValue) {
    return this.createI4Element(intValue.toString());
  }

  /**
   * Creates a new element having "i4" as its tag name.
   * 
   * @param text
   *          the text of the created element.
   * @return the created element.
   */
  protected final Element createI4Element(String text) {
    Element i4Element = this.createI4Element();
    i4Element.setTextContent(text);
    return i4Element;
  }

  /**
   * Creates a new element having "int" as its tag name.
   * 
   * @return the created element.
   */
  protected final Element createIntElement() {
    return this.document.createElement("int");
  }

  /**
   * Creates a new element having "int" as its tag name.
   * 
   * @param intValue
   *          the <code>Integer</code> to set as text of the created element.
   * @return the created element.
   */
  protected final Element createIntElement(Integer intValue) {
    Element intElement = this.createIntElement(intValue.toString());
    return intElement;
  }

  /**
   * Creates a new element having "int" as its tag name.
   * 
   * @param text
   *          the text of the created element.
   * @return the created element.
   */
  protected final Element createIntElement(String text) {
    Element intElement = this.createIntElement();
    intElement.setTextContent(text);
    return intElement;
  }

  /**
   * Creates a new element having "member" as its tag name.
   * 
   * @return the created element.
   */
  protected final Element createMemberElement() {
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
  protected final Element createMemberElement(Element nameElement,
      Element valueElement) {
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
  protected final Element createNameElement(String text) {
    Element nameElement = this.document.createElement("name");
    nameElement.setTextContent(text);
    return nameElement;
  }

  /**
   * Creates a new element having "param" as its tag name.
   * 
   * @return the created element.
   */
  protected final Element createParamElement() {
    return this.document.createElement("param");
  }

  /**
   * Creates a new element having "param" as its tag name.
   * 
   * @param valueElement
   *          element to be added as child as the created element.
   * @return the created element.
   */
  protected final Element createParamElement(Element valueElement) {
    Element paramElement = this.createParamElement();
    paramElement.appendChild(valueElement);
    return paramElement;
  }

  /**
   * Creates a new element having "params" as its tag name.
   * 
   * @return the created element.
   */
  protected final Element createParamsElement() {
    return this.document.createElement("params");
  }

  /**
   * Creates a new element having "string" as its tag name.
   * 
   * @return the created element.
   */
  protected final Element createStringElement() {
    return this.document.createElement("string");
  }

  /**
   * Creates a new element having "string" as its tag name.
   * 
   * @param text
   *          the text of the created element.
   * @return the created element.
   */
  protected final Element createStringElement(String text) {
    Element stringElement = this.createStringElement();
    stringElement.setTextContent(text);
    return stringElement;
  }

  /**
   * Creates a new element having "struct" as its tag name.
   * 
   * @return the created element.
   */
  protected final Element createStructElement() {
    return this.document.createElement("struct");
  }

  /**
   * Creates a new element having "value" as its tag name.
   * 
   * @return the created element.
   */
  protected Element createValueElement() {
    return this.document.createElement("value");
  }

  /**
   * Creates a new element having "value" as its tag name.
   * 
   * @param element
   *          element to be added as child as the created element.
   * @return the created element.
   */
  protected final Element createValueElement(Element element) {
    Element valueElement = this.createValueElement();
    valueElement.appendChild(element);
    return valueElement;
  }

  /**
   * Creates a new element having "value" as its tag name.
   * 
   * @param text
   *          the text of the created element.
   * @return the created element.
   */
  protected final Element createValueElement(String text) {
    Element valueElement = this.createValueElement();
    valueElement.setTextContent(text);
    return valueElement;
  }

  /**
   * Gives subclasses the opportunity to set up their own fixture.
   */
  protected void onSetUp() throws Exception {
    // subclasses may override this method.
  }

  /**
   * @see junit.framework.TestCase#setUp()
   */
  protected final void setUp() throws Exception {
    super.setUp();

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    this.document = builder.newDocument();

    this.dateTimeFormat = new Iso8601DateTimeFormat();

    this.onSetUp();
  }
}
