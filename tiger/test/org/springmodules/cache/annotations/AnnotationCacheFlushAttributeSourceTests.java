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
 * Unit Test for <code>{@link AnnotationCacheFlushAttributeSource}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.4 $ $Date: 2005/09/06 01:41:42 $
 */
public class AnnotationCacheFlushAttributeSourceTests extends TestCase {

  /**
   * A method containing JDK 1.5+ Annotations.
   */
  private Method annotatedMethod;

  private AnnotationCacheFlushAttributeSource cacheFlushAttributeSource;

  public AnnotationCacheFlushAttributeSourceTests(String name) {
    super(name);
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();

    this.cacheFlushAttributeSource = new AnnotationCacheFlushAttributeSource();

    Class targetClass = TigerCacheableService.class;
    this.annotatedMethod = targetClass.getDeclaredMethod("updateName",
        new Class[] { int.class, String.class });
  }

  public void testFindAllAttributes() throws Exception {
    Collection expectedAnnotations = Arrays.asList(this.annotatedMethod
        .getAnnotations());

    Collection actualAnnotations = this.cacheFlushAttributeSource
        .findAllAttributes(this.annotatedMethod);

    assertEquals("<Annotations>", expectedAnnotations, actualAnnotations);
  }

  public void testFindAttribute() {
    Collection attributes = Arrays
        .asList(this.annotatedMethod.getAnnotations());

    CacheFlush expected = this.annotatedMethod.getAnnotation(CacheFlush.class);

    FlushCache actual = (FlushCache) this.cacheFlushAttributeSource
        .findAttribute(attributes);

    assertTrue("<Cache profile Id>", Arrays.equals(expected.cacheProfileIds(),
        actual.getCacheProfileIds()));
    assertEquals("<Flag 'flushBeforeExecution'>", expected
        .flushBeforeExecution(), actual.isFlushBeforeExecution());
  }

  public void testFindAttributeWithCollectionOfAttributesEqualToNull() {
    assertNull(this.cacheFlushAttributeSource.findAttribute(null));
  }
  
  public void testFindAttributeWithCollectionOfAttributesWithoutCachingAttributes() {
    Collection<Object> attributes = new ArrayList<Object>();
    attributes.add("Anakin Skywalker");

    assertNull(this.cacheFlushAttributeSource.findAttribute(attributes));
  }
}
