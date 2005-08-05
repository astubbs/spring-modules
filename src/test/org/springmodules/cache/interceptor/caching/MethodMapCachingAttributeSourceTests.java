/* 
 * Created on Apr 7, 2005
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

import java.lang.reflect.Method;
import java.util.Map;

import org.springmodules.cache.interceptor.SimulatedService;

import junit.framework.TestCase;

/**
 * <p>
 * Unit Tests for <code>{@link MethodMapCachingAttributeSource}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.3 $ $Date: 2005/08/05 02:45:16 $
 */
public final class MethodMapCachingAttributeSourceTests extends TestCase {

  /**
   * A caching attribute to register with the methods declared in
   * <code>{@link #targetClass}</code>.
   */
  private Cached cachingAttribute;

  /**
   * Primary object that is under test.
   */
  private MethodMapCachingAttributeSource cachingAttributeSource;

  /**
   * Representation of the method
   * <code>{@link SimulatedService#getPersonName(long)}</code>.
   */
  private Method getPersonNameMethod;

  /**
   * Representation of the method
   * <code>{@link SimulatedService#getPersons()}</code>.
   */
  private Method getPersonsMethod;

  /**
   * Reference to the class <code>{@link SimulatedService}</code>.
   */
  private Class targetClass;

  public MethodMapCachingAttributeSourceTests(String name) {
    super(name);
  }

  private void assertAddCachingAttributeThrowsException(
      String fullyQualifiedMethodName) {
    try {
      this.cachingAttributeSource.addCachingAttribute(fullyQualifiedMethodName,
          this.cachingAttribute);
      fail("Expecting a <" + IllegalArgumentException.class.getName() + ">");

    } catch (IllegalArgumentException exception) {
      // we are expecting this exception.
    }
  }

  /**
   * Asserts that the given caching attribute was added to
   * <code>{@link #cachingAttributeSource}</code>.
   * 
   * @param method
   *          the method used as key of the entry.
   * @param expectedCachingAttribute
   *          the caching attribute that should have been added.
   */
  private void assertCachingAttributeWasAdded(Method method,
      Cached expectedCachingAttribute) {
    Map actualAttributeMap = this.cachingAttributeSource.getAttributeMap();

    assertTrue("The map of metadata attributes should contain the key '"
        + method + "'", actualAttributeMap.containsKey(method));

    assertSame("<Caching Attribute>", expectedCachingAttribute,
        actualAttributeMap.get(method));
  }

  protected void setUp() throws Exception {
    super.setUp();

    this.cachingAttribute = new Cached("myCache");

    this.cachingAttributeSource = new MethodMapCachingAttributeSource();

    this.targetClass = SimulatedService.class;

    this.getPersonNameMethod = this.targetClass.getDeclaredMethod(
        "getPersonName", new Class[] { long.class });

    this.getPersonsMethod = this.targetClass.getDeclaredMethod("getPersons",
        null);
  }

  /**
   * Verifies that the method
   * <code>{@link MethodMapCachingAttributeSource#addCachingAttribute(String, Cached)}</code>
   * registers the given caching attributes attached with the matching methods.
   */
  public void testAddCachingAttributeUsingWildcardsBeforeSpecificMethodName() {
    // use wildcards.
    String fullyQualifiedMethodName = this.targetClass.getName() + ".get*";

    this.cachingAttributeSource.addCachingAttribute(fullyQualifiedMethodName,
        this.cachingAttribute);

    // verify the caching attributes were added
    this.assertCachingAttributeWasAdded(this.getPersonNameMethod,
        this.cachingAttribute);

    this.assertCachingAttributeWasAdded(this.getPersonsMethod,
        this.cachingAttribute);

    // use a more specific method name
    fullyQualifiedMethodName = this.targetClass.getName() + ".getPersons";

    Cached otherCachingAttribute = new Cached("myOtherCache");

    this.cachingAttributeSource.addCachingAttribute(fullyQualifiedMethodName,
        otherCachingAttribute);

    // verify the caching attributes were added
    this.assertCachingAttributeWasAdded(this.getPersonNameMethod,
        this.cachingAttribute);

    this.assertCachingAttributeWasAdded(this.getPersonsMethod,
        otherCachingAttribute);
  }

  /**
   * Verifies that the method
   * <code>{@link MethodMapCachingAttributeSource#addCachingAttribute(String, Cached)}</code>
   * throws a <code>{@link IllegalArgumentException}</code> if the class
   * specified in the given fully qualified method name does not exist.
   */
  public void testAddCachingAttributeWithNotExistingClass() {
    String fullyQualifiedMethodName = "MyFakeClass.get*";
    this.assertAddCachingAttributeThrowsException(fullyQualifiedMethodName);
  }

  /**
   * Verifies that the method
   * <code>{@link MethodMapCachingAttributeSource#addCachingAttribute(String, Cached)}</code>
   * throws a <code>{@link IllegalArgumentException}</code> if there is not
   * any method matching the given fully qualified method name.
   */
  public void testAddCachingAttributeWithNotMatchingMethod() {
    String fullyQualifiedMethodName = this.targetClass.getName() + ".addNew*";
    this.assertAddCachingAttributeThrowsException(fullyQualifiedMethodName);
  }

  /**
   * Verifies that the method
   * <code>{@link MethodMapCachingAttributeSource#addCachingAttribute(String, Cached)}</code>
   * throws a <code>{@link IllegalArgumentException}</code> if the given
   * method name is not a fully qualified name.
   */
  public void testAddCachingAttributeWithoutFullyQualifiedMethodName() {
    String notFullyQualifiedMethodName = "get*";
    this.assertAddCachingAttributeThrowsException(notFullyQualifiedMethodName);
  }

  /**
   * Verifies that the method
   * <code>{@link MethodMapCachingAttributeSource#addCachingAttribute(String, Cached)}</code>
   * registers the given caching attributes attached with the matching methods.
   */
  public void testAddCachingAttributeWithSpecificMethodNameBeforeUsingWildcards() {
    // use a more specific method name
    String fullyQualifiedMethodName = this.targetClass.getName()
        + ".getPersons";

    Cached otherCachingAttribute = new Cached("myOtherCache");

    this.cachingAttributeSource.addCachingAttribute(fullyQualifiedMethodName,
        otherCachingAttribute);

    // verify the caching attributes were added
    this.assertCachingAttributeWasAdded(this.getPersonsMethod,
        otherCachingAttribute);

    // use wildcards.
    fullyQualifiedMethodName = this.targetClass.getName() + ".get*";

    this.cachingAttributeSource.addCachingAttribute(fullyQualifiedMethodName,
        this.cachingAttribute);

    // verify the caching attributes were added
    this.assertCachingAttributeWasAdded(this.getPersonNameMethod,
        this.cachingAttribute);

    this.assertCachingAttributeWasAdded(this.getPersonsMethod,
        otherCachingAttribute);
  }

  /**
   * Verifies that the method
   * <code>{@link MethodMapCachingAttributeSource#getCachingAttribute(Method, Class)}</code>
   * returns the caching attributed attached to the given method.
   */
  public void testGetCachingAttribute() {
    // use a more specific method name
    String fullyQualifiedMethodName = this.targetClass.getName()
        + ".getPersons";

    this.cachingAttributeSource.addCachingAttribute(fullyQualifiedMethodName,
        this.cachingAttribute);

    assertSame("<Caching Attribute>", this.cachingAttribute,
        this.cachingAttributeSource.getCachingAttribute(this.getPersonsMethod,
            this.targetClass));
  }

}
