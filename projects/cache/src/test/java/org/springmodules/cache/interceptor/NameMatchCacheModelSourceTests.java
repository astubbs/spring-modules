/* 
 * Created on Jan 19, 2005
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

package org.springmodules.cache.interceptor;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.springmodules.cache.CacheModel;

/**
 * <p>
 * Unit Tests for <code>{@link AbstractNameMatchCacheModelSource}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public final class NameMatchCacheModelSourceTests extends TestCase {

  private Map cacheModels;

  private Method method;

  private AbstractNameMatchCacheModelSource source;

  public NameMatchCacheModelSourceTests(String name) {
    super(name);
  }

  public void testGetCacheModelWithMappedNameEqualToMethodName() {
    String mappedName = method.getName();
    assertSame(putNewCacheModelInMap(mappedName), source.getCacheModel(method));
  }

  public void testGetCacheModelWithMatchingMappedNameBeingBestMatch() {
    String methodName = method.getName();
    String mappedName = methodName.substring(0, methodName.length() - 4) + "*";
    putNewCacheModelInMap(mappedName);

    String bestMatchMappedName = methodName.substring(0,
        methodName.length() - 3)
        + "*";
    assertSame(putNewCacheModelInMap(bestMatchMappedName), source
        .getCacheModel(method));
  }

  public void testGetCacheModelWithMatchingMappedNameEndingWithWildcard() {
    String methodName = method.getName();
    String mappedName = methodName.substring(0, methodName.length() - 2) + "*";
    assertSame(putNewCacheModelInMap(mappedName), source.getCacheModel(method));
  }

  public void testGetCacheModelWithMatchingMappedNameStartingWithWildcard() {
    String mappedName = "*" + method.getName().substring(2);
    assertSame(putNewCacheModelInMap(mappedName), source.getCacheModel(method));
  }

  public void testGetCacheModelWithNotMatchingMappedName() {
    putNewCacheModelInMap("toString");
    assertNull(source.getCacheModel(method));
  }

  protected void setUp() throws Exception {
    method = String.class.getDeclaredMethod("toLowerCase", new Class[0]);

    cacheModels = new HashMap();

    source = new AbstractNameMatchCacheModelSource() {
      // no extra implementation.
    };
    source.setCacheModels(cacheModels);
  }

  private CacheModel putNewCacheModelInMap(String key) {
    CacheModel model = new CacheModel() {
      private static final long serialVersionUID = 4608839803287089680L;
    };
    cacheModels.put(key, model);
    return model;
  }
}