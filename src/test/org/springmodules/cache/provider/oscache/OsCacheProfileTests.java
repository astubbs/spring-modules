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
 * Unit Tests for <code>{@link OsCacheProfile}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.15 $ $Date: 2005/09/25 05:26:22 $
 */
public final class OsCacheProfileTests extends TestCase implements
    EqualsHashCodeTestCase {

  private static Log logger = LogFactory.getLog(OsCacheProfileTests.class);

  private OsCacheProfile cacheProfile;

  public OsCacheProfileTests(String name) {
    super(name);
  }

  private void assertToStringIsCorrect() {
    StringBuffer buffer = new StringBuffer(cacheProfile.getClass().getName());
    buffer.append("@" + System.identityHashCode(cacheProfile) + "[");
    buffer.append("refreshPeriod=" + cacheProfile.getRefreshPeriod() + ", ");
    buffer.append("groups=" + ArrayUtils.toString(cacheProfile.getGroups())
        + ", ");
    buffer.append("cronExpression="
        + Strings.quote(cacheProfile.getCronExpression()) + "]");

    String expected = buffer.toString();
    String actual = cacheProfile.toString();

    logger.debug("Expected 'toString': " + expected);
    logger.debug("Actual 'toString':   " + actual);

    assertEquals(expected, actual);
  }

  protected void setUp() throws Exception {
    super.setUp();

    cacheProfile = new OsCacheProfile();
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsHashCodeRelationship()
   */
  public void testEqualsHashCodeRelationship() {
    String cronExpression = "* * 0 0 0";
    String groups = "Test,Valid";
    Integer refreshPeriod = new Integer(43);

    cacheProfile.setCronExpression(cronExpression);
    cacheProfile.setGroups(groups);
    cacheProfile.setRefreshPeriod(refreshPeriod);

    OsCacheProfile anotherProfile = new OsCacheProfile(groups, refreshPeriod,
        cronExpression);

    EqualsHashCodeAssert.assertEqualsHashCodeRelationshipIsCorrect(
        cacheProfile, anotherProfile);

    cronExpression = null;
    cacheProfile.setCronExpression(cronExpression);
    anotherProfile.setCronExpression(cronExpression);

    EqualsHashCodeAssert.assertEqualsHashCodeRelationshipIsCorrect(
        cacheProfile, anotherProfile);

    groups = null;
    cacheProfile.setGroups(groups);
    anotherProfile.setGroups(groups);

    EqualsHashCodeAssert.assertEqualsHashCodeRelationshipIsCorrect(
        cacheProfile, anotherProfile);

    String[] groupArray = { null, "Pojos" };
    cacheProfile.setGroups(groupArray);
    anotherProfile.setGroups(groupArray);

    EqualsHashCodeAssert.assertEqualsHashCodeRelationshipIsCorrect(
        cacheProfile, anotherProfile);

    refreshPeriod = null;
    cacheProfile.setRefreshPeriod(refreshPeriod);
    anotherProfile.setRefreshPeriod(refreshPeriod);

    EqualsHashCodeAssert.assertEqualsHashCodeRelationshipIsCorrect(
        cacheProfile, anotherProfile);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsConsistent()
   */
  public void testEqualsIsConsistent() {
    String groups = "Services,Pojos";
    int refreshPeriod = 43;

    cacheProfile.setGroups(groups);
    cacheProfile.setRefreshPeriod(refreshPeriod);

    OsCacheProfile anotherProfile = new OsCacheProfile(groups, refreshPeriod);

    assertEquals(cacheProfile, anotherProfile);

    anotherProfile.setCronExpression("* * * * *");
    assertFalse(cacheProfile.equals(anotherProfile));

    anotherProfile.setCronExpression(null);
    anotherProfile.setGroups("Pojos");
    assertFalse(cacheProfile.equals(anotherProfile));

    anotherProfile.setGroups(groups);
    anotherProfile.setRefreshPeriod(99);
    assertFalse(cacheProfile.equals(anotherProfile));
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsReflexive()
   */
  public void testEqualsIsReflexive() {
    EqualsHashCodeAssert.assertEqualsIsReflexive(cacheProfile);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsSymmetric()
   */
  public void testEqualsIsSymmetric() {
    String cronExpression = "* * * * *";
    String groups = "Services,Pojos";
    int refreshPeriod = 43;

    cacheProfile.setCronExpression(cronExpression);
    cacheProfile.setGroups(groups);
    cacheProfile.setRefreshPeriod(refreshPeriod);

    OsCacheProfile anotherProfile = new OsCacheProfile(groups, refreshPeriod,
        cronExpression);

    EqualsHashCodeAssert.assertEqualsIsSymmetric(cacheProfile, anotherProfile);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsTransitive()
   */
  public void testEqualsIsTransitive() {
    String groups = "Persistence";
    int refreshPeriod = 35;

    cacheProfile.setGroups(groups);
    cacheProfile.setRefreshPeriod(refreshPeriod);

    OsCacheProfile secondProfile = new OsCacheProfile(groups, refreshPeriod);
    OsCacheProfile thirdProfile = new OsCacheProfile(groups, refreshPeriod);

    EqualsHashCodeAssert.assertEqualsIsTransitive(cacheProfile, secondProfile,
        thirdProfile);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsNullComparison()
   */
  public void testEqualsNullComparison() {
    EqualsHashCodeAssert.assertEqualsNullComparisonReturnsFalse(cacheProfile);
  }

  public void testToStringWithEmptyGroups() {
    cacheProfile.setGroups(new String[0]);
    cacheProfile.setRefreshPeriod(98);
    cacheProfile.setCronExpression("* * 0 0 0");

    assertToStringIsCorrect();
  }

  public void testToStringWithGroupsAndCronExpressionEqualToNull() {
    cacheProfile.setGroups((String[]) null);
    cacheProfile.setRefreshPeriod(34);
    cacheProfile.setCronExpression(null);

    assertToStringIsCorrect();
  }

  public void testToStringWithNotEmptyGroups() {
    cacheProfile.setGroups(new String[] { "main", null });
    cacheProfile.setRefreshPeriod(9);
    cacheProfile.setCronExpression("* * * * *");

    assertToStringIsCorrect();
  }
}