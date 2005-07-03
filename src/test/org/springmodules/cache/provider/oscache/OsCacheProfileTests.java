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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springmodules.cache.AbstractJavaBeanTests;

/**
 * <p>
 * Unit Test for <code>{@link OsCacheProfile}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.5 $ $Date: 2005/07/03 04:33:13 $
 */
public final class OsCacheProfileTests extends AbstractJavaBeanTests {

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
   * @see AbstractJavaBeanTests#getEqualObjects()
   */
  protected Object[] getEqualObjects() {
    String[] groups = new String[] { "Test", "Valid" };
    OsCacheProfile equalProfile = new OsCacheProfile();
    equalProfile.setCronExpression("* * 0 0 0");
    equalProfile.setGroups(groups);
    equalProfile.setRefreshPeriod(43);

    return new Object[] { equalProfile };
  }

  /**
   * @see AbstractJavaBeanTests#getExpectedHashCode()
   */
  protected int getExpectedHashCode() {
    int hash = 7;
    hash = 31 * hash + this.cacheProfile.getCronExpression().hashCode();

    String[] groups = this.cacheProfile.getGroups();
    int groupCount = groups.length;
    for (int i = 0; i < groupCount; i++) {
      String group = groups[i];
      hash = 31 * hash + (group != null ? group.hashCode() : 0);
    }

    hash = 31 * hash + this.cacheProfile.getRefreshPeriod().hashCode();
    return hash;
  }

  /**
   * @see AbstractJavaBeanTests#getExpectedToString()
   */
  protected String getExpectedToString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append(this.cacheProfile.getClass().getName() + ": ");
    buffer.append("refreshPeriod=" + this.cacheProfile.getRefreshPeriod()
        + "; ");

    buffer.append("groups=");

    String[] groups = this.cacheProfile.getGroups();
    int groupCount = groups.length;
    for (int i = 0; i < groupCount; i++) {
      if (i == 0) {
        buffer.append('[');
      } else {
        buffer.append(", ");
      }

      buffer.append("'" + groups[i] + "'");
    }
    buffer.append("]; ");

    buffer.append("cronExpression='" + this.cacheProfile.getCronExpression()
        + "'; ");
    buffer.append("systemHashCode="
        + System.identityHashCode(this.cacheProfile));

    String expectedToString = buffer.toString();
    logger.debug("expectedToString: " + expectedToString);

    return expectedToString;
  }

  /**
   * @see AbstractJavaBeanTests#getNotEqualObjects()
   */
  protected Object[] getNotEqualObjects() {
    OsCacheProfile notEqualProfile = new OsCacheProfile();
    notEqualProfile.setCronExpression("0 0 0 0 0");
    notEqualProfile.setGroups("Alt");
    notEqualProfile.setRefreshPeriod(5);

    return new Object[] { notEqualProfile };
  }

  /**
   * @see AbstractJavaBeanTests#getPrimaryObject()
   */
  protected Object getPrimaryObject() {
    return this.cacheProfile;
  }

  /**
   * Sets up the test fixture.
   */
  protected void setUp() throws Exception {
    super.setUp();

    this.cacheProfile = new OsCacheProfile();
    this.cacheProfile.setCronExpression("* * 0 0 0");
    this.cacheProfile.setGroups("Test,Valid");
    this.cacheProfile.setRefreshPeriod(43);
  }

  /**
   * Verifies that the method
   * <code>{@link OsCacheProfile#setGroups(String)}</code> sets the array of
   * groups equal to <code>null</code> if an empty String is passed as
   * argument.
   */
  public void testSetGroupsWithEmptyString() {

    assertTrue("The profile should have groups",
        this.cacheProfile.getGroups().length > 0);

    this.cacheProfile.setGroups("");

    assertNull("The profile should not have any group", this.cacheProfile
        .getGroups());
  }

  /**
   * Verifies that the method <code>{@link OsCacheProfile#toString()}</code>
   * creates a correct String representation of a
   * <code>{@link OsCacheProfile}</code> if the array of groups is
   * empty.
   */
  public void testToStringWithEmptyGroups() {
    this.cacheProfile.setGroups(new String[0]);

    StringBuffer buffer = new StringBuffer();
    buffer.append(this.cacheProfile.getClass().getName() + ": ");
    buffer.append("refreshPeriod=" + this.cacheProfile.getRefreshPeriod()
        + "; ");
    buffer.append("groups=[]; ");
    buffer.append("cronExpression='" + this.cacheProfile.getCronExpression()
        + "'; ");
    buffer.append("systemHashCode="
        + System.identityHashCode(this.cacheProfile));

    String expectedToString = buffer.toString();
    logger.debug("expectedToString: " + expectedToString);

    String actualToString = this.cacheProfile.toString();

    assertEquals("<toString>", expectedToString, actualToString);
  }

  /**
   * Verifies that the method <code>{@link OsCacheProfile#toString()}</code>
   * creates a correct String representation of a
   * <code>{@link OsCacheProfile}</code> if the array of groups is
   * <code>null</code>.
   */
  public void testToStringWithGroupsEqualToNull() {
    this.cacheProfile.setGroups((String[]) null);

    StringBuffer buffer = new StringBuffer();
    buffer.append(this.cacheProfile.getClass().getName() + ": ");
    buffer.append("refreshPeriod=" + this.cacheProfile.getRefreshPeriod()
        + "; ");
    buffer.append("groups=null; ");
    buffer.append("cronExpression='" + this.cacheProfile.getCronExpression()
        + "'; ");
    buffer.append("systemHashCode="
        + System.identityHashCode(this.cacheProfile));

    String expectedToString = buffer.toString();
    logger.debug("expectedToString: " + expectedToString);

    String actualToString = this.cacheProfile.toString();

    assertEquals("<toString>", expectedToString, actualToString);
  }
}