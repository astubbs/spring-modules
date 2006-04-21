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

import java.io.Serializable;

import org.springmodules.cache.impl.Element;

import edu.emory.mathcs.backport.java.util.concurrent.locks.ReentrantLock;

/**
 * Thread-safe version of a hash table.
 * 
 * @author Alex Ruiz
 */
final class LruCacheSegment extends ReentrantLock implements Serializable {

  private static final int MAXIMUM_CAPACITY = 1 << 30;

  private static final long serialVersionUID = 6068479672867699708L;

  /**
   * The number of elements in this segment's region..
   */
  private transient volatile int count;

  private final float loadFactor;

  /**
   * Number of updates that alter the size of the table. This is used during
   * bulk-read methods to make sure they see a consistent snapshot: If
   * <code>modCount</code> change during a traversal of segments computing
   * size or checking containsValue, then we might have an inconsistent view of
   * state so (usually) must retry.
   */
  private transient int modCount;

  private transient volatile LruCacheEntry[] table;

  /**
   * The table is rehashed when its size exceeds this threshold. (The value of
   * this field is always <code>(int)(capacity * loadFactor)</code>.)
   */
  private transient int threshold;

  LruCacheSegment(int initialCapacity, float newLoadFactor) {
    loadFactor = newLoadFactor;
    setTable(new LruCacheEntry[initialCapacity]);
  }

  void clear() {
    if (empty()) return;

    lock();
    try {
      LruCacheEntry[] tableRef = table;
      for (int i = 0; i < tableRef.length; i++) {
        if (tableRef[i] == null) continue;
        tableRef[i].recordRemoval();
        tableRef[i] = null;
      }
      segmentModified();
      count = 0;

    } finally {
      unlock();
    }
  }

  boolean containsKey(Serializable key, int hash) {
    if (empty()) return false;
    LruCacheEntry entry = find(key, hash, first(hash));
    return entry != null;
  }

  int count() {
    return count;
  }

  Serializable get(Serializable key, int hash, LruCache cache) {
    if (empty()) return null;

    for (LruCacheEntry entry = first(hash); entry != null; entry = entry.next()) {
      if (entry.matches(key, hash)) {
        entry.recordAccess(cache);
        return entry.element.getValue();
      }
    }
    return null;
  }

  Serializable put(Element element, int hash, LruCache cache) {
    lock();

    try {
      int countRef = count;
      if (countRef++ > threshold) rehash();

      LruCacheEntry first = first(hash);
      LruCacheEntry entry = find(element, hash, first);

      if (entry != null) return entry.replace(element);

      putNew(new LruCacheEntry(element, hash, first), cache);
      count = countRef;
      return null;

    } finally {
      unlock();
    }
  }

  Serializable remove(Serializable key, int hash) {
    lock();
    try {
      int countAfterRemoving = count - 1;

      LruCacheEntry[] tableRef = table;
      int firstEntryIndex = firstEntryIndex(hash, tableRef);
      LruCacheEntry first = tableRef[firstEntryIndex];

      LruCacheEntry toRemove = find(key, hash, first);
      if (toRemove == null) return null;

      Serializable oldValue = toRemove.element.getValue();
      segmentModified();

      // remove entry from bucket
      if (first == toRemove) {
        tableRef[firstEntryIndex] = toRemove.next();
      } else {
        LruCacheEntry previous = previousInBucket(first, toRemove);
        if (previous != null) {
          previous.next = toRemove.next;
        }
      }

      // remove entry from LRU linked list
      toRemove.next = null;
      toRemove.recordRemoval();

      count = countAfterRemoving;
      return oldValue;

    } finally {
      unlock();
    }
  }

  int size() {
    return table.length;
  }

  private void addToTable(LruCacheEntry e) {
    LruCacheEntry[] t = table;
    int i = firstEntryIndex(e.hash, t);
    t[i] = e;
  }

  private boolean empty() {
    return count == 0;
  }

  private LruCacheEntry find(Element element, int hash, LruCacheEntry start) {
    return find(element.getKey(), hash, start);
  }

  private LruCacheEntry find(Serializable key, int hash, LruCacheEntry start) {
    LruCacheEntry entry = start;
    while (entry != null && !entry.matches(key, hash))
      entry = entry.next();

    return entry;
  }

  /**
   * @return first entry of bin for given hash
   */
  private LruCacheEntry first(int hash) {
    return table[firstEntryIndex(hash, table)];
  }

  private int firstEntryIndex(int hash, LruCacheEntry[] tableRef) {
    return hash & (tableRef.length - 1);
  }

  private LruCacheEntry previousInBucket(LruCacheEntry first,
      LruCacheEntry target) {
    for (LruCacheEntry prev = first; prev != null; prev = prev.next()) {
      if (prev.next() == target) return prev;
    }
    return null;
  }

  private void putNew(LruCacheEntry e, LruCache cache) {
    segmentModified();
    e.addBefore(cache.header());
    addToTable(e);
  }

  private void rehash() {
    LruCacheEntry[] oldTable = table;
    int oldCapacity = oldTable.length;
    if (oldCapacity >= MAXIMUM_CAPACITY) return;

    LruCacheEntry[] newTable = new LruCacheEntry[oldCapacity << 1];
    threshold = threshold(newTable);

    int sizeMask = newTable.length - 1;
    for (int i = 0; i < oldCapacity; i++) {
      LruCacheEntry entry = oldTable[i];
      if (entry == null) continue;
      int idx = entry.hash & sizeMask;
      newTable[idx] = entry;
    }
    table = newTable;
  }

  private void segmentModified() {
    modCount++;
  }

  /**
   * Sets the table of entries. Called only while holding lock or in
   * constructor.
   * 
   * @param newTable
   *          the new entry table for this segment
   */
  private void setTable(LruCacheEntry[] newTable) {
    threshold = threshold(newTable);
    table = newTable;
  }

  private int threshold(LruCacheEntry[] tableRef) {
    return (int) (tableRef.length * loadFactor);
  }
}