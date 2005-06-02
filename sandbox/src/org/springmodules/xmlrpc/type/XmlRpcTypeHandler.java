/* 
 * Created on May 31, 2005
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
package org.springmodules.xmlrpc.type;

/**
 * <p>
 * Handles the conversion between Java data types and the data types supported
 * by the Java implementation of XML-RCP.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/06/02 23:31:54 $
 */
public interface XmlRpcTypeHandler {

  /**
   * Returns the class this handler can convert.
   * 
   * @return the class this handler can convert.
   */
  Class getSupportedClass();

  /**
   * Handles the convertion of the given object into an instance of a class
   * supported by Apache XML-RPC.
   * 
   * @param obj
   *          the object to convert.
   * @param typeHandlerRegistry
   *          a registry containing data type handlers that may assist in the
   *          convertion.
   * @return a new instance of a class supported by Apache XML-RPC.
   */
  Object handleType(Object obj, XmlRpcTypeHandlerRegistry typeHandlerRegistry);

  /**
   * Returns <code>true</code> if this handler supports the convertion of the
   * given class.
   * 
   * @param clazz
   *          the class to verify.
   * @return <code>true</code> if this handler supports the convertion of the
   *         given class.
   */
  boolean isSupported(Class clazz);
}
