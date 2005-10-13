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
package org.springmodules.cache.provider.jcs;

import java.io.Serializable;
import java.util.Arrays;

import org.springframework.util.StringUtils;
import org.springmodules.cache.provider.AbstractFlushingModel;
import org.springmodules.util.ArrayUtils;
import org.springmodules.util.Strings;

/**
 * <p>
 * Configuration options needed to flush one or more cache and/or groups from
 * JCS.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public final class JcsFlushingModel extends AbstractFlushingModel {

  public static class CacheStruct implements Serializable {

    private static final long serialVersionUID = -2168328935167938683L;

    private String cacheName;

    private String[] groups;

    public CacheStruct() {
      super();
    }

    public CacheStruct(String cacheName) {
      this();
      setCacheName(cacheName);
    }

    public CacheStruct(String cacheName, String csvGroups) {
      this(cacheName);
      setGroups(csvGroups);
    }

    public CacheStruct(String cacheName, String[] groups) {
      this(cacheName);
      setGroups(groups);
    }

    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (!(obj instanceof CacheStruct)) {
        return false;
      }
      CacheStruct cacheStruct = (CacheStruct) obj;
      if (cacheName != null ? !cacheName.equals(cacheStruct.cacheName)
          : cacheStruct.cacheName != null) {
        return false;
      }
      if (!Arrays.equals(groups, cacheStruct.groups)) {
        return false;
      }
      return true;
    }

    public String getCacheName() {
      return cacheName;
    }

    public String[] getGroups() {
      return groups;
    }

    public int hashCode() {
      int multiplier = 31;
      int hash = 7;
      hash = multiplier * hash + (cacheName != null ? cacheName.hashCode() : 0);
      hash = multiplier * hash + ArrayUtils.hashCode(groups);
      return hash;
    }

    public void setCacheName(String newCacheName) {
      cacheName = newCacheName;
    }

    public void setGroups(String csvGroups) {
      String[] newGroups = null;
      if (StringUtils.hasText(csvGroups)) {
        newGroups = StringUtils.commaDelimitedListToStringArray(csvGroups);
      }
      setGroups(newGroups);
    }

    public void setGroups(String[] newGroups) {
      groups = newGroups;
    }

    public String toString() {
      StringBuffer buffer = new StringBuffer(getClass().getName());
      buffer.append("@" + System.identityHashCode(this) + "[");
      buffer.append("cacheName=" + Strings.quote(cacheName) + ", ");
      buffer.append("groups=" + ArrayUtils.toString(groups));
      return buffer.toString();
    }
  }

  private static final long serialVersionUID = -1497138716500203888L;

  /**
   * Struct containing the names of the caches and/or groups to flush.
   */
  private CacheStruct[] cacheStructs;

  public JcsFlushingModel() {
    super();
  }

  public JcsFlushingModel(CacheStruct cacheStruct) {
    this();
    setCacheStruct(cacheStruct);
  }

  public JcsFlushingModel(String cacheName) {
    this(new CacheStruct(cacheName));
  }

  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof JcsFlushingModel)) {
      return false;
    }
    JcsFlushingModel flushingModel = (JcsFlushingModel) obj;
    if (!Arrays.equals(cacheStructs, flushingModel.cacheStructs)) {
      return false;
    }
    return true;
  }

  public CacheStruct[] getCacheStructs() {
    return cacheStructs;
  }

  public int hashCode() {
    int multiplier = 31;
    int hash = 7;
    hash = multiplier * hash + ArrayUtils.hashCode(cacheStructs);
    return hash;
  }

  public void setCacheStruct(CacheStruct cacheStruct) {
    setCacheStructs(new CacheStruct[] { cacheStruct });
  }

  public void setCacheStructs(CacheStruct[] newCacheStructs) {
    cacheStructs = newCacheStructs;
  }

  public String toString() {
    StringBuffer buffer = new StringBuffer(getClass().getName());
    buffer.append("@" + System.identityHashCode(this) + "[");
    buffer.append("cacheStructs=" + ArrayUtils.toString(cacheStructs) + ", ");
    buffer.append("flushBeforeMethodExecution="
        + isFlushBeforeMethodExecution() + "]");
    return buffer.toString();
  }

}
