/* 
 * Created on Oct 27, 2005
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
package org.springmodules.cache.interceptor.caching;

import java.lang.reflect.Method;

import junit.framework.TestCase;

/**
 * <p>
 * Unit Tests for <code>{@link CachingUtils}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class CachingUtilsTests extends TestCase {

  public CachingUtilsTests(String name) {
    super(name);
  }

  public void testIsCacheableWithCacheableMethod() throws Exception {
    Method method = MethodFactory.createCacheableMethod();
    assertTrue(CachingUtils.isCacheable(method));
  }

  public void testIsCacheableWithNotCacheableMethod() throws Exception {
    Method method = MethodFactory.createNonCacheableMethod();
    assertFalse(CachingUtils.isCacheable(method));
  }

}
