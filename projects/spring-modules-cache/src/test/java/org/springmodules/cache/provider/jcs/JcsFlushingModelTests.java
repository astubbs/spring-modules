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
 * Unit Tests for <code>{@link JcsFlushingModel}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class JcsFlushingModelTests extends AbstractEqualsHashCodeTestCase {

  private JcsFlushingModel model;

  public JcsFlushingModelTests(String name) {
    super(name);
  }

  /**
   * @see org.springmodules.EqualsHashCodeTestCase#testEqualsHashCodeRelationship()
   */
  public void testEqualsHashCodeRelationship() {
    CacheStruct cacheStruct = new CacheStruct("main");
    model.setCacheStruct(cacheStruct);

    JcsFlushingModel model2 = new JcsFlushingModel(cacheStruct);
    assertEqualsHashCodeRelationshipIsCorrect(model, model2);

    cacheStruct = null;
    model.setCacheStruct(cacheStruct);
    model2.setCacheStruct(cacheStruct);
    assertEqualsHashCodeRelationshipIsCorrect(model, model2);
  }

  /**
   * @see org.springmodules.EqualsHashCodeTestCase#testEqualsIsConsistent()
   */
  public void testEqualsIsConsistent() {
    CacheStruct cacheStruct = new CacheStruct("main");
    model.setCacheStruct(cacheStruct);

    JcsFlushingModel model2 = new JcsFlushingModel(cacheStruct);
    assertEquals(model, model2);

    model2.setCacheStructs(new CacheStruct[0]);
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
    CacheStruct cacheStruct = new CacheStruct("test", "group1,group2");
    model.setCacheStruct(cacheStruct);

    JcsFlushingModel model2 = new JcsFlushingModel(cacheStruct);
    assertEqualsIsSymmetric(model, model2);
  }

  /**
   * @see org.springmodules.EqualsHashCodeTestCase#testEqualsIsTransitive()
   */
  public void testEqualsIsTransitive() {
    CacheStruct cacheStruct = new CacheStruct("main", "group1,group2");
    model.setCacheStruct(cacheStruct);

    JcsFlushingModel model2 = new JcsFlushingModel(cacheStruct);
    JcsFlushingModel model3 = new JcsFlushingModel(cacheStruct);

    assertEqualsIsTransitive(model, model2, model3);
  }

  /**
   * @see org.springmodules.EqualsHashCodeTestCase#testEqualsNullComparison()
   */
  public void testEqualsNullComparison() {
    assertEqualsNullComparisonReturnsFalse(model);
  }

  public void testToString() {
    CacheStruct cacheStruct = new CacheStruct("main");
    model.setCacheStruct(cacheStruct);
    model.setFlushBeforeMethodExecution(true);

    String actual = model.getClass().getName() + "@"
        + ObjectUtils.getIdentityHexString(model) + "[cacheStructs={"
        + cacheStruct.getClass().getName() + "@"
        + ObjectUtils.getIdentityHexString(cacheStruct)
        + "[cacheName='main', groups=null]"
        + "}, flushBeforeMethodExecution=true]";
    assertEquals(model.toString(), actual);
  }

  public void testToStringWithCacheStructArrayEqualToNull() {
    model.setCacheStructs(null);
    model.setFlushBeforeMethodExecution(true);

    String actual = model.getClass().getName() + "@"
        + ObjectUtils.getIdentityHexString(model)
        + "[cacheStructs=null, flushBeforeMethodExecution=true]";
    assertEquals(model.toString(), actual);
  }

  public void testToStringWithEmptyCacheStructArray() {
    model.setCacheStructs(new CacheStruct[0]);
    model.setFlushBeforeMethodExecution(true);

    String actual = model.getClass().getName() + "@"
        + ObjectUtils.getIdentityHexString(model)
        + "[cacheStructs={}, flushBeforeMethodExecution=true]";
    assertEquals(model.toString(), actual);
  }

  protected void setUp() {
    model = new JcsFlushingModel();
  }

}
