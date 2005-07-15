/* 
 * JcsCachingAttributeTest.java
 * 
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

package org.springmodules.cache.provider.jcs;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springmodules.EqualsHashCodeTestCase;

/**
 * <p>
 * Unit Test for <code>{@link JcsProfile}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.5 $ $Date: 2005/07/15 18:03:57 $
 */
public final class JcsProfileTests extends TestCase implements
    EqualsHashCodeTestCase {

  /**
   * Message logger.
   */
  private static Log logger = LogFactory.getLog(JcsProfileTests.class);

  /**
   * Primary object (instance of the class to test).
   */
  private JcsProfile cacheProfile;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the Test Case.
   */
  public JcsProfileTests(String name) {
    super(name);
  }

  /**
   * Sets up the test fixture.
   */
  protected void setUp() throws Exception {
    super.setUp();

    this.cacheProfile = new JcsProfile();
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsHashCodeRelationship()
   */
  public void testEqualsHashCodeRelationship() {
    String cacheName = "main";
    String group = "test";

    this.cacheProfile.setCacheName(cacheName);
    this.cacheProfile.setGroup(group);

    JcsProfile anotherProfile = new JcsProfile(cacheName, group);

    assertEquals(this.cacheProfile, anotherProfile);
    assertEquals(this.cacheProfile.hashCode(), anotherProfile.hashCode());
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsConsistent()
   */
  public void testEqualsIsConsistent() {
    String cacheName = "ch01";
    String group = "grp87";

    this.cacheProfile.setCacheName(cacheName);
    this.cacheProfile.setGroup(group);

    JcsProfile anotherProfile = new JcsProfile(cacheName, group);

    assertEquals(this.cacheProfile, anotherProfile);

    anotherProfile.setCacheName("main");
    anotherProfile.setGroup("test");

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
    String cacheName = "mainCache";
    String group = "testGroup";

    this.cacheProfile.setCacheName(cacheName);
    this.cacheProfile.setGroup(group);

    JcsProfile anotherProfile = new JcsProfile(cacheName, group);

    assertTrue(this.cacheProfile.equals(anotherProfile));
    assertTrue(anotherProfile.equals(this.cacheProfile));
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsTransitive()
   */
  public void testEqualsIsTransitive() {
    String cacheName = "pojos";
    String group = "model";

    this.cacheProfile.setCacheName(cacheName);
    this.cacheProfile.setGroup(group);

    JcsProfile secondProfile = new JcsProfile(cacheName, group);
    JcsProfile thirdProfile = new JcsProfile(cacheName, group);

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
   * Verifies that the method <code>{@link JcsProfile#toString()}</code>
   * returns a String representation of a <code>{@link JcsProfile}</code> when
   * the properties <code>cacheProfileId</code> and <code>group</code> are
   * equal to <code>null</code>.
   */
  public void testToStringWithCacheNameAndGroupEqualToNull() {
    this.cacheProfile.setCacheName(null);
    this.cacheProfile.setGroup(null);

    StringBuffer buffer = new StringBuffer();
    buffer.append(this.cacheProfile.getClass().getName());
    buffer.append("@" + System.identityHashCode(this.cacheProfile) + "[");
    buffer.append("cacheName=null, ");
    buffer.append("group=null]");

    String expected = buffer.toString();
    String actual = this.cacheProfile.toString();

    logger.debug("Expected toString: " + expected);
    logger.debug("Actual toString:   " + actual);

    assertEquals("<ToString>", expected, actual);
  }

  /**
   * Verifies that the method <code>{@link JcsProfile#toString()}</code>
   * returns a String representation of a <code>{@link JcsProfile}</code> when
   * the properties <code>cacheProfileId</code> and <code>group</code> are
   * not equal to <code>null</code>.
   */
  public void testToStringWithCacheNameAndGroupNotEqualToNull() {
    String cacheName = "main";
    String group = "services";
    this.cacheProfile.setCacheName(cacheName);
    this.cacheProfile.setGroup(group);

    StringBuffer buffer = new StringBuffer();
    buffer.append(this.cacheProfile.getClass().getName());
    buffer.append("@" + System.identityHashCode(this.cacheProfile) + "[");
    buffer.append("cacheName='" + cacheName + "', ");
    buffer.append("group='" + group + "']");

    String expected = buffer.toString();
    String actual = this.cacheProfile.toString();

    logger.debug("Expected toString: " + expected);
    logger.debug("Actual toString:   " + actual);

    assertEquals("<ToString>", expected, actual);
  }
}