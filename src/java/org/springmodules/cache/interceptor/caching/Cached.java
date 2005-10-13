/* 
 * Created on Nov 10, 2004
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

import org.springmodules.cache.CacheAttribute;
import org.springmodules.util.Strings;

/**
 * <p>
 * Source-level metadata attribute that identifies the methods which return
 * value should be stored in the cache.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class Cached implements CacheAttribute {

  private static final long serialVersionUID = 3256728394032297785L;

  /**
   * The id of the
   * <code>{@link org.springmodules.cache.CachingModel}</code> to
   * use.
   */
  private String modelId;

  public Cached() {
    super();
  }

  public Cached(String newModelId) {
    this();
    setModelId(newModelId);
  }

  /**
   * @see Object#equals(Object)
   */
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Cached)) {
      return false;
    }

    final Cached cached = (Cached) obj;

    if (modelId != null ? !modelId.equals(cached.modelId)
        : cached.modelId != null) {
      return false;
    }

    return true;
  }

  public final String getModelId() {
    return modelId;
  }

  /**
   * @see Object#hashCode()
   */
  public int hashCode() {
    int multiplier = 31;
    int hash = 7;
    hash = multiplier * hash + (modelId != null ? modelId.hashCode() : 0);
    return hash;
  }

  public final void setModelId(String newCachingModelId) {
    modelId = newCachingModelId;
  }

  /**
   * @see Object#toString()
   */
  public String toString() {
    StringBuffer buffer = new StringBuffer(getClass().getName());
    buffer.append("@" + System.identityHashCode(this) + "[");
    buffer.append("modelId=" + Strings.quote(modelId) + "]");

    return buffer.toString();
  }
}