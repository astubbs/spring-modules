/* 
 * Created on Oct 21, 2004
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

import org.springframework.metadata.Attributes;

import org.springmodules.cache.CacheAttribute;
import org.springmodules.cache.interceptor.AbstractSingleMetadataCacheAttributeSource;

/**
 * <p>
 * Implementation of <code>{@link FlushingAttributeSource}</code> that uses
 * source-level metadata attributes.
 * </p>
 * 
 * @author Alex Ruiz
 */
public final class MetadataFlushingAttributeSource extends
    AbstractSingleMetadataCacheAttributeSource implements
    FlushingAttributeSource {

  /**
   * Underlying Attributes implementation we're using.
   */
  private Attributes attributes;

  /**
   * @see FlushingAttributeSource#getFlushingAttribute(Method, Class)
   */
  public FlushCache getFlushingAttribute(Method method, Class targetClass) {
    FlushCache attribute = (FlushCache) getAttribute(method, targetClass);
    return attribute;
  }

  /**
   * Sets the underlying metadata attributes to use.
   * 
   * @param newAttributes
   *          the new underlying metadata attributes
   */
  public final void setAttributes(Attributes newAttributes) {
    attributes = newAttributes;
  }

  /**
   * @see org.springmodules.cache.interceptor.AbstractSingleMetadataCacheAttributeSource#findAllAttributes(Method)
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
        if (object instanceof FlushCache) {
          attribute = (CacheAttribute) object;
        }
      }
    }

    return attribute;
  }

  /**
   * @return the underlying metadata attributes we are using
   */
  protected Attributes getAttributes() {
    return attributes;
  }

}