/* 
 * Created on Jul 7, 2005
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletInputStream;

import org.easymock.MockControl;
import org.springframework.mock.web.DelegatingServletInputStream;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springmodules.remoting.xmlrpc.support.XmlRpcElementFactory;
import org.springmodules.remoting.xmlrpc.support.XmlRpcRequest;
import org.springmodules.remoting.xmlrpc.support.XmlRpcResponse;
import org.springmodules.remoting.xmlrpc.support.XmlRpcString;

import junit.framework.TestCase;

/**
 * <p>
 * Unit Tests for <code>{@link XmlRpcServiceRouter}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public class XmlRpcServiceRouterTests extends TestCase {

  /**
   * Mock implementation of the HttpServletRequest interface.
   */
  private class CustomMockHttpServletRequest extends MockHttpServletRequest {

    ServletInputStream inputStream;

    public ServletInputStream getInputStream() {
      return this.inputStream;
    }
  }

  /**
   * Mock object that simulates a
   * <code>{@link javax.servlet.http.HttpServletRequest}</code>
   */
  private CustomMockHttpServletRequest request;

  /**
   * Mock object that simulates a
   * <code>{@link javax.servlet.http.HttpServletResponse}</code>.
   */
  private MockHttpServletResponse response;

  /**
   * Name of the service to export.
   */
  private String serviceName;

  /**
   * Mock object that simulates a <code>{@link XmlRpcElementFactory}</code>.
   */
  private XmlRpcElementFactory xmlRpcElementFactory;

  /**
   * Controls the behavior of <code>{@link #xmlRpcElementFactory}</code>.
   */
  private MockControl xmlRpcElementFactoryControl;

  /**
   * Mock object that simulates a <code>{@link XmlRpcRequestParser}</code>.
   */
  private XmlRpcRequestParser xmlRpcRequestParser;

  /**
   * Controls the behavior of <code>{@link #xmlRpcRequestParser}</code>.
   */
  private MockControl xmlRpcRequestParserControl;

  /**
   * Mock object that simulates a <code>{@link XmlRpcResponseWriter}</code>.
   */
  private XmlRpcResponseWriter xmlRpcResponseWriter;

  /**
   * Controls the behavior of <code>{@link #xmlRpcResponseWriter}</code>.
   */
  private MockControl xmlRpcResponseWriterControl;

  /**
   * Mock object that simulates a <code>{@link XmlRpcServiceExporter}</code>.
   */
  private XmlRpcServiceExporter xmlRpcServiceExporter;

  /**
   * Controls the behavior of <code>{@link #xmlRpcServiceExporter}</code>.
   */
  private MockControl xmlRpcServiceExporterControl;

  /**
   * Primary object that is under test.
   */
  private XmlRpcServiceRouter XmlRpcServiceRouter;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case to construct.
   */
  public XmlRpcServiceRouterTests(String name) {
    super(name);
  }

  /**
   * Sets up the test fixture.
   */
  protected void setUp() throws Exception {
    super.setUp();
    this.XmlRpcServiceRouter = new XmlRpcServiceRouter();
  }

  /**
   * Sets up all the mock objects.
   */
  private void setUpMockObjects() {
    this.request = new CustomMockHttpServletRequest();
    this.response = new MockHttpServletResponse();

    this.xmlRpcElementFactoryControl = MockControl
        .createControl(XmlRpcElementFactory.class);
    this.xmlRpcElementFactory = (XmlRpcElementFactory) this.xmlRpcElementFactoryControl
        .getMock();
    this.XmlRpcServiceRouter.setXmlRpcElementFactory(this.xmlRpcElementFactory);

    this.xmlRpcRequestParserControl = MockControl
        .createControl(XmlRpcRequestParser.class);
    this.xmlRpcRequestParser = (XmlRpcRequestParser) this.xmlRpcRequestParserControl
        .getMock();
    this.XmlRpcServiceRouter.setRequestParser(this.xmlRpcRequestParser);

    this.xmlRpcResponseWriterControl = MockControl
        .createControl(XmlRpcResponseWriter.class);
    this.xmlRpcResponseWriter = (XmlRpcResponseWriter) this.xmlRpcResponseWriterControl
        .getMock();
    this.XmlRpcServiceRouter.setResponseWriter(this.xmlRpcResponseWriter);

    this.xmlRpcServiceExporterControl = MockControl
        .createControl(XmlRpcServiceExporter.class);
    this.xmlRpcServiceExporter = (XmlRpcServiceExporter) this.xmlRpcServiceExporterControl
        .getMock();

    // set up the exported service.
    this.serviceName = "service";
    Map exportedServices = new HashMap();
    exportedServices.put(this.serviceName, this.xmlRpcServiceExporter);
    this.XmlRpcServiceRouter.setExportedServices(exportedServices);
  }

  /*
   * Test method for
   * 'org.springmodules.remoting.xmlrpc.XmlRpcServiceRouter.afterPropertiesSet()'
   */
  public void testAfterPropertiesSet() {

  }

  /*
   * Test method for
   * 'org.springmodules.remoting.xmlrpc.XmlRpcServiceRouter.handleRequest(HttpServletRequest,
   * HttpServletResponse)'
   */
  public void testHandleRequest() throws Exception {
    this.setUpMockObjects();
    
    byte[] content = { 4, 6, 7 };
    ServletInputStream inputStream = new DelegatingServletInputStream(
        new ByteArrayInputStream(content));
    this.request.inputStream = inputStream;

    XmlRpcRequest xmlRpcRequest = new XmlRpcRequest();
    xmlRpcRequest.setServiceName(this.serviceName);

    // expectation: parse the XML-RPC request.
    this.xmlRpcRequestParser.parseRequest(inputStream);
    this.xmlRpcRequestParserControl.setReturnValue(xmlRpcRequest);

    String result = "Luke";
    // expectation: service exporter executes remote method.
    this.xmlRpcServiceExporter.invoke(xmlRpcRequest);
    this.xmlRpcServiceExporterControl.setReturnValue(result);

    XmlRpcString parameter = new XmlRpcString(result);
    // expectation: create a XML-RPC element from the result.
    this.xmlRpcElementFactory.createXmlRpcElement(result);
    this.xmlRpcElementFactoryControl.setReturnValue(parameter);

    byte[] serializedResponse = { 5, 7, 3 };
    // expectation: serialize the XML-RPC response.
    this.xmlRpcResponseWriter.writeResponse(new XmlRpcResponse(parameter));
    this.xmlRpcResponseWriterControl.setReturnValue(serializedResponse);

    // set the state of the mock controls to "replay".
    this.xmlRpcElementFactoryControl.replay();
    this.xmlRpcRequestParserControl.replay();
    this.xmlRpcResponseWriterControl.replay();
    this.xmlRpcServiceExporterControl.replay();

    // execute the method to test.
    ModelAndView modelAndView = this.XmlRpcServiceRouter.handleRequest(
        this.request, this.response);

    assertNull("<ModelAndView should be null>", modelAndView);
    assertEquals("<HTTP response content type>", "text/xml", this.response
        .getContentType());
    assertEquals("<HTTP response content length>", serializedResponse.length,
        this.response.getContentLength());

    // verify the HTTP response contains the XML-RPC response.
    byte[] responseContent = this.response.getContentAsByteArray();
    assertTrue("<HTTP response content>. Expected: "
        + Arrays.toString(serializedResponse) + " but was: "
        + Arrays.toString(responseContent), Arrays.equals(serializedResponse,
        responseContent));

    // verify that the expectations were met.
    this.xmlRpcElementFactoryControl.verify();
    this.xmlRpcRequestParserControl.verify();
    this.xmlRpcResponseWriterControl.verify();
    this.xmlRpcServiceExporterControl.verify();
  }

}
