/* 
 * Created on Mar 8, 2006
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

/**
 * <p>
 * Unit Tests for <code>{@link EhCacheAnnotationsParser}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class EhCacheAnnotationsParserTests extends
    AbstractEhCacheSetupStrategyTestCase {

  private EhCacheAnnotationsParser parser;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case
   */
  public EhCacheAnnotationsParserTests(String name) {
    super(name);
  }

  @Override
  protected Object getCacheModelParser() {
    return parser.getCacheModelParser();
  }

  @Override
  protected Object getCacheProviderFacadeDefinitionValidator() {
    return parser.getCacheProviderFacadeDefinitionValidator();
  }

  @Override
  protected void setUp() {
    parser = new EhCacheAnnotationsParser();
  }

}
