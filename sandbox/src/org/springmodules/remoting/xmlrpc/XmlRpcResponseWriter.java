/* 
 * Created on Jun 22, 2005
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

import org.springmodules.remoting.xmlrpc.support.XmlRpcResponse;

/**
 * <p>
 * Builds a XML-RPC response.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.3 $ $Date: 2005/06/23 01:42:53 $
 */
public interface XmlRpcResponseWriter {

  /**
   * Serializes a XML-RPC response into an array of bytes.
   * 
   * @param response
   *          the response of the execution of a XML-RPC request.
   * @return the given XML-RPC response as an array of bytes.
   */
  byte[] writeResponse(XmlRpcResponse response);
}
