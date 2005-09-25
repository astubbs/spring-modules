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

import org.springmodules.remoting.xmlrpc.XmlRpcInvalidPayloadException;

/**
 * <p>
 * Represents a XML-RPC boolean value.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.12 $ $Date: 2005/09/25 05:19:58 $
 */
public final class XmlRpcBoolean implements XmlRpcScalar {

  public static final String FALSE = "0";

  public static final String TRUE = "1";

  private Boolean value;

  public XmlRpcBoolean() {
    super();
  }

  public XmlRpcBoolean(Boolean newValue) {
    super();
    value = newValue;
  }

  public XmlRpcBoolean(String newValue) throws XmlRpcInvalidPayloadException {
    super();
    if (TRUE.equals(newValue)) {
      value = Boolean.TRUE;

    } else if (FALSE.equals(newValue)) {
      value = Boolean.FALSE;

    } else {
      throw new XmlRpcInvalidPayloadException("'" + newValue
          + "' is not a boolean value");
    }
  }

  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof XmlRpcBoolean)) {
      return false;
    }

    final XmlRpcBoolean xmlRpcBoolean = (XmlRpcBoolean) obj;

    if (value != null ? !value.equals(xmlRpcBoolean.value)
        : xmlRpcBoolean.value != null) {
      return false;
    }

    return true;
  }

  /**
   * @see XmlRpcElement#getMatchingValue(Class)
   */
  public Object getMatchingValue(Class targetType) {
    Object matchingValue = NOT_MATCHING;

    if (Boolean.class.equals(targetType) || Boolean.TYPE.equals(targetType)) {
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
    return value.booleanValue() ? TRUE : FALSE;
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
