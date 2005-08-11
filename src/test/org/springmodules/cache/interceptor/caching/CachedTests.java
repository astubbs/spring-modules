/* 
 * Created on Jan 17, 2005
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

package org.springmodules.cache.interceptor.caching;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springmodules.AssertEqualsHashCode;
import org.springmodules.EqualsHashCodeTestCase;

/**
 * <p>
 * Unit Tests for <code>{@link Cached}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.8 $ $Date: 2005/08/11 04:31:14 $
 */
public final class CachedTests extends TestCase implements
    EqualsHashCodeTestCase {

  private static Log logger = LogFactory.getLog(CachedTests.class);

  /**
   * Primary object under test.
   */
  private Cached cached;

  public CachedTests(String name) {
    super(name);
  }

  private void assertEqualToString(String expected) {
    String actual = this.cached.toString();

    logger.debug("Expected toString: " + expected);
    logger.debug("Actual toString:   " + actual);

    assertEquals("<ToString>", expected, actual);
  }

  protected void setUp() throws Exception {
    super.setUp();

    this.cached = new Cached();
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsHashCodeRelationship()
   */
  public void testEqualsHashCodeRelationship() {
    String cacheProfileId = "main";
    this.cached.setCacheProfileId(cacheProfileId);

    Cached anotherCached = new Cached(cacheProfileId);

    AssertEqualsHashCode.assertEqualsHashCodeRelationshipIsCorrect(this.cached,
        anotherCached);

    this.cached.setCacheProfileId(null);
    anotherCached.setCacheProfileId(null);

    AssertEqualsHashCode.assertEqualsHashCodeRelationshipIsCorrect(this.cached,
        anotherCached);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsConsistent()
   */
  public void testEqualsIsConsistent() {
    String cacheProfileId = "test";
    this.cached.setCacheProfileId(cacheProfileId);

    Cached anotherCached = new Cached(cacheProfileId);
    assertEquals(this.cached, anotherCached);

    anotherCached.setCacheProfileId("main");
    assertFalse(this.cached.equals(anotherCached));
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsReflexive()
   */
  public void testEqualsIsReflexive() {
    AssertEqualsHashCode.assertEqualsIsReflexive(this.cached);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsSymmetric()
   */
  public void testEqualsIsSymmetric() {
    String cacheProfileId = "test";
    this.cached.setCacheProfileId(cacheProfileId);

    Cached anotherCached = new Cached(cacheProfileId);

    AssertEqualsHashCode.assertEqualsIsSymmetric(this.cached, anotherCached);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsTransitive()
   */
  public void testEqualsIsTransitive() {
    String cacheProfileId = "test";
    this.cached.setCacheProfileId(cacheProfileId);

    Cached secondCached = new Cached(cacheProfileId);
    Cached thirdCached = new Cached(cacheProfileId);

    AssertEqualsHashCode.assertEqualsIsTransitive(this.cached, secondCached,
        thirdCached);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsNullComparison()
   */
  public void testEqualsNullComparison() {
    AssertEqualsHashCode.assertEqualsNullComparisonReturnsFalse(this.cached);
  }

  public void testToStringWithCacheProfileIdEqualToNull() {
    this.cached.setCacheProfileId(null);

    StringBuffer buffer = new StringBuffer();
    buffer.append(this.cached.getClass().getName());
    buffer.append("@" + System.identityHashCode(this.cached) + "[");
    buffer.append("cacheProfileId=null]");

    String expected = buffer.toString();
    this.assertEqualToString(expected);
  }

  public void testToStringWithCacheProfileIdNotEqualToNull() {
    String cacheProfileId = "main";
    this.cached.setCacheProfileId(cacheProfileId);

    StringBuffer buffer = new StringBuffer();
    buffer.append(this.cached.getClass().getName());
    buffer.append("@" + System.identityHashCode(this.cached) + "[");
    buffer.append("cacheProfileId='");
    buffer.append(cacheProfileId);
    buffer.append("']");

    String expected = buffer.toString();
    this.assertEqualToString(expected);
  }
}