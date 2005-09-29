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
import org.springmodules.EqualsHashCodeAssert;
import org.springmodules.EqualsHashCodeTestCase;
import org.springmodules.util.Strings;

/**
 * <p>
 * Unit Tests for <code>{@link Cached}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.14 $ $Date: 2005/09/29 01:22:06 $
 */
public final class CachedTests extends TestCase implements
    EqualsHashCodeTestCase {

  private static Log logger = LogFactory.getLog(CachedTests.class);

  private Cached cached;

  public CachedTests(String name) {
    super(name);
  }

  private void assertToStringIsCorrect() {
    StringBuffer buffer = new StringBuffer(cached.getClass().getName());
    buffer.append("@" + System.identityHashCode(cached) + "[");
    buffer.append("cacheModelId="
        + Strings.quote(cached.getCacheModelId()) + "]");

    String expected = buffer.toString();
    String actual = cached.toString();
    
    logger.debug("Expected 'toString': " + expected);
    logger.debug("Actual 'toString':   " + actual);
    
    assertEquals(expected, actual);
  }

  protected void setUp() throws Exception {
    super.setUp();

    cached = new Cached();
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsHashCodeRelationship()
   */
  public void testEqualsHashCodeRelationship() {
    String cacheModelId = "main";
    cached.setCacheModelId(cacheModelId);

    Cached anotherCached = new Cached(cacheModelId);

    EqualsHashCodeAssert.assertEqualsHashCodeRelationshipIsCorrect(cached,
        anotherCached);

    cached.setCacheModelId(null);
    anotherCached.setCacheModelId(null);

    EqualsHashCodeAssert.assertEqualsHashCodeRelationshipIsCorrect(cached,
        anotherCached);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsConsistent()
   */
  public void testEqualsIsConsistent() {
    String cacheModelId = "test";
    cached.setCacheModelId(cacheModelId);

    Cached anotherCached = new Cached(cacheModelId);
    assertEquals(cached, anotherCached);

    anotherCached.setCacheModelId("main");
    assertFalse(cached.equals(anotherCached));
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsReflexive()
   */
  public void testEqualsIsReflexive() {
    EqualsHashCodeAssert.assertEqualsIsReflexive(cached);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsSymmetric()
   */
  public void testEqualsIsSymmetric() {
    String cacheModelId = "test";
    cached.setCacheModelId(cacheModelId);

    Cached anotherCached = new Cached(cacheModelId);

    EqualsHashCodeAssert.assertEqualsIsSymmetric(cached, anotherCached);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsTransitive()
   */
  public void testEqualsIsTransitive() {
    String cacheModelId = "test";
    cached.setCacheModelId(cacheModelId);

    Cached secondCached = new Cached(cacheModelId);
    Cached thirdCached = new Cached(cacheModelId);

    EqualsHashCodeAssert.assertEqualsIsTransitive(cached, secondCached,
        thirdCached);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsNullComparison()
   */
  public void testEqualsNullComparison() {
    EqualsHashCodeAssert.assertEqualsNullComparisonReturnsFalse(cached);
  }

  public void testToString() {
    cached.setCacheModelId("main");
    assertToStringIsCorrect();
  }

  public void testToStringWithCacheModelIdEqualToNull() {
    cached.setCacheModelId(null);
    assertToStringIsCorrect();
  }
}