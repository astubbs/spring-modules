/* 
 * Created on Jun 21, 2005
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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
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
import org.springmodules.remoting.xmlrpc.support.XmlRpcInteger;
import org.springmodules.remoting.xmlrpc.support.XmlRpcRequest;
import org.springmodules.remoting.xmlrpc.support.XmlRpcString;
import org.springmodules.remoting.xmlrpc.support.XmlRpcStruct;
import org.springmodules.remoting.xmlrpc.support.XmlRpcStruct.XmlRpcMember;

/**
 * <p>
 * Template for testing implementations of
 * <code>{@link XmlRpcRequestParser}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/06/23 01:48:09 $
 */
public abstract class AbstractXmlRpcRequestParserTests extends TestCase {

  /**
   * Message logger.
   */
  protected final Log logger = LogFactory.getLog(this.getClass());

  /**
   * Name of the method specified in the XML-RPC request.
   */
  private String methodName;

  /**
   * Primary object that is under test.
   */
  private XmlRpcRequestParser parser;

  /**
   * Contains the names of the service and method separated by a dot.
   */
  private String serviceAndMethodNames;

  /**
   * Name of the service specified in the XML-RPC request.
   */
  private String serviceName;

  /**
   * Constructor.
   */
  public AbstractXmlRpcRequestParserTests() {
    super();
  }

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case to construct.
   */
  public AbstractXmlRpcRequestParserTests(String name) {
    super(name);
  }

  /**
   * Returns the implementation of <code>{@link XmlRpcRequestParser}</code> to
   * test.
   * 
   * @return the primary object that is under test.
   */
  protected abstract XmlRpcRequestParser getXmlRpcRequestParserImplementation();

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
    this.parser = this.getXmlRpcRequestParserImplementation();
    this.serviceName = "myService";
    this.methodName = "myMethod";
    this.serviceAndMethodNames = this.serviceName + "." + this.methodName;
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcRequestParser#parseRequest(InputStream)}</code> parses
   * correctly a XML-RPC request containing array parameters.
   */
  public final void testParseRequestWithArrayParameters() {
    XmlRpcArray xmlRpcArray = new XmlRpcArray();
    xmlRpcArray.add(new XmlRpcString("Luke"));
    xmlRpcArray.add(new XmlRpcString("Leia"));

    XmlRpcElement[] parameters = { xmlRpcArray };

    XmlRpcRequest expected = new XmlRpcRequest(this.serviceName,
        this.methodName, parameters);

    StringBuffer builder = new StringBuffer();
    builder.append("<?xml version=\"1.0\"?>");
    builder.append("<methodCall><methodName>");
    builder.append(this.serviceAndMethodNames);
    builder.append("</methodName><params><param><value><array><data>");

    XmlRpcElement[] elements = xmlRpcArray.getElements();
    int memberCount = elements.length;
    for (int i = 0; i < memberCount; i++) {
      XmlRpcString element = (XmlRpcString) elements[i];

      builder.append("<value>");
      builder.append(element.getValue());
      builder.append("</value>");
    }

    builder.append("</data></array></value></param></params></methodCall>");

    String request = builder.toString();
    this.logger.info("XML-RPC request: " + request);

    InputStream inputStream = new ByteArrayInputStream(request.getBytes());
    XmlRpcRequest actual = this.parser.parseRequest(inputStream);

    assertEquals("<XML-RPC request>", expected, actual);
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcRequestParser#parseRequest(InputStream)}</code> parses
   * correctly a XML-RPC request containing base64 parameters.
   */
  public final void testParseRequestWithBase64Parameters() {
    byte[] value = new byte[] { 8, 94, 6 };
    byte[] encodedValue = Base64.encodeBase64(value);

    XmlRpcBase64 xmlRpcBase64 = new XmlRpcBase64(value);
    XmlRpcElement[] parameters = { xmlRpcBase64 };

    XmlRpcRequest expected = new XmlRpcRequest(this.serviceName,
        this.methodName, parameters);

    StringBuffer builder = new StringBuffer();
    builder.append("<?xml version=\"1.0\"?>");
    builder.append("<methodCall><methodName>");
    builder.append(this.serviceAndMethodNames);
    builder.append("</methodName><params><param><value><base64>");
    builder.append(new String(encodedValue, 0, encodedValue.length));
    builder.append("</base64></value></param></params></methodCall>");

    String request = builder.toString();
    this.logger.info("XML-RPC request: " + request);

    InputStream inputStream = new ByteArrayInputStream(request.getBytes());
    XmlRpcRequest actual = this.parser.parseRequest(inputStream);

    assertEquals("<XML-RPC request>", expected, actual);
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcRequestParser#parseRequest(InputStream)}</code> parses
   * correctly a XML-RPC request containing boolean parameters.
   */
  public final void testParseRequestWithBooleanParameters() {
    XmlRpcBoolean[] parameters = { new XmlRpcBoolean(Boolean.TRUE),
        new XmlRpcBoolean(Boolean.FALSE) };

    XmlRpcRequest expected = new XmlRpcRequest(this.serviceName,
        this.methodName, parameters);

    StringBuffer builder = new StringBuffer();
    builder.append("<?xml version=\"1.0\"?>");
    builder.append("<methodCall><methodName>");
    builder.append(this.serviceAndMethodNames);
    builder.append("</methodName><params>");

    int parameterCount = parameters.length;
    for (int i = 0; i < parameterCount; i++) {
      XmlRpcBoolean xmlRpcBoolean = parameters[i];
      Boolean value = (Boolean) xmlRpcBoolean.getValue();

      builder.append("<param><value><boolean>");
      builder.append(value.booleanValue() ? "1" : "0");
      builder.append("</boolean></value></param>");
    }

    builder.append("</params></methodCall>");

    String request = builder.toString();
    this.logger.info("XML-RPC request: " + request);

    InputStream inputStream = new ByteArrayInputStream(request.getBytes());
    XmlRpcRequest actual = this.parser.parseRequest(inputStream);

    assertEquals("<XML-RPC request>", expected, actual);
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcRequestParser#parseRequest(InputStream)}</code> parses
   * correctly a XML-RPC request containing date/time parameters.
   */
  public final void testParseRequestWithDateTimeParameters() {
    DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HH:mm:ss");

    Calendar calendar = new GregorianCalendar();
    calendar.set(Calendar.MILLISECOND, 0);

    Date value = calendar.getTime();
    XmlRpcDateTime xmlRpcDateTime = new XmlRpcDateTime(value);
    XmlRpcElement[] parameters = { xmlRpcDateTime };

    XmlRpcRequest expected = new XmlRpcRequest(this.serviceName,
        this.methodName, parameters);

    StringBuffer builder = new StringBuffer();
    builder.append("<?xml version=\"1.0\"?>");
    builder.append("<methodCall><methodName>");
    builder.append(this.serviceAndMethodNames);
    builder.append("</methodName><params><param><value><dateTime.iso8601>");
    builder.append(dateFormat.format(value));
    builder.append("</dateTime.iso8601></value></param></params></methodCall>");

    String request = builder.toString();
    this.logger.info("XML-RPC request: " + request);

    InputStream inputStream = new ByteArrayInputStream(request.getBytes());
    XmlRpcRequest actual = this.parser.parseRequest(inputStream);

    assertEquals("<XML-RPC request>", expected, actual);
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcRequestParser#parseRequest(InputStream)}</code> parses
   * correctly a XML-RPC request containing double-precision signed floating
   * point numbers as parameters.
   */
  public final void testParseRequestWithDoubleParameters() {
    Double value = new Double(782.99);

    XmlRpcDouble xmlRpcDouble = new XmlRpcDouble(value);
    XmlRpcElement[] parameters = { xmlRpcDouble };

    XmlRpcRequest expected = new XmlRpcRequest(this.serviceName,
        this.methodName, parameters);

    StringBuffer builder = new StringBuffer();
    builder.append("<?xml version=\"1.0\"?>");
    builder.append("<methodCall><methodName>");
    builder.append(this.serviceAndMethodNames);
    builder.append("</methodName><params><param><value><double>");
    builder.append(value.toString());
    builder.append("</double></value></param></params></methodCall>");

    String request = builder.toString();
    this.logger.info("XML-RPC request: " + request);

    InputStream inputStream = new ByteArrayInputStream(request.getBytes());
    XmlRpcRequest actual = this.parser.parseRequest(inputStream);

    assertEquals("<XML-RPC request>", expected, actual);
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcRequestParser#parseRequest(InputStream)}</code> parses
   * correctly a XML-RPC request containing four-byte signed integer parameters.
   */
  public final void testParseRequestWithIntegerParameters() {
    XmlRpcInteger[] parameters = { new XmlRpcInteger(new Integer(54)),
        new XmlRpcInteger(new Integer(438)) };

    XmlRpcRequest expected = new XmlRpcRequest(this.serviceName,
        this.methodName, parameters);

    StringBuffer builder = new StringBuffer();
    builder.append("<?xml version=\"1.0\"?>");
    builder.append("<methodCall><methodName>");
    builder.append(this.serviceAndMethodNames);
    builder.append("</methodName><params>");

    int parameterCount = parameters.length;
    for (int i = 0; i < parameterCount; i++) {
      XmlRpcInteger xmlRpcInteger = parameters[i];
      Integer value = (Integer) xmlRpcInteger.getValue();

      // even parameters will have "i4" as name, odd parameters will have "int"
      // as name.
      String elementName = (i % 2 == 0) ? "i4" : "int";

      builder.append("<param><value><");
      builder.append(elementName);
      builder.append(">");
      builder.append(value.toString());
      builder.append("</");
      builder.append(elementName);
      builder.append("></value></param>");
    }

    builder.append("</params></methodCall>");

    String request = builder.toString();
    this.logger.info("XML-RPC request: " + request);

    InputStream inputStream = new ByteArrayInputStream(request.getBytes());
    XmlRpcRequest actual = this.parser.parseRequest(inputStream);

    assertEquals("<XML-RPC request>", expected, actual);
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcRequestParser#parseRequest(InputStream)}</code> parses
   * correctly a XML-RPC request containing string parameters.
   */
  public final void testParseRequestWithStringParameters() {
    XmlRpcString[] parameters = { new XmlRpcString("Hoth"),
        new XmlRpcString("Endor") };

    XmlRpcRequest expected = new XmlRpcRequest(this.serviceName,
        this.methodName, parameters);

    StringBuffer builder = new StringBuffer();
    builder.append("<?xml version=\"1.0\"?>");
    builder.append("<methodCall><methodName>");
    builder.append(this.serviceAndMethodNames);
    builder.append("</methodName><params>");

    int parameterCount = parameters.length;
    for (int i = 0; i < parameterCount; i++) {
      XmlRpcString xmlRpcString = parameters[i];
      String value = (String) xmlRpcString.getValue();

      // even parameters will have an extra "string" element.
      boolean addStringElement = (i % 2 == 0);

      builder.append("<param><value>");
      if (addStringElement) {
        builder.append("<string>");
      }
      builder.append(value);
      if (addStringElement) {
        builder.append("</string>");
      }
      builder.append("</value></param>");
    }

    builder.append("</params></methodCall>");

    String request = builder.toString();
    this.logger.info("XML-RPC request: " + request);

    InputStream inputStream = new ByteArrayInputStream(request.getBytes());
    XmlRpcRequest actual = this.parser.parseRequest(inputStream);

    assertEquals("<XML-RPC request>", expected, actual);
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcRequestParser#parseRequest(InputStream)}</code> parses
   * correctly a XML-RPC request containing struct parameters.
   */
  public final void testParseRequestWithStructParameters() {
    XmlRpcStruct xmlRpcStruct = new XmlRpcStruct();
    xmlRpcStruct.add("name", new XmlRpcString("Darth Vader"));
    xmlRpcStruct.add("role", new XmlRpcString("Sith lord"));

    XmlRpcElement[] parameters = { xmlRpcStruct };

    XmlRpcRequest expected = new XmlRpcRequest(this.serviceName,
        this.methodName, parameters);

    StringBuffer builder = new StringBuffer();
    builder.append("<?xml version=\"1.0\"?>");
    builder.append("<methodCall><methodName>");
    builder.append(this.serviceAndMethodNames);
    builder.append("</methodName><params><param><value><struct>");

    XmlRpcMember[] members = xmlRpcStruct.getMembers();
    int memberCount = members.length;
    for (int i = 0; i < memberCount; i++) {
      XmlRpcMember member = members[i];
      XmlRpcString value = (XmlRpcString) member.value;

      builder.append("<member><name>");
      builder.append(member.name);
      builder.append("</name><value>");
      builder.append(value.getValue());
      builder.append("</value></member>");
    }

    builder.append("</struct></value></param></params></methodCall>");

    String request = builder.toString();
    this.logger.info("XML-RPC request: " + request);

    InputStream inputStream = new ByteArrayInputStream(request.getBytes());
    XmlRpcRequest actual = this.parser.parseRequest(inputStream);

    assertEquals("<XML-RPC request>", expected, actual);
  }
}
