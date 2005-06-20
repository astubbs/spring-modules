/* 
 * Created on Jun 20, 2005
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
package org.springmodules.remoting.xmlrpc.support;

import junit.framework.TestCase;

/**
 * <p>
 * Unit Tests for <code>{@link XmlRpcRequest}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/06/20 22:51:43 $
 */
public class XmlRpcRequestTests extends TestCase {

  /**
   * Primary object that is under test.
   */
  private XmlRpcRequest request;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case to construct.
   */
  public XmlRpcRequestTests(String name) {
    super(name);
  }

  /**
   * Sets up the test fixture.
   */
  protected void setUp() throws Exception {
    super.setUp();

    this.request = new XmlRpcRequest();
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcRequest#setServiceAndMethodNames(String)}</code> sets
   * the names of the service and the method from the given String.
   */
  public void testSetServiceAndMethodNames() {
    String serviceName = "myService";
    String methodName = "myMethod";
    String serviceAndMethodNames = serviceName + "." + methodName;

    this.request.setServiceAndMethodNames(serviceAndMethodNames);
    assertEquals("<Service name>", serviceName, this.request.getServiceName());
    assertEquals("<Method name>", methodName, this.request.getMethodName());
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcRequest#setServiceAndMethodNames(String)}</code>
   * throws a <code>{@link IllegalArgumentException}</code> if the given
   * String does not have a dot separating the name of the service from the name
   * of the method.
   */
  public void testSetServiceAndMethodNamesWhenTextDoesNotHaveDot() {
    try {
      this.request.setServiceAndMethodNames("serviceAndMethod");
      fail("A 'IllegalArgumentException' should have been thrown");

    } catch (IllegalArgumentException exception) {
      // we are expecting this exception.
    }
  }
}
