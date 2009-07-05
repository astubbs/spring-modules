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
import java.util.List;

import junit.framework.TestCase;

import org.easymock.MockControl;

import org.springframework.metadata.Attributes;

/**
 * <p>
 * Unit Tests for <code>{@link MetadataCachingAttributeSource}</code>.
 * </p>
 * 
 * @author Alex Ruiz
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

  /**
   * Verifies that the method
   * <code>{@link MetadataCachingAttributeSource#attribute(Method, Class)}</code>
   * returns an instance of <code>{@link Cached}</code> if the return value of
   * the specified method is not <code>void</code>.
   */
  public void testGetCachingAttributeWithCacheableMethod() throws Exception {
    setUpTargetClassAndCacheableMethod();

    Cached expected = new Cached();

    List attributeList = new ArrayList();
    attributeList.add(expected);

    attributesControl.expectAndReturn(attributes.getAttributes(method),
        attributeList);
    attributesControl.replay();

    assertSame(expected, source.attribute(method, targetClass));

    attributesControl.verify();

  }

  /**
   * Verifies that the method
   * <code>{@link MetadataCachingAttributeSource#attribute(Method, Class)}</code>
   * returns <code>null</code> if the return value of the specified method is
   * <code>void</code>.
   */
  public void testGetCachingAttributeWithNotCacheableMethod() throws Exception {
    setUpTargetClassAndNonCacheableMethod();
    assertNull(source.attribute(method, targetClass));
  }

  protected void setUp() {
    attributesControl = MockControl.createControl(Attributes.class);
    attributes = (Attributes) attributesControl.getMock();

    source = new MetadataCachingAttributeSource();
    source.setAttributes(attributes);
  }

  private void setUpTargetClassAndCacheableMethod() throws Exception {
    method = MethodFactory.createCacheableMethod();
    targetClass = method.getDeclaringClass();
  }

  private void setUpTargetClassAndNonCacheableMethod() throws Exception {
    method = MethodFactory.createNonCacheableMethod();
    targetClass = method.getDeclaringClass();
  }
}