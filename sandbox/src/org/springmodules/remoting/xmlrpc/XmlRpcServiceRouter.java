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
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.remoting.support.RemoteInvocationResult;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springmodules.remoting.xmlrpc.dom.DomXmlRpcRequestParser;
import org.springmodules.remoting.xmlrpc.support.XmlRpcRequest;

/**
 * <p>
 * TODO Document class.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.2 $ $Date: 2005/06/17 09:57:50 $
 */
public class XmlRpcServiceRouter implements InitializingBean, Controller {

  /**
   * Message logger.
   */
  protected final Log logger = LogFactory.getLog(this.getClass());

  /**
   * Registry of services to export. Methods of this objects will be callable
   * over XML-RPC as "servicename.methodname".
   */
  private Map exportedServices;

  /**
   * Parses the XML-RPC request.
   */
  private XmlRpcRequestParser requestParser;

  /**
   * Constructor.
   */
  public XmlRpcServiceRouter() {
    super();
  }

  /**
   * @see InitializingBean#afterPropertiesSet()
   */
  public void afterPropertiesSet() throws Exception {
    if (this.requestParser == null) {
      this.requestParser = new DomXmlRpcRequestParser();
    }

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
  }

  /**
   * @see Controller#handleRequest(HttpServletRequest, HttpServletResponse)
   */
  public ModelAndView handleRequest(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    RemoteInvocationResult result = null;

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

    } catch (Exception exception) {
      result = new RemoteInvocationResult(exception);
    }

    if (this.logger.isDebugEnabled()) {
      this.logger.debug("Result: [value= " + result.getValue() + "][exception="
          + result.getException() + "]");
    }

    if (result.hasException()) {

    } else {

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

}
