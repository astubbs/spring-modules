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
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.springmodules.cache.impl.Element;

/**
 * Unit Tests for <code>{@link LruCacheSegment}</code>.
 * 
 * @author Alex Ruiz
 */
public class LruCacheSegmentTests extends TestCase {

  private static final int HASH = 10;

  private static final String KEY = "key";

  private static final String VALUE = "value";

  private LruCache cache;

  private LruCacheSegment segment;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case
   */
  public LruCacheSegmentTests(String name) {
    super(name);
  }

  public void testClearWithEmptySegment() {
    assertClearRemovesAllSegmentEntries();
  }

  public void testClearWithSegmentHavingOneEntry() {
    int hash = HASH;
    Element element = defaultElement();
    Serializable oldValue = null;
    assertPutInsertsElement(element, hash, oldValue);

    assertClearRemovesAllSegmentEntries();
  }

  public void testGetInsertsAccessedEntryBeforeHeaderInLruList() {
    int hash = HASH;
    Element element1 = defaultElement();
    segment.put(element1, hash, cache);

    Element element2 = new Element("newKey", "newValue");
    segment.put(element2, hash, cache);

    Serializable actualValue = segment.get(element1.getKey(), hash, cache);
    assertEquals(element1.getValue(), actualValue);

    LruCacheEntry[] expectedEntries = {
        new LruCacheEntry(element1, hash, null),
        new LruCacheEntry(element2, hash, null) };
    assertLruListIsCorrect(expectedEntries);
  }

  public void testInitialCapacity() {
    assertEquals("<initial capacity>", 2, segment.getTable().length);
  }

  public void testPutInsertsNewEntryAsFirstInBucket() {
    int hash = HASH;
    Element element1 = defaultElement();
    segment.put(element1, hash, cache);

    Element element2 = new Element("newKey", "newValue");
    segment.put(element2, hash, cache);

    LruCacheEntry[] entriesInBucket = getEntriesAsOrderedInBucket();
    assertEquals("<entry count>", 2, entriesInBucket.length);

    assertSame(element2, entriesInBucket[0].element);
    assertSame(element1, entriesInBucket[1].element);

    assertSame(entriesInBucket[1], entriesInBucket[0].next);
    assertNull(entriesInBucket[1].next);
  }

  private LruCacheEntry[] getEntriesAsOrderedInBucket() {
    List entryList = new ArrayList();

    LruCacheEntry current = cache.getHeader().before;
    while (current != null) {
      entryList.add(current);
      current = current.next();
    }

    LruCacheEntry[] entries = (LruCacheEntry[]) entryList
        .toArray(new LruCacheEntry[entryList.size()]);
    return entries;
  }

  public void testPutInsertsNewEntryBeforeHeaderInLruList() {
    int hash = HASH;
    Element element1 = defaultElement();
    segment.put(element1, hash, cache);

    Element element2 = new Element("newKey", "newValue");
    segment.put(element2, hash, cache);

    LruCacheEntry[] expectedEntries = {
        new LruCacheEntry(element2, hash, null),
        new LruCacheEntry(element1, hash, null) };
    assertLruListIsCorrect(expectedEntries);
  }

  public void testPutWithOneNewEntry() {
    int hash = HASH;
    Element element = defaultElement();
    Serializable oldValue = null;
    assertPutInsertsElement(element, hash, oldValue);
  }

  public void testPutWithTwoNewEntriesHavingSameKeyAndDifferentValues() {
    int hash = HASH;
    Element element1 = defaultElement();
    Serializable oldValue = null;
    assertPutInsertsElement(element1, hash, oldValue);

    Element element2 = new Element(element1.getKey(), "newValue");
    oldValue = element1.getValue();
    assertPutReplacesElement(element2, hash, oldValue);
  }

  public void testPutWithTwoNewEntriesWithDifferentKeys() {
    int hash = HASH;
    Element element1 = defaultElement();
    Serializable oldValue = null;
    assertPutInsertsElement(element1, hash, oldValue);

    Element element2 = new Element("newKey", "newValue");
    assertPutInsertsElement(element2, hash, oldValue);
  }

  public void testRemoveWithEmptySegment() {
    segment.remove(KEY, HASH);
    assertEmptySegment();
    assertEmptyCache();
  }

  public void testRemoveEntryInMiddlePositionInBucket() {
    int hash = HASH;
    Element element1 = defaultElement();
    segment.put(element1, hash, cache);

    Element element2 = new Element("key2", "value2");
    segment.put(element2, hash, cache);

    Element element3 = new Element("key3", "value3");
    segment.put(element3, hash, cache);

    Serializable removedValue = segment.remove(element2.getKey(), hash);
    assertEquals("<removed value>", element2.getValue(), removedValue);
  }

  public void testRemoveWithSegmentHavingOneEntry() {
    int hash = HASH;
    Element element = defaultElement();
    Serializable oldValue = null;
    assertPutInsertsElement(element, hash, oldValue);

    Serializable removedValue = segment.remove(element.getKey(), hash);
    assertEquals("<removed value>", element.getValue(), removedValue);

    assertEmptySegment();
    assertEmptyCache();
  }

  protected void setUp() {
    cache = new LruCache();
    segment = new LruCacheSegment(2, 2);
  }

  private void assertClearRemovesAllSegmentEntries() {
    segment.clear();
    assertEmptySegment();
    assertEmptyCache();
  }

  private void assertEmptyCache() {
    LruCacheEntry header = cache.getHeader();
    assertSame("<header.after>", header, header.after);
    assertSame("<header.before>", header, header.before);
  }

  private void assertEmptySegment() {
    assertEntryCount(0);
  }

  private void assertEntryCount(int expected) {
    assertEquals("<entry count>", expected, segment.count());
  }

  private void assertLruListIsCorrect(LruCacheEntry[] expectedEntries) {
    LruCacheEntry header = cache.getHeader();
    LruCacheEntry actualEntry = header.before;

    int entryCount = expectedEntries.length;
    for (int i = 0; i < entryCount; i++) {
      Element expectedElement = expectedEntries[i].element;
      Element actualElement = actualEntry.element;

      assertEquals("<element>", expectedElement, actualElement);
      actualEntry = actualEntry.before;
    }
  }

  private void assertPutAddsGivenElement(Element element, int hash,
      Serializable expectedOldValue, boolean shouldCreateNewEntry) {
    int count = segment.count();

    Serializable actualOldValue = segment.put(element, hash, cache);
    assertEquals("<old value>", expectedOldValue, actualOldValue);

    if (shouldCreateNewEntry) {
      count++;
    }
    assertEntryCount(count);

    Serializable insertedValue = segment.get(element.getKey(), hash, cache);
    assertEquals("<entry value>", element.getValue(), insertedValue);
  }

  private void assertPutInsertsElement(Element element, int hash,
      Serializable expectedOldValue) {
    assertPutAddsGivenElement(element, hash, expectedOldValue, true);
  }

  private void assertPutReplacesElement(Element element, int hash,
      Serializable expectedOldValue) {
    assertPutAddsGivenElement(element, hash, expectedOldValue, false);
  }

  private Element defaultElement() {
    return new Element(KEY, VALUE);
  }
}
