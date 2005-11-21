/* 
 * Created on Nov 18, 2005
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

import java.util.Iterator;
import java.util.Map;

/**
 * <p>
 * Utility class that assists in implementing a <code>hashCode</code> method.
 * </p>
 * 
 * @author Alex Ruiz
 */
public abstract class HashCodeBuilder {

  private static final int INITIAL_HASH = 7;

  private static final int MULTIPLIER = 31;

  /**
   * Returns the <code>hashCode</code> of the given argument, 0 if it is
   * <code>null</code>.
   * 
   * @param obj
   *          the given Object.
   * @return the hash code of the given argument, 0 if it is <code>null</code>.
   */
  public static int hashCode(Object obj) {
    if (isNull(obj)) {
      return 0;
    }
    return obj.hashCode();
  }

  private static boolean isNull(Object obj) {
    return obj == null;
  }

  private static int reflectionHashCode(Map map) {
    int hash = INITIAL_HASH;

    for (Iterator i = map.entrySet().iterator(); i.hasNext();) {
      Map.Entry entry = (Map.Entry) i.next();
      hash = MULTIPLIER * hash + reflectionHashCode(entry);
    }

    return hash;
  }

  private static int reflectionHashCode(Map.Entry entry) {
    int hash = INITIAL_HASH;

    Object key = entry.getKey();
    hash = MULTIPLIER * hash + reflectionHashCode(key);

    Object value = entry.getKey();
    hash = MULTIPLIER * hash + reflectionHashCode(value);

    return hash;
  }

  /**
   * Uses reflection to build a hash code value.
   * 
   * @param obj
   *          the Object to create a <code>hashCode</code> for.
   * 
   * @return the generated hash code, 0 if the argument is <code>null</code>.
   */
  public static int reflectionHashCode(Object obj) {
    if (isNull(obj)) {
      return 0;
    }

    if (obj instanceof Map) {
      return reflectionHashCode((Map) obj);
    }

    return org.apache.commons.lang.builder.HashCodeBuilder
        .reflectionHashCode(obj);
  }

  public static int hashCode(Object[] array) {
    if (isNull(array)) {
      return 0;
    }

    int hash = INITIAL_HASH;

    int length = array.length;
    if (length > 0) {
      for (int i = 0; i < length; i++) {
        Object obj = array[i];
        hash = MULTIPLIER * hash + (obj != null ? obj.hashCode() : 0);
      }

    } else {
      hash = MULTIPLIER * hash;
    }

    return hash;
  }

}
