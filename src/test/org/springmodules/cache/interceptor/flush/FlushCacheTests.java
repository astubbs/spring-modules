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
import org.springmodules.EqualsHashCodeAssert;
import org.springmodules.EqualsHashCodeTestCase;
import org.springmodules.cache.util.ArrayUtils;

/**
 * <p>
 * Unit Tests for <code>{@link FlushCache}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.11 $ $Date: 2005/09/07 02:01:43 $
 */
public final class FlushCacheTests extends TestCase implements
    EqualsHashCodeTestCase {

  private static Log logger = LogFactory.getLog(FlushCacheTests.class);

  private FlushCache flushCache;

  public FlushCacheTests(String name) {
    super(name);
  }

  private void assertToStringIsCorrect() {
    StringBuffer buffer = new StringBuffer(this.flushCache.getClass().getName());
    buffer.append("@" + System.identityHashCode(this.flushCache) + "[");
    buffer.append("cacheProfileIds="
        + ArrayUtils.toString(this.flushCache.getCacheProfileIds()) + ", ");
    buffer.append("flushBeforeExecution="
        + this.flushCache.isFlushBeforeExecution() + "]");

    String expected = buffer.toString();
    String actual = this.flushCache.toString();

    logger.debug("Expected 'toString': " + expected);
    logger.debug("Actual 'toString':   " + actual);

    assertEquals(expected, actual);
  }

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

    EqualsHashCodeAssert.assertEqualsHashCodeRelationshipIsCorrect(
        this.flushCache, anotherFlushCache);

    this.flushCache.setCacheProfileIds((String[]) null);
    anotherFlushCache.setCacheProfileIds((String[]) null);

    EqualsHashCodeAssert.assertEqualsHashCodeRelationshipIsCorrect(
        this.flushCache, anotherFlushCache);

    String[] newCacheProfileIds = { null, "main" };
    this.flushCache.setCacheProfileIds(newCacheProfileIds);
    anotherFlushCache.setCacheProfileIds(newCacheProfileIds);

    EqualsHashCodeAssert.assertEqualsHashCodeRelationshipIsCorrect(
        this.flushCache, anotherFlushCache);
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
    assertFalse(this.flushCache.equals(anotherFlushCache));

    anotherFlushCache.setCacheProfileIds(cacheProfileIds);
    anotherFlushCache.setFlushBeforeExecution(false);
    assertFalse(this.flushCache.equals(anotherFlushCache));
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsReflexive()
   */
  public void testEqualsIsReflexive() {
    EqualsHashCodeAssert.assertEqualsIsReflexive(this.flushCache);
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

    EqualsHashCodeAssert.assertEqualsIsSymmetric(this.flushCache,
        anotherFlushCache);
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

    EqualsHashCodeAssert.assertEqualsIsTransitive(this.flushCache,
        secondFlushCache, thirdFlushCache);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsNullComparison()
   */
  public void testEqualsNullComparison() {
    EqualsHashCodeAssert
        .assertEqualsNullComparisonReturnsFalse(this.flushCache);
  }

  public void testToStringWithCacheProfileIdEqualToNull() {
    this.flushCache.setCacheProfileIds(new String[] { "empire", null });
    this.flushCache.setFlushBeforeExecution(true);

    assertToStringIsCorrect();
  }

  public void testToStringWithCacheProfileIdsEqualToNull() {
    this.flushCache.setCacheProfileIds((String[]) null);
    this.flushCache.setFlushBeforeExecution(false);

    assertToStringIsCorrect();
  }

  public void testToStringWithEmptyCacheProfileIds() {
    this.flushCache.setCacheProfileIds(new String[0]);

    assertToStringIsCorrect();
  }

  public void testToStringWithNotEmptyCacheProfileIds() {
    this.flushCache.setCacheProfileIds(new String[] { "empire", "rebels" });
    assertToStringIsCorrect();
  }
}