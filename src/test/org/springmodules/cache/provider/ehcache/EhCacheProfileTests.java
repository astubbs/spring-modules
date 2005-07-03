/* 
 * Created on Oct 29, 2004
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

package org.springmodules.cache.provider.ehcache;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springmodules.cache.AbstractJavaBeanTests;

/**
 * <p>
 * Unit Test for <code>{@link EhCacheProfile}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.4 $ $Date: 2005/07/03 04:33:12 $
 */
public final class EhCacheProfileTests extends AbstractJavaBeanTests {

  /**
   * Message logger.
   */
  private static Log logger = LogFactory.getLog(EhCacheProfileTests.class);

  /**
   * Primary object (instance of the class to test).
   */
  private EhCacheProfile cacheProfile;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the Test Case.
   */
  public EhCacheProfileTests(String name) {
    super(name);
  }

  /**
   * @see AbstractJavaBeanTests#getEqualObjects()
   */
  protected Object[] getEqualObjects() {
    EhCacheProfile equalProfile = new EhCacheProfile();
    equalProfile.setCacheName("main");

    return new Object[] { equalProfile };
  }

  /**
   * @see AbstractJavaBeanTests#getExpectedHashCode()
   */
  protected int getExpectedHashCode() {
    int multiplier = 31;
    int hash = 7;
    hash = multiplier * hash + this.cacheProfile.getCacheName().hashCode();
    return hash;
  }

  /**
   * @see AbstractJavaBeanTests#getExpectedToString()
   */
  protected String getExpectedToString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append(this.cacheProfile.getClass().getName() + ": ");
    buffer.append("cacheName='" + this.cacheProfile.getCacheName() + "'; ");
    buffer.append("systemHashCode="
        + System.identityHashCode(this.cacheProfile));

    String expectedToString = buffer.toString();
    logger.debug("expectedToString: " + expectedToString);

    return expectedToString;
  }

  /**
   * @see AbstractJavaBeanTests#getNotEqualObjects()
   */
  protected Object[] getNotEqualObjects() {
    EhCacheProfile notEqualProfile = new EhCacheProfile();
    notEqualProfile.setCacheName("temp");

    return new Object[] { notEqualProfile };
  }

  /**
   * @see AbstractJavaBeanTests#getPrimaryObject()
   */
  protected Object getPrimaryObject() {
    return this.cacheProfile;
  }

  /**
   * Sets up the test fixture.
   */
  protected final void setUp() throws Exception {
    super.setUp();

    this.cacheProfile = new EhCacheProfile();
    this.cacheProfile.setCacheName("main");
  }
}