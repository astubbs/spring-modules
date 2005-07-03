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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springmodules.cache.AbstractJavaBeanTests;

/**
 * <p>
 * Unit Test for <code>{@link Cached}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.4 $ $Date: 2005/07/03 04:33:13 $
 */
public final class CachedTests extends AbstractJavaBeanTests {

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
   * @see AbstractJavaBeanTests#getEqualObjects()
   */
  protected Object[] getEqualObjects() {
    Cached equalCached = new Cached();
    equalCached.setCacheProfileId("main");

    return new Object[] { equalCached };
  }

  /**
   * @see AbstractJavaBeanTests#getExpectedHashCode()
   */
  protected int getExpectedHashCode() {
    int multiplier = 31;
    int hash = 7;
    hash = multiplier * hash + (this.cached.getCacheProfileId().hashCode());
    return hash;
  }

  /**
   * @see AbstractJavaBeanTests#getExpectedToString()
   */
  protected String getExpectedToString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append(this.cached.getClass().getName() + ": ");
    buffer.append("cacheProfileId='" + this.cached.getCacheProfileId() + "'; ");
    buffer.append("systemHashCode=" + System.identityHashCode(this.cached));
    
    String expectedToString = buffer.toString();
    logger.debug("expectedToString: " + expectedToString);
    
    return expectedToString;
  }

  /**
   * @see AbstractJavaBeanTests#getNotEqualObjects()
   */
  protected Object[] getNotEqualObjects() {
    Cached notEqualCached = new Cached();
    notEqualCached.setCacheProfileId("test");

    return new Object[] { notEqualCached };
  }

  /**
   * @see AbstractJavaBeanTests#getPrimaryObject()
   */
  protected Object getPrimaryObject() {
    return this.cached;
  }

  /**
   * Sets up the test fixture.
   */
  protected void setUp() throws Exception {
    super.setUp();

    this.cached = new Cached();
    this.cached.setCacheProfileId("main");
  }
}