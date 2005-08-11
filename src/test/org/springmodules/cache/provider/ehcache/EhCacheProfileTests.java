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
import org.springmodules.EqualsHashCodeTestCase;

/**
 * <p>
 * Unit Tests for <code>{@link EhCacheProfile}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.7 $ $Date: 2005/08/11 04:34:34 $
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

  private void assertEqualToString(String expected, String actual) {
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

    assertEquals(this.cacheProfile, anotherProfile);
    assertEquals(this.cacheProfile.hashCode(), anotherProfile.hashCode());
    
    cacheName = null;
    this.cacheProfile.setCacheName(cacheName);
    anotherProfile.setCacheName(cacheName);

    assertEquals(this.cacheProfile, anotherProfile);
    assertEquals(this.cacheProfile.hashCode(), anotherProfile.hashCode());
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
    assertEquals(this.cacheProfile, this.cacheProfile);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsSymmetric()
   */
  public void testEqualsIsSymmetric() {
    String cacheName = "test";

    this.cacheProfile.setCacheName(cacheName);

    EhCacheProfile anotherProfile = new EhCacheProfile(cacheName);

    assertTrue(this.cacheProfile.equals(anotherProfile));
    assertTrue(anotherProfile.equals(this.cacheProfile));
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsTransitive()
   */
  public void testEqualsIsTransitive() {
    String cacheName = "test";

    this.cacheProfile.setCacheName(cacheName);

    EhCacheProfile secondProfile = new EhCacheProfile(cacheName);
    EhCacheProfile thirdProfile = new EhCacheProfile(cacheName);

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

  public void testToStringWithCacheNameEqualToNull() {
    this.cacheProfile.setCacheName(null);

    StringBuffer buffer = new StringBuffer();
    buffer.append(this.cacheProfile.getClass().getName());
    buffer.append("@" + System.identityHashCode(this.cacheProfile) + "[");
    buffer.append("cacheName=null]");

    String expected = buffer.toString();
    String actual = this.cacheProfile.toString();

    logger.debug("Expected toString: " + expected);
    logger.debug("Actual toString:   " + actual);

    this.assertEqualToString(expected, actual);
  }

  public void testToStringWithCacheNameNotEqualToNull() {
    String cacheName = "main";
    this.cacheProfile.setCacheName(cacheName);

    StringBuffer buffer = new StringBuffer();
    buffer.append(this.cacheProfile.getClass().getName());
    buffer.append("@" + System.identityHashCode(this.cacheProfile) + "[");
    buffer.append("cacheName='" + cacheName + "']");

    String expected = buffer.toString();
    String actual = this.cacheProfile.toString();

    logger.debug("Expected toString: " + expected);
    logger.debug("Actual toString:   " + actual);

    this.assertEqualToString(expected, actual);
  }
}