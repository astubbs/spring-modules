/* 
 * Created on Oct 28, 2005
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
package org.springmodules.cache.provider.jcs;

import org.springframework.util.ObjectUtils;

import org.springmodules.AbstractEqualsHashCodeTestCase;
import org.springmodules.cache.provider.jcs.JcsFlushingModel.CacheStruct;

/**
 * <p>
 * Unit Tests for <code>{@link JcsFlushingModel.CacheStruct}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class CacheStructTests extends AbstractEqualsHashCodeTestCase {

  private CacheStruct struct;

  public CacheStructTests(String name) {
    super(name);
  }

  /**
   * @see org.springmodules.EqualsHashCodeTestCase#testEqualsHashCodeRelationship()
   */
  public void testEqualsHashCodeRelationship() {
    String cacheName = "rebels";
    String[] groups = { "endor", "hoth" };
    struct.setCacheName(cacheName);
    struct.setGroups(groups);

    CacheStruct struct2 = new CacheStruct(cacheName, groups);
    assertEqualsHashCodeRelationshipIsCorrect(struct, struct2);

    cacheName = null;
    groups = new String[0];
    struct.setCacheName(cacheName);
    struct.setGroups(groups);

    struct2.setCacheName(cacheName);
    struct2.setGroups(groups);
    assertEqualsHashCodeRelationshipIsCorrect(struct, struct2);
  }

  /**
   * @see org.springmodules.EqualsHashCodeTestCase#testEqualsIsConsistent()
   */
  public void testEqualsIsConsistent() {
    String cacheName = "empire";
    String groups = "deathstar,stardestroyer";

    struct.setCacheName(cacheName);
    struct.setGroups(groups);

    CacheStruct struct2 = new CacheStruct(cacheName, groups);
    assertEquals(struct, struct2);

    struct2.setGroups((String) null);
    assertFalse(struct.equals(struct2));

    struct2.setCacheName(null);
    struct2.setGroups(groups);
    assertFalse(struct.equals(struct2));
  }

  /**
   * @see org.springmodules.EqualsHashCodeTestCase#testEqualsIsReflexive()
   */
  public void testEqualsIsReflexive() {
    assertEqualsIsReflexive(struct);
  }

  /**
   * @see org.springmodules.EqualsHashCodeTestCase#testEqualsIsSymmetric()
   */
  public void testEqualsIsSymmetric() {
    String cacheName = "empire";
    String groups = "deathstar,stardestroyer";

    struct.setCacheName(cacheName);
    struct.setGroups(groups);

    CacheStruct struct2 = new CacheStruct(cacheName, groups);
    assertEqualsIsSymmetric(struct, struct2);
  }

  /**
   * @see org.springmodules.EqualsHashCodeTestCase#testEqualsIsTransitive()
   */
  public void testEqualsIsTransitive() {
    String cacheName = "empire";
    String groups = "deathstar,stardestroyer";

    struct.setCacheName(cacheName);
    struct.setGroups(groups);

    CacheStruct struct2 = new CacheStruct(cacheName, groups);
    CacheStruct struct3 = new CacheStruct(cacheName, groups);

    assertEqualsIsTransitive(struct, struct2, struct3);
  }

  /**
   * @see org.springmodules.EqualsHashCodeTestCase#testEqualsNullComparison()
   */
  public void testEqualsNullComparison() {
    assertEqualsNullComparisonReturnsFalse(struct);
  }

  public void testToStringWithCacheNamesEqualToNull() {
    struct.setCacheName("ewok");
    struct.setGroups((String[]) null);

    String actual = struct.getClass().getName() + "@"
        + ObjectUtils.getIdentityHexString(struct) + "[cacheName='ewok', groups=null]";
    assertEquals(struct.toString(), actual);
  }

  public void testToStringWithEmptyCacheNames() {
    struct.setCacheName("jabba");
    struct.setGroups(new String[0]);

    String actual = struct.getClass().getName() + "@"
        + ObjectUtils.getIdentityHexString(struct) + "[cacheName='jabba', groups={}]";
    assertEquals(struct.toString(), actual);
  }

  public void testToStringWithNotEmptyCacheNames() {
    struct.setCacheName("androids");
    struct.setGroups(new String[] { "c3p0" });

    String actual = struct.getClass().getName() + "@"
        + ObjectUtils.getIdentityHexString(struct)
        + "[cacheName='androids', groups={'c3p0'}]";
    assertEquals(struct.toString(), actual);
  }

  protected void setUp() {
    struct = new CacheStruct();
  }

}
