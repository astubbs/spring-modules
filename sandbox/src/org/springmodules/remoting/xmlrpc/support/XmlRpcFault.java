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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * <p>
 * XML-RPC fault. Returned to the client when an error ocurred when invoking a
 * remote method.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.3 $ $Date: 2005/06/23 02:13:47 $
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
    this.faultStruct = new XmlRpcStruct();

    XmlRpcInteger faultCode = new XmlRpcInteger(new Integer(code));
    this.faultStruct.add(XmlRpcElementNames.FAULT_CODE, faultCode);

    XmlRpcString faultString = new XmlRpcString(message);
    this.faultStruct.add(XmlRpcElementNames.FAULT_STRING, faultString);
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   * 
   * @param obj
   *          the reference object with which to compare
   * @return <code>true</code> if this object is the same as the obj argument;
   *         <code>false</code> otherwise.
   * 
   * @see Object#equals(java.lang.Object)
   */
  public boolean equals(Object obj) {
    boolean equals = true;

    if (null == obj || !(obj instanceof XmlRpcFault)) {
      equals = false;
    } else if (this != obj) {
      XmlRpcFault xmlRpcFault = (XmlRpcFault) obj;

      EqualsBuilder equalsBuilder = new EqualsBuilder();
      equalsBuilder.append(this.getFaultStruct(), xmlRpcFault.getFaultStruct());

      equals = equalsBuilder.isEquals();
    }

    return equals;
  }

  /**
   * Getter for field <code>{@link #faultStruct}</code>.
   * 
   * @return the field <code>faultStruct</code>.
   */
  public XmlRpcStruct getFaultStruct() {
    return this.faultStruct;
  }

  /**
   * Returns a hash code value for the object. This method is supported for the
   * benefit of hashtables such as those provided by
   * <code>java.util.Hashtable</code>.
   * 
   * @return a hash code value for this object.
   * 
   * @see Object#hashCode()
   */
  public int hashCode() {
    HashCodeBuilder hashCodeBuilder = new HashCodeBuilder(1433, 1439);
    hashCodeBuilder.append(this.faultStruct);

    int hashCode = hashCodeBuilder.toHashCode();
    return hashCode;
  }

  /**
   * Returns a string representation of the object. In general, the
   * <code>toString</code> method returns a string that "textually represents"
   * this object.
   * 
   * @return a string representation of the object.
   * 
   * @see Object#toString()
   */
  public String toString() {
    ToStringBuilder toStringBuilder = new ToStringBuilder(this);
    toStringBuilder.append("faultStruct", this.faultStruct);

    String toString = toStringBuilder.toString();
    return toString;
  }
}
