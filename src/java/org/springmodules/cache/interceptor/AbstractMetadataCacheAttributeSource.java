/* 
 * Created on Oct 25, 2004
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
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Template for classes that retrieve a source-level metadata attribute from a
 * method.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.5 $ $Date: 2005/09/09 02:19:08 $
 */
public abstract class AbstractMetadataCacheAttributeSource {

  /**
   * Canonical value held in cache to indicate no attribute was found for this
   * method, and we don't need to look again.
   */
  protected static final Object NULL_ATTRIBUTE = new Object();

  /**
   * Map that caches the source-level metadata attributes found for classes and
   * methods.
   */
  private Map attributeMap;

  public AbstractMetadataCacheAttributeSource() {
    super();
    attributeMap = new HashMap();
  }

  /**
   * Subclasses should implement this method to return all the metadata
   * attributes from the specified method. May return <code>null</code>.
   * 
   * @param method
   *          the method to retrieve attributes for.
   * @return all the metadata attributes associated with the specified method.
   */
  protected abstract Collection findAllAttributes(Method method);

  /**
   * Returns the key of an entry of <code>{@link #attributeMap}</code>.
   * 
   * @param method
   *          the method to retrieve attributes for.
   * @param targetClass
   *          the target class declaring the method.
   * @return the key to access the attribute cache.
   */
  protected final Object getAttributeEntryKey(Method method, Class targetClass) {
    StringBuffer keyBuffer = new StringBuffer(64);
    keyBuffer.append(targetClass);
    keyBuffer.append(System.identityHashCode(method));
    return keyBuffer.toString();
  }

  protected final Map getAttributeMap() {
    return attributeMap;
  }

}