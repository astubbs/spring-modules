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
import org.springmodules.AssertEqualsHashCode;
import org.springmodules.EqualsHashCodeTestCase;

/**
 * <p>
 * Unit Tests for <code>{@link FlushCache}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.8 $ $Date: 2005/08/11 04:31:41 $
 */
public final class FlushCacheTests extends TestCase implements
    EqualsHashCodeTestCase {

  private static Log logger = LogFactory.getLog(FlushCacheTests.class);

  /**
   * Primary object that is under test.
   */
  private FlushCache flushCache;

  public FlushCacheTests(String name) {
    super(name);
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

    AssertEqualsHashCode.assertEqualsHashCodeRelationshipIsCorrect(
        this.flushCache, anotherFlushCache);

    this.flushCache.setCacheProfileIds((String[]) null);
    anotherFlushCache.setCacheProfileIds((String[]) null);

    AssertEqualsHashCode.assertEqualsHashCodeRelationshipIsCorrect(
        this.flushCache, anotherFlushCache);

    String[] newCacheProfileIds = { null, "main" };
    this.flushCache.setCacheProfileIds(newCacheProfileIds);
    anotherFlushCache.setCacheProfileIds(newCacheProfileIds);

    AssertEqualsHashCode.assertEqualsHashCodeRelationshipIsCorrect(
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
    AssertEqualsHashCode.assertEqualsIsReflexive(this.flushCache);
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

    AssertEqualsHashCode.assertEqualsIsSymmetric(this.flushCache,
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

    AssertEqualsHashCode.assertEqualsIsTransitive(this.flushCache,
        secondFlushCache, thirdFlushCache);
  }

  /**
   * @see EqualsHashCodeTestCase#testEqualsNullComparison()
   */
  public void testEqualsNullComparison() {
    AssertEqualsHashCode
        .assertEqualsNullComparisonReturnsFalse(this.flushCache);
  }

  private void assertEqualToString(String expected) {
    String actual = this.flushCache.toString();

    logger.debug("Expected toString: " + expected);
    logger.debug("Actual toString:   " + actual);

    assertEquals("<ToString>", expected, actual);
  }

  public void testToStringWithCacheProfileIdEqualToNull() {
    String cacheProfileId = "main";
    String[] cacheProfileIds = { cacheProfileId, null };
    boolean flushBeforeExecution = true;

    this.flushCache.setCacheProfileIds(cacheProfileIds);
    this.flushCache.setFlushBeforeExecution(flushBeforeExecution);

    StringBuffer buffer = new StringBuffer();
    buffer.append(this.flushCache.getClass().getName());
    buffer.append("@" + System.identityHashCode(this.flushCache) + "[");
    buffer.append("cacheProfileIds={'" + cacheProfileId + "', null}, ");
    buffer.append("flushBeforeExecution=" + flushBeforeExecution + "]");

    String expected = buffer.toString();
    this.assertEqualToString(expected);
  }

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
    this.assertEqualToString(expected);
  }

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
    this.assertEqualToString(expected);
  }

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
    this.assertEqualToString(expected);
  }
}