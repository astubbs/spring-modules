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

import org.springmodules.xmlrpc.type.apache.SupportedDataTypeHandler;

import junit.framework.TestCase;

/**
 * <p>
 * Unit Tests for <code>{@link SupportedDataTypeHandler}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/06/02 23:31:46 $
 */
public final class SupportedDataTypeHandlerTests extends TestCase {

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case to construct.
   */
  public SupportedDataTypeHandlerTests(String name) {
    super(name);
  }

  /**
   * Verifies that <code>{@link SupportedDataTypeHandler#BOOLEAN_HANDLER}</code>
   * has <code>{@link Boolean}</code> as the supported class.
   */
  public void testBooleanHandlerGetSupportedClass() {
    assertSame("<Supported class>", Boolean.class,
        SupportedDataTypeHandler.BOOLEAN_HANDLER.getSupportedClass());
  }

  /**
   * Verifies that the method <code>handleType</code> of
   * <code>{@link SupportedDataTypeHandler#BOOLEAN_HANDLER}</code> returns the
   * object sent as argument.
   */
  public void testBooleanHandlerConvertion() {
    Boolean objectToSerialize = Boolean.TRUE;

    assertSame("<Serialized object>", objectToSerialize,
        SupportedDataTypeHandler.BOOLEAN_HANDLER.handleType(objectToSerialize,
            null));
  }

  /**
   * Verifies that
   * <code>{@link SupportedDataTypeHandler#BYTE_ARRAY_HANDLER}</code> has
   * <code>byte[]</code> as the supported class.
   */
  public void testByteArrayHandlerGetSupportedClass() {
    assertSame("<Supported class>", byte[].class,
        SupportedDataTypeHandler.BYTE_ARRAY_HANDLER.getSupportedClass());
  }

  /**
   * Verifies that the method <code>handleType</code> of
   * <code>{@link SupportedDataTypeHandler#BYTE_ARRAY_HANDLER}</code> returns
   * the object sent as argument.
   */
  public void testByteArrayHandlerConvertion() {
    byte[] objectToSerialize = new byte[4];

    assertSame("<Serialized object>", objectToSerialize,
        SupportedDataTypeHandler.BYTE_ARRAY_HANDLER.handleType(
            objectToSerialize, null));
  }

  /**
   * Verifies that <code>{@link SupportedDataTypeHandler#DATE_HANDLER}</code>
   * has <code>{@link Date}</code> as the supported class.
   */
  public void testDateHandlerGetSupportedClass() {
    assertSame("<Supported class>", Date.class,
        SupportedDataTypeHandler.DATE_HANDLER.getSupportedClass());
  }

  /**
   * Verifies that the method <code>handleType</code> of
   * <code>{@link SupportedDataTypeHandler#DATE_HANDLER}</code> returns the
   * object sent as argument.
   */
  public void testDateHandlerConvertion() {
    Date objectToSerialize = new Date();

    assertSame("<Serialized object>", objectToSerialize,
        SupportedDataTypeHandler.DATE_HANDLER.handleType(objectToSerialize,
            null));
  }

  /**
   * Verifies that <code>{@link SupportedDataTypeHandler#DOUBLE_HANDLER}</code>
   * has <code>{@link Double}</code> as the supported class.
   */
  public void testDoubleHandlerGetSupportedClass() {
    assertSame("<Supported class>", Double.class,
        SupportedDataTypeHandler.DOUBLE_HANDLER.getSupportedClass());
  }

  /**
   * Verifies that the method <code>handleType</code> of
   * <code>{@link SupportedDataTypeHandler#DOUBLE_HANDLER}</code> returns the
   * object sent as argument.
   */
  public void testDoubleHandlerConvertion() {
    Double objectToSerialize = new Double(553.9);

    assertSame("<Serialized object>", objectToSerialize,
        SupportedDataTypeHandler.DOUBLE_HANDLER.handleType(objectToSerialize,
            null));
  }

  /**
   * Verifies that <code>{@link SupportedDataTypeHandler#FLOAT_HANDLER}</code>
   * has <code>{@link Float}</code> as the supported class.
   */
  public void testFloatHandlerGetSupportedClass() {
    assertSame("<Supported class>", Float.class,
        SupportedDataTypeHandler.FLOAT_HANDLER.getSupportedClass());
  }

  /**
   * Verifies that the method <code>handleType</code> of
   * <code>{@link SupportedDataTypeHandler#FLOAT_HANDLER}</code> returns the
   * object sent as argument.
   */
  public void testFloatHandlerConvertion() {
    Float objectToSerialize = new Float(99.3f);

    assertSame("<Serialized object>", objectToSerialize,
        SupportedDataTypeHandler.FLOAT_HANDLER.handleType(objectToSerialize,
            null));
  }

  /**
   * Verifies that <code>{@link SupportedDataTypeHandler#INTEGER_HANDLER}</code>
   * has <code>{@link Integer}</code> as the supported class.
   */
  public void testIntegerHandlerGetSupportedClass() {
    assertSame("<Supported class>", Integer.class,
        SupportedDataTypeHandler.INTEGER_HANDLER.getSupportedClass());
  }

  /**
   * Verifies that the method <code>handleType</code> of
   * <code>{@link SupportedDataTypeHandler#INTEGER_HANDLER}</code> returns the
   * object sent as argument.
   */
  public void testIntegerHandlerConvertion() {
    Integer objectToSerialize = new Integer(64);

    assertSame("<Serialized object>", objectToSerialize,
        SupportedDataTypeHandler.INTEGER_HANDLER.handleType(objectToSerialize,
            null));
  }

  /**
   * Verifies that <code>{@link SupportedDataTypeHandler#STRING_HANDLER}</code>
   * has <code>{@link Integer}</code> as the supported class.
   */
  public void testStringHandlerGetSupportedClass() {
    assertSame("<Supported class>", String.class,
        SupportedDataTypeHandler.STRING_HANDLER.getSupportedClass());
  }

  /**
   * Verifies that the method <code>handleType</code> of
   * <code>{@link SupportedDataTypeHandler#STRING_HANDLER}</code> returns the
   * object sent as argument.
   */
  public void testStringHandlerConvertion() {
    String objectToSerialize = "Hi mamma";

    assertSame("<Serialized object>", objectToSerialize,
        SupportedDataTypeHandler.STRING_HANDLER.handleType(objectToSerialize,
            null));
  }
}
