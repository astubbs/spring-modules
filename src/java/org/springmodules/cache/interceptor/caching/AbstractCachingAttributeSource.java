/* 
 * Created on Sep 21, 2004
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
 * Copyright @2004 the original author or authors.
 */

package org.springmodules.cache.interceptor.caching;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;

import org.springmodules.cache.CacheAttribute;
import org.springmodules.cache.interceptor.AbstractSingleMetadataCacheAttributeSource;

/**
 * <p>
 * Template for implementations of <code>{@link CachingAttributeSource}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.3 $ $Date: 2005/04/27 01:41:08 $
 */
public abstract class AbstractCachingAttributeSource extends
    AbstractSingleMetadataCacheAttributeSource implements
    CachingAttributeSource {

  /**
   * Constructor.
   */
  public AbstractCachingAttributeSource() {
    super();
  }

  /**
   * @see AbstractSingleMetadataCacheAttributeSource#findAttribute(Collection)
   */
  protected CacheAttribute findAttribute(Collection attributes) {

    CacheAttribute attribute = null;

    if (attributes != null && !attributes.isEmpty()) {
      Iterator attributeIterator = attributes.iterator();

      while (attributeIterator.hasNext() && null == attribute) {
        Object object = attributeIterator.next();

        if (object instanceof Cached) {
          attribute = (CacheAttribute) object;
        } // end 'if (object instanceof Cached)'
      } // end 'while (attributeIterator.hasNext() && null == attribute)'
    } // end 'if (attributes != null)'

    return attribute;
  }

  /**
   * @see CachingAttributeSource#getCachingAttribute(Method, Class)
   */
  public final Cached getCachingAttribute(Method method, Class targetClass) {
    Cached attribute = null;

    boolean cacheable = this.isCacheable(method);
    if (cacheable) {
      attribute = (Cached) super.getAttribute(method, targetClass);
    }

    return attribute;
  }

  /**
   * Returns <code>true</code> if the return type of a method can be
   * cacheable. In order to be cacheable, the method should:
   * <ul>
   * <li>have a return type (not <code>void</code>)</li>
   * </ul>
   * 
   * @param method
   *          the method definition to verify.
   * @return <code>true</code> if the return type of a method can be
   *         cacheable.
   */
  protected final boolean isCacheable(Method method) {
    boolean cacheable = true;

    Class returnType = method.getReturnType();
    if ("void".equalsIgnoreCase(returnType.getName())) {
      cacheable = false;
    }

    return cacheable;
  }
}