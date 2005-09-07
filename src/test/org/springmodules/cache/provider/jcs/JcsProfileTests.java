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
import org.springmodules.EqualsHashCodeAssert;
import org.springmodules.EqualsHashCodeTestCase;
import org.springmodules.cache.util.Strings;

/**
 * <p>
 * Unit Tests for <code>{@link JcsProfile}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.12 $ $Date: 2005/09/07 02:01:39 $
 */
public final class JcsProfileTests extends TestCase implements
    EqualsHashCodeTestCase {

  private static Log logger = LogFactory.getLog(JcsProfileTests.class);

  private JcsProfile cacheProfile;

  public JcsProfileTests(String name) {
    super(name);
  }

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

    EqualsHashCodeAssert.assertEqualsHashCodeRelationshipIsCorrect(
        this.cacheProfile, anotherProfile);

    cacheName = null;
    this.cacheProfile.setCacheName(cacheName);
    anotherProfile.setCacheName(cacheName);

    EqualsHashCodeAssert.assertEqualsHashCodeRelationshipIsCorrect(
        this.cacheProfile, anotherProfile);

    group = null;
    this.cacheProfile.setGroup(group);
    anotherProfile.setGroup(group);

    EqualsHashCodeAssert.assertEqualsHashCodeRelationshipIsCorrect(
        this.cacheProfile, anotherProfile);
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
    assertFalse(this.cacheProfile.equals(anotherProfile));

    anotherProfile.setCacheName(cacheName);
    anotherProfile.setGroup("test");
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
    String cacheName = "mainCache";
    String group = "testGroup";

    this.cacheProfile.setCacheName(cacheName);
    this.cacheProfile.setGroup(group);

    JcsProfile anotherProfile = new JcsProfile(cacheName, group);

    EqualsHashCodeAssert.assertEqualsIsSymmetric(this.cacheProfile,
        anotherProfile);
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

  private void assertToStringIsCorrect() {
    StringBuffer buffer = new StringBuffer(this.cacheProfile.getClass()
        .getName());
    buffer.append("@" + System.identityHashCode(this.cacheProfile) + "[");
    buffer.append("cacheName="
        + Strings.quote(this.cacheProfile.getCacheName()) + ", ");
    buffer.append("group=" + Strings.quote(this.cacheProfile.getGroup()) + "]");

    String expected = buffer.toString();
    String actual = this.cacheProfile.toString();

    logger.debug("Expected 'toString': " + expected);
    logger.debug("Actual 'toString':   " + actual);

    assertEquals(expected, actual);
  }

  public void testToStringWithCacheNameAndGroupEqualToNull() {
    this.cacheProfile.setCacheName(null);
    this.cacheProfile.setGroup(null);

    assertToStringIsCorrect();
  }

  public void testToStringWithCacheNameAndGroupNotEqualToNull() {
    this.cacheProfile.setCacheName("main");
    this.cacheProfile.setGroup("services");

    assertToStringIsCorrect();
  }
}