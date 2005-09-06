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
package org.springmodules.cache.interceptor.flush;

import java.lang.reflect.Method;
import java.util.Map;

import org.springmodules.cache.interceptor.SimulatedService;

import junit.framework.TestCase;

/**
 * <p>
 * Unit Tests for <code>{@link MethodMapCacheFlushAttributeSource}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.4 $ $Date: 2005/09/06 01:41:25 $
 */
public final class MethodMapCacheFlushAttributeSourceTests extends TestCase {

  /**
   * A caching attribute to register with the methods declared in
   * <code>{@link #targetClass}</code>.
   */
  private FlushCache cachingAttribute;

  private MethodMapCacheFlushAttributeSource cachingAttributeSource;

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
   * Representation of <code>{@link SimulatedService}</code>.
   */
  private Class targetClass;

  public MethodMapCacheFlushAttributeSourceTests(String name) {
    super(name);
  }

  /**
   * Asserts that the given caching attribute was added to
   * <code>{@link #cachingAttributeSource}</code>.
   * 
   * @param method
   *          the method used as key of the entry.
   * @param expectedCacheFlushAttribute
   *          the caching attribute that should have been added.
   */
  private void assertCacheFlushAttributeWasAdded(Method method,
      FlushCache expectedCacheFlushAttribute) {
    Map actualAttributeMap = this.cachingAttributeSource.getAttributeMap();

    assertTrue("The map should have the key '" + method + "'",
        actualAttributeMap.containsKey(method));

    assertSame(expectedCacheFlushAttribute, actualAttributeMap.get(method));
  }

  protected void setUp() throws Exception {
    super.setUp();

    this.cachingAttribute = new FlushCache("myCache");

    this.cachingAttributeSource = new MethodMapCacheFlushAttributeSource();

    this.targetClass = SimulatedService.class;

    this.getPersonNameMethod = this.targetClass.getDeclaredMethod(
        "getPersonName", new Class[] { long.class });

    this.getPersonsMethod = this.targetClass.getDeclaredMethod("getPersons",
        null);
  }

  /**
   * Verifies that the method
   * <code>{@link MethodMapCacheFlushAttributeSource#addCacheFlushAttribute(String, FlushCache)}</code>
   * registers the given caching attributes attached with the matching methods.
   */
  public void testAddCacheFlushAttributeUsingWildcardsBeforeSpecificMethodName() {
    // use wildcards.
    String fullyQualifiedMethodName = this.targetClass.getName() + ".get*";

    this.cachingAttributeSource.addCacheFlushAttribute(
        fullyQualifiedMethodName, this.cachingAttribute);

    // verify the caching attributes were added
    assertCacheFlushAttributeWasAdded(this.getPersonNameMethod,
        this.cachingAttribute);

    assertCacheFlushAttributeWasAdded(this.getPersonsMethod,
        this.cachingAttribute);

    // use a more specific method name
    fullyQualifiedMethodName = this.targetClass.getName() + ".getPersons";

    FlushCache otherCacheFlushAttribute = new FlushCache("myOtherCache");

    this.cachingAttributeSource.addCacheFlushAttribute(
        fullyQualifiedMethodName, otherCacheFlushAttribute);

    // verify the caching attributes were added
    assertCacheFlushAttributeWasAdded(this.getPersonNameMethod,
        this.cachingAttribute);

    assertCacheFlushAttributeWasAdded(this.getPersonsMethod,
        otherCacheFlushAttribute);
  }

  private void assertAddCacheFlushAttributeThrowsIllegalArgumentException(
      String fullyQualifiedMethodName) {
    try {
      this.cachingAttributeSource.addCacheFlushAttribute(
          fullyQualifiedMethodName, this.cachingAttribute);
      fail();

    } catch (IllegalArgumentException exception) {
      // we are expecting this exception.
    }

  }

  /**
   * Verifies that the method
   * <code>{@link MethodMapCacheFlushAttributeSource#addCacheFlushAttribute(String, FlushCache)}</code>
   * throws a <code>{@link IllegalArgumentException}</code> if the class
   * specified in the given fully qualified method name does not exist.
   */
  public void testAddCacheFlushAttributeWithNotExistingClass() {
    assertAddCacheFlushAttributeThrowsIllegalArgumentException("MyFakeClass.get*");
  }

  /**
   * Verifies that the method
   * <code>{@link MethodMapCacheFlushAttributeSource#addCacheFlushAttribute(String, FlushCache)}</code>
   * throws a <code>{@link IllegalArgumentException}</code> if there is not
   * any method matching the given fully qualified method name.
   */
  public void testAddCacheFlushAttributeWithNotMatchingMethod() {
    String fullyQualifiedMethodName = this.targetClass.getName() + ".addNew*";
    assertAddCacheFlushAttributeThrowsIllegalArgumentException(fullyQualifiedMethodName);
  }

  /**
   * Verifies that the method
   * <code>{@link MethodMapCacheFlushAttributeSource#addCacheFlushAttribute(String, FlushCache)}</code>
   * throws a <code>{@link IllegalArgumentException}</code> if the given
   * method name is not a fully qualified name.
   */
  public void testAddCacheFlushAttributeWithoutFullyQualifiedMethodName() {
    assertAddCacheFlushAttributeThrowsIllegalArgumentException("get*");
  }

  /**
   * Verifies that the method
   * <code>{@link MethodMapCacheFlushAttributeSource#addCacheFlushAttribute(String, FlushCache)}</code>
   * registers the given caching attributes attached with the matching methods.
   */
  public void testAddCacheFlushAttributeWithSpecificMethodNameBeforeUsingWildcards() {
    // use a more specific method name
    String fullyQualifiedMethodName = this.targetClass.getName()
        + ".getPersons";

    FlushCache otherCacheFlushAttribute = new FlushCache("myOtherCache");

    this.cachingAttributeSource.addCacheFlushAttribute(
        fullyQualifiedMethodName, otherCacheFlushAttribute);

    // verify the caching attributes were added
    assertCacheFlushAttributeWasAdded(this.getPersonsMethod,
        otherCacheFlushAttribute);

    // use wildcards.
    fullyQualifiedMethodName = this.targetClass.getName() + ".get*";

    this.cachingAttributeSource.addCacheFlushAttribute(
        fullyQualifiedMethodName, this.cachingAttribute);

    // verify the caching attributes were added
    assertCacheFlushAttributeWasAdded(this.getPersonNameMethod,
        this.cachingAttribute);

    assertCacheFlushAttributeWasAdded(this.getPersonsMethod,
        otherCacheFlushAttribute);
  }

  /**
   * Verifies that the method
   * <code>{@link MethodMapCacheFlushAttributeSource#getCacheFlushAttribute(Method, Class)}</code>
   * returns the caching attributed attached to the given method.
   */
  public void testGetCacheFlushAttribute() {
    // use a more specific method name
    String fullyQualifiedMethodName = this.targetClass.getName()
        + ".getPersons";

    this.cachingAttributeSource.addCacheFlushAttribute(
        fullyQualifiedMethodName, this.cachingAttribute);

    assertSame("<Cache flush Attribute>", this.cachingAttribute,
        this.cachingAttributeSource.getCacheFlushAttribute(
            this.getPersonsMethod, this.targetClass));
  }

}
