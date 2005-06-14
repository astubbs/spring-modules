/* 
 * Created on Jun 13, 2005
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

import org.springmodules.remoting.xmlrpc.XmlRpcParsingException;

/**
 * <p>
 * Parses/formats <code>java.util.Date</code> values.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/06/14 00:47:17 $
 */
public class DateTimeHandler implements ScalarHandler {

  /**
   * Pattern describing the format for date/time supported by XML-RPC.
   */
  public static final String PATTERN = "yyyyMMdd'T'HH:mm:ss";

  /**
   * Constructor.
   */
  public DateTimeHandler() {
    super();
  }

  /**
   * @see ScalarHandler#format(java.lang.Object)
   */
  public String format(Object source) {
    return this.getDateFormat().format(source);
  }

  /**
   * Returns an instance of <code>java.text.DateFormat</code> passing
   * <code>{@link #PATTERN}</code> as the pattern to use and the default date
   * format symbols for the default locale.
   * 
   * @return the created instance of <code>java.text.DateFormat</code>.
   */
  private DateFormat getDateFormat() {
    return new SimpleDateFormat(PATTERN);
  }

  /**
   * @see ScalarHandler#parse(String)
   */
  public Object parse(String source) {
    try {
      return this.getDateFormat().parse(source);

    } catch (ParseException exception) {
      throw new XmlRpcParsingException("'" + source + "' is not a date",
          exception);
    }
  }

}
