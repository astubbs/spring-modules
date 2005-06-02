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
 * @version $Revision: 1.1 $ $Date: 2005/06/02 00:25:36 $
 */
public class XmlRcpServiceController extends ServletXmlRcpServiceExporter
    implements Controller {

  /**
   * Constructor.
   */
  public XmlRcpServiceController() {
    super();
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

    byte[] result = super.readXmlRpcRequest(request);
    super.writeXmlRpcResult(response, result);

    return null;
  }

}
