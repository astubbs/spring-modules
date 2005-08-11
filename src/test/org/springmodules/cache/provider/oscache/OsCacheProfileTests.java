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
import org.springmodules.AssertEqualsHashCode;
import org.springmodules.EqualsHashCodeTestCase;

/**
 * <p>
 * Unit Tests for <code>{@link OsCacheProfile}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.10 $ $Date: 2005/08/11 04:48:07 $
 */
public final class OsCacheProfileTests extends TestCase implements
    EqualsHashCodeTestCase {

  private static Log logger = LogFactory.getLog(OsCacheProfileTests.class);

  /**
   * Primary object that is under test.
   */
  private OsCacheProfile cacheProfile;

  public OsCacheProfileTests(String name) {

    super(name);
  }

  private void assertEqualToString(String expected) {
    String actual = this.cacheProfile.toString();

    logger.debug("Expected toString: " + expected);
    logger.debug("Actual toString:   " + actual);

    assertEquals("<ToString>", expected, actual);
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

    AssertEqualsHashCode.assertEqualsHashCodeRelationshipIsCorrect(
        this.cacheProfile, anotherProfile);

    cronExpression = null;
    this.cacheProfile.setCronExpression(cronExpression);
    anotherProfile.setCronExpression(cronExpression);

    AssertEqualsHashCode.assertEqualsHashCodeRelationshipIsCorrect(
        this.cacheProfile, anotherProfile);

    groups = null;
    this.cacheProfile.setGroups(groups);
    anotherProfile.setGroups(groups);

    AssertEqualsHashCode.assertEqualsHashCodeRelationshipIsCorrect(
        this.cacheProfile, anotherProfile);

    String[] groupArray = { null, "Pojos" };
    this.cacheProfile.setGroups(groupArray);
    anotherProfile.setGroups(groupArray);

    AssertEqualsHashCode.assertEqualsHashCodeRelationshipIsCorrect(
        this.cacheProfile, anotherProfile);

    refreshPeriod = null;
    this.cacheProfile.setRefreshPeriod(refreshPeriod);
    anotherProfile.setRefreshPeriod(refreshPeriod);

    AssertEqualsHashCode.assertEqualsHashCodeRelationshipIsCorrect(
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
    AssertEqualsHashCode.assertEqualsIsReflexive(this.cacheProfile);
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

    AssertEqualsHashCode.assertEqualsIsSymmetric(this.cacheProfile,
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

    AssertEqualsHashCode.assertEqualsIsTransitive(this.cacheProfile,
        secondProfile, thirdProfile);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsNullComparison()
   */
  public void testEqualsNullComparison() {
    AssertEqualsHashCode
        .assertEqualsNullComparisonReturnsFalse(this.cacheProfile);
  }

  public void testToStringWithGroupsAndCronExpressionEqualToNull() {
    String groups = null;
    int refreshPeriod = 34;

    this.cacheProfile.setGroups(groups);
    this.cacheProfile.setRefreshPeriod(refreshPeriod);
    this.cacheProfile.setCronExpression(null);

    StringBuffer buffer = new StringBuffer();
    buffer.append(this.cacheProfile.getClass().getName());
    buffer.append("@" + System.identityHashCode(this.cacheProfile) + "[");
    buffer.append("refreshPeriod=" + refreshPeriod + ", ");
    buffer.append("groups=null, ");
    buffer.append("cronExpression=null]");

    String expected = buffer.toString();
    this.assertEqualToString(expected);
  }

  public void testToStringWithEmptyGroups() {
    int refreshPeriod = 978;
    String cronExpression = "* * 0 0 0";

    this.cacheProfile.setGroups(new String[0]);
    this.cacheProfile.setRefreshPeriod(refreshPeriod);
    this.cacheProfile.setCronExpression(cronExpression);

    StringBuffer buffer = new StringBuffer();
    buffer.append(this.cacheProfile.getClass().getName());
    buffer.append("@" + System.identityHashCode(this.cacheProfile) + "[");
    buffer.append("refreshPeriod=" + refreshPeriod + ", ");
    buffer.append("groups={}, ");
    buffer.append("cronExpression='" + cronExpression + "']");

    String expected = buffer.toString();
    this.assertEqualToString(expected);
  }

  public void testToStringWithNotEmptyGroups() {
    String[] groups = { "main", null };
    int refreshPeriod = 543;
    String cronExpression = "* * 0 0 0";

    this.cacheProfile.setGroups(groups);
    this.cacheProfile.setRefreshPeriod(refreshPeriod);
    this.cacheProfile.setCronExpression(cronExpression);

    StringBuffer buffer = new StringBuffer();
    buffer.append(this.cacheProfile.getClass().getName());
    buffer.append("@" + System.identityHashCode(this.cacheProfile) + "[");
    buffer.append("refreshPeriod=" + refreshPeriod + ", ");
    buffer.append("groups=");
    int groupCount = groups.length;
    for (int i = 0; i < groupCount; i++) {
      if (i == 0) {
        buffer.append("{");
      } else {
        buffer.append(", ");
      }

      String group = groups[i];
      String formattedGroup = null;

      if (group != null) {
        formattedGroup = "'" + group + "'";
      }
      buffer.append(formattedGroup);
    }
    buffer.append("}, ");
    buffer.append("cronExpression='" + cronExpression + "']");

    String expected = buffer.toString();
    this.assertEqualToString(expected);
  }
}