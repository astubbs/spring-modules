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

package org.springmodules.cache.provider.jcs;

import java.util.Properties;

import junit.framework.TestCase;

import org.springmodules.cache.provider.CacheProfile;

/**
 * <p>
 * Unit Tests for <code>{@link JcsProfileEditor}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.3 $ $Date: 2005/08/05 02:45:14 $
 */
public final class JcsProfileEditorTests extends TestCase {

  /**
   * Primary object that is under test.
   */
  private JcsProfileEditor factory;

  public JcsProfileEditorTests(String name) {
    super(name);
  }

  private void assertCreateCacheProfileThrowsIllegalArgumentException(
      Properties cacheProfileProperties) {

    try {
      this.factory.createCacheProfile(cacheProfileProperties);
      fail("Expecting exception <" + IllegalArgumentException.class.getName()
          + ">");
    } catch (IllegalArgumentException exception) {
      // we are expecting this exception.
    }
  }

  protected void setUp() throws Exception {
    super.setUp();
    this.factory = new JcsProfileEditor();
  }

  /**
   * Verifies that the method
   * <code>{@link JcsProfileEditor#createCacheProfile(Properties)}</code>
   * creates a new instance of <code>{@link JcsProfile}</code> and populates
   * it with the values of the properties "cacheName" and "group" set in a
   * <code>java.util.Properties</code>.
   */
  public void testCreateCacheProfile() {
    String cacheName = "main";
    String group = "test";

    Properties properties = new Properties();
    properties.setProperty("cacheName", cacheName);
    properties.setProperty("group", group);

    JcsProfile expectedProfile = new JcsProfile();
    expectedProfile.setCacheName(cacheName);
    expectedProfile.setGroup(group);

    CacheProfile actualProfile = this.factory.createCacheProfile(properties);

    assertEquals("<Cache profile>", expectedProfile, actualProfile);
  }

  /**
   * Verifies that the method
   * <code>{@link JcsProfileEditor#createCacheProfile(Properties)}</code>
   * throws an <code>IllegalArgumentException</code> if the property
   * "cacheName" set in a <code>java.util.Properties</code> is empty.
   */
  public void testCreateCacheProfileWithEmptyCacheName() {
    Properties properties = new Properties();
    properties.setProperty("cacheName", "");
    this.assertCreateCacheProfileThrowsIllegalArgumentException(properties);
  }

  /**
   * Verifies that the method
   * <code>{@link JcsProfileEditor#createCacheProfile(Properties)}</code>
   * throws an <code>IllegalArgumentException</code> if an empty
   * <code>java.util.Properties</code> is specified.
   */
  public void testCreateCacheProfileWithEmptySetOfProperties() {
    Properties properties = new Properties();
    this.assertCreateCacheProfileThrowsIllegalArgumentException(properties);
  }

}