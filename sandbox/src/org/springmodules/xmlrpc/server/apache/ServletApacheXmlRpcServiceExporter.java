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
package org.springmodules.xmlrpc.server.apache;

import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springmodules.xmlrpc.server.ServletXmlRpcServiceExporter;

/**
 * <p>
 * Implementation of <code>{@link ServletXmlRpcServiceExporter}</code> that
 * uses Apache implementation of XML-RPC.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/06/02 23:31:45 $
 */
public class ServletApacheXmlRpcServiceExporter extends
    AbstractApacheXmlRpcServiceExporter implements ServletXmlRpcServiceExporter {

  /**
   * Constructor.
   */
  public ServletApacheXmlRpcServiceExporter() {
    super();
  }

  /**
   * @see ServletXmlRpcServiceExporter#readXmlRpcRequest(HttpServletRequest)
   */
  public final byte[] readXmlRpcRequest(HttpServletRequest request)
      throws Exception {
    byte[] result = super.getXmlRpcServer().execute(request.getInputStream());
    return result;
  }

  /**
   * @see ServletXmlRpcServiceExporter#writeXmlRpcResult(HttpServletResponse,
   *      byte[])
   */
  public final void writeXmlRpcResult(HttpServletResponse response,
      byte[] result) throws Exception {

    response.setContentType("text/xml");
    response.setContentLength(result.length);

    OutputStream outputStream = response.getOutputStream();
    outputStream.write(result);
    outputStream.flush();
  }
}
