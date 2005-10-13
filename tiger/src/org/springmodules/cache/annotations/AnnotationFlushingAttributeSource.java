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
import org.springmodules.cache.interceptor.flush.AbstractFlushingAttributeSource;
import org.springmodules.cache.interceptor.flush.FlushCache;

/**
 * <p>
 * Implementation of
 * <code>{@link org.springmodules.cache.interceptor.flush.FlushingAttributeSource}</code>
 * for working with caching metadata in JDK 1.5+ annotation format.
 * </p>
 * 
 * <p>
 * This class reads the JDK 1.5+ <code>{@link CacheFlush}</code> annotation
 * and exposes corresponding cache-flush attributes to our caching
 * infrastructure.
 * </p>
 * 
 * <p>
 * This is a direct alternative to
 * <code>{@link org.springmodules.cache.interceptor.flush.MetadataFlushingAttributeSource}</code>,
 * which is able to read in source-level attributes via Commons Attributes.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public class AnnotationFlushingAttributeSource extends
    AbstractFlushingAttributeSource {

  public AnnotationFlushingAttributeSource() {
    super();
  }

  /**
   * @return all JDK 1.5+ annotations found for the given method.
   */
  @Override
  protected Collection findAllAttributes(Method method) {
    return Arrays.asList(method.getAnnotations());
  }

  /**
   * @see AbstractFlushingAttributeSource#findAttribute(java.util.Collection)
   */
  @Override
  protected CacheAttribute findAttribute(Collection attributes) {
    FlushCache flushCache = null;

    if (attributes != null) {
      for (Object attribute : attributes) {
        if (attribute instanceof CacheFlush) {
          CacheFlush cacheFlush = (CacheFlush) attribute;
          flushCache = new FlushCache(cacheFlush.modelId());
          break;
        }
      }
    }

    return flushCache;
  }

}
