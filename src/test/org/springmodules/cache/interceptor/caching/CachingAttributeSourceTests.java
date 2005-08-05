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
 * Unit Tests for <code>{@link AbstractCachingAttributeSource}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.2 $ $Date: 2005/08/05 02:18:50 $
 */
public final class CachingAttributeSourceTests extends TestCase {

  /**
   * Primary object that is under test.
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

  public CachingAttributeSourceTests(String name) {
    super(name);
  }

  private void assertCacheAttributeCouldNotBeFound(CacheAttribute cacheAttribute) {
    assertNull("The given metadata attribute should not have been found",
        cacheAttribute);
  }

  private void assertMethodIsCacheable(boolean expected, boolean actual) {
    assertEquals("<Method is cacheable>", expected, actual);

  }

  protected void setUp() throws Exception {
    super.setUp();

    this.setUpCachingAttributeSource();
  }

  private void setUpCachingAttributeSource() {
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
   * @param useCacheableMethod
   *          if <code>true</code>, a method with return type other than
   *          <code>void</code> will be used. If <code>false</code>, a
   *          method with return type <code>void</code> will be used.
   */
  private void setUpTargetClassAndMethod(boolean useCacheableMethod)
      throws Exception {

    this.targetClass = String.class;

    if (useCacheableMethod) {
      this.method = this.targetClass.getMethod("charAt",
          new Class[] { int.class });
    } else {
      this.method = this.targetClass.getMethod("notify", null);
    }
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractCachingAttributeSource#findAttribute(Collection)}</code>
   * returns the first element of the given collection of metadata attributes.
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
    this.assertCacheAttributeCouldNotBeFound(foundCacheAttribute);
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

    this.assertCacheAttributeCouldNotBeFound(foundCacheAttribute);
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

    this.assertCacheAttributeCouldNotBeFound(foundCacheAttribute);
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

    assertNotNull("The caching attribute should not be null", cached);
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

    assertNull("The caching attribute should be null", cached);
  }

  public void testIsCacheableWithCacheableMethod() throws Exception {
    boolean expectedIsCacheable = true;
    this.setUpTargetClassAndMethod(expectedIsCacheable);

    boolean actualIsCacheable = this.cachingAttributeSource
        .isCacheable(this.method);

    this.assertMethodIsCacheable(expectedIsCacheable, actualIsCacheable);
  }

  public void testIsCacheableWithNotCacheableMethod() throws Exception {
    boolean expectedIsCacheable = false;
    this.setUpTargetClassAndMethod(expectedIsCacheable);

    boolean actualIsCacheable = this.cachingAttributeSource
        .isCacheable(this.method);

    this.assertMethodIsCacheable(expectedIsCacheable, actualIsCacheable);
  }
}