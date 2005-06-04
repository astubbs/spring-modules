/* 
 * Created on Jun 3, 2005
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
package org.springmodules.xmlrpc.server;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;

import org.easymock.MockControl;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * Unit Tests for <code>{@link XmlRpcServiceController}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/06/04 01:20:20 $
 */
public final class XmlRpcServiceControllerTests extends TestCase {

  /**
   * Primary object that is under test.
   */
  private XmlRpcServiceController controller;

  /**
   * Mock object that simulates a
   * <code>{@link ServletXmlRpcServiceExporter}</code>.
   */
  private ServletXmlRpcServiceExporter xmlRpcServiceExporter;

  /**
   * Controls the behavior of <code>{@link #xmlRpcServiceExporter}</code>.
   */
  private MockControl xmlRpcServiceExporterControl;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case to construct.
   */
  public XmlRpcServiceControllerTests(String name) {
    super(name);
  }

  /**
   * Sets up the test fixture.
   */
  protected void setUp() throws Exception {
    super.setUp();

    this.xmlRpcServiceExporterControl = MockControl
        .createControl(ServletXmlRpcServiceExporter.class);
    this.xmlRpcServiceExporter = (ServletXmlRpcServiceExporter) this.xmlRpcServiceExporterControl
        .getMock();

    this.controller = new XmlRpcServiceController();
    this.controller.setXmlRpcServiceExporter(this.xmlRpcServiceExporter);
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcServiceController#afterPropertiesSet()}</code> does
   * not throw any exception if <code>XmlRcpServiceController</code> contains
   * a <code>{@link ServletXmlRpcServiceExporter}</code> not equal to
   * <code>null</code>.
   */
  public void testAfterPropertiesSetWhenXmlRcpServiceExporterIsNotNull() {
    this.controller.afterPropertiesSet();
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcServiceController#afterPropertiesSet()}</code> throws
   * a <code>{@link BeanCreationException}</code> if the
   * <code>XmlRcpServiceController</code> contains a
   * <code>{@link ServletXmlRpcServiceExporter}</code> equal to
   * <code>null</code>.
   */
  public void testAfterPropertiesSetWhenXmlRcpServiceExporterIsEqualToNull() {
    this.controller.setXmlRpcServiceExporter(null);
    try {
      this.controller.afterPropertiesSet();
      fail("A 'BeanCreationException' should have been thrown");
    } catch (BeanCreationException exception) {
      // we are expecting this exception.
    }
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcServiceController#handleRequest(HttpServletRequest, HttpServletResponse)}</code>
   * calls the XML-RCP method specified in the request and writes the result in
   * the response.
   */
  public void testHandleRequest() throws Exception {
    HttpServletRequest request = new MockHttpServletRequest();
    HttpServletResponse response = new MockHttpServletResponse();

    byte[] result = new byte[] { 0, 0, 2 };

    // expectation: call the XML-RPC method.
    this.xmlRpcServiceExporter.readXmlRpcRequest(request);
    this.xmlRpcServiceExporterControl.setReturnValue(result);

    // expectation: save the result in the response.
    this.xmlRpcServiceExporter.writeXmlRpcResult(response, result);

    // set the state of the mock object to "replay".
    this.xmlRpcServiceExporterControl.replay();

    // execute the method to test.
    ModelAndView modelAndView = this.controller
        .handleRequest(request, response);

    assertNull("The ModelAndView should be null", modelAndView);

    // verify the expectations of the mock object were met.
    this.xmlRpcServiceExporterControl.verify();
  }

}
