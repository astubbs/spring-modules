/* 
 * Created on Jun 9, 2005
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springmodules.remoting.xmlrpc.dom.DomXmlRpcRequestReader;
import org.springmodules.remoting.xmlrpc.util.Base64Handler;

/**
 * <p>
 * Template for tests for implementations of
 * <code>{@link XmlRpcRequestReader}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.5 $ $Date: 2005/06/14 00:47:21 $
 */
public abstract class AbstractXmlRpcRequestReaderTests extends TestCase {

  /**
   * Message logger.
   */
  protected final Log logger = LogFactory.getLog(this.getClass());

  /**
   * Primary object that is under test.
   */
  private XmlRpcRequestReader requestReader;

  /**
   * Handles base64 values.
   */
  private Base64Handler base64Handler;
  
  /**
   * Constructor.
   */
  public AbstractXmlRpcRequestReaderTests() {
    super();
  }

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case to construct.
   */
  public AbstractXmlRpcRequestReaderTests(String name) {
    super(name);
  }

  /**
   * <p>
   * Creates a new XML element having "array" as its tag name. The created XML
   * element is wrapped by a "value" XML element.
   * </p>
   * <p>
   * This method assumes the all the elements of the list are
   * <code>Integer</code>.
   * </p>
   * 
   * @param value
   *          the value to be set as text of the element.
   * @return the String representation of the created XML element.
   * @see #createValueElement(Object)
   */
  protected String createArrayElement(List value) {
    StringBuffer buffer = new StringBuffer();

    buffer.append("<array><data>");

    int size = value.size();
    for (int i = 0; i < size; i++) {
      Integer item = (Integer) value.get(i);
      buffer.append(this.createI4Element(item));
    }

    buffer.append("</data></array>");

    return this.createValueElement(buffer.toString());
  }

  /**
   * Creates a new XML element having "base64" as its tag name. The created XML
   * element is wrapped by a "value" XML element.
   * 
   * @param value
   *          the value to be set as text of the element.
   * @return the String representation of the created XML element.
   * @see #createValueElement(Object)
   */
  protected String createBase64Element(byte[] value) {
    String base64Element = "<base64>" + this.base64Handler.format(value) + "</base64>";
    return this.createValueElement(base64Element);
  }

  /**
   * Creates a new XML element having "boolean" as its tag name. The created XML
   * element is wrapped by a "value" XML element.
   * 
   * @param value
   *          the value to be set as text of the element.
   * @return the String representation of the created XML element.
   * @see #createValueElement(Object)
   */
  protected String createBooleanElement(Boolean value) {
    String booleanElement = "<boolean>" + (value.booleanValue() ? "1" : "0")
        + "</boolean>";
    return this.createValueElement(booleanElement);
  }

  /**
   * Creates a new XML element having "double" as its tag name. The created XML
   * element is wrapped by a "value" XML element.
   * 
   * @param value
   *          the value to be set as text of the element.
   * @return the String representation of the created XML element.
   * @see #createValueElement(Object)
   */
  protected String createDoubleElement(Double value) {
    String doubleElement = "<double>" + value.toString() + "</double>";
    return this.createValueElement(doubleElement);
  }

  /**
   * Creates a new XML element having "i4" as its tag name. The created XML
   * element is wrapped by a "value" XML element.
   * 
   * @param value
   *          the value to be set as text of the element.
   * @return the String representation of the created XML element.
   * @see #createValueElement(Object)
   */
  protected String createI4Element(Integer value) {
    String integerElement = "<i4>" + value + "</i4>";
    return this.createValueElement(integerElement);
  }

  /**
   * Creates a new XML element having "string" as its tag name. The created XML
   * element is wrapped by a "value" XML element.
   * 
   * @param value
   *          the value to be set as text of the element.
   * @return the String representation of the created XML element.
   * @see #createValueElement(Object)
   */
  protected String createStringElement(String value) {
    String stringElement = "<string>" + value + "</string>";
    return this.createValueElement(stringElement);
  }

  protected String createStructElement(Map value) {
    StringBuffer buffer = new StringBuffer();

    buffer.append("<struct>");

    Iterator entrySetIterator = value.entrySet().iterator();
    while (entrySetIterator.hasNext()) {
      buffer.append("<member><name>");
      Map.Entry entry = (Map.Entry) entrySetIterator.next();
      buffer.append(entry.getKey());
      buffer.append("</name>");
      buffer.append(this.createStringElement(entry.getValue().toString()));
      buffer.append("</member>");
    }

    buffer.append("</struct>");

    return this.createValueElement(buffer.toString());
  }

  /**
   * Creates a new XML element having "value" as its tag name.
   * 
   * @param value
   *          the value to be set as text of the element.
   * @return the String representation of the created XML element.
   */
  protected String createValueElement(Object value) {
    return "<value>" + value + "</value>";
  }

  /**
   * Returns the object we are going to test.
   * 
   * @return the object we are going to test.
   */
  protected abstract XmlRpcRequestReader getXmlRpcRequestReader();

  /**
   * Give subclasses the opportunity to set up their own fixture.F
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
    this.requestReader = this.getXmlRpcRequestReader();
    this.base64Handler = new Base64Handler();
  }

  /**
   * Verifies that the method
   * <code>{@link DomXmlRpcRequestReader#readXmlRpcRequest(InputStream)}</code>
   * creates a new <code>{@link XmlRpcRemoteInvocation}</code> from the
   * specified <code>InputStream</code> containing the XML-RPC request.
   */
  public void testReadXmlRpcRequest() throws Exception {
    List arrayParameter = new ArrayList(Arrays.asList(new Object[] {
        new Integer(10), new Integer(45) }));
    byte[] base64Parameter = new byte[] { 43, 65, 6 };
    Boolean booleanParameter = Boolean.TRUE;
    Double doubleParameter = new Double(885.09);
    Integer i4Parameter = new Integer(55);
    String stringParameter = "Tie Fighter";
    Map structParameter = new HashMap();
    structParameter.put("firstName", "Leia");
    structParameter.put("lastName", "Organa");
    structParameter.put("role", "Princess");
    String valueParameter = "Death Star";

    Object[] arguments = { arrayParameter, base64Parameter, booleanParameter,
        doubleParameter, i4Parameter, stringParameter, valueParameter,
        structParameter };
    String beanName = "bean";
    String methodName = "getUser";
    Class[] parameterTypes = { List.class, byte[].class, Boolean.class,
        Double.class, Integer.class, String.class, String.class, Map.class };

    XmlRpcRemoteInvocation expectedInvocation = new XmlRpcRemoteInvocation();
    expectedInvocation.setArguments(arguments);
    expectedInvocation.setServiceName(beanName);
    expectedInvocation.setMethodName(methodName);
    expectedInvocation.setParameterTypes(parameterTypes);

    StringBuffer buffer = new StringBuffer();
    buffer.append("<?xml version=\"1.0\"?>");
    buffer.append("<methodCall><methodName>");
    buffer.append(beanName);
    buffer.append(".");
    buffer.append(methodName);
    buffer.append("</methodName><params><param>");
    buffer.append(this.createArrayElement(arrayParameter));
    buffer.append("</param><param>");
    buffer.append(this.createBase64Element(base64Parameter));
    buffer.append("</param><param>");
    buffer.append(this.createBooleanElement(booleanParameter));
    buffer.append("</param><param>");
    buffer.append(this.createDoubleElement(doubleParameter));
    buffer.append("</param><param>");
    buffer.append(this.createI4Element(i4Parameter));
    buffer.append("</param><param>");
    buffer.append(this.createStringElement(stringParameter));
    buffer.append("</param><param>");
    buffer.append(this.createValueElement(valueParameter));
    buffer.append("</param><param>");
    buffer.append(this.createStructElement(structParameter));
    buffer.append("</param></params></methodCall>");

    String xmlRpcRequest = buffer.toString();
    if (this.logger.isDebugEnabled()) {
      this.logger.debug(xmlRpcRequest);
    }

    InputStream inputStream = new ByteArrayInputStream(xmlRpcRequest.getBytes());

    long startTime = System.currentTimeMillis();

    XmlRpcRemoteInvocation actualInvocation = this.requestReader
        .readXmlRpcRequest(inputStream);

    long endTime = System.currentTimeMillis();
    if (this.logger.isDebugEnabled()) {
      this.logger.debug("Execution time: " + (endTime - startTime) + " ms");
    }

    assertEquals("<XML-RPC remote invocation>", expectedInvocation,
        actualInvocation);
  }
}
