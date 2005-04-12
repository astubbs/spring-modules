/* 
 * Created on Jan 19, 2005
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

package org.springmodules.cache.provider.oscache;

import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.springmodules.cache.provider.AbstractCacheProfileEditor;
import org.springmodules.cache.provider.CacheProfile;

import com.opensymphony.oscache.base.CacheEntry;

/**
 * <p>
 * Creates a new instance of <code>{@link OscacheCacheProfile}</code> by
 * parsing a String of the form
 * <code>[cronExpression=value][groups=value][refreshPeriod=value]</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/04/12 08:37:49 $
 */
public final class OscacheCacheProfileEditor extends AbstractCacheProfileEditor {

  /**
   * Constructor.
   */
  public OscacheCacheProfileEditor() {
    super();
  }

  /**
   * Creates a new instance of <code>{@link OscacheCacheProfile}</code> from
   * the specified set of properties.
   * 
   * @param properties
   *          the specified set of properties.
   * @return a new instance of <code>OscacheCacheProfile</code>.
   * 
   * @throws IllegalArgumentException
   *           if the value of the property "refreshPeriod" is not an integer or
   *           not equal to the String "INDEFINITE_EXPIRY".
   */
  protected CacheProfile createCacheProfile(Properties properties) {
    OscacheCacheProfile cacheProfile = null;

    if (properties.isEmpty()) {
      cacheProfile = new OscacheCacheProfile();
    } else {
      // set the cron expression.
      String cronExpression = properties.getProperty("cronExpression");

      // set the groups.
      String groups = properties.getProperty("groups");

      // set the refresh period.
      String refreshPeriodString = properties.getProperty("refreshPeriod");

      Integer refreshPeriod = null;

      if (StringUtils.isNotEmpty(refreshPeriodString)) {
        if (refreshPeriodString.equalsIgnoreCase("INDEFINITE_EXPIRY")) {
          refreshPeriod = new Integer(CacheEntry.INDEFINITE_EXPIRY);
        } else {
          try {
            refreshPeriod = new Integer(refreshPeriodString);
          } catch (NumberFormatException numberFormatException) {
            throw new IllegalArgumentException(
                "Refresh period should be an integer or the String 'INDEFINITE_EXPIRY'");
          } // end 'catch(..)'
        } // end 'else'
      } // end 'if (StringUtils.isNotEmpty(refreshPeriodString))'

      // create a new cache profile after validating the given properties.
      cacheProfile = new OscacheCacheProfile();
      cacheProfile.setCronExpression(cronExpression);
      cacheProfile.setGroups(groups);
      cacheProfile.setRefreshPeriod(refreshPeriod);
    }

    return cacheProfile;
  }

}