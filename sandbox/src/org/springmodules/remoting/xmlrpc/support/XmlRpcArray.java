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
 * Represents an array.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/06/17 09:57:49 $
 */
public class XmlRpcArray implements XmlRpcElement {

  /**
   * List of values of this array.
   */
  private List values;

  /**
   * Constructor.
   */
  public XmlRpcArray() {
    super();
    this.values = new ArrayList();
  }

  /**
   * Adds a value as an element of this array.
   * 
   * @param value
   *          the value to add.
   */
  public void add(XmlRpcElement value) {
    this.values.add(value);
  }

  /**
   * Returns the values of this array.
   * 
   * @return the values of this array.
   */
  public XmlRpcElement[] getValues() {
    return (XmlRpcElement[]) this.values.toArray(new XmlRpcElement[this.values
        .size()]);
  }

  /**
   * @see XmlRpcElement#getMatchingValue(Class)
   */
  public Object getMatchingValue(Class type) {
    Object matchingValue = NOT_MATCHING;

    if (type.isArray()) {
      Class componentType = type.getComponentType();
      boolean matching = true;

      int valueSize = this.values.size();
      Object array = Array.newInstance(componentType, valueSize);

      for (int i = 0; i < valueSize; i++) {
        XmlRpcElement value = (XmlRpcElement) this.values.get(i);
        Object item = value.getMatchingValue(componentType);

        if (item == NOT_MATCHING) {
          matching = false;
          break;
        }
        Array.set(array, i, item);
      }

      if (matching) {
        matchingValue = array;
      }

    } else if (Collection.class.equals(type) || List.class.equals(type)
        || ArrayList.class.equals(type)) {
      boolean matching = true;

      int valueSize = this.values.size();
      Collection collection = new ArrayList(valueSize);

      for (int i = 0; i < valueSize; i++) {
        XmlRpcElement value = (XmlRpcElement) this.values.get(i);
        if (value instanceof XmlRpcScalar) {
          XmlRpcScalar scalar = (XmlRpcScalar) value;
          collection.add(scalar.getValue());
        } else {
          matching = false;
        }
      }

      if (matching) {
        matchingValue = collection;
      }
    }

    return matchingValue;
  }
}
