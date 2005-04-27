/* 
 * Created on Jan 11, 2005
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

import java.util.Properties;

import junit.framework.TestCase;

import org.springmodules.cache.provider.CacheProfile;

import com.opensymphony.oscache.base.CacheEntry;

/**
 * <p>
 * Unit Test for <code>{@link OscacheCacheProfileEditor}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/04/27 01:41:50 $
 */
public final class OscacheCacheProfileEditorTests extends TestCase {

  /**
   * Primary object (instance of the class to test).
   */
  private OscacheCacheProfileEditor editor;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the Test Case.
   */
  public OscacheCacheProfileEditorTests(String name) {
    super(name);
  }

  /**
   * Sets up the test fixture.
   */
  protected void setUp() throws Exception {
    super.setUp();
    this.editor = new OscacheCacheProfileEditor();
  }

  /**
   * Verifies that the method
   * <code>{@link OscacheCacheProfileEditor#createCacheProfile(Properties)}</code>
   * creates a new instance of <code>{@link OscacheCacheProfile}</code> from
   * an empty <code>java.util.Properties</code>.
   */
  public void testCreateCacheProfileWithEmptySetOfProperties() {
    Properties properties = new Properties();

    OscacheCacheProfile expectedProfile = new OscacheCacheProfile();
    CacheProfile actualProfile = this.editor.createCacheProfile(properties);
    assertEquals("<Cache profile>", expectedProfile, actualProfile);
  }

  /**
   * Verifies that the method
   * <code>{@link OscacheCacheProfileEditor#createCacheProfile(Properties)}</code>
   * creates a new instance of <code>{@link OscacheCacheProfile}</code>
   * populated with the values of the properties "cronExpression", "groups" set
   * in a <code>java.util.Properties</code>. Also verifies that when the
   * property "refreshPeriod" (in the <code>java.util.Properties</code>) is
   * equal to "INDEFINITE_EXPIRY", the new instance of
   * <code>{@link OscacheCacheProfile}</code> must be set with the value of
   * <code>com.opensymphony.oscache.base.CacheEntry.INDEFINITE_EXPIRY</code>.
   */
  public void testCreateCacheProfileWithNonIntegerRefreshPeriodAndEqualToIndefiniteExpire() {
    String cronExpression = "* * * * *";
    String groups = "test,main";

    Properties properties = new Properties();
    properties.setProperty("cronExpression", cronExpression);
    properties.setProperty("groups", groups);
    properties.setProperty("refreshPeriod", "INDEFINITE_EXPIRY");

    OscacheCacheProfile expectedProfile = new OscacheCacheProfile();
    expectedProfile.setCronExpression(cronExpression);
    expectedProfile.setGroups(groups);
    expectedProfile.setRefreshPeriod(CacheEntry.INDEFINITE_EXPIRY);

    CacheProfile actualProfile = this.editor.createCacheProfile(properties);
    assertEquals("<Cache profile>", expectedProfile, actualProfile);
  }

  /**
   * Verifies that the method
   * <code>{@link OscacheCacheProfileEditor#createCacheProfile(Properties)}</code>
   * creates a new instance of <code>{@link OscacheCacheProfile}</code>
   * populated with the values of the properties "cronExpression", "groups" and
   * "refreshPeriod" set in a <code>java.util.Properties</code>.
   */
  public void testCreateCacheProfileWithNotEmptySetOfProperties() {
    String cronExpression = "* * * * *";
    String groups = "test,main";
    int refreshPeriod = 12;

    Properties properties = new Properties();
    properties.setProperty("cronExpression", cronExpression);
    properties.setProperty("groups", groups);
    properties.setProperty("refreshPeriod", Integer.toString(refreshPeriod));

    OscacheCacheProfile expectedProfile = new OscacheCacheProfile();
    expectedProfile.setCronExpression(cronExpression);
    expectedProfile.setGroups(groups);
    expectedProfile.setRefreshPeriod(refreshPeriod);

    CacheProfile actualProfile = this.editor.createCacheProfile(properties);
    assertEquals("<Cache profile>", expectedProfile, actualProfile);
  }

  /**
   * Verifies that the method
   * <code>{@link OscacheCacheProfileEditor#createCacheProfile(Properties)}</code>
   * creates a new instance of <code>{@link OscacheCacheProfile}</code>
   * populated with the values of the properties "cronExpression" and "groups"
   * set in a <code>java.util.Properties</code>.
   */
  public void testCreateCacheProfileWithoutRefreshPeriod() {
    String cronExpression = "* * * * *";
    String groups = "test,main";

    Properties properties = new Properties();
    properties.setProperty("cronExpression", cronExpression);
    properties.setProperty("groups", groups);

    OscacheCacheProfile expectedProfile = new OscacheCacheProfile();
    expectedProfile.setCronExpression(cronExpression);
    expectedProfile.setGroups(groups);

    CacheProfile actualProfile = this.editor.createCacheProfile(properties);
    assertEquals("<Cache profile>", expectedProfile, actualProfile);
  }

  /**
   * Verifies that the method
   * <code>{@link OscacheCacheProfileEditor#createCacheProfile(Properties)}</code>
   * throws an <code>IllegalArgumentException</code> if the property
   * "refreshPeriod" set in a <code>java.util.Properties</code> is not an
   * integer and it is not equal to the string "INDEFINITE_EXPIRY".
   */
  public void testCreateCacheProfileWithRefreshPeriodNotBeingAndInteger() {
    Properties properties = new Properties();
    properties.setProperty("cronExpression", "* * * * *");
    properties.setProperty("groups", "test,main");
    properties.setProperty("refreshPeriod", "two");

    try {
      this.editor.createCacheProfile(properties);
      fail("An 'IllegalArgumentException' should have been thrown");
    } catch (IllegalArgumentException exception) {
      // we are expecting this exception.
    }
  }
}