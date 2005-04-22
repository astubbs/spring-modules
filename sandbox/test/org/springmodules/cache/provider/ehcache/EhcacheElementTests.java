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

package org.springmodules.cache.provider.ehcache;

import net.sf.ehcache.Element;

import org.springmodules.cache.AbstractJavaBeanTests;

/**
 * <p>
 * Unit Test for <code>{@link EhcacheElement}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/04/22 02:18:58 $
 */
public final class EhcacheElementTests extends AbstractJavaBeanTests {

  /**
   * Primary object (Instance of the class to test).
   */
  private EhcacheElement ehcacheElement;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the Test Case.
   */
  public EhcacheElementTests(String name) {
    super(name);
  }

  /**
   * @see AbstractJavaBeanTests#getEqualObjects()
   */
  protected Object[] getEqualObjects() {
    EhcacheElement equalElement = new EhcacheElement("KEY", "VALUE", 1L);

    return new Object[] { equalElement };
  }

  /**
   * @see AbstractJavaBeanTests#getExpectedHashCode()
   */
  protected int getExpectedHashCode() {
    Element element = new Element("KEY", "VALUE", 1L);
    return element.hashCode();
  }

  /**
   * @see AbstractJavaBeanTests#getExpectedToString()
   */
  protected String getExpectedToString() {
    Element element = new Element("KEY", "VALUE", 1L);
    return element.toString();
  }

  /**
   * @see AbstractJavaBeanTests#getNotEqualObjects()
   */
  protected Object[] getNotEqualObjects() {
    return new Object[] { new EhcacheElement("KEY", "VALUE", 2L),
        new EhcacheElement("ANOTHER_KEY", "VALUE", 1L),
        new EhcacheElement("KEY", "ANOTHER_VALUE", 1L),
        new EhcacheElement("ANOTHER_KEY", "ANOTHER_VALUE", 2L) };
  }

  /**
   * @see AbstractJavaBeanTests#getPrimaryObject()
   */
  protected Object getPrimaryObject() {
    return this.ehcacheElement;
  }

  /**
   * Sets up the cache fixture.
   */
  protected void setUp() throws Exception {
    super.setUp();

    this.ehcacheElement = new EhcacheElement("KEY", "VALUE", 1L);
  }

}