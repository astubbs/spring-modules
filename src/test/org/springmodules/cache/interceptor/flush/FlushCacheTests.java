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
import org.springmodules.util.ArrayUtils;

/**
 * <p>
 * Unit Tests for <code>{@link FlushCache}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.13 $ $Date: 2005/09/29 01:21:41 $
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
    buffer.append("cacheModelIds="
        + ArrayUtils.toString(this.flushCache.getCacheModelIds()) + ", ");
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
    this.flushCache.setCacheModelIds("main,test");
    this.flushCache.setFlushBeforeExecution(true);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsHashCodeRelationship()
   */
  public void testEqualsHashCodeRelationship() {
    String cacheModelIds = "main,test";
    boolean flushBeforeExecution = true;

    this.flushCache.setCacheModelIds(cacheModelIds);
    this.flushCache.setFlushBeforeExecution(flushBeforeExecution);

    FlushCache anotherFlushCache = new FlushCache(cacheModelIds,
        flushBeforeExecution);

    EqualsHashCodeAssert.assertEqualsHashCodeRelationshipIsCorrect(
        this.flushCache, anotherFlushCache);

    this.flushCache.setCacheModelIds((String[]) null);
    anotherFlushCache.setCacheModelIds((String[]) null);

    EqualsHashCodeAssert.assertEqualsHashCodeRelationshipIsCorrect(
        this.flushCache, anotherFlushCache);

    String[] newCacheModelIds = { null, "main" };
    this.flushCache.setCacheModelIds(newCacheModelIds);
    anotherFlushCache.setCacheModelIds(newCacheModelIds);

    EqualsHashCodeAssert.assertEqualsHashCodeRelationshipIsCorrect(
        this.flushCache, anotherFlushCache);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsConsistent()
   */
  public void testEqualsIsConsistent() {
    String cacheModelIds = "main,test";
    boolean flushBeforeExecution = true;

    this.flushCache.setCacheModelIds(cacheModelIds);
    this.flushCache.setFlushBeforeExecution(flushBeforeExecution);

    FlushCache anotherFlushCache = new FlushCache(cacheModelIds,
        flushBeforeExecution);

    assertEquals(this.flushCache, anotherFlushCache);

    anotherFlushCache.setCacheModelIds("main");
    assertFalse(this.flushCache.equals(anotherFlushCache));

    anotherFlushCache.setCacheModelIds(cacheModelIds);
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
    String cacheModelIds = "main,test";
    boolean flushBeforeExecution = true;

    this.flushCache.setCacheModelIds(cacheModelIds);
    this.flushCache.setFlushBeforeExecution(flushBeforeExecution);

    FlushCache anotherFlushCache = new FlushCache(cacheModelIds,
        flushBeforeExecution);

    EqualsHashCodeAssert.assertEqualsIsSymmetric(this.flushCache,
        anotherFlushCache);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsIsTransitive()
   */
  public void testEqualsIsTransitive() {
    String cacheModelIds = "main,test";
    boolean flushBeforeExecution = true;

    this.flushCache.setCacheModelIds(cacheModelIds);
    this.flushCache.setFlushBeforeExecution(flushBeforeExecution);

    FlushCache secondFlushCache = new FlushCache(cacheModelIds,
        flushBeforeExecution);
    FlushCache thirdFlushCache = new FlushCache(cacheModelIds,
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

  public void testToStringWithCacheModelIdEqualToNull() {
    this.flushCache.setCacheModelIds(new String[] { "empire", null });
    this.flushCache.setFlushBeforeExecution(true);

    assertToStringIsCorrect();
  }

  public void testToStringWithCacheModelIdsEqualToNull() {
    this.flushCache.setCacheModelIds((String[]) null);
    this.flushCache.setFlushBeforeExecution(false);

    assertToStringIsCorrect();
  }

  public void testToStringWithEmptyCacheModelIds() {
    this.flushCache.setCacheModelIds(new String[0]);

    assertToStringIsCorrect();
  }

  public void testToStringWithNotEmptyCacheModelIds() {
    this.flushCache.setCacheModelIds(new String[] { "empire", "rebels" });
    assertToStringIsCorrect();
  }
}