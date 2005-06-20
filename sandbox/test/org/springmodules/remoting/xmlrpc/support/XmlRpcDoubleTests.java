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
 * Unit Tests for <code>{@link XmlRpcDouble}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/06/20 04:39:39 $
 */
public class XmlRpcDoubleTests extends TestCase {

  /**
   * Primary object that is under test.
   */
  private XmlRpcDouble xmlRpcDouble;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case to construct.
   */
  public XmlRpcDoubleTests(String name) {
    super(name);
  }

  /**
   * Verifies that the constructor
   * <code>{@link XmlRpcDouble#XmlRpcDouble(Double)}</code> stores the given
   * <code>{@link Double}</code> as its internal value.
   */
  public void testConstructorWithDoubleArgument() {
    Double expected = new Double(884.09);
    this.xmlRpcDouble = new XmlRpcDouble(expected);
    assertSame("<Value>", expected, this.xmlRpcDouble.getValue());
  }

  /**
   * Verifies that the constructor
   * <code>{@link XmlRpcDouble#XmlRpcDouble(Double)}</code> parses the given
   * String and stores the resulting <code>Double</code> as its internal
   * value.
   */
  public void testConstructorWithStringArgument() {
    Double expected = new Double(884.09);
    this.xmlRpcDouble = new XmlRpcDouble(expected.toString());
    assertEquals("<Value>", expected, this.xmlRpcDouble.getValue());
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcDateTime#getMatchingValue(Class)}</code> returns its
   * internal value if the given type represents a double-precision decimal
   * number.
   */
  public void testGetMatchingValue() {
    Double expected = new Double(884.09);
    this.xmlRpcDouble = new XmlRpcDouble(expected);

    Class[] types = { Double.class, Double.TYPE };
    int typeCount = types.length;

    for (int i = 0; i < typeCount; i++) {
      Object actual = this.xmlRpcDouble.getMatchingValue(types[i]);
      assertSame("<Matching value>", expected, actual);
    }
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcDouble#getMatchingValue(Class)}</code> returns
   * <code>{@link XmlRpcElement#NOT_MATCHING}</code> if the specified type
   * does not represent a double-precision decimal number.
   */
  public void testGetMatchingValueWhenTypeIsNotBoolean() {
    this.xmlRpcDouble = new XmlRpcDouble();

    Object actual = this.xmlRpcDouble.getMatchingValue(String.class);
    assertSame("<Matching value>", XmlRpcElement.NOT_MATCHING, actual);
  }
}
