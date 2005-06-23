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
package org.springmodules.remoting.xmlrpc.support;

/**
 * <p>
 * Represents a XML-RPC response.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/06/23 01:47:19 $
 */
public final class XmlRpcResponse {

  /**
   * Fault thrown if an error ocurred when executing a remote method.
   */
  private XmlRpcFault fault;

  /**
   * Returns <code>true</code> if the processing of the XML-RPC request was
   * not successful.
   */
  private boolean faultThrown;

  /**
   * The parameters of this response.
   */
  private XmlRpcElement[] parameters;

  /**
   * Constructor.
   * 
   * @param parameters
   *          the new parameters of this response.
   */
  public XmlRpcResponse(XmlRpcElement[] parameters) {
    super();
    this.parameters = parameters;
  }

  /**
   * Constructor.
   * 
   * @param fault
   *          the fault thrown if an error ocurred when executing a remote
   *          method.
   */
  public XmlRpcResponse(XmlRpcFault fault) {
    super();
    this.fault = fault;
    this.faultThrown = true;
  }

  /**
   * Getter for field <code>{@link #fault}</code>.
   * 
   * @return the field <code>fault</code>.
   */
  public XmlRpcFault getFault() {
    return this.fault;
  }

  /**
   * Getter for field <code>{@link #parameters}</code>.
   * 
   * @return the field <code>parameters</code>.
   */
  public XmlRpcElement[] getParameters() {
    return this.parameters;
  }

  /**
   * Getter for field <code>{@link #faultThrown}</code>.
   * 
   * @return the field <code>faultThrown</code>.
   */
  public boolean isFaultThrown() {
    return this.faultThrown;
  }
}
