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
 * @version $Revision: 1.5 $ $Date: 2005/09/09 02:19:01 $
 */
public final class MethodMapCachingAttributeSourceTests extends TestCase {

  private static final String GET_PERSON_NAME_METHOD = "getPersonName";

  private static final String GET_PERSONS_METHOD = "getPersons";

  private static final String METHOD_NAME_WILDCARD = "get*";

  /**
   * A caching attribute to register with the methods declared in
   * <code>{@link #targetClass}</code>.
   */
  private Cached cachingAttribute;

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
      cachingAttributeSource.addCachingAttribute(fullyQualifiedMethodName,
          cachingAttribute);
      fail();

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
    Map actualAttributeMap = cachingAttributeSource.getAttributeMap();

    assertTrue("The map of metadata attributes should contain the key '"
        + method + "'", actualAttributeMap.containsKey(method));

    assertSame(expectedCachingAttribute, actualAttributeMap.get(method));
  }

  private String createMethodFqn(String methodName) {
    return targetClass.getName() + "." + methodName;
  }

  private Method getTargetClassMethod(String methodName) throws Exception {
    return getTargetClassMethod(methodName, null);
  }

  private Method getTargetClassMethod(String methodName, Class[] parameters)
      throws Exception {
    return targetClass.getDeclaredMethod(methodName, parameters);
  }

  protected void setUp() throws Exception {
    super.setUp();

    cachingAttribute = new Cached("myCache");

    cachingAttributeSource = new MethodMapCachingAttributeSource();

    targetClass = SimulatedService.class;

    getPersonNameMethod = getTargetClassMethod(GET_PERSON_NAME_METHOD,
        new Class[] { long.class });

    getPersonsMethod = getTargetClassMethod(GET_PERSONS_METHOD);
  }

  /**
   * Verifies that the method
   * <code>{@link MethodMapCachingAttributeSource#addCachingAttribute(String, Cached)}</code>
   * registers the given caching attributes attached with the matching methods.
   */
  public void testAddCachingAttributeUsingWildcardsBeforeSpecificMethodName() {
    // use wildcards.
    cachingAttributeSource.addCachingAttribute(
        createMethodFqn(METHOD_NAME_WILDCARD), cachingAttribute);

    // verify the caching attributes were added
    assertCachingAttributeWasAdded(getPersonNameMethod, cachingAttribute);

    assertCachingAttributeWasAdded(getPersonsMethod, cachingAttribute);

    // use a more specific method name
    Cached otherCachingAttribute = new Cached("myOtherCache");

    cachingAttributeSource.addCachingAttribute(
        createMethodFqn(GET_PERSONS_METHOD), otherCachingAttribute);

    // verify the caching attributes were added
    assertCachingAttributeWasAdded(getPersonNameMethod, cachingAttribute);
    assertCachingAttributeWasAdded(getPersonsMethod, otherCachingAttribute);
  }

  /**
   * Verifies that the method
   * <code>{@link MethodMapCachingAttributeSource#addCachingAttribute(String, Cached)}</code>
   * throws a <code>{@link IllegalArgumentException}</code> if the class
   * specified in the given fully qualified method name does not exist.
   */
  public void testAddCachingAttributeWithNotExistingClass() {
    String fullyQualifiedMethodName = "MyFakeClass.get*";
    assertAddCachingAttributeThrowsException(fullyQualifiedMethodName);
  }

  /**
   * Verifies that the method
   * <code>{@link MethodMapCachingAttributeSource#addCachingAttribute(String, Cached)}</code>
   * throws a <code>{@link IllegalArgumentException}</code> if there is not
   * any method matching the given fully qualified method name.
   */
  public void testAddCachingAttributeWithNotMatchingMethod() {
    assertAddCachingAttributeThrowsException(createMethodFqn("addNew*"));
  }

  /**
   * Verifies that the method
   * <code>{@link MethodMapCachingAttributeSource#addCachingAttribute(String, Cached)}</code>
   * throws a <code>{@link IllegalArgumentException}</code> if the given
   * method name is not a fully qualified name.
   */
  public void testAddCachingAttributeWithoutFullyQualifiedMethodName() {
    assertAddCachingAttributeThrowsException(METHOD_NAME_WILDCARD);
  }

  /**
   * Verifies that the method
   * <code>{@link MethodMapCachingAttributeSource#addCachingAttribute(String, Cached)}</code>
   * registers the given caching attributes attached with the matching methods.
   */
  public void testAddCachingAttributeWithSpecificMethodNameBeforeUsingWildcards() {
    // use a more specific method name
    Cached otherCachingAttribute = new Cached("myOtherCache");

    cachingAttributeSource.addCachingAttribute(
        createMethodFqn(GET_PERSONS_METHOD), otherCachingAttribute);

    // verify the caching attributes were added
    assertCachingAttributeWasAdded(getPersonsMethod, otherCachingAttribute);

    // use wildcards.
    cachingAttributeSource.addCachingAttribute(
        createMethodFqn(METHOD_NAME_WILDCARD), cachingAttribute);

    // verify the caching attributes were added
    assertCachingAttributeWasAdded(getPersonNameMethod, cachingAttribute);
    assertCachingAttributeWasAdded(getPersonsMethod, otherCachingAttribute);
  }

  /**
   * Verifies that the method
   * <code>{@link MethodMapCachingAttributeSource#getCachingAttribute(Method, Class)}</code>
   * returns the caching attributed attached to the given method.
   */
  public void testGetCachingAttribute() {
    // use a more specific method name
    cachingAttributeSource.addCachingAttribute(
        createMethodFqn(GET_PERSONS_METHOD), cachingAttribute);

    assertSame(cachingAttribute, cachingAttributeSource.getCachingAttribute(
        getPersonsMethod, targetClass));
  }

}
