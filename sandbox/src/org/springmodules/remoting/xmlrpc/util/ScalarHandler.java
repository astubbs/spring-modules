/* 
 * Created on Jun 13, 2005
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
package org.springmodules.remoting.xmlrpc.util;

import org.springmodules.remoting.xmlrpc.XmlRpcParsingException;

/**
 * <p>
 * Parses and formats scalar values supported by XML-RPC.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/06/14 00:47:16 $
 */
public interface ScalarHandler {

  /**
   * Returns the String representation of the given object after applying some
   * formatting.
   * 
   * @param source
   *          the object to format.
   * @return the String representation of the given object.
   */
  String format(Object source);

  /**
   * Parses the given String into an Object.
   * 
   * @param source
   *          the String to parse.
   * @return the created Object.
   * @throws XmlRpcParsingException
   *           if there is any error during the parsing.
   */
  Object parse(String source) throws XmlRpcParsingException;
}
