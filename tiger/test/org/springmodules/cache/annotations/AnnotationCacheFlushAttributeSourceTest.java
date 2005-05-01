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

import org.springmodules.cache.interceptor.flush.FlushCache;

/**
 * <p>
 * Unit Test for <code>{@link AnnotationCacheFlushAttributeSource}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/05/01 23:20:15 $
 */
public class AnnotationCacheFlushAttributeSourceTest extends TestCase {

  /**
   * A method containing JDK 1.5+ Annotations.
   */
  private Method annotatedMethod;

  /**
   * Primary object that is under test.
   */
  private AnnotationCacheFlushAttributeSource cacheFlushAttributeSource;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case to construct.
   */
  public AnnotationCacheFlushAttributeSourceTest(String name) {
    super(name);
  }

  /**
   * Sets up the test fixture.
   */
  protected void setUp() throws Exception {
    super.setUp();

    this.cacheFlushAttributeSource = new AnnotationCacheFlushAttributeSource();

    Class targetClass = CacheableBean.class;
    this.annotatedMethod = targetClass.getDeclaredMethod("updateName",
        new Class[] { int.class, String.class });
  }

  /**
   * Verifies that the method
   * <code>{@link AnnotationCacheFlushAttributeSource#findAllAttributes(Method)}</code>
   * returns all the JDK 1.5+ Annotations for a given method.
   */
  public void testFindAllAttributes() throws Exception {
    Collection expectedAnnotations = Arrays.asList(this.annotatedMethod
        .getAnnotations());

    Collection actualAnnotations = this.cacheFlushAttributeSource
        .findAllAttributes(this.annotatedMethod);

    assertEquals("<Annotations>", expectedAnnotations, actualAnnotations);
  }

  /**
   * Verifies that the method
   * <code>{@link AnnotationCacheFlushAttributeSource#findAttribute(Collection)}</code>
   * returns an instance of <code>{@link FlushCache}</code> created from the
   * JDK 1.5+ Annotation <code>{@link CacheFlush}</code> contained in the
   * given collection of attributes.
   */
  public void testFindAttribute() {
    Collection attributes = Arrays
        .asList(this.annotatedMethod.getAnnotations());

    CacheFlush expected = this.annotatedMethod.getAnnotation(CacheFlush.class);

    FlushCache actual = (FlushCache) this.cacheFlushAttributeSource
        .findAttribute(attributes);

    assertTrue("<Cache profile Id>", Arrays.equals(expected.cacheProfileIds(),
        actual.getCacheProfileIds()));

    assertEquals("<'flushBeforeExecution'>", expected.flushBeforeExecution(),
        actual.isFlushBeforeExecution());
  }

}
