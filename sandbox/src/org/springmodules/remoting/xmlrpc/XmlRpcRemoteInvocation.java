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

import java.lang.reflect.InvocationTargetException;

import org.springframework.remoting.support.RemoteInvocation;

/**
 * <p>
 * TODO Document class.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/06/06 02:13:56 $
 */
public class XmlRpcRemoteInvocation extends RemoteInvocation {

  /**
   * Version number of this class.
   * 
   * @see java.io.Serializable
   */
  private static final long serialVersionUID = 3761405300678605108L;

  /**
   * Name of the bean containing the method to execute.
   */
  private String beanName;

  /**
   * Constructor.
   */
  public XmlRpcRemoteInvocation() {
    super();
  }

  /**
   * Constructor.
   * 
   * @param methodName
   *          the name of the method to execute.
   * @param parameterTypes
   *          the types of the arguments of the method.
   * @param arguments
   *          the arguments of the method.
   * @see #setMethodName(String)
   */
  public XmlRpcRemoteInvocation(String methodName, Class[] parameterTypes,
      Object[] arguments) {
    super();
    super.setArguments(arguments);
    this.setMethodName(methodName);
    super.setParameterTypes(parameterTypes);
  }

  /**
   * Getter for field <code>{@link #beanName}</code>.
   * 
   * @return the field <code>beanName</code>.
   */
  public final String getBeanName() {
    return this.beanName;
  }

  /**
   * @see RemoteInvocation#invoke(Object)
   */
  public Object invoke(Object targetObject) throws NoSuchMethodException,
      IllegalAccessException, InvocationTargetException {
    // TODO Implement overriden version of invoke.
    return super.invoke(targetObject);
  }

  /**
   * Sets the name of the bean to call and the method to execute.
   * 
   * @param methodName
   *          a String containing the names of the bean and the method separated
   *          by a dot.
   * @throws XmlRpcParsingException
   *           if the given String does not contain the needed fields.
   * @see RemoteInvocation#setMethodName(String)
   */
  public void setMethodName(String methodName) {
    int dotIndex = methodName.indexOf(".");

    if (dotIndex == -1) {
      throw new XmlRpcParsingException(
          "The given text should contain the name of the bean and the method separated by a dot");
    }

    String newBeanName = methodName.substring(0, dotIndex);
    this.beanName = newBeanName;

    String realMethodName = methodName.substring(++dotIndex, methodName
        .length());
    super.setMethodName(realMethodName);
  }

}
