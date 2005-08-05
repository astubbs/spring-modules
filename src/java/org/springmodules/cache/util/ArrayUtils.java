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
package org.springmodules.cache.util;

/**
 * <p>
 * This class contains various methods for manipulating arrays.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public abstract class ArrayUtils {

  /**
   * Returns a string representation of the contents of the specified array. If
   * the array contains other arrays as elements, they are converted to strings
   * by the {@link Object#toString} method inherited from <tt>Object</tt>,
   * which describes their <i>identities</i> rather than their contents.
   * 
   * <p>
   * The value returned by this method is equal to the value that would be
   * returned by <tt>Arrays.asList(a).toString()</tt>, unless <tt>a</tt> is
   * <tt>null</tt>, in which case <tt>"null"</tt> is returned.
   * 
   * @param array
   *          the array whose string representation to return.
   * @return a string representation of <tt>array</tt>.
   */
  public static String toString(String[] array) {
    if (array == null)
      return "null";
    if (array.length == 0)
      return "{}";

    StringBuffer buffer = new StringBuffer();

    for (int i = 0; i < array.length; i++) {
      if (i == 0)
        buffer.append('{');
      else
        buffer.append(", ");

      String element = array[i];
      String formattedElement = null;
      if (element != null) {
        formattedElement = "'" + String.valueOf(array[i]) + "'";
      }
      buffer.append(formattedElement);
    }

    buffer.append("}");
    return buffer.toString();
  }
}
