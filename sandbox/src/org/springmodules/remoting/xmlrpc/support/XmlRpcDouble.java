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
 * Represents a double-precision signed floating point number.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.13 $ $Date: 2005/07/06 13:06:26 $
 */
public final class XmlRpcDouble implements XmlRpcScalar {

  /**
   * The value of this scalar.
   */
  private Double value;

  /**
   * Constructor.
   */
  public XmlRpcDouble() {
    super();
  }

  /**
   * Constructor.
   * 
   * @param value
   *          the new value of this scalar.
   */
  public XmlRpcDouble(Double value) {
    this();
    this.value = value;
  }

  /**
   * Constructor.
   * 
   * @param value
   *          the new value of this scalar.
   */
  public XmlRpcDouble(Float value) {
    this(new Double(value.doubleValue()));
  }

  /**
   * Constructor.
   * 
   * @param value
   *          the new value of this scalar.
   * @throws XmlRpcInvalidPayloadException
   *           if the given value is not a parsable number.
   */
  public XmlRpcDouble(String value) {
    this();

    try {
      this.value = new Double(value);

    } catch (NumberFormatException exception) {
      throw new XmlRpcInvalidPayloadException("'" + value
          + "' is not a double-precision signed floating point number",
          exception);
    }
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
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof XmlRpcDouble)) {
      return false;
    }

    final XmlRpcDouble xmlRpcDouble = (XmlRpcDouble) obj;

    if (this.value != null ? !this.value.equals(xmlRpcDouble.value)
        : xmlRpcDouble.value != null) {
      return false;
    }

    return true;
  }

  /**
   * Returns the value of this scalar if the given type is equal to
   * <code>{@link Double}</code> or <code>{@link Double#TYPE}</code>.
   * 
   * @param targetType
   *          the given type.
   * @return the value of this scalar if the given type represents a
   *         double-precision signed floating point number.
   * 
   * @see XmlRpcElement#getMatchingValue(Class)
   */
  public Object getMatchingValue(Class targetType) {
    Object matchingValue = NOT_MATCHING;

    if (Double.class.equals(targetType) || Double.TYPE.equals(targetType)) {
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
   * @see XmlRpcScalar#getValueAsString()
   */
  public String getValueAsString() {
    return this.value.toString();
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
    int multiplier = 31;
    int hash = 7;
    hash = multiplier * hash + (this.value != null ? this.value.hashCode() : 0);
    return hash;
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
    StringBuffer buffer = new StringBuffer();
    buffer.append(this.getClass().getName() + ": ");
    buffer.append("value=" + this.value + "; ");
    buffer.append("systemHashCode=" + System.identityHashCode(this));

    return buffer.toString();
  }
}
