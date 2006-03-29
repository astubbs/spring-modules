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

import java.util.Arrays;

import org.springmodules.util.ArrayUtils;
import org.springmodules.util.Strings;

/**
 * <p>
 * Represents a XML-RPC request.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
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

  public XmlRpcRequest() {
    super();
  }

  public XmlRpcRequest(String newServiceName, String newMethodName,
      XmlRpcElement[] newParameters) {
    this();
    setMethodName(newMethodName);
    setParameters(newParameters);
    setServiceName(newServiceName);
  }

  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof XmlRpcRequest)) {
      return false;
    }

    final XmlRpcRequest xmlRpcRequest = (XmlRpcRequest) obj;

    if (methodName != null ? !methodName.equals(xmlRpcRequest.methodName)
        : xmlRpcRequest.methodName != null) {
      return false;
    }
    if (!Arrays.equals(parameters, xmlRpcRequest.parameters)) {
      return false;
    }
    if (serviceName != null ? !serviceName.equals(xmlRpcRequest.serviceName)
        : xmlRpcRequest.serviceName != null) {
      return false;
    }

    return true;
  }

  public final String getMethodName() {
    return methodName;
  }

  public final XmlRpcElement[] getParameters() {
    return parameters;
  }

  public final String getServiceName() {
    return serviceName;
  }

  public int hashCode() {
    int multiplier = 31;
    int hash = 7;
    hash = multiplier * hash + (methodName != null ? methodName.hashCode() : 0);

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

    hash = multiplier * hash
        + (serviceName != null ? serviceName.hashCode() : 0);
    return hash;
  }

  public final void setMethodName(String newMethodName) {
    methodName = newMethodName;
  }

  /**
   * Setter for the field <code>{@link #parameters}</code>.
   * 
   * @param newParameters
   *          the new value to set.
   */
  public final void setParameters(XmlRpcElement[] newParameters) {
    parameters = newParameters;
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
          "The given text should have the format 'service.method'");
    }

    String newServiceName = serviceAndMethodNames.substring(0, dotIndex);
    setServiceName(newServiceName);

    String newMethodName = serviceAndMethodNames.substring(++dotIndex,
        serviceAndMethodNames.length());
    setMethodName(newMethodName);
  }

  public final void setServiceName(String newServiceName) {
    serviceName = newServiceName;
  }

  public String toString() {
    StringBuffer buffer = new StringBuffer(getClass().getName());
    buffer.append("@" + System.identityHashCode(this) + "[");
    buffer.append("serviceName=" + Strings.quote(serviceName) + ", ");
    buffer.append("parameters=" + ArrayUtils.toString(parameters) + "]");

    return buffer.toString();
  }
}
