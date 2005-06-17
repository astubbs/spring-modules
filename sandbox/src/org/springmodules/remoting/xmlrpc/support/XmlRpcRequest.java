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

/**
 * <p>
 * Represents a XML-RPC request.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/06/17 09:57:48 $
 */
public class XmlRpcRequest {

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

}
