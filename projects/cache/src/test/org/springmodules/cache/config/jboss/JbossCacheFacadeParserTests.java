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
package org.springmodules.cache.config.jboss;

import junit.framework.TestCase;

import org.springmodules.cache.provider.jboss.JbossCacheFacade;
import org.springmodules.cache.provider.jboss.JbossCacheManagerFactoryBean;

/**
 * <p>
 * Unit Tests for <code>{@link JbossCacheFacadeParser}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class JbossCacheFacadeParserTests extends TestCase {

  private JbossCacheFacadeParser parser;

  public void testGetCacheManagerClass() {
    assertEquals("<Cache Manager class>", JbossCacheManagerFactoryBean.class,
        parser.getCacheManagerClass());
  }

  public void testGetCacheProviderFacadeClass() {
    assertEquals("<Cache Provider Facade class>", JbossCacheFacade.class, parser
        .getCacheProviderFacadeClass());
  }

  protected void setUp() {
    parser = new JbossCacheFacadeParser();
  }
}
