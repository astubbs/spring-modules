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
package org.springmodules.xmlrpc.server.apache;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;

import junit.framework.TestCase;

import org.apache.xmlrpc.XmlRpcServer;
import org.easymock.classextension.MockClassControl;
import org.springframework.mock.web.DelegatingServletInputStream;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * <p>
 * Unit Tests for <code>{@link ServletApacheXmlRpcServiceExporter}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.2 $ $Date: 2005/06/03 09:34:39 $
 */
public class ServletApacheXmlRpcServiceExporterTests extends TestCase {

  /**
   * Simulates a <code>{@link ServletOutputStream}</code>.
   */
  private class MockServletOutputStream extends ServletOutputStream {
    /**
     * Content set by <code>{@link #write(byte[])}</code>.
     */
    byte[] content;

    /**
     * Indicates if this OutputStream was flushed.
     */
    boolean flushed;

    /**
     * @see java.io.OutputStream#flush()
     */
    public void flush() {
      this.flushed = true;
    }

    /**
     * @see java.io.OutputStream#write(byte[])
     */
    public void write(byte[] newContent) {
      this.content = newContent;
    }

    /**
     * @see java.io.OutputStream#write(int)
     */
    public void write(int newContent) {
      // do nothing
    }
  }

  /**
   * Primary object that is under test.
   */
  private ServletApacheXmlRpcServiceExporter exporter;

  /**
   * Mock object that simulates a <code>{@link ServletOutputStream}</code>.
   */
  private MockServletOutputStream outputStream;

  /**
   * Mock object that simulates a
   * <code>{@link javax.servlet.http.HttpServletRequest}</code>
   */
  private MockHttpServletRequest request;

  /**
   * Mock object that simulates a
   * <code>{@link javax.servlet.http.HttpServletResponse}</code>
   */
  private MockHttpServletResponse response;

  /**
   * Mock object that simulates a <code>{@link XmlRpcServer}</code>
   */
  private XmlRpcServer xmlRpcServer;

  /**
   * Controls the behavior of <code>{@link #xmlRpcServer}</code>
   */
  private MockClassControl xmlRpcServerControl;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case to construct.
   */
  public ServletApacheXmlRpcServiceExporterTests(String name) {
    super(name);
  }

  /**
   * Sets up the test fixture.
   */
  protected void setUp() throws Exception {
    super.setUp();

    this.exporter = new ServletApacheXmlRpcServiceExporter();
    this.request = new MockHttpServletRequest() {
      private static final long serialVersionUID = 3257565105352161589L;

      ServletInputStream inputStream;

      public ServletInputStream getInputStream() {
        return this.inputStream;
      }

      public void setContent(byte[] content) {
        this.inputStream = new DelegatingServletInputStream(
            new ByteArrayInputStream(content));
      }
    };

    this.outputStream = new MockServletOutputStream();
    final ServletOutputStream servletOutputStream = this.outputStream;

    this.response = new MockHttpServletResponse() {
      public ServletOutputStream getOutputStream() {
        return servletOutputStream;
      }
    };

    Class targetClass = XmlRpcServer.class;
    Method executeMethod = targetClass.getDeclaredMethod("execute",
        new Class[] { InputStream.class });
    Method[] methodsToMock = new Method[] { executeMethod };
    this.xmlRpcServerControl = MockClassControl.createControl(targetClass,
        null, null, methodsToMock);
    this.xmlRpcServer = (XmlRpcServer) this.xmlRpcServerControl.getMock();

    this.exporter.setXmlRpcServer(this.xmlRpcServer);
  }

  /**
   * Verifies that the method
   * <code>{@link ServletApacheXmlRpcServiceExporter#readXmlRpcRequest(javax.servlet.http.HttpServletRequest)}</code>
   * delegates the XML-RPC method call to
   * <code>{@link XmlRpcServer#execute(java.io.InputStream)}</code> sending as
   * argument the value obtained from
   * <code>{@link javax.servlet.ServletRequest#getInputStream()}</code>.
   */
  public void testReadXmlRpcRequest() throws Exception {
    byte[] expectedResult = new byte[] { 0, 0, 6 };
    byte[] requestContent = new byte[] { 0, 2, 6 };
    this.request.setContent(requestContent);
    InputStream expectedStream = this.request.getInputStream();

    // expectation: call execute(InputStream)
    this.xmlRpcServer.execute(expectedStream);
    this.xmlRpcServerControl.setReturnValue(expectedResult);

    // set the state of the mock object to "replay".
    this.xmlRpcServerControl.replay();

    // execute the method to test.
    byte[] actualResult = this.exporter.readXmlRpcRequest(this.request);

    assertSame("<Result>", expectedResult, actualResult);

    // verify the expectations of the mock object were met.
    this.xmlRpcServerControl.verify();
  }

  /**
   * Verifies that the method
   * <code>{@link ServletApacheXmlRpcServiceExporter#writeXmlRpcResult(javax.servlet.http.HttpServletResponse, byte[])}</code>
   * writes the result of XML-RPC method call to the response.
   */
  public void testWriteXmlRpcResult() throws Exception {
    byte[] expectedResult = new byte[] { 0, 0, 6 };

    // execute the method to test.
    this.exporter.writeXmlRpcResult(this.response, expectedResult);

    assertEquals("<Response content type>", "text/xml", this.response
        .getContentType());
    assertEquals("<Response content size>", expectedResult.length,
        this.response.getContentLength());

    assertSame("<OutputStream content>", expectedResult,
        this.outputStream.content);

    assertTrue("The OutputStream should have been flushed",
        this.outputStream.flushed);
  }

}
