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
 * An element of a XML-RPC request/response.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/06/17 09:57:48 $
 */
public interface XmlRpcElement {

  /**
   * Canonical value indicating the specified type in
   * <code>{@link #getMatchingValue(Class)}</code> is not supported.
   */
  public static final Object NOT_MATCHING = new Object();

  /**
   * Returns an instance of the specified type containing the value stored in
   * this element. If the specified type is supported
   * <code>{@link #NOT_MATCHING}</code> should be returned.
   * 
   * @param targetType
   *          the target type.
   * @return an instance of the specified type containing the value stored in
   *         this element.
   */
  Object getMatchingValue(Class targetType);
}
