/* 
 * Created on May 30, 2005
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

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

/**
 * <p>
 * Spring-MVC controller that exports the specified service bean(s) as XML-RPC
 * service(s).
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.2 $ $Date: 2005/06/02 23:31:43 $
 */
public final class XmlRcpServiceController implements Controller,
    InitializingBean {

  /**
   * Exports services as XML-RPC services.
   */
  private ServletXmlRpcServiceExporter xmlRpcServiceExporter;

  /**
   * Constructor.
   */
  public XmlRcpServiceController() {
    super();
  }

  /**
   * Validates that <code>{@link #xmlRpcServiceExporter}</code> is not
   * <code>null</code>.
   * 
   * @throws BeanCreationException
   *           if the property 'xmlRpcServiceExporter' is <code>null</code>.
   * @see InitializingBean#afterPropertiesSet()
   */
  public void afterPropertiesSet() throws Exception {
    if (this.xmlRpcServiceExporter == null) {
      throw new BeanCreationException(
          "The property 'xmlRpcServiceExporter' should not be null");
    }
  }

  /**
   * Parse the request and execute the handler method, if one is found. Writes
   * the result (as XML) to the response.
   * 
   * @param request
   *          the current HTTP request.
   * @param response
   *          the current HTTP response.
   * @return <code>null</code>.
   * 
   * @see Controller#handleRequest(HttpServletRequest, HttpServletResponse)
   */
  public ModelAndView handleRequest(HttpServletRequest request,
      HttpServletResponse response) throws Exception {

    byte[] result = this.xmlRpcServiceExporter.readXmlRpcRequest(request);
    this.xmlRpcServiceExporter.writeXmlRpcResult(response, result);

    return null;
  }

  /**
   * Setter for the field <code>{@link #xmlRpcServiceExporter}</code>.
   * 
   * @param xmlRpcServiceExporter
   *          the new value to set.
   */
  public void setXmlRpcServiceExporter(
      ServletXmlRpcServiceExporter xmlRpcServiceExporter) {
    this.xmlRpcServiceExporter = xmlRpcServiceExporter;
  }

}
