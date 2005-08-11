/* 
 * Created on Oct 29, 2004
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

package org.springmodules.cache.provider.ehcache;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springmodules.AssertEqualsHashCode;
import org.springmodules.EqualsHashCodeTestCase;

/**
 * <p>
 * Unit Tests for <code>{@link EhCacheProfile}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.8 $ $Date: 2005/08/11 04:48:05 $
 */
public final class EhCacheProfileTests extends TestCase implements
    EqualsHashCodeTestCase {

  private static Log logger = LogFactory.getLog(EhCacheProfileTests.class);

  /**
   * Primary object that is under test.
   */
  private EhCacheProfile cacheProfile;

  public EhCacheProfileTests(String name) {
    super(name);
  }

  private void assertEqualToString(String expected) {
    String actual = this.cacheProfile.toString();

    logger.debug("Expected toString: " + expected);
    logger.debug("Actual toString:   " + actual);

    assertEquals("<ToString>", expected, actual);
  }

  protected final void setUp() throws Exception {
    super.setUp();

    this.cacheProfile = new EhCacheProfile();
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsHashCodeRelationship()
   */
  public void testEqualsHashCodeRelationship() {
    String cacheName = "main";

    this.cacheProfile.setCacheName(cacheName);

    EhCacheProfile anotherProfile = new EhCacheProfile(cacheName);

    AssertEqualsHashCode.assertEqualsHashCodeRelationshipIsCorrect(
        this.cacheProfile, anotherProfile);

    cacheName = null;
    this.cacheProfile.setCacheName(cacheName);
    anotherProfile.setCacheName(cacheName);

    AssertEqualsHashCode.assertEqualsHashCodeRelationshipIsCorrect(
        this.cacheProfile, anotherProfile);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsConsistent()
   */
  public void testEqualsIsConsistent() {
    String cacheName = "test";

    this.cacheProfile.setCacheName(cacheName);

    EhCacheProfile anotherProfile = new EhCacheProfile(cacheName);

    assertEquals(this.cacheProfile, anotherProfile);

    anotherProfile.setCacheName("main");
    assertFalse(this.cacheProfile.equals(anotherProfile));
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsReflexive()
   */
  public void testEqualsIsReflexive() {
    AssertEqualsHashCode.assertEqualsIsReflexive(this.cacheProfile);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsSymmetric()
   */
  public void testEqualsIsSymmetric() {
    String cacheName = "test";

    this.cacheProfile.setCacheName(cacheName);

    EhCacheProfile anotherProfile = new EhCacheProfile(cacheName);

    AssertEqualsHashCode.assertEqualsIsSymmetric(this.cacheProfile,
        anotherProfile);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsTransitive()
   */
  public void testEqualsIsTransitive() {
    String cacheName = "test";

    this.cacheProfile.setCacheName(cacheName);

    EhCacheProfile secondProfile = new EhCacheProfile(cacheName);
    EhCacheProfile thirdProfile = new EhCacheProfile(cacheName);

    AssertEqualsHashCode.assertEqualsIsTransitive(this.cacheProfile,
        secondProfile, thirdProfile);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsNullComparison()
   */
  public void testEqualsNullComparison() {
    AssertEqualsHashCode
        .assertEqualsNullComparisonReturnsFalse(this.cacheProfile);
  }

  public void testToStringWithCacheNameEqualToNull() {
    this.cacheProfile.setCacheName(null);

    StringBuffer buffer = new StringBuffer();
    buffer.append(this.cacheProfile.getClass().getName());
    buffer.append("@" + System.identityHashCode(this.cacheProfile) + "[");
    buffer.append("cacheName=null]");

    String expected = buffer.toString();
    this.assertEqualToString(expected);
  }

  public void testToStringWithCacheNameNotEqualToNull() {
    String cacheName = "main";
    this.cacheProfile.setCacheName(cacheName);

    StringBuffer buffer = new StringBuffer();
    buffer.append(this.cacheProfile.getClass().getName());
    buffer.append("@" + System.identityHashCode(this.cacheProfile) + "[");
    buffer.append("cacheName='" + cacheName + "']");

    String expected = buffer.toString();
    this.assertEqualToString(expected);
  }
}