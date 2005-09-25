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
 * Represents a four-byte signed integer.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.15 $ $Date: 2005/09/25 05:19:58 $
 */
public final class XmlRpcInteger implements XmlRpcScalar {

  private Integer value;

  public XmlRpcInteger() {
    super();
  }

  public XmlRpcInteger(Integer newValue) {
    this();
    value = newValue;
  }

  public XmlRpcInteger(Short newValue) {
    this(new Integer(newValue.intValue()));
  }

  public XmlRpcInteger(String newValue) throws XmlRpcInvalidPayloadException {
    super();
    try {
      value = new Integer(newValue);

    } catch (NumberFormatException exception) {
      throw new XmlRpcInvalidPayloadException("'" + newValue
          + "' is not a 32-bit signed integer", exception);
    }
  }

  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof XmlRpcInteger)) {
      return false;
    }

    final XmlRpcInteger xmlRpcInteger = (XmlRpcInteger) obj;

    if (value != null ? !value.equals(xmlRpcInteger.value)
        : xmlRpcInteger.value != null) {
      return false;
    }

    return true;
  }

  /**
   * Returns the value of this scalar if the given type is equal to
   * <code>{@link Integer}</code> or <code>{@link Integer#TYPE}</code>.
   * 
   * @param targetType
   *          the given type.
   * @return the value of this scalar if the given type represents a 32-bit
   *         signed integer.
   * 
   * @see XmlRpcElement#getMatchingValue(Class)
   */
  public Object getMatchingValue(Class targetType) {
    Object matchingValue = NOT_MATCHING;

    if (Integer.class.equals(targetType) || Integer.TYPE.equals(targetType)) {
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
    return value.toString();
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
