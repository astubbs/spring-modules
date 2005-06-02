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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;

import org.springmodules.xmlrpc.type.ClassNotSupportedException;
import org.springmodules.xmlrpc.type.XmlRpcTypeHandlerRegistry;
import org.springmodules.xmlrpc.type.apache.CollectionHandler;
import org.springmodules.xmlrpc.type.apache.ListTypeHandlerRegistry;

import junit.framework.TestCase;

/**
 * <p>
 * Unit Tests for <code>{@link CollectionHandler}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/06/02 23:31:46 $
 */
public final class CollectionHandlerTests extends TestCase {

  /**
   * Primary object that is under test.
   */
  private CollectionHandler typeHandler;

  /**
   * Registry of available data type handlers;
   */
  private XmlRpcTypeHandlerRegistry registry;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case to construct.
   */
  public CollectionHandlerTests(String name) {
    super(name);
  }

  /**
   * Sets up the test fixture.
   */
  protected void setUp() throws Exception {
    super.setUp();

    this.typeHandler = new CollectionHandler();
    this.registry = new ListTypeHandlerRegistry();
  }

  /**
   * Verifies that the method
   * <code>{@link CollectionHandler#handleType(Object, XmlRpcTypeHandlerRegistry)}</code>
   * returns a <code>{@link Vector}</code> containing each of the elements of
   * the given collection.
   */
  public void testSerialize() {
    List objectToSerialize = Arrays
        .asList(new String[] { "Leia", "Luke", "Han" });

    Vector expected = new Vector();
    int size = objectToSerialize.size();
    for (int i = 0; i < size; i++) {
      expected.add(objectToSerialize.get(i));
    }

    Object actual = this.typeHandler.handle(objectToSerialize, this.registry);

    assertEquals("<Serialized object>", expected, actual);
  }

  /**
   * Verifies that the method
   * <code>{@link CollectionHandler#handleType(Object, XmlRpcTypeHandlerRegistry)}</code>
   * returns an empty <code>{@link Vector}</code> if the given collection is
   * equal to <code>null</code>.
   */
  public void testSerializeWithCollectionEqualToNull() {
    List objectToSerialize = null;

    Vector actual = (Vector) this.typeHandler.handle(objectToSerialize,
        this.registry);
    assertTrue("The returned vector should be empty", actual.isEmpty());
  }

  /**
   * Verifies that the method
   * <code>{@link CollectionHandler#handleType(Object, XmlRpcTypeHandlerRegistry)}</code>
   * throws a <code>{@link ClassNotSupportedException}</code> if the registry
   * of type handlers does not contain a handler for the class of the elements
   * of the given collection.
   */
  public void testSerializeWithCollectionHavingNotSupportedClass() {
    List objectToSerialize = new ArrayList();
    objectToSerialize.add(new JButton());
    try {
      this.typeHandler.handle(objectToSerialize, this.registry);
      fail("A 'ClassNotSupportedException' should have been thrown");
    } catch (ClassNotSupportedException exception) {
      // we are expecting this class.
    }
  }

  /**
   * Verifies that the method
   * <code>{@link CollectionHandler#handleType(Object, XmlRpcTypeHandlerRegistry)}</code>
   * returns an empty <code>{@link Vector}</code> if the given collection is
   * empty.
   */
  public void testSerializeWithEmptyCollection() {
    List objectToSerialize = new ArrayList();

    Vector actual = (Vector) this.typeHandler.handle(objectToSerialize,
        this.registry);
    assertTrue("The returned vector should be empty", actual.isEmpty());
  }
}
