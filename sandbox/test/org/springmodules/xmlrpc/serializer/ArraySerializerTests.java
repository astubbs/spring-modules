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

import java.util.Vector;

import javax.swing.JButton;

import junit.framework.TestCase;

/**
 * <p>
 * Unit Tests for <code>{@link ArraySerializer}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.2 $ $Date: 2005/06/02 10:27:44 $
 */
public final class ArraySerializerTests extends TestCase {

  /**
   * Primary object that is under test.
   */
  private ArraySerializer serializer;

  /**
   * Registry of available serializers;
   */
  private XmlRpcSerializerRegistry serializerRegistry;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case to construct.
   */
  public ArraySerializerTests(String name) {
    super(name);
  }

  /**
   * Sets up the test fixture.
   */
  protected void setUp() throws Exception {
    super.setUp();

    this.serializer = new ArraySerializer();
    this.serializerRegistry = new ListSerializerRegistry();
  }

  /**
   * Verifies that the method
   * <code>{@link ArraySerializer#serialize(Object, XmlRpcSerializerRegistry)}</code>
   * returns a <code>{@link Vector}</code> containing each of the serialized
   * elements of the given array.
   */
  public void testSerialize() {
    int[] objectToSerialize = new int[] { 0, 1, 2 };

    Vector expected = new Vector();
    int size = objectToSerialize.length;
    for (int i = 0; i < size; i++) {
      expected.add(new Integer(objectToSerialize[i]));
    }

    Object actual = this.serializer.serialize(objectToSerialize,
        this.serializerRegistry);

    assertEquals("<Serialized object>", expected, actual);
  }

  /**
   * Verifies that the method
   * <code>{@link ArraySerializer#serialize(Object, XmlRpcSerializerRegistry)}</code>
   * returns an empty <code>{@link Vector}</code> if the given array is equal
   * to <code>null</code>.
   */
  public void testSerializeWithArrayEqualToNull() {
    String[] objectToSerialize = null;

    Vector actual = (Vector) this.serializer.serialize(objectToSerialize,
        this.serializerRegistry);
    assertTrue("The returned vector should be empty", actual.isEmpty());
  }

  /**
   * Verifies that the method
   * <code>{@link ArraySerializer#serialize(Object, XmlRpcSerializerRegistry)}</code>
   * throws a <code>{@link ClassNotSupportedException}</code> if the registry
   * of serializers does not contain a serializer for the class of the elements
   * of the given array.
   */
  public void testSerializeWithArrayHavingNotSupportedClass() {
    JButton[] objectToSerialize = new JButton[] { new JButton() };
    try {
      this.serializer.serialize(objectToSerialize, this.serializerRegistry);
      fail("A 'ClassNotSupportedException' should have been thrown");
    } catch (ClassNotSupportedException exception) {
      // we are expecting this class.
    }
  }

  /**
   * Verifies that the method
   * <code>{@link ArraySerializer#serialize(Object, XmlRpcSerializerRegistry)}</code>
   * returns an empty <code>{@link Vector}</code> if the given array is empty.
   */
  public void testSerializeWithEmptyArray() {
    String[] objectToSerialize = new String[0];

    Vector actual = (Vector) this.serializer.serialize(objectToSerialize,
        this.serializerRegistry);
    assertTrue("The returned vector should be empty", actual.isEmpty());
  }
}
