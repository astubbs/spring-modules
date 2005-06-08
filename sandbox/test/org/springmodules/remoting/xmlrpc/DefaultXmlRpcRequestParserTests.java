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

import java.lang.reflect.Method;

import org.easymock.classextension.MockClassControl;
import org.w3c.dom.Element;

/**
 * <p>
 * Unit Tests for <code>{@link  DefaultXmlRpcRequestParser}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/06/08 01:54:04 $
 */
public final class DefaultXmlRpcRequestParserTests extends
    AbstractXmlRpcParserTestCase {

  /**
   * Primary object that is under test.
   */
  private DefaultXmlRpcRequestParser parser;

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
  public DefaultXmlRpcRequestParserTests(String name) {
    super(name);
  }

  /**
   * Creates a new element having "methodCall" as its tag name.
   * 
   * @return the created element.
   */
  private Element createMethodCallElement() {
    return super.document.createElement("methodCall");
  }

  /**
   * Creates a new element having "methodName" as its tag name.
   * 
   * @param text
   *          the text of the created element.
   * @return the created element.
   */
  private Element createMethodNameElement(String text) {
    Element stringElement = super.document.createElement("methodName");
    stringElement.setTextContent(text);
    return stringElement;
  }

  /**
   * Sets up the test fixture.
   */
  protected void onSetUp() throws Exception {
    super.onSetUp();

    Class targetClass = DefaultXmlRpcRequestParser.class;

    Method parseParametersElementMethod = AbstractXmlRpcParser.class
        .getDeclaredMethod("parseParametersElement",
            new Class[] { Element.class });

    Method[] methodsToMock = { parseParametersElementMethod };

    this.parserControl = MockClassControl.createControl(targetClass, null,
        null, methodsToMock);

    this.parser = (DefaultXmlRpcRequestParser) this.parserControl
        .getMock();
  }

  /**
   * Verifies that the method
   * <code>{@link DefaultXmlRpcRequestParser#parseXmlRpcRequest(org.w3c.dom.Document)}</code>
   * creates a new <code>{@link XmlRpcRemoteInvocation}</code> from the
   * specified DOM document.
   */
  public void testParseXmlRpcRequest() throws Exception {
    String beanAndMethodNames = "bean.addUser";
    XmlRpcRemoteInvocationArguments invocationArguments = new XmlRpcRemoteInvocationArguments();
    invocationArguments.addArgument(new Integer(30), Integer.class);
    
    XmlRpcRemoteInvocation expectedInvocation = new XmlRpcRemoteInvocation(
        beanAndMethodNames, invocationArguments);
    
    Element methodNameElement = this
        .createMethodNameElement(beanAndMethodNames);
    Element paramsElement = super.createParamsElement();
    Element methodCallElement = this.createMethodCallElement();
    methodCallElement.appendChild(methodNameElement);
    methodCallElement.appendChild(paramsElement);

    super.document.appendChild(methodCallElement);

    // expectation: parse the "params" element.
    this.parser.parseParametersElement(paramsElement);
    this.parserControl.setReturnValue(invocationArguments);

    // set the state of the mock object to "replay".
    this.parserControl.replay();

    // execute the method to test.
    XmlRpcRemoteInvocation actualInvocation = this.parser
        .parseXmlRpcRequest(super.document);

    assertEquals("<XML-RPC remote invocation>", expectedInvocation,
        actualInvocation);

    // verify the expectations of the mock object were met.
    this.parserControl.verify();
  }
}
