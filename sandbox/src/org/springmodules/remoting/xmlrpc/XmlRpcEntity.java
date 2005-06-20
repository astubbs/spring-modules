/* 
 * Created on Jun 8, 2005
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

/**
 * <p>
 * XML entities supported XML-RPC.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.3 $ $Date: 2005/06/20 05:02:11 $
 */
public interface XmlRpcEntity {

  /**
   * Name of the XML entity that represents an array.
   */
  String ARRAY = "array";

  /**
   * Name of the XML entity that represents a base64-encoded binary.
   */
  String BASE_64 = "base64";

  /**
   * Name of the XML entity that represents a boolean.
   */
  String BOOLEAN = "boolean";

  /**
   * Name of the XML entity that represents the data portion of an array.
   */
  String DATA = "data";

  /**
   * Name of the XML entity that represents a date.
   */
  String DATE_TIME = "dateTime.iso8601";

  /**
   * Name of the XML entity that represents a double-precision signed floating
   * point number.
   */
  String DOUBLE = "double";

  /**
   * Name of the XML entity that represents a 32-bit integer.
   */
  String I4 = "i4";

  /**
   * Name of the XML entity that represents a 32-bit integer.
   */
  String INT = "int";

  /**
   * Name of the XML entity that represents a member of a struct.
   */
  String MEMBER = "member";

  /**
   * Name of the XML entity that represents the name of a XML-RPC method.
   */
  String METHOD_NAME = "methodName";

  /**
   * Name of the XML entity that represents the name of a member of a struct.
   */
  String NAME = "name";

  /**
   * Name of the XML entity that represents a parameter.
   */
  String PARAM = "param";

  /**
   * Name of the XML entity that represents a list of parameters.
   */
  String PARAMS = "params";

  /**
   * Name of the XML entity that represents a string.
   */
  String STRING = "string";

  /**
   * Name of the XML entity that represents a struct.
   */
  String STRUCT = "struct";

  /**
   * Name of the XML entity that represents the value of a parameter.
   */
  String VALUE = "value";

}
