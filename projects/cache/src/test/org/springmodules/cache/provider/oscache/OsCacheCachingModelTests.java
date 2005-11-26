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

package org.springmodules.cache.provider.oscache;

import org.springframework.util.ObjectUtils;
import org.springmodules.AbstractEqualsHashCodeTestCase;

/**
 * <p>
 * Unit Tests for <code>{@link OsCacheCachingModel}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public final class OsCacheCachingModelTests extends
    AbstractEqualsHashCodeTestCase {

  private OsCacheCachingModel model;

  public OsCacheCachingModelTests(String name) {
    super(name);
  }

  protected void setUp() {
    model = new OsCacheCachingModel();
  }

  /**
   * @see org.springmodules.EqualsHashCodeTestCase#testEqualsHashCodeRelationship()
   */
  public void testEqualsHashCodeRelationship() {
    String cronExpression = "* * 0 0 0";
    String groups = "Test,Valid";
    Integer refreshPeriod = new Integer(43);

    model.setCronExpression(cronExpression);
    model.setGroups(groups);
    model.setRefreshPeriod(refreshPeriod);

    OsCacheCachingModel model2 = new OsCacheCachingModel(groups, refreshPeriod,
        cronExpression);

    assertEqualsHashCodeRelationshipIsCorrect(model, model2);

    cronExpression = null;
    model.setCronExpression(cronExpression);
    model2.setCronExpression(cronExpression);

    assertEqualsHashCodeRelationshipIsCorrect(model, model2);

    groups = null;
    model.setGroups(groups);
    model2.setGroups(groups);

    assertEqualsHashCodeRelationshipIsCorrect(model, model2);

    String[] groupArray = { null, "Pojos" };
    model.setGroups(groupArray);
    model2.setGroups(groupArray);

    assertEqualsHashCodeRelationshipIsCorrect(model, model2);

    refreshPeriod = null;
    model.setRefreshPeriod(refreshPeriod);
    model2.setRefreshPeriod(refreshPeriod);

    assertEqualsHashCodeRelationshipIsCorrect(model, model2);
  }

  /**
   * @see org.springmodules.EqualsHashCodeTestCase#testEqualsIsConsistent()
   */
  public void testEqualsIsConsistent() {
    String groups = "Services,Pojos";
    int refreshPeriod = 43;

    model.setGroups(groups);
    model.setRefreshPeriod(refreshPeriod);

    OsCacheCachingModel model2 = new OsCacheCachingModel(groups, refreshPeriod);

    assertEquals(model, model2);

    model2.setCronExpression("* * * * *");
    assertFalse(model.equals(model2));

    model2.setCronExpression(null);
    model2.setGroups("Pojos");
    assertFalse(model.equals(model2));

    model2.setGroups(groups);
    model2.setRefreshPeriod(99);
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
    String cronExpression = "* * * * *";
    String groups = "Services,Pojos";
    int refreshPeriod = 43;

    model.setCronExpression(cronExpression);
    model.setGroups(groups);
    model.setRefreshPeriod(refreshPeriod);

    OsCacheCachingModel model2 = new OsCacheCachingModel(groups, refreshPeriod,
        cronExpression);

    assertEqualsIsSymmetric(model, model2);
  }

  /**
   * @see org.springmodules.EqualsHashCodeTestCase#testEqualsIsTransitive()
   */
  public void testEqualsIsTransitive() {
    String groups = "Persistence";
    int refreshPeriod = 35;

    model.setGroups(groups);
    model.setRefreshPeriod(refreshPeriod);

    OsCacheCachingModel model2 = new OsCacheCachingModel(groups, refreshPeriod);
    OsCacheCachingModel model3 = new OsCacheCachingModel(groups, refreshPeriod);

    assertEqualsIsTransitive(model, model2, model3);
  }

  /**
   * @see org.springmodules.EqualsHashCodeTestCase#testEqualsNullComparison()
   */
  public void testEqualsNullComparison() {
    assertEqualsNullComparisonReturnsFalse(model);
  }

  public void testToStringWithEmptyGroups() {
    model.setGroups(new String[0]);
    model.setRefreshPeriod(98);
    model.setCronExpression("* * 0 0 0");
    String actual = model.getClass().getName() + "@"
        + ObjectUtils.getIdentityHexString(model)
        + "[refreshPeriod=98, groups={}, cronExpression='* * 0 0 0']";
    assertEquals(model.toString(), actual);
  }

  public void testToStringWithGroupsAndCronExpressionEqualToNull() {
    model.setGroups((String[]) null);
    model.setRefreshPeriod(34);
    model.setCronExpression(null);
    String actual = model.getClass().getName() + "@"
        + ObjectUtils.getIdentityHexString(model)
        + "[refreshPeriod=34, groups=null, cronExpression=null]";
    assertEquals(model.toString(), actual);
  }

  public void testToStringWithNotEmptyGroups() {
    model.setGroups(new String[] { "main", null });
    model.setRefreshPeriod(9);
    model.setCronExpression("* * * * *");
    String actual = model.getClass().getName()
        + "@"
        + ObjectUtils.getIdentityHexString(model)
        + "[refreshPeriod=9, groups={'main', null}, cronExpression='* * * * *']";
    assertEquals(model.toString(), actual);
  }
}