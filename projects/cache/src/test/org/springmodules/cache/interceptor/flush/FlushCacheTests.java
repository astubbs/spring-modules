/* 
 * Created on Jan 17, 2005
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

package org.springmodules.cache.interceptor.flush;

import org.springframework.util.ObjectUtils;

import org.springmodules.AbstractEqualsHashCodeTestCase;

/**
 * <p>
 * Unit Tests for <code>{@link FlushCache}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public final class FlushCacheTests extends AbstractEqualsHashCodeTestCase {

  private FlushCache attribute;

  public FlushCacheTests(String name) {
    super(name);
  }

  /**
   * @see org.springmodules.EqualsHashCodeTestCase#testEqualsHashCodeRelationship()
   */
  public void testEqualsHashCodeRelationship() {
    String modelId = "main";
    attribute.setModelId(modelId);

    FlushCache attribute2 = new FlushCache(modelId);

    assertEqualsHashCodeRelationshipIsCorrect(attribute, attribute2);

    attribute.setModelId(null);
    attribute2.setModelId(null);

    assertEqualsHashCodeRelationshipIsCorrect(attribute, attribute2);
  }

  /**
   * @see org.springmodules.EqualsHashCodeTestCase#testEqualsIsConsistent()
   */
  public void testEqualsIsConsistent() {
    String modelId = "test";
    attribute.setModelId(modelId);

    FlushCache attribute2 = new FlushCache(modelId);
    assertEquals(attribute, attribute2);

    attribute2.setModelId("main");
    assertFalse(attribute.equals(attribute2));
  }

  /**
   * @see org.springmodules.EqualsHashCodeTestCase#testEqualsIsReflexive()
   */
  public void testEqualsIsReflexive() {
    assertEqualsIsReflexive(attribute);
  }

  /**
   * @see org.springmodules.EqualsHashCodeTestCase#testEqualsIsSymmetric()
   */
  public void testEqualsIsSymmetric() {
    String modelId = "service";
    attribute.setModelId(modelId);

    FlushCache attribute2 = new FlushCache(modelId);

    assertEqualsIsSymmetric(attribute, attribute2);
  }

  /**
   * @see org.springmodules.EqualsHashCodeTestCase#testEqualsIsTransitive()
   */
  public void testEqualsIsTransitive() {
    String modelId = "pojo";
    attribute.setModelId(modelId);

    FlushCache attribute2 = new FlushCache(modelId);
    FlushCache attribute3 = new FlushCache(modelId);

    assertEqualsIsTransitive(attribute, attribute2, attribute3);
  }

  /**
   * @see org.springmodules.EqualsHashCodeTestCase#testEqualsNullComparison()
   */
  public void testEqualsNullComparison() {
    assertEqualsNullComparisonReturnsFalse(attribute);
  }

  public void testToString() {
    attribute.setModelId("main");
    String actual = attribute.getClass().getName() + "@"
        + ObjectUtils.getIdentityHexString(attribute) + "[modelId='main']";
    assertEquals(attribute.toString(), actual);
  }

  public void testToStringWithModelIdEqualToNull() {
    attribute.setModelId(null);
    String actual = attribute.getClass().getName() + "@"
        + ObjectUtils.getIdentityHexString(attribute) + "[modelId=null]";
    assertEquals(attribute.toString(), actual);
  }

  protected void setUp() {
    attribute = new FlushCache();
  }
}