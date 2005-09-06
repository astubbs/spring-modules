/* 
 * Created on March 2, 2005
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
package org.springmodules.cache.interceptor.caching;

import java.lang.reflect.Method;

import org.springmodules.cache.interceptor.AbstractMethodMapCacheAttributeSource;

/**
 * <p>
 * Simple implementation of <code>{@link CachingAttributeSource}</code> that
 * allows attributes to be stored per method in a map.
 * </p>
 * 
 * @author Xavier Dury
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.5 $ $Date: 2005/09/06 01:41:29 $
 */
public class MethodMapCachingAttributeSource extends
    AbstractMethodMapCacheAttributeSource implements CachingAttributeSource {

  public MethodMapCachingAttributeSource() {
    super();
  }

  /**
   * <p>
   * Adds a new entry to map of caching attributes using the methods which name
   * that match the given fully qualified name as the entry key and the given
   * caching attribute as the entry value.
   * </p>
   * <p>
   * Fully qualified names can end or start with "*" for matching multiple
   * methods.
   * </p>
   * 
   * @param fullyQualifiedMethodName
   *          the fully qualified name of the methods to attach the caching
   *          attribute to. class and method name, separated by a dot
   * @param cachingAttribute
   *          the caching attribute.
   * 
   * @throws IllegalArgumentException
   *           if the given method name is not a fully qualified name.
   * @throws IllegalArgumentException
   *           if the class specified in the fully qualified method name cannot
   *           be found.
   */
  public final void addCachingAttribute(String fullyQualifiedMethodName,
      Cached cachingAttribute) {
    addCacheAttribute(fullyQualifiedMethodName, cachingAttribute);
  }

  /**
   * @see CachingAttributeSource#getCachingAttribute(Method, Class)
   */
  public final Cached getCachingAttribute(Method method, Class targetClass) {
    return (Cached) getAttributeMap().get(method);
  }
}