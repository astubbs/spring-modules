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
 * Unit Tests for <code>{@link XmlRpcInteger}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public class XmlRpcIntegerTests extends TestCase {

  /**
   * Primary object that is under test.
   */
  private XmlRpcInteger xmlRpcInteger;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case to construct.
   */
  public XmlRpcIntegerTests(String name) {
    super(name);
  }

  /**
   * Verifies that the constructor
   * <code>{@link XmlRpcInteger#XmlRpcInteger(Integer)}</code> stores the
   * given <code>{@link Integer}</code> as its internal value.
   */
  public void testConstructorWithIntegerArgument() {
    Integer expected = new Integer(98);
    this.xmlRpcInteger = new XmlRpcInteger(expected);
    assertSame("<Value>", expected, this.xmlRpcInteger.getValue());
  }

  /**
   * Verifies that the constructor
   * <code>{@link XmlRpcInteger#XmlRpcInteger(Integer)}</code> parses the
   * given String and stores the resulting <code>Integer</code> as its
   * internal value.
   */
  public void testConstructorWithStringArgument() {
    Integer expected = new Integer(83);
    this.xmlRpcInteger = new XmlRpcInteger(expected.toString());
    assertEquals("<Value>", expected, this.xmlRpcInteger.getValue());
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcDateTime#getMatchingValue(Class)}</code> returns its
   * internal value if the given type represents an integer.
   */
  public void testGetMatchingValueWhenTargetTypeIsInteger() {
    Integer expected = new Integer(45);
    this.xmlRpcInteger = new XmlRpcInteger(expected);

    Class[] types = { Integer.class, Integer.TYPE };
    int typeCount = types.length;

    for (int i = 0; i < typeCount; i++) {
      Object actual = this.xmlRpcInteger.getMatchingValue(types[i]);
      assertSame("<Matching value>", expected, actual);
    }
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcInteger#getMatchingValue(Class)}</code> returns
   * <code>{@link XmlRpcElement#NOT_MATCHING}</code> if the specified type
   * does not represent an integer.
   */
  public void testGetMatchingValueWhenTargetTypeIsNotInteger() {
    this.xmlRpcInteger = new XmlRpcInteger();

    Object actual = this.xmlRpcInteger.getMatchingValue(String.class);
    assertSame("<Matching value>", XmlRpcElement.NOT_MATCHING, actual);
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcInteger#getValueAsString()}</code> returns a String
   * containing the internal value of the <code>XmlRpcInteger</code>.
   */
  public void testGetValueAsString() {
    Integer value = new Integer(85);
    this.xmlRpcInteger = new XmlRpcInteger(value);

    assertEquals("<Value as String>", value.toString(), this.xmlRpcInteger
        .getValueAsString());
  }
}
