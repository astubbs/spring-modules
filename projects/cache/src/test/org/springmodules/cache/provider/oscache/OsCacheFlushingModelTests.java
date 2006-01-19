/* 
 * Created on Oct 27, 2005
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
package org.springmodules.cache.provider.oscache;

import org.springframework.util.ObjectUtils;

import org.springmodules.AbstractEqualsHashCodeTestCase;

/**
 * <p>
 * Unit Tests for <code>{@link OsCacheFlushingModel}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class OsCacheFlushingModelTests extends AbstractEqualsHashCodeTestCase {

  private OsCacheFlushingModel model;

  public OsCacheFlushingModelTests(String name) {
    super(name);
  }

  /**
   * @see org.springmodules.EqualsHashCodeTestCase#testEqualsHashCodeRelationship()
   */
  public void testEqualsHashCodeRelationship() {
    String groups = "main,pojos";
    model.setGroups(groups);

    OsCacheFlushingModel model2 = new OsCacheFlushingModel(groups);
    assertEqualsHashCodeRelationshipIsCorrect(model, model2);

    groups = "test";
    model.setGroups(groups);
    model2.setGroups(groups);
    assertEqualsHashCodeRelationshipIsCorrect(model, model2);
  }

  /**
   * @see org.springmodules.EqualsHashCodeTestCase#testEqualsIsConsistent()
   */
  public void testEqualsIsConsistent() {
    String groups = "empire,rebels";
    model.setGroups(groups);

    OsCacheFlushingModel model2 = new OsCacheFlushingModel(groups);
    assertEquals(model, model2);

    model2.setGroups((String) null);
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
    String[] groups = { "pojos" };
    model.setGroups(groups);

    OsCacheFlushingModel model2 = new OsCacheFlushingModel(groups);
    assertEqualsIsSymmetric(model, model2);
  }

  /**
   * @see org.springmodules.EqualsHashCodeTestCase#testEqualsIsTransitive()
   */
  public void testEqualsIsTransitive() {
    String[] groups = { "main" };
    model.setGroups(groups);

    OsCacheFlushingModel model2 = new OsCacheFlushingModel(groups);
    OsCacheFlushingModel model3 = new OsCacheFlushingModel(groups);

    assertEqualsIsTransitive(model, model2, model3);
  }

  /**
   * @see org.springmodules.EqualsHashCodeTestCase#testEqualsNullComparison()
   */
  public void testEqualsNullComparison() {
    assertEqualsNullComparisonReturnsFalse(model);
  }

  public void testToStringWithCacheNamesEqualToNull() {
    model.setGroups((String[]) null);
    model.setFlushBeforeMethodExecution(true);

    String actual = model.getClass().getName() + "@"
        + ObjectUtils.getIdentityHexString(model)
        + "[groups=null, flushBeforeMethodExecution=true]";
    assertEquals(model.toString(), actual);
  }

  public void testToStringWithEmptyCacheNames() {
    model.setGroups(new String[0]);
    model.setFlushBeforeMethodExecution(true);

    String actual = model.getClass().getName() + "@"
        + ObjectUtils.getIdentityHexString(model)
        + "[groups={}, flushBeforeMethodExecution=true]";
    assertEquals(model.toString(), actual);
  }

  public void testToStringWithNotEmptyCacheNames() {
    model.setGroups(new String[] { "main" });
    model.setFlushBeforeMethodExecution(true);

    String actual = model.getClass().getName() + "@"
        + ObjectUtils.getIdentityHexString(model)
        + "[groups={'main'}, flushBeforeMethodExecution=true]";
    assertEquals(model.toString(), actual);
  }

  protected void setUp() {
    model = new OsCacheFlushingModel();
  }

}
