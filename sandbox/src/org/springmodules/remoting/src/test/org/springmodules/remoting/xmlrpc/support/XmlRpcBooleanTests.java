/* 
 * Created on Jun 17, 2005
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
 * Unit Tests for <code>{@link XmlRpcBoolean}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public class XmlRpcBooleanTests extends TestCase {

  /**
   * Primary object that is under test.
   */
  private XmlRpcBoolean xmlRpcBoolean;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case to construct.
   */
  public XmlRpcBooleanTests(String name) {
    super(name);
  }

  /**
   * Verifies that the constructor
   * <code>{@link XmlRpcBoolean#XmlRpcBoolean(Boolean)}</code> stores the
   * given <code>{@link Boolean}</code> as its internal value.
   */
  public void testConstructorWithBooleanValue() {
    Boolean expected = Boolean.TRUE;
    this.xmlRpcBoolean = new XmlRpcBoolean(expected);
    assertSame("<Value>", expected, this.xmlRpcBoolean.getValue());
  }

  /**
   * Verifies that the constructor
   * <code>{@link XmlRpcBoolean#XmlRpcBoolean(Boolean)}</code> stores
   * <code>{@link Boolean#TRUE}</code> as its internal value if the given
   * String is equal to "1" ("1" represents <code>true</code> in XML-RPC).
   */
  public void testConstructorWithStringValueEqualToTrue() {
    this.xmlRpcBoolean = new XmlRpcBoolean("1");
    assertEquals("<Value>", Boolean.TRUE, this.xmlRpcBoolean.getValue());
  }

  /**
   * Verifies that the constructor
   * <code>{@link XmlRpcBoolean#XmlRpcBoolean(Boolean)}</code> stores
   * <code>{@link Boolean#FALSE}</code> as its internal value if the given
   * String is not equal to "1" ("1" represents <code>true</code> in XML-RPC).
   */
  public void testConstructorWithStringValueNotEqualToTrue() {
    this.xmlRpcBoolean = new XmlRpcBoolean("0");
    assertEquals("<Value>", Boolean.FALSE, this.xmlRpcBoolean.getValue());
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcBoolean#getMatchingValue(Class)}</code> returns the
   * value stored in the <code>XmlRpcBoolean</code> when the specified type is
   * <code>boolean</code> or <code>Boolean</code>.
   */
  public void testGetMatchingValueWhenTargetTypeIsBoolean() {
    Boolean expected = Boolean.TRUE;
    this.xmlRpcBoolean = new XmlRpcBoolean(expected);

    Class[] types = { Boolean.class, Boolean.TYPE };
    int typeCount = types.length;

    for (int i = 0; i < typeCount; i++) {
      Object actual = this.xmlRpcBoolean.getMatchingValue(types[i]);
      assertSame("<Matching value>", expected, actual);
    }
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcBoolean#getMatchingValue(Class)}</code> returns
   * <code>{@link XmlRpcElement#NOT_MATCHING}</code> if the specified type
   * does not represent a boolean.
   */
  public void testGetMatchingValueWhenTargetTypeIsNotBoolean() {
    this.xmlRpcBoolean = new XmlRpcBoolean();

    Object actual = this.xmlRpcBoolean.getMatchingValue(String.class);
    assertSame("<Matching value>", XmlRpcElement.NOT_MATCHING, actual);
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcBoolean#getValueAsString()}</code> returns "0" if the
   * internal value <code>XmlRpcBoolean</code> is
   * <code>{@link Boolean#FALSE}</code>.
   */
  public void testGetValueAsStringWithValueEqualToFalse() {
    this.xmlRpcBoolean = new XmlRpcBoolean(Boolean.FALSE);

    assertEquals("<Value as String>", "0", this.xmlRpcBoolean
        .getValueAsString());
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcBoolean#getValueAsString()}</code> returns "1" if the
   * internal value <code>XmlRpcBoolean</code> is
   * <code>{@link Boolean#TRUE}</code>.
   */
  public void testGetValueAsStringWithValueEqualToTrue() {
    this.xmlRpcBoolean = new XmlRpcBoolean(Boolean.TRUE);

    assertEquals("<Value as String>", "1", this.xmlRpcBoolean
        .getValueAsString());
  }
}
