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

import org.w3c.dom.Element;

/**
 * <p>
 * Unit Tests for <code>{@link  DefaultXmlRpcServerRequestParser}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/06/07 04:40:00 $
 */
public final class DefaultXmlRpcServerRequestParserTests extends
    AbstractXmlRpcParserTestCase {

  /**
   * Primary object that is under test.
   */
  private DefaultXmlRpcServerRequestParser parser;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case to construct.
   */
  public DefaultXmlRpcServerRequestParserTests(String name) {
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

    this.parser = new DefaultXmlRpcServerRequestParser();
  }

  /**
   * Verifies that the method
   * <code>{@link DefaultXmlRpcServerRequestParser#parseXmlRpcServerRequest(org.w3c.dom.Document)}</code>
   * creates a new <code>{@link XmlRpcRemoteInvocation}</code> from the
   * specified DOM document.
   */
  public void testParseXmlRpcServerRequest() {
    String beanName = "bean";
    String methodName = "addUser";
    Integer argument = new Integer(30);

    XmlRpcRemoteInvocation expectedInvocation = new XmlRpcRemoteInvocation();
    expectedInvocation.setBeanName(beanName);
    expectedInvocation.setMethodName(methodName);
    expectedInvocation.setParameterTypes(new Class[] { argument.getClass() });
    expectedInvocation.setArguments(new Object[] { argument });

    Element methodNameElement = this.createMethodNameElement(beanName + "."
        + methodName);

    Element intElement = super.createIntElement(argument);
    Element valueElement = super.createValueElement(intElement);
    Element paramElement = super.createParamElement(valueElement);
    Element paramsElement = super.createParamsElement();
    paramsElement.appendChild(paramElement);

    Element methodCallElement = this.createMethodCallElement();
    methodCallElement.appendChild(methodNameElement);
    methodCallElement.appendChild(paramsElement);

    super.document.appendChild(methodCallElement);

    XmlRpcRemoteInvocation actualInvocation = this.parser
        .parseXmlRpcServerRequest(super.document);

    assertEquals("<XML-RPC remote invocation>", expectedInvocation,
        actualInvocation);
  }
}
