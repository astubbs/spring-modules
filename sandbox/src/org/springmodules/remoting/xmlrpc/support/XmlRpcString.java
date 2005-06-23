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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

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
 * @version $Revision: 1.6 $ $Date: 2005/06/23 01:47:00 $
 */
public final class XmlRpcString implements XmlRpcScalar {

  /**
   * The value of this scalar.
   */
  private String value;

  /**
   * Constructor.
   * 
   * @param value
   *          the new value of this scalar.
   */
  public XmlRpcString(Long value) {
    this(value.toString());
  }

  /**
   * Constructor.
   * 
   * @param value
   *          the new value of this scalar.
   */
  public XmlRpcString(String value) {
    super();
    this.value = value;
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   * 
   * @param obj
   *          the reference object with which to compare
   * @return <code>true</code> if this object is the same as the obj argument;
   *         <code>false</code> otherwise.
   * 
   * @see Object#equals(java.lang.Object)
   */
  public boolean equals(Object obj) {
    boolean equals = true;

    if (null == obj || !(obj instanceof XmlRpcString)) {
      equals = false;
    } else if (this != obj) {
      XmlRpcString xmlRpcString = (XmlRpcString) obj;

      EqualsBuilder equalsBuilder = new EqualsBuilder();
      equalsBuilder.append(this.getValue(), xmlRpcString.getValue());

      equals = equalsBuilder.isEquals();
    }

    return equals;
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
      matchingValue = this.value;

    } else if (Long.class.equals(targetType) || Long.TYPE.equals(targetType)) {
      try {
        matchingValue = new Long(this.value);

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
    return this.value;
  }

  /**
   * @see XmlRpcScalar#getValueAsString()
   */
  public String getValueAsString() {
    return this.value;
  }

  /**
   * Returns a hash code value for the object. This method is supported for the
   * benefit of hashtables such as those provided by
   * <code>java.util.Hashtable</code>.
   * 
   * @return a hash code value for this object.
   * 
   * @see Object#hashCode()
   */
  public int hashCode() {
    HashCodeBuilder hashCodeBuilder = new HashCodeBuilder(1217, 1223);
    hashCodeBuilder.append(this.value);

    int hashCode = hashCodeBuilder.toHashCode();
    return hashCode;
  }

  /**
   * Returns a string representation of the object. In general, the
   * <code>toString</code> method returns a string that "textually represents"
   * this object.
   * 
   * @return a string representation of the object.
   * 
   * @see Object#toString()
   */
  public String toString() {
    ToStringBuilder toStringBuilder = new ToStringBuilder(this);
    toStringBuilder.append("value", this.value);

    String toString = toStringBuilder.toString();
    return toString;
  }
}
