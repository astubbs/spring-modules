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

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springmodules.cache.CacheModel;
import org.springmodules.cache.util.TextMatcher;

/**
 * <p>
 * Template for classes that allow attributes to be matched by registered name.
 * </p>
 * 
 * @author Alex Ruiz
 */
public abstract class AbstractNameMatchCacheModelSource {

  /**
   * Stores instances of <code>{@link CacheModel}</code> implementations under
   * the name of the method associated to it.
   */
  private Map cacheModels;

  protected final Log logger = LogFactory.getLog(getClass());

  public AbstractNameMatchCacheModelSource() {
    super();
  }

  /**
   * Returns an instance of <code>{@link CacheModel}</code> for the
   * intercepted method.
   * 
   * @param method
   *          the definition of the intercepted method.
   * @return a model from the intercepted method.
   */
  protected final CacheModel getCacheModel(Method method) {
    String methodName = method.getName();
    CacheModel model = (CacheModel) cacheModels.get(methodName);

    if (model == null) {
      // look up most specific name match
      String bestNameMatch = null;

      for (Iterator i = cacheModels.keySet().iterator(); i.hasNext();) {
        String mappedMethodName = (String) i.next();

        if (isMatch(methodName, mappedMethodName)
            && (bestNameMatch == null || bestNameMatch.length() <= mappedMethodName
                .length())) {
          model = (CacheModel) cacheModels.get(mappedMethodName);
          bestNameMatch = mappedMethodName;
        }
      }
    }

    return model;
  }

  protected final Map getCacheModels() {
    return cacheModels;
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

  protected final void setCacheModels(Map newCacheModels) {
    cacheModels = newCacheModels;
  }
}