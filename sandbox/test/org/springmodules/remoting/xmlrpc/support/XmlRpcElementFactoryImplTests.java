/* 
 * Created on Jul 5, 2005
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

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

/**
 * <p>
 * Unit Tests for <code>{@link XmlRpcElementFactoryImpl}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public class XmlRpcElementFactoryImplTests extends TestCase {

  /**
   * Primary object that is under test.
   */
  private XmlRpcElementFactoryImpl factory;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case to construct.
   */
  public XmlRpcElementFactoryImplTests(String name) {
    super(name);
  }

  /**
   * Sets up the test fixture.
   */
  protected void setUp() throws Exception {
    super.setUp();

    this.factory = new XmlRpcElementFactoryImpl();
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcElementFactoryImpl#createXmlRpcElement(Object)}</code>
   * creates a new <code>{@link XmlRpcArray}</code> if the specified argument
   * is an array (not an array of <code>byte</code>).
   */
  public void testCreateXmlRpcElementWithArrayArgument() {
    String[] source = { "Luke", "Leia", "Han" };

    XmlRpcArray expected = new XmlRpcArray();
    int nameCount = source.length;
    for (int i = 0; i < nameCount; i++) {
      expected.add(new XmlRpcString(source[i]));
    }

    XmlRpcElement actual = this.factory.createXmlRpcElement(source);

    assertEquals("<XML-RPC element>", expected, actual);
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcElementFactoryImpl#createXmlRpcElement(Object)}</code>
   * creates a new <code>{@link XmlRpcBoolean}</code> if the specified
   * argument is a <code>Boolean</code>.
   */
  public void testCreateXmlRpcElementWithBooleanArgument() {
    Boolean source = Boolean.TRUE;
    XmlRpcBoolean expected = new XmlRpcBoolean(source);
    XmlRpcElement actual = this.factory.createXmlRpcElement(source);

    assertEquals("<XML-RPC element>", expected, actual);
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcElementFactoryImpl#createXmlRpcElement(Object)}</code>
   * creates a new <code>{@link XmlRpcBase64}</code> if the specified argument
   * is an array of <code>byte</code>.
   */
  public void testCreateXmlRpcElementWithByteArrayArgument() {
    byte[] source = { 9, 8, 8 };
    XmlRpcBase64 expected = new XmlRpcBase64(source);
    XmlRpcElement actual = this.factory.createXmlRpcElement(source);

    assertEquals("<XML-RPC element>", expected, actual);
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcElementFactoryImpl#createXmlRpcElement(Object)}</code>
   * creates a new <code>{@link XmlRpcString}</code> if the specified argument
   * is a <code>Character</code>.
   */
  public void testCreateXmlRpcElementWithCharacterArgument() {
    Character source = new Character('t');
    XmlRpcString expected = new XmlRpcString(source);
    XmlRpcElement actual = this.factory.createXmlRpcElement(source);

    assertEquals("<XML-RPC element>", expected, actual);
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcElementFactoryImpl#createXmlRpcElement(Object)}</code>
   * creates a new <code>{@link XmlRpcArray}</code> if the specified argument
   * is a <code>java.util.Collection</code>.
   */
  public void testCreateXmlRpcElementWithCollectionArgument() {
    String[] names = { "Luke", "Leia", "Han" };
    Collection source = Arrays.asList(names);

    XmlRpcArray expected = new XmlRpcArray();
    int nameCount = names.length;
    for (int i = 0; i < nameCount; i++) {
      expected.add(new XmlRpcString(names[i]));
    }

    XmlRpcElement actual = this.factory.createXmlRpcElement(source);

    assertEquals("<XML-RPC element>", expected, actual);
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcElementFactoryImpl#createXmlRpcElement(Object)}</code>
   * creates a new <code>{@link XmlRpcDate}</code> if the specified argument
   * is a <code>java.util.Date</code>.
   */
  public void testCreateXmlRpcElementWithDateArgument() {
    Date source = new Date();
    XmlRpcDateTime expected = new XmlRpcDateTime(source);
    XmlRpcElement actual = this.factory.createXmlRpcElement(source);

    assertEquals("<XML-RPC element>", expected, actual);
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcElementFactoryImpl#createXmlRpcElement(Object)}</code>
   * creates a new <code>{@link XmlRpcDouble}</code> if the specified argument
   * is a <code>Double</code>.
   */
  public void testCreateXmlRpcElementWithDoubleArgument() {
    Double source = new Double(43.99);
    XmlRpcDouble expected = new XmlRpcDouble(source);
    XmlRpcElement actual = this.factory.createXmlRpcElement(source);

    assertEquals("<XML-RPC element>", expected, actual);
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcElementFactoryImpl#createXmlRpcElement(Object)}</code>
   * creates a new <code>{@link XmlRpcDouble}</code> if the specified argument
   * is a <code>Float</code>.
   */
  public void testCreateXmlRpcElementWithFloatArgument() {
    Float source = new Float(43.99f);
    XmlRpcDouble expected = new XmlRpcDouble(source);
    XmlRpcElement actual = this.factory.createXmlRpcElement(source);

    assertEquals("<XML-RPC element>", expected, actual);
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcElementFactoryImpl#createXmlRpcElement(Object)}</code>
   * creates a new <code>{@link XmlRpcInteger}</code> if the specified
   * argument is a <code>Integer</code>.
   */
  public void testCreateXmlRpcElementWithIntegerArgument() {
    Integer source = new Integer(44);
    XmlRpcInteger expected = new XmlRpcInteger(source);
    XmlRpcElement actual = this.factory.createXmlRpcElement(source);

    assertEquals("<XML-RPC element>", expected, actual);
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcElementFactoryImpl#createXmlRpcElement(Object)}</code>
   * creates a new <code>{@link XmlRpcStruct}</code> if the specified argument
   * is a JavaBean.
   */
  public void testCreateXmlRpcElementWithJavaBeanArgument() {
    XmlRpcStruct expected = new XmlRpcStruct();
    expected.add("id", new XmlRpcInteger(new Integer(45)));
    expected.add("name", new XmlRpcString("Anakin"));
    List expectedMembers = Arrays.asList(expected.getMembers());

    Person source = new Person();
    source.setId(new Integer(45));
    source.setName("Anakin");
    XmlRpcStruct actual = (XmlRpcStruct) this.factory
        .createXmlRpcElement(source);

    List actualMembers = Arrays.asList(actual.getMembers());

    assertEquals("<XML-RPC struct member count>", expectedMembers.size(),
        actualMembers.size());

    assertTrue("<XML-RPC struct members>. Expected: " + expectedMembers
        + " but was: " + actualMembers, expectedMembers
        .containsAll(actualMembers));
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcElementFactoryImpl#createXmlRpcElement(Object)}</code>
   * creates a new <code>{@link XmlRpcString}</code> if the specified argument
   * is a <code>Long</code>.
   */
  public void testCreateXmlRpcElementWithLongArgument() {
    Long source = new Long(434);
    XmlRpcString expected = new XmlRpcString(source);
    XmlRpcElement actual = this.factory.createXmlRpcElement(source);

    assertEquals("<XML-RPC element>", expected, actual);
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcElementFactoryImpl#createXmlRpcElement(Object)}</code>
   * creates a new <code>{@link XmlRpcStruct}</code> if the specified argument
   * is a <code>java.util.Map</code>.
   */
  public void testCreateXmlRpcElementWithMapArgument() {
    XmlRpcStruct expected = new XmlRpcStruct();
    expected.add("Luke", new XmlRpcString("Jedi"));
    expected.add("Leia", new XmlRpcString("Princess"));
    List expectedMembers = Arrays.asList(expected.getMembers());

    Map source = new HashMap();
    source.put("Luke", "Jedi");
    source.put("Leia", "Princess");

    XmlRpcStruct actual = (XmlRpcStruct) this.factory
        .createXmlRpcElement(source);
    List actualMembers = Arrays.asList(actual.getMembers());

    assertEquals("<XML-RPC struct member count>", expectedMembers.size(),
        actualMembers.size());

    assertTrue("<XML-RPC struct members>. Expected: " + expectedMembers
        + " but was: " + actualMembers, expectedMembers
        .containsAll(actualMembers));
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcElementFactoryImpl#createXmlRpcElement(Object)}</code>
   * creates a new <code>{@link XmlRpcString}</code> if the specified argument
   * is <code>null</code>.
   */
  public void testCreateXmlRpcElementWithNullArgument() {
    XmlRpcString expected = new XmlRpcString("");
    XmlRpcElement actual = this.factory.createXmlRpcElement(null);

    assertEquals("<XML-RPC element>", expected, actual);
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcElementFactoryImpl#createXmlRpcElement(Object)}</code>
   * creates a new <code>{@link XmlRpcInteger}</code> if the specified
   * argument is a <code>Short</code>.
   */
  public void testCreateXmlRpcElementWithShortArgument() {
    Short source = new Short((short) 4);
    XmlRpcInteger expected = new XmlRpcInteger(source);
    XmlRpcElement actual = this.factory.createXmlRpcElement(source);

    assertEquals("<XML-RPC element>", expected, actual);
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcElementFactoryImpl#createXmlRpcElement(Object)}</code>
   * creates a new <code>{@link XmlRpcString}</code> if the specified argument
   * is a <code>String</code>.
   */
  public void testCreateXmlRpcElementWithStringArgument() {
    String source = "X-Wing";
    XmlRpcString expected = new XmlRpcString(source);
    XmlRpcElement actual = this.factory.createXmlRpcElement(source);

    assertEquals("<XML-RPC element>", expected, actual);
  }
}
