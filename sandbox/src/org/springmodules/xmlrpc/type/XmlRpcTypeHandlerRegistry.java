/* 
 * Created on Jun 1, 2005
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
 * Contains a collection of <code>{@link XmlRpcTypeHandler}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/06/02 23:31:53 $
 */
public interface XmlRpcTypeHandlerRegistry {

  /**
   * Finds a type handler that map the given object into another object instance
   * of a class supported by XML-RPC.
   * 
   * @param targetObject
   *          the object to handle.
   * @return the handler that can perform the data type conversion.
   * @throws ClassNotSupportedException
   *           if this registry does not contain the expected handler.
   */
  XmlRpcTypeHandler findTypeHandler(Object targetObject)
      throws ClassNotSupportedException;
}
