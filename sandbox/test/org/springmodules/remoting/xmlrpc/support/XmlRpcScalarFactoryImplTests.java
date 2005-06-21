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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.codec.binary.Base64;
import org.springmodules.remoting.xmlrpc.XmlRpcElementNames;
import org.springmodules.remoting.xmlrpc.XmlRpcParsingException;

import junit.framework.TestCase;

/**
 * <p>
 * Unit Tests for <code>{@link XmlRpcScalarFactoryImpl}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/06/21 02:23:12 $
 */
public class XmlRpcScalarFactoryImplTests extends TestCase {

  /**
   * Primary object that is under test.
   */
  private XmlRpcScalarFactoryImpl scalarFactoryImpl;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case to construct.
   */
  public XmlRpcScalarFactoryImplTests(String name) {
    super(name);
  }

  /**
   * Sets up the test fixture.
   */
  protected void setUp() throws Exception {
    super.setUp();

    this.scalarFactoryImpl = new XmlRpcScalarFactoryImpl();
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcScalarFactoryImpl#createScalarValue(String, String)}</code>
   * throws a <code>{@link XmlRpcParsingException}</code> if the specified
   * element name is invalid.
   */
  public void testCreateScalarValueWhenElementNameIsInvalid() {
    try {
      this.scalarFactoryImpl.createScalarValue("InvalidName", "");
      fail("A 'XmlRpcParsingException' should have been thrown");
    } catch (XmlRpcParsingException exception) {
      // we are expecting this exception.
    }
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcScalarFactoryImpl#createScalarValue(String, String)}</code>
   * creates a new <code>{@link XmlRpcBase64}</code> if the specified element
   * name is equal to <code>{@link XmlRpcElementNames#BASE_64}</code>.
   */
  public void testCreateScalarValueWhenScalarIsBase64() {
    byte[] binaryData = { 0, 2, 6, 4 };
    XmlRpcBase64 expected = new XmlRpcBase64(binaryData);

    byte[] encoded = Base64.encodeBase64(binaryData);

    XmlRpcScalar actual = this.scalarFactoryImpl.createScalarValue(
        XmlRpcElementNames.BASE_64, new String(encoded, 0, encoded.length));

    assertEquals("<XML-RPC base64>", expected, actual);
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcScalarFactoryImpl#createScalarValue(String, String)}</code>
   * creates a new <code>{@link XmlRpcBoolean}</code> if the specified element
   * name is equal to <code>{@link XmlRpcElementNames#BOOLEAN}</code>.
   */
  public void testCreateScalarValueWhenScalarIsBoolean() {
    XmlRpcBoolean expected = new XmlRpcBoolean(Boolean.TRUE);

    XmlRpcScalar actual = this.scalarFactoryImpl.createScalarValue(
        XmlRpcElementNames.BOOLEAN, XmlRpcBoolean.TRUE);

    assertEquals("<XML-RPC boolean>", expected, actual);
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcScalarFactoryImpl#createScalarValue(String, String)}</code>
   * creates a new <code>{@link XmlRpcDateTime}</code> if the specified
   * element name is equal to <code>{@link XmlRpcElementNames#DATE_TIME}</code>.
   */
  public void testCreateScalarValueWhenScalarIsDateTime() {
    Calendar calendar = new GregorianCalendar();
    calendar.set(Calendar.MILLISECOND, 0);
    Date date = calendar.getTime();
    XmlRpcDateTime expected = new XmlRpcDateTime(date);

    DateFormat dateFormat = new SimpleDateFormat(XmlRpcDateTime.PATTERN);

    XmlRpcScalar actual = this.scalarFactoryImpl.createScalarValue(
        XmlRpcElementNames.DATE_TIME, dateFormat.format(date));

    assertEquals("<XML-RPC date/time>", expected, actual);
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcScalarFactoryImpl#createScalarValue(String, String)}</code>
   * creates a new <code>{@link XmlRpcDouble}</code> if the specified element
   * name is equal to <code>{@link XmlRpcElementNames#DOUBLE}</code>.
   */
  public void testCreateScalarValueWhenScalarIsDouble() {
    Double doubleValue = new Double(882.90);
    XmlRpcDouble expected = new XmlRpcDouble(doubleValue);

    XmlRpcScalar actual = this.scalarFactoryImpl.createScalarValue(
        XmlRpcElementNames.DOUBLE, doubleValue.toString());

    assertEquals("<XML-RPC double>", expected, actual);
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcScalarFactoryImpl#createScalarValue(String, String)}</code>
   * creates a new <code>{@link XmlRpcInteger}</code> if the specified element
   * name is equal to <code>{@link XmlRpcElementNames#I4}</code>.
   */
  public void testCreateScalarValueWhenScalarIsI4() {
    Integer integerValue = new Integer(34);
    XmlRpcInteger expected = new XmlRpcInteger(integerValue);

    XmlRpcScalar actual = this.scalarFactoryImpl.createScalarValue(
        XmlRpcElementNames.I4, integerValue.toString());

    assertEquals("<XML-RPC integer>", expected, actual);
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcScalarFactoryImpl#createScalarValue(String, String)}</code>
   * creates a new <code>{@link XmlRpcInteger}</code> if the specified element
   * name is equal to <code>{@link XmlRpcElementNames#INT}</code>.
   */
  public void testCreateScalarValueWhenScalarIsInt() {
    Integer integerValue = new Integer(34);
    XmlRpcInteger expected = new XmlRpcInteger(integerValue);

    XmlRpcScalar actual = this.scalarFactoryImpl.createScalarValue(
        XmlRpcElementNames.INT, integerValue.toString());

    assertEquals("<XML-RPC integer>", expected, actual);
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcScalarFactoryImpl#createScalarValue(String, String)}</code>
   * creates a new <code>{@link XmlRpcString}</code> if the specified element
   * name is equal to <code>{@link XmlRpcElementNames#STRING}</code>.
   */
  public void testCreateScalarValueWhenScalarIsString() {
    String stringValue = "Yoda";
    XmlRpcString expected = new XmlRpcString(stringValue);

    XmlRpcScalar actual = this.scalarFactoryImpl.createScalarValue(
        XmlRpcElementNames.STRING, stringValue.toString());

    assertEquals("<XML-RPC string>", expected, actual);
  }
}
