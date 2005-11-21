/* 
 * Created on Sep 29, 2005
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
package org.springmodules.cache.provider.ehcache;

import java.util.Arrays;

import org.springframework.util.StringUtils;
import org.springmodules.cache.provider.AbstractFlushingModel;
import org.springmodules.util.ArrayUtils;
import org.springmodules.util.Strings;

/**
 * <p>
 * Configuration options needed to flush one or more caches from EHCache.
 * </p>
 * 
 * @author Alex Ruiz
 */
public final class EhCacheFlushingModel extends AbstractFlushingModel {

  private static final long serialVersionUID = 7299844898815952890L;

  /**
   * Names of the caches to flush.
   */
  private String[] cacheNames;

  public EhCacheFlushingModel() {
    super();
  }

  public EhCacheFlushingModel(String csvCacheNames) {
    this();
    setCacheNames(csvCacheNames);
  }

  public EhCacheFlushingModel(String[] newCacheNames) {
    this();
    setCacheNames(newCacheNames);
  }

  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof EhCacheFlushingModel)) {
      return false;
    }
    EhCacheFlushingModel flushingModel = (EhCacheFlushingModel) obj;
    if (!Arrays.equals(cacheNames, flushingModel.cacheNames)) {
      return false;
    }
    return true;
  }

  public String[] getCacheNames() {
    return cacheNames;
  }

  public int hashCode() {
    int multiplier = 31;
    int hash = 7;
    hash = multiplier * hash + ArrayUtils.hashCode(cacheNames);
    return hash;
  }

  /**
   * Sets the names of the caches to flush.
   * 
   * @param csvCacheNames
   *          a comma-separated list of Strings containing the names of the
   *          caches to flush.
   */
  public void setCacheNames(String csvCacheNames) {
    String[] newCacheNames = null;
    if (csvCacheNames != null) {
      newCacheNames = StringUtils
          .commaDelimitedListToStringArray(csvCacheNames);
    }
    setCacheNames(newCacheNames);
  }

  public void setCacheNames(String[] newCacheNames) {
    cacheNames = Strings.removeDuplicates(newCacheNames);
  }

  public String toString() {
    StringBuffer buffer = new StringBuffer(getClass().getName());
    buffer.append("@" + System.identityHashCode(this) + "[");
    buffer.append("cacheNames=" + ArrayUtils.toString(cacheNames) + ", ");
    buffer.append("flushBeforeMethodExecution="
        + isFlushBeforeMethodExecution() + "]");
    return buffer.toString();
  }
}
