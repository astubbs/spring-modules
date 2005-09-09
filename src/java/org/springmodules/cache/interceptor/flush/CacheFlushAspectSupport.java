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

/**
 * <p>
 * Superclass for aspects that perform cache flushing.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.5 $ $Date: 2005/09/09 02:19:04 $
 */
public class CacheFlushAspectSupport {

  /**
   * Retrieves source-level metadata attributes from class methods.
   */
  private CacheFlushAttributeSource cacheFlushAttributeSource;

  public CacheFlushAspectSupport() {
    super();
  }

  public final CacheFlushAttributeSource getCacheFlushAttributeSource() {
    return cacheFlushAttributeSource;
  }

  public final void setCacheFlushAttributeSource(
      CacheFlushAttributeSource newCacheFlushAttributeSource) {
    cacheFlushAttributeSource = newCacheFlushAttributeSource;
  }

}