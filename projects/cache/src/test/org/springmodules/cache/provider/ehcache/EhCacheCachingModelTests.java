/* 
 * Created on Oct 29, 2004
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

package org.springmodules.cache.provider.ehcache;

import org.springmodules.AbstractEqualsHashCodeTestCase;

/**
 * <p>
 * Unit Tests for <code>{@link EhCacheCachingModel}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public final class EhCacheCachingModelTests extends
    AbstractEqualsHashCodeTestCase {

  private EhCacheCachingModel model;

  public EhCacheCachingModelTests(String name) {
    super(name);
  }

  protected final void setUp() {
    model = new EhCacheCachingModel();
  }

  /**
   * @see org.springmodules.EqualsHashCodeTestCase#testEqualsHashCodeRelationship()
   */
  public void testEqualsHashCodeRelationship() {
    String cacheName = "main";
    model.setCacheName(cacheName);
    EhCacheCachingModel model2 = new EhCacheCachingModel(cacheName);

    assertEqualsHashCodeRelationshipIsCorrect(model, model2);

    cacheName = null;
    model.setCacheName(cacheName);
    model2.setCacheName(cacheName);

    assertEqualsHashCodeRelationshipIsCorrect(model, model2);
  }

  /**
   * @see org.springmodules.EqualsHashCodeTestCase#testEqualsIsConsistent()
   */
  public void testEqualsIsConsistent() {
    String cacheName = "test";
    model.setCacheName(cacheName);

    EhCacheCachingModel model2 = new EhCacheCachingModel(cacheName);
    assertEquals(model, model2);

    model2.setCacheName("main");
    assertFalse(model.equals(model2));
  }

  /**
   * @see org.springmodules.EqualsHashCodeTestCase#testEqualsIsReflexive()
   */
  public void testEqualsIsReflexive() {
    assertEqualsIsReflexive(model);
  }

  /**
   * @see org.springmodules.EqualsHashCodeTestCase#testEqualsIsSymmetric()
   */
  public void testEqualsIsSymmetric() {
    String cacheName = "test";
    model.setCacheName(cacheName);

    EhCacheCachingModel model2 = new EhCacheCachingModel(cacheName);
    assertEqualsIsSymmetric(model, model2);
  }

  /**
   * @see org.springmodules.EqualsHashCodeTestCase#testEqualsIsTransitive()
   */
  public void testEqualsIsTransitive() {
    String cacheName = "test";
    model.setCacheName(cacheName);

    EhCacheCachingModel model2 = new EhCacheCachingModel(cacheName);
    EhCacheCachingModel model3 = new EhCacheCachingModel(cacheName);

    assertEqualsIsTransitive(model, model2, model3);
  }

  /**
   * @see org.springmodules.EqualsHashCodeTestCase#testEqualsNullComparison()
   */
  public void testEqualsNullComparison() {
    assertEqualsNullComparisonReturnsFalse(model);
  }

  public void testToStringWithCacheNameEqualToNull() {
    model.setCacheName(null);
    String actual = model.getClass().getName() + "@"
        + System.identityHashCode(model) + "[cacheName=null]";
    assertEquals(model.toString(), actual);
  }

  public void testToStringWithCacheNameNotEqualToNull() {
    model.setCacheName("main");
    String actual = model.getClass().getName() + "@"
        + System.identityHashCode(model) + "[cacheName='main']";
    assertEquals(model.toString(), actual);
  }
}