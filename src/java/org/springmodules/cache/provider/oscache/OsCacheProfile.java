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

/**
 * <p>
 * Set of configuration options needed for:
 * <ul>
 * <li>Retrieving an entry from a OSCache cache</li>
 * <li>Storing an object in a OSCache cache</li>
 * <li>Flushing one or more cache groups</li>
 * </ul>
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.2 $ $Date: 2005/06/25 06:53:20 $
 */
public class OsCacheProfile implements CacheProfile {

  /**
   * Version number of this class.
   * 
   * @see java.io.Serializable
   */
  private static final long serialVersionUID = 3904681574367770928L;

  /**
   * A cron expression that the age of the cache entry will be compared to. If
   * the entry is older than the most recent match for the cron expression, the
   * entry will be considered stale.
   */
  private String cronExpression;

  /**
   * The groups that the object to cache belongs to.
   */
  private String[] groups;

  /**
   * How long the object can stay in cache in seconds.
   */
  private Integer refreshPeriod;

  /**
   * Constructor.
   */
  public OsCacheProfile() {

    super();
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   * 
   * @param obj
   *          the reference object with which to compare.
   * @return <code>true</code> if this object is the same as the obj argument;
   *         <code>false</code> otherwise.
   * 
   * @see Object#equals(java.lang.Object)
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

  /**
   * Getter for field <code>{@link #cronExpression}</code>.
   * 
   * @return the field <code>cronExpression</code>.
   */
  public final String getCronExpression() {
    return this.cronExpression;
  }

  /**
   * Getter for field <code>{@link #groups}</code>.
   * 
   * @return the field <code>groups</code>.
   */
  public final String[] getGroups() {
    return this.groups;
  }

  /**
   * Getter for field <code>{@link #refreshPeriod}</code>.
   * 
   * @return the field <code>refreshPeriod</code>.
   */
  public final Integer getRefreshPeriod() {
    return this.refreshPeriod;
  }

  /**
   * Returns a hash code value for the object. This method is supported for the
   * benefit of hashtables such as those provided by
   * <code>java.util.Hashtable</code>.
   * 
   * @return a hash code value for this object.
   * 
   * @see Object#hashCode()
   */
  public int hashCode() {
    int result = (this.cronExpression != null ? this.cronExpression.hashCode()
        : 0);
    result = 29 * result
        + (this.refreshPeriod != null ? this.refreshPeriod.hashCode() : 0);
    return result;
  }

  /**
   * Setter for the field <code>{@link #cronExpression}</code>.
   * 
   * @param cronExpression
   *          the new value to set.
   */
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
    this.setGroups(newGroups);
  }

  /**
   * Setter for the field <code>{@link #groups}</code>.
   * 
   * @param groups
   *          the new value to set.
   */
  public final void setGroups(String[] groups) {
    this.groups = groups;
  }

  /**
   * Setter for the field <code>{@link #refreshPeriod}</code>.
   * 
   * @param refreshPeriod
   *          the new value to set.
   */
  public final void setRefreshPeriod(int refreshPeriod) {
    this.setRefreshPeriod(new Integer(refreshPeriod));
  }

  /**
   * Setter for the field <code>{@link #refreshPeriod}</code>.
   * 
   * @param refreshPeriod
   *          the new value to set.
   */
  public final void setRefreshPeriod(Integer refreshPeriod) {
    this.refreshPeriod = refreshPeriod;
  }

  /**
   * Returns a string representation of the object. In general, the
   * <code>toString</code> method returns a string that "textually represents"
   * this object.
   * 
   * @return a string representation of the object.
   * 
   * @see Object#toString()
   */
  public String toString() {
    return "OsCacheProfile: refreshPeriod=" + this.refreshPeriod + ", groups="
        + Arrays.toString(this.groups) + ", cronExpression='"
        + this.cronExpression + "'";
  }
}