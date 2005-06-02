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

import java.util.ArrayList;
import java.util.Collection;

import junit.framework.TestCase;

/**
 * <p>
 * Unit Test for <code>{@link AbstractXmlRpcSerializer}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/06/02 00:27:15 $
 */
public final class AbstractXmlRpcSerializerTests extends TestCase {

  /**
   * Primary object that is under test.
   */
  private AbstractXmlRpcSerializer serializer;

  /**
   * The class to be handled by <code>{@link #serializer}</code>.
   */
  private Class supportedClass;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case to construct.
   */
  public AbstractXmlRpcSerializerTests(String name) {
    super(name);
  }

  /**
   * Sets up the test fixture.
   */
  protected void setUp() throws Exception {
    super.setUp();

    this.supportedClass = Collection.class;
    final Class clazz = this.supportedClass;

    this.serializer = new AbstractXmlRpcSerializer() {

      public Class getSupportedClass() {
        return clazz;
      }

      public Object serialize(Object obj,
          XmlRpcSerializerRegistry serializerRegistry) {
        return null;
      }
    };
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractXmlRpcSerializer#isSupported(Class)}</code> returns
   * <code>true</code> if the specified class is a subclass of the class
   * supported by the serializer.
   */
  public void testIsSupportedWithSameClass() {
    Class targetClass = this.supportedClass;
    boolean supported = this.serializer.isSupported(targetClass);

    assertEquals("Class '" + targetClass.getName() + "' is supported",
        this.supportedClass.isAssignableFrom(targetClass), supported);
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractXmlRpcSerializer#isSupported(Class)}</code> returns
   * <code>true</code> if the specified class is a subclass of the class
   * supported by the serializer.
   */
  public void testIsSupportedWithSubclassOfSupportedClass() {
    Class targetClass = ArrayList.class;
    boolean supported = this.serializer.isSupported(targetClass);

    assertEquals("Class '" + targetClass.getName() + "' is supported",
        this.supportedClass.isAssignableFrom(targetClass), supported);
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractXmlRpcSerializer#isSupported(Class)}</code> returns
   * <code>false</code> if the class supported by the serializer is not
   * assignable from the given class.
   */
  public void testIsSupportedWithSupportedClassNotAssignableFromGivenClass() {
    Class targetClass = String.class;
    boolean supported = this.serializer.isSupported(targetClass);

    assertEquals("Class '" + targetClass.getName() + "' is supported",
        this.supportedClass.isAssignableFrom(targetClass), supported);
  }
}
