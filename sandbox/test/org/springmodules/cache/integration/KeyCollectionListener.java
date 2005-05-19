/* 
 * Created on Nov 4, 2004
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

package org.springmodules.cache.integration;

import java.util.LinkedList;
import java.util.List;

import org.springmodules.cache.interceptor.caching.EntryStoredListener;

/**
 * <p>
 * Listener that stores in a collection the keys used to add entries to the
 * cache.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.3 $ $Date: 2005/05/19 02:20:01 $
 */
public class KeyCollectionListener implements EntryStoredListener {

  /**
   * Keys generated to add entries to the cache.
   */
  private List generatedKeys;

  /**
   * Constructor.
   */
  public KeyCollectionListener() {
    super();
    this.generatedKeys = new LinkedList();
  }

  /**
   * Getter for field <code>{@link #generatedKeys}</code>.
   * 
   * @return the field <code>generatedKeys</code>.
   */
  public final List getGeneratedKeys() {
    return this.generatedKeys;
  }

  /**
   * Adds the specified key to the list of keys.
   * 
   * @param key
   *          the key used to store the entry.
   * @param obj
   *          the object stored in the cache. Not used in this implementation.
   * 
   * @see EntryStoredListener#onEntryAdd(Object, Object)
   */
  public final void onEntryAdd(Object key, Object obj) {
    this.generatedKeys.add(key);
  }
}