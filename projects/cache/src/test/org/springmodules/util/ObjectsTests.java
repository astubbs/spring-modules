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

import junit.framework.TestCase;

import org.springframework.util.ObjectUtils;

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

  public void testHashCodeWithBooleanFalse() {
    int expected = Boolean.FALSE.hashCode();
    assertEquals(expected, Objects.hashCode(false));
  }

  public void testHashCodeWithBooleanTrue() {
    int expected = Boolean.TRUE.hashCode();
    assertEquals(expected, Objects.hashCode(true));
  }

  public void testHashCodeWithDouble() {
    double dbl = 9830.43;
    int expected = (new Double(dbl)).hashCode();
    assertEquals(expected, Objects.hashCode(dbl));
  }

  public void testHashCodeWithFloat() {
    float flt = 34.8f;
    int expected = (new Float(flt)).hashCode();
    assertEquals(expected, Objects.hashCode(flt));
  }

  public void testHashCodeWithLong() {
    long lng = 883l;
    int expected = (new Long(lng)).hashCode();
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

  public void testIsArrayOfPrimitivesWithBooleanArray() {
    assertTrue(Objects.isArrayOfPrimitives(new boolean[] { true, false }));
  }

  public void testIsArrayOfPrimitivesWithObjectArray() {
    assertFalse(Objects.isArrayOfPrimitives(new Object[] { "Darth Vader " }));
  }

  public void testIsArrayOfPrimitivesWithObjectEqualToNull() {
    assertFalse(Objects.isArrayOfPrimitives(null));
  }

  public void testIsArrayOfPrimitivesWithObjectNotBeingArray() {
    assertFalse(Objects.isArrayOfPrimitives("Han Solo"));
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

  public void testNullSafeHashCodeWithBooleanArray() {
    int expected = 31 * 7 + Boolean.TRUE.hashCode();
    expected = 31 * expected + Boolean.FALSE.hashCode();

    boolean[] array = { true, false };
    int actual = Objects.nullSafeHashCode(array);

    assertEquals(expected, actual);
  }

  public void testNullSafeHashCodeWithBooleanArrayEqualToNull() {
    assertEquals(0, Objects.nullSafeHashCode((boolean[]) null));
  }

  public void testNullSafeHashCodeWithByteArray() {
    int expected = 31 * 7 + 8;
    expected = 31 * expected + 10;

    byte[] array = { 8, 10 };
    int actual = Objects.nullSafeHashCode(array);

    assertEquals(expected, actual);
  }

  public void testNullSafeHashCodeWithByteArrayEqualToNull() {
    assertEquals(0, Objects.nullSafeHashCode((byte[]) null));
  }

  public void testNullSafeHashCodeWithCharArray() {
    int expected = 31 * 7 + 'a';
    expected = 31 * expected + 'E';

    char[] array = { 'a', 'E' };
    int actual = Objects.nullSafeHashCode(array);

    assertEquals(expected, actual);
  }

  public void testNullSafeHashCodeWithCharArrayEqualToNull() {
    assertEquals(0, Objects.nullSafeHashCode((char[]) null));
  }

  public void testNullSafeHashCodeWithDoubleArray() {
    long bits = Double.doubleToLongBits(8449.65);
    int expected = 31 * 7 + (int) (bits ^ (bits >>> 32));
    bits = Double.doubleToLongBits(9944.923);
    expected = 31 * expected + (int) (bits ^ (bits >>> 32));

    double[] array = { 8449.65, 9944.923 };
    int actual = Objects.nullSafeHashCode(array);

    assertEquals(expected, actual);
  }

  public void testNullSafeHashCodeWithDoubleArrayEqualToNull() {
    assertEquals(0, Objects.nullSafeHashCode((double[]) null));
  }

  public void testNullSafeHashCodeWithFloatArray() {
    int expected = 31 * 7 + Float.floatToIntBits(9.6f);
    expected = 31 * expected + Float.floatToIntBits(7.4f);

    float[] array = { 9.6f, 7.4f };
    int actual = Objects.nullSafeHashCode(array);

    assertEquals(expected, actual);
  }

  public void testNullSafeHashCodeWithFloatArrayEqualToNull() {
    assertEquals(0, Objects.nullSafeHashCode((float[]) null));
  }

  public void testNullSafeHashCodeWithIntArray() {
    int expected = 31 * 7 + 884;
    expected = 31 * expected + 340;

    int[] array = { 884, 340 };
    int actual = Objects.nullSafeHashCode(array);

    assertEquals(expected, actual);
  }

  public void testNullSafeHashCodeWithIntArrayEqualToNull() {
    assertEquals(0, Objects.nullSafeHashCode((int[]) null));
  }

  public void testNullSafeHashCodeWithLongArray() {
    long lng = 7993l;
    int expected = 31 * 7 + (int) (lng ^ (lng >>> 32));
    lng = 84320l;
    expected = 31 * expected + (int) (lng ^ (lng >>> 32));

    long[] array = { 7993l, 84320l };
    int actual = Objects.nullSafeHashCode(array);

    assertEquals(expected, actual);
  }

  public void testNullSafeHashCodeWithLongArrayEqualToNull() {
    assertEquals(0, Objects.nullSafeHashCode((long[]) null));
  }

  public void testNullSafeHashCodeWithObject() {
    String str = "Luke";
    assertEquals(str.hashCode(), Objects.nullSafeHashCode(str));
  }

  public void testNullSafeHashCodeWithObjectArray() {
    int expected = 31 * 7 + "Leia".hashCode();
    expected = 31 * expected + "Han".hashCode();

    Object[] array = { "Leia", "Han" };
    int actual = Objects.nullSafeHashCode(array);

    assertEquals(expected, actual);
  }

  public void testNullSafeHashCodeWithObjectArrayEqualToNull() {
    assertEquals(0, Objects.nullSafeHashCode((Object[]) null));
  }

  /**
   * Verifies that the method
   * <code>{@link Objects#nullSafeHashCode(Object)}</code> calls
   * <code>{@link Objects#nullSafeHashCode(boolean[])}</code> is the given
   * object is an array of <code>boolean</code>.
   */
  public void testNullSafeHashCodeWithObjectBeingBooleanArray() {
    Object array = new boolean[] { true, false };
    int expected = Objects.nullSafeHashCode((boolean[]) array);
    assertEqualHashCodes(expected, array);
  }

  /**
   * Verifies that the method
   * <code>{@link Objects#nullSafeHashCode(Object)}</code> calls
   * <code>{@link Objects#nullSafeHashCode(byte[])}</code> is the given object
   * is an array of <code>byte</code>.
   */
  public void testNullSafeHashCodeWithObjectBeingByteArray() {
    Object array = new byte[] { 6, 39 };
    int expected = Objects.nullSafeHashCode((byte[]) array);
    assertEqualHashCodes(expected, array);
  }

  /**
   * Verifies that the method
   * <code>{@link Objects#nullSafeHashCode(Object)}</code> calls
   * <code>{@link Objects#nullSafeHashCode(char[])}</code> is the given object
   * is an array of <code>char</code>.
   */
  public void testNullSafeHashCodeWithObjectBeingCharArray() {
    Object array = new char[] { 'l', 'M' };
    int expected = Objects.nullSafeHashCode((char[]) array);
    assertEqualHashCodes(expected, array);
  }

  /**
   * Verifies that the method
   * <code>{@link Objects#nullSafeHashCode(Object)}</code> calls
   * <code>{@link Objects#nullSafeHashCode(double[])}</code> is the given
   * object is an array of <code>double</code>.
   */
  public void testNullSafeHashCodeWithObjectBeingDoubleArray() {
    Object array = new double[] { 68930.993, 9022.009 };
    int expected = Objects.nullSafeHashCode((double[]) array);
    assertEqualHashCodes(expected, array);
  }

  /**
   * Verifies that the method
   * <code>{@link Objects#nullSafeHashCode(Object)}</code> calls
   * <code>{@link Objects#nullSafeHashCode(float[])}</code> is the given
   * object is an array of <code>float</code>.
   */
  public void testNullSafeHashCodeWithObjectBeingFloatArray() {
    Object array = new float[] { 9.9f, 9.54f };
    int expected = Objects.nullSafeHashCode((float[]) array);
    assertEqualHashCodes(expected, array);
  }

  /**
   * Verifies that the method
   * <code>{@link Objects#nullSafeHashCode(Object)}</code> calls
   * <code>{@link Objects#nullSafeHashCode(int[])}</code> is the given object
   * is an array of <code>int</code>.
   */
  public void testNullSafeHashCodeWithObjectBeingIntArray() {
    Object array = new int[] { 89, 32 };
    int expected = Objects.nullSafeHashCode((int[]) array);
    assertEqualHashCodes(expected, array);
  }

  /**
   * Verifies that the method
   * <code>{@link Objects#nullSafeHashCode(Object)}</code> calls
   * <code>{@link Objects#nullSafeHashCode(long[])}</code> is the given object
   * is an array of <code>long</code>.
   */
  public void testNullSafeHashCodeWithObjectBeingLongArray() {
    Object array = new long[] { 4389, 320 };
    int expected = Objects.nullSafeHashCode((long[]) array);
    assertEqualHashCodes(expected, array);
  }

  /**
   * Verifies that the method
   * <code>{@link Objects#nullSafeHashCode(Object)}</code> calls
   * <code>{@link Objects#nullSafeHashCode(Object[])}</code> is the given
   * object is an array of <code>Object</code>.
   */
  public void testNullSafeHashCodeWithObjectBeingObjectArray() {
    Object array = new Object[] { "Luke", "Anakin" };
    int expected = Objects.nullSafeHashCode((Object[]) array);
    assertEqualHashCodes(expected, array);
  }

  /**
   * Verifies that the method
   * <code>{@link Objects#nullSafeHashCode(Object)}</code> calls
   * <code>{@link Objects#nullSafeHashCode(short[])}</code> is the given
   * object is an array of <code>short</code>.
   */
  public void testNullSafeHashCodeWithObjectBeingShortArray() {
    Object array = new short[] { 5, 3 };
    int expected = Objects.nullSafeHashCode((short[]) array);
    assertEqualHashCodes(expected, array);
  }

  public void testNullSafeHashCodeWithObjectEqualToNull() {
    assertEquals(0, Objects.nullSafeHashCode((Object) null));
  }

  public void testNullSafeHashCodeWithShortArray() {
    int expected = 31 * 7 + 70;
    expected = 31 * expected + 8;

    short[] array = { 70, 8 };
    int actual = Objects.nullSafeHashCode(array);

    assertEquals(expected, actual);
  }

  public void testNullSafeHashCodeWithShortArrayEqualToNull() {
    assertEquals(0, Objects.nullSafeHashCode((short[]) null));
  }

  public void testNullSafeToStringWithBooleanArray() {
    boolean[] array = { true, false };
    assertEquals("{true, false}", Objects.nullSafeToString(array));
  }

  public void testNullSafeToStringWithBooleanArrayBeingEmpty() {
    boolean[] array = {};
    assertEquals("{}", Objects.nullSafeToString(array));
  }

  public void testNullSafeToStringWithBooleanArrayEqualToNull() {
    assertEquals("null", Objects.nullSafeToString((boolean[]) null));
  }

  public void testNullSafeToStringWithByteArray() {
    byte[] array = { 5, 8 };
    assertEquals("{5, 8}", Objects.nullSafeToString(array));
  }

  public void testNullSafeToStringWithByteArrayBeingEmpty() {
    byte[] array = {};
    assertEquals("{}", Objects.nullSafeToString(array));
  }

  public void testNullSafeToStringWithByteArrayEqualToNull() {
    assertEquals("null", Objects.nullSafeToString((byte[]) null));
  }

  public void testNullSafeToStringWithCharArray() {
    char[] array = { 'A', 'B' };
    assertEquals("{'A', 'B'}", Objects.nullSafeToString(array));
  }

  public void testNullSafeToStringWithCharArrayBeingEmpty() {
    char[] array = {};
    assertEquals("{}", Objects.nullSafeToString(array));
  }

  public void testNullSafeToStringWithCharArrayEqualToNull() {
    assertEquals("null", Objects.nullSafeToString((char[]) null));
  }

  public void testNullSafeToStringWithDoubleArray() {
    double[] array = { 8594.93, 8594023.95 };
    assertEquals("{8594.93, 8594023.95}", Objects.nullSafeToString(array));
  }

  public void testNullSafeToStringWithDoubleArrayBeingEmpty() {
    double[] array = {};
    assertEquals("{}", Objects.nullSafeToString(array));
  }

  public void testNullSafeToStringWithDoubleArrayEqualToNull() {
    assertEquals("null", Objects.nullSafeToString((double[]) null));
  }

  public void testNullSafeToStringWithFloatArray() {
    float[] array = { 8.6f, 43.8f };
    assertEquals("{8.6, 43.8}", Objects.nullSafeToString(array));
  }

  public void testNullSafeToStringWithFloatArrayBeingEmpty() {
    float[] array = {};
    assertEquals("{}", Objects.nullSafeToString(array));
  }

  public void testNullSafeToStringWithFloatArrayEqualToNull() {
    assertEquals("null", Objects.nullSafeToString((float[]) null));
  }

  public void testNullSafeToStringWithIntArray() {
    int[] array = { 9, 64 };
    assertEquals("{9, 64}", Objects.nullSafeToString(array));
  }

  public void testNullSafeToStringWithIntArrayBeingEmpty() {
    int[] array = {};
    assertEquals("{}", Objects.nullSafeToString(array));
  }

  public void testNullSafeToStringWithIntArrayEqualToNull() {
    assertEquals("null", Objects.nullSafeToString((int[]) null));
  }

  public void testNullSafeToStringWithLongArray() {
    long[] array = { 434l, 23423l };
    assertEquals("{434, 23423}", Objects.nullSafeToString(array));
  }

  public void testNullSafeToStringWithLongArrayBeingEmpty() {
    long[] array = {};
    assertEquals("{}", Objects.nullSafeToString(array));
  }

  public void testNullSafeToStringWithLongArrayEqualToNull() {
    assertEquals("null", Objects.nullSafeToString((long[]) null));
  }

  public void testNullSafeToStringWithObjectArray() {
    Object[] array = { "Han", new Long(43) };
    assertEquals("{'Han', 43}", Objects.nullSafeToString(array));
  }

  public void testNullSafeToStringWithObjectArrayBeingEmpty() {
    Object[] array = {};
    assertEquals("{}", Objects.nullSafeToString(array));
  }

  public void testNullSafeToStringWithObjectArrayEqualToNull() {
    assertEquals("null", Objects.nullSafeToString((Object[]) null));
  }

  public void testNullSafeToStringWithShortArray() {
    short[] array = { 7, 9 };
    assertEquals("{7, 9}", Objects.nullSafeToString(array));
  }

  public void testNullSafeToStringWithShortArrayBeingEmpty() {
    short[] array = {};
    assertEquals("{}", Objects.nullSafeToString(array));
  }

  public void testNullSafeToStringWithShortArrayEqualToNull() {
    assertEquals("null", Objects.nullSafeToString((short[]) null));
  }

  public void testNullSafeToStringWithStringArray() {
    String[] array = { "Luke", "Anakin" };
    assertEquals("{'Luke', 'Anakin'}", Objects.nullSafeToString(array));
  }

  public void testNullSafeToStringWithStringArrayBeingEmpty() {
    String[] array = {};
    assertEquals("{}", Objects.nullSafeToString(array));
  }

  public void testNullSafeToStringWithStringArrayEqualToNull() {
    assertEquals("null", Objects.nullSafeToString((String[]) null));
  }

  /**
   * Asserts that the given hash code is equal to the one obtained from
   * <code>{@link Objects#nullSafeHashCode(Object)}</code>.
   * 
   * @param expected
   *          the expected hash code.
   * @param array
   *          the array of primitives.
   */
  private void assertEqualHashCodes(int expected, Object array) {
    int actual = Objects.nullSafeHashCode(array);

    assertEquals(expected, actual);
    assertTrue(array.hashCode() != actual);
  }

}
