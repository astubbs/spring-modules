/* 
 * Created on Feb 12, 2006
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

import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;

import org.springmodules.cache.provider.ehcache.EhCacheFacade;

/**
 * <p>
 * Unit Tests for <code>{@link EhCacheFacadeParser}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class EhCacheFacadeParserTests extends TestCase {

  private EhCacheFacadeParser parser;

  public void testGetCacheManagerClass() {
    assertEquals("<Cache Manager class>", EhCacheManagerFactoryBean.class,
        parser.getCacheManagerClass());
  }

  public void testGetCacheProviderFacadeClass() {
    assertEquals("<Cache Provider Facade class>", EhCacheFacade.class, parser
        .getCacheProviderFacadeClass());
  }

  protected void setUp() {
    parser = new EhCacheFacadeParser();
  }
}
