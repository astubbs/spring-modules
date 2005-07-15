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

package org.springmodules.cache.interceptor.flush;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springmodules.EqualsHashCodeTestCase;

/**
 * <p>
 * Unit Test for <code>{@link FlushCache}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.6 $ $Date: 2005/07/15 18:03:59 $
 */
public final class FlushCacheTests extends TestCase implements
    EqualsHashCodeTestCase {

  /**
   * Message logger.
   */
  private static Log logger = LogFactory.getLog(FlushCacheTests.class);

  /**
   * Primary object (instance of the class to test).
   */
  private FlushCache flushCache;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the Test Case to construct.
   */
  public FlushCacheTests(String name) {
    super(name);
  }

  /**
   * Sets up the test fixture.
   */
  protected void setUp() throws Exception {
    super.setUp();

    this.flushCache = new FlushCache();
    this.flushCache.setCacheProfileIds("main,test");
    this.flushCache.setFlushBeforeExecution(true);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsHashCodeRelationship()
   */
  public void testEqualsHashCodeRelationship() {
    String cacheProfileIds = "main,test";
    boolean flushBeforeExecution = true;

    this.flushCache.setCacheProfileIds(cacheProfileIds);
    this.flushCache.setFlushBeforeExecution(flushBeforeExecution);

    FlushCache anotherFlushCache = new FlushCache(cacheProfileIds,
        flushBeforeExecution);

    assertEquals(this.flushCache, anotherFlushCache);
    assertEquals(this.flushCache.hashCode(), anotherFlushCache.hashCode());
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsConsistent()
   */
  public void testEqualsIsConsistent() {
    String cacheProfileIds = "main,test";
    boolean flushBeforeExecution = true;

    this.flushCache.setCacheProfileIds(cacheProfileIds);
    this.flushCache.setFlushBeforeExecution(flushBeforeExecution);

    FlushCache anotherFlushCache = new FlushCache(cacheProfileIds,
        flushBeforeExecution);

    assertEquals(this.flushCache, anotherFlushCache);

    anotherFlushCache.setCacheProfileIds("main");
    anotherFlushCache.setFlushBeforeExecution(false);

    assertFalse(this.flushCache.equals(anotherFlushCache));
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsReflexive()
   */
  public void testEqualsIsReflexive() {
    assertEquals(this.flushCache, this.flushCache);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsSymmetric()
   */
  public void testEqualsIsSymmetric() {
    String cacheProfileIds = "main,test";
    boolean flushBeforeExecution = true;

    this.flushCache.setCacheProfileIds(cacheProfileIds);
    this.flushCache.setFlushBeforeExecution(flushBeforeExecution);

    FlushCache anotherFlushCache = new FlushCache(cacheProfileIds,
        flushBeforeExecution);

    assertTrue(this.flushCache.equals(anotherFlushCache));
    assertTrue(anotherFlushCache.equals(this.flushCache));
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsTransitive()
   */
  public void testEqualsIsTransitive() {
    String cacheProfileIds = "main,test";
    boolean flushBeforeExecution = true;

    this.flushCache.setCacheProfileIds(cacheProfileIds);
    this.flushCache.setFlushBeforeExecution(flushBeforeExecution);

    FlushCache secondFlushCache = new FlushCache(cacheProfileIds,
        flushBeforeExecution);
    FlushCache thirdFlushCache = new FlushCache(cacheProfileIds,
        flushBeforeExecution);

    assertTrue(this.flushCache.equals(secondFlushCache));
    assertTrue(secondFlushCache.equals(thirdFlushCache));
    assertTrue(this.flushCache.equals(thirdFlushCache));
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsNullComparison()
   */
  public void testEqualsNullComparison() {
    assertFalse(this.flushCache.equals(null));
  }

  /**
   * Verifies that the method <code>{@link FlushCache#toString()}</code>
   * returns a String representation of a <code>{@link FlushCache}</code> when
   * the property <code>cacheProfileIds</code> is equal to <code>null</code>.
   */
  public void testToStringWithCacheProfileIdsEqualToNull() {
    String cacheProfileIds = null;
    boolean flushBeforeExecution = true;

    this.flushCache.setCacheProfileIds(cacheProfileIds);
    this.flushCache.setFlushBeforeExecution(flushBeforeExecution);

    StringBuffer buffer = new StringBuffer();
    buffer.append(this.flushCache.getClass().getName());
    buffer.append("@" + System.identityHashCode(this.flushCache) + "[");
    buffer.append("cacheProfileIds=null, ");
    buffer.append("flushBeforeExecution=" + flushBeforeExecution + "]");

    String expected = buffer.toString();
    String actual = this.flushCache.toString();

    logger.debug("Expected toString: " + expected);
    logger.debug("Actual toString:   " + actual);

    assertEquals("<ToString>", expected, actual);
  }

  /**
   * Verifies that the method <code>{@link FlushCache#toString()}</code>
   * returns a String representation of a <code>{@link FlushCache}</code> when
   * the property <code>cacheProfileIds</code> is an empty array.
   */
  public void testToStringWithEmptyCacheProfileIds() {
    boolean flushBeforeExecution = true;

    this.flushCache.setCacheProfileIds(new String[0]);
    this.flushCache.setFlushBeforeExecution(flushBeforeExecution);

    StringBuffer buffer = new StringBuffer();
    buffer.append(this.flushCache.getClass().getName());
    buffer.append("@" + System.identityHashCode(this.flushCache) + "[");
    buffer.append("cacheProfileIds={}, ");
    buffer.append("flushBeforeExecution=" + flushBeforeExecution + "]");

    String expected = buffer.toString();
    String actual = this.flushCache.toString();

    logger.debug("Expected toString: " + expected);
    logger.debug("Actual toString:   " + actual);

    assertEquals("<ToString>", expected, actual);
  }

  /**
   * Verifies that the method <code>{@link FlushCache#toString()}</code>
   * returns a String representation of a <code>{@link FlushCache}</code> when
   * the property <code>cacheProfileIds</code> is not equal to
   * <code>null</code> and is not an empty array.
   */
  public void testToStringWithNotEmptyCacheProfileIds() {
    String[] cacheProfileIds = { "main", "test" };
    boolean flushBeforeExecution = true;

    this.flushCache.setCacheProfileIds(cacheProfileIds);
    this.flushCache.setFlushBeforeExecution(flushBeforeExecution);

    StringBuffer buffer = new StringBuffer();
    buffer.append(this.flushCache.getClass().getName());
    buffer.append("@" + System.identityHashCode(this.flushCache) + "[");
    buffer.append("cacheProfileIds=");
    int cacheProfileIdCount = cacheProfileIds.length;
    for (int i = 0; i < cacheProfileIdCount; i++) {
      if (i == 0) {
        buffer.append("{");
      } else {
        buffer.append(", ");
      }
      buffer.append("'" + cacheProfileIds[i] + "'");
    }
    buffer.append("}, ");
    buffer.append("flushBeforeExecution=" + flushBeforeExecution + "]");

    String expected = buffer.toString();
    String actual = this.flushCache.toString();

    logger.debug("Expected toString: " + expected);
    logger.debug("Actual toString:   " + actual);

    assertEquals("<ToString>", expected, actual);
  }
}