/* 
 * Created on Jun 15, 2005
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

import org.springmodules.remoting.xmlrpc.support.XmlRpcRequest;

/**
 * <p>
 * Exports an object as a XML-RPC service.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.4 $ $Date: 2005/07/05 09:50:32 $
 */
public interface XmlRpcServiceExporter {

  /**
   * Invokes the method (of the exposed service) specified in the given XML-RPC
   * request.
   * 
   * @param xmlRpcRequest
   *          the XML-RPC request specifying the method to execute and its
   *          arguments.
   * @return the result of the invocation.
   * @throws XmlRpcException
   *           if there is any internal error.
   */
  Object invoke(XmlRpcRequest xmlRpcRequest) throws XmlRpcException;
}
