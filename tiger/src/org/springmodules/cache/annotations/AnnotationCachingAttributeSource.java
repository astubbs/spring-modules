/* 
 * Created on Apr 29, 2005
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
package org.springmodules.cache.annotations;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;

import org.springmodules.cache.CacheAttribute;
import org.springmodules.cache.interceptor.caching.AbstractCachingAttributeSource;
import org.springmodules.cache.interceptor.caching.Cached;

/**
 * <p>
 * Implementation of
 * <code>{@link org.springmodules.cache.interceptor.caching.CachingAttributeSource}</code>
 * for working with caching metadata in JDK 1.5+ annotation format.
 * </p>
 * 
 * <p>
 * This class reads the JDK 1.5+ <code>{@link Cacheable}</code> annotation and
 * exposes corresponding caching attributes to our caching infrastructure.
 * </p>
 * 
 * <p>
 * This is a direct alternative to
 * <code>{@link org.springmodules.cache.interceptor.caching.MetadataCachingAttributeSource}</code>,
 * which is able to read in source-level attributes via Commons Attributes.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.2 $ $Date: 2005/09/29 01:21:48 $
 */
public class AnnotationCachingAttributeSource extends
    AbstractCachingAttributeSource {

  /**
   * Constructor.
   */
  public AnnotationCachingAttributeSource() {
    super();
  }

  /**
   * Returns all JDK 1.5+ annotations found for the given method.
   * 
   * @return all JDK 1.5+ annotations found for the given method.
   */
  @Override
  protected Collection findAllAttributes(Method method) {
    return Arrays.asList(method.getAnnotations());
  }

  /**
   * Return the caching attribute, given this set of attributes attached to a
   * method. Overrides method from parent class. This version actually converts
   * JDK 5.0+ Annotations to <code>{@link Cached}</code>.
   * 
   * @param attributes
   *          attributes attached to a method. May be <code>null</code>, in
   *          which case a <code>null</code> caching attribute will be
   *          returned.
   * @return a caching attribute (if found).
   */
  @Override
  protected CacheAttribute findAttribute(Collection attributes) {
    Cached cached = null;

    if (attributes != null) {
      for (Object attribute : attributes) {
        if (attribute instanceof Cacheable) {
          Cacheable cacheable = (Cacheable) attribute;
          String cacheModelId = cacheable.cacheModelId();

          cached = new Cached(cacheModelId);
          break;
        }
      }
    }

    return cached;
  }

}
