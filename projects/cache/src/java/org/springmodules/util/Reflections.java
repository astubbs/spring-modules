/* 
 * Created on Nov 25, 2005
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

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.springframework.util.ReflectionUtils;

/**
 * <p>
 * Reflection-related utility methods.
 * </p>
 * 
 * @author Alex Ruiz
 */
public abstract class Reflections {

  private static final int INITIAL_HASH = 7;

  private static final int MULTIPLIER = 31;

  private static int reflectionHashCode(Collection collection) {
    int hash = INITIAL_HASH;

    for (Iterator i = collection.iterator(); i.hasNext();) {
      hash = MULTIPLIER * hash + reflectionHashCode(i.next());
    }

    return hash;
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
    hash = MULTIPLIER * hash + reflectionHashCode(entry.getKey());
    hash = MULTIPLIER * hash + reflectionHashCode(entry.getValue());
    return hash;
  }

  public static int reflectionHashCode(Object obj) {
    if (obj == null)
      return 0;

    Class targetClass = obj.getClass();
    if (targetClass.isArray() || Objects.isPrimitiveOrWrapper(targetClass)
        || obj instanceof String) {
      return Objects.nullSafeHashCode(obj);
    }

    if (obj instanceof Collection) {
      return reflectionHashCode((Collection) obj);
    }

    if (obj instanceof Map) {
      return reflectionHashCode((Map) obj);
    }

    int hash = INITIAL_HASH;

    try {
      while (targetClass != null) {
        Field[] fields = targetClass.getDeclaredFields();
        AccessibleObject.setAccessible(fields, true);

        for (int i = 0; i < fields.length; i++) {
          Field field = fields[i];
          int modifiers = field.getModifiers();

          if (!Modifier.isStatic(modifiers) && !Modifier.isTransient(modifiers)) {
            hash = MULTIPLIER * hash + reflectionHashCode(field.get(obj));
          }
        }
        targetClass = targetClass.getSuperclass();
      }
    } catch (IllegalAccessException exception) {
      ReflectionUtils.handleReflectionException(exception);
    }

    return hash;
  }
}
