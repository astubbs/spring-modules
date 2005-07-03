/* 
 * JcsCachingAttributeTest.java
 * 
 * Created on Sep 24, 2004
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

package org.springmodules.cache.provider.jcs;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springmodules.cache.AbstractJavaBeanTests;

/**
 * <p>
 * Unit Test for <code>{@link JcsProfile}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.4 $ $Date: 2005/07/03 04:33:11 $
 */
public final class JcsProfileTests extends AbstractJavaBeanTests {

  /**
   * Message logger.
   */
  private static Log logger = LogFactory.getLog(JcsProfileTests.class);

  /**
   * Primary object (instance of the class to test).
   */
  private JcsProfile cacheProfile;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the Test Case.
   */
  public JcsProfileTests(String name) {
    super(name);
  }

  /**
   * @see AbstractJavaBeanTests#getEqualObjects()
   */
  protected Object[] getEqualObjects() {
    JcsProfile equalProfile = new JcsProfile();
    equalProfile.setCacheName("main");
    equalProfile.setGroup("test");

    return new Object[] { equalProfile };
  }

  /**
   * @see AbstractJavaBeanTests#getExpectedHashCode()
   */
  protected int getExpectedHashCode() {
    int hash = 7;
    hash = 31 * hash + this.cacheProfile.getCacheName().hashCode();
    hash = 31 * hash + this.cacheProfile.getGroup().hashCode();
    return hash;
  }

  /**
   * @see AbstractJavaBeanTests#getExpectedToString()
   */
  protected String getExpectedToString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append(this.cacheProfile.getClass().getName() + ": ");
    buffer.append("cacheName='" + this.cacheProfile.getCacheName() + "'; ");
    buffer.append("group='" + this.cacheProfile.getGroup() + "'; ");
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
    JcsProfile notEqualProfile = new JcsProfile();
    notEqualProfile.setCacheName("temp");
    notEqualProfile.setGroup("dev");

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
  protected void setUp() throws Exception {
    super.setUp();

    this.cacheProfile = new JcsProfile();
    this.cacheProfile.setCacheName("main");
    this.cacheProfile.setGroup("test");
  }
}