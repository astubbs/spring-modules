/* 
 * Created on Jun 20, 2005
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

import org.springmodules.remoting.xmlrpc.XmlRpcElementNames;
import org.springmodules.remoting.xmlrpc.XmlRpcParsingException;

/**
 * <p>
 * Factory of implementations of <code>{@link XmlRpcScalar}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/06/21 01:03:32 $
 */
public interface XmlRpcScalarFactory {

  /**
   * <p>
   * Creates a new implementation of <code>{@link XmlRpcScalar}</code>
   * depending on the specified element name.
   * </p>
   * <p>
   * Valid element names are:
   * <ul>
   * <li>{@link XmlRpcElementNames#BASE_64}</li>
   * <li>{@link XmlRpcElementNames#BOOLEAN}</li>
   * <li>{@link XmlRpcElementNames#DATE_TIME}</li>
   * <li>{@link XmlRpcElementNames#DOUBLE}</li>
   * <li>{@link XmlRpcElementNames#I4}</li>
   * <li>{@link XmlRpcElementNames#INT}</li>
   * <li>{@link XmlRpcElementNames#STRING}</li>
   * </ul>
   * </p>
   * 
   * @param elementName
   *          the name of the element that indicates which implementation of
   *          <code>XmlRpcScalar</code> should be created.
   * @param value
   *          the value of the new scalar.
   * @return the created implementation of <code>XmlRpcScalar</code>.
   * @throws XmlRpcParsingException
   *           if the there is any parsing error while creating the new scalar
   *           value.
   * @throws XmlRpcParsingException
   *           if the element name is invalid.
   */
  XmlRpcScalar createScalarValue(String elementName, String value)
      throws XmlRpcParsingException;
}
