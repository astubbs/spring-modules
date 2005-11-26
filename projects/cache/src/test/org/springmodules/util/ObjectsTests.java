/* 
 * Created on Nov 26, 2005
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
package org.springmodules.util;

import org.springframework.util.ObjectUtils;

import junit.framework.TestCase;

/**
 * <p>
 * Unit Tests for <code>{@link Objects}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class ObjectsTests extends TestCase {

  public ObjectsTests(String name) {
    super(name);
  }

  /**
   * Verifies that the method <code>{@link Objects#hashCode(boolean)}</code>
   * returns <code>0</code> if the given argument is <code>false</code>.
   */
  public void testHashCodeWithBooleanFalseAsArgument() {
    int expected = 0;
    assertEquals(expected, Objects.hashCode(false));
  }

  /**
   * Verifies that the method <code>{@link Objects#hashCode(boolean)}</code>
   * returns <code>1</code> if the given argument is <code>true</code>.
   */
  public void testHashCodeWithBooleanTrueAsArgument() {
    int expected = 1;
    assertEquals(expected, Objects.hashCode(true));
  }

  /**
   * Tests <code>{@link Objects#hashCode(double)}</code>.
   */
  public void testHashCodeWithDoubleAsArgument() {
    double dbl = 9830.43;
    long bits = Double.doubleToLongBits(dbl);
    int expected = (int) (bits ^ (bits >>> 32));
    assertEquals(expected, Objects.hashCode(dbl));
  }

  /**
   * Tests <code>{@link Objects#hashCode(float)}</code>.
   */
  public void testHashCodeWithFloatAsArgument() {
    float flt = 34.8f;
    int expected = Float.floatToIntBits(flt);
    assertEquals(expected, Objects.hashCode(flt));
  }

  /**
   * Tests <code>{@link Objects#hashCode(long)}</code>.
   */
  public void testHashCodeWithLongAsArgument() {
    long lng = 883l;
    int expected = (int) (lng ^ (lng >>> 32));
    assertEquals(expected, Objects.hashCode(lng));
  }

  /**
   * Verifies that the method
   * <code>{@link Objects#identityToString(Object)}</code> returns a
   * <code>StringBuffer</code> containing the class name of the given object,
   * the "@" symbol and the hex string form of the object's identity hash code.
   */
  public void testIdentityToString() {
    Object obj = new Object();

    StringBuffer expected = new StringBuffer(obj.getClass().getName());
    expected.append("@" + ObjectUtils.getIdentityHexString(obj));

    StringBuffer actual = Objects.identityToString(obj);

    assertEquals(expected.toString(), actual.toString());
  }

  /**
   * Verifies that the method
   * <code>{@link Objects#identityToString(Object)}</code> returns an empty
   * <code>StringBuffer</code> if the given object is <code>null</code>.
   */
  public void testIdentityToStringWithNullObject() {
    assertEquals("", Objects.identityToString(null).toString());
  }

  public void testIsPrimitiveOrWrapperWithBooleanPrimitiveClass() {
    assertTrue(Objects.isPrimitiveOrWrapper(boolean.class));
  }

  public void testIsPrimitiveOrWrapperWithBooleanWrapperClass() {
    assertTrue(Objects.isPrimitiveOrWrapper(Boolean.class));
  }

  public void testIsPrimitiveOrWrapperWithBytePrimitiveClass() {
    assertTrue(Objects.isPrimitiveOrWrapper(byte.class));
  }

  public void testIsPrimitiveOrWrapperWithByteWrapperClass() {
    assertTrue(Objects.isPrimitiveOrWrapper(Byte.class));
  }

  public void testIsPrimitiveOrWrapperWithCharacterClass() {
    assertTrue(Objects.isPrimitiveOrWrapper(Character.class));
  }

  public void testIsPrimitiveOrWrapperWithCharClass() {
    assertTrue(Objects.isPrimitiveOrWrapper(char.class));
  }

  public void testIsPrimitiveOrWrapperWithDoublePrimitiveClass() {
    assertTrue(Objects.isPrimitiveOrWrapper(double.class));
  }

  public void testIsPrimitiveOrWrapperWithDoubleWrapperClass() {
    assertTrue(Objects.isPrimitiveOrWrapper(Double.class));
  }

  public void testIsPrimitiveOrWrapperWithFloatPrimitiveClass() {
    assertTrue(Objects.isPrimitiveOrWrapper(float.class));
  }

  public void testIsPrimitiveOrWrapperWithFloatWrapperClass() {
    assertTrue(Objects.isPrimitiveOrWrapper(Float.class));
  }

  public void testIsPrimitiveOrWrapperWithIntClass() {
    assertTrue(Objects.isPrimitiveOrWrapper(int.class));
  }

  public void testIsPrimitiveOrWrapperWithIntegerClass() {
    assertTrue(Objects.isPrimitiveOrWrapper(Integer.class));
  }

  public void testIsPrimitiveOrWrapperWithLongPrimitiveClass() {
    assertTrue(Objects.isPrimitiveOrWrapper(long.class));
  }

  public void testIsPrimitiveOrWrapperWithLongWrapperClass() {
    assertTrue(Objects.isPrimitiveOrWrapper(Long.class));
  }

  public void testIsPrimitiveOrWrapperWithNonPrimitiveOrWrapperClass() {
    assertFalse(Objects.isPrimitiveOrWrapper(Object.class));
  }

  public void testIsPrimitiveOrWrapperWithShortPrimitiveClass() {
    assertTrue(Objects.isPrimitiveOrWrapper(short.class));
  }

  public void testIsPrimitiveOrWrapperWithShortWrapperClass() {
    assertTrue(Objects.isPrimitiveOrWrapper(Short.class));
  }
}
