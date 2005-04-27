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

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springmodules.cache.AbstractJavaBeanTests;

/**
 * <p>
 * Unit Test for <code>{@link JcsCacheProfile}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/04/27 01:41:03 $
 */
public final class JcsCacheProfileTests extends AbstractJavaBeanTests {

  /**
   * Primary object (instance of the class to test).
   */
  private JcsCacheProfile profile;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the Test Case.
   */
  public JcsCacheProfileTests(String name) {
    super(name);
  }

  /**
   * @see AbstractJavaBeanTests#getEqualObjects()
   */
  protected Object[] getEqualObjects() {
    JcsCacheProfile equalProfile = new JcsCacheProfile();
    equalProfile.setCacheName("main");
    equalProfile.setGroup("test");

    return new Object[] { equalProfile };
  }

  /**
   * @see AbstractJavaBeanTests#getExpectedHashCode()
   */
  protected int getExpectedHashCode() {
    HashCodeBuilder hashCodeBuilder = new HashCodeBuilder(3, 7);
    hashCodeBuilder.append(this.profile.getCacheName());
    hashCodeBuilder.append(this.profile.getGroup());

    int expectedHashCode = hashCodeBuilder.toHashCode();
    return expectedHashCode;
  }

  /**
   * @see AbstractJavaBeanTests#getExpectedToString()
   */
  protected String getExpectedToString() {
    ToStringBuilder toStringBuilder = new ToStringBuilder(this.profile);
    toStringBuilder.append("cacheName", this.profile.getCacheName());
    toStringBuilder.append("group", this.profile.getGroup());

    String expectedToString = toStringBuilder.toString();
    return expectedToString;
  }

  /**
   * @see AbstractJavaBeanTests#getNotEqualObjects()
   */
  protected Object[] getNotEqualObjects() {
    JcsCacheProfile notEqualProfile = new JcsCacheProfile();
    notEqualProfile.setCacheName("temp");
    notEqualProfile.setGroup("dev");

    return new Object[] { notEqualProfile };
  }

  /**
   * @see AbstractJavaBeanTests#getPrimaryObject()
   */
  protected Object getPrimaryObject() {
    return this.profile;
  }

  /**
   * Sets up the test fixture.
   */
  protected void setUp() throws Exception {
    super.setUp();

    this.profile = new JcsCacheProfile();
    this.profile.setCacheName("main");
    this.profile.setGroup("test");
  }
}