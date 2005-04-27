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

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springmodules.cache.AbstractJavaBeanTests;

/**
 * <p>
 * Unit Test for <code>{@link OscacheCacheProfile}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/04/27 01:41:49 $
 */
public final class OscacheCacheProfileTests extends AbstractJavaBeanTests {

  /**
   * Primary object (instance of the class to test).
   */
  private OscacheCacheProfile profile;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the Test Case.
   */
  public OscacheCacheProfileTests(String name) {

    super(name);
  }

  /**
   * @see AbstractJavaBeanTests#getEqualObjects()
   */
  protected Object[] getEqualObjects() {
    String[] groups = new String[] { "Test", "Valid" };
    OscacheCacheProfile equalProfile = new OscacheCacheProfile();
    equalProfile.setCronExpression("* * 0 0 0");
    equalProfile.setGroups(groups);
    equalProfile.setRefreshPeriod(43);

    return new Object[] { equalProfile };
  }

  /**
   * @see AbstractJavaBeanTests#getExpectedHashCode()
   */
  protected int getExpectedHashCode() {
    HashCodeBuilder hashCodeBuilder = new HashCodeBuilder(3, 11);
    hashCodeBuilder.append(this.profile.getCronExpression());
    hashCodeBuilder.append(this.profile.getGroups());
    hashCodeBuilder.append(this.profile.getRefreshPeriod());

    int expectedHashCode = hashCodeBuilder.toHashCode();
    return expectedHashCode;
  }

  /**
   * @see AbstractJavaBeanTests#getExpectedToString()
   */
  protected String getExpectedToString() {
    ToStringBuilder toStringBuilder = new ToStringBuilder(this.profile);
    toStringBuilder.append("refreshPeriod", this.profile.getRefreshPeriod());
    toStringBuilder.append("groups", this.profile.getGroups());
    toStringBuilder.append("cronExpression", this.profile.getCronExpression());

    String expectedToString = toStringBuilder.toString();
    return expectedToString;
  }

  /**
   * @see AbstractJavaBeanTests#getNotEqualObjects()
   */
  protected Object[] getNotEqualObjects() {
    OscacheCacheProfile notEqualProfile = new OscacheCacheProfile();
    notEqualProfile.setCronExpression("0 0 0 0 0");
    notEqualProfile.setGroups("Alt");
    notEqualProfile.setRefreshPeriod(5);

    return new Object[] { notEqualProfile };
  }

  /**
   * @see AbstractJavaBeanTests#getPrimaryObject()
   */
  protected Object getPrimaryObject() {
    return this.profile;
  }

  /**
   * Sets up the test fixture.
   */
  protected void setUp() throws Exception {
    super.setUp();

    this.profile = new OscacheCacheProfile();
    this.profile.setCronExpression("* * 0 0 0");
    this.profile.setGroups("Test,Valid");
    this.profile.setRefreshPeriod(43);
  }

  /**
   * Verifies that the method
   * <code>{@link OscacheCacheProfile#setGroups(String)}</code> sets the array
   * of groups equal to <code>null</code> if an empty String is passed as
   * argument.
   */
  public void testSetGroupsWithEmptyString() {

    assertTrue("The profile should have groups",
        this.profile.getGroups().length > 0);

    this.profile.setGroups("");

    assertNull("The profile should not have any group", this.profile
        .getGroups());
  }
}