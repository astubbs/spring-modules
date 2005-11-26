/* 
 * Created on Sep 24, 2004
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

package org.springmodules.cache.provider.jcs;

import org.springframework.util.ObjectUtils;
import org.springmodules.AbstractEqualsHashCodeTestCase;

/**
 * <p>
 * Unit Tests for <code>{@link JcsCachingModel}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public final class JcsCachingModelTests extends AbstractEqualsHashCodeTestCase {

  private JcsCachingModel model;

  public JcsCachingModelTests(String name) {
    super(name);
  }

  protected void setUp() {
    model = new JcsCachingModel();
  }

  /**
   * @see org.springmodules.EqualsHashCodeTestCase#testEqualsHashCodeRelationship()
   */
  public void testEqualsHashCodeRelationship() {
    String cacheName = "main";
    String group = "test";

    model.setCacheName(cacheName);
    model.setGroup(group);

    JcsCachingModel model2 = new JcsCachingModel(cacheName, group);

    assertEqualsHashCodeRelationshipIsCorrect(model, model2);

    cacheName = null;
    model.setCacheName(cacheName);
    model2.setCacheName(cacheName);

    assertEqualsHashCodeRelationshipIsCorrect(model, model2);

    group = null;
    model.setGroup(group);
    model2.setGroup(group);

    assertEqualsHashCodeRelationshipIsCorrect(model, model2);
  }

  /**
   * @see org.springmodules.EqualsHashCodeTestCase#testEqualsIsConsistent()
   */
  public void testEqualsIsConsistent() {
    String cacheName = "ch01";
    String group = "grp87";

    model.setCacheName(cacheName);
    model.setGroup(group);

    JcsCachingModel model2 = new JcsCachingModel(cacheName, group);

    assertEquals(model, model2);

    model2.setCacheName("main");
    assertFalse(model.equals(model2));

    model2.setCacheName(cacheName);
    model2.setGroup("test");
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
    String cacheName = "mainCache";
    String group = "testGroup";

    model.setCacheName(cacheName);
    model.setGroup(group);

    JcsCachingModel model2 = new JcsCachingModel(cacheName, group);
    assertEqualsIsSymmetric(model, model2);
  }

  /**
   * @see org.springmodules.EqualsHashCodeTestCase#testEqualsIsTransitive()
   */
  public void testEqualsIsTransitive() {
    String cacheName = "pojos";
    String group = "model";

    model.setCacheName(cacheName);
    model.setGroup(group);

    JcsCachingModel model2 = new JcsCachingModel(cacheName, group);
    JcsCachingModel model3 = new JcsCachingModel(cacheName, group);

    assertEqualsIsTransitive(model, model2, model3);
  }

  /**
   * @see org.springmodules.EqualsHashCodeTestCase#testEqualsNullComparison()
   */
  public void testEqualsNullComparison() {
    assertEqualsNullComparisonReturnsFalse(model);
  }

  public void testToStringWithCacheNameAndGroupEqualToNull() {
    model.setCacheName(null);
    model.setGroup(null);
    String actual = model.getClass().getName() + "@"
        + ObjectUtils.getIdentityHexString(model)
        + "[cacheName=null, group=null]";
    assertEquals(model.toString(), actual);
  }

  public void testToStringWithCacheNameAndGroupNotEqualToNull() {
    model.setCacheName("main");
    model.setGroup("services");
    String actual = model.getClass().getName() + "@"
        + ObjectUtils.getIdentityHexString(model)
        + "[cacheName='main', group='services']";
    assertEquals(model.toString(), actual);
  }
}