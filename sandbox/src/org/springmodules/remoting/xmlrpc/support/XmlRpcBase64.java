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

import java.util.Arrays;

import org.apache.commons.codec.binary.Base64;
import org.springmodules.util.ArrayUtils;

/**
 * <p>
 * Represents a base64-encoded binary.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.10 $ $Date: 2005/09/25 05:19:59 $
 */
public final class XmlRpcBase64 implements XmlRpcScalar {

  private byte[] value;

  public XmlRpcBase64(byte[] newValue) {
    super();
    value = newValue;
  }

  public XmlRpcBase64(String value) {
    this(Base64.decodeBase64(value.getBytes()));
  }

  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof XmlRpcBase64)) {
      return false;
    }

    final XmlRpcBase64 xmlRpcBase64 = (XmlRpcBase64) obj;

    if (!Arrays.equals(value, xmlRpcBase64.value)) {
      return false;
    }

    return true;
  }

  /**
   * @see XmlRpcElement#getMatchingValue(Class)
   */
  public Object getMatchingValue(Class targetType) {
    Object matchingValue = NOT_MATCHING;

    if (targetType.isArray() && targetType.getComponentType().equals(Byte.TYPE)) {
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
    byte[] encodedValue = Base64.encodeBase64(value);
    return new String(encodedValue, 0, encodedValue.length);
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
    buffer.append("value=" + ArrayUtils.toString(value) + "]");

    return buffer.toString();
  }
}
