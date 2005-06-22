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

/**
 * <p>
 * Exception thrown when a XML-RPC request specifies a service that does not
 * exist.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.2 $ $Date: 2005/06/22 08:49:44 $
 */
public class XmlRpcServiceNotFoundException extends XmlRpcException {

  /**
   * Version number of this class.
   * 
   * @see java.io.Serializable
   */
  private static final long serialVersionUID = 3257005449604510518L;

  /**
   * Constructor.
   * 
   * @param msg
   *          the detail message.
   */
  public XmlRpcServiceNotFoundException(String msg) {
    super(msg);
  }

  /**
   * Constructor.
   * 
   * @param msg
   *          the detail message.
   * @param nestedException
   *          the nested exception.
   */
  public XmlRpcServiceNotFoundException(String msg, Throwable nestedException) {
    super(msg, nestedException);
  }

}
