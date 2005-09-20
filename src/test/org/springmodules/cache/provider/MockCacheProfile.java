/* 
 * Created on Sep 9, 2005
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
package org.springmodules.cache.provider;

import org.springmodules.cache.util.Strings;

/**
 * <p>
 * Mock cache profile.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public class MockCacheProfile implements CacheProfile {

  private static final long serialVersionUID = -3403707289414100362L;

  private String cacheName;

  private String group;

  public MockCacheProfile() {
    super();
  }

  public MockCacheProfile(String newCacheName) {
    this();
    setCacheName(newCacheName);
  }

  public MockCacheProfile(String newCacheName, String newGroup) {
    this(newCacheName);
    setGroup(newGroup);
  }

  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (!(obj instanceof MockCacheProfile)) {
      return false;
    }

    final MockCacheProfile profile = (MockCacheProfile) obj;

    if (cacheName != null ? !cacheName.equals(profile.cacheName)
        : profile.cacheName != null) {
      return false;
    }

    if (group != null ? !group.equals(profile.group) : group != null) {
      return false;
    }

    return true;
  }

  public final String getCacheName() {
    return cacheName;
  }

  public int hashCode() {
    int multiplier = 31;
    int hash = 17;

    hash = multiplier * hash + (cacheName != null ? cacheName.hashCode() : 0);
    hash = multiplier * hash + (group != null ? group.hashCode() : 0);

    return hash;
  }

  public final void setCacheName(String newCacheName) {
    cacheName = newCacheName;
  }

  public final String getGroup() {
    return group;
  }

  public final void setGroup(String newGroup) {
    group = newGroup;
  }

  public String toString() {
    StringBuffer buffer = new StringBuffer(getClass().getName());
    buffer.append("@" + System.identityHashCode(this) + "[");
    buffer.append("cacheName=" + Strings.quote(cacheName) + ", ");
    buffer.append("group=" + Strings.quote(group) + ", ");

    return buffer.toString();
  }
}
