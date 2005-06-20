/* 
 * Created on Jun 20, 2005
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

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.springmodules.remoting.xmlrpc.support.XmlRpcStruct.XmlRpcMember;

/**
 * <p>
 * Unit Tests for <code>{@link XmlRpcStruct}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/06/20 22:51:43 $
 */
public class XmlRpcStructTests extends TestCase {

  /**
   * Primary object that is under test.
   */
  private XmlRpcStruct xmlRpcStruct;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case to construct.
   */
  public XmlRpcStructTests(String name) {
    super(name);
  }

  /**
   * Sets up the test fixture.
   */
  protected void setUp() throws Exception {
    super.setUp();

    this.xmlRpcStruct = new XmlRpcStruct();
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcStruct#add(String, XmlRpcElement)}</code> adds a new
   * member created from the given String and <code>{@link XmlRpcElement}</code>.
   */
  public void testAddStringXmlRpcElement() {
    XmlRpcMember expected = new XmlRpcMember("name", new XmlRpcString("C3-PO"));

    this.xmlRpcStruct.add(expected.name, expected.value);
    XmlRpcMember[] members = this.xmlRpcStruct.getMembers();

    assertEquals("<Member count>", 1, members.length);
    assertEquals("<Member>", expected, members[0]);
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcStruct#add(XmlRpcMember)}</code> adds the given
   * <code>{@link XmlRpcElement}</code> to the list of members.
   */
  public void testAddXmlRpcMember() {
    XmlRpcMember expected = new XmlRpcMember("name", new XmlRpcString("C3-PO"));

    this.xmlRpcStruct.add(expected);
    XmlRpcMember[] members = this.xmlRpcStruct.getMembers();

    assertEquals("<Member count>", 1, members.length);
    assertSame("<Member>", expected, members[0]);
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcStruct#getMatchingValue(Class)}</code> returns
   * <code>{@link XmlRpcElement#NOT_MATCHING}</code> containing the members of
   * the struct if the specified class is <code>{@link Map}</code> or
   * <code>{@link HashMap}</code> and the members of the struct are not
   * implementations of <code>{@link XmlRpcScalar}</code>.
   */
  public void testGetMatchingValueWhenTargetClassIsMapAndMembersAreNotScalarValues() {
    this.xmlRpcStruct.add("id", new XmlRpcInteger("569"));
    this.xmlRpcStruct.add("name", new XmlRpcArray());

    Class[] types = { Map.class, HashMap.class };
    int typeCount = types.length;

    for (int i = 0; i < typeCount; i++) {
      Object actual = this.xmlRpcStruct.getMatchingValue(types[0]);
      assertEquals("<Matching value>", XmlRpcElement.NOT_MATCHING, actual);
    }
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcStruct#getMatchingValue(Class)}</code> returns a map
   * containing the members of the struct if the specified class is
   * <code>{@link Map}</code> or <code>{@link HashMap}</code> and the
   * members of the struct are implementations of
   * <code>{@link XmlRpcScalar}</code>.
   */
  public void testGetMatchingValueWhenTargetClassIsMapAndMembersAreScalarValues() {
    Map expected = new HashMap();
    expected.put("id", new Integer(569));
    expected.put("name", "Luke");

    this.xmlRpcStruct.add("id", new XmlRpcInteger("569"));
    this.xmlRpcStruct.add("name", new XmlRpcString("Luke"));

    Class[] types = { Map.class, HashMap.class };
    int typeCount = types.length;

    for (int i = 0; i < typeCount; i++) {
      Object actual = this.xmlRpcStruct.getMatchingValue(types[0]);
      assertEquals("<Matching value>", expected, actual);
    }
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcStruct#getMatchingValue(Class)}</code> returns
   * <code>{@link XmlRpcElement#NOT_MATCHING}</code> if the name of a member
   * of the struct is not the name of a writable property of the JavaBean to
   * create.
   */
  public void testGetMatchingValueWhenTargetClassRepresentsJavaBeanAndMemberNameIsNotWritableProperty() {
    this.xmlRpcStruct.add("role", new XmlRpcString("Sith Lord"));

    Object actual = this.xmlRpcStruct.getMatchingValue(Person.class);
    assertEquals("<Matching value>", XmlRpcElement.NOT_MATCHING, actual);
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcStruct#getMatchingValue(Class)}</code> returns a
   * JavaBean instance of the given target class if the name of the members of
   * the struct are the names of writable properties of the target class.
   */
  public void testGetMatchingValueWhenTargetClassRepresentsJavaBeanAndMemberNamesAreWritableProperties() {
    Person expected = new Person();
    expected.setId(new Integer(9954));
    expected.setName("Vader");

    this.xmlRpcStruct.add("id", new XmlRpcInteger(expected.getId()));
    this.xmlRpcStruct.add("name", new XmlRpcString(expected.getName()));

    Object actual = this.xmlRpcStruct.getMatchingValue(expected.getClass());
    assertEquals("<Matching value>", expected, actual);
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcStruct#getMatchingValue(Class)}</code> returns
   * <code>{@link XmlRpcElement#NOT_MATCHING}</code> if the type of a member
   * of the struct does not match the type of a property of the JavaBean to
   * create.
   */
  public void testGetMatchingValueWhenTargetClassRepresentsJavaBeanAndMemberTypeDoesNotMatchPropertyType() {
    this.xmlRpcStruct.add("id", new XmlRpcString("234"));
    this.xmlRpcStruct.add("name", new XmlRpcString("Ewok"));

    Object actual = this.xmlRpcStruct.getMatchingValue(Person.class);
    assertEquals("<Matching value>", XmlRpcElement.NOT_MATCHING, actual);
  }
}
