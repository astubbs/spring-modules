/* 
 * Created on Oct 15, 2004
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
 * Copyright @2004 the original author or authors.
 */

package org.springmodules.cache.key;

import org.springmodules.cache.AbstractJavaBeanTests;

/**
 * <p>
 * Unit Test for <code>{@link HashCodeCacheKey}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/04/27 01:41:11 $
 */
public final class HashCodeCacheKeyTests extends AbstractJavaBeanTests {

  /**
   * Primary object (instance of the class to test).
   */
  private HashCodeCacheKey hashCodeCacheKey;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the Test Case.
   */
  public HashCodeCacheKeyTests(String name) {
    super(name);
  }

  /**
   * @see AbstractJavaBeanTests#getEqualObjects()
   */
  protected Object[] getEqualObjects() {
    HashCodeCacheKey equalHashCodeCacheKey = new HashCodeCacheKey();
    equalHashCodeCacheKey.setCheckSum(44322);
    equalHashCodeCacheKey.setHashCode(544);

    return new Object[] { equalHashCodeCacheKey };
  }

  /**
   * @see AbstractJavaBeanTests#getExpectedHashCode()
   */
  protected int getExpectedHashCode() {
    return this.hashCodeCacheKey.getHashCode();
  }

  /**
   * @see AbstractJavaBeanTests#getExpectedToString()
   */
  protected String getExpectedToString() {
    String expectedToString = this.hashCodeCacheKey.getHashCode() + "|"
        + this.hashCodeCacheKey.getCheckSum();

    return expectedToString;
  }

  /**
   * @see AbstractJavaBeanTests#getNotEqualObjects()
   */
  protected Object[] getNotEqualObjects() {
    HashCodeCacheKey nonEqualHashCodeCacheKey = new HashCodeCacheKey(4, 67);

    return new Object[] { nonEqualHashCodeCacheKey };
  }

  /**
   * @see AbstractJavaBeanTests#getPrimaryObject()
   */
  protected Object getPrimaryObject() {
    return this.hashCodeCacheKey;
  }

  /**
   * Sets up the test fixture.
   */
  protected void setUp() throws Exception {
    super.setUp();
    this.hashCodeCacheKey = new HashCodeCacheKey(44322, 544);
  }
}