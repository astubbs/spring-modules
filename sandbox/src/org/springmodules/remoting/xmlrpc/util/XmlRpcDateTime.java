/* 
 * Created on Jun 8, 2005
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
package org.springmodules.remoting.xmlrpc.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springmodules.remoting.xmlrpc.XmlRpcParsingException;

/**
 * <p>
 * Formats and parses dates using the pattern supported by XML-RPC.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/06/13 08:53:41 $
 */
public class XmlRpcDateTime {

  /**
   * Pattern describing the format for date/time supported by XML-RPC.
   */
  public static final String PATTERN = "yyyyMMdd'T'HH:mm:ss";

  /**
   * Formats a <code>java.util.Date</code> into a date/time string.
   * 
   * @param date
   *          the time value to be formatted into a time string.
   * @return the formatted time string.
   */
  public static String toString(Date date) {
    return getDateFormat().format(date);
  }

  /**
   * Returns an instance of <code>java.text.DateFormat</code> passing
   * <code>{@link #PATTERN}</code> as the pattern to use and the default date
   * format symbols for the default locale.
   * 
   * @return the created instance of <code>java.text.DateFormat</code>.
   */
  protected static DateFormat getDateFormat() {
    return new SimpleDateFormat(PATTERN);
  }

  /**
   * Parses text from the beginning of the given string to produce a date.
   * 
   * @param source
   *          a <code>String</code> whose beginning should be parsed.
   * @return a <code>java.util.Date</code> parsed from the string.
   * @exception XmlRpcParsingException
   *              if the beginning of the specified string cannot be parsed.
   */
  public static Date toDate(String source) throws XmlRpcParsingException {
    try {
      return getDateFormat().parse(source);
      
    } catch (ParseException exception) {
      throw new XmlRpcParsingException("'" + source + "' is not a date",
          exception);
    }
  }

  /**
   * Constructor.
   */
  public XmlRpcDateTime() {
    super();
  }
}
