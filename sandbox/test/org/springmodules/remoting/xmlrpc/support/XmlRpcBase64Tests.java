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

import java.util.Arrays;

import org.apache.commons.codec.binary.Base64;

import junit.framework.TestCase;

/**
 * <p>
 * Unit Test for <code>{@link XmlRpcBase64}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.3 $ $Date: 2005/06/20 22:51:22 $
 */
public class XmlRpcBase64Tests extends TestCase {

  /**
   * Primary object that is under test.
   */
  private XmlRpcBase64 base64;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of test case to construct.
   */
  public XmlRpcBase64Tests(String name) {
    super(name);
  }

  /**
   * Sets up the test fixture.
   */
  protected void setUp() throws Exception {
    super.setUp();
  }

  /**
   * Verifies that the constructor
   * <code>{@link XmlRpcBase64#XmlRpcBase64(byte[])}</code> stores the given
   * array of bytes as its internal value.
   */
  public void testConstructorWithByteArrayArgument() {
    byte[] expected = { 0, 2, 6, 4 };
    this.base64 = new XmlRpcBase64(expected);

    assertSame("<Value>", expected, this.base64.getValue());
  }

  /**
   * Verifies that the constructor
   * <code>{@link XmlRpcBase64#XmlRpcBase64(String)}</code> decodes the given
   * String using the base64 algorithm and stores the resulting array of bytes
   * as its internal value.
   */
  public void testConstructorWithStringArgument() {
    byte[] expected = { 0, 2, 6, 4 };
    byte[] encoded = Base64.encodeBase64(expected);

    this.base64 = new XmlRpcBase64(new String(encoded, 0, encoded.length));
    byte[] actual = (byte[]) this.base64.getValue();

    assertTrue("Expected: " + Arrays.toString(expected) + " but was: "
        + Arrays.toString(actual), Arrays.equals(expected, actual));
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcBase64#getMatchingValue(Class)}</code> returns its
   * internal value if the given type represents an array of bytes.
   */
  public void testGetMatchingValueWhenTargetTypeIsByteArray() {
    byte[] expected = { 0, 2, 6, 4 };
    this.base64 = new XmlRpcBase64(expected);

    byte[] actual = (byte[]) this.base64.getMatchingValue(expected.getClass());
    assertSame("<Value>", expected, actual);
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcBase64#getMatchingValue(Class)}</code> returns
   * <code>{@link XmlRpcElement#NOT_MATCHING}</code> if the given type does
   * not represent an array of bytes.
   */
  public void testGetMatchingValueWhenTargetTypeIsNotByteArray() {
    this.base64 = new XmlRpcBase64(new byte[0]);
    Object actual = this.base64.getMatchingValue(String.class);
    assertSame("<Value>", XmlRpcElement.NOT_MATCHING, actual);
  }
}
