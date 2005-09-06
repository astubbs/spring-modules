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

package org.springmodules.cache.provider.oscache;

import java.util.Arrays;

import org.springframework.util.StringUtils;
import org.springmodules.cache.provider.CacheProfile;
import org.springmodules.cache.util.ArrayUtils;

/**
 * <p>
 * Configuration options needed to access OSCache.
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.9 $ $Date: 2005/09/06 01:41:22 $
 */
public class OsCacheProfile implements CacheProfile {

  private static final long serialVersionUID = 3904681574367770928L;

  /**
   * A cron expression that the age of the cache entry will be compared to. If
   * the entry is older than the most recent match for the cron expression, the
   * entry will be considered stale.
   */
  private String cronExpression;

  /**
   * The group(s) that the object to cache belongs to.
   */
  private String[] groups;

  /**
   * How long the object can stay in cache in seconds.
   */
  private Integer refreshPeriod;

  public OsCacheProfile() {
    super();
  }

  public OsCacheProfile(String csvGroups, int refreshPeriod) {
    this();
    setGroups(csvGroups);
    setRefreshPeriod(refreshPeriod);
  }

  public OsCacheProfile(String csvGroups, int refreshPeriod,
      String cronExpression) {
    this(csvGroups, new Integer(refreshPeriod), cronExpression);
  }

  public OsCacheProfile(String csvGroups, Integer refreshPeriod,
      String cronExpression) {
    this();
    setCronExpression(cronExpression);
    setGroups(csvGroups);
    setRefreshPeriod(refreshPeriod);
  }

  /**
   * @see Object#equals(Object)
   */
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof OsCacheProfile)) {
      return false;
    }

    final OsCacheProfile osCacheProfile = (OsCacheProfile) obj;

    if (this.cronExpression != null ? !this.cronExpression
        .equals(osCacheProfile.cronExpression)
        : osCacheProfile.cronExpression != null) {
      return false;
    }
    if (!Arrays.equals(this.groups, osCacheProfile.groups)) {
      return false;
    }
    if (this.refreshPeriod != null ? !this.refreshPeriod
        .equals(osCacheProfile.refreshPeriod)
        : osCacheProfile.refreshPeriod != null) {
      return false;
    }

    return true;
  }

  public final String getCronExpression() {
    return this.cronExpression;
  }

  public final String[] getGroups() {
    return this.groups;
  }

  public final Integer getRefreshPeriod() {
    return this.refreshPeriod;
  }

  /**
   * @see Object#hashCode()
   */
  public int hashCode() {
    int multiplier = 31;
    int hash = 7;
    hash = multiplier * hash
        + (this.cronExpression != null ? this.cronExpression.hashCode() : 0);

    if (this.groups == null) {
      hash = multiplier * hash;
    } else {
      int groupCount = this.groups.length;
      for (int i = 0; i < groupCount; i++) {
        String group = this.groups[i];
        hash = multiplier * hash + (group != null ? group.hashCode() : 0);
      }
    }

    hash = multiplier * hash
        + (this.refreshPeriod != null ? this.refreshPeriod.hashCode() : 0);
    return hash;
  }

  public final void setCronExpression(String cronExpression) {
    this.cronExpression = cronExpression;
  }

  /**
   * Sets the cache groups from a comma-delimited list of values.
   * 
   * @param csvGroups
   *          the comma-delimited list of values containing the cache groups.
   */
  public final void setGroups(String csvGroups) {
    String[] newGroups = null;
    if (StringUtils.hasText(csvGroups)) {
      newGroups = StringUtils.commaDelimitedListToStringArray(csvGroups);
    }
    setGroups(newGroups);
  }

  public final void setGroups(String[] groups) {
    this.groups = groups;
  }

  public final void setRefreshPeriod(int refreshPeriod) {
    this.setRefreshPeriod(new Integer(refreshPeriod));
  }

  public final void setRefreshPeriod(Integer refreshPeriod) {
    this.refreshPeriod = refreshPeriod;
  }

  /**
   * @see Object#toString()
   */
  public String toString() {
    StringBuffer buffer = new StringBuffer(getClass().getName());
    buffer.append("@" + System.identityHashCode(this) + "[");
    buffer.append("refreshPeriod=" + this.refreshPeriod + ", ");
    buffer.append("groups=" + ArrayUtils.toString(this.groups) + ", ");
    buffer.append("cronExpression=");
    String formattedCronExpression = null;
    if (this.cronExpression != null) {
      formattedCronExpression = "'" + this.cronExpression + "'";
    }
    buffer.append(formattedCronExpression + "]");

    return buffer.toString();
  }
}