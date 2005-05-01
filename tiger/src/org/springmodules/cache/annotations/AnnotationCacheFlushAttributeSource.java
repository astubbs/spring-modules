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
import org.springmodules.cache.interceptor.flush.AbstractCacheFlushAttributeSource;
import org.springmodules.cache.interceptor.flush.FlushCache;

/**
 * <p>
 * TODO Document class.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/05/01 23:20:14 $
 */
public class AnnotationCacheFlushAttributeSource extends
    AbstractCacheFlushAttributeSource {

  /**
   * Constructor.
   */
  public AnnotationCacheFlushAttributeSource() {
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
   * @see AbstractCacheFlushAttributeSource#findAttribute(java.util.Collection)
   */
  @Override
  protected CacheAttribute findAttribute(Collection attributes) {
    FlushCache flushCache = null;

    if (attributes != null) {
      for (Object attribute : attributes) {
        if (attribute instanceof CacheFlush) {
          CacheFlush cacheFlush = (CacheFlush) attribute;
          String[] cacheProfileIds = cacheFlush.cacheProfileIds();
          boolean flushBeforeExecution = cacheFlush.flushBeforeExecution();
          
          flushCache = new FlushCache(cacheProfileIds, flushBeforeExecution);
          break;
        }
      }
    }

    return flushCache;
  }

}
