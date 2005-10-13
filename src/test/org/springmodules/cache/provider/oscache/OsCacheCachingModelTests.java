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

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springmodules.EqualsHashCodeAssert;
import org.springmodules.EqualsHashCodeTestCase;
import org.springmodules.util.ArrayUtils;
import org.springmodules.util.Strings;

/**
 * <p>
 * Unit Tests for <code>{@link OsCacheCachingModel}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public final class OsCacheCachingModelTests extends TestCase implements
    EqualsHashCodeTestCase {

  private static Log logger = LogFactory.getLog(OsCacheCachingModelTests.class);

  private OsCacheCachingModel cacheModel;

  public OsCacheCachingModelTests(String name) {
    super(name);
  }

  private void assertToStringIsCorrect() {
    StringBuffer buffer = new StringBuffer(cacheModel.getClass().getName());
    buffer.append("@" + System.identityHashCode(cacheModel) + "[");
    buffer.append("refreshPeriod=" + cacheModel.getRefreshPeriod() + ", ");
    buffer.append("groups=" + ArrayUtils.toString(cacheModel.getGroups())
        + ", ");
    buffer.append("cronExpression="
        + Strings.quote(cacheModel.getCronExpression()) + "]");

    String expected = buffer.toString();
    String actual = cacheModel.toString();

    logger.debug("Expected 'toString': " + expected);
    logger.debug("Actual 'toString':   " + actual);

    assertEquals(expected, actual);
  }

  protected void setUp() {
    cacheModel = new OsCacheCachingModel();
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsHashCodeRelationship()
   */
  public void testEqualsHashCodeRelationship() {
    String cronExpression = "* * 0 0 0";
    String groups = "Test,Valid";
    Integer refreshPeriod = new Integer(43);

    cacheModel.setCronExpression(cronExpression);
    cacheModel.setGroups(groups);
    cacheModel.setRefreshPeriod(refreshPeriod);

    OsCacheCachingModel anotherModel = new OsCacheCachingModel(groups,
        refreshPeriod, cronExpression);

    EqualsHashCodeAssert.assertEqualsHashCodeRelationshipIsCorrect(cacheModel,
        anotherModel);

    cronExpression = null;
    cacheModel.setCronExpression(cronExpression);
    anotherModel.setCronExpression(cronExpression);

    EqualsHashCodeAssert.assertEqualsHashCodeRelationshipIsCorrect(cacheModel,
        anotherModel);

    groups = null;
    cacheModel.setGroups(groups);
    anotherModel.setGroups(groups);

    EqualsHashCodeAssert.assertEqualsHashCodeRelationshipIsCorrect(cacheModel,
        anotherModel);

    String[] groupArray = { null, "Pojos" };
    cacheModel.setGroups(groupArray);
    anotherModel.setGroups(groupArray);

    EqualsHashCodeAssert.assertEqualsHashCodeRelationshipIsCorrect(cacheModel,
        anotherModel);

    refreshPeriod = null;
    cacheModel.setRefreshPeriod(refreshPeriod);
    anotherModel.setRefreshPeriod(refreshPeriod);

    EqualsHashCodeAssert.assertEqualsHashCodeRelationshipIsCorrect(cacheModel,
        anotherModel);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsConsistent()
   */
  public void testEqualsIsConsistent() {
    String groups = "Services,Pojos";
    int refreshPeriod = 43;

    cacheModel.setGroups(groups);
    cacheModel.setRefreshPeriod(refreshPeriod);

    OsCacheCachingModel anotherModel = new OsCacheCachingModel(groups,
        refreshPeriod);

    assertEquals(cacheModel, anotherModel);

    anotherModel.setCronExpression("* * * * *");
    assertFalse(cacheModel.equals(anotherModel));

    anotherModel.setCronExpression(null);
    anotherModel.setGroups("Pojos");
    assertFalse(cacheModel.equals(anotherModel));

    anotherModel.setGroups(groups);
    anotherModel.setRefreshPeriod(99);
    assertFalse(cacheModel.equals(anotherModel));
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsReflexive()
   */
  public void testEqualsIsReflexive() {
    EqualsHashCodeAssert.assertEqualsIsReflexive(cacheModel);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsSymmetric()
   */
  public void testEqualsIsSymmetric() {
    String cronExpression = "* * * * *";
    String groups = "Services,Pojos";
    int refreshPeriod = 43;

    cacheModel.setCronExpression(cronExpression);
    cacheModel.setGroups(groups);
    cacheModel.setRefreshPeriod(refreshPeriod);

    OsCacheCachingModel anotherModel = new OsCacheCachingModel(groups,
        refreshPeriod, cronExpression);

    EqualsHashCodeAssert.assertEqualsIsSymmetric(cacheModel, anotherModel);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsTransitive()
   */
  public void testEqualsIsTransitive() {
    String groups = "Persistence";
    int refreshPeriod = 35;

    cacheModel.setGroups(groups);
    cacheModel.setRefreshPeriod(refreshPeriod);

    OsCacheCachingModel secondModel = new OsCacheCachingModel(groups,
        refreshPeriod);
    OsCacheCachingModel thirdModel = new OsCacheCachingModel(groups,
        refreshPeriod);

    EqualsHashCodeAssert.assertEqualsIsTransitive(cacheModel, secondModel,
        thirdModel);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsNullComparison()
   */
  public void testEqualsNullComparison() {
    EqualsHashCodeAssert.assertEqualsNullComparisonReturnsFalse(cacheModel);
  }

  public void testToStringWithEmptyGroups() {
    cacheModel.setGroups(new String[0]);
    cacheModel.setRefreshPeriod(98);
    cacheModel.setCronExpression("* * 0 0 0");

    assertToStringIsCorrect();
  }

  public void testToStringWithGroupsAndCronExpressionEqualToNull() {
    cacheModel.setGroups((String[]) null);
    cacheModel.setRefreshPeriod(34);
    cacheModel.setCronExpression(null);

    assertToStringIsCorrect();
  }

  public void testToStringWithNotEmptyGroups() {
    cacheModel.setGroups(new String[] { "main", null });
    cacheModel.setRefreshPeriod(9);
    cacheModel.setCronExpression("* * * * *");

    assertToStringIsCorrect();
  }
}