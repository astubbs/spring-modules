/* 
 * Created on Apr 26, 2005
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
package org.springmodules.cache.interceptor;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springmodules.cache.CacheModel;
import org.springmodules.cache.util.TextMatcher;

/**
 * <p>
 * Template thats allow cache models to be stored per method in a map.
 * </p>
 * 
 * @author Alex Ruiz
 */
public abstract class AbstractMethodMapCacheModelSource {

  /** Logger available to subclasses */
  protected final Log logger = LogFactory.getLog(getClass());

  private Map cacheModels;

  /**
   * Map containing the methods that matches a given fully qualified name. Each
   * element uses an instance of <code>{@link Method}</code> as key and the
   * fully qualified name as value.
   */
  private Map registeredMethodMap;

  /**
   * Construct a <code>AbstractMethodMapCacheModelSource</code>.s
   */
  public AbstractMethodMapCacheModelSource() {
    super();
    cacheModels = new HashMap();
    registeredMethodMap = new HashMap();
  }

  /**
   * <p>
   * Binds the given cache model to the methods of the given class or interface
   * which names match the given target method key.
   * </p>
   * <p>
   * Methods names can end or start with "*" for matching multiple methods.
   * </p>
   * 
   * @param targetClass
   *          target interface or class
   * @param targetMethodName
   *          the method name to match
   * @param cacheModel
   *          the cache model
   * 
   * @throws IllegalArgumentException
   *           if there are not any methods matching the given name
   */
  private void addCacheModel(Class targetClass, String targetMethodName,
      CacheModel cacheModel) {
    String fullyQualifiedTargetMethodName = targetClass.getName() + '.'
        + targetMethodName;

    List matchingMethods = new LinkedList();

    // get the methods that match the given target method name.
    Method[] methods = targetClass.getDeclaredMethods();
    int methodCount = methods.length;
    for (int i = 0; i < methodCount; i++) {
      Method method = methods[i];
      String methodName = method.getName();

      if (methodName.equals(targetMethodName)
          || isMatch(methodName, targetMethodName)) {
        matchingMethods.add(method);
      }
    }

    if (matchingMethods.isEmpty()) {
      throw new IllegalArgumentException("Couldn't find any method matching '"
          + fullyQualifiedTargetMethodName + "'");
    }

    // register all matching methods.
    for (Iterator i = matchingMethods.iterator(); i.hasNext();) {
      Method method = (Method) i.next();
      String registeredMethodName = (String) registeredMethodMap.get(method);

      if (registeredMethodName == null
          || (!registeredMethodName.equals(fullyQualifiedTargetMethodName) && registeredMethodName
              .length() <= fullyQualifiedTargetMethodName.length())) {
        // method name was not registered or we have a more specific method
        // name.
        registeredMethodMap.put(method, fullyQualifiedTargetMethodName);
        addCacheModel(method, cacheModel);
      }
    }
  }

  /**
   * Binds the given cache model to the given method.
   * 
   * @param method
   *          the given method
   * @param cacheModel
   *          the cache model to bind to the method
   */
  private void addCacheModel(Method method, CacheModel cacheModel) {
    logger.info("Adding method [" + method + "] with cache model ["
        + cacheModel + "]");
    cacheModels.put(method, cacheModel);
  }

  /**
   * <p>
   * Binds the given cache model to the methods which names match the given
   * method name. The fully qualified name of the class declaring the method
   * should be included.
   * </p>
   * <p>
   * Method names can end or start with "*" for matching multiple methods.
   * </p>
   * 
   * @param fullyQualifiedMethodName
   *          the fully qualified name of the class methods to attach the cache
   *          attribute to. class and method name, separated by a dot
   * @param cacheModel
   *          the cache model
   * 
   * @throws IllegalArgumentException
   *           if the given method name does not include the fully qualified
   *           name of the declaring class
   * @throws IllegalArgumentException
   *           if the specified class cannot be found
   * 
   * @see #addCacheModel(Class, String, CacheModel)
   */
  protected final void addCacheModel(String fullyQualifiedMethodName,
      CacheModel cacheModel) {
    int methodSeparatorIndex = fullyQualifiedMethodName.lastIndexOf(".");

    if (methodSeparatorIndex == -1) {
      throw new IllegalArgumentException("'" + fullyQualifiedMethodName
          + "' is not a fully qualified name");
    }

    String className = fullyQualifiedMethodName.substring(0,
        methodSeparatorIndex);
    String methodName = fullyQualifiedMethodName
        .substring(methodSeparatorIndex + 1);

    try {
      Class targetClass = Class.forName(className, true, Thread.currentThread()
          .getContextClassLoader());
      addCacheModel(targetClass, methodName, cacheModel);

    } catch (ClassNotFoundException exception) {
      throw new IllegalArgumentException("Class '" + className + "' not found");
    }
  }

  /**
   * @return an <i>unmodifiable</i> view of the map containing cache models.
   *          The key of each entry is the <code>java.lang.reflect.Method</code>
   *          to which the cache model is attached to
   */
  public final Map getCacheModels() {
    return Collections.unmodifiableMap(cacheModels);
  }

  /**
   * <p>
   * Returns <code>true</code> if the given method name matches the mapped
   * name. The default implementation checks for "xxx*" and "*xxx" matches.
   * </p>
   * <p>
   * For example, this method will return <code>true</code> if the given
   * method name is &quot;getUser&quot; and the mapped name is &quot;get*&quot;
   * </p>
   * 
   * @param methodName
   *          the method name
   * @param mappedName
   *          the name in the descriptor
   * @return <code>true</code> if the names match
   */
  protected boolean isMatch(String methodName, String mappedName) {
    return TextMatcher.isMatch(methodName, mappedName);
  }
}
