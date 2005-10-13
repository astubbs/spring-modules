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
import java.util.List;

import junit.framework.TestCase;

import org.easymock.MockControl;
import org.springframework.metadata.Attributes;
import org.springmodules.cache.CacheAttribute;

/**
 * <p>
 * Unit Tests for <code>{@link MetadataCachingAttributeSource}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.5 $ $Date: 2005/10/13 04:52:06 $
 */
public final class MetadataCachingAttributeSourceTests extends TestCase {

  private Attributes attributes;

  private MockControl attributesControl;

  /**
   * Method to get the caching attributes for.
   */
  private Method method;

  private MetadataCachingAttributeSource source;

  /**
   * Class declaring <code>{@link #method}</code>.
   */
  private Class targetClass;

  public MetadataCachingAttributeSourceTests(String name) {
    super(name);
  }

  protected void setUp() {
    attributesControl = MockControl.createControl(Attributes.class);
    attributes = (Attributes) attributesControl.getMock();

    source = new MetadataCachingAttributeSource();
    source.setAttributes(attributes);
  }

  /**
   * @param useCacheableMethod
   *          if <code>true</code>, a method with return type other than
   *          <code>void</code> will be used. If <code>false</code>, a
   *          method with return type <code>void</code> will be used.
   */
  private void setUpTargetClassAndMethod(boolean useCacheableMethod)
      throws Exception {

    targetClass = String.class;

    if (useCacheableMethod) {
      method = targetClass.getMethod("charAt", new Class[] { int.class });
    } else {
      method = targetClass.getMethod("notify", null);
    }
  }

  public void testFindAllAttributesMethod() throws Exception {
    setUpTargetClassAndMethod(true);

    List attributeList = new ArrayList();
    attributes.getAttributes(method);
    attributesControl.setReturnValue(attributeList);
    attributesControl.replay();

    Collection returnedAttributes = source.findAllAttributes(method);

    assertSame(attributeList, returnedAttributes);

    attributesControl.verify();
  }

  public void testFindAttribute() {
    Cached expected = new Cached();
    Collection allAttributes = new ArrayList();
    allAttributes.add(expected);

    assertSame(expected, source.findAttribute(allAttributes));
  }

  /**
   * Verifies that the method
   * <code>{@link MetadataCachingAttributeSource#findAttribute(Collection)}</code>
   * does not return any <code>{@link CacheAttribute}</code> if the given
   * collection of metadata attributes is equal to <code>null</code>.
   */
  public void testFindAttributeWithCollectionEqualToNull() {
    CacheAttribute foundCacheAttribute = source.findAttribute(null);
    assertNull(foundCacheAttribute);
  }

  /**
   * Verifies that the method
   * <code>{@link MetadataCachingAttributeSource#findAttribute(Collection)}</code>
   * does not return any <code>{@link CacheAttribute}</code> if the given
   * collection of metadata attributes is empty.
   */
  public void testFindAttributeWithEmptyCollection() {
    assertNull(source.findAttribute(new ArrayList()));
  }

  /**
   * Verifies that the method
   * <code>{@link MetadataCachingAttributeSource#findAttribute(Collection)}</code>
   * returns <code>null</code> if the given collection of metadata attributes
   * is not empty but does not contain any instance of
   * <code>{@link Cached}</code>.
   */
  public void testFindAttributeWithCollectionNotHavingCacheAttributes() {
    Collection allAttributes = new ArrayList();
    allAttributes.add("A String!");
    assertNull(source.findAttribute(allAttributes));
  }

  /**
   * Verifies that the method
   * <code>{@link MetadataCachingAttributeSource#getCachingAttribute(Method, Class)}</code>
   * returns an instance of <code>{@link Cached}</code> if the return value of
   * the specified method is not <code>void</code>.
   */
  public void testGetCachingAttributeWithCacheableMethod() throws Exception {
    setUpTargetClassAndMethod(true);

    Cached expected = new Cached();

    List attributeList = new ArrayList();
    attributeList.add(expected);

    attributes.getAttributes(method);
    attributesControl.setReturnValue(attributeList);
    attributesControl.replay();

    assertSame(expected, source.getCachingAttribute(method, targetClass));

    attributesControl.verify();

  }

  /**
   * Verifies that the method
   * <code>{@link MetadataCachingAttributeSource#getCachingAttribute(Method, Class)}</code>
   * returns <code>null</code> if the return value of the specified method is
   * <code>void</code>.
   */
  public void testGetCachingAttributeWithNotCacheableMethod() throws Exception {
    setUpTargetClassAndMethod(false);
    assertNull(source.getCachingAttribute(method, targetClass));
  }

  public void testIsCacheableWithCacheableMethod() throws Exception {
    setUpTargetClassAndMethod(true);
    assertTrue(source.isCacheable(method));
  }

  public void testIsCacheableWithNotCacheableMethod() throws Exception {
    setUpTargetClassAndMethod(false);
    assertFalse(source.isCacheable(method));
  }

}