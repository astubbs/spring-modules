/* 
 * Created on Jun 14, 2005
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
 * Represents a XML-RPC request.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.4 $ $Date: 2005/06/23 01:47:02 $
 */
public final class XmlRpcRequest {

  /**
   * The name of the method to execute.
   */
  private String methodName;

  /**
   * The parameters to pass to the method.
   */
  private XmlRpcElement[] parameters;

  /**
   * The name of the service containing the method to execute.
   */
  private String serviceName;

  /**
   * Constructor.
   */
  public XmlRpcRequest() {
    super();
  }

  /**
   * Constructor.
   * 
   * @param serviceName
   *          the new name of the service.
   * @param methodName
   *          the new name of the method to execute.
   * @param parameters
   *          the new parameters to pass to the method.
   */
  public XmlRpcRequest(String serviceName, String methodName,
      XmlRpcElement[] parameters) {
    this();
    this.setMethodName(methodName);
    this.setParameters(parameters);
    this.setServiceName(serviceName);
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

    if (null == obj || !(obj instanceof XmlRpcRequest)) {
      equals = false;
    } else if (this != obj) {
      XmlRpcRequest xmlRpcRequest = (XmlRpcRequest) obj;

      EqualsBuilder equalsBuilder = new EqualsBuilder();
      equalsBuilder.append(this.getMethodName(), xmlRpcRequest.getMethodName());
      equalsBuilder.append(this.getParameters(), xmlRpcRequest.getParameters());
      equalsBuilder.append(this.getServiceName(), xmlRpcRequest
          .getServiceName());

      equals = equalsBuilder.isEquals();
    }

    return equals;
  }

  /**
   * Getter for field <code>{@link #methodName}</code>.
   * 
   * @return the field <code>methodName</code>.
   */
  public final String getMethodName() {
    return this.methodName;
  }

  /**
   * Getter for field <code>{@link #parameters}</code>.
   * 
   * @return the field <code>parameters</code>.
   */
  public final XmlRpcElement[] getParameters() {
    return this.parameters;
  }

  /**
   * Getter for field <code>{@link #serviceName}</code>.
   * 
   * @return the field <code>serviceName</code>.
   */
  public final String getServiceName() {
    return this.serviceName;
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
    HashCodeBuilder hashCodeBuilder = new HashCodeBuilder(2281, 2287);
    hashCodeBuilder.append(this.methodName);
    hashCodeBuilder.append(this.parameters);
    hashCodeBuilder.append(this.serviceName);

    int hashCode = hashCodeBuilder.toHashCode();
    return hashCode;
  }

  /**
   * Setter for the field <code>{@link #methodName}</code>.
   * 
   * @param methodName
   *          the new value to set.
   */
  public final void setMethodName(String methodName) {
    this.methodName = methodName;
  }

  /**
   * Setter for the field <code>{@link #parameters}</code>.
   * 
   * @param parameters
   *          the new value to set.
   */
  public final void setParameters(XmlRpcElement[] parameters) {
    this.parameters = parameters;
  }

  /**
   * Sets the name of the service to call and the method to execute.
   * 
   * @param serviceAndMethodNames
   *          a String containing the names of the service and the method
   *          separated by a dot.
   * @throws IllegalArgumentException
   *           if the given String does not contain the needed fields.
   */
  public final void setServiceAndMethodNames(String serviceAndMethodNames) {
    int dotIndex = serviceAndMethodNames.indexOf(".");

    if (dotIndex == -1) {
      throw new IllegalArgumentException(
          "The given text should contain the name of the service and the method separated by a dot");
    }

    String newServiceName = serviceAndMethodNames.substring(0, dotIndex);
    this.setServiceName(newServiceName);

    String newMethodName = serviceAndMethodNames.substring(++dotIndex,
        serviceAndMethodNames.length());
    this.setMethodName(newMethodName);
  }

  /**
   * Setter for the field <code>{@link #serviceName}</code>.
   * 
   * @param serviceName
   *          the new value to set.
   */
  public final void setServiceName(String serviceName) {
    this.serviceName = serviceName;
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
    toStringBuilder.append("serviceName", this.serviceName);
    toStringBuilder.append("methodName", this.methodName);
    toStringBuilder.append("parameters", this.parameters);

    String toString = toStringBuilder.toString();
    return toString;
  }
}
