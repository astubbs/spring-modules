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

import org.springframework.metadata.Attributes;
import org.springmodules.cache.CacheAttribute;
import org.springmodules.cache.interceptor.AbstractSingleMetadataCacheAttributeSource;

/**
 * <p>
 * Implementation of <code>{@link CachingAttributeSource}</code> that uses
 * source-level metadata attributes.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.6 $ $Date: 2005/10/13 04:51:38 $
 */
public final class MetadataCachingAttributeSource extends
    AbstractCachingAttributeSource {

  /**
   * Underlying Attributes implementation we're using.
   */
  private Attributes attributes;

  public MetadataCachingAttributeSource() {
    super();
  }

  /**
   * @see org.springmodules.cache.interceptor.AbstractMetadataCacheAttributeSource#findAllAttributes(Method)
   */
  protected Collection findAllAttributes(Method method) {
    Collection allAttributes = attributes.getAttributes(method);
    return allAttributes;
  }

  /**
   * @see AbstractSingleMetadataCacheAttributeSource#findAttribute(Collection)
   */
  protected CacheAttribute findAttribute(Collection methodAttributes) {
    CacheAttribute attribute = null;
    if (methodAttributes != null && !methodAttributes.isEmpty()) {
      for (Iterator i = methodAttributes.iterator(); i.hasNext();) {
        Object object = i.next();
        if (object instanceof Cached) {
          attribute = (CacheAttribute) object;
        }
      }
    }
    return attribute;
  }

  protected Attributes getAttributes() {
    return attributes;
  }

  public void setAttributes(Attributes newAttributes) {
    attributes = newAttributes;
  }

}