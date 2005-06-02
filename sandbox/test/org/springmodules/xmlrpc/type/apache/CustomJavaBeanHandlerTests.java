/* 
 * Created on Jun 2, 2005
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
import java.util.Hashtable;
import java.util.List;

import org.springmodules.xmlrpc.type.XmlRpcTypeHandlerRegistry;
import org.springmodules.xmlrpc.type.apache.CustomJavaBeanHandler;
import org.springmodules.xmlrpc.type.apache.ListTypeHandlerRegistry;

import junit.framework.TestCase;

/**
 * <p>
 * Unit Tests for <code>{@link CustomJavaBeanHandler}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/06/02 23:31:46 $
 */
public final class CustomJavaBeanHandlerTests extends TestCase {

  /**
   * The JavaBean to serialize.
   */
  private Person person;

  /**
   * Primary object that is under test.
   */
  private CustomJavaBeanHandler typeHandler;

  /**
   * Registry of available type handlers;
   */
  private XmlRpcTypeHandlerRegistry registry;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case to construct.
   */
  public CustomJavaBeanHandlerTests(String name) {
    super(name);
  }

  /**
   * Sets up the test fixture.
   */
  protected void setUp() throws Exception {
    super.setUp();

    this.person = new Person();
    this.person.setAge(18);
    this.person.setFirstName("Leia");
    this.person.setLastName("Organa");

    List properties = new ArrayList();
    properties.add("age");
    properties.add("firstName");
    properties.add("lastName");

    this.typeHandler = new CustomJavaBeanHandler();
    this.typeHandler.setProperties(properties);
    this.typeHandler.setSupportedClass(this.person.getClass());

    this.registry = new ListTypeHandlerRegistry();
  }

  /**
   * Verifies that the method
   * <code>{@link CustomJavaBeanHandler#handleType(Object, XmlRpcTypeHandlerRegistry)}</code>
   * creates a <code>{@link Hashtable}</code> containing the properties of the
   * bean to handle.
   */
  public void testSerialize() {
    Hashtable expected = new Hashtable();
    expected.put("age", new Integer(this.person.getAge()));
    expected.put("firstName", this.person.getFirstName());
    expected.put("lastName", this.person.getLastName());

    Object actual = this.typeHandler.handle(this.person, this.registry);

    assertEquals("<Serialized object>", expected, actual);
  }

  /**
   * Verifies that the method
   * <code>{@link CustomJavaBeanHandler#handleType(Object, XmlRpcTypeHandlerRegistry)}</code>
   * creates a <code>{@link Hashtable}</code> containing the properties of the
   * bean to handle.
   */
  public void testSerializeWithNullObject() {
    Hashtable actual = (Hashtable) this.typeHandler.handle(null, this.registry);

    assertTrue("The obtained Hashtable should be empty", actual.isEmpty());
  }

  /**
   * Verifies that the method
   * <code>{@link CustomJavaBeanHandler#handleType(Object, XmlRpcTypeHandlerRegistry)}</code>
   * creates a <code>{@link Hashtable}</code> containing the properties of the
   * bean to handle even if a JavaBean property is <code>null</code>.
   */
  public void testSerializeWithNullProperty() {
    this.person.setLastName(null);

    Hashtable expected = new Hashtable();
    expected.put("age", new Integer(this.person.getAge()));
    expected.put("firstName", this.person.getFirstName());
    expected.put("lastName", "");

    Object actual = this.typeHandler.handle(this.person, this.registry);

    assertEquals("<Serialized object>", expected, actual);
  }

}
