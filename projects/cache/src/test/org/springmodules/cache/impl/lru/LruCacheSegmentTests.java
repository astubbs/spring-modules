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

import org.springframework.util.ObjectUtils;

/**
 * Unit Tests for <code>{@link LruCacheSegment}</code>.
 * 
 * @author Alex Ruiz
 */
public class LruCacheSegmentTests extends TestCase {

  private static final Element ELEMENT_1 = new Element("key_1", "value_1");

  private static final Element ELEMENT_2 = new Element("key_2", "value_2");

  private static final Element ELEMENT_3 = new Element("key_3", "value_3");
  
  private static final int HASH = 10;

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
    Element element = ELEMENT_1;
    Serializable oldValue = null;
    assertPutInsertsElement(element, hash, oldValue);

    assertClearRemovesAllSegmentEntries();
  }

  public void testGetInsertsAccessedEntryBeforeHeaderInLruList() {
    int hash = HASH;
    Element element1 = ELEMENT_1;
    segment.put(element1, hash, cache);

    Element element2 = ELEMENT_2;
    segment.put(element2, hash, cache);

    Serializable actualValue = segment.get(element1.getKey(), hash, cache);
    assertEquals(element1.getValue(), actualValue);

    LruCacheEntry[] expectedEntries = {
        new LruCacheEntry(element1, hash, null),
        new LruCacheEntry(element2, hash, null) };
    assertLruListIsCorrect(expectedEntries);
  }

  public void testInitialCapacity() {
    assertEquals("<initial capacity>", 4, segment.size());
  }

  public void testPutInsertsNewEntryAsFirstInBucket() {
    int hash = HASH;
    Element element1 = ELEMENT_1;
    segment.put(element1, hash, cache);

    Element element2 = ELEMENT_2;
    segment.put(element2, hash, cache);

    Element[] expectedElementsInBucket = { element2, element1 };
    assertBucketEntriesAreCorrect(expectedElementsInBucket);
  }

  public void testPutInsertsNewEntryBeforeHeaderInLruList() {
    int hash = HASH;
    Element element1 = ELEMENT_1;
    segment.put(element1, hash, cache);

    Element element2 = ELEMENT_2;
    segment.put(element2, hash, cache);

    LruCacheEntry[] expectedEntries = {
        new LruCacheEntry(element2, hash, null),
        new LruCacheEntry(element1, hash, null) };
    assertLruListIsCorrect(expectedEntries);
  }

  public void testPutWithOneNewEntry() {
    int hash = HASH;
    Element element = ELEMENT_1;
    Serializable oldValue = null;
    assertPutInsertsElement(element, hash, oldValue);
  }

  public void testPutWithTwoNewEntriesHavingSameKeyAndDifferentValues() {
    int hash = HASH;
    Element element1 = ELEMENT_1;
    Serializable oldValue = null;
    assertPutInsertsElement(element1, hash, oldValue);

    Element element2 = new Element(element1.getKey(), "someOtherValue");
    oldValue = element1.getValue();
    assertPutReplacesElement(element2, hash, oldValue);
  }

  public void testPutWithTwoNewEntriesWithDifferentKeys() {
    int hash = HASH;
    Element element1 = ELEMENT_1;
    Serializable oldValue = null;
    assertPutInsertsElement(element1, hash, oldValue);

    Element element2 = ELEMENT_2;
    assertPutInsertsElement(element2, hash, oldValue);
  }

  public void testRemoveEntryInFirstPosition() {
    int hash = HASH;
    Element element1 = ELEMENT_1;
    segment.put(element1, hash, cache);

    Element element2 = ELEMENT_2;
    segment.put(element2, hash, cache);

    Element element3 = ELEMENT_3;
    segment.put(element3, hash, cache);

    LruCacheEntry[] expectedEntriesInLruList = {
        new LruCacheEntry(element2, hash, null),
        new LruCacheEntry(element1, hash, null) };

    Element[] expectedElementsInBucket = { element2, element1 };

    assertRemoveIsCorrect(element3, hash, expectedEntriesInLruList,
        expectedElementsInBucket);
  }

  public void testRemoveEntryInLastPosition() {
    int hash = HASH;
    Element element1 = ELEMENT_1;
    segment.put(element1, hash, cache);

    Element element2 = ELEMENT_2;
    segment.put(element2, hash, cache);

    Element element3 = ELEMENT_3;
    segment.put(element3, hash, cache);

    LruCacheEntry[] expectedEntriesInLruList = {
        new LruCacheEntry(element3, hash, null),
        new LruCacheEntry(element2, hash, null) };

    Element[] expectedElementsInBucket = { element3, element2 };

    assertRemoveIsCorrect(element1, hash, expectedEntriesInLruList,
        expectedElementsInBucket);
  }

  public void testRemoveEntryInMiddlePosition() {
    int hash = HASH;
    Element element1 = ELEMENT_1;
    segment.put(element1, hash, cache);

    Element element2 = ELEMENT_2;
    segment.put(element2, hash, cache);

    Element element3 = ELEMENT_3;
    segment.put(element3, hash, cache);

    LruCacheEntry[] expectedEntriesInLruList = {
        new LruCacheEntry(element3, hash, null),
        new LruCacheEntry(element1, hash, null) };

    Element[] expectedElementsInBucket = { element3, element1 };

    assertRemoveIsCorrect(element2, hash, expectedEntriesInLruList,
        expectedElementsInBucket);
  }

  public void testRemoveWithEmptySegment() {
    segment.remove(ELEMENT_1.getKey(), HASH);
    assertEmptySegment();
    assertEmptyCache();
  }

  public void testRemoveWithSegmentHavingOneEntry() {
    int hash = HASH;
    Element element = ELEMENT_1;
    Serializable oldValue = null;
    assertPutInsertsElement(element, hash, oldValue);

    assertRemoveIsCorrect(element, hash);
  }

  protected void setUp() {
    cache = new LruCache();
    segment = new LruCacheSegment(4, 2);
  }

  private void assertBucketEntriesAreCorrect(Element[] expectedElements) {
    int expectedEntryCount = expectedElements.length;

    LruCacheEntry[] actualEntries = getEntriesAsOrderedInBucket();
    assertEquals("<entry count>", expectedEntryCount, actualEntries.length);

    for (int i = 0; i < expectedEntryCount; i++) {
      assertSame(expectedElements[i], actualEntries[i].element);
    }
  }

  private void assertClearRemovesAllSegmentEntries() {
    segment.clear();
    assertEmptySegment();
    assertEmptyCache();
  }

  private void assertEmptyCache() {
    LruCacheEntry header = cache.header();
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
    LruCacheEntry header = cache.header();
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

  private void assertRemoveIsCorrect(Element elementToRemove, int hash) {
    assertRemoveIsCorrect(elementToRemove, hash, null, null);
  }

  private void assertRemoveIsCorrect(Element elementToRemove, int hash,
      LruCacheEntry[] expectedEntriesInLruList,
      Element[] expectedElementInBucket) {
    Serializable removedValue = segment.remove(elementToRemove.getKey(), hash);
    assertEquals("<removed value>", elementToRemove.getValue(), removedValue);

    // verify LRU linked list.
    if (ObjectUtils.isEmpty(expectedEntriesInLruList)) {
      assertEmptySegment();
    } else {
      assertLruListIsCorrect(expectedEntriesInLruList);
    }

    // verify bucket.
    if (ObjectUtils.isEmpty(expectedElementInBucket)) {
      assertEmptyCache();
    } else {
      assertBucketEntriesAreCorrect(expectedElementInBucket);
    }
  }

  private LruCacheEntry[] getEntriesAsOrderedInBucket() {
    List entryList = new ArrayList();

    LruCacheEntry current = cache.header().before;
    while (current != null) {
      entryList.add(current);
      current = current.next();
    }

    LruCacheEntry[] entries = (LruCacheEntry[]) entryList
        .toArray(new LruCacheEntry[entryList.size()]);
    return entries;
  }
}
