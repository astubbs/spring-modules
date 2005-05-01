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
 * @version $Revision: 1.1 $ $Date: 2005/05/01 23:20:15 $
 */
public class AnnotationCachingAttributeSourceTest extends TestCase {

  /**
   * A method containing JDK 1.5+ Annotations.
   */
  private Method annotatedMethod;

  /**
   * Primary object that is under test.
   */
  private AnnotationCachingAttributeSource cachingAttributeSource;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case to construct.
   */
  public AnnotationCachingAttributeSourceTest(String name) {
    super(name);
  }

  /**
   * Sets up the test fixture.
   */
  @Override
  protected void setUp() throws Exception {
    super.setUp();

    this.cachingAttributeSource = new AnnotationCachingAttributeSource();

    Class targetClass = CacheableBean.class;
    this.annotatedMethod = targetClass.getDeclaredMethod("getName",
        new Class[] { int.class });
  }

  /**
   * Verifies that the method
   * <code>{@link AnnotationCachingAttributeSource#findAllAttributes(Method)}</code>
   * returns all the JDK 1.5+ Annotations for a given method.
   */
  public void testFindAllAttributes() throws Exception {
    Collection expectedAnnotations = Arrays.asList(this.annotatedMethod
        .getAnnotations());

    Collection actualAnnotations = this.cachingAttributeSource
        .findAllAttributes(this.annotatedMethod);

    assertEquals("<Annotations>", expectedAnnotations, actualAnnotations);
  }

  /**
   * Verifies that the method
   * <code>{@link AnnotationCachingAttributeSource#findAttribute(Collection)}</code>
   * returns an instance of <code>{@link Cached}</code> created from the JDK
   * 1.5+ Annotation <code>{@link Cacheable}</code> contained in the given
   * collection of attributes.
   */
  public void testFindAttribute() {
    Collection attributes = Arrays
        .asList(this.annotatedMethod.getAnnotations());

    Cacheable expected = this.annotatedMethod.getAnnotation(Cacheable.class);

    Cached actual = (Cached) this.cachingAttributeSource
        .findAttribute(attributes);

    assertEquals("<Cache profile Id>", expected.cacheProfileId(), actual
        .getCacheProfileId());
  }

}
