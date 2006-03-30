/* 
 * Created on Jun 14, 2005
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springmodules.remoting.xmlrpc.XmlRpcInvalidPayloadException;

/**
 * <p>
 * Represents a XML-RPC date/time value in IS0 8601 format.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public final class XmlRpcDateTime implements XmlRpcScalar {

  /**
   * Pattern for ISO 8601, the date and time representation standard.
   */
  public static final String PATTERN = "yyyyMMdd'T'HH:mm:ss";

  private DateFormat dateFormat = new SimpleDateFormat(PATTERN);

  private Date value;

  public XmlRpcDateTime(Date newValue) {
    super();
    value = newValue;
  }

  public XmlRpcDateTime(String newValue) throws XmlRpcInvalidPayloadException {
    super();

    try {
      value = dateFormat.parse(newValue);

    } catch (ParseException exception) {
      throw new XmlRpcInvalidPayloadException("'" + newValue
          + "' is not a date in ISO 8601 format", exception);
    }
  }

  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof XmlRpcDateTime)) {
      return false;
    }

    final XmlRpcDateTime xmlRpcDateTime = (XmlRpcDateTime) obj;

    if (value != null ? !value.equals(xmlRpcDateTime.value)
        : xmlRpcDateTime.value != null) {
      return false;
    }

    return true;
  }

  /**
   * @see XmlRpcElement#getMatchingValue(Class)
   */
  public Object getMatchingValue(Class targetType) {
    Object matchingValue = NOT_MATCHING;

    if (Date.class.equals(targetType)) {
      matchingValue = value;
    }
    return matchingValue;
  }

  /**
   * @see XmlRpcScalar#getValue()
   */
  public Object getValue() {
    return value;
  }

  /**
   * @see XmlRpcScalar#getValueAsString()
   */
  public String getValueAsString() {
    return dateFormat.format(value);
  }

  public int hashCode() {
    int multiplier = 31;
    int hash = 7;
    hash = multiplier * hash + (value != null ? value.hashCode() : 0);
    return hash;
  }

  public String toString() {
    StringBuffer buffer = new StringBuffer(getClass().getName());
    buffer.append("@" + System.identityHashCode(this) + "[");
    buffer.append("value=" + value + "]");

    return buffer.toString();
  }
}
