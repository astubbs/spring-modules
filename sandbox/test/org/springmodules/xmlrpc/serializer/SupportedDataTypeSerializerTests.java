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

import junit.framework.TestCase;

/**
 * <p>
 * Unit Tests for <code>{@link SupportedDataTypeSerializer}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/06/02 00:27:16 $
 */
public final class SupportedDataTypeSerializerTests extends TestCase {

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case to construct.
   */
  public SupportedDataTypeSerializerTests(String name) {
    super(name);
  }

  /**
   * Verifies that
   * <code>{@link SupportedDataTypeSerializer#BOOLEAN_SERIALIZER}</code> has
   * <code>{@link Boolean}</code> as the supported class.
   */
  public void testBooleanSerializerGetSupportedClass() {
    assertSame("<Supported class>", Boolean.class,
        SupportedDataTypeSerializer.BOOLEAN_SERIALIZER.getSupportedClass());
  }

  /**
   * Verifies that
   * <code>{@link SupportedDataTypeSerializer#BOOLEAN_SERIALIZER}</code>
   * returns the same object to serialize sent as argument.
   */
  public void testBooleanSerializerSerializes() {
    Boolean objectToSerialize = Boolean.TRUE;

    assertSame("<Serialized object>", objectToSerialize,
        SupportedDataTypeSerializer.BOOLEAN_SERIALIZER.serialize(
            objectToSerialize, null));
  }

  /**
   * Verifies that
   * <code>{@link SupportedDataTypeSerializer#BYTE_ARRAY_SERIALIZER}</code>
   * has <code>byte[]</code> as the supported class.
   */
  public void testByteArraySerializerGetSupportedClass() {
    assertSame("<Supported class>", byte[].class,
        SupportedDataTypeSerializer.BYTE_ARRAY_SERIALIZER.getSupportedClass());
  }

  /**
   * Verifies that
   * <code>{@link SupportedDataTypeSerializer#BYTE_ARRAY_SERIALIZER}</code>
   * returns the same object to serialize sent as argument.
   */
  public void testByteArraySerializerSerializes() {
    byte[] objectToSerialize = new byte[4];

    assertSame("<Serialized object>", objectToSerialize,
        SupportedDataTypeSerializer.BYTE_ARRAY_SERIALIZER.serialize(
            objectToSerialize, null));
  }

  /**
   * Verifies that
   * <code>{@link SupportedDataTypeSerializer#DATE_SERIALIZER}</code> has
   * <code>{@link Date}</code> as the supported class.
   */
  public void testDateSerializerGetSupportedClass() {
    assertSame("<Supported class>", Date.class,
        SupportedDataTypeSerializer.DATE_SERIALIZER.getSupportedClass());
  }

  /**
   * Verifies that
   * <code>{@link SupportedDataTypeSerializer#DATE_SERIALIZER}</code> returns
   * the same object to serialize sent as argument.
   */
  public void testDateSerializerSerializes() {
    Date objectToSerialize = new Date();

    assertSame("<Serialized object>", objectToSerialize,
        SupportedDataTypeSerializer.DATE_SERIALIZER.serialize(
            objectToSerialize, null));
  }

  /**
   * Verifies that
   * <code>{@link SupportedDataTypeSerializer#DOUBLE_SERIALIZER}</code> has
   * <code>{@link Double}</code> as the supported class.
   */
  public void testDoubleSerializerGetSupportedClass() {
    assertSame("<Supported class>", Double.class,
        SupportedDataTypeSerializer.DOUBLE_SERIALIZER.getSupportedClass());
  }

  /**
   * Verifies that
   * <code>{@link SupportedDataTypeSerializer#DOUBLE_SERIALIZER}</code>
   * returns the same object to serialize sent as argument.
   */
  public void testDoubleSerializerSerializes() {
    Double objectToSerialize = new Double(553.9);

    assertSame("<Serialized object>", objectToSerialize,
        SupportedDataTypeSerializer.DOUBLE_SERIALIZER.serialize(
            objectToSerialize, null));
  }

  /**
   * Verifies that
   * <code>{@link SupportedDataTypeSerializer#FLOAT_SERIALIZER}</code> has
   * <code>{@link Float}</code> as the supported class.
   */
  public void testFloatSerializerGetSupportedClass() {
    assertSame("<Supported class>", Float.class,
        SupportedDataTypeSerializer.FLOAT_SERIALIZER.getSupportedClass());
  }

  /**
   * Verifies that
   * <code>{@link SupportedDataTypeSerializer#FLOAT_SERIALIZER}</code> returns
   * the same object to serialize sent as argument.
   */
  public void testFloatSerializerSerializes() {
    Float objectToSerialize = new Float(99.3f);

    assertSame("<Serialized object>", objectToSerialize,
        SupportedDataTypeSerializer.FLOAT_SERIALIZER.serialize(
            objectToSerialize, null));
  }

  /**
   * Verifies that
   * <code>{@link SupportedDataTypeSerializer#INTEGER_SERIALIZER}</code> has
   * <code>{@link Integer}</code> as the supported class.
   */
  public void testIntegerSerializerGetSupportedClass() {
    assertSame("<Supported class>", Integer.class,
        SupportedDataTypeSerializer.INTEGER_SERIALIZER.getSupportedClass());
  }

  /**
   * Verifies that
   * <code>{@link SupportedDataTypeSerializer#INTEGER_SERIALIZER}</code>
   * returns the same object to serialize sent as argument.
   */
  public void testIntegerSerializerSerializes() {
    Integer objectToSerialize = new Integer(64);

    assertSame("<Serialized object>", objectToSerialize,
        SupportedDataTypeSerializer.INTEGER_SERIALIZER.serialize(
            objectToSerialize, null));
  }

  /**
   * Verifies that
   * <code>{@link SupportedDataTypeSerializer#STRING_SERIALIZER}</code> has
   * <code>{@link Integer}</code> as the supported class.
   */
  public void testStringSerializerGetSupportedClass() {
    assertSame("<Supported class>", String.class,
        SupportedDataTypeSerializer.STRING_SERIALIZER.getSupportedClass());
  }

  /**
   * Verifies that
   * <code>{@link SupportedDataTypeSerializer#STRING_SERIALIZER}</code>
   * returns the same object to serialize sent as argument.
   */
  public void testStringSerializerSerializes() {
    String objectToSerialize = "Hi mamma";

    assertSame("<Serialized object>", objectToSerialize,
        SupportedDataTypeSerializer.STRING_SERIALIZER.serialize(
            objectToSerialize, null));
  }
}
