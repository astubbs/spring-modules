/* 
 * Created on Jun 9, 2005
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
package org.springmodules.remoting.xmlrpc;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import junit.framework.TestCase;

import org.apache.commons.codec.binary.Base64;
import org.springmodules.remoting.xmlrpc.util.Iso8601DateTimeFormat;

/**
 * <p>
 * Unit Tests for <code>{@link AbstractXmlRpcParser}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.4 $ $Date: 2005/06/10 01:48:09 $
 */
public class XmlRpcParserTests extends TestCase {

  /**
   * Primary object that is under test.
   */
  private AbstractXmlRpcParser parser;

  /**
   * Formats/parses dates.
   */
  private Iso8601DateTimeFormat dateTimeFormat;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case to construct.
   */
  public XmlRpcParserTests(String name) {
    super(name);
  }

  /**
   * Sets up the test fixture.
   */
  protected void setUp() throws Exception {
    super.setUp();

    this.parser = new AbstractXmlRpcParser() {
      // no extra implementation.
    };

    this.dateTimeFormat = new Iso8601DateTimeFormat();
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractXmlRpcParser#parseBase64(String)}</code> parses the
   * given text into an array of <code>byte</code>s.
   */
  public void testParseBase64() {
    String source = "eW91IGNhbid0IHJlYWQgdGhpcyE=";
    byte[] expected = Base64.decodeBase64(source.getBytes());
    byte[] actual = this.parser.parseBase64(source);

    assertTrue("<Byte array>", Arrays.equals(expected, actual));
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractXmlRpcParser#parseBoolean(String)}</code> returns
   * <code>{@link Boolean#FALSE}</code> if the given text is equal to "1".
   */
  public void testParseBooleanWhenValueIsEqualToOne() {
    assertEquals("<Boolean>", Boolean.TRUE, this.parser.parseBoolean("1"));
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractXmlRpcParser#parseBoolean(String)}</code> returns
   * <code>{@link Boolean#FALSE}</code> if the given text is not equal to "1".
   */
  public void testParseBooleanWhenValueIsNotEqualToOne() {
    assertEquals("<Boolean>", Boolean.FALSE, this.parser.parseBoolean("0"));
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractXmlRpcParser#parseDateTime(String)}</code> parses
   * the given text into a <code>java.util.Date</code>.
   */
  public void testParseDateTime() throws Exception {
    Calendar calendar = new GregorianCalendar();
    calendar.set(Calendar.YEAR, 1998);
    calendar.set(Calendar.MONTH, 6);
    calendar.set(Calendar.DATE, 17);
    calendar.set(Calendar.HOUR, 2);
    calendar.set(Calendar.AM_PM, Calendar.PM);
    calendar.set(Calendar.MINUTE, 8);
    calendar.set(Calendar.SECOND, 55);
    calendar.set(Calendar.MILLISECOND, 0);

    Date expected = calendar.getTime();
    String source = this.dateTimeFormat.format(expected);
    Date actual = this.parser.parseDateTime(source);

    assertEquals("<Date>", expected, actual);
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractXmlRpcParser#parseDateTime(String)}</code> throws a
   * <code>{@link XmlRpcParsingException}</code> if the given text cannot be
   * parsed into a date.
   */
  public void testParseDateTimeWhenTextCannotBeParsedIntoDate() {
    try {
      this.parser.parseDateTime("someText");
      fail("A 'XmlRpcParsingException' should have been thrown");
    } catch (XmlRpcParsingException exception) {
      // we are expecting this exception.
    }
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractXmlRpcParser#parseDouble(String)}</code> parses the
   * given text into a <code>{@link Double}</code>.
   */
  public void testParseDouble() throws Exception {
    Double expected = new Double(99.88);
    String source = expected.toString();
    Double actual = this.parser.parseDouble(source);

    assertEquals("<Double>", expected, actual);
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractXmlRpcParser#parseDouble(String)}</code> throws a
   * <code>{@link XmlRpcParsingException}</code> if the given text cannot be
   * parsed into a double.
   */
  public void testParseDoubleWhenTextCannotBeParsedIntoDouble() {
    try {
      this.parser.parseDouble("eight");
      fail("A 'XmlRpcParsingException' should have been thrown");
    } catch (XmlRpcParsingException exception) {
      // we are expecting this exception.
    }
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractXmlRpcParser#parseInteger(String)}</code> parses the
   * given text into an <code>{@link Integer}</code>.
   */
  public void testParseInteger() throws Exception {
    Integer expected = new Integer(30);
    String source = expected.toString();
    Integer actual = this.parser.parseInteger(source);

    assertEquals("<Integer>", expected, actual);
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractXmlRpcParser#parseInteger(String)}</code> throws a
   * <code>{@link XmlRpcParsingException}</code> if the given text cannot be
   * parsed into a integer.
   */
  public void testParseIntegerWhenTextCannotBeParsedIntoInteger() {
    try {
      this.parser.parseInteger("one");
      fail("A 'XmlRpcParsingException' should have been thrown");
    } catch (XmlRpcParsingException exception) {
      // we are expecting this exception.
    }
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractXmlRpcParser#parseString(String)}</code> parses the
   * given text into a <code>String</code>.
   */
  public void testParseString() {
    String expected = "Some text";
    assertEquals("<String>", expected, this.parser.parseString(expected));
  }

}
