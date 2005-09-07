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
import org.springmodules.EqualsHashCodeAssert;
import org.springmodules.EqualsHashCodeTestCase;
import org.springmodules.cache.util.Strings;

/**
 * <p>
 * Unit Tests for <code>{@link EhCacheProfile}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.11 $ $Date: 2005/09/07 02:01:39 $
 */
public final class EhCacheProfileTests extends TestCase implements
    EqualsHashCodeTestCase {

  private static Log logger = LogFactory.getLog(EhCacheProfileTests.class);

  private EhCacheProfile cacheProfile;

  public EhCacheProfileTests(String name) {
    super(name);
  }

  
  private void assertToStringIsCorrect() {
    StringBuffer buffer = new StringBuffer(this.cacheProfile.getClass().getName());
    buffer.append("@" + System.identityHashCode(this.cacheProfile) + "[");
    buffer.append("cacheName="
        + Strings.quote(this.cacheProfile.getCacheName()) + "]");

    String expected = buffer.toString();
    String actual = this.cacheProfile.toString();
    
    logger.debug("Expected 'toString': " + expected);
    logger.debug("Actual 'toString':   " + actual);
    
    assertEquals(expected, actual);
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

    EqualsHashCodeAssert.assertEqualsHashCodeRelationshipIsCorrect(
        this.cacheProfile, anotherProfile);

    cacheName = null;
    this.cacheProfile.setCacheName(cacheName);
    anotherProfile.setCacheName(cacheName);

    EqualsHashCodeAssert.assertEqualsHashCodeRelationshipIsCorrect(
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
    EqualsHashCodeAssert.assertEqualsIsReflexive(this.cacheProfile);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsSymmetric()
   */
  public void testEqualsIsSymmetric() {
    String cacheName = "test";

    this.cacheProfile.setCacheName(cacheName);

    EhCacheProfile anotherProfile = new EhCacheProfile(cacheName);

    EqualsHashCodeAssert.assertEqualsIsSymmetric(this.cacheProfile,
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
  
  public void testToStringWithCacheNameEqualToNull() {
    this.cacheProfile.setCacheName(null);

    assertToStringIsCorrect();
  }

  public void testToStringWithCacheNameNotEqualToNull() {
    this.cacheProfile.setCacheName("main");

    assertToStringIsCorrect();
  }
}