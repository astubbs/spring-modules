/* 
 * Created on Jan 18, 2005
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

import java.beans.PropertyEditor;
import java.lang.reflect.Method;
import java.util.Properties;

import junit.framework.TestCase;

/**
 * <p>
 * Unit Test for <code>{@link NameMatchCacheFlushAttributeSource}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/04/22 02:19:18 $
 */
public class NameMatchCacheFlushAttributeSourceTests extends TestCase {

  /**
   * Primary object (instance of the class to test).
   */
  private NameMatchCacheFlushAttributeSource attributeSource;

  /**
   * Method to obtain cache-flush-attributes for.
   */
  private Method method;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the Test Case.
   */
  public NameMatchCacheFlushAttributeSourceTests(String name) {
    super(name);
  }

  /**
   * Sets up the test fixture.
   */
  protected void setUp() throws Exception {
    super.setUp();

    this.attributeSource = new NameMatchCacheFlushAttributeSource();
  }

  /**
   * Sets up <code>{@link #method}</code>.
   */
  private void setUpMethod() throws Exception {
    this.method = String.class.getDeclaredMethod("charAt",
        new Class[] { int.class });
  }

  /**
   * Verifies that the method
   * <code>{@link NameMatchCacheFlushAttributeSource#getCacheAttributeEditor()}</code>
   * returns a new instance of <code>{@link CacheFlushAttributeEditor}</code>.
   */
  public void testGetCacheAttributeEditor() {
    PropertyEditor cacheFlushAttributeEditor = this.attributeSource
        .getCacheAttributeEditor();

    assertNotNull("The editor of caching-attributes should not be null",
        cacheFlushAttributeEditor);
    assertTrue(
        "The editor should be an instance of 'CacheFlushAttributeEditor'",
        cacheFlushAttributeEditor instanceof CacheFlushAttributeEditor);
  }

  /**
   * Verifies that the method
   * <code>{@link NameMatchCacheFlushAttributeSource#getCacheFlushAttribute(Method, Class)}</code>
   * returns a cache-flush-attribute when the name of the given method matches a
   * mapped name that does not contain wild cards.
   */
  public void testGetCacheFlushAttributeWithMethodNameMatchingMappedNameWithoutWildCards()
      throws Exception {

    this.setUpMethod();
    FlushCache expectedCacheFlushAttribute = new FlushCache("main");

    Properties properties = new Properties();

    // the first property key should match, but the second one is used because
    // the key of the second property also matches and it's longer than the
    // first one.
    properties.setProperty(this.method.getName().substring(0, 2) + "*",
        "[cacheProfileIds=test]");
    properties.setProperty(this.method.getName().substring(0, 3) + "*",
        "[cacheProfileIds=main]");

    this.attributeSource.setProperties(properties);

    // execute the method to test.
    FlushCache actualCacheFlushAttribute = this.attributeSource
        .getCacheFlushAttribute(this.method, this.method.getDeclaringClass());

    assertEquals("<cache-flush-attribute>", expectedCacheFlushAttribute,
        actualCacheFlushAttribute);
  }

  /**
   * Verifies that the method
   * <code>{@link NameMatchCacheFlushAttributeSource#getCacheFlushAttribute(Method, Class)}</code>
   * returns a cache-flush-attribute when the name of the given method matches a
   * mapped name containing wild cards.
   */
  public void testGetCacheFlushAttributeWithMethodNameMatchingMappedNameWithWildCards()
      throws Exception {

    this.setUpMethod();
    FlushCache expectedCacheFlushAttribute = new FlushCache("main");

    Properties properties = new Properties();
    properties.setProperty(this.method.getName(), "[cacheProfileIds=main]");

    this.attributeSource.setProperties(properties);

    // execute the method to test.
    FlushCache actualCacheFlushAttribute = this.attributeSource
        .getCacheFlushAttribute(this.method, this.method.getDeclaringClass());

    assertEquals("<cache-flush-attribute>", expectedCacheFlushAttribute,
        actualCacheFlushAttribute);
  }

  /**
   * Verifies that the method
   * <code>{@link NameMatchCacheFlushAttributeSource#getCacheFlushAttribute(Method, Class)}</code>
   * does not return any cache-flush-attribute if the name of the given method
   * does not match any of the mapped methods.
   */
  public void testGetCacheFlushAttributeWithNotMatchingMethodName()
      throws Exception {

    this.setUpMethod();

    Properties properties = new Properties();
    properties.setProperty("aMethodThatDoesNotExist",
        "[cacheProfileIds=test,main]");

    this.attributeSource.setProperties(properties);

    // execute the method to test.
    FlushCache actualCacheFlushAttribute = this.attributeSource
        .getCacheFlushAttribute(this.method, this.method.getDeclaringClass());

    assertNull("The cache-flush-attribute should be null",
        actualCacheFlushAttribute);
  }

}