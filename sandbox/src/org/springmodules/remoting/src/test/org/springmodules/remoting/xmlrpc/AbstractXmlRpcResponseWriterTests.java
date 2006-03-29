/* 
 * Created on Jun 23, 2005
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import junit.framework.TestCase;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springmodules.remoting.xmlrpc.support.XmlRpcArray;
import org.springmodules.remoting.xmlrpc.support.XmlRpcBase64;
import org.springmodules.remoting.xmlrpc.support.XmlRpcBoolean;
import org.springmodules.remoting.xmlrpc.support.XmlRpcDateTime;
import org.springmodules.remoting.xmlrpc.support.XmlRpcDouble;
import org.springmodules.remoting.xmlrpc.support.XmlRpcElement;
import org.springmodules.remoting.xmlrpc.support.XmlRpcFault;
import org.springmodules.remoting.xmlrpc.support.XmlRpcInteger;
import org.springmodules.remoting.xmlrpc.support.XmlRpcResponse;
import org.springmodules.remoting.xmlrpc.support.XmlRpcString;
import org.springmodules.remoting.xmlrpc.support.XmlRpcStruct;
import org.springmodules.remoting.xmlrpc.support.XmlRpcStruct.XmlRpcMember;

/**
 * <p>
 * Template for testing implementations of
 * <code>{@link XmlRpcResponseWriter}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public abstract class AbstractXmlRpcResponseWriterTests extends TestCase {

  /**
   * Message logger.
   */
  protected final Log logger = LogFactory.getLog(this.getClass());

  /**
   * Primary object that is under test.
   */
  private XmlRpcResponseWriter writer;

  /**
   * Constructor.
   */
  public AbstractXmlRpcResponseWriterTests() {
    super();
  }

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case to construct.
   */
  public AbstractXmlRpcResponseWriterTests(String name) {
    super(name);
  }

  /**
   * Returns the implementation of <code>{@link XmlRpcResponseWriter}</code>
   * to test.
   * 
   * @return the primary object that is under test.
   */
  protected abstract XmlRpcResponseWriter getXmlRpcResponseWriterImplementation();

  /**
   * Give subclasses the opportunity to set up their own test fixture.
   */
  protected void onSetUp() throws Exception {
    // subclasses may override this method.
  }

  /**
   * Sets up the test fixture.
   */
  protected final void setUp() throws Exception {
    super.setUp();

    this.onSetUp();
    this.writer = this.getXmlRpcResponseWriterImplementation();
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcResponseWriter#writeResponse(XmlRpcResponse)}</code>
   * creates the XML-RPC response correctly if the response contains a fault.
   */
  public final void testWriteResponseWithFault() {
    int faultCode = 8453;
    String faultMessage = "A fault";

    XmlRpcFault xmlRpcFault = new XmlRpcFault(faultCode, faultMessage);

    XmlRpcResponse response = new XmlRpcResponse(xmlRpcFault);

    StringBuffer builder = new StringBuffer(256);
    builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    builder
        .append("<methodResponse><fault><struct><member><name>faultCode</name><value><i4>");
    builder.append(faultCode);
    builder
        .append("</i4></value></member><member><name>faultString</name><value><string>");
    builder.append(faultMessage);
    builder
        .append("</string></value></member></struct></fault></methodResponse>");

    String expected = builder.toString();

    byte[] actualResponse = this.writer.writeResponse(response);
    String actual = new String(actualResponse, 0, actualResponse.length);

    this.logger.debug(response);
    this.logger.debug("XML-RPC response: " + actual);

    assertEquals("<XML-RPC response>", expected, actual);

  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcResponseWriter#writeResponse(XmlRpcResponse)}</code>
   * creates the XML-RPC response correctly if the response contains array
   * parameters.
   */
  public final void testWriteResponseWithArrayParameters() {
    XmlRpcString firstValue = new XmlRpcString("Darth Vader");
    XmlRpcString secondValue = new XmlRpcString("Darth Sidius");

    XmlRpcArray xmlRpcArray = new XmlRpcArray();
    xmlRpcArray.add(firstValue);
    xmlRpcArray.add(secondValue);

    XmlRpcElement[] parameters = { xmlRpcArray };
    XmlRpcResponse response = new XmlRpcResponse(parameters);

    StringBuffer builder = new StringBuffer(256);
    builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    builder
        .append("<methodResponse><params><param><value><array><data><value><string>");
    builder.append(firstValue.getValue());
    builder.append("</string></value><value><string>");
    builder.append(secondValue.getValue());
    builder
        .append("</string></value></data></array></value></param></params></methodResponse>");

    String expected = builder.toString();

    byte[] actualResponse = this.writer.writeResponse(response);
    String actual = new String(actualResponse, 0, actualResponse.length);

    this.logger.debug(response);
    this.logger.debug("XML-RPC response: " + actual);

    assertEquals("<XML-RPC response>", expected, actual);
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcResponseWriter#writeResponse(XmlRpcResponse)}</code>
   * creates the XML-RPC response correctly if the response contains base64
   * parameters.
   */
  public final void testWriteResponseWithBase64Parameters() {
    byte[] value = { 4, 6, 99, 5 };
    byte[] encodedValue = Base64.encodeBase64(value);
    XmlRpcBase64 xmlRpcBase64 = new XmlRpcBase64(value);

    XmlRpcElement[] parameters = { xmlRpcBase64 };
    XmlRpcResponse response = new XmlRpcResponse(parameters);

    StringBuffer builder = new StringBuffer(256);
    builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    builder.append("<methodResponse><params><param><value><base64>");
    builder.append(new String(encodedValue, 0, encodedValue.length));
    builder.append("</base64></value></param></params></methodResponse>");

    String expected = builder.toString();

    byte[] actualResponse = this.writer.writeResponse(response);
    String actual = new String(actualResponse, 0, actualResponse.length);

    this.logger.debug(response);
    this.logger.debug("XML-RPC response: " + actual);

    assertEquals("<XML-RPC response>", expected, actual);
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcResponseWriter#writeResponse(XmlRpcResponse)}</code>
   * creates the XML-RPC response correctly if the response contains boolean
   * parameters.
   */
  public final void testWriteResponseWithBooleanParameters() {
    XmlRpcBoolean[] parameters = { new XmlRpcBoolean(Boolean.TRUE),
        new XmlRpcBoolean(Boolean.FALSE) };

    XmlRpcResponse response = new XmlRpcResponse(parameters);

    StringBuffer builder = new StringBuffer();
    builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    builder.append("<methodResponse><params>");

    int parameterCount = parameters.length;
    for (int i = 0; i < parameterCount; i++) {
      XmlRpcBoolean xmlRpcBoolean = parameters[i];
      Boolean value = (Boolean) xmlRpcBoolean.getValue();

      builder.append("<param><value><boolean>");
      builder.append(value.booleanValue() ? "1" : "0");
      builder.append("</boolean></value></param>");
    }

    builder.append("</params></methodResponse>");
    String expected = builder.toString();

    byte[] actualResponse = this.writer.writeResponse(response);
    String actual = new String(actualResponse, 0, actualResponse.length);

    this.logger.debug(response);
    this.logger.debug("XML-RPC response: " + actual);

    assertEquals("<XML-RPC response>", expected, actual);
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcResponseWriter#writeResponse(XmlRpcResponse)}</code>
   * creates the XML-RPC response correctly if the response contains date/time
   * parameters.
   */
  public final void testWriteResponseWithDateTimeParameters() {
    DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HH:mm:ss");

    Calendar calendar = new GregorianCalendar();
    calendar.set(Calendar.MILLISECOND, 0);

    Date value = calendar.getTime();
    XmlRpcDateTime xmlRpcDateTime = new XmlRpcDateTime(value);
    XmlRpcElement[] parameters = { xmlRpcDateTime };

    XmlRpcResponse response = new XmlRpcResponse(parameters);

    StringBuffer builder = new StringBuffer(256);
    builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    builder.append("<methodResponse><params><param><value><dateTime.iso8601>");
    builder.append(dateFormat.format(value));
    builder
        .append("</dateTime.iso8601></value></param></params></methodResponse>");

    String expected = builder.toString();

    byte[] actualResponse = this.writer.writeResponse(response);
    String actual = new String(actualResponse, 0, actualResponse.length);

    this.logger.debug(response);
    this.logger.debug("XML-RPC response: " + actual);

    assertEquals("<XML-RPC response>", expected, actual);
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcResponseWriter#writeResponse(XmlRpcResponse)}</code>
   * creates the XML-RPC response correctly if the response contains
   * double-precision signed floating point numbers as parameters.
   */
  public final void testWriteResponseWithDoubleParameters() {
    Double value = new Double(782.99);

    XmlRpcDouble xmlRpcDouble = new XmlRpcDouble(value);
    XmlRpcElement[] parameters = { xmlRpcDouble };

    XmlRpcResponse response = new XmlRpcResponse(parameters);

    StringBuffer builder = new StringBuffer(256);
    builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    builder.append("<methodResponse><params><param><value><double>");
    builder.append(value);
    builder.append("</double></value></param></params></methodResponse>");

    String expected = builder.toString();

    byte[] actualResponse = this.writer.writeResponse(response);
    String actual = new String(actualResponse, 0, actualResponse.length);

    this.logger.debug(response);
    this.logger.debug("XML-RPC response: " + actual);

    assertEquals("<XML-RPC response>", expected, actual);
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcResponseWriter#writeResponse(XmlRpcResponse)}</code>
   * creates the XML-RPC response correctly if the response contains four-byte
   * signed integer parameters.
   */
  public final void testWriteResponseWithIntegerParameters() {
    Integer value = new Integer(54);

    XmlRpcInteger xmlRpcInteger = new XmlRpcInteger(value);
    XmlRpcElement[] parameters = { xmlRpcInteger };

    XmlRpcResponse response = new XmlRpcResponse(parameters);

    StringBuffer builder = new StringBuffer(256);
    builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    builder.append("<methodResponse><params><param><value><i4>");
    builder.append(value);
    builder.append("</i4></value></param></params></methodResponse>");

    String expected = builder.toString();

    byte[] actualResponse = this.writer.writeResponse(response);
    String actual = new String(actualResponse, 0, actualResponse.length);

    this.logger.debug(response);
    this.logger.debug("XML-RPC response: " + actual);

    assertEquals("<XML-RPC response>", expected, actual);
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcResponseWriter#writeResponse(XmlRpcResponse)}</code>
   * creates the XML-RPC response correctly if the response contains string
   * parameters.
   */
  public final void testWriteResponseWithStringParameters() {
    String value = "X-Wing";

    XmlRpcString xmlRpcString = new XmlRpcString(value);
    XmlRpcElement[] parameters = { xmlRpcString };

    XmlRpcResponse response = new XmlRpcResponse(parameters);

    StringBuffer builder = new StringBuffer(256);
    builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    builder.append("<methodResponse><params><param><value><string>");
    builder.append(value);
    builder.append("</string></value></param></params></methodResponse>");

    String expected = builder.toString();

    byte[] actualResponse = this.writer.writeResponse(response);
    String actual = new String(actualResponse, 0, actualResponse.length);

    this.logger.debug(response);
    this.logger.debug("XML-RPC response: " + actual);

    assertEquals("<XML-RPC response>", expected, actual);
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcResponseWriter#writeResponse(XmlRpcResponse)}</code>
   * creates the XML-RPC response correctly if the response contains struct
   * parameters.
   */
  public final void testWriteResponseWithStructParameters() {
    XmlRpcMember integerMember = new XmlRpcMember("id", new XmlRpcInteger(
        new Integer(56)));
    XmlRpcMember stringMember = new XmlRpcMember("fullName", new XmlRpcString(
        "Luke Skywalker"));

    XmlRpcStruct xmlRpcStruct = new XmlRpcStruct();
    xmlRpcStruct.add(integerMember);
    xmlRpcStruct.add(stringMember);

    XmlRpcElement[] parameters = { xmlRpcStruct };
    XmlRpcResponse response = new XmlRpcResponse(parameters);

    StringBuffer builder = new StringBuffer(256);
    builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    builder
        .append("<methodResponse><params><param><value><struct><member><name>");
    builder.append(integerMember.name);
    builder.append("</name><value><i4>");
    builder.append(((XmlRpcInteger) integerMember.value).getValue());
    builder.append("</i4></value></member><member><name>");
    builder.append(stringMember.name);
    builder.append("</name><value><string>");
    builder.append(((XmlRpcString) stringMember.value).getValue());
    builder
        .append("</string></value></member></struct></value></param></params></methodResponse>");

    String expected = builder.toString();

    byte[] actualResponse = this.writer.writeResponse(response);
    String actual = new String(actualResponse, 0, actualResponse.length);

    this.logger.debug(response);
    this.logger.debug("XML-RPC response: " + actual);

    assertEquals("<XML-RPC response>", expected, actual);
  }
}
