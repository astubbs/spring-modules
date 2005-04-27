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
package org.springmodules.cache.interceptor.flush;

import java.lang.reflect.Method;

import org.springmodules.cache.interceptor.AbstractMethodMapCacheAttributeSource;
import org.springmodules.cache.interceptor.caching.CachingAttributeSource;

/**
 * <p>
 * Simple implementation of <code>{@link CachingAttributeSource}</code> that
 * allows attributes to be stored per method in a map.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/04/27 01:38:07 $
 */
public class MethodMapCacheFlushAttributeSource extends
    AbstractMethodMapCacheAttributeSource implements CacheFlushAttributeSource {

  /**
   * Constructor.
   */
  public MethodMapCacheFlushAttributeSource() {
    super();
  }

  /**
   * <p>
   * Adds a new entry to map of cache-flush attributes using the methods which
   * name that match the given fully qualified name as the entry key and the
   * given cache-flush attribute as the entry value.
   * </p>
   * <p>
   * Fully qualified names can end or start with "*" for matching multiple
   * methods.
   * </p>
   * 
   * @param fullyQualifiedMethodName
   *          the fully qualified name of the methods to attach the caching
   *          attribute to. class and method name, separated by a dot
   * @param cacheFlushAttribute
   *          the cache-flush attribute.
   * 
   * @throws IllegalArgumentException
   *           if the given method name is not a fully qualified name.
   * @throws IllegalArgumentException
   *           if the class specified in the fully qualified method name cannot
   *           be found.
   */
  public final void addCacheFlushAttribute(String fullyQualifiedMethodName,
      FlushCache cacheFlushAttribute) {
    super.addCacheAttribute(fullyQualifiedMethodName, cacheFlushAttribute);
  }

  /**
   * @see CacheFlushAttributeSource#getCacheFlushAttribute(Method, Class)
   */
  public FlushCache getCacheFlushAttribute(Method method, Class targetClass) {
    return (FlushCache) super.getAttributeMap().get(method);
  }
}