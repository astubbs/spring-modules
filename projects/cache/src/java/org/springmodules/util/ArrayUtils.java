/* 
 * Created on Aug 2, 2005
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
package org.springmodules.util;

import java.util.Set;
import java.util.TreeSet;

/**
 * <p>
 * Utiltity methods for manipulating arrays.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public abstract class ArrayUtils {

  private static final String ARRAY_END = "}";

  private static final String ARRAY_START = "{";

  private static final String ELEMENT_SEPARATOR = ", ";

  private static final String EMPTY_ARRAY = "{}";

  private static final String NULL_ARRAY = "null";

  public static boolean hasElements(Object[] array) {
    return (array != null && array.length > 0);
  }

  public static int hashCode(Object[] array) {
    int multiplier = 31;
    int hash = 7;

    if (array == null || array.length == 0) {
      hash = multiplier * hash;
    } else {
      int length = array.length;
      for (int i = 0; i < length; i++) {
        Object obj = array[i];
        hash = multiplier * hash + (obj != null ? obj.hashCode() : 0);
      }
    }

    return hash;
  }

  public static String[] removeDuplicates(String[] array) {
    if (!hasElements(array)) {
      return array;
    }
    Set set = new TreeSet();
    int length = array.length;
    for (int i = 0; i < length; i++) {
      set.add(array[i]);
    }
    return (String[]) set.toArray(new String[set.size()]);
  }

  public static String toString(Object[] array) {
    if (array == null)
      return NULL_ARRAY;
    if (array.length == 0)
      return EMPTY_ARRAY;

    StringBuffer buffer = new StringBuffer();
    int length = array.length;
    for (int i = 0; i < length; i++) {
      if (i == 0)
        buffer.append(ARRAY_START);
      else
        buffer.append(ELEMENT_SEPARATOR);

      buffer.append(Strings.quoteIfString(array[i]));
    }

    buffer.append(ARRAY_END);
    return buffer.toString();
  }

  public static String toString(String[] array) {
    if (array == null)
      return NULL_ARRAY;
    if (array.length == 0)
      return EMPTY_ARRAY;

    StringBuffer buffer = new StringBuffer();
    int length = array.length;
    for (int i = 0; i < length; i++) {
      if (i == 0)
        buffer.append(ARRAY_START);
      else
        buffer.append(ELEMENT_SEPARATOR);

      buffer.append(Strings.quote(array[i]));
    }

    buffer.append(ARRAY_END);
    return buffer.toString();
  }
}
