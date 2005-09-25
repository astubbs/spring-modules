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
import org.springmodules.util.ArrayUtils;
import org.springmodules.util.Strings;

/**
 * <p>
 * Configuration options needed to access OSCache.
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.13 $ $Date: 2005/09/25 05:22:16 $
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
    this(csvGroups, cronExpression);
    setRefreshPeriod(refreshPeriod);
  }

  public OsCacheProfile(String cvsGroups, String cronExpression) {
    this();
    setCronExpression(cronExpression);
    setGroups(cvsGroups);
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

    final OsCacheProfile profile = (OsCacheProfile) obj;

    if (cronExpression != null ? !cronExpression.equals(profile.cronExpression)
        : profile.cronExpression != null) {
      return false;
    }
    if (!Arrays.equals(groups, profile.groups)) {
      return false;
    }
    if (refreshPeriod != null ? !refreshPeriod.equals(profile.refreshPeriod)
        : profile.refreshPeriod != null) {
      return false;
    }

    return true;
  }

  public final String getCronExpression() {
    return cronExpression;
  }

  public final String[] getGroups() {
    return groups;
  }

  public final Integer getRefreshPeriod() {
    return refreshPeriod;
  }

  /**
   * @see Object#hashCode()
   */
  public int hashCode() {
    int multiplier = 31;
    int hash = 17;
    hash = multiplier * hash
        + (cronExpression != null ? cronExpression.hashCode() : 0);

    if (groups == null) {
      hash = multiplier * hash;
    } else {
      int groupCount = groups.length;
      for (int i = 0; i < groupCount; i++) {
        String group = groups[i];
        hash = multiplier * hash + (group != null ? group.hashCode() : 0);
      }
    }

    hash = multiplier * hash
        + (refreshPeriod != null ? refreshPeriod.hashCode() : 0);
    return hash;
  }

  public final void setCronExpression(String newCronExpression) {
    cronExpression = newCronExpression;
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

  public final void setGroups(String[] newGroups) {
    groups = newGroups;
  }

  public final void setRefreshPeriod(int newRefreshPeriod) {
    setRefreshPeriod(new Integer(newRefreshPeriod));
  }

  public final void setRefreshPeriod(Integer newRefreshPeriod) {
    refreshPeriod = newRefreshPeriod;
  }

  /**
   * @see Object#toString()
   */
  public String toString() {
    StringBuffer buffer = new StringBuffer(getClass().getName());
    buffer.append("@" + System.identityHashCode(this) + "[");
    buffer.append("refreshPeriod=" + refreshPeriod + ", ");
    buffer.append("groups=" + ArrayUtils.toString(groups) + ", ");
    buffer.append("cronExpression=" + Strings.quote(cronExpression) + "]");

    return buffer.toString();
  }
}