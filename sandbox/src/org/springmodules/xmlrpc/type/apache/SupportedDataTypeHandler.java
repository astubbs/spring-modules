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
package org.springmodules.xmlrpc.type.apache;

import java.util.Date;

import org.springmodules.xmlrpc.type.XmlRpcTypeHandler;
import org.springmodules.xmlrpc.type.XmlRpcTypeHandlerRegistry;

/**
 * <p>
 * Handles the data types supported by Apache XML-RPC.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/06/02 23:31:50 $
 */
public final class SupportedDataTypeHandler extends AbstractApacheXmlRpcTypeHandler {

  /**
   * Serializes a <code>{@link Boolean}</code>.
   */
  public static final SupportedDataTypeHandler BOOLEAN_HANDLER = new SupportedDataTypeHandler(
      Boolean.class);

  /**
   * Serializes an array of <code>byte</code>.
   */
  public static final SupportedDataTypeHandler BYTE_ARRAY_HANDLER = new SupportedDataTypeHandler(
      byte[].class);

  /**
   * Serializes a <code>{@link Date}</code>.
   */
  public static final SupportedDataTypeHandler DATE_HANDLER = new SupportedDataTypeHandler(
      Date.class);

  /**
   * Serializes a <code>{@link Double}</code>.
   */
  public static final SupportedDataTypeHandler DOUBLE_HANDLER = new SupportedDataTypeHandler(
      Double.class);

  /**
   * Serializes a <code>{@link Float}</code>.
   */
  public static final SupportedDataTypeHandler FLOAT_HANDLER = new SupportedDataTypeHandler(
      Float.class);

  /**
   * Serializes an <code>{@link Integer}</code>.
   */
  public static final SupportedDataTypeHandler INTEGER_HANDLER = new SupportedDataTypeHandler(
      Integer.class);

  /**
   * Serializes an <code>{@link String}</code>.
   */
  public static final SupportedDataTypeHandler STRING_HANDLER = new SupportedDataTypeHandler(
      String.class);

  /**
   * The class this serializer handles.
   */
  private Class supportedClass;

  /**
   * Constructor.
   */
  private SupportedDataTypeHandler(Class supportedClass) {
    super();
    this.supportedClass = supportedClass;
  }

  /**
   * @see XmlRpcTypeHandler#getSupportedClass()
   */
  public Class getSupportedClass() {
    return this.supportedClass;
  }

  /**
   * @see XmlRpcTypeHandler#handleType(Object, XmlRpcTypeHandlerRegistry)
   */
  protected Object handle(Object obj, XmlRpcTypeHandlerRegistry registry) {
    return obj;
  }
}
