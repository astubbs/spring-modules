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

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springmodules.cache.AbstractJavaBeanTests;

/**
 * <p>
 * Unit Test for <code>{@link FlushCache}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/04/27 01:41:04 $
 */
public final class FlushCacheTests extends AbstractJavaBeanTests {

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
    HashCodeBuilder hashCodeBuilder = new HashCodeBuilder(5, 7);
    hashCodeBuilder.append(this.flushCache.getCacheProfileIds());
    hashCodeBuilder.append(this.flushCache.isFlushBeforeExecution());

    int expectedHashCode = hashCodeBuilder.toHashCode();
    return expectedHashCode;
  }

  /**
   * @see AbstractJavaBeanTests#getExpectedToString()
   */
  protected String getExpectedToString() {
    ToStringBuilder toStringBuilder = new ToStringBuilder(this.flushCache);
    toStringBuilder.append("cacheProfileIds", this.flushCache
        .getCacheProfileIds());
    toStringBuilder.append("flushedBeforeExecution", this.flushCache
        .isFlushBeforeExecution());

    String expectedToString = toStringBuilder.toString();
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

}