/* 
 * Created on Jun 7, 2005
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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * <p>
 * Encapsulates the arguments and types of a XML-RPC remote invocation.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/06/08 01:54:25 $
 */
public class XmlRpcRemoteInvocationArguments {

  /**
   * Arguments of the XML-RPC remote invocation.
   */
  private List arguments;

  /**
   * Parameter types of the XML-RPC remote invocation.
   */
  private List parameterTypes;

  /**
   * Constructor.
   */
  public XmlRpcRemoteInvocationArguments() {
    super();
    this.arguments = new ArrayList();
    this.parameterTypes = new ArrayList();
  }

  /**
   * <p>
   * Adds the specified argument this collection.
   * </p>
   * 
   * @param argument
   *          the argument to add.
   * @param parameterType
   *          the type of the given argument.
   */
  public void addArgument(Object argument, Class parameterType) {
    this.arguments.add(argument);
    this.parameterTypes.add(parameterType);
  }

  /**
   * Clears this collection.
   */
  public void clear() {
    this.arguments.clear();
    this.parameterTypes.clear();
  }

  /**
   * Returns the arguments of the XML-RPC remote invocation.
   * 
   * @return the arguments of the XML-RPC remote invocation.
   */
  public Object[] getArguments() {
    return this.arguments.toArray();
  }

  /**
   * Returns the parameter types of the XML-RPC remote invocation.
   * 
   * @return the parameter types of the XML-RPC remote invocation.
   */
  public Class[] getParameterTypes() {
    return (Class[]) this.parameterTypes.toArray(new Class[this.parameterTypes
        .size()]);
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
    toStringBuilder.append("arguments", this.arguments);
    toStringBuilder.append("parameterTypes", this.parameterTypes);

    String toString = toStringBuilder.toString();
    return toString;
  }
}
