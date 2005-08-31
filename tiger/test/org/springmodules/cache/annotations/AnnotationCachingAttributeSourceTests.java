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
 * Unit Test for <code>{@link AnnotationCachingAttributeSource}s</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.3 $ $Date: 2005/08/31 01:22:35 $
 */
public class AnnotationCachingAttributeSourceTests extends TestCase {

  /**
   * A method containing JDK 1.5+ Annotations.
   */
  private Method annotatedMethod;

  /**
   * Primary object that is under test.
   */
  private AnnotationCachingAttributeSource cachingAttributeSource;

  public AnnotationCachingAttributeSourceTests(String name) {
    super(name);
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();

    this.cachingAttributeSource = new AnnotationCachingAttributeSource();

    Class targetClass = TigerCacheableService.class;
    this.annotatedMethod = targetClass.getDeclaredMethod("getName",
        new Class[] { int.class });
  }

  public void testFindAllAttributes() throws Exception {
    Collection expectedAnnotations = Arrays.asList(this.annotatedMethod
        .getAnnotations());

    Collection actualAnnotations = this.cachingAttributeSource
        .findAllAttributes(this.annotatedMethod);

    assertEquals("<Annotations>", expectedAnnotations, actualAnnotations);
  }

  public void testFindAttribute() {
    Collection attributes = Arrays
        .asList(this.annotatedMethod.getAnnotations());

    Cacheable expected = this.annotatedMethod.getAnnotation(Cacheable.class);

    Cached actual = (Cached) this.cachingAttributeSource
        .findAttribute(attributes);

    assertEquals("<Cache profile Id>", expected.cacheProfileId(), actual
        .getCacheProfileId());
  }

  public void testFindAttributeWithCollectionOfAttributesEqualToNull() {
    assertNull(this.cachingAttributeSource.findAttribute(null));
  }
  
  public void testFindAttributeWithCollectionOfAttributesWithoutCachingAttributes() {
    Collection<Object> attributes = new ArrayList<Object>();
    attributes.add("Luke Skywalker");

    assertNull(this.cachingAttributeSource.findAttribute(attributes));
  }
}
