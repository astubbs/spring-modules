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

import junit.framework.TestCase;

/**
 * <p>
 * Unit Tests for <code>{@link XmlRpcString}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.2 $ $Date: 2005/06/22 08:51:25 $
 */
public class XmlRpcStringTests extends TestCase {

  /**
   * Primary object that is under test.
   */
  private XmlRpcString xmlRpcString;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case to construct.
   */
  public XmlRpcStringTests(String name) {
    super(name);
  }

  /**
   * Verifies that the constructor
   * <code>{@link XmlRpcString#XmlRpcString(Long)}</code> converts the given
   * <code>{@link Long}</code> to a String and stores it as its internal
   * value.
   */
  public void testConstructorWithLongArgument() {
    Long expected = new Long(432);
    this.xmlRpcString = new XmlRpcString(expected);
    assertEquals("<Value>", expected.toString(), this.xmlRpcString.getValue());
  }

  /**
   * Verifies that the constructor
   * <code>{@link XmlRpcString#XmlRpcString(String)}</code> stores the given
   * String as its internal value.
   */
  public void testConstructorWithStringArgument() {
    String expected = "Leia";
    this.xmlRpcString = new XmlRpcString(expected);
    assertSame("<Value>", expected, this.xmlRpcString.getValue());
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcString#getMatchingValue(Class)}</code> returns
   * <code>{@link XmlRpcElement#NOT_MATCHING}</code> if the given type
   * represents a 64-bit signed integer and the value of the XmlRpcString is not
   * a parsable number.
   */
  public void testGetMatchingValueWhenTargetTypeIsLongAndValueIsNotParsableNumber() {
    this.xmlRpcString = new XmlRpcString("R2-D2");

    Class[] types = { Long.class, Long.TYPE };
    int typeCount = types.length;

    for (int i = 0; i < typeCount; i++) {
      assertEquals("<Value>", XmlRpcElement.NOT_MATCHING, this.xmlRpcString
          .getMatchingValue(types[i]));
    }
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcString#getMatchingValue(Class)}</code> returns its
   * internal value (as <code>{@link Long}</code> if the given type represents
   * a 64-bit signed integer and the value of the XmlRpcString is a parsable
   * number.
   */
  public void testGetMatchingValueWhenTargetTypeIsLongAndValueIsParsableNumber() {
    Long expected = new Long(432);
    this.xmlRpcString = new XmlRpcString(expected);

    Class[] types = { Long.class, Long.TYPE };
    int typeCount = types.length;

    for (int i = 0; i < typeCount; i++) {
      assertEquals("<Value>", expected, this.xmlRpcString
          .getMatchingValue(types[i]));
    }
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcString#getMatchingValue(Class)}</code> returns
   * <code>{@link XmlRpcElement#NOT_MATCHING}</code> if the specified type
   * does not represent a string or a 64-bit signed integer.
   */
  public void testGetMatchingValueWhenTargetTypeIsNotStringNorLong() {
    this.xmlRpcString = new XmlRpcString("");

    Object actual = this.xmlRpcString.getMatchingValue(Integer.class);
    assertSame("<Matching value>", XmlRpcElement.NOT_MATCHING, actual);
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcString#getMatchingValue(Class)}</code> returns its
   * internal value if the given type is <code>{@link String}</code>.
   */
  public void testGetMatchingValueWhenTargetTypeIsString() {
    String expected = "Jabba";
    this.xmlRpcString = new XmlRpcString(expected);
    assertSame("<Value>", expected, this.xmlRpcString
        .getMatchingValue(String.class));
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcString#getValueAsString()}</code> returns a String
   * containing the internal value of the <code>XmlRpcInteger</code>.
   */
  public void testGetValueAsString() {
    String value = "Jabba";
    this.xmlRpcString = new XmlRpcString(value);

    assertEquals("<Value as String>", value, this.xmlRpcString
        .getValueAsString());
  }
}
