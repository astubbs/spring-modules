/* 
 * Created on Jun 17, 2005
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

import junit.framework.TestCase;

/**
 * <p>
 * Unit Tests for <code>{@link XmlRpcDateTime}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public class XmlRpcDateTimeTests extends TestCase {

  /**
   * Primary object that is under test.
   */
  private XmlRpcDateTime xmlRpcDateTime;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case to construct.
   */
  public XmlRpcDateTimeTests(String name) {
    super(name);
  }

  /**
   * Verifies that the constructor
   * <code>{@link XmlRpcDateTime#XmlRpcDateTime(Date)}</code> stores the
   * <code>{@link Date}</code> passed as argument as its internal value.
   */
  public void testConstructorWithDateArgument() {
    Date expected = new Date();
    this.xmlRpcDateTime = new XmlRpcDateTime(expected);
    assertSame("<Value>", expected, this.xmlRpcDateTime.getValue());
  }

  /**
   * Verifies that the constructor
   * <code>{@link XmlRpcDateTime#XmlRpcDateTime(String)}</code> parses the
   * given String and stores the resulting date as its internal value.
   */
  public void testConstructorWithStringArgument() {
    Calendar calendar = new GregorianCalendar();
    calendar.set(Calendar.MILLISECOND, 0);
    Date expected = calendar.getTime();

    DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HH:mm:ss");
    String date = dateFormat.format(expected);

    this.xmlRpcDateTime = new XmlRpcDateTime(date);
    assertEquals("<Value>", expected, this.xmlRpcDateTime.getValue());
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcDateTime#getMatchingValue(Class)}</code> returns its
   * internal value if the given type is <code>{@link Date}</code>.
   */
  public void testGetMatchingValueWhenTargetTypeIsDate() {
    Date expected = new Date();
    this.xmlRpcDateTime = new XmlRpcDateTime(expected);

    assertSame("<Value>", expected, this.xmlRpcDateTime
        .getMatchingValue(Date.class));
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcDateTime#getMatchingValue(Class)}</code> returns
   * <code>{@link XmlRpcElement#NOT_MATCHING}</code> if the given type is not
   * <code>{@link Date}</code>.
   */
  public void testGetMatchingValueWhenTargetTypeIsNotDate() {
    this.xmlRpcDateTime = new XmlRpcDateTime(new Date());
    Object actual = this.xmlRpcDateTime.getMatchingValue(String.class);
    assertSame("<Value>", XmlRpcElement.NOT_MATCHING, actual);
  }

  /**
   * Verifies that the method
   * <code>{@link XmlRpcDateTime#getValueAsString()}</code> returns the
   * internal value of the <code>XmlRpcDateTime</code> formatted using the ISO
   * 8601 format.
   */
  public void testGetValueAsString() {
    Calendar calendar = new GregorianCalendar();
    calendar.set(Calendar.MILLISECOND, 0);
    Date date = calendar.getTime();
    this.xmlRpcDateTime = new XmlRpcDateTime(date);

    DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HH:mm:ss");
    String expected = dateFormat.format(date);

    assertEquals("<Value as String>", expected, this.xmlRpcDateTime
        .getValueAsString());
  }
}
