/* 
 * Created on Apr 29, 2005
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
package org.springmodules.cache.annotations;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import junit.framework.TestCase;

import org.springmodules.cache.interceptor.flush.FlushCache;

/**
 * <p>
 * Unit Tests for <code>{@link AnnotationFlushingAttributeSource}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class AnnotationCacheFlushAttributeSourceTests extends TestCase {

  /**
   * A method containing JDK 1.5+ Annotations.
   */
  private Method annotatedMethod;

  private AnnotationFlushingAttributeSource cacheFlushAttributeSource;

  public AnnotationCacheFlushAttributeSourceTests(String name) {
    super(name);
  }

  @Override
  protected void setUp() throws Exception {
    cacheFlushAttributeSource = new AnnotationFlushingAttributeSource();

    Class targetClass = TigerCacheableService.class;
    annotatedMethod = targetClass.getDeclaredMethod("updateName", new Class[] {
        int.class, String.class });
  }

  public void testFindAllAttributes() throws Exception {
    Collection expected = Arrays.asList(annotatedMethod.getAnnotations());
    Collection actual = cacheFlushAttributeSource
        .findAllAttributes(annotatedMethod);

    assertEquals(expected, actual);
  }

  public void testFindAttribute() {
    Collection attributes = Arrays.asList(annotatedMethod.getAnnotations());
    CacheFlush expected = annotatedMethod.getAnnotation(CacheFlush.class);
    FlushCache actual = (FlushCache) cacheFlushAttributeSource
        .findAttribute(attributes);

    assertEquals(expected.modelId(), actual.getModelId());
  }

  public void testFindAttributeWithCollectionOfAttributesEqualToNull() {
    assertNull(cacheFlushAttributeSource.findAttribute(null));
  }

  public void testFindAttributeWithCollectionOfAttributesWithoutCachingAttributes() {
    Collection<Object> attributes = new ArrayList<Object>();
    attributes.add("Anakin Skywalker");
    assertNull(cacheFlushAttributeSource.findAttribute(attributes));
  }
}
