/* 
 * Created on Jan 14, 2005
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

package org.springmodules.cache.provider.ehcache;

import java.util.Properties;

import junit.framework.TestCase;

import org.springmodules.cache.provider.CacheProfile;

/**
 * <p>
 * Unit Test for <code>{@link EhCacheProfileEditor}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/05/15 02:14:35 $
 */
public final class EhCacheProfileEditorTests extends TestCase {

  /**
   * Primary object (instance of the class to test).
   */
  private EhCacheProfileEditor factory;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the Test Case.
   */
  public EhCacheProfileEditorTests(String name) {
    super(name);
  }

  /**
   * Sets up the test fixture.
   */
  protected void setUp() throws Exception {
    super.setUp();
    this.factory = new EhCacheProfileEditor();
  }

  /**
   * Verifies that the method
   * <code>{@link EhCacheProfileEditor#createCacheProfile(Properties)}</code>
   * throws an <code>IllegalArgumentException</code> when an empty
   * <code>java.util.Properties</code> is specified.
   */
  public void testCreateCacheProfileWithEmptySetOfProperties() {
    Properties properties = new Properties();

    try {
      this.factory.createCacheProfile(properties);
      fail("An IllegalArgumentException should have been thrown");
    } catch (IllegalArgumentException exception) {
      // we are expecting this exception.
    }
  }

  /**
   * Verifies that the method
   * <code>{@link EhCacheProfileEditor#createCacheProfile(Properties)}</code>
   * throws an <code>IllegalArgumentException</code> when the value of the
   * property "cacheName" set in a <code>java.util.Properties</code> is empty.
   */
  public void testCreateCacheProfileWithEmptyCacheName() {

    Properties properties = new Properties();
    properties.setProperty("cacheName", "");

    try {
      this.factory.createCacheProfile(properties);
      fail("An IllegalArgumentException should have been thrown");
    } catch (IllegalArgumentException exception) {
      // we are expecting this exception.
    }
  }

  /**
   * Verifies that the method
   * <code>{@link EhCacheProfileEditor#createCacheProfile(Properties)}</code>
   * creates a new instance of <code>{@link EhCacheProfile}</code>
   * populated with the values of the properties "cacheName" and "group" set in
   * a <code>java.util.Properties</code>.
   */
  public void testCreateCacheProfileWithNotEmptyGroup() {
    String cacheName = "main";

    Properties properties = new Properties();
    properties.setProperty("cacheName", cacheName);

    EhCacheProfile expectedProfile = new EhCacheProfile();
    expectedProfile.setCacheName(cacheName);

    CacheProfile actualProfile = this.factory.createCacheProfile(properties);

    assertEquals("<CacheProfile>", expectedProfile, actualProfile);
  }

}