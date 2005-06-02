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

import org.springmodules.xmlrpc.type.XmlRpcTypeHandlerRegistry;
import org.springmodules.xmlrpc.type.apache.AbstractApacheXmlRpcTypeHandler;

import junit.framework.TestCase;

/**
 * <p>
 * Unit Test for <code>{@link AbstractApacheXmlRpcTypeHandler}</code>.
 * </p>
 * TODO: Add tests for method 'handle'.
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/06/02 23:31:46 $
 */
public final class AbstractXmlRpcTypeHandlerTests extends TestCase {

  /**
   * Primary object that is under test.
   */
  private AbstractApacheXmlRpcTypeHandler typeHandler;

  /**
   * The class to be handled by <code>{@link #typeHandler}</code>.
   */
  private Class supportedClass;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case to construct.
   */
  public AbstractXmlRpcTypeHandlerTests(String name) {
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
        return null;
      }
    };
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractApacheXmlRpcTypeHandler#isSupported(Class)}</code> returns
   * <code>true</code> if the specified class is a subclass of the class
   * supported by the type handler.
   */
  public void testIsSupportedWithSameClass() {
    Class targetClass = this.supportedClass;
    boolean supported = this.typeHandler.isSupported(targetClass);

    assertEquals("Class '" + targetClass.getName() + "' is supported",
        this.supportedClass.isAssignableFrom(targetClass), supported);
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractApacheXmlRpcTypeHandler#isSupported(Class)}</code> returns
   * <code>true</code> if the specified class is a subclass of the class
   * supported by the type handler.
   */
  public void testIsSupportedWithSubclassOfSupportedClass() {
    Class targetClass = ArrayList.class;
    boolean supported = this.typeHandler.isSupported(targetClass);

    assertEquals("Class '" + targetClass.getName() + "' is supported",
        this.supportedClass.isAssignableFrom(targetClass), supported);
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractApacheXmlRpcTypeHandler#isSupported(Class)}</code> returns
   * <code>false</code> if the class supported by the type handler is not
   * assignable from the given class.
   */
  public void testIsSupportedWithSupportedClassNotAssignableFromGivenClass() {
    Class targetClass = String.class;
    boolean supported = this.typeHandler.isSupported(targetClass);

    assertEquals("Class '" + targetClass.getName() + "' is supported",
        this.supportedClass.isAssignableFrom(targetClass), supported);
  }
}
