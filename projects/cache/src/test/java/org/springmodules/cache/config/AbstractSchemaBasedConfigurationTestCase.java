/* 
 * Created on Mar 9, 2006
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

import junit.framework.TestCase;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.ParserContext;

/**
 * <p>
 * Test cases for parsers used for schema-based configuration.
 * </p>
 * 
 * @author Alex Ruiz
 */
public abstract class AbstractSchemaBasedConfigurationTestCase extends TestCase {

  protected ParserContext parserContext;

  protected BeanDefinitionRegistry registry;

  /**
   * Constructor.
   */
  public AbstractSchemaBasedConfigurationTestCase() {
    super();
  }

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case
   */
  public AbstractSchemaBasedConfigurationTestCase(String name) {
    super(name);
  }

  protected void onSetUp() throws Exception {
    // no implementation.
  }

  protected final void setUp() throws Exception {
    parserContext = ParserContextFactory.create();
    registry = parserContext.getRegistry();

    onSetUp();
  }
}
