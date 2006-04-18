/* 
 * Created on Apr 14, 2006
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
 * Copyright @2006 the original author or authors.
 */
package org.springmodules.cache.impl.lru;

import org.springmodules.cache.impl.CacheEntry;
import org.springmodules.cache.impl.Element;

/**
 * TODO Describe this class
 * 
 * @author Alex Ruiz
 */
class LruCacheEntry extends CacheEntry {

  private static final int DEFAULT_HASH = -1;

  /**
   * Points to the next element in the linked list.
   */
  public LruCacheEntry after;

  /**
   * Points to the previous element in the linked list.
   */
  public LruCacheEntry before;

  /**
   * Constructor.
   */
  public LruCacheEntry() {
    super(null, DEFAULT_HASH);
  }

  /**
   * Constructor.
   * 
   * @param newElement
   *          the new cache element to store in this entry
   * @param newHash
   *          the new hash of this entry
   * @param newNext
   *          the next entry in this bucket
   */
  public LruCacheEntry(Element newElement, int newHash, LruCacheEntry newNext) {
    super(newElement, newHash, newNext);
  }

  /**
   * Adds this entry before the given entry in the linked list.
   * 
   * @param existingEntry
   *          the given entry to be the this entry's "next"
   */
  public void addBefore(LruCacheEntry existingEntry) {
    after = existingEntry;
    before = existingEntry.before;
    before.after = this;
    after.before = this;
  }

  public LruCacheEntry next() {
    return (LruCacheEntry) next;
  }

  public void recordAccess(LruCache cache) {
    remove();
    addBefore(cache.getHeader());
  }

  public void recordRemoval() {
    remove();
  }

  /**
   * Removes this entry from the linked list.
   */
  private void remove() {
    before.after = after;
    after.before = before;
  }
}