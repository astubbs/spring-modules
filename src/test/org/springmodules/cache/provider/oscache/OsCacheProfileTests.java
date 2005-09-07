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
import org.springmodules.cache.util.ArrayUtils;
import org.springmodules.cache.util.Strings;

/**
 * <p>
 * Unit Tests for <code>{@link OsCacheProfile}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.13 $ $Date: 2005/09/07 02:01:42 $
 */
public final class OsCacheProfileTests extends TestCase implements
    EqualsHashCodeTestCase {

  private static Log logger = LogFactory.getLog(OsCacheProfileTests.class);

  private OsCacheProfile cacheProfile;

  public OsCacheProfileTests(String name) {
    super(name);
  }

  private void assertToStringIsCorrect() {
    StringBuffer buffer = new StringBuffer(this.cacheProfile.getClass()
        .getName());
    buffer.append("@" + System.identityHashCode(this.cacheProfile) + "[");
    buffer.append("refreshPeriod=" + this.cacheProfile.getRefreshPeriod()
        + ", ");
    buffer.append("groups="
        + ArrayUtils.toString(this.cacheProfile.getGroups()) + ", ");
    buffer.append("cronExpression="
        + Strings.quote(this.cacheProfile.getCronExpression()) + "]");

    String expected = buffer.toString();
    String actual = this.cacheProfile.toString();

    logger.debug("Expected 'toString': " + expected);
    logger.debug("Actual 'toString':   " + actual);

    assertEquals(expected, actual);
  }

  protected void setUp() throws Exception {
    super.setUp();

    this.cacheProfile = new OsCacheProfile();
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsHashCodeRelationship()
   */
  public void testEqualsHashCodeRelationship() {
    String cronExpression = "* * 0 0 0";
    String groups = "Test,Valid";
    Integer refreshPeriod = new Integer(43);

    this.cacheProfile.setCronExpression(cronExpression);
    this.cacheProfile.setGroups(groups);
    this.cacheProfile.setRefreshPeriod(refreshPeriod);

    OsCacheProfile anotherProfile = new OsCacheProfile(groups, refreshPeriod,
        cronExpression);

    EqualsHashCodeAssert.assertEqualsHashCodeRelationshipIsCorrect(
        this.cacheProfile, anotherProfile);

    cronExpression = null;
    this.cacheProfile.setCronExpression(cronExpression);
    anotherProfile.setCronExpression(cronExpression);

    EqualsHashCodeAssert.assertEqualsHashCodeRelationshipIsCorrect(
        this.cacheProfile, anotherProfile);

    groups = null;
    this.cacheProfile.setGroups(groups);
    anotherProfile.setGroups(groups);

    EqualsHashCodeAssert.assertEqualsHashCodeRelationshipIsCorrect(
        this.cacheProfile, anotherProfile);

    String[] groupArray = { null, "Pojos" };
    this.cacheProfile.setGroups(groupArray);
    anotherProfile.setGroups(groupArray);

    EqualsHashCodeAssert.assertEqualsHashCodeRelationshipIsCorrect(
        this.cacheProfile, anotherProfile);

    refreshPeriod = null;
    this.cacheProfile.setRefreshPeriod(refreshPeriod);
    anotherProfile.setRefreshPeriod(refreshPeriod);

    EqualsHashCodeAssert.assertEqualsHashCodeRelationshipIsCorrect(
        this.cacheProfile, anotherProfile);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsConsistent()
   */
  public void testEqualsIsConsistent() {
    String groups = "Services,Pojos";
    int refreshPeriod = 43;

    this.cacheProfile.setGroups(groups);
    this.cacheProfile.setRefreshPeriod(refreshPeriod);

    OsCacheProfile anotherProfile = new OsCacheProfile(groups, refreshPeriod);

    assertEquals(this.cacheProfile, anotherProfile);

    anotherProfile.setCronExpression("* * * * *");
    assertFalse(this.cacheProfile.equals(anotherProfile));

    anotherProfile.setCronExpression(null);
    anotherProfile.setGroups("Pojos");
    assertFalse(this.cacheProfile.equals(anotherProfile));

    anotherProfile.setGroups(groups);
    anotherProfile.setRefreshPeriod(99);
    assertFalse(this.cacheProfile.equals(anotherProfile));
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsReflexive()
   */
  public void testEqualsIsReflexive() {
    EqualsHashCodeAssert.assertEqualsIsReflexive(this.cacheProfile);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsSymmetric()
   */
  public void testEqualsIsSymmetric() {
    String cronExpression = "* * * * *";
    String groups = "Services,Pojos";
    int refreshPeriod = 43;

    this.cacheProfile.setCronExpression(cronExpression);
    this.cacheProfile.setGroups(groups);
    this.cacheProfile.setRefreshPeriod(refreshPeriod);

    OsCacheProfile anotherProfile = new OsCacheProfile(groups, refreshPeriod,
        cronExpression);

    EqualsHashCodeAssert.assertEqualsIsSymmetric(this.cacheProfile,
        anotherProfile);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsTransitive()
   */
  public void testEqualsIsTransitive() {
    String groups = "Persistence";
    int refreshPeriod = 35;

    this.cacheProfile.setGroups(groups);
    this.cacheProfile.setRefreshPeriod(refreshPeriod);

    OsCacheProfile secondProfile = new OsCacheProfile(groups, refreshPeriod);
    OsCacheProfile thirdProfile = new OsCacheProfile(groups, refreshPeriod);

    EqualsHashCodeAssert.assertEqualsIsTransitive(this.cacheProfile,
        secondProfile, thirdProfile);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsNullComparison()
   */
  public void testEqualsNullComparison() {
    EqualsHashCodeAssert
        .assertEqualsNullComparisonReturnsFalse(this.cacheProfile);
  }

  public void testToStringWithEmptyGroups() {
    this.cacheProfile.setGroups(new String[0]);
    this.cacheProfile.setRefreshPeriod(98);
    this.cacheProfile.setCronExpression("* * 0 0 0");

    assertToStringIsCorrect();
  }

  public void testToStringWithGroupsAndCronExpressionEqualToNull() {
    this.cacheProfile.setGroups((String[]) null);
    this.cacheProfile.setRefreshPeriod(34);
    this.cacheProfile.setCronExpression(null);

    assertToStringIsCorrect();
  }

  public void testToStringWithNotEmptyGroups() {
    this.cacheProfile.setGroups(new String[] { "main", null });
    this.cacheProfile.setRefreshPeriod(9);
    this.cacheProfile.setCronExpression("* * * * *");

    assertToStringIsCorrect();
  }
}