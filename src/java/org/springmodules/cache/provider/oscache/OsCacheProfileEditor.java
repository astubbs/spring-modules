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

import org.springframework.util.StringUtils;
import org.springmodules.cache.provider.AbstractCacheProfileEditor;
import org.springmodules.cache.provider.CacheProfile;

import com.opensymphony.oscache.base.CacheEntry;

/**
 * <p>
 * Creates a new instance of <code>{@link OsCacheProfile}</code> by parsing a
 * String of the form
 * <code>[cronExpression=value][groups=value][refreshPeriod=value]</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.4 $ $Date: 2005/08/11 04:29:07 $
 */
public final class OsCacheProfileEditor extends AbstractCacheProfileEditor {

  private static final String INDEFINITE_EXPIRY = "INDEFINITE_EXPIRY";

  public OsCacheProfileEditor() {
    super();
  }

  /**
   * Creates a new instance of <code>{@link OsCacheProfile}</code> from the
   * specified set of properties.
   * 
   * @param properties
   *          the specified set of properties.
   * @return a new instance of <code>OsCacheProfile</code>.
   * 
   * @throws IllegalArgumentException
   *           if the value of the property "refreshPeriod" is not an integer or
   *           not equal to the String "INDEFINITE_EXPIRY".
   */
  protected CacheProfile createCacheProfile(Properties properties) {
    OsCacheProfile cacheProfile = null;

    if (properties.isEmpty()) {
      cacheProfile = new OsCacheProfile();

    } else {
      String cronExpression = properties.getProperty("cronExpression");

      String groups = properties.getProperty("groups");

      String refreshPeriodAsText = properties.getProperty("refreshPeriod");
      Integer refreshPeriod = null;

      if (INDEFINITE_EXPIRY.equalsIgnoreCase(refreshPeriodAsText)) {
        refreshPeriod = new Integer(CacheEntry.INDEFINITE_EXPIRY);

      } else if (StringUtils.hasText(refreshPeriodAsText)) {
        try {
          refreshPeriod = new Integer(refreshPeriodAsText);

        } catch (NumberFormatException numberFormatException) {
          throw new IllegalArgumentException(
              "Refresh period should be an integer or the String '"
                  + INDEFINITE_EXPIRY + "'");
        }
      }

      cacheProfile = new OsCacheProfile(groups, refreshPeriod, cronExpression);
    }

    return cacheProfile;
  }

}