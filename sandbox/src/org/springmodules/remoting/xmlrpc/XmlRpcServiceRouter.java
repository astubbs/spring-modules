/* 
 * Created on Jun 13, 2005
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

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springmodules.remoting.xmlrpc.dom.DomXmlRpcRequestParser;
import org.springmodules.remoting.xmlrpc.dom.DomXmlRpcResponseWriter;
import org.springmodules.remoting.xmlrpc.support.XmlRpcElement;
import org.springmodules.remoting.xmlrpc.support.XmlRpcElementFactory;
import org.springmodules.remoting.xmlrpc.support.XmlRpcElementFactoryImpl;
import org.springmodules.remoting.xmlrpc.support.XmlRpcFault;
import org.springmodules.remoting.xmlrpc.support.XmlRpcRequest;
import org.springmodules.remoting.xmlrpc.support.XmlRpcResponse;

/**
 * <p>
 * Spring MVC Controller that receives a XML-RPC request, sends it the service
 * specified in the request and writes the result of the execution to HTTP
 * response.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.4 $ $Date: 2005/07/15 12:36:16 $
 */
public class XmlRpcServiceRouter implements InitializingBean, Controller {

  /**
   * Registry of services to export. Methods of this objects will be callable
   * over XML-RPC as "servicename.methodname".
   */
  private Map exportedServices;

  /**
   * Message logger.
   */
  protected final Log logger = LogFactory.getLog(this.getClass());

  /**
   * Parses the XML-RPC request.
   */
  private XmlRpcRequestParser requestParser;

  /**
   * Writes the XML-RPC response.
   */
  private XmlRpcResponseWriter responseWriter;

  /**
   * Creates implementations of <code>{@link XmlRpcElement}</code> from Java
   * objects.
   */
  private XmlRpcElementFactory xmlRpcElementFactory;

  /**
   * Constructor.
   */
  public XmlRpcServiceRouter() {
    super();
  }

  /**
   * @see InitializingBean#afterPropertiesSet()
   */
  public void afterPropertiesSet() {
    // validate the services to export.
    if (this.exportedServices == null || this.exportedServices.isEmpty()) {
      throw new IllegalArgumentException(
          "This router should have at least one service to export");
    }

    Iterator entrySetIterator = this.exportedServices.entrySet().iterator();
    while (entrySetIterator.hasNext()) {
      Map.Entry entry = (Map.Entry) entrySetIterator.next();

      Object entryValue = entry.getValue();
      if (!(entryValue instanceof XmlRpcServiceExporter)) {
        throw new IllegalArgumentException(
            "Services should be instances of 'XmlRpcServiceExporter'");
      }
    }

    if (this.requestParser == null) {
      this.requestParser = new DomXmlRpcRequestParser();
    }

    if (this.responseWriter == null) {
      this.responseWriter = new DomXmlRpcResponseWriter();
    }

    if (this.xmlRpcElementFactory == null) {
      this.xmlRpcElementFactory = new XmlRpcElementFactoryImpl();
    }
  }

  /**
   * Getter for field <code>{@link #requestParser}</code>.
   * 
   * @return the field <code>requestParser</code>.
   */
  protected final XmlRpcRequestParser getRequestParser() {
    return this.requestParser;
  }

  /**
   * Getter for field <code>{@link #responseWriter}</code>.
   * 
   * @return the field <code>responseWriter</code>.
   */
  protected final XmlRpcResponseWriter getResponseWriter() {
    return this.responseWriter;
  }

  /**
   * Getter for field <code>{@link #xmlRpcElementFactory}</code>.
   * 
   * @return the field <code>xmlRpcElementFactory</code>.
   */
  protected final XmlRpcElementFactory getXmlRpcElementFactory() {
    return this.xmlRpcElementFactory;
  }

  /**
   * @see Controller#handleRequest(HttpServletRequest, HttpServletResponse)
   */
  public ModelAndView handleRequest(HttpServletRequest request,
      HttpServletResponse response) {
    Object result = null;
    XmlRpcException xmlRpcException = null;

    try {
      InputStream requestInputStream = request.getInputStream();

      XmlRpcRequest xmlRpcRequest = this.requestParser
          .parseRequest(requestInputStream);

      String serviceName = xmlRpcRequest.getServiceName();
      if (this.logger.isDebugEnabled()) {
        this.logger.debug("Remote service: " + serviceName);
      }

      if (!this.exportedServices.containsKey(serviceName)) {
        throw new XmlRpcServiceNotFoundException("The service '" + serviceName
            + "' was not found");
      }

      XmlRpcServiceExporter serviceExporter = (XmlRpcServiceExporter) this.exportedServices
          .get(serviceName);
      result = serviceExporter.invoke(xmlRpcRequest);

      if (this.logger.isDebugEnabled()) {
        this.logger.debug("XML-RPC remote invocation result: " + result);
      }

    } catch (XmlRpcException exception) {
      xmlRpcException = exception;
      if (this.logger.isDebugEnabled()) {
        this.logger.debug("XML-RPC Exception", exception);
      }

    } catch (Exception exception) {
      if (this.logger.isDebugEnabled()) {
        this.logger.debug("Server Exception", exception);
      }
      xmlRpcException = new XmlRpcInternalException(
          "Server error. Internal xml-rpc error");
    }

    XmlRpcResponse xmlRpcResponse = null;

    if (xmlRpcException == null) {
      XmlRpcElement parameter = this.xmlRpcElementFactory
          .createXmlRpcElement(result);
      xmlRpcResponse = new XmlRpcResponse(parameter);

    } else {
      XmlRpcFault xmlRpcFault = new XmlRpcFault(xmlRpcException.getCode(),
          xmlRpcException.getMessage());

      xmlRpcResponse = new XmlRpcResponse(xmlRpcFault);
    }

    byte[] serializedResponse = this.responseWriter
        .writeResponse(xmlRpcResponse);

    response.setContentType("text/xml");
    response.setContentLength(serializedResponse.length);

    try {
      OutputStream output = response.getOutputStream();
      output.write(serializedResponse);
      output.flush();
    } catch (Exception exception) {
      if (this.logger.isDebugEnabled()) {
        this.logger.debug(
            "Unable to write result to request. Server I/O Exception.",
            exception);
      }

    }
    return null;
  }

  /**
   * Setter for the field <code>{@link #exportedServices}</code>.
   * 
   * @param exportedServices
   *          the new value to set.
   */
  public final void setExportedServices(Map exportedServices) {
    this.exportedServices = exportedServices;
  }

  /**
   * Setter for the field <code>{@link #requestParser}</code>.
   * 
   * @param requestParser
   *          the new value to set.
   */
  public final void setRequestParser(XmlRpcRequestParser requestParser) {
    this.requestParser = requestParser;
  }

  /**
   * Setter for the field <code>{@link #responseWriter}</code>.
   * 
   * @param responseWriter
   *          the new value to set.
   */
  public final void setResponseWriter(XmlRpcResponseWriter responseWriter) {
    this.responseWriter = responseWriter;
  }

  /**
   * Setter for the field <code>{@link #xmlRpcElementFactory}</code>.
   * 
   * @param xmlRpcElementFactory
   *          the new value to set.
   */
  public final void setXmlRpcElementFactory(
      XmlRpcElementFactory xmlRpcElementFactory) {
    this.xmlRpcElementFactory = xmlRpcElementFactory;
  }

}
