/* 
 * Created on Mar 6, 2005
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
package org.springmodules.cache;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * <p>
 * Template for cache entries that adds support for time expiration.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/04/12 08:37:56 $
 */
public abstract class AbstractCacheEntry implements Serializable {

  /**
   * Specifying this as the refresh period ensures an entry does not become
   * stale until it is either explicitly flushed.
   */
  public static final int INDEFINITE_EXPIRY = -1;

  /**
   * Default initialization value for <code>{@link #lastUpdate}</code>.
   */
  private static final byte NOT_YET = -1;

  /**
   * The object to store in the cache.
   */
  private Object content;

  /**
   * The time this entry was last updated.
   */
  private long lastUpdate;

  /**
   * Period of refresh (in seconds). Setting it to {@link #INDEFINITE_EXPIRY}
   * will result in the content never becoming stale unless it is explicitly
   * flushed. Setting it to 0 will always result in a refresh being required.
   */
  private int refreshPeriod;

  /**
   * Constructor.
   */
  public AbstractCacheEntry() {
    super();
    this.setLastUpdate(NOT_YET);
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
    boolean equals = true;

    if (null == obj || !(obj instanceof AbstractCacheEntry)) {
      equals = false;
    } else if (this != obj) {
      AbstractCacheEntry entry = (AbstractCacheEntry) obj;

      EqualsBuilder equalsBuilder = new EqualsBuilder();
      equalsBuilder.append(this.getContent(), entry.getContent());
      equalsBuilder.append(this.getLastUpdate(), entry.getLastUpdate());
      equalsBuilder.append(this.getRefreshPeriod(), entry.getRefreshPeriod());

      equals = equalsBuilder.isEquals();
    }

    return equals;
  }

  /**
   * Getter for field <code>{@link #content}</code>.
   * 
   * @return the field <code>content</code>.
   */
  public final Object getContent() {
    return this.content;
  }

  /**
   * Getter for field <code>{@link #lastUpdate}</code>.
   * 
   * @return the field <code>lastUpdate</code>.
   */
  public final long getLastUpdate() {
    return this.lastUpdate;
  }

  /**
   * Getter for field <code>{@link #refreshPeriod}</code>.
   * 
   * @return the field <code>refreshPeriod</code>.
   */
  public final int getRefreshPeriod() {
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
    HashCodeBuilder hashCodeBuilder = new HashCodeBuilder(5, 11);
    hashCodeBuilder.append(this.getContent());
    hashCodeBuilder.append(this.getLastUpdate());
    hashCodeBuilder.append(this.getRefreshPeriod());

    int hashCode = hashCodeBuilder.toHashCode();
    return hashCode;
  }

  /**
   * Checks if this entry needs to be refreshed.
   * 
   * @return <code>true</code> if this entry needs to be refreshed,
   *         <code>false</code> otherwise.
   */
  public final boolean needsRefresh() {
    boolean stale;

    if (this.lastUpdate == NOT_YET) {
      stale = true;
    } else if (this.refreshPeriod == INDEFINITE_EXPIRY) {
      stale = false;
    } else if (this.refreshPeriod == 0) {
      stale = true;
    } else {
      long currentTimeMillis = System.currentTimeMillis();
      long currentLiveTime = this.lastUpdate + (this.refreshPeriod * 1000L);

      if (this.refreshPeriod > 0 && currentTimeMillis >= currentLiveTime) {
        stale = true;
      } else {
        stale = false;
      }
    }

    return stale;
  }

  /**
   * Setter for the field <code>{@link #content}</code>.
   * 
   * @param content
   *          the new value to set
   */
  public final void setContent(Object content) {
    this.content = content;
    this.setLastUpdate(System.currentTimeMillis());
  }

  /**
   * Setter for the field <code>{@link #lastUpdate}</code>.
   * 
   * @param lastUpdate
   *          the new value to set
   */
  public final void setLastUpdate(long lastUpdate) {
    this.lastUpdate = lastUpdate;
  }

  /**
   * Setter for the field <code>{@link #refreshPeriod}</code>.
   * 
   * @param refreshPeriod
   *          the new value to set
   */
  public final void setRefreshPeriod(int refreshPeriod) {
    this.refreshPeriod = refreshPeriod;
  }

}
