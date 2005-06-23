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
 * Represents a XML-RPC response.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.2 $ $Date: 2005/06/23 01:57:45 $
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

    if (null == obj || !(obj instanceof XmlRpcResponse)) {
      equals = false;
    } else if (this != obj) {
      XmlRpcResponse xmlRpcResponse = (XmlRpcResponse) obj;

      EqualsBuilder equalsBuilder = new EqualsBuilder();
      equalsBuilder.append(this.getFault(), xmlRpcResponse.getFault());
      equalsBuilder
          .append(this.getParameters(), xmlRpcResponse.getParameters());
      equalsBuilder
          .append(this.isFaultThrown(), xmlRpcResponse.isFaultThrown());

      equals = equalsBuilder.isEquals();
    }

    return equals;
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
   * Returns a hash code value for the object. This method is supported for the
   * benefit of hashtables such as those provided by
   * <code>java.util.Hashtable</code>.
   * 
   * @return a hash code value for this object.
   * 
   * @see Object#hashCode()
   */
  public int hashCode() {
    HashCodeBuilder hashCodeBuilder = new HashCodeBuilder(1447, 1451);
    hashCodeBuilder.append(this.fault);
    hashCodeBuilder.append(this.faultThrown);
    hashCodeBuilder.append(this.parameters);

    int hashCode = hashCodeBuilder.toHashCode();
    return hashCode;
  }

  /**
   * Getter for field <code>{@link #faultThrown}</code>.
   * 
   * @return the field <code>faultThrown</code>.
   */
  public boolean isFaultThrown() {
    return this.faultThrown;
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
    toStringBuilder.append("faultThrown", this.faultThrown);
    toStringBuilder.append("fault", this.fault);
    toStringBuilder.append("parameters", this.parameters);

    String toString = toStringBuilder.toString();
    return toString;
  }
}
