/* 
 * Created on Oct 19, 2004
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

package org.springmodules.cache.interceptor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import junit.framework.TestCase;

import org.easymock.classextension.MockClassControl;
import org.springmodules.cache.CacheAttribute;
import org.springmodules.cache.mock.MockCacheAttribute;

/**
 * <p>
 * Unit Tests for
 * <code>{@link AbstractSingleMetadataCacheAttributeSource}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public final class SingleMetadataCacheAttributeSourceTests extends TestCase {

  /**
   * Collection to be used as return value for the method
   * <code>{@link AbstractMetadataCacheAttributeSource#findAllAttributes(Method)}</code>.
   * This collection is supposed to contain all the metadata attributes for a
   * given <code>Method</code>.
   */
  private Collection allAttributes;

  private AbstractSingleMetadataCacheAttributeSource attributeSource;

  private MockClassControl attributeSourceControl;

  /**
   * Cache attribute to be stored in <code>{@link #allAttributes}</code>.
   */
  private CacheAttribute cacheAttribute;

  /**
   * Method definition containing metadata attributes.
   */
  private Method method;

  /**
   * Class declaring <code>{@link #method}</code>.
   */
  private Class targetClass;

  public SingleMetadataCacheAttributeSourceTests(String name) {
    super(name);
  }

  /**
   * Asserts that the specified metadata attribute was cached by
   * <code>{@link AbstractSingleMetadataCacheAttributeSource}</code>.
   * 
   * @param expectedCachedAttribute
   *          the attribute that is expected to be cached.
   */
  private void assertCacheAttributeIsCached(Object expectedCachedAttribute) {
    Map attributeMap = attributeSource.getAttributeMap();
    assertFalse(attributeMap.isEmpty());

    boolean found = false;
    for (Iterator i = attributeMap.entrySet().iterator(); i.hasNext();) {
      Map.Entry attributeMapEntry = (Map.Entry) i.next();
      found = (expectedCachedAttribute == attributeMapEntry.getValue());
    }

    assertTrue("The metadata attribute '" + expectedCachedAttribute
        + "' was not cached.", found);
  }

  protected void setUp() throws Exception {
    setUpSingleAttributeSourceAsMockObject();

    targetClass = String.class;
    method = targetClass.getMethod("charAt", new Class[] { int.class });

    cacheAttribute = new MockCacheAttribute();

    allAttributes = new ArrayList();
  }

  private void setUpSingleAttributeSourceAsMockObject() throws Exception {
    Class classToMock = AbstractSingleMetadataCacheAttributeSource.class;
    Class superOfClassToMock = AbstractMetadataCacheAttributeSource.class;

    Method findAllAttributesMethod = superOfClassToMock.getDeclaredMethod(
        "findAllAttributes", new Class[] { Method.class });

    Method findAttributeMethod = classToMock.getDeclaredMethod("findAttribute",
        new Class[] { Collection.class });

    Method[] methodsToMock = new Method[] { findAllAttributesMethod,
        findAttributeMethod };

    attributeSourceControl = MockClassControl.createStrictControl(classToMock,
        null, null, methodsToMock);

    attributeSource = (AbstractSingleMetadataCacheAttributeSource) attributeSourceControl
        .getMock();
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractSingleMetadataCacheAttributeSource#getAttribute(Method, Class)}</code>
   * stores
   * <code>{@link AbstractMetadataCacheAttributeSource#NULL_ATTRIBUTE}</code>
   * as an metadata attribute if the map of attributes returns <code>null</code>.
   */
  public void testGetAttributeWhenCacheAttributeFoundIsEqualToNull() {
    // retrieve the metadata attributes for the given method two times: the
    // first time for the most specific method and second time for the original
    // method.
    for (int i = 0; i < 2; i++) {
      // get all the metadata attributes.
      attributeSourceControl.expectAndReturn(attributeSource
          .findAllAttributes(method), allAttributes);

      // should not be able to find a CacheAttribute from the collection
      // containing all the metadata attributes.
      attributeSourceControl.expectAndReturn(attributeSource
          .findAttribute(allAttributes), null);
    }

    attributeSourceControl.replay();

    // execute the method to test.
    CacheAttribute returnedAttribute = attributeSource.getAttribute(method,
        targetClass);

    // we are expecting not to find the cache attribute.
    assertNull(returnedAttribute);

    // verify that the object representing a null cache attribute was cached.
    assertCacheAttributeIsCached(AbstractMetadataCacheAttributeSource.NULL_ATTRIBUTE);

    attributeSourceControl.verify();
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractSingleMetadataCacheAttributeSource#getAttribute(Method, Class)}</code>
   * retrieves and caches the non-null metadata attribute for the given class
   * and method.
   */
  public void testGetAttributeWhenCacheAttributeFoundIsNotEqualToNull() {
    attributeSourceControl.expectAndReturn(attributeSource
        .findAllAttributes(method), allAttributes);

    attributeSourceControl.expectAndReturn(attributeSource
        .findAttribute(allAttributes), cacheAttribute);

    attributeSourceControl.replay();

    // execute the method to test.
    CacheAttribute returnedCacheAttribute = attributeSource.getAttribute(
        method, targetClass);

    assertSame(cacheAttribute, returnedCacheAttribute);
    assertCacheAttributeIsCached(cacheAttribute);

    attributeSourceControl.verify();
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractSingleMetadataCacheAttributeSource#getAttribute(Method, Class)}</code>
   * returns <code>null</code> if the cached attribute is equal to
   * <code>{@link AbstractMetadataCacheAttributeSource#NULL_ATTRIBUTE}</code>.
   */
  public void testGetAttributeWhenCacheAttributeFoundIsNullAttribute() {
    // add NULL_ATTRIBUTE to the map of cached attributes.
    Map attributeMap = attributeSource.getAttributeMap();

    StringBuffer keyBuffer = new StringBuffer(targetClass.toString());
    keyBuffer.append(System.identityHashCode(method));

    attributeMap.put(keyBuffer.toString(),
        AbstractMetadataCacheAttributeSource.NULL_ATTRIBUTE);

    attributeSourceControl.replay();

    // execute the method to test.
    CacheAttribute actual = attributeSource.getAttribute(method, targetClass);
    assertNull(actual);

    attributeSourceControl.verify();
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractSingleMetadataCacheAttributeSource#retrieveAttribute(Method, Class)}</code>
   * stops searching for cache attributes when it finds a cache attribute for
   * the most specific method.
   */
  public void testRetrieveAttributeFromMostSpecificMethod() {
    attributeSourceControl.expectAndReturn(attributeSource
        .findAllAttributes(method), allAttributes);

    attributeSourceControl.expectAndReturn(attributeSource
        .findAttribute(allAttributes), cacheAttribute);

    attributeSourceControl.replay();

    // execute the method to test.
    CacheAttribute returnedCacheAttribute = attributeSource.retrieveAttribute(
        method, targetClass);

    assertSame(cacheAttribute, returnedCacheAttribute);

    attributeSourceControl.verify();
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractSingleMetadataCacheAttributeSource#retrieveAttribute(Method, Class)}</code>
   * stops searching for cache attributes when it does not find any cache
   * attribute for the most specific method if the most specific method and the
   * original method are the same.
   */
  public void testRetrieveAttributeFromMostSpecificMethodWhenMostSpecificMethodAndOriginalMethodAreTheSame() {
    attributeSourceControl.expectAndReturn(attributeSource
        .findAllAttributes(method), allAttributes);

    attributeSourceControl.expectAndReturn(attributeSource
        .findAttribute(allAttributes), null);

    attributeSourceControl.replay();

    // we are not using the target class we set up for this test case. We send
    // 'int.class' to trick 'AopUtils.getMostSpecificMethod(..)', making the
    // most specific method and the original method the same.
    CacheAttribute returnedCacheAttribute = attributeSource.retrieveAttribute(
        method, int.class);

    assertNull(returnedCacheAttribute);

    attributeSourceControl.verify();
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractSingleMetadataCacheAttributeSource#retrieveAttribute(Method, Class)}</code>
   * searches for cache attributes for the original method if:
   * <ul>
   * <li>There are not any cache attributes for the most specific method</li>
   * <li>The most specific method is not the same as the original method</li>
   * </ul>
   */
  public void testRetrieveAttributeFromOriginalMethod() {
    attributeSourceControl.expectAndReturn(attributeSource
        .findAllAttributes(method), allAttributes);

    attributeSourceControl.expectAndReturn(attributeSource
        .findAttribute(allAttributes), null);

    attributeSourceControl.expectAndReturn(attributeSource
        .findAllAttributes(method), allAttributes);

    attributeSourceControl.expectAndReturn(attributeSource
        .findAttribute(allAttributes), cacheAttribute);

    attributeSourceControl.replay();

    // execute the method to test.
    CacheAttribute returnedCacheAttribute = attributeSource.retrieveAttribute(
        method, targetClass);

    assertSame(cacheAttribute, returnedCacheAttribute);

    attributeSourceControl.verify();
  }

  /**
   * Tests
   * <code>{@link AbstractSingleMetadataCacheAttributeSource#retrieveAttribute(Method, Class)}</code>.
   * Verifies that when the returned metadata attribute is <code>null</code>,
   * <code>{@link AbstractSingleMetadataCacheAttributeSource#findAttribute(Collection)}</code>
   * is called two times: for the most specific method and for the original
   * method.
   */
  public void testRetrieveAttributeReturningNull() {
    // retrieve the metadata attributes for the given method two times: the
    // first time for the most specific method and second time for the original
    // method.
    for (int i = 0; i < 2; i++) {
      // get all the metadata attributes.
      attributeSourceControl.expectAndReturn(attributeSource
          .findAllAttributes(method), allAttributes);

      attributeSourceControl.expectAndReturn(attributeSource
          .findAttribute(allAttributes), null);
    }

    attributeSourceControl.replay();

    // execute the method to test.
    CacheAttribute returnedCacheAttribute = attributeSource.retrieveAttribute(
        method, targetClass);

    assertNull(returnedCacheAttribute);

    attributeSourceControl.verify();
  }

}