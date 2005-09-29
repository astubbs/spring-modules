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
import java.util.Map;
import java.util.Properties;

import junit.framework.TestCase;

/**
 * <p>
 * Unit Tests for <code>{@link NameMatchCachingAttributeSource}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.7 $ $Date: 2005/09/29 01:22:05 $
 */
public class NameMatchCachingAttributeSourceTests extends TestCase {

  private NameMatchCachingAttributeSource attributeSource;

  private Method method;

  public NameMatchCachingAttributeSourceTests(String name) {
    super(name);
  }

  protected void setUp() throws Exception {
    super.setUp();

    attributeSource = new NameMatchCachingAttributeSource();
  }

  private void setUpMethod() throws Exception {
    method = String.class
        .getDeclaredMethod("charAt", new Class[] { int.class });
  }

  /**
   * Verifies that the method
   * <code>{@link NameMatchCachingAttributeSource#addCachingAttribute(String, Cached)}</code>
   * adds the specified caching attribute to the map of attributes using the
   * specified method name as the key.
   */
  public void testAddCachingAttribute() {
    String methodName = "addUser";
    Cached cachingAttribute = new Cached();

    attributeSource.addCachingAttribute(methodName, cachingAttribute);

    Map cachingAttributeMap = attributeSource.getCachingAttributeMap();
    assertTrue("There should be an entry with key '" + methodName + "'",
        cachingAttributeMap.containsKey(methodName));

    assertSame(cachingAttribute, cachingAttributeMap.get(methodName));
  }

  /**
   * Verifies that the method
   * <code>{@link NameMatchCachingAttributeSource#getCacheAttributeEditor()}</code>
   * returns a new instance of <code>{@link CachingAttributeEditor}</code>.
   */
  public void testGetCacheAttributeEditor() {
    PropertyEditor cacheAttributeEditor = attributeSource
        .getCacheAttributeEditor();

    assertNotNull(cacheAttributeEditor);

    Class expectedEditorClass = CachingAttributeEditor.class;
    assertEquals(expectedEditorClass, cacheAttributeEditor.getClass());
  }

  /**
   * Verifies that the method
   * <code>{@link NameMatchCachingAttributeSource#getCachingAttribute(Method, Class)}</code>
   * returns a caching attribute when the name of the given method matches a
   * mapped name that does not contain wild cards.
   */
  public void testGetCachingAttributeWithMethodNameMatchingMappedNameWithoutWildCards()
      throws Exception {

    // set the properties to be used to create caching attributes.
    Properties properties = new Properties();
    properties.setProperty("charAt", "[cacheModelId=main]");
    attributeSource.setProperties(properties);

    setUpMethod();
    Cached expectedCachingAttribute = new Cached("main");

    // execute the method to test.
    Cached actualCachingAttribute = attributeSource.getCachingAttribute(method,
        method.getDeclaringClass());

    assertEquals(expectedCachingAttribute, actualCachingAttribute);
  }

  /**
   * Verifies that the method
   * <code>{@link NameMatchCachingAttributeSource#getCachingAttribute(Method, Class)}</code>
   * returns a caching attribute when the name of the given method matches a
   * mapped name containing wild cards.
   */
  public void testGetCachingAttributeWithMethodNameMatchingMappedNameWithWildCards()
      throws Exception {

    // set the properties to be used to create caching attributes.
    Properties properties = new Properties();

    // both property keys match the name of the method, but the second one is
    // used since has a longer length.
    properties.setProperty("ch*", "[cacheModelId=test]");
    properties.setProperty("char*", "[cacheModelId=main]");
    attributeSource.setProperties(properties);

    setUpMethod();
    Cached expectedCachingAttribute = new Cached("main");

    // execute the method to test.
    Cached actualCachingAttribute = attributeSource.getCachingAttribute(method,
        method.getDeclaringClass());

    assertEquals(expectedCachingAttribute, actualCachingAttribute);
  }

  /**
   * Verifies that the method
   * <code>{@link NameMatchCachingAttributeSource#getCachingAttribute(Method, Class)}</code>
   * does not return any caching attribute if the name of the given method does
   * not match any of the mapped methods.
   */
  public void testGetCachingAttributeWithNotMatchingMethodName()
      throws Exception {
    setUpMethod();

    Properties properties = new Properties();
    properties.setProperty("aMethodThatDoesNotExist", "[cacheModelId=main]");

    attributeSource.setProperties(properties);

    // execute the method to test.
    Cached actualCachingAttribute = attributeSource.getCachingAttribute(method,
        method.getDeclaringClass());

    assertNull(actualCachingAttribute);
  }
}