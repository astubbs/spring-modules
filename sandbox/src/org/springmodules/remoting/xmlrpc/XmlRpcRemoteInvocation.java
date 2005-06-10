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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.remoting.support.RemoteInvocation;

/**
 * <p>
 * Encapsulates a XML-RPC remote invocation.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.4 $ $Date: 2005/06/10 01:44:40 $
 */
public class XmlRpcRemoteInvocation extends RemoteInvocation {

  /**
   * Version number of this class.
   * 
   * @see java.io.Serializable
   */
  private static final long serialVersionUID = 3761405300678605108L;

  /**
   * Name of the service containing the method to execute.
   */
  private String serviceName;

  /**
   * Constructor.
   */
  public XmlRpcRemoteInvocation() {
    super();
  }

  /**
   * Constructor.
   * 
   * @param serviceAndMethodNames
   *          the name of the service and the method to execute.
   * @param invocationArguments
   *          encapsulates the arguments for this invocation.
   * @see #setServiceAndMethodNames(String)         
   */
  public XmlRpcRemoteInvocation(String serviceAndMethodNames,
      XmlRpcRemoteInvocationArguments invocationArguments)
      throws XmlRpcParsingException {
    super();
    this.setServiceAndMethodNames(serviceAndMethodNames);

    if (invocationArguments != null) {
      super.setArguments(invocationArguments.getArguments());
      super.setParameterTypes(invocationArguments.getParameterTypes());
    }
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

    if (null == obj || !(obj instanceof XmlRpcRemoteInvocation)) {
      equals = false;
    } else if (this != obj) {
      XmlRpcRemoteInvocation remoteInvocation = (XmlRpcRemoteInvocation) obj;

      EqualsBuilder equalsBuilder = new EqualsBuilder();
      equalsBuilder.append(super.getArguments(), remoteInvocation
          .getArguments());
      equalsBuilder.append(this.getServiceName(), remoteInvocation
          .getServiceName());
      equalsBuilder.append(super.getMethodName(), remoteInvocation
          .getMethodName());
      equalsBuilder.append(super.getParameterTypes(), remoteInvocation
          .getParameterTypes());

      equals = equalsBuilder.isEquals();
    }

    return equals;
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
    HashCodeBuilder hashCodeBuilder = new HashCodeBuilder(7, 17);
    hashCodeBuilder.append(super.getArguments());
    hashCodeBuilder.append(this.getServiceName());
    hashCodeBuilder.append(super.getMethodName());
    hashCodeBuilder.append(super.getParameterTypes());

    int hashCode = hashCodeBuilder.toHashCode();
    return hashCode;
  }

  /**
   * Sets the name of the service to call and the method to execute.
   * 
   * @param serviceAndMethodNames
   *          a String containing the names of the service and the method
   *          separated by a dot.
   * @throws XmlRpcParsingException
   *           if the given String does not contain the needed fields.
   * @see RemoteInvocation#setMethodName(String)
   */
  public final void setServiceAndMethodNames(String serviceAndMethodNames)
      throws XmlRpcParsingException {
    int dotIndex = serviceAndMethodNames.indexOf(".");

    if (dotIndex == -1) {
      throw new XmlRpcParsingException(
          "The given text should contain the name of the service and the method separated by a dot");
    }

    String newServiceName = serviceAndMethodNames.substring(0, dotIndex);
    this.serviceName = newServiceName;

    String newMethodName = serviceAndMethodNames.substring(++dotIndex,
        serviceAndMethodNames.length());
    super.setMethodName(newMethodName);
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
    toStringBuilder.append("serviceName", this.getServiceName());
    toStringBuilder.append("methodName", super.getMethodName());
    toStringBuilder.append("parameterTypes", super.getParameterTypes());
    toStringBuilder.append("arguments", super.getArguments());

    String toString = toStringBuilder.toString();
    return toString;
  }
}
