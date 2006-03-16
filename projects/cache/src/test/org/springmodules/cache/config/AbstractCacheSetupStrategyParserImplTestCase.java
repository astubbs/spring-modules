/* 
 * Created on Mar 6, 2006
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
package org.springmodules.cache.config;

import java.util.ArrayList;
import java.util.HashMap;

import org.easymock.MockControl;

import org.springframework.beans.factory.config.RuntimeBeanReference;

/**
 * <p>
 * Test cases for implementations of
 * <code>{@link AbstractCacheSetupStrategyParser}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public abstract class AbstractCacheSetupStrategyParserImplTestCase extends
    AbstractSchemaBasedConfigurationTestCase {

  protected BeanReferenceParser beanReferenceParser;

  protected MockControl beanReferenceParserControl;

  protected CacheSetupStrategyPropertySource propertySource;

  /**
   * Constructor.
   */
  public AbstractCacheSetupStrategyParserImplTestCase() {
    super();
  }

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case
   */
  public AbstractCacheSetupStrategyParserImplTestCase(String name) {
    super(name);
  }

  protected void afterSetUp() throws Exception {
    // no implementation.
  }

  protected final void onSetUp() throws Exception {
    propertySource = new CacheSetupStrategyPropertySource(
        new RuntimeBeanReference("cacheKeyGenerator"),
        new RuntimeBeanReference("cacheProvider"), new ArrayList(),
        new HashMap(), new HashMap());

    afterSetUp();
  }

  protected final void setUpBeanReferenceParser() {
    beanReferenceParserControl = MockControl
        .createControl(BeanReferenceParser.class);
    beanReferenceParser = (BeanReferenceParser) beanReferenceParserControl
        .getMock();
  }
}
