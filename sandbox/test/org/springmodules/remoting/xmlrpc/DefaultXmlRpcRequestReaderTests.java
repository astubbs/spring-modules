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

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import junit.framework.TestCase;

/**
 * <p>
 * Unit Tests for <code>{@link DefaultXmlRpcRequestReader}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/06/08 01:54:05 $
 */
public class DefaultXmlRpcRequestReaderTests extends TestCase {

  /**
   * Primary object that is under test.
   */
  private DefaultXmlRpcRequestReader requestReader;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case to construct.
   */
  public DefaultXmlRpcRequestReaderTests(String name) {
    super(name);
  }

  /**
   * Sets up the test fixture.
   */
  protected void setUp() throws Exception {
    super.setUp();

    this.requestReader = new DefaultXmlRpcRequestReader();
  }

  /**
   * Verifies that the method
   * <code>{@link DefaultXmlRpcRequestReader#readXmlRpcRequest(InputStream)}</code>
   * creates a new <code>{@link XmlRpcRemoteInvocation}</code> from the
   * specified <code>InputStream</code> containing the XML-RPC request.
   */
  public void testReadXmlRpcRequest() throws Exception {
    this.requestReader.afterPropertiesSet();

    Object[] arguments = { new Integer(30), "AL" };
    String beanName = "bean";
    String methodName = "getUser";
    Class[] parameterTypes = { Integer.class, String.class };
    int i = 0;

    XmlRpcRemoteInvocation expectedInvocation = new XmlRpcRemoteInvocation();
    expectedInvocation.setArguments(arguments);
    expectedInvocation.setBeanName(beanName);
    expectedInvocation.setMethodName(methodName);
    expectedInvocation.setParameterTypes(parameterTypes);

    StringBuffer buffer = new StringBuffer();
    buffer.append("<?xml version=\"1.0\"?>");
    buffer.append("<methodCall><methodName>");
    buffer.append(beanName);
    buffer.append(".");
    buffer.append(methodName);
    buffer.append("</methodName>");
    buffer.append("<params><param><value><i4>");
    buffer.append(arguments[i++]);
    buffer.append("</i4></value></param><param><value><string>");
    buffer.append(arguments[i]);
    buffer.append("</string></value></param></params></methodCall>");

    String xmlRpcRequest = buffer.toString();
    InputStream inputStream = new ByteArrayInputStream(xmlRpcRequest.getBytes());

    XmlRpcRemoteInvocation actualInvocation = this.requestReader
        .readXmlRpcRequest(inputStream);

    assertEquals("<XML-RPC remote invocation>", expectedInvocation,
        actualInvocation);
  }

}
