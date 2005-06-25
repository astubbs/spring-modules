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

import java.util.Arrays;

import org.springmodules.cache.AbstractJavaBeanTests;

/**
 * <p>
 * Unit Test for <code>{@link OsCacheProfile}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.4 $ $Date: 2005/06/25 22:54:30 $
 */
public final class OsCacheProfileTests extends AbstractJavaBeanTests {

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
    return "OsCacheProfile: refreshPeriod="
        + this.cacheProfile.getRefreshPeriod() + ", groups="
        + Arrays.toString(this.cacheProfile.getGroups()) + ", cronExpression='"
        + this.cacheProfile.getCronExpression() + "'";
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
}