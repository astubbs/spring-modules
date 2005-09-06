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

package org.springmodules.cache.interceptor.flush;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;

import org.springmodules.cache.CacheAttribute;
import org.springmodules.cache.interceptor.AbstractSingleMetadataCacheAttributeSource;

/**
 * <p>
 * Template for implementations of
 * <code>{@link CacheFlushAttributeSource}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.5 $ $Date: 2005/09/06 01:41:35 $
 */
public abstract class AbstractCacheFlushAttributeSource extends
    AbstractSingleMetadataCacheAttributeSource implements
    CacheFlushAttributeSource {

  public AbstractCacheFlushAttributeSource() {
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

        if (object instanceof FlushCache) {
          attribute = (CacheAttribute) object;
        } // end 'if (object instanceof FlushCache)'
      } // end 'while (attributeIterator.hasNext() && null == attribute)'
    } // if (attributes != null)

    return attribute;
  }

  /**
   * @see CacheFlushAttributeSource#getCacheFlushAttribute(Method, Class)
   */
  public FlushCache getCacheFlushAttribute(Method method, Class targetClass) {
    FlushCache attribute = (FlushCache) getAttribute(method, targetClass);
    return attribute;
  }

}