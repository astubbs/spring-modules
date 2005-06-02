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
package org.springmodules.xmlrpc.serializer;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import junit.framework.TestCase;

/**
 * <p>
 * Unit Tests for <code>{@link BeanSerializer}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/06/02 10:28:11 $
 */
public final class BeanSerializerTests extends TestCase {

  /**
   * The JavaBean to serialize.
   */
  private Person person;

  /**
   * Primary object that is under test.
   */
  private BeanSerializer serializer;

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
  public BeanSerializerTests(String name) {
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

    this.serializer = new BeanSerializer();
    this.serializer.setProperties(properties);
    this.serializer.setSupportedClass(this.person.getClass());

    this.serializerRegistry = new ListSerializerRegistry();
  }

  /**
   * Verifies that the method
   * <code>{@link BeanSerializer#serialize(Object, XmlRpcSerializerRegistry)}</code>
   * creates a <code>{@link Hashtable}</code> containing the properties of the
   * bean to serialize.
   */
  public void testSerialize() {
    Hashtable expected = new Hashtable();
    expected.put("age", new Integer(this.person.getAge()));
    expected.put("firstName", this.person.getFirstName());
    expected.put("lastName", this.person.getLastName());

    Object actual = this.serializer.serialize(this.person,
        this.serializerRegistry);

    assertEquals("<Serialized object>", expected, actual);
  }

  /**
   * Verifies that the method
   * <code>{@link BeanSerializer#serialize(Object, XmlRpcSerializerRegistry)}</code>
   * creates a <code>{@link Hashtable}</code> containing the properties of the
   * bean to serialize.
   */
  public void testSerializeWithNullObject() {
    Hashtable actual = (Hashtable) this.serializer.serialize(null,
        this.serializerRegistry);

    assertTrue("The serialized object should be an empty Hashtable", actual
        .isEmpty());
  }

  /**
   * Verifies that the method
   * <code>{@link BeanSerializer#serialize(Object, XmlRpcSerializerRegistry)}</code>
   * creates a <code>{@link Hashtable}</code> containing the properties of the
   * bean to serialize even if a JavaBean property is <code>null</code>.
   */
  public void testSerializeWithNullProperty() {
    this.person.setLastName(null);

    Hashtable expected = new Hashtable();
    expected.put("age", new Integer(this.person.getAge()));
    expected.put("firstName", this.person.getFirstName());
    expected.put("lastName", "");

    Object actual = this.serializer.serialize(this.person,
        this.serializerRegistry);

    assertEquals("<Serialized object>", expected, actual);
  }

}
