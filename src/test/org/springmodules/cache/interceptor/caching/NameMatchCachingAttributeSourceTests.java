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

package org.springmodules.cache.interceptor.caching;

import java.beans.PropertyEditor;
import java.lang.reflect.Method;
import java.util.Properties;

import junit.framework.TestCase;

/**
 * <p>
 * Unit Test for <code>{@link NameMatchCachingAttributeSource}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/04/27 01:41:13 $
 */
public class NameMatchCachingAttributeSourceTests extends TestCase {

  /**
   * Primary object (instance of the class to test).
   */
  private NameMatchCachingAttributeSource attributeSource;

  /**
   * Method to obtain caching-attributes for.
   */
  private Method method;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the Test Case.
   */
  public NameMatchCachingAttributeSourceTests(String name) {
    super(name);
  }

  /**
   * Sets up the test fixture.
   */
  protected void setUp() throws Exception {
    super.setUp();

    this.attributeSource = new NameMatchCachingAttributeSource();
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
   * <code>{@link NameMatchCachingAttributeSource#getCacheAttributeEditor()}</code>
   * returns a new instance of <code>{@link CachingAttributeEditor}</code>.
   */
  public void testGetCacheAttributeEditor() {
    PropertyEditor cachingAttributeEditor = this.attributeSource
        .getCacheAttributeEditor();

    assertNotNull("The editor of caching-attributes should not be null",
        cachingAttributeEditor);
    assertTrue("The editor should be an instance of 'CachingAttributeEditor'",
        cachingAttributeEditor instanceof CachingAttributeEditor);
  }

  /**
   * Verifies that the method
   * <code>{@link NameMatchCachingAttributeSource#getCachingAttribute(Method, Class)}</code>
   * returns a caching-attribute when the name of the given method matches a
   * mapped name that does not contain wild cards.
   */
  public void testGetCachingAttributeWithMethodNameMatchingMappedNameWithoutWildCards()
      throws Exception {

    // set the properties to be used to create caching-attributes.
    Properties properties = new Properties();
    properties.setProperty("charAt", "[cacheProfileId=main]");
    this.attributeSource.setProperties(properties);

    this.setUpMethod();
    Cached expectedCachingAttribute = new Cached("main");

    // execute the method to test.
    Cached actualCachingAttribute = this.attributeSource.getCachingAttribute(
        this.method, this.method.getDeclaringClass());

    assertEquals("<Caching-attribute>", expectedCachingAttribute,
        actualCachingAttribute);
  }

  /**
   * Verifies that the method
   * <code>{@link NameMatchCachingAttributeSource#getCachingAttribute(Method, Class)}</code>
   * returns a caching-attribute when the name of the given method matches a
   * mapped name containing wild cards.
   */
  public void testGetCachingAttributeWithMethodNameMatchingMappedNameWithWildCards()
      throws Exception {

    // set the properties to be used to create caching-attributes.
    Properties properties = new Properties();

    // both property keys match the name of the method, but the second one is
    // used since has a longer length.
    properties.setProperty("ch*", "[cacheProfileId=test]");
    properties.setProperty("char*", "[cacheProfileId=main]");
    this.attributeSource.setProperties(properties);

    this.setUpMethod();
    Cached expectedCachingAttribute = new Cached("main");

    // execute the method to test.
    Cached actualCachingAttribute = this.attributeSource.getCachingAttribute(
        this.method, this.method.getDeclaringClass());

    assertEquals("<Caching-attribute>", expectedCachingAttribute,
        actualCachingAttribute);
  }

  /**
   * Verifies that the method
   * <code>{@link NameMatchCachingAttributeSource#getCachingAttribute(Method, Class)}</code>
   * does not return any caching-attribute if the name of the given method does
   * not match any of the mapped methods.
   */
  public void testGetCachingAttributeWithNotMatchingMethodName()
      throws Exception {

    this.setUpMethod();

    Properties properties = new Properties();
    properties.setProperty("aMethodThatDoesNotExist", "[cacheProfileId=main]");

    this.attributeSource.setProperties(properties);

    // execute the method to test.
    Cached actualCachingAttribute = this.attributeSource.getCachingAttribute(
        this.method, this.method.getDeclaringClass());

    assertNull("The caching-attribute should be null", actualCachingAttribute);
  }
}