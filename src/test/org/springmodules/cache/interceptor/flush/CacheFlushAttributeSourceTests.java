/* 
 * Created on Oct 22, 2004
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

package org.springmodules.cache.interceptor.flush;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

import junit.framework.TestCase;

import org.springmodules.cache.CacheAttribute;
import org.springmodules.cache.interceptor.AbstractMetadataCacheAttributeSource;

/**
 * <p>
 * Unit Tests for <code>{@link AbstractCacheFlushAttributeSource}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.3 $ $Date: 2005/08/11 04:32:04 $
 */
public final class CacheFlushAttributeSourceTests extends TestCase {

  /**
   * Primary object that is under test.
   */
  private AbstractCacheFlushAttributeSource cacheFlushAttributeSource;

  /**
   * Method to get the cache flushing attributes for.
   */
  private Method method;

  /**
   * Class declaring <code>{@link #method}</code>.
   */
  private Class targetClass;

  public CacheFlushAttributeSourceTests(String name) {
    super(name);
  }

  protected void setUp() throws Exception {
    super.setUp();

    this.setUpCacheFlushAttributeSourceAsMockObject();
  }

  private void setUpCacheFlushAttributeSourceAsMockObject() throws Exception {

    // we subclass instead of using EasyMock because the implemented abstract
    // method cannot be accessed since it is protected and this test does not
    // subclass 'AbstractMetadataCacheAttributeSource' nor is in the same
    // package.
    this.cacheFlushAttributeSource = new AbstractCacheFlushAttributeSource() {

      /**
       * Returns a collection containing only one instance of
       * <code>{@link FlushCache}</code>. We only need one element in the
       * collection to test
       * <code>{@link AbstractCacheFlushAttributeSource#getCacheFlushAttribute(Method, Class)}</code>.
       * 
       * @see AbstractMetadataCacheAttributeSource#findAllAttributes(Method)
       */
      protected Collection findAllAttributes(Method targetMethod) {
        Collection allAttributes = new ArrayList();
        allAttributes.add(new FlushCache());

        return allAttributes;
      }
    };
  }

  private void setUpTargetClassAndMethod() throws Exception {
    this.targetClass = String.class;

    this.method = this.targetClass.getMethod("charAt",
        new Class[] { int.class });
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractCacheFlushAttributeSource#findAttribute(Collection)}</code>.
   * returns the first instance of <code>{@link FlushCache}</code> contained
   * in the given collection of metadata attributes.
   */
  public void testFindAttributeWithCollectionContainingAnInstanceOfFlushCache() {
    FlushCache flushCache = new FlushCache();
    Collection allAttributes = new ArrayList();
    allAttributes.add(flushCache);

    // execute the method to test.
    CacheAttribute foundAttribute = this.cacheFlushAttributeSource
        .findAttribute(allAttributes);

    // verify that we are getting the cache flushing attribute we expect.
    assertSame("<Cache flushing attribute>", flushCache, foundAttribute);
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractCacheFlushAttributeSource#findAttribute(Collection)}</code>.
   * does not return any <code>{@link CacheAttribute}</code> if the given
   * collection of metadata attributes is equal to <code>null</code>.
   */
  public void testFindAttributeWithCollectionEqualToNull() {
    // execute the method to test.
    CacheAttribute foundAttribute = this.cacheFlushAttributeSource
        .findAttribute(null);

    assertNull(foundAttribute);
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractCacheFlushAttributeSource#findAttribute(Collection)}</code>.
   * does not return any <code>{@link CacheAttribute}</code> if the given
   * collection of metadata attributes is empty.
   */
  public void testFindAttributeWithEmptyCollection() {
    Collection allAttributes = new ArrayList();

    // execute the method to test.
    CacheAttribute foundCacheAttribute = this.cacheFlushAttributeSource
        .findAttribute(allAttributes);

    assertNull(foundCacheAttribute);
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractCacheFlushAttributeSource#findAttribute(Collection)}</code>.
   * does not return any <code>{@link CacheAttribute}</code> if the given
   * collection of metadata attributes is not empty but does not contain any
   * instance of <code>{@link FlushCache}</code>.
   */
  public void testFindAttributeWithNotEmptyCollectionNotContainingAnInstanceOfMatching() {
    String attribute = "A String!";
    Collection allAttributes = new ArrayList();
    allAttributes.add(attribute);

    // execute the method to test.
    CacheAttribute foundCacheAttribute = this.cacheFlushAttributeSource
        .findAttribute(allAttributes);

    assertNull(foundCacheAttribute);
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractCacheFlushAttributeSource#getCacheFlushAttribute(Method, Class)}</code>
   * returns an instance of <code>{@link FlushCache}</code>.
   */
  public void testGetCacheFlushAttribute() throws Exception {
    this.setUpTargetClassAndMethod();

    FlushCache flushCache = this.cacheFlushAttributeSource
        .getCacheFlushAttribute(this.method, this.targetClass);

    assertNotNull(flushCache);
  }

}