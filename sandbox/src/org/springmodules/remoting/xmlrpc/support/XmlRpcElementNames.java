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
package org.springmodules.remoting.xmlrpc.support;

/**
 * <p>
 * Names of the XML elements supported by XML-RPC.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/06/23 02:13:47 $
 */
public interface XmlRpcElementNames {

  /**
   * Name of the XML-RPC element that represents an array.
   */
  String ARRAY = "array";

  /**
   * Name of the XML-RPC element that represents a base64-encoded binary.
   */
  String BASE_64 = "base64";

  /**
   * Name of the XML-RPC element that represents a boolean.
   */
  String BOOLEAN = "boolean";

  /**
   * Name of the XML-RPC element that represents the data portion of a XML-RPC
   * array.
   */
  String DATA = "data";

  /**
   * Name of the XML-RPC element that represents a date.
   */
  String DATE_TIME = "dateTime.iso8601";

  /**
   * Name of the XML-RPC element that represents a double-precision signed
   * floating point number.
   */
  String DOUBLE = "double";

  /**
   * Name of the XML-RPC element that represents a fault.
   */
  String FAULT = "fault";

  /**
   * Name of the XML-RPC element that represents the code of a fault.
   */
  String FAULT_CODE = "faultCode";

  /**
   * Name of the XML-RPC element that represents the message of a fault.
   */
  String FAULT_STRING = "faultString";

  /**
   * Name of the XML-RPC element that represents a 32-bit integer.
   */
  String I4 = "i4";

  /**
   * Name of the XML-RPC element that represents a 32-bit integer.
   */
  String INT = "int";

  /**
   * Name of the XML-RPC element that represents a member of a struct.
   */
  String MEMBER = "member";

  /**
   * Name of the XML-RPC element that represents the name of a method in a
   * XML-RPC request.
   */
  String METHOD_NAME = "methodName";

  /**
   * Name of the XML-RPC element that represents the response to an XML-RPC
   * request.
   */
  String METHOD_RESPONSE = "methodResponse";

  /**
   * Name of the XML-RPC element that represents the name of a member of a
   * struct.
   */
  String NAME = "name";

  /**
   * Name of the XML-RPC element that represents a parameter.
   */
  String PARAM = "param";

  /**
   * Name of the XML-RPC element that represents a list of parameters.
   */
  String PARAMS = "params";

  /**
   * Name of the XML-RPC element that represents a string.
   */
  String STRING = "string";

  /**
   * Name of the XML-RPC element that represents a struct.
   */
  String STRUCT = "struct";

  /**
   * Name of the XML-RPC element that represents the value of a parameter.
   */
  String VALUE = "value";

}
