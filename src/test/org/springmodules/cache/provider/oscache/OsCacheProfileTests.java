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
import org.springmodules.EqualsHashCodeTestCase;

/**
 * <p>
 * Unit Test for <code>{@link OsCacheProfile}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.6 $ $Date: 2005/07/15 18:03:58 $
 */
public final class OsCacheProfileTests extends TestCase implements
    EqualsHashCodeTestCase {

  /**
   * Message logger.
   */
  private static Log logger = LogFactory.getLog(OsCacheProfileTests.class);

  /**
   * Primary object (instance of the class to test).
   */
  private OsCacheProfile cacheProfile;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the Test Case.
   */
  public OsCacheProfileTests(String name) {

    super(name);
  }

  /**
   * Sets up the test fixture.
   */
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
    int refreshPeriod = 43;

    this.cacheProfile.setCronExpression(cronExpression);
    this.cacheProfile.setGroups(groups);
    this.cacheProfile.setRefreshPeriod(refreshPeriod);

    OsCacheProfile anotherProfile = new OsCacheProfile(groups, refreshPeriod,
        cronExpression);

    assertEquals(this.cacheProfile, anotherProfile);
    assertEquals(this.cacheProfile.hashCode(), anotherProfile.hashCode());
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
    anotherProfile.setGroups("Pojos");
    anotherProfile.setRefreshPeriod(99);

    assertFalse(this.cacheProfile.equals(anotherProfile));
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsReflexive()
   */
  public void testEqualsIsReflexive() {
    assertEquals(this.cacheProfile, this.cacheProfile);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsSymmetric()
   */
  public void testEqualsIsSymmetric() {
    String groups = "Services,Pojos";
    int refreshPeriod = 43;

    this.cacheProfile.setGroups(groups);
    this.cacheProfile.setRefreshPeriod(refreshPeriod);

    OsCacheProfile anotherProfile = new OsCacheProfile(groups, refreshPeriod);

    assertTrue(this.cacheProfile.equals(anotherProfile));
    assertTrue(anotherProfile.equals(this.cacheProfile));
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

    assertTrue(this.cacheProfile.equals(secondProfile));
    assertTrue(secondProfile.equals(thirdProfile));
    assertTrue(this.cacheProfile.equals(thirdProfile));
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsNullComparison()
   */
  public void testEqualsNullComparison() {
    assertFalse(this.cacheProfile.equals(null));
  }

  /**
   * Verifies that the method <code>{@link OsCacheProfile#toString()}</code>
   * returns a String representation of a <code>{@link OsCacheProfile}</code>
   * when the properties <code>groups</code> and <code>cronExpression</code>
   * are equal to <code>null</code>.
   */
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
    String actual = this.cacheProfile.toString();

    logger.debug("Expected toString: " + expected);
    logger.debug("Actual toString:   " + actual);

    assertEquals("<ToString>", expected, actual);
  }

  /**
   * Verifies that the method <code>{@link OsCacheProfile#toString()}</code>
   * returns a String representation of a <code>{@link OsCacheProfile}</code>
   * when the property <code>groups</code> is an empty array.
   */
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
    String actual = this.cacheProfile.toString();

    logger.debug("Expected toString: " + expected);
    logger.debug("Actual toString:   " + actual);

    assertEquals("<ToString>", expected, actual);
  }

  /**
   * Verifies that the method <code>{@link OsCacheProfile#toString()}</code>
   * returns a String representation of a <code>{@link OsCacheProfile}</code>
   * when the property <code>groups</code> is not equal to <code>null</code>
   * and is not an empty array.
   */
  public void testToStringWithNotEmptyGroups() {
    String[] groups = { "main", "test" };
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
      buffer.append("'" + groups[i] + "'");
    }
    buffer.append("}, ");
    buffer.append("cronExpression='" + cronExpression + "']");

    String expected = buffer.toString();
    String actual = this.cacheProfile.toString();

    logger.debug("Expected toString: " + expected);
    logger.debug("Actual toString:   " + actual);

    assertEquals("<ToString>", expected, actual);
  }
}