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

/**
 * <p>
 * Represents a XML-RPC array.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.10 $ $Date: 2005/09/25 05:19:59 $
 */
public final class XmlRpcArray implements XmlRpcElement {

  /**
   * Elements of this array.
   */
  private List elements;

  public XmlRpcArray() {
    super();
    elements = new ArrayList();
  }

  public void add(XmlRpcElement value) {
    elements.add(value);
  }

  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof XmlRpcArray)) {
      return false;
    }

    final XmlRpcArray xmlRpcArray = (XmlRpcArray) obj;

    if (elements != null ? !elements.equals(xmlRpcArray.elements)
        : xmlRpcArray.elements != null) {
      return false;
    }

    return true;
  }

  public XmlRpcElement[] getElements() {
    return (XmlRpcElement[]) elements
        .toArray(new XmlRpcElement[elements.size()]);
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
  private Object getMatchingArrayValue(Class targetType) {
    Object matchingValue = null;

    Class componentType = targetType.getComponentType();
    boolean matching = true;

    int valueSize = elements.size();
    Object array = Array.newInstance(componentType, valueSize);

    for (int i = 0; i < valueSize; i++) {
      XmlRpcElement value = (XmlRpcElement) elements.get(i);
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
  private Object getMatchingCollectionValue() {
    Object matchingValue = null;

    boolean matching = true;

    int valueSize = elements.size();
    Collection collection = new ArrayList(valueSize);

    for (int i = 0; i < valueSize; i++) {
      XmlRpcElement value = (XmlRpcElement) elements.get(i);
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
   * @see #getMatchingArrayValue(Class)
   * @see #getMatchingCollectionValue()
   * @see XmlRpcElement#getMatchingValue(Class)
   */
  public Object getMatchingValue(Class targetType) {
    Object matchingValue = NOT_MATCHING;

    if (targetType.isArray()) {
      matchingValue = getMatchingArrayValue(targetType);

    } else if (Collection.class.equals(targetType)
        || List.class.equals(targetType) || ArrayList.class.equals(targetType)) {
      matchingValue = getMatchingCollectionValue();
    }

    return matchingValue;
  }

  public int hashCode() {
    int multiplier = 31;
    int hash = 7;
    hash = multiplier * hash + (elements != null ? elements.hashCode() : 0);
    return hash;
  }

  public String toString() {
    StringBuffer buffer = new StringBuffer(getClass().getName());
    buffer.append("@" + System.identityHashCode(this) + "[");
    buffer.append("elements=" + elements + "]");

    return buffer.toString();
  }
}
