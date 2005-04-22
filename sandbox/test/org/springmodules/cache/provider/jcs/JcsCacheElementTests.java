/* 
 * Created on Dec 4, 2004
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

import org.apache.jcs.engine.CacheElement;
import org.springmodules.cache.AbstractJavaBeanTests;

/**
 * <p>
 * Unit Test for <code>{@link JcsCacheElement}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/04/22 02:19:10 $
 */
public final class JcsCacheElementTests extends AbstractJavaBeanTests {

  /**
   * Instance of the class to test.
   */
  private JcsCacheElement jcsCacheElement;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the Test Case.
   */
  public JcsCacheElementTests(String name) {
    super(name);
  }

  /**
   * @see AbstractJavaBeanTests#getEqualObjects()
   */
  protected Object[] getEqualObjects() {
    JcsCacheElement equalElement = new JcsCacheElement("CACHE_NAME", "KEY",
        "VALUE");
    return new Object[] { equalElement };
  }

  /**
   * @see AbstractJavaBeanTests#getExpectedHashCode()
   */
  protected int getExpectedHashCode() {
    CacheElement element = new CacheElement("CACHE_NAME", "KEY", "VALUE");
    return element.hashCode();
  }

  /**
   * @see AbstractJavaBeanTests#getExpectedToString()
   */
  protected String getExpectedToString() {
    CacheElement element = new CacheElement("CACHE_NAME", "KEY", "VALUE");
    return element.toString();
  }

  /**
   * @see AbstractJavaBeanTests#getNotEqualObjects()
   */
  protected Object[] getNotEqualObjects() {
    JcsCacheElement notEqualElement = new JcsCacheElement("OTHER_CACHE_NAME",
        "KEY", "VALUE");
    return new Object[] { notEqualElement };
  }

  /**
   * @see AbstractJavaBeanTests#getPrimaryObject()
   */
  protected Object getPrimaryObject() {
    return this.jcsCacheElement;
  }

  /**
   * Sets up the cache fixture.
   */
  protected void setUp() throws Exception {
    super.setUp();

    this.jcsCacheElement = new JcsCacheElement("CACHE_NAME", "KEY", "VALUE");
  }

}