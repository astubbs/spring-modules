/* 
 * Created on Sep 23, 2004
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

package org.springmodules.cache.interceptor.caching;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

import junit.framework.TestCase;

import org.springmodules.cache.CacheAttribute;
import org.springmodules.cache.interceptor.AbstractMetadataCacheAttributeSource;

/**
 * <p>
 * Unit Test for <code>{@link AbstractCachingAttributeSource}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/04/22 02:37:24 $
 */
public final class CachingAttributeSourceTests extends TestCase {

  /**
   * Primary object (instance of the class to test).
   */
  private AbstractCachingAttributeSource cachingAttributeSource;

  /**
   * Method to get the caching-attributes for.
   */
  private Method method;

  /**
   * Class declaring <code>{@link #method}</code>.
   */
  private Class targetClass;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the Test Case.
   */
  public CachingAttributeSourceTests(String name) {
    super(name);
  }

  /**
   * Sets up the test fixture.
   */
  protected void setUp() throws Exception {
    super.setUp();

    this.setUpCachingAttributeSource();
  }

  /**
   * Sets up {@link #cachingAttributeSource}</code>.
   */
  private void setUpCachingAttributeSource() {

    // we subclass instead of using EasyMock because the implemented abstract
    // method cannot be accessed since it is protected and this test does not
    // subclass 'AbstractMetadataCacheAttributeSource' nor is in the same
    // package.
    this.cachingAttributeSource = new AbstractCachingAttributeSource() {

      /**
       * Returns a collection containing only one instance of
       * <code>{@link Cached}</code>. We only need one element in the
       * collection to test
       * <code>{@link AbstractCachingAttributeSource#getCachingAttribute(Method, Class)}</code>.
       * 
       * @see AbstractMetadataCacheAttributeSource#findAllAttributes(Method)
       */
      protected Collection findAllAttributes(Method targetMethod) {
        Collection allAttributes = new ArrayList();
        allAttributes.add(new Cached());

        return allAttributes;
      }
    };

  }

  /**
   * Sets up:
   * <li><code>{@link #targetClass}</code></li>
   * <li><code>{@link #method}</code></li>
   * </ul>
   * 
   * @param cacheableMethod
   *          if <code>true</code>, the return type of
   *          <code>methodDefinition</code> is not <code>void</code>. If
   *          <code>false</code>, the return type of
   *          <code>methodDefinition</code> is <code>void</code>.
   */
  private void setUpTargetClassAndMethod(boolean cacheableMethod)
      throws Exception {

    this.targetClass = String.class;

    if (cacheableMethod) {
      this.method = this.targetClass.getMethod("charAt",
          new Class[] { int.class });
    } else {
      this.method = this.targetClass.getMethod("notify", null);
    }
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractCachingAttributeSource#findAttribute(Collection)}</code>
   * returns the first instance of <code>{@link CacheAttribute}</code> that
   * finds in the given collection of metadata attributes.
   */
  public void testFindAttributeWithCollectionContainingAnInstanceOfCached() {
    Cached expectedCachingAttribute = new Cached();
    Collection allAttributes = new ArrayList();
    allAttributes.add(expectedCachingAttribute);

    // execute the method to test.
    CacheAttribute actualCachingAttribute = this.cachingAttributeSource
        .findAttribute(allAttributes);

    // verify that we are getting the caching-attribute we expect.
    assertSame("<Caching-attribute>", expectedCachingAttribute,
        actualCachingAttribute);
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractCachingAttributeSource#findAttribute(Collection)}</code>
   * does not return any <code>{@link CacheAttribute}</code> if the given
   * collection of metadata attributes is equal to <code>null</code>.
   */
  public void testFindAttributeWithCollectionEqualToNull() {
    // execute the method to test.
    CacheAttribute foundCacheAttribute = this.cachingAttributeSource
        .findAttribute(null);

    assertNull("The returned caching-attribute should be null",
        foundCacheAttribute);
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractCachingAttributeSource#findAttribute(Collection)}</code>
   * does not return any <code>{@link CacheAttribute}</code> if the given
   * collection of metadata attributes is empty.
   */
  public void testFindAttributeWithEmptyCollection() {
    Collection allAttributes = new ArrayList();

    // execute the method to test.
    CacheAttribute foundCacheAttribute = this.cachingAttributeSource
        .findAttribute(allAttributes);

    assertNull("The returned caching-attribute should be null",
        foundCacheAttribute);
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractCachingAttributeSource#findAttribute(Collection)}</code>
   * does not return any <code>{@link CacheAttribute}</code> if the given
   * collection of metadata attributes is not empty but does not contain any
   * instance of <code>{@link Cached}</code>.
   */
  public void testFindAttributeWithNotEmptyCollectionNotContainingAnInstanceOfMatching() {
    String attribute = "A String!";
    Collection allAttributes = new ArrayList();
    allAttributes.add(attribute);

    // execute the method to test.
    CacheAttribute foundCacheAttribute = this.cachingAttributeSource
        .findAttribute(allAttributes);

    assertNull("The returned caching-attribute should be null",
        foundCacheAttribute);
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractCachingAttributeSource#getCachingAttribute(Method, Class)}</code>
   * returns an instance of <code>{@link Cached}</code> if the return value of
   * the specified method is not <code>void</code>.
   */
  public void testGetCachingAttributeWithCacheableMethod() throws Exception {
    boolean methodIsCacheable = true;
    this.setUpTargetClassAndMethod(methodIsCacheable);

    Cached cached = this.cachingAttributeSource.getCachingAttribute(
        this.method, this.targetClass);

    assertNotNull("The returned caching-attribute should not be null", cached);
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractCachingAttributeSource#getCachingAttribute(Method, Class)}</code>
   * returns <code>null</code> if the return value of the specified method is
   * <code>void</code>.
   */
  public void testGetCachingAttributeWithNotCacheableMethod() throws Exception {
    boolean methodIsCacheable = false;
    this.setUpTargetClassAndMethod(methodIsCacheable);

    Cached cached = this.cachingAttributeSource.getCachingAttribute(
        this.method, this.targetClass);

    assertNull("The returned caching-attribute should be null", cached);
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractCachingAttributeSource#isCacheable(Method)}</code>
   * returns <code>true</code> if the return type of the specified method
   * definition is not <code>void</code>.
   */
  public void testIsCacheableWithCacheableMethod() throws Exception {
    boolean expectedIsCacheable = true;
    this.setUpTargetClassAndMethod(expectedIsCacheable);

    boolean actualIsCacheable = this.cachingAttributeSource
        .isCacheable(this.method);

    assertEquals("<isCacheable()>", expectedIsCacheable, actualIsCacheable);
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractCachingAttributeSource#isCacheable(Method)}</code>
   * returns <code>false</code> if the return type of the specified method
   * definition is <code>void</code>.
   */
  public void testIsCacheableWithNotCacheableMethod() throws Exception {
    boolean expectedIsCacheable = false;
    this.setUpTargetClassAndMethod(expectedIsCacheable);

    boolean actualIsCacheable = this.cachingAttributeSource
        .isCacheable(this.method);

    assertEquals("<isCacheable()>", expectedIsCacheable, actualIsCacheable);
  }
}