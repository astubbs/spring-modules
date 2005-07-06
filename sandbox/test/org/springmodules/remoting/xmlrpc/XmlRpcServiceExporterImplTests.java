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

import java.util.Arrays;

import junit.framework.TestCase;

import org.easymock.MockControl;
import org.springframework.remoting.support.RemoteInvocation;
import org.springmodules.remoting.xmlrpc.support.XmlRpcElement;
import org.springmodules.remoting.xmlrpc.support.XmlRpcRequest;
import org.springmodules.remoting.xmlrpc.support.XmlRpcString;

/**
 * <p>
 * Unit Tests for <code>{@link XmlRpcServiceExporterImpl}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.3 $ $Date: 2005/07/06 13:06:41 $
 */
public class XmlRpcServiceExporterImplTests extends TestCase {

  /**
   * Simulates a service to export.
   */
  private MyService myService;

  /**
   * Controls the behavior of <code>{@link #myService}</code>.
   */
  private MockControl myServiceControl;

  /**
   * Primary object that is under test.
   */
  private XmlRpcServiceExporterImpl serviceExporter;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case to construct.
   */
  public XmlRpcServiceExporterImplTests(String name) {
    super(name);
  }

  /**
   * Sets up the test fixture.
   */
  protected void setUp() throws Exception {
    super.setUp();

    this.serviceExporter = new XmlRpcServiceExporterImpl();

    Class serviceInterface = MyService.class;

    this.myServiceControl = MockControl.createControl(serviceInterface);
    this.myService = (MyService) this.myServiceControl.getMock();

    this.serviceExporter.setService(this.myService);
    this.serviceExporter.setServiceInterface(serviceInterface);
    this.serviceExporter.afterPropertiesSet();
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcServiceExporterImpl#findMatchingMethod(XmlRpcRequest)}</code>
   * finds a matching method if the method name and parameters of the given
   * XML-RPC request match a method of the service.
   */
  public void testFindMatchingMethodWithMatchingMethodWithArguments() {
    Long customerId = new Long(542);

    String methodName = "getCustomerName";
    XmlRpcElement[] parameters = { new XmlRpcString(customerId.toString()) };

    XmlRpcRequest request = new XmlRpcRequest();
    request.setMethodName(methodName);
    request.setParameters(parameters);

    RemoteInvocation remoteInvocation = this.serviceExporter
        .findMatchingMethod(request);

    assertEquals("<Method name>", methodName, remoteInvocation.getMethodName());

    Class[] expectedParameterTypes = { Long.class };
    Class[] actualParameterTypes = remoteInvocation.getParameterTypes();
    assertTrue("<Parameter Types>. Expected: '"
        + Arrays.toString(expectedParameterTypes) + "' but was: '"
        + Arrays.toString(actualParameterTypes), Arrays.equals(
        expectedParameterTypes, actualParameterTypes));

    Object[] expectedArguments = { customerId };
    Object[] actualArguments = remoteInvocation.getArguments();

    assertTrue("<Arguments>. Expected: '" + Arrays.toString(expectedArguments)
        + "' but was: '" + Arrays.toString(actualArguments), Arrays.equals(
        expectedArguments, actualArguments));
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcServiceExporterImpl#findMatchingMethod(XmlRpcRequest)}</code>
   * throws a <code>{@link XmlRpcMethodNotFoundException}</code> if the method
   * name of the given XML-RPC request does not match any method name of the
   * service.
   */
  public void testFindMatchingMethodWithNotMatchingMethodName() {
    XmlRpcRequest request = new XmlRpcRequest();
    request.setMethodName("notExistingMethod");

    try {
      this.serviceExporter.findMatchingMethod(request);
      fail("A 'XmlRpcMethodNotFoundException' should have been thrown");

    } catch (XmlRpcMethodNotFoundException exception) {
      // we are expecting this exception.
    }
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcServiceExporterImpl#findMatchingMethod(XmlRpcRequest)}</code>
   * throws a <code>{@link XmlRpcMethodNotFoundException}</code> if the number
   * of parameters of the given XML-RPC request does not match the number of
   * parameters of the service method with matching name.
   */
  public void testFindMatchingMethodWithNotMatchingParameterCount() {
    XmlRpcRequest request = new XmlRpcRequest();
    request.setMethodName("getCustomerName");

    XmlRpcElement[] parameters = { new XmlRpcString("Luke"),
        new XmlRpcString("Anakin") };
    request.setParameters(parameters);

    try {
      this.serviceExporter.findMatchingMethod(request);
      fail("A 'XmlRpcMethodNotFoundException' should have been thrown");

    } catch (XmlRpcMethodNotFoundException exception) {
      // we are expecting this exception.
    }
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcServiceExporterImpl#findMatchingMethod(XmlRpcRequest)}</code>
   * throws a <code>{@link XmlRpcMethodNotFoundException}</code> if the type
   * of the parameters of the given XML-RPC request does not match the type of
   * the parameters of the service method with matching name.
   */
  public void testFindMatchingMethodWithNotMatchingParameterType() {
    XmlRpcRequest request = new XmlRpcRequest();
    request.setMethodName("getCustomerName");

    XmlRpcElement[] parameters = { new XmlRpcString("Luke") };
    request.setParameters(parameters);

    try {
      this.serviceExporter.findMatchingMethod(request);
      fail("A 'XmlRpcMethodNotFoundException' should have been thrown");

    } catch (XmlRpcMethodNotFoundException exception) {
      // we are expecting this exception.
    }
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcServiceExporterImpl#invoke(XmlRpcRequest)}</code>
   * executes a method of the exported service.
   */
  public void testInvokeXmlRpcRequest() throws Exception {
    Long argument = new Long(54);
    String customerName = "Luke";

    String methodName = "getCustomerName";
    XmlRpcElement[] parameters = { new XmlRpcString(argument.toString()) };

    XmlRpcRequest request = new XmlRpcRequest();
    request.setMethodName(methodName);
    request.setParameters(parameters);
    
    RemoteInvocation invocation = new RemoteInvocation();
    invocation.setMethodName("getCustomerName");
    invocation.setParameterTypes(new Class[] { Long.class });
    invocation.setArguments(new Object[] { argument });

    // expectation: call the service method.
    this.myService.getCustomerName(argument);
    this.myServiceControl.setReturnValue(customerName);

    // set the status of the mock object to "replay"
    this.myServiceControl.replay();

    // execute the method to test.
    Object result = this.serviceExporter.invoke(request);

    assertEquals("<Result>", customerName, result);

    // verify the expectations of the mock objects were met.
    this.myServiceControl.verify();
  }
}
