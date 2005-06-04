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
import java.util.Collection;
import java.util.Vector;

import junit.framework.TestCase;

import org.springmodules.xmlrpc.type.XmlRpcTypeHandlerRegistry;

/**
 * <p>
 * Unit Test for <code>{@link AbstractApacheXmlRpcTypeHandler}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/06/04 01:22:39 $
 */
public final class ApacheXmlRpcTypeHandlerTests extends TestCase {

  /**
   * The class to be handled by <code>{@link #typeHandler}</code>.
   */
  private Class supportedClass;

  /**
   * Primary object that is under test.
   */
  private AbstractApacheXmlRpcTypeHandler typeHandler;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case to construct.
   */
  public ApacheXmlRpcTypeHandlerTests(String name) {
    super(name);
  }

  /**
   * Sets up the test fixture.
   */
  protected void setUp() throws Exception {
    super.setUp();

    this.supportedClass = Collection.class;
    final Class clazz = this.supportedClass;

    this.typeHandler = new AbstractApacheXmlRpcTypeHandler() {

      public Class getSupportedClass() {
        return clazz;
      }

      protected Object handle(Object obj, XmlRpcTypeHandlerRegistry registry) {
        return obj;
      }
    };
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractApacheXmlRpcTypeHandler#handleType(Object, XmlRpcTypeHandlerRegistry)}</code>
   * can handle arrays.
   */
  public void testHandleTypeWhenObjectIsArray() {
    String[] names = { "Luke", "Leia", "Han" };

    Vector handled = (Vector) this.typeHandler.handleType(names, null);

    int nameCount = names.length;
    assertEquals("<Vector length>", nameCount, handled.size());

    for (int i = 0; i < nameCount; i++) {
      String expected = names[i];
      Object actual = handled.get(i);

      assertEquals(expected, actual);
    }
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractApacheXmlRpcTypeHandler#handleType(Object, XmlRpcTypeHandlerRegistry)}</code>
   * returns an empty string if the specified object is <code>null</code>.
   */
  public void testHandleTypeWhenObjectIsEqualToNull() {
    Object handled = this.typeHandler.handleType(null, null);

    assertEquals("<Handled object>", "", handled);
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractApacheXmlRpcTypeHandler#handleType(Object, XmlRpcTypeHandlerRegistry)}</code>
   * does not treat the specified object as array if the object is not an array.
   */
  public void testHandleTypeWhenObjectIsNotArray() {
    String name = "Vader";

    Object handled = this.typeHandler.handleType(name, null);

    assertEquals("<Handled object>", name, handled);
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractApacheXmlRpcTypeHandler#isSupported(Class)}</code>
   * returns <code>true</code> if the specified class is a subclass of the
   * class supported by the type handler.
   */
  public void testIsSupportedWithSameClass() {
    Class targetClass = this.supportedClass;
    boolean supported = this.typeHandler.isSupported(targetClass);

    assertEquals("Class '" + targetClass.getName() + "' is supported",
        this.supportedClass.isAssignableFrom(targetClass), supported);
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractApacheXmlRpcTypeHandler#isSupported(Class)}</code>
   * returns <code>true</code> if the specified class is a subclass of the
   * class supported by the type handler.
   */
  public void testIsSupportedWithSubclassOfSupportedClass() {
    Class targetClass = ArrayList.class;
    boolean supported = this.typeHandler.isSupported(targetClass);

    assertEquals("Class '" + targetClass.getName() + "' is supported",
        this.supportedClass.isAssignableFrom(targetClass), supported);
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractApacheXmlRpcTypeHandler#isSupported(Class)}</code>
   * returns <code>false</code> if the class supported by the type handler is
   * not assignable from the given class.
   */
  public void testIsSupportedWithSupportedClassNotAssignableFromGivenClass() {
    Class targetClass = String.class;
    boolean supported = this.typeHandler.isSupported(targetClass);

    assertEquals("Class '" + targetClass.getName() + "' is supported",
        this.supportedClass.isAssignableFrom(targetClass), supported);
  }
}
