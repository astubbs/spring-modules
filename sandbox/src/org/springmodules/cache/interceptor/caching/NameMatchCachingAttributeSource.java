/* 
 * Created on Jan 17, 2005
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

import java.beans.PropertyEditor;
import java.lang.reflect.Method;
import java.util.Map;

import org.springmodules.cache.interceptor.AbstractNameMatchCacheAttributeSource;

/**
 * <p>
 * Simple implementation of <code>{@link CachingAttributeSource}</code> that
 * allows <code>{@link Cached}</code> to be matched by registered name.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/04/12 08:37:53 $
 */
public final class NameMatchCachingAttributeSource extends
    AbstractNameMatchCacheAttributeSource implements CachingAttributeSource {

  /**
   * Constructor.
   */
  public NameMatchCachingAttributeSource() {
    super();
  }

  /**
   * Add a caching attribute for a caching method. Method names can end with "*"
   * for matching multiple methods.
   * 
   * @param methodName
   *          the name of the method.
   * @param cachingAttribute
   *          caching attribute associated with the method.
   */
  protected final void addCachingAttribute(String methodName,
      Cached cachingAttribute) {
    super.addAttribute(methodName, cachingAttribute);
  }

  /**
   * @see AbstractNameMatchCacheAttributeSource#getCacheAttributeEditor()
   */
  protected final PropertyEditor getCacheAttributeEditor() {
    return new CachingAttributeEditor();
  }

  /**
   * @see CachingAttributeSource#getCachingAttribute(Method, Class)
   */
  public final Cached getCachingAttribute(Method method, Class targetClass) {
    Cached cachingAttribute = (Cached) super.getCacheAttribute(method);
    return cachingAttribute;
  }

  /**
   * Returns an unmodifiable view of the map of caching attributes.
   * 
   * @return an unmodifiable view of the map of caching attributes.
   */
  protected final Map getCachingAttributeMap() {
    return super.getAttributeMap();
  }
}