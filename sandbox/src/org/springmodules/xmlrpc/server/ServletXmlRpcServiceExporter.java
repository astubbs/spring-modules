/* 
 * Created on Jun 2, 2005
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

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * Exports business services as XML-RPC handlers taking care of the reading a
 * HTTP request for a XML-RPC service and the writing of the result to a HTTP
 * response.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/06/02 23:31:44 $
 */
public interface ServletXmlRpcServiceExporter {

  /**
   * Parse the request and execute the handler method, if one is found. Returns
   * the result as XML.
   * 
   * @param request
   *          the HTTP request.
   * @return the result of the XML-RPC call as XML.
   * @throws IOException
   *           if an input or output exception occurred.
   */
  byte[] readXmlRpcRequest(HttpServletRequest request) throws Exception;

  /**
   * Writes the given result of a XML-RPC call to the response.
   * 
   * @param response
   *          the HTTP response.
   * @param result
   *          the result of a XML-RPC call.
   * @throws IOException
   *           if an input or output exception occurred.
   */
  void writeXmlRpcResult(HttpServletResponse response, byte[] result)
      throws Exception;

}
