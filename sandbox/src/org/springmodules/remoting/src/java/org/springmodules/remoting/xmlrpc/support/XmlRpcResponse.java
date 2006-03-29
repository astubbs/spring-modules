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

import java.util.Arrays;

import org.springmodules.util.ArrayUtils;

/**
 * <p>
 * Represents a XML-RPC response.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
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
   * @param newParameter
   *          the new parameter of this response.
   */
  public XmlRpcResponse(XmlRpcElement newParameter) {
    this(new XmlRpcElement[] { newParameter });
  }

  /**
   * @param newParameters
   *          the new parameters of this response.
   */
  public XmlRpcResponse(XmlRpcElement[] newParameters) {
    super();
    parameters = newParameters;
  }

  /**
   * @param newFault
   *          the fault thrown if an error ocurred when executing a remote
   *          method.
   */
  public XmlRpcResponse(XmlRpcFault newFault) {
    super();
    fault = newFault;
    faultThrown = true;
  }

  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof XmlRpcResponse)) {
      return false;
    }

    final XmlRpcResponse xmlRpcResponse = (XmlRpcResponse) obj;

    if (faultThrown != xmlRpcResponse.faultThrown) {
      return false;
    }
    if (fault != null ? !fault.equals(xmlRpcResponse.fault)
        : xmlRpcResponse.fault != null) {
      return false;
    }
    if (!Arrays.equals(parameters, xmlRpcResponse.parameters)) {
      return false;
    }

    return true;
  }

  public XmlRpcFault getFault() {
    return fault;
  }

  public XmlRpcElement[] getParameters() {
    return parameters;
  }

  public int hashCode() {
    int multiplier = 31;
    int hash = 7;
    hash = multiplier * hash + (fault != null ? fault.hashCode() : 0);
    hash = multiplier * hash + (faultThrown ? 1 : 0);

    if (parameters == null) {
      hash = multiplier * hash;
    } else {
      int parameterCount = parameters.length;
      for (int i = 0; i < parameterCount; i++) {
        XmlRpcElement parameter = parameters[i];
        hash = multiplier * hash
            + (parameter != null ? parameter.hashCode() : 0);
      }
    }

    return hash;
  }

  public boolean isFaultThrown() {
    return faultThrown;
  }

  public String toString() {
    StringBuffer buffer = new StringBuffer(getClass().getName());
    buffer.append("@" + System.identityHashCode(this) + "[");
    buffer.append("faultThrown=" + faultThrown + ", ");
    buffer.append("fault=" + fault + ", ");
    buffer.append("parameters=" + ArrayUtils.toString(parameters) + "]");

    return buffer.toString();
  }
}
