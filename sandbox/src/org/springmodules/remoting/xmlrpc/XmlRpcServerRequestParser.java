/* 
 * Created on Jun 5, 2005
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

import org.w3c.dom.Document;

/**
 * <p>
 * Creates a XML-RPC remote invocation from a DOM document containing the name
 * of the method and its arguments.
 * </p>
 * <p>
 * The created remote invocation represents the request made by a client to a
 * server.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/06/06 02:13:57 $
 */
public interface XmlRpcServerRequestParser {

  /**
   * Creates a XML-RPC remote invocation from the specified DOM document.
   * 
   * @param document
   *          the DOM document.
   * @return the created XML-RPC remote invocation.
   */
  XmlRpcRemoteInvocation parseXmlRpcServerRequest(Document document);

}