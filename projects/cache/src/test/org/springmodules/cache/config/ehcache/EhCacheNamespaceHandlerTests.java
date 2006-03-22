/* 
 * Created on Mar 15, 2006
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
 * Copyright @2006 the original author or authors.
 */
package org.springmodules.cache.config.ehcache;

import org.springmodules.AssertExt;

import junit.framework.TestCase;

/**
 * <p>
 * Unit Tests for <code>{@link EhCacheNamespaceHandler}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class EhCacheNamespaceHandlerTests extends TestCase {

  private EhCacheNamespaceHandler handler;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case
   */
  public EhCacheNamespaceHandlerTests(String name) {
    super(name);
  }

  public void testGetCacheModelParser() {
    AssertExt.assertInstanceOf(EhCacheModelParser.class, handler
        .getCacheModelParser());
  }

  public void testGetCacheProviderFacadeParser() {
    AssertExt.assertInstanceOf(EhCacheFacadeParser.class, handler
        .getCacheProviderFacadeParser());
  }

  protected void setUp() {
    handler = new EhCacheNamespaceHandler();
  }

}
