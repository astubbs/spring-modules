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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springmodules.cache.AbstractJavaBeanTests;

/**
 * <p>
 * Unit Test for <code>{@link FlushCache}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.5 $ $Date: 2005/07/03 04:33:13 $
 */
public final class FlushCacheTests extends AbstractJavaBeanTests {

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
   *          the name of the Test Case.
   */
  public FlushCacheTests(String name) {
    super(name);
  }

  /**
   * @see AbstractJavaBeanTests#getEqualObjects()
   */
  protected Object[] getEqualObjects() {
    String[] cacheProfileIds = new String[] { "main", "test" };
    FlushCache equalFlushCache = new FlushCache(cacheProfileIds, true);

    return new Object[] { equalFlushCache };
  }

  /**
   * @see AbstractJavaBeanTests#getExpectedHashCode()
   */
  protected int getExpectedHashCode() {
    int multiplier = 31;
    int hash = 7;
    hash = multiplier * hash + 1;

    String[] cacheProfileIds = this.flushCache.getCacheProfileIds();
    int cacheProfileIdCount = cacheProfileIds.length;
    for (int i = 0; i < cacheProfileIdCount; i++) {
      String cacheProfileId = cacheProfileIds[i];
      hash = multiplier * hash
          + (cacheProfileId != null ? cacheProfileId.hashCode() : 0);
    }

    return hash;
  }

  /**
   * @see AbstractJavaBeanTests#getExpectedToString()
   */
  protected String getExpectedToString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append(this.flushCache.getClass().getName() + ": ");
    buffer.append("cacheProfileIds=");

    String[] cacheProfileIds = this.flushCache.getCacheProfileIds();
    int cacheProfileIdCount = cacheProfileIds.length;
    for (int i = 0; i < cacheProfileIdCount; i++) {
      if (i == 0) {
        buffer.append('[');
      } else {
        buffer.append(", ");
      }

      buffer.append("'" + cacheProfileIds[i] + "'");
    }
    buffer.append("]; ");

    buffer.append("flushBeforeExecution="
        + this.flushCache.isFlushBeforeExecution() + "; ");
    buffer.append("systemHashCode=" + System.identityHashCode(this.flushCache));

    String expectedToString = buffer.toString();
    logger.debug("expectedToString: " + expectedToString);

    return expectedToString;
  }

  /**
   * @see AbstractJavaBeanTests#getNotEqualObjects()
   */
  protected Object[] getNotEqualObjects() {
    FlushCache notEqualFlushCache = new FlushCache("test");

    return new Object[] { notEqualFlushCache };
  }

  /**
   * @see AbstractJavaBeanTests#getPrimaryObject()
   */
  protected Object getPrimaryObject() {
    return this.flushCache;
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
   * Verifies that the method <code>{@link FlushCache#toString()}</code>
   * creates a correct String representation of a
   * <code>{@link FlushCache}</code> if the array of cache profile ids is
   * <code>null</code>.
   */
  public void testToStringWithCacheProfileIdsEqualToNull() {
    this.flushCache.setCacheProfileIds((String[]) null);

    StringBuffer buffer = new StringBuffer();
    buffer.append(this.flushCache.getClass().getName() + ": ");
    buffer.append("cacheProfileIds=null; ");
    buffer.append("flushBeforeExecution="
        + this.flushCache.isFlushBeforeExecution() + "; ");
    buffer.append("systemHashCode=" + System.identityHashCode(this.flushCache));

    String expectedToString = buffer.toString();
    logger.debug("expectedToString: " + expectedToString);

    String actualToString = this.flushCache.toString();

    assertEquals("<toString>", expectedToString, actualToString);
  }

  /**
   * Verifies that the method <code>{@link FlushCache#toString()}</code>
   * creates a correct String representation of a
   * <code>{@link FlushCache}</code> if the array of cache profile ids is
   * empty.
   */
  public void testToStringWithEmptyCacheProfileIds() {
    this.flushCache.setCacheProfileIds(new String[0]);

    StringBuffer buffer = new StringBuffer();
    buffer.append(this.flushCache.getClass().getName() + ": ");
    buffer.append("cacheProfileIds=[]; ");
    buffer.append("flushBeforeExecution="
        + this.flushCache.isFlushBeforeExecution() + "; ");
    buffer.append("systemHashCode=" + System.identityHashCode(this.flushCache));

    String expectedToString = buffer.toString();
    logger.debug("expectedToString: " + expectedToString);

    String actualToString = this.flushCache.toString();

    assertEquals("<toString>", expectedToString, actualToString);
  }
}