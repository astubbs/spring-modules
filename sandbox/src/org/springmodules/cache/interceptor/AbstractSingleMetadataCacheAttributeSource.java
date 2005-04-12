/* 
 * Created on Oct 19, 2004
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

package org.springmodules.cache.interceptor;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

import org.springframework.aop.support.AopUtils;
import org.springmodules.cache.CacheAttribute;

/**
 * <p>
 * Template for classes that retrieve a single source-level metadata attribute
 * per class method.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/04/12 08:37:54 $
 */
public abstract class AbstractSingleMetadataCacheAttributeSource extends
    AbstractMetadataCacheAttributeSource {

  /**
   * Constructor.
   */
  public AbstractSingleMetadataCacheAttributeSource() {
    super();
  }

  /**
   * Returns an instance of <code>{@link CacheAttribute}</code>, given this
   * set of metadata attributes attached to a method or class.
   * 
   * @param attributes
   *          metadata attributes attached to a method. May be <code>null</code>,
   *          in which case <code>null</code> will be returned.
   * @return an instance of <code>CacheAttribute</code>.
   */
  protected abstract CacheAttribute findAttribute(Collection attributes);

  /**
   * Returns an instance of <code>{@link CacheAttribute}</code> for the
   * specified method definition.
   * 
   * @param method
   *          the specified method definition.
   * @param targetClass
   *          the target class. May be null, in which case the declaring class
   *          of the method must be used.
   * @return a <code>CachingAttribute</code> for the specified method.
   */
  protected final CacheAttribute getAttribute(Method method, Class targetClass) {
    Map attributeMap = super.getAttributeMap();

    // First, see if we have a cached value
    Object attributeEntryKey = super.getAttributeEntryKey(method, targetClass);
    Object cachedAttribute = attributeMap.get(attributeEntryKey);

    if (cachedAttribute != null) {
      // Value will either be canonical value indicating there is no caching
      // attribute, or an actual attribute
      if (cachedAttribute == NULL_ATTRIBUTE) {
        return null;
      }

      return (CacheAttribute) cachedAttribute;
    }

    CacheAttribute attribute = this.retrieveAttribute(method, targetClass);

    // Put it in the cache
    if (attribute == null) {
      attributeMap.put(attributeEntryKey, NULL_ATTRIBUTE);
    } else {
      attributeMap.put(attributeEntryKey, attribute);
    }

    return attribute;
  }

  /**
   * Same as <code>{@link #getAttribute(Method, Class)}</code>, but doesn't
   * cache the result. <code>{@link #getAttribute(Method, Class)}</code> is a
   * decorator for the specified method definition.
   * 
   * @param method
   *          the specified method definition.
   * @param targetClass
   *          the target class. May be null, in which case the declaring class
   *          of the method must be used.
   */
  protected final CacheAttribute retrieveAttribute(Method method,
      Class targetClass) {

    CacheAttribute attribute = null;

    // The method may be on an interface, but we need attributes from the
    // target class.

    // The AopUtils class provides a convenience method for this. If the
    // target class is null, the method will be unchanged.
    Method specificMethod = AopUtils.getMostSpecificMethod(method, targetClass);

    // First try is the method in the target class
    Collection allAttributes = this.findAllAttributes(specificMethod);
    attribute = this.findAttribute(allAttributes);

    if (null == attribute && specificMethod != method) {
      // Fallback is to look at the original method
      allAttributes = this.findAllAttributes(method);
      attribute = this.findAttribute(allAttributes);
    }

    return attribute;
  }
}