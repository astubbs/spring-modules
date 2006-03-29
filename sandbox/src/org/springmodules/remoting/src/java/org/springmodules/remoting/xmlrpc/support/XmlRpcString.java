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

import org.springmodules.util.Strings;

/**
 * <p>
 * Represents a string or a string representation of a 64-bit signed integer.
 * </p>
 * <p>
 * The XML-RPC specification does not support 64-bit signed integers (and it is
 * very unlikely this is going to change). 64-bit signed integers need to be
 * represented as Strings.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public final class XmlRpcString implements XmlRpcScalar {

  /**
   * The value of this scalar.
   */
  private String value;

  public XmlRpcString(Character value) {
    this(value.toString());
  }

  public XmlRpcString(Long value) {
    this(value.toString());
  }

  public XmlRpcString(String newValue) {
    super();
    value = newValue;
  }

  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof XmlRpcString)) {
      return false;
    }

    final XmlRpcString xmlRpcString = (XmlRpcString) obj;

    if (value != null ? !value.equals(xmlRpcString.value)
        : xmlRpcString.value != null) {
      return false;
    }

    return true;
  }

  /**
   * Returns the value of this scalar if the given type is equal to:
   * <ul>
   * <li><code>{@link String}</code></li>
   * <li><code>{@link Long}</code> or <code>{@link Long#TYPE}</code>. This
   * case is valid only if the value of this scalar can be parsed into a 64-bit
   * signed integer.</li>
   * </ul>
   * 
   * @param targetType
   *          the given type.
   * @return the value of this scalar if the given type represents a string or a
   *         64-bit signed integer.
   * 
   * @see XmlRpcElement#getMatchingValue(Class)
   */
  public Object getMatchingValue(Class targetType) {
    Object matchingValue = NOT_MATCHING;

    if (String.class.equals(targetType)) {
      matchingValue = value;

    } else if (Long.class.equals(targetType) || Long.TYPE.equals(targetType)) {
      try {
        matchingValue = new Long(value);

      } catch (NumberFormatException exception) {
        matchingValue = NOT_MATCHING;
      }
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
    return value;
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
    buffer.append("value=" + Strings.quote(value) + "]");

    return buffer.toString();
  }
}
