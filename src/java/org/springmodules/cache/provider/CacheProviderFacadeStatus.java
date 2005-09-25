/* 
 * Created on Sep 20, 2005
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

import org.springmodules.util.Strings;

/**
 * <p>
 * Enum containing the possible states of a cache provider.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public final class CacheProviderFacadeStatus {

  public static final CacheProviderFacadeStatus INVALID = new CacheProviderFacadeStatus("Invalid");

  public static final CacheProviderFacadeStatus READY = new CacheProviderFacadeStatus("Ready");

  public static final CacheProviderFacadeStatus UNINITIALIZED = new CacheProviderFacadeStatus(
      "Uninitialized");

  private String description;

  private CacheProviderFacadeStatus(String newDescription) {
    super();
    description = newDescription;
  }

  public String toString() {
    StringBuffer buffer = new StringBuffer(getClass().getName());
    buffer.append("@" + System.identityHashCode(this) + "[");
    buffer.append("description=" + Strings.quote(description) + "]");

    return buffer.toString();
  }

}
