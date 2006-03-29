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
 * XML-RPC fault. Returned to the client when an error ocurred when invoking a
 * remote method.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public final class XmlRpcFault {

  /**
   * Struct containing the code and message of this fault.
   */
  private XmlRpcStruct faultStruct;

  /**
   * Constructor.
   * 
   * @param code
   *          the new code of this fault.
   * @param message
   *          the new message of this fault.
   */
  public XmlRpcFault(int code, String message) {
    super();
    faultStruct = new XmlRpcStruct();

    XmlRpcInteger faultCode = new XmlRpcInteger(new Integer(code));
    faultStruct.add(XmlRpcElementNames.FAULT_CODE, faultCode);

    XmlRpcString faultString = new XmlRpcString(message);
    faultStruct.add(XmlRpcElementNames.FAULT_STRING, faultString);
  }

  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof XmlRpcFault)) {
      return false;
    }

    final XmlRpcFault xmlRpcFault = (XmlRpcFault) obj;

    if (faultStruct != null ? !faultStruct.equals(xmlRpcFault.faultStruct)
        : xmlRpcFault.faultStruct != null) {
      return false;
    }

    return true;
  }

  public XmlRpcStruct getFaultStruct() {
    return faultStruct;
  }

  public int hashCode() {
    int multiplier = 31;
    int hash = 7;
    hash = multiplier * hash
        + (faultStruct != null ? faultStruct.hashCode() : 0);
    return hash;
  }

  public String toString() {
    StringBuffer buffer = new StringBuffer(getClass().getName());
    buffer.append("@" + System.identityHashCode(this) + "[");
    buffer.append("faultStruct=" + this.faultStruct + "]");

    return buffer.toString();
  }
}
