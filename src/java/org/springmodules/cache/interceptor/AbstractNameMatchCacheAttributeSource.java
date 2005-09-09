/* 
 * Created on Jan 19, 2005
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

import java.beans.PropertyEditor;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springmodules.cache.CacheAttribute;
import org.springmodules.cache.util.TextMatcher;

/**
 * <p>
 * Template for classes that allow attributes to be matched by registered name.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.7 $ $Date: 2005/09/09 02:19:09 $
 */
public abstract class AbstractNameMatchCacheAttributeSource {

  /**
   * Map containing instances of <code>{@link CacheAttribute}</code>. The key
   * of each entry is the name of the method to attach the attribute to.
   */
  private Map attributeMap;

  protected final Log logger = LogFactory.getLog(getClass());

  public AbstractNameMatchCacheAttributeSource() {
    super();
    attributeMap = new HashMap();
  }

  /**
   * Add an attribute for a caching method. Method names can end with "*" for
   * matching multiple methods.
   * 
   * @param methodName
   *          the name of the method
   * @param cacheAttribute
   *          attribute associated with the method
   */
  protected final void addAttribute(String methodName,
      CacheAttribute cacheAttribute) {
    if (logger.isDebugEnabled()) {
      logger
          .debug("Method 'addMethod(String, CacheAttribute)'. Adding method ["
              + methodName + "] with attribute [" + cacheAttribute + "]");
    }
    attributeMap.put(methodName, cacheAttribute);
  }

  /**
   * Returns an unmodifiable view of <code>{@link #attributeMap}</code>.
   * 
   * @return an unmodifiable view of the member variable
   *         <code>attributeMap</code>.
   */
  protected final Map getAttributeMap() {
    return Collections.unmodifiableMap(attributeMap);
  }

  /**
   * Returns an instance of <code>{@link CacheAttribute}</code> for the
   * intercepted method.
   * 
   * @param method
   *          the definition of the intercepted method.
   * @return a metadata attribute from the intercepted method.
   */
  protected final CacheAttribute getCacheAttribute(Method method) {
    String methodName = method.getName();
    CacheAttribute cacheAttribute = getCacheAttributeFromMap(methodName);

    if (cacheAttribute == null) {
      // look up most specific name match
      String bestNameMatch = null;

      for (Iterator i = attributeMap.keySet().iterator(); i.hasNext();) {
        String mappedMethodName = (String) i.next();

        if (isMatch(methodName, mappedMethodName)
            && (bestNameMatch == null || bestNameMatch.length() <= mappedMethodName
                .length())) {
          cacheAttribute = getCacheAttributeFromMap(mappedMethodName);
          bestNameMatch = mappedMethodName;
        }
      }
    }

    return cacheAttribute;
  }

  /**
   * Returns a property editor that creates instances of
   * <code>{@link CacheAttribute}</code>.
   * 
   * @return a property editor for cache attributes.
   */
  protected abstract PropertyEditor getCacheAttributeEditor();

  private CacheAttribute getCacheAttributeFromMap(String methodName) {
    return (CacheAttribute) attributeMap.get(methodName);
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

  /**
   * Parses the given properties into a name/attribute map. Expects method names
   * as keys and String attributes definitions as values, parsable into
   * <code>{@link CacheAttribute}</code>..
   * 
   * @param cacheAttributes
   *          the given properties.
   */
  public final void setProperties(Properties cacheAttributes) {
    PropertyEditor propertyEditor = getCacheAttributeEditor();

    Iterator keySetIterator = cacheAttributes.keySet().iterator();
    while (keySetIterator.hasNext()) {
      String methodName = (String) keySetIterator.next();
      String cacheAttributeProperties = cacheAttributes.getProperty(methodName);

      propertyEditor.setAsText(cacheAttributeProperties);
      CacheAttribute cacheAttribute = (CacheAttribute) propertyEditor
          .getValue();
      if (cacheAttribute != null) {
        addAttribute(methodName, cacheAttribute);
      }
    }
  }
}