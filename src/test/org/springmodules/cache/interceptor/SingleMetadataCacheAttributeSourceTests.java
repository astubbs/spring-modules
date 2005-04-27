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
import java.util.Set;

import junit.framework.TestCase;

import org.easymock.classextension.MockClassControl;
import org.springmodules.cache.CacheAttribute;
import org.springmodules.cache.mock.MockCacheAttribute;

/**
 * <p>
 * Unit Test for <code>{@link AbstractSingleMetadataCacheAttributeSource}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/04/27 01:41:20 $
 */
public final class SingleMetadataCacheAttributeSourceTests extends
    TestCase {

  /**
   * Collection to be used as the return value of the abstract method
   * <code>{@link AbstractMetadataCacheAttributeSource#findAllAttributes(Method)}</code>
   * which is supposed to contain all the metadata attributes for a given
   * method.
   */
  private Collection allAttributes;

  /**
   * Primary object (instance of the class to test).
   */
  private AbstractSingleMetadataCacheAttributeSource attributeSource;

  /**
   * Controls the behavior and mocks the abstract methods of
   * <code>{@link #attributeSource}</code>.
   */
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

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the Test Case.
   */
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

    Map attributeMap = this.attributeSource.getAttributeMap();
    assertFalse("The map of cached attributes should not be empty",
        attributeMap.isEmpty());

    Set attributeMapEntrySet = attributeMap.entrySet();
    Iterator attributeMapEntryIterator = attributeMapEntrySet.iterator();

    boolean found = false;
    while (attributeMapEntryIterator.hasNext()) {
      Map.Entry attributeMapEntry = (Map.Entry) attributeMapEntryIterator
          .next();

      Object actualCachedAttribute = attributeMapEntry.getValue();
      found = (expectedCachedAttribute == actualCachedAttribute);
    }

    assertTrue("The metadata attribute '" + expectedCachedAttribute
        + "' was not cached.", found);
  }

  /**
   * Sets up the test fixture.
   */
  protected void setUp() throws Exception {
    super.setUp();

    this.setUpSingleAttributeSource();

    this.targetClass = String.class;
    this.method = this.targetClass.getMethod("charAt",
        new Class[] { int.class });

    this.cacheAttribute = new MockCacheAttribute();

    this.allAttributes = new ArrayList();
  }

  /**
   * Sets up:
   * <ul>
   * <li><code>{@link #attributeSource}</code></li>
   * <li><code>{@link #attributeSourceControl}</code></li>
   * </ul>
   */
  private void setUpSingleAttributeSource() throws Exception {

    // set up the class to mock.
    Class classToMock = AbstractSingleMetadataCacheAttributeSource.class;

    // set up the abstract methods to mock.
    Method findAllAttributesMethod = AbstractMetadataCacheAttributeSource.class
        .getDeclaredMethod("findAllAttributes", new Class[] { Method.class });

    Method findAttributeMethod = classToMock.getDeclaredMethod("findAttribute",
        new Class[] { Collection.class });

    Method[] methodsToMock = new Method[] { findAllAttributesMethod,
        findAttributeMethod };

    // create the mock control.
    this.attributeSourceControl = MockClassControl.createControl(classToMock,
        null, null, methodsToMock);

    // create the mock object.
    this.attributeSource = (AbstractSingleMetadataCacheAttributeSource) this.attributeSourceControl
        .getMock();
  }

  /**
   * Tests
   * <code>{@link AbstractSingleMetadataCacheAttributeSource#getAttribute(Method, Class)}</code>.
   * Verifies that the first time <code>getAttribute(..)</code> returns
   * <code>null</code>,
   * <code>{@link AbstractMetadataCacheAttributeSource#NULL_ATTRIBUTE}</code>
   * is cached. From the second time it is called, it should always return
   * <code>null</code>.
   */
  public void testGetAttributeWhenCacheAttributeFoundIsEqualToNull() {

    // expectation: retrieve the metadata attributes for the given method two
    // times: the first time for the most specific method and second time for
    // the original method.
    for (int i = 0; i < 2; i++) {
      // expectation: get all the metadata attributes.
      this.attributeSource.findAllAttributes(this.method);
      this.attributeSourceControl.setReturnValue(this.allAttributes);

      // expectation: should not be able to find a CacheAttribute from the
      // collection containing all the metadata attributes.
      this.attributeSource.findAttribute(this.allAttributes);
      this.attributeSourceControl.setReturnValue(null);
    }

    // set the state of the mock control to "replay".
    this.attributeSourceControl.replay();

    // execute the method to test.
    CacheAttribute returnedAttribute = this.attributeSource.getAttribute(
        this.method, this.targetClass);

    // we are expecting not to find the cache attribute.
    assertNull("The returned CacheAttribute should be null", returnedAttribute);

    // verify that the object represing a null cache attribute was cached.
    Object expectedCachedAttribute = AbstractMetadataCacheAttributeSource.NULL_ATTRIBUTE;
    this.assertCacheAttributeIsCached(expectedCachedAttribute);

    // verify that the expectations of the mock control were met.
    this.attributeSourceControl.verify();
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractSingleMetadataCacheAttributeSource#getAttribute(Method, Class)}</code>
   * retrieves and caches the (non-null) metadata attribute for the given class
   * and method.
   */
  public void testGetAttributeWhenCacheAttributeFoundIsNotEqualToNull() {

    // expectation: get all the metadata attributes for the most specific
    // method.
    this.attributeSource.findAllAttributes(this.method);
    this.attributeSourceControl.setReturnValue(this.allAttributes);

    // expectation: find a CacheAttribute from the collection containing all
    // the metadata attributes.
    this.attributeSource.findAttribute(this.allAttributes);
    this.attributeSourceControl.setReturnValue(this.cacheAttribute);

    // set the state of the mock control to "replay".
    this.attributeSourceControl.replay();

    // execute the method to test.
    CacheAttribute returnedCacheAttribute = this.attributeSource.getAttribute(
        this.method, this.targetClass);

    // verify that the returned attribute is the same we were expecting.
    assertSame("<Returned CacheAttribute>", this.cacheAttribute,
        returnedCacheAttribute);

    // verify that the returned attribute is cached.
    this.assertCacheAttributeIsCached(this.cacheAttribute);

    // verify that the expectations of the mock control were met.
    this.attributeSourceControl.verify();
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractSingleMetadataCacheAttributeSource#getAttribute(Method, Class)}</code>
   * returns <code>null</code> if the cached attribute is equal to
   * <code>{@link AbstractMetadataCacheAttributeSource#NULL_ATTRIBUTE}</code>.
   */
  public void testGetAttributeWhenCacheAttributeFoundIsNullAttribute() {

    // add NULL_ATTRIBUTE to the map of cached attributes.
    Map attributeMap = this.attributeSource.getAttributeMap();

    StringBuffer keyBuffer = new StringBuffer(64);
    keyBuffer.append(this.targetClass);
    keyBuffer.append(System.identityHashCode(this.method));

    attributeMap.put(keyBuffer.toString(),
        AbstractMetadataCacheAttributeSource.NULL_ATTRIBUTE);

    // set the state of the mock control to "replay".
    this.attributeSourceControl.replay();

    // execute the method to test.
    CacheAttribute returnedCacheAttribute = this.attributeSource.getAttribute(
        this.method, this.targetClass);

    // verify that the returned attribute is the same we were expecting.
    assertNull("The returned CacheAttribute should be null",
        returnedCacheAttribute);

    // verify that the expectations of the mock control were met.
    this.attributeSourceControl.verify();
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractSingleMetadataCacheAttributeSource#retrieveAttribute(Method, Class)}</code>
   * stops searching for cache attributes when it finds a cache attribute for
   * the most specific method.
   */
  public void testRetrieveAttributeFromMostSpecificMethod() {

    // expectation: get all the metadata attributes for the most specific
    // method.
    this.attributeSource.findAllAttributes(this.method);
    this.attributeSourceControl.setReturnValue(this.allAttributes);

    // expectation: find a CacheAttribute from the collection containing all
    // the metadata attributes.
    this.attributeSource.findAttribute(this.allAttributes);
    this.attributeSourceControl.setReturnValue(this.cacheAttribute);

    // set the state of the mock control to "replay".
    this.attributeSourceControl.replay();

    // execute the method to test.
    CacheAttribute returnedCacheAttribute = this.attributeSource
        .retrieveAttribute(this.method, this.targetClass);

    // verify that the returned attribute is the same we were expecting.
    assertSame("<Returned CacheAttribute>", this.cacheAttribute,
        returnedCacheAttribute);

    // verify that the expectations of the mock control were met.
    this.attributeSourceControl.verify();
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractSingleMetadataCacheAttributeSource#retrieveAttribute(Method, Class)}</code>
   * stops searching for cache attributes when it does not find any cache
   * attribute for the most specific method if the most specific method and the
   * original method are the same.
   */
  public void testRetrieveAttributeFromMostSpecificMethodWhenMostSpecificMethodAndOriginalMethodAreTheSame() {

    // expectation: get all the metadata attributes for the most specific
    // method.
    this.attributeSource.findAllAttributes(this.method);
    this.attributeSourceControl.setReturnValue(this.allAttributes);

    // expectation: find a CacheAttribute from the collection containing all
    // the metadata attributes.
    this.attributeSource.findAttribute(this.allAttributes);
    this.attributeSourceControl.setReturnValue(null);

    // set the state of the mock control to "replay".
    this.attributeSourceControl.replay();

    // we are not using the target class we set up for this test case. We
    // send 'int.class' to trick 'AopUtils.getMostSpecificMethod(..)', making
    // the most specific method and the original method the same.
    CacheAttribute returnedCacheAttribute = this.attributeSource
        .retrieveAttribute(this.method, int.class);

    // verify that the returned attribute is the same we were expecting.
    assertNull("The returned CacheAttribute should be null",
        returnedCacheAttribute);

    // verify that the expectations of the mock control were met.
    this.attributeSourceControl.verify();
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

    // expectation: get all the metadata attributes for the most specific
    // method.
    this.attributeSource.findAllAttributes(this.method);
    this.attributeSourceControl.setReturnValue(this.allAttributes);

    // expectation: should not be able to find a CacheAttribute from the
    // collection containing all the metadata attributes.
    this.attributeSource.findAttribute(this.allAttributes);
    this.attributeSourceControl.setReturnValue(null);

    // expectation: get all the metadata attributes for the original
    // method.
    this.attributeSource.findAllAttributes(this.method);
    this.attributeSourceControl.setReturnValue(this.allAttributes);

    // expectation: should not be able to find a CacheAttribute from the
    // collection containing all the metadata attributes.
    this.attributeSource.findAttribute(this.allAttributes);
    this.attributeSourceControl.setReturnValue(this.cacheAttribute);

    // set the state of the mock control to "replay".
    this.attributeSourceControl.replay();

    // execute the method to test.
    CacheAttribute returnedCacheAttribute = this.attributeSource
        .retrieveAttribute(this.method, this.targetClass);

    // verify that the returned attribute is the same we were expecting.
    assertSame("<Returned CacheAttribute>", this.cacheAttribute,
        returnedCacheAttribute);

    // verify that the expectations of the mock control were met.
    this.attributeSourceControl.verify();
  }

  /**
   * Tests
   * <code>{@link AbstractSingleMetadataCacheAttributeSource#retrieveAttribute(Method, Class)}</code>.
   * Verifies that when the returned metadata attribute is <code>null</code>,
   * <code>{@link AbstractSingleMetadataCacheAttributeSource#findAttribute(Collection)}</code>
   * is called two times.
   */
  public void testRetrieveAttributeReturningNull() {

    // expectation: retrieve the metadata attributes for the given method two
    // times: the first time for the most specific method and second time for
    // the original method.
    for (int i = 0; i < 2; i++) {
      // expectation: get all the metadata attributes.
      this.attributeSource.findAllAttributes(this.method);
      this.attributeSourceControl.setReturnValue(this.allAttributes);

      // expectation: should not be able to find a CacheAttribute from the
      // collection containing all the metadata attributes.
      this.attributeSource.findAttribute(this.allAttributes);
      this.attributeSourceControl.setReturnValue(null);
    }

    // set the state of the mock control to "replay".
    this.attributeSourceControl.replay();

    // execute the method to test.
    CacheAttribute returnedCacheAttribute = this.attributeSource
        .retrieveAttribute(this.method, this.targetClass);

    assertNull("The returned CacheAttribute should be null",
        returnedCacheAttribute);

    // verify that the expectations of the mock control were met.
    this.attributeSourceControl.verify();
  }
}