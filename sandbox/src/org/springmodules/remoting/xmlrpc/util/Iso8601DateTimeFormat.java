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

/**
 * <p>
 * Formats and parses dates using the pattern supported by XML-RPC.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/06/10 01:45:53 $
 */
public class Iso8601DateTimeFormat {

  /**
   * Pattern describing the format for date/time supported by XML-RPC.
   */
  public static final String PATTERN = "yyyyMMdd'T'HH:mm:ss";

  /**
   * Internal <code>DateFormat</code> wrapped by this class.
   */
  private DateFormat dateFormat;

  /**
   * Constructor.
   */
  public Iso8601DateTimeFormat() {
    super();
    this.dateFormat = new SimpleDateFormat(PATTERN);
  }

  /**
   * Formats a <code>java.util.Date</code> into a date/time string.
   * 
   * @param date
   *          the time value to be formatted into a time string.
   * @return the formatted time string.
   */
  public String format(Date date) {
    return this.dateFormat.format(date);
  }

  /**
   * Parses text from the beginning of the given string to produce a date.
   * 
   * @param source
   *          a <code>String</code> whose beginning should be parsed.
   * @return a <code>java.util.Date</code> parsed from the string.
   * @exception ParseException
   *              if the beginning of the specified string cannot be parsed.
   */
  public Date parse(String source) throws ParseException {
    return this.dateFormat.parse(source);
  }
}
