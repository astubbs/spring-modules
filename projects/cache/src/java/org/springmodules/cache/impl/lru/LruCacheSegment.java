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
public class LruCacheSegment extends ReentrantLock implements Serializable {

  /**
   * The maximum capacity.
   */
  private static final int MAXIMUM_CAPACITY = 1 << 30;

  private static final long serialVersionUID = 6068479672867699708L;

  /**
   * The number of elements in this segment's region..
   */
  private transient volatile int count;

  /**
   * The load factor for the hash table. Even though this value is same for all
   * segments, it is replicated to avoid needing links to outer object.
   */
  private final float loadFactor;

  /**
   * Number of updates that alter the size of the table. This is used during
   * bulk-read methods to make sure they see a consistent snapshot: If modCounts
   * change during a traversal of segments computing size or checking
   * containsValue, then we might have an inconsistent view of state so
   * (usually) must retry.
   */
  private transient int modCount;

  private transient volatile LruCacheEntry[] table;

  /**
   * The table is rehashed when its size exceeds this threshold. (The value of
   * this field is always <code>(int)(capacity * loadFactor)</code>.)
   */
  private transient int threshold;

  /**
   * Constructor.
   * 
   * @param initialCapacity
   *          the initial capacity for this segment
   * @param newLoadFactor
   *          the load factor for this segment
   */
  public LruCacheSegment(int initialCapacity, float newLoadFactor) {
    loadFactor = newLoadFactor;
    setTable(new LruCacheEntry[initialCapacity]);
  }

  /**
   * Removes the entries in this segment.
   */
  public void clear() {
    if (empty()) {
      return;
    }

    lock();
    try {
      LruCacheEntry[] tableRef = table;
      for (int i = 0; i < tableRef.length; i++) {
        if (tableRef[i] != null) {
          tableRef[i].recordRemoval();
          tableRef[i] = null;
        }
      }
      modCount++;
      count = 0;

    } finally {
      unlock();
    }
  }

  /**
   * Returns <code>true</code> if this segment contains a mapping for the
   * specified key.
   * 
   * @param key
   *          key whose presence in this segment is to be tested
   * @param hash
   *          the hash needed to locate the mapping containing the key
   * @return <code>true</code> if this segment contains a mapping for the
   *         specified key
   */
  public boolean containsKey(Serializable key, int hash) {
    if (empty()) {
      return false;
    }

    LruCacheEntry first = getFirst(hash);
    LruCacheEntry entry = find(key, hash, first);
    return entry != null;
  }

  public final int count() {
    return count;
  }

  /**
   * Returns the value to which this segment maps the specified key. Returns
   * <code>null</code> if the segment contains no mapping for this key. A
   * return value of <code>null</code> does not <i>necessarily</i> indicate
   * that the segment contains no mapping for the key; it's also possible that
   * the segment explicitly maps the key to <code>null</code> or the cache
   * entry has expired. The <code>containsKey</code> operation may be used to
   * distinguish these two cases.
   * 
   * @param key
   *          key whose associated value is to be returned
   * @param hash
   *          the hash needed to locate the mapping containing the key
   * @param cache
   *          the cache this segment belongs to
   * @return the value to which this segment maps the specified key, or
   *         <code>null</code> if this segment contains no mapping for this
   *         key
   */
  public Serializable get(Serializable key, int hash, LruCache cache) {
    if (empty()) {
      return null;
    }

    LruCacheEntry entry = getFirst(hash);
    while (entry != null) {
      if (entry.hash == hash && key.equals(entry.element.getKey())) {
        entry.recordAccess(cache);
        Serializable value = entry.element.getValue();
        return value;
      }
      entry = entry.next();
    }
    return null;
  }

  /**
   * Stores the given cache element in this segment. If the cache previously
   * contained a mapping for this key, the old value is replaced by the
   * specified value.
   * 
   * @param element
   *          the key/value mapping to store in this segment
   * @param hash
   *          the hash necessary to locate the bucket for the new entry
   * @param cache
   *          the cache this segment belongs to
   * @return previous value associated with specified key, or <code>null</code>
   *         if there was no mapping for key. A <code>null</code> return can
   *         also indicate that this segment previously associated
   *         <code>null</code> with the specified key
   */
  public Serializable put(Element element, int hash, LruCache cache) {
    lock();

    try {
      int countRef = count;
      if (countRef++ > threshold) {
        rehash();
      }

      LruCacheEntry[] tableRef = table;
      int firstIndex = firstEntryIndex(hash, tableRef);
      LruCacheEntry first = tableRef[firstIndex];

      LruCacheEntry entry = find(element.getKey(), hash, first);
      if (entry != null) {
        Serializable oldValue = entry.element.getValue();
        entry.element = element;
        return oldValue;
      }

      modCount++;

      LruCacheEntry newEntry = new LruCacheEntry(element, hash, first);
      newEntry.addBefore(cache.getHeader());
      tableRef[firstIndex] = newEntry;

      count = countRef;
      return null;

    } finally {
      unlock();
    }
  }

  /**
   * Removes the mapping for this key from this cache if it is present.
   * 
   * @param key
   *          key whose mapping is to be removed from the cache
   * @param hash
   *          the hash needed to locate the mapping containing the key
   * @return previous value associated with specified key, or <code>null</code>
   *         if there was no mapping for key
   */
  public Serializable remove(Serializable key, int hash) {
    lock();
    try {
      int newCount = count - 1;

      LruCacheEntry[] tableRef = table;
      int firstEntryIndex = firstEntryIndex(hash, tableRef);
      LruCacheEntry first = tableRef[firstEntryIndex];

      LruCacheEntry entryToRemove = find(key, hash, first);
      if (entryToRemove == null) {
        return null;
      }

      Serializable entryValue = entryToRemove.element.getValue();
      Serializable oldValue = entryValue;
      modCount++;

      // remove entry from bucket
      if (first == entryToRemove) {
        tableRef[firstEntryIndex] = entryToRemove.next();
      } else {
        LruCacheEntry prev = first;
        while (prev != null) {
          if (prev.next == entryToRemove) {
            prev.next = entryToRemove.next;
            break;
          }
          prev = prev.next();
        }
      }
      
      // remove entry from LRU linked list
      entryToRemove.next = null;
      entryToRemove.recordRemoval();

      count = newCount;
      return oldValue;

    } finally {
      unlock();
    }
  }

  protected int getTableSize() {
    return table.length;
  }

  private int calculateThreshold(LruCacheEntry[] tableRef) {
    return (int) (tableRef.length * loadFactor);
  }

  private boolean empty() {
    return count == 0;
  }

  private LruCacheEntry find(Serializable key, int hash, LruCacheEntry first) {
    LruCacheEntry entry = first;
    while (entry != null
        && (entry.hash != hash || !key.equals(entry.element.getKey()))) {
      entry = entry.next();
    }
    return entry;
  }

  private int firstEntryIndex(int hash, LruCacheEntry[] tableRef) {
    return hash & (tableRef.length - 1);
  }

  /**
   * @return first entry of bin for given hash
   */
  private LruCacheEntry getFirst(int hash) {
    LruCacheEntry[] tableRef = table;
    return tableRef[firstEntryIndex(hash, tableRef)];
  }

  private void rehash() {
    LruCacheEntry[] oldTable = table;
    int oldCapacity = oldTable.length;
    if (oldCapacity >= MAXIMUM_CAPACITY)
      return;

    LruCacheEntry[] newTable = new LruCacheEntry[oldCapacity << 1];
    threshold = calculateThreshold(newTable);

    int sizeMask = newTable.length - 1;
    for (int i = 0; i < oldCapacity; i++) {
      // We need to guarantee that any existing reads of old Map can proceed. So
      // we cannot yet null out each bin.
      LruCacheEntry entry = oldTable[i];

      if (entry == null) {
        continue;
      }

      // LruCacheEntry next = entry.next();
      int idx = entry.hash & sizeMask;
      newTable[idx] = entry;

      // // Single node on list
      // if (next == null) {
      // newTable[idx] = entry;
      // continue;
      // }
      //
      // // Reuse trailing consecutive sequence at same slot
      // LruCacheEntry lastRun = entry;
      // int lastIdx = idx;
      // for (LruCacheEntry last = next; last != null; last = last.next()) {
      // int k = last.hash & sizeMask;
      // if (k != lastIdx) {
      // lastIdx = k;
      // lastRun = last;
      // }
      // }
      // newTable[lastIdx] = lastRun;
      //
      // // Clone all remaining nodes
      // for (HashEntry<K, V> p = entry; p != lastRun; p = p.next) {
      // int k = p.hash & sizeMask;
      // HashEntry<K, V> n = (HashEntry<K, V>) newTable[k];
      // newTable[k] = new HashEntry<K, V>(p.key, p.hash, n, p.value);
      // }
    }
    table = newTable;
  }

  /**
   * Sets the table of entries. Called only while holding lock or in
   * constructor.
   * 
   * @param newTable
   *          the new entry table for this segment
   */
  private void setTable(LruCacheEntry[] newTable) {
    threshold = calculateThreshold(newTable);
    table = newTable;
  }
}