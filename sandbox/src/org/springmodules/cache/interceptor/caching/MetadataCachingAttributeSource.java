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

import org.springframework.metadata.Attributes;

/**
 * <p>
 * Implementation of <code>{@link CachingAttributeSource}</code> that uses
 * source-level metadata attributes.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/04/12 08:37:52 $
 */
public final class MetadataCachingAttributeSource extends
    AbstractCachingAttributeSource {

  /**
   * Underlying Attributes implementation we're using.
   */
  private Attributes attributes;

  /**
   * Constructor.
   */
  public MetadataCachingAttributeSource() {
    super();
  }

  /**
   * @see org.springmodules.cache.interceptor.AbstractMetadataCacheAttributeSource#findAllAttributes(Method)
   */
  protected Collection findAllAttributes(Method method) {
    Collection allAttributes = this.attributes.getAttributes(method);
    return allAttributes;
  }

  /**
   * Setter for the field <code>{@link #attributes}</code>.
   * 
   * @param attributes
   *          the new value to set
   */
  public final void setAttributes(Attributes attributes) {
    this.attributes = attributes;
  }

}