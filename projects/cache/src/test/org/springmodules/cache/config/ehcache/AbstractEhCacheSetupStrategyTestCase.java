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

import junit.framework.TestCase;

import org.springmodules.AssertExt;

/**
 * <p>
 * Test Cases for cache setup strategies for EHCache.
 * </p>
 * 
 * @author Alex Ruiz
 */
public abstract class AbstractEhCacheSetupStrategyTestCase extends TestCase {

  /**
   * Constructor.
   */
  public AbstractEhCacheSetupStrategyTestCase() {
    super();
  }

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case
   */
  public AbstractEhCacheSetupStrategyTestCase(String name) {
    super(name);
  }

  public final void testCacheModelParserClass() {
    AssertExt.assertInstanceOf(EhCacheModelParser.class, getCacheModelParser());
  }

  protected abstract Object getCacheModelParser();
  
  protected abstract Object getCacheProviderFacadeDefinitionValidator();

}
