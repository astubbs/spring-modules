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
 * Template for classes that allow cache models to be stored per method in a
 * map.
 * </p>
 * 
 * @author Alex Ruiz
 */
public abstract class AbstractMethodMapCacheModelSource {

  protected final Log logger = LogFactory.getLog(getClass());

  /**
   * Map containing instances of <code>{@link CacheModel}</code>. The key of
   * each entry is instance of <code>{@link Method}</code> to attach the
   * attribute to.
   */
  private Map modelMap;

  /**
   * Map containing the methods that matches a given fully qualified name. Each
   * element uses an instance of <code>{@link Method}</code> as key and the
   * fully qualified name as value.
   */
  private Map registeredMethodMap;

  public AbstractMethodMapCacheModelSource() {
    super();
    modelMap = new HashMap();
    registeredMethodMap = new HashMap();
  }

  /**
   * <p>
   * Adds a new entry to <code>{@link #modelMap}</code> using the methods,
   * which names match the given method name, as the entry key and the given
   * cache model as the entry value.
   * </p>
   * <p>
   * Fully qualified names can end or start with "*" for matching multiple
   * methods.
   * </p>
   * 
   * @param targetClass
   *          target interface or class.
   * @param targetMethodName
   *          the method name to match.
   * @param cacheModel
   *          the cache model.
   * 
   * @throws IllegalArgumentException
   *           if there are not any methods matching the given name.
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
      String registeredMethodName = getRegisteredMethodName(method);

      if (registeredMethodName == null
          || (!registeredMethodName.equals(fullyQualifiedTargetMethodName) && registeredMethodName
              .length() <= fullyQualifiedTargetMethodName.length())) {
        // method name was not registered or we have a more specific method
        // name.
        registeredMethodMap.put(method, fullyQualifiedTargetMethodName);
        addCacheModel(method, cacheModel);

      } else {
        if (logger.isDebugEnabled() && registeredMethodName != null) {
          logger.debug("Keeping attribute for cached method [" + method
              + "]: current name '" + fullyQualifiedTargetMethodName
              + "' is not more specific than '" + registeredMethodName + "'");
        }
      }
    }
  }

  private String getRegisteredMethodName(Method key) {
    return (String) registeredMethodMap.get(key);
  }

  /**
   * Adds a new entry to <code>{@link #modelMap}</code> using the given method
   * as key and the given cache attribute as value.
   * 
   * @param method
   *          the method to use as key of the new entry.
   * @param cacheModel
   *          the cache model used as value of the new entry.
   */
  private void addCacheModel(Method method, CacheModel cacheModel) {
    logger.info("Adding method [" + method + "] with cache model ["
        + cacheModel + "]");
    modelMap.put(method, cacheModel);
  }

  /**
   * <p>
   * Adds a new entry to <code>{@link #modelMap}</code> using the methods
   * which name that match the given fully qualified name as the entry key and
   * the given cache model as the entry value.
   * </p>
   * <p>
   * Fully qualified names can end or start with "*" for matching multiple
   * methods.
   * </p>
   * 
   * @param fullyQualifiedMethodName
   *          the fully qualified name of the methods to attach the cache
   *          attribute to. class and method name, separated by a dot
   * @param cacheModel
   *          the cache model.
   * 
   * @throws IllegalArgumentException
   *           if the given method name is not a fully qualified name.
   * @throws IllegalArgumentException
   *           if the class specified in the fully qualified method name cannot
   *           be found.
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
   * Returns an unmodifiable view of <code>{@link #modelMap}</code>.
   * 
   * @return an unmodifiable view of the member variable
   *         <code>attributeMap</code>.
   */
  public final Map getModelMap() {
    return Collections.unmodifiableMap(modelMap);
  }

  /**
   * Returns <code>true</code> if the given method name matches the mapped
   * name. The default implementation checks for "xxx*" and "*xxx" matches.
   * 
   * @param methodName
   *          the method name of the class.
   * @param mappedName
   *          the name in the descriptor.
   * @return <code>true</code> if the names match.
   */
  protected boolean isMatch(String methodName, String mappedName) {
    return TextMatcher.isMatch(methodName, mappedName);
  }
}
