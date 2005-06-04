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
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;

import org.springmodules.xmlrpc.type.ClassNotSupportedException;
import org.springmodules.xmlrpc.type.XmlRpcTypeHandlerRegistry;

/**
 * <p>
 * Unit Tests for <code>{@link CollectionHandler}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.2 $ $Date: 2005/06/04 01:24:02 $
 */
public final class CollectionHandlerTests extends
    AbstractApacheXmlRpcTypeHandlerTestCase {

  /**
   * Object to be handled by <code>{@link #typeHandler}</code>
   */
  private List objectToHandle;

  /**
   * Registry of available data type handlers;
   */
  private XmlRpcTypeHandlerRegistry registry;

  /**
   * Primary object that is under test.
   */
  private CollectionHandler typeHandler;

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
   * @see AbstractApacheXmlRpcTypeHandlerTestCase#getExpectedHandledObject()
   */
  protected Object getExpectedHandledObject() {
    Vector expected = new Vector();
    int size = this.objectToHandle.size();
    for (int i = 0; i < size; i++) {
      expected.add(this.objectToHandle.get(i));
    }

    return expected;
  }

  /**
   * @see AbstractApacheXmlRpcTypeHandlerTestCase#getExpectedSupportedClass()
   */
  protected Class getExpectedSupportedClass() {
    return Collection.class;
  }

  /**
   * @see AbstractApacheXmlRpcTypeHandlerTestCase#getObjectToHandle()
   */
  protected Object getObjectToHandle() {
    return this.objectToHandle;
  }

  /**
   * @see AbstractApacheXmlRpcTypeHandlerTestCase#getTypeHandler()
   */
  protected AbstractApacheXmlRpcTypeHandler getTypeHandler() {
    return this.typeHandler;
  }

  /**
   * @see AbstractApacheXmlRpcTypeHandlerTestCase#getTypeHandlerRegistry()
   */
  protected XmlRpcTypeHandlerRegistry getTypeHandlerRegistry() {
    return this.registry;
  }

  /**
   * Sets up the test fixture.
   */
  protected void onSetUp() throws Exception {
    super.onSetUp();

    this.typeHandler = new CollectionHandler();
    this.registry = new ListTypeHandlerRegistry();
    this.objectToHandle = Arrays.asList(new String[] { "Leia", "Luke", "Han" });
  }

  /**
   * Verifies that the method
   * <code>{@link CollectionHandler#handle(Object, XmlRpcTypeHandlerRegistry)}</code>
   * returns an empty <code>{@link Vector}</code> if the given collection is
   * equal to <code>null</code>.
   */
  public void testHandleWithCollectionEqualToNull() {
    Vector actual = (Vector) this.typeHandler.handle(null, this.registry);
    assertTrue("The returned vector should be empty", actual.isEmpty());
  }

  /**
   * Verifies that the method
   * <code>{@link CollectionHandler#handle(Object, XmlRpcTypeHandlerRegistry)}</code>
   * throws a <code>{@link ClassNotSupportedException}</code> if the registry
   * of type handlers does not contain a handler for the class of the elements
   * of the given collection.
   */
  public void testHandleWithCollectionHavingNotSupportedClass() {
    this.objectToHandle = new ArrayList();

    this.objectToHandle.add(new JButton());
    try {
      this.typeHandler.handle(this.objectToHandle, this.registry);
      fail("A 'ClassNotSupportedException' should have been thrown");
    } catch (ClassNotSupportedException exception) {
      // we are expecting this class.
    }
  }

  /**
   * Verifies that the method
   * <code>{@link CollectionHandler#handle(Object, XmlRpcTypeHandlerRegistry)}</code>
   * returns an empty <code>{@link Vector}</code> if the given collection is
   * empty.
   */
  public void testHandleWithEmptyCollection() {
    this.objectToHandle = new ArrayList();

    Vector actual = (Vector) this.typeHandler.handle(this.objectToHandle,
        this.registry);
    assertTrue("The returned vector should be empty", actual.isEmpty());
  }
}
