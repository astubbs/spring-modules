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

import org.springmodules.cache.CachingModel;
import org.springmodules.cache.interceptor.AbstractMethodMapCacheModelSource;

/**
 * <p>
 * Simple implementation of <code>{@link CachingModelSource}</code> that
 * allows caching models to be stored per method in a map.
 * </p>
 * 
 * @author Xavier Dury
 * @author Alex Ruiz
 */
public class MethodMapCachingModelSource extends
    AbstractMethodMapCacheModelSource implements CachingModelSource {

  public MethodMapCachingModelSource() {
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
   * @param model
   *          the caching model.
   * 
   * @throws IllegalArgumentException
   *           if the given method name is not a fully qualified name.
   * @throws IllegalArgumentException
   *           if the class specified in the fully qualified method name cannot
   *           be found.
   */
  public final void addCachingModel(String fullyQualifiedMethodName,
      CachingModel model) {
    addCacheModel(fullyQualifiedMethodName, model);
  }

  /**
   * @see CachingModelSource#getCachingModel(Method, Class)
   */
  public CachingModel getCachingModel(Method method, Class targetClass) {
    return (CachingModel) getModelMap().get(method);
  }
}