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
 * @version $Revision: 1.12 $ $Date: 2005/09/09 02:19:11 $
 */
public final class EhCacheProfileTests extends TestCase implements
    EqualsHashCodeTestCase {

  private static Log logger = LogFactory.getLog(EhCacheProfileTests.class);

  private EhCacheProfile cacheProfile;

  public EhCacheProfileTests(String name) {
    super(name);
  }

  private void assertToStringIsCorrect() {
    StringBuffer buffer = new StringBuffer(cacheProfile.getClass().getName());
    buffer.append("@" + System.identityHashCode(cacheProfile) + "[");
    buffer.append("cacheName=" + Strings.quote(cacheProfile.getCacheName())
        + "]");

    String expected = buffer.toString();
    String actual = cacheProfile.toString();

    logger.debug("Expected 'toString': " + expected);
    logger.debug("Actual 'toString':   " + actual);

    assertEquals(expected, actual);
  }

  protected final void setUp() throws Exception {
    super.setUp();

    cacheProfile = new EhCacheProfile();
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsHashCodeRelationship()
   */
  public void testEqualsHashCodeRelationship() {
    String cacheName = "main";

    cacheProfile.setCacheName(cacheName);

    EhCacheProfile anotherProfile = new EhCacheProfile(cacheName);

    EqualsHashCodeAssert.assertEqualsHashCodeRelationshipIsCorrect(
        cacheProfile, anotherProfile);

    cacheName = null;
    cacheProfile.setCacheName(cacheName);
    anotherProfile.setCacheName(cacheName);

    EqualsHashCodeAssert.assertEqualsHashCodeRelationshipIsCorrect(
        cacheProfile, anotherProfile);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsConsistent()
   */
  public void testEqualsIsConsistent() {
    String cacheName = "test";

    cacheProfile.setCacheName(cacheName);

    EhCacheProfile anotherProfile = new EhCacheProfile(cacheName);

    assertEquals(cacheProfile, anotherProfile);

    anotherProfile.setCacheName("main");
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
    String cacheName = "test";

    cacheProfile.setCacheName(cacheName);

    EhCacheProfile anotherProfile = new EhCacheProfile(cacheName);

    EqualsHashCodeAssert.assertEqualsIsSymmetric(cacheProfile, anotherProfile);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsTransitive()
   */
  public void testEqualsIsTransitive() {
    String cacheName = "test";

    cacheProfile.setCacheName(cacheName);

    EhCacheProfile secondProfile = new EhCacheProfile(cacheName);
    EhCacheProfile thirdProfile = new EhCacheProfile(cacheName);

    EqualsHashCodeAssert.assertEqualsIsTransitive(cacheProfile, secondProfile,
        thirdProfile);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsNullComparison()
   */
  public void testEqualsNullComparison() {
    EqualsHashCodeAssert.assertEqualsNullComparisonReturnsFalse(cacheProfile);
  }

  public void testToStringWithCacheNameEqualToNull() {
    cacheProfile.setCacheName(null);

    assertToStringIsCorrect();
  }

  public void testToStringWithCacheNameNotEqualToNull() {
    cacheProfile.setCacheName("main");

    assertToStringIsCorrect();
  }
}