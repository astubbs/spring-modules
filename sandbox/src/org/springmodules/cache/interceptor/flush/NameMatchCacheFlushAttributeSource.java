/* 
 * Created on Jan 19, 2005
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

package org.springmodules.cache.interceptor.flush;

import java.beans.PropertyEditor;
import java.lang.reflect.Method;

import org.springmodules.cache.interceptor.AbstractNameMatchCacheAttributeSource;

/**
 * <p>
 * Simple implementation of <code>{@link CacheFlushAttributeSource}</code>
 * that allows <code>{@link FlushCache}</code> to be matched by registered
 * name.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/04/12 08:37:44 $
 */
public final class NameMatchCacheFlushAttributeSource extends
    AbstractNameMatchCacheAttributeSource implements CacheFlushAttributeSource {

  /**
   * Constructor.
   */
  public NameMatchCacheFlushAttributeSource() {
    super();
  }

  /**
   * @see AbstractNameMatchCacheAttributeSource#getCacheAttributeEditor()
   */
  protected PropertyEditor getCacheAttributeEditor() {
    return new CacheFlushAttributeEditor();
  }

  /**
   * @see CacheFlushAttributeSource#getCacheFlushAttribute(Method, Class)
   */
  public FlushCache getCacheFlushAttribute(Method method, Class targetClass) {
    FlushCache cacheFlushAttribute = (FlushCache) super
        .getCacheAttribute(method);
    return cacheFlushAttribute;
  }
}