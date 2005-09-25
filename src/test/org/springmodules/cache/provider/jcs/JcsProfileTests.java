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
import org.springmodules.util.Strings;

/**
 * <p>
 * Unit Tests for <code>{@link JcsProfile}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.14 $ $Date: 2005/09/25 05:26:20 $
 */
public final class JcsProfileTests extends TestCase implements
    EqualsHashCodeTestCase {

  private static Log logger = LogFactory.getLog(JcsProfileTests.class);

  private JcsProfile cacheProfile;

  public JcsProfileTests(String name) {
    super(name);
  }

  private void assertToStringIsCorrect() {
    StringBuffer buffer = new StringBuffer(cacheProfile.getClass()
        .getName());
    buffer.append("@" + System.identityHashCode(cacheProfile) + "[");
    buffer.append("cacheName="
        + Strings.quote(cacheProfile.getCacheName()) + ", ");
    buffer.append("group=" + Strings.quote(cacheProfile.getGroup()) + "]");

    String expected = buffer.toString();
    String actual = cacheProfile.toString();

    logger.debug("Expected 'toString': " + expected);
    logger.debug("Actual 'toString':   " + actual);

    assertEquals(expected, actual);
  }

  protected void setUp() throws Exception {
    super.setUp();
    cacheProfile = new JcsProfile();
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsHashCodeRelationship()
   */
  public void testEqualsHashCodeRelationship() {
    String cacheName = "main";
    String group = "test";

    cacheProfile.setCacheName(cacheName);
    cacheProfile.setGroup(group);

    JcsProfile anotherProfile = new JcsProfile(cacheName, group);

    EqualsHashCodeAssert.assertEqualsHashCodeRelationshipIsCorrect(
        cacheProfile, anotherProfile);

    cacheName = null;
    cacheProfile.setCacheName(cacheName);
    anotherProfile.setCacheName(cacheName);

    EqualsHashCodeAssert.assertEqualsHashCodeRelationshipIsCorrect(
        cacheProfile, anotherProfile);

    group = null;
    cacheProfile.setGroup(group);
    anotherProfile.setGroup(group);

    EqualsHashCodeAssert.assertEqualsHashCodeRelationshipIsCorrect(
        cacheProfile, anotherProfile);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsConsistent()
   */
  public void testEqualsIsConsistent() {
    String cacheName = "ch01";
    String group = "grp87";

    cacheProfile.setCacheName(cacheName);
    cacheProfile.setGroup(group);

    JcsProfile anotherProfile = new JcsProfile(cacheName, group);

    assertEquals(cacheProfile, anotherProfile);

    anotherProfile.setCacheName("main");
    assertFalse(cacheProfile.equals(anotherProfile));

    anotherProfile.setCacheName(cacheName);
    anotherProfile.setGroup("test");
    assertFalse(cacheProfile.equals(anotherProfile));
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsReflexive()
   */
  public void testEqualsIsReflexive() {
    EqualsHashCodeAssert.assertEqualsIsReflexive(cacheProfile);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsSymmetric()
   */
  public void testEqualsIsSymmetric() {
    String cacheName = "mainCache";
    String group = "testGroup";

    cacheProfile.setCacheName(cacheName);
    cacheProfile.setGroup(group);

    JcsProfile anotherProfile = new JcsProfile(cacheName, group);

    EqualsHashCodeAssert.assertEqualsIsSymmetric(cacheProfile,
        anotherProfile);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsTransitive()
   */
  public void testEqualsIsTransitive() {
    String cacheName = "pojos";
    String group = "model";

    cacheProfile.setCacheName(cacheName);
    cacheProfile.setGroup(group);

    JcsProfile secondProfile = new JcsProfile(cacheName, group);
    JcsProfile thirdProfile = new JcsProfile(cacheName, group);

    EqualsHashCodeAssert.assertEqualsIsTransitive(cacheProfile,
        secondProfile, thirdProfile);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsNullComparison()
   */
  public void testEqualsNullComparison() {
    EqualsHashCodeAssert
        .assertEqualsNullComparisonReturnsFalse(cacheProfile);
  }

  public void testToStringWithCacheNameAndGroupEqualToNull() {
    cacheProfile.setCacheName(null);
    cacheProfile.setGroup(null);

    assertToStringIsCorrect();
  }

  public void testToStringWithCacheNameAndGroupNotEqualToNull() {
    cacheProfile.setCacheName("main");
    cacheProfile.setGroup("services");

    assertToStringIsCorrect();
  }
}