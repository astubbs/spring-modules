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

import org.springmodules.cache.interceptor.caching.Cached;

/**
 * <p>
 * Unit Tests for <code>{@link AnnotationCachingAttributeSource}s</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.6 $ $Date: 2005/10/13 04:52:45 $
 */
public class AnnotationCachingAttributeSourceTests extends TestCase {

  /**
   * A method containing JDK 1.5+ Annotations.
   */
  private Method annotatedMethod;

  private AnnotationCachingAttributeSource cachingAttributeSource;

  public AnnotationCachingAttributeSourceTests(String name) {
    super(name);
  }

  @Override
  protected void setUp() throws Exception {
    cachingAttributeSource = new AnnotationCachingAttributeSource();

    Class targetClass = TigerCacheableService.class;
    annotatedMethod = targetClass.getDeclaredMethod("getName",
        new Class[] { int.class });
  }

  public void testFindAllAttributes() throws Exception {
    Collection expected = Arrays.asList(annotatedMethod.getAnnotations());
    Collection actual = cachingAttributeSource
        .findAllAttributes(annotatedMethod);

    assertEquals(expected, actual);
  }

  public void testFindAttribute() {
    Collection attributes = Arrays.asList(annotatedMethod.getAnnotations());
    Cacheable expected = annotatedMethod.getAnnotation(Cacheable.class);
    Cached actual = (Cached) cachingAttributeSource.findAttribute(attributes);

    assertEquals(expected.modelId(), actual.getModelId());
  }

  public void testFindAttributeWithCollectionOfAttributesEqualToNull() {
    assertNull(cachingAttributeSource.findAttribute(null));
  }

  public void testFindAttributeWithCollectionOfAttributesWithoutCachingAttributes() {
    Collection<Object> attributes = new ArrayList<Object>();
    attributes.add("Luke Skywalker");

    assertNull(cachingAttributeSource.findAttribute(attributes));
  }
}
