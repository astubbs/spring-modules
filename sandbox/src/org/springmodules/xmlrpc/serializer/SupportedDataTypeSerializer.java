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
package org.springmodules.xmlrpc.serializer;

import java.util.Date;

/**
 * <p>
 * Serializer that handles the data types supported by Apache XML-RPC.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/06/02 00:39:26 $
 */
public final class SupportedDataTypeSerializer extends AbstractXmlRpcSerializer {

  /**
   * Serializes a <code>{@link Boolean}</code>.
   */
  public static final SupportedDataTypeSerializer BOOLEAN_SERIALIZER = new SupportedDataTypeSerializer(
      Boolean.class);

  /**
   * Serializes an array of <code>byte</code>.
   */
  public static final SupportedDataTypeSerializer BYTE_ARRAY_SERIALIZER = new SupportedDataTypeSerializer(
      byte[].class);

  /**
   * Serializes a <code>{@link Date}</code>.
   */
  public static final SupportedDataTypeSerializer DATE_SERIALIZER = new SupportedDataTypeSerializer(
      Date.class);

  /**
   * Serializes a <code>{@link Double}</code>.
   */
  public static final SupportedDataTypeSerializer DOUBLE_SERIALIZER = new SupportedDataTypeSerializer(
      Double.class);

  /**
   * Serializes a <code>{@link Float}</code>.
   */
  public static final SupportedDataTypeSerializer FLOAT_SERIALIZER = new SupportedDataTypeSerializer(
      Float.class);

  /**
   * Serializes an <code>{@link Integer}</code>.
   */
  public static final SupportedDataTypeSerializer INTEGER_SERIALIZER = new SupportedDataTypeSerializer(
      Integer.class);

  /**
   * Serializes an <code>{@link String}</code>.
   */
  public static final SupportedDataTypeSerializer STRING_SERIALIZER = new SupportedDataTypeSerializer(
      String.class);

  /**
   * The class this serializer handles.
   */
  private Class supportedClass;

  /**
   * Constructor.
   */
  private SupportedDataTypeSerializer(Class supportedClass) {
    super();
    this.supportedClass = supportedClass;
  }

  /**
   * @see XmlRpcSerializer#getSupportedClass()
   */
  public Class getSupportedClass() {
    return this.supportedClass;
  }

  /**
   * @see XmlRpcSerializer#serialize(Object, XmlRpcSerializerRegistry)
   */
  public Object serialize(Object obj,
      XmlRpcSerializerRegistry serializerRegistry) {

    Object serialized = (obj == null ? "" : obj);
    return serialized;
  }
}
