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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import junit.framework.TestCase;

import org.easymock.MockControl;

/**
 * <p>
 * Unit Tests for <code>{@link XmlRpcArray}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/06/19 12:39:11 $
 */
public class XmlRpcArrayTests extends TestCase {

  /**
   * Primary object that is under test.
   */
  private XmlRpcArray array;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case to construct.
   */
  public XmlRpcArrayTests(String name) {
    super(name);
  }

  /**
   * Sets up the test fixture.
   */
  protected void setUp() throws Exception {
    super.setUp();

    this.array = new XmlRpcArray();
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcArray#getMatchingValue(Class)}</code> returns an array
   * of matching values if the specified type represents an array and each of
   * the values of the <code>XmlRpcArray</code> returns a matching value.
   */
  public void testGetMatchingValueWhenTypeIsArray() {
    MockControl firstValueControl = MockControl
        .createControl(XmlRpcElement.class);
    XmlRpcElement firstValue = (XmlRpcElement) firstValueControl.getMock();
    this.array.add(firstValue);

    MockControl secondValueControl = MockControl
        .createControl(XmlRpcElement.class);
    XmlRpcElement secondValue = (XmlRpcElement) secondValueControl.getMock();
    this.array.add(secondValue);

    String[] expected = { "firstValue", "secondValue" };
    Class type = expected.getClass();
    int i = 0;

    // expectation: first value returns a matching value.
    firstValue.getMatchingValue(String.class);
    firstValueControl.setReturnValue(expected[i++]);

    // expectation: second value returns a matching value.
    secondValue.getMatchingValue(String.class);
    secondValueControl.setReturnValue(expected[i]);

    // set the state of the mock objects to "replay"
    firstValueControl.replay();
    secondValueControl.replay();

    String[] actual = (String[]) this.array.getMatchingValue(type);

    assertTrue("Expected: " + Arrays.toString(expected) + " but was: "
        + Arrays.toString(actual), Arrays.equals(expected, actual));

    // verify the expectations were met.
    firstValueControl.verify();
    secondValueControl.verify();
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcArray#getMatchingValue(Class)}</code> returns an array
   * <code>{@link XmlRpcElement#NOT_MATCHING}</code> if the specified type
   * represents an array and any of the values of the <code>XmlRpcArray</code>
   * returns <code>{@link XmlRpcElement#NOT_MATCHING}</code>.
   */
  public void testGetMatchingValueWhenTypeIsArrayAndElementReturnsNotMatchingValue() {
    MockControl valueControl = MockControl.createControl(XmlRpcElement.class);
    XmlRpcElement value = (XmlRpcElement) valueControl.getMock();
    this.array.add(value);

    String[] stringArray = new String[0];
    Class type = stringArray.getClass();

    // expectation: first value returns a not matching value.
    value.getMatchingValue(String.class);
    valueControl.setReturnValue(XmlRpcElement.NOT_MATCHING);

    // set the state of the mock objects to "replay"
    valueControl.replay();

    Object actual = this.array.getMatchingValue(type);

    assertSame("<Matching value>", XmlRpcElement.NOT_MATCHING, actual);

    // verify the expectations were met.
    valueControl.verify();
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcArray#getMatchingValue(Class)}</code> returns
   * <code>{@link XmlRpcElement#NOT_MATCHING}</code> if the specified type
   * represents a collection and any of the values of the
   * <code>XmlRpcArray</code> is not an implementation of
   * <code>{@link XmlRpcScalar}</code>.
   */
  public void testGetMatchingValueWhenTypeIsCollectionAndElementIsNotScalarValue() {
    MockControl valueControl = MockControl.createControl(XmlRpcElement.class);
    XmlRpcElement value = (XmlRpcElement) valueControl.getMock();
    this.array.add(value);

    Class[] types = { Collection.class, List.class, ArrayList.class };
    int typeCount = types.length;

    for (int i = 0; i < typeCount; i++) {
      Class type = types[i];

      // set the state of the mock objects to "replay"
      valueControl.replay();

      Object actual = this.array.getMatchingValue(type);
      assertEquals("<Matching value>", XmlRpcElement.NOT_MATCHING, actual);

      // verify the expectations were met.
      valueControl.verify();
      valueControl.reset();
    }
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcArray#getMatchingValue(Class)}</code> returns an list
   * of matching values if the specified type represents a collection and each
   * of the values of the <code>XmlRpcArray</code> is an implementation of
   * <code>{@link XmlRpcScalar}</code>.
   */
  public void testGetMatchingValueWhenTypeIsCollectionAndElementsAreScalarValues() {
    MockControl firstValueControl = MockControl
        .createControl(XmlRpcScalar.class);
    XmlRpcScalar firstValue = (XmlRpcScalar) firstValueControl.getMock();
    this.array.add(firstValue);

    MockControl secondValueControl = MockControl
        .createControl(XmlRpcScalar.class);
    XmlRpcScalar secondValue = (XmlRpcScalar) secondValueControl.getMock();
    this.array.add(secondValue);

    List expected = Arrays.asList(new String[] { "firstValue", "secondValue" });

    Class[] types = { Collection.class, List.class, ArrayList.class };
    int typeCount = types.length;

    for (int i = 0; i < typeCount; i++) {
      Class type = types[i];
      int j = 0;

      // expectation: first value returns its stored value.
      firstValue.getValue();
      firstValueControl.setReturnValue(expected.get(j++));

      // expectation: second value returns its stored value.
      secondValue.getValue();
      secondValueControl.setReturnValue(expected.get(j));

      // set the state of the mock objects to "replay"
      firstValueControl.replay();
      secondValueControl.replay();

      List actual = (List) this.array.getMatchingValue(type);

      assertEquals("<Matching value>", expected, actual);

      // verify the expectations were met.
      firstValueControl.verify();
      secondValueControl.verify();

      // reset mock controls.
      firstValueControl.reset();
      secondValueControl.reset();
    }
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcArray#getMatchingValue(Class)}</code> returns
   * <code>{@link XmlRpcElement#NOT_MATCHING}</code> if the specified type
   * does not represent an array or a collection.
   */
  public void testGetMatchingValueWhenTypeIsNotArrayNorCollection() {
    Object actual = this.array.getMatchingValue(Object.class);
    assertEquals("<Matching value>", XmlRpcElement.NOT_MATCHING, actual);
  }

  /**
   * Verifies that the method <code>{@link XmlRpcArray#getValues()}</code>
   * returns an array containing all the values stored in the
   * <code>XmlRpcArray</code>.
   */
  public void testGetValues() {
    XmlRpcElement[] expected = { new XmlRpcBase64(), new XmlRpcBoolean(),
        new XmlRpcDateTime() };

    int expectedCount = expected.length;

    for (int i = 0; i < expectedCount; i++) {
      this.array.add(expected[i]);
    }

    XmlRpcElement[] actual = this.array.getValues();

    assertTrue("Expected: " + Arrays.toString(expected) + " but was: "
        + Arrays.toString(actual), Arrays.equals(expected, actual));
  }
}
