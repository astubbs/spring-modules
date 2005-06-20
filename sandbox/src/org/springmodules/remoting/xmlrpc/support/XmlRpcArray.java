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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * <p>
 * Represents a XML-RPC array.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.4 $ $Date: 2005/06/20 22:50:54 $
 */
public class XmlRpcArray implements XmlRpcElement {

  /**
   * Elements of this array.
   */
  private List elements;

  /**
   * Constructor.
   */
  public XmlRpcArray() {
    super();
    this.elements = new ArrayList();
  }

  /**
   * Adds a value as an element of this array.
   * 
   * @param value
   *          the value to add.
   */
  public void add(XmlRpcElement value) {
    this.elements.add(value);
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

    if (null == obj || !(obj instanceof XmlRpcArray)) {
      equals = false;
    } else if (this != obj) {
      XmlRpcArray xmlRpcArray = (XmlRpcArray) obj;

      EqualsBuilder equalsBuilder = new EqualsBuilder();
      equalsBuilder.append(this.getElements(), xmlRpcArray.getElements());

      equals = equalsBuilder.isEquals();
    }

    return equals;
  }

  /**
   * Returns an array of objects of the specified type if the such type is
   * supported by this XML-RPC array. Otherwise returns
   * <code>{@link #NOT_MATCHING}</code>.
   * 
   * @param targetType
   *          the target type.
   * @return an array of objects of the specified type.
   */
  protected Object getArrayMatchingValue(Class targetType) {
    Object matchingValue = null;

    Class componentType = targetType.getComponentType();
    boolean matching = true;

    int valueSize = this.elements.size();
    Object array = Array.newInstance(componentType, valueSize);

    for (int i = 0; i < valueSize; i++) {
      XmlRpcElement value = (XmlRpcElement) this.elements.get(i);
      Object item = value.getMatchingValue(componentType);

      if (item == NOT_MATCHING) {
        matching = false;
        break;
      }
      Array.set(array, i, item);
    }

    if (matching) {
      matchingValue = array;
    } else {
      matchingValue = NOT_MATCHING;
    }

    return matchingValue;
  }

  /**
   * Returns an collection only if this XML-RPC array contains scalar values.
   * Otherwise returns <code>{@link #NOT_MATCHING}</code>.
   * 
   * @return an collection of scalar values.
   */
  protected Object getCollectionMatchingValue() {
    Object matchingValue = null;

    boolean matching = true;

    int valueSize = this.elements.size();
    Collection collection = new ArrayList(valueSize);

    for (int i = 0; i < valueSize; i++) {
      XmlRpcElement value = (XmlRpcElement) this.elements.get(i);
      if (value instanceof XmlRpcScalar) {
        XmlRpcScalar scalar = (XmlRpcScalar) value;
        collection.add(scalar.getValue());
      } else {
        matching = false;
      }
    }

    if (matching) {
      matchingValue = collection;
    } else {
      matchingValue = NOT_MATCHING;
    }

    return matchingValue;
  }

  /**
   * Returns the elements of this array.
   * 
   * @return the elements of this array.
   */
  public XmlRpcElement[] getElements() {
    return (XmlRpcElement[]) this.elements
        .toArray(new XmlRpcElement[this.elements.size()]);
  }

  /**
   * Returns:
   * <ul>
   * <li>An array. If the specified type is an array and all the elements of
   * this XML-RPC array match the component type of the given type.</li>
   * <li>A collection. If the specified type is <code>{@link Collection}</code>,
   * <code>{@link List}</code> or <code>{@link ArrayList}</code> and the
   * elements of this XML-RPC array are scalar values.</li>
   * </ul>
   * 
   * @param targetType
   *          the target type.
   * @return an array or collection depending on the given target typeF.
   * 
   * @see #getArrayMatchingValue(Class)
   * @see #getCollectionMatchingValue()
   * @see XmlRpcElement#getMatchingValue(Class)
   */
  public Object getMatchingValue(Class targetType) {
    Object matchingValue = NOT_MATCHING;

    if (targetType.isArray()) {
      matchingValue = this.getArrayMatchingValue(targetType);

    } else if (Collection.class.equals(targetType)
        || List.class.equals(targetType) || ArrayList.class.equals(targetType)) {
      matchingValue = this.getCollectionMatchingValue();
    }

    return matchingValue;
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
    HashCodeBuilder hashCodeBuilder = new HashCodeBuilder(1429, 1433);
    hashCodeBuilder.append(this.elements);

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
    toStringBuilder.append("elements", this.elements);

    String toString = toStringBuilder.toString();
    return toString;
  }
}
