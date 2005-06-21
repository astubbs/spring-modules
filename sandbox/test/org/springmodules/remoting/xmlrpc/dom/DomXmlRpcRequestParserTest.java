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
package org.springmodules.remoting.xmlrpc.dom;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.TestCase;

import org.easymock.classextension.MockClassControl;
import org.springmodules.remoting.xmlrpc.XmlRpcElementNames;
import org.springmodules.remoting.xmlrpc.support.XmlRpcElement;
import org.springmodules.remoting.xmlrpc.support.XmlRpcRequest;
import org.springmodules.remoting.xmlrpc.support.XmlRpcString;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * <p>
 * Unit Tests for <code>{@link DomXmlRpcRequestParser}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/06/21 08:56:08 $
 */
public class DomXmlRpcRequestParserTest extends TestCase {

  /**
   * A XML document to be parsed.
   */
  private Document document;

  /**
   * Primary object that is under test.
   */
  private DomXmlRpcRequestParser parser;

  /**
   * Controls the behavior of <code>{@link #parser}</code>.
   */
  private MockClassControl parserControl;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case to construct.
   */
  public DomXmlRpcRequestParserTest(String name) {
    super(name);
  }

  /**
   * Sets up the test fixture.
   */
  protected void setUp() throws Exception {
    super.setUp();

    Class targetClass = DomXmlRpcRequestParser.class;

    Method loadXmlDocumentMethod = AbstractDomXmlRpcParser.class
        .getDeclaredMethod("loadXmlDocument", new Class[] { InputStream.class });

    Method parseParametersElementMethod = AbstractDomXmlRpcParser.class
        .getDeclaredMethod("parseParametersElement",
            new Class[] { Element.class });

    Method[] methodsToMock = { loadXmlDocumentMethod,
        parseParametersElementMethod };

    this.parserControl = MockClassControl.createControl(targetClass, null,
        null, methodsToMock);

    this.parser = (DomXmlRpcRequestParser) this.parserControl.getMock();

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    this.document = builder.newDocument();
  }

  /**
   * Verifies that the method
   * <code>{@link DomXmlRpcRequestParser#parseRequest(InputStream)}</code>
   * creates a <code>{@link XmlRpcRequest}</code> by parsing the given
   * <code>InputStream</code> containing the XML-RPC request.
   */
  public void testParseRequest() {
    String serviceName = "myService";
    String methodName = "myMethod";

    Element methodCallElement = this.document.createElement("methodCall");
    this.document.appendChild(methodCallElement);

    Element methodNameElement = this.document
        .createElement(XmlRpcElementNames.METHOD_NAME);
    methodNameElement.setTextContent(serviceName + "." + methodName);
    methodCallElement.appendChild(methodNameElement);

    Element parametersElement = this.document
        .createElement(XmlRpcElementNames.PARAMS);
    methodCallElement.appendChild(parametersElement);

    InputStream inputStream = new ByteArrayInputStream(new byte[0]);

    XmlRpcElement[] parameters = { new XmlRpcString("Luke") };
    XmlRpcRequest expected = new XmlRpcRequest();
    expected.setMethodName(methodName);
    expected.setParameters(parameters);
    expected.setServiceName(serviceName);

    // expectation: parse the XML this.document.
    this.parser.loadXmlDocument(inputStream);
    this.parserControl.setReturnValue(this.document);

    // expectation: parse the parameters.
    this.parser.parseParametersElement(parametersElement);
    this.parserControl.setReturnValue(parameters);

    // set the state of the mock object to "replay".
    this.parserControl.replay();

    // execute the method to test.
    XmlRpcRequest actual = this.parser.parseRequest(inputStream);

    assertEquals("<XML-RPC request>", expected, actual);

    // verify the expectations of the mock object were met.
    this.parserControl.verify();
  }

}
