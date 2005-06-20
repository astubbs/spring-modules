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
 * Represents a XML-RPC boolean value.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.4 $ $Date: 2005/06/20 22:50:54 $
 */
public class XmlRpcBoolean implements XmlRpcScalar {

  /**
   * Represents <code>false</code> in XML-RPC.
   */
  public static final String FALSE = "0";

  /**
   * Represents <code>true</code> in XML-RPC.
   */
  public static final String TRUE = "1";

  /**
   * The value of this scalar.
   */
  private Boolean value;

  /**
   * Constructor.
   */
  public XmlRpcBoolean() {
    super();
  }

  /**
   * Constructor.
   * 
   * @param value
   *          the new value of this scalar.
   */
  public XmlRpcBoolean(Boolean value) {
    super();
    this.value = value;
  }

  /**
   * Constructor.
   * 
   * @param value
   *          the new value of this scalar.
   */
  public XmlRpcBoolean(String value) {
    this(TRUE.equals(value) ? Boolean.TRUE : Boolean.FALSE);
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

    if (null == obj || !(obj instanceof XmlRpcBoolean)) {
      equals = false;
    } else if (this != obj) {
      XmlRpcBoolean xmlRpcBoolean = (XmlRpcBoolean) obj;

      EqualsBuilder equalsBuilder = new EqualsBuilder();
      equalsBuilder.append(this.getValue(), xmlRpcBoolean.getValue());

      equals = equalsBuilder.isEquals();
    }

    return equals;
  }

  /**
   * @see XmlRpcElement#getMatchingValue(Class)
   */
  public Object getMatchingValue(Class targetType) {
    Object matchingValue = NOT_MATCHING;

    if (Boolean.class.equals(targetType) || Boolean.TYPE.equals(targetType)) {
      matchingValue = this.value;
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
   * Returns a hash code value for the object. This method is supported for the
   * benefit of hashtables such as those provided by
   * <code>java.util.Hashtable</code>.
   * 
   * @return a hash code value for this object.
   * 
   * @see Object#hashCode()
   */
  public int hashCode() {
    HashCodeBuilder hashCodeBuilder = new HashCodeBuilder(1061, 1063);
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
