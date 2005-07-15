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
import org.springmodules.EqualsHashCodeTestCase;

/**
 * <p>
 * Unit Tests for <code>{@link Cached}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.5 $ $Date: 2005/07/15 18:03:58 $
 */
public final class CachedTests extends TestCase implements
    EqualsHashCodeTestCase {

  /**
   * Message logger.
   */
  private static Log logger = LogFactory.getLog(CachedTests.class);

  /**
   * Instance of the class to test.
   */
  private Cached cached;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the Test Case.
   */
  public CachedTests(String name) {
    super(name);
  }

  /**
   * Sets up the test fixture.
   */
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

    assertEquals(this.cached, anotherCached);
    assertEquals(this.cached.hashCode(), anotherCached.hashCode());
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
    assertEquals(this.cached, this.cached);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsSymmetric()
   */
  public void testEqualsIsSymmetric() {
    String cacheProfileId = "test";
    this.cached.setCacheProfileId(cacheProfileId);

    Cached anotherCached = new Cached(cacheProfileId);

    assertTrue(this.cached.equals(anotherCached));
    assertTrue(anotherCached.equals(this.cached));
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsTransitive()
   */
  public void testEqualsIsTransitive() {
    String cacheProfileId = "test";
    this.cached.setCacheProfileId(cacheProfileId);

    Cached secondCached = new Cached(cacheProfileId);
    Cached thirdCached = new Cached(cacheProfileId);

    assertTrue(this.cached.equals(secondCached));
    assertTrue(secondCached.equals(thirdCached));
    assertTrue(this.cached.equals(thirdCached));
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsNullComparison()
   */
  public void testEqualsNullComparison() {
    assertFalse(this.cached.equals(null));
  }

  /**
   * Verifies that the method <code>{@link Cached#toString()}</code> returns a
   * String representation of a <code>{@link Cached}</code> when the property
   * <code>cacheProfileId</code> is equal to <code>null</code>.
   */
  public void testToStringWithCacheProfileIdEqualToNull() {
    this.cached.setCacheProfileId(null);
    
    StringBuffer buffer = new StringBuffer();
    buffer.append(this.cached.getClass().getName());
    buffer.append("@" + System.identityHashCode(this.cached) + "[");
    buffer.append("cacheProfileId=null]");

    String expected = buffer.toString();
    String actual = this.cached.toString();

    logger.debug("Expected toString: " + expected);
    logger.debug("Actual toString:   " + actual);

    assertEquals("<ToString>", expected, actual);
  }

  /**
   * Verifies that the method <code>{@link Cached#toString()}</code> returns a
   * String representation of a <code>{@link Cached}</code> when the property
   * <code>cacheProfileId</code> is not equal to <code>null</code>.
   */
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
    String actual = this.cached.toString();

    logger.debug("Expected toString: " + expected);
    logger.debug("Actual toString:   " + actual);

    assertEquals("<ToString>", expected, actual);
  }
}