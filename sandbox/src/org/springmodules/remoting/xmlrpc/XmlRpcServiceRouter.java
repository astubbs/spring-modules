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
import org.springmodules.util.Strings;

/**
 * <p>
 * Spring MVC Controller that receives a XML-RPC request, sends it the service
 * specified in such request and writes the result of the execution to the HTTP
 * response.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.6 $ $Date: 2005/09/25 05:20:00 $
 */
public class XmlRpcServiceRouter implements InitializingBean, Controller {

  /**
   * Registry of services to export. Methods of this objects will be callable
   * over XML-RPC as "servicename.methodname".
   */
  private Map exportedServices;

  protected final Log logger = LogFactory.getLog(getClass());

  /**
   * <p>
   * Parses the XML-RPC request.
   * </p>
   * <p>
   * If not set, a default <code>{@link DomXmlRpcRequestParser}</code> will be
   * created.
   * </p>
   */
  private XmlRpcRequestParser requestParser;

  /**
   * <p>
   * Writes the XML-RPC response.
   * </p>
   * <p>
   * If not set, a default <code>{@link DomXmlRpcResponseWriter}</code> will
   * be created.
   * </p>
   */
  private XmlRpcResponseWriter responseWriter;

  /**
   * <p>
   * Creates implementations of <code>{@link XmlRpcElement}</code> from Java
   * objects.
   * </p>
   * <p>
   * If not set, a default <code>{@link XmlRpcElementFactoryImpl}</code> will
   * be created.
   * </p>
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
    if (exportedServices == null || exportedServices.isEmpty()) {
      throw new IllegalArgumentException(
          "This router should have at least one service to export");
    }

    for (Iterator i = exportedServices.entrySet().iterator(); i.hasNext();) {
      Map.Entry entry = (Map.Entry) i.next();

      Object entryValue = entry.getValue();
      if (!(entryValue instanceof XmlRpcServiceExporter)) {
        throw new IllegalArgumentException(
            "Services should be instances of 'XmlRpcServiceExporter'");
      }
    }

    if (requestParser == null) {
      requestParser = new DomXmlRpcRequestParser();
    }

    if (responseWriter == null) {
      responseWriter = new DomXmlRpcResponseWriter();
    }

    if (xmlRpcElementFactory == null) {
      xmlRpcElementFactory = new XmlRpcElementFactoryImpl();
    }
  }

  protected final XmlRpcRequestParser getRequestParser() {
    return requestParser;
  }

  protected final XmlRpcResponseWriter getResponseWriter() {
    return responseWriter;
  }

  protected final XmlRpcElementFactory getXmlRpcElementFactory() {
    return xmlRpcElementFactory;
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
      if (logger.isDebugEnabled()) {
        logger.debug("Remote service: " + serviceName);
      }

      if (!exportedServices.containsKey(serviceName)) {
        throw new XmlRpcServiceNotFoundException("The service "
            + Strings.quote(serviceName) + " was not found");
      }

      XmlRpcServiceExporter serviceExporter = (XmlRpcServiceExporter) exportedServices
          .get(serviceName);
      result = serviceExporter.invoke(xmlRpcRequest);

      if (logger.isDebugEnabled()) {
        logger.debug("XML-RPC remote invocation result: " + result);
      }

    } catch (XmlRpcException exception) {
      xmlRpcException = exception;
      if (logger.isErrorEnabled()) {
        logger.error("XML-RPC Exception", exception);
      }

    } catch (Exception exception) {
      if (logger.isErrorEnabled()) {
        logger.error("Server Exception", exception);
      }
      xmlRpcException = new XmlRpcInternalException(
          "Server error. Internal xml-rpc error");
    }

    XmlRpcResponse xmlRpcResponse = null;

    if (xmlRpcException == null) {
      XmlRpcElement parameter = xmlRpcElementFactory
          .createXmlRpcElement(result);
      xmlRpcResponse = new XmlRpcResponse(parameter);

    } else {
      XmlRpcFault xmlRpcFault = new XmlRpcFault(xmlRpcException.getCode(),
          xmlRpcException.getMessage());

      xmlRpcResponse = new XmlRpcResponse(xmlRpcFault);
    }

    byte[] serializedResponse = responseWriter.writeResponse(xmlRpcResponse);

    response.setContentType("text/xml");
    response.setContentLength(serializedResponse.length);

    try {
      OutputStream output = response.getOutputStream();
      output.write(serializedResponse);
      output.flush();
    } catch (Exception exception) {
      if (logger.isDebugEnabled()) {
        logger.debug(
            "Unable to write result to request. Server I/O Exception.",
            exception);
      }

    }
    return null;
  }

  public final void setExportedServices(Map newExportedServices) {
    exportedServices = newExportedServices;
  }

  public final void setRequestParser(XmlRpcRequestParser newRequestParser) {
    requestParser = newRequestParser;
  }

  public final void setResponseWriter(XmlRpcResponseWriter newResponseWriter) {
    responseWriter = newResponseWriter;
  }

  public final void setXmlRpcElementFactory(
      XmlRpcElementFactory newXmlRpcElementFactory) {
    xmlRpcElementFactory = newXmlRpcElementFactory;
  }

}
