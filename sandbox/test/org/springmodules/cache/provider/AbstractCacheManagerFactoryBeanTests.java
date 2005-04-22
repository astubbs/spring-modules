/* 
 * Created on Oct 18, 2004
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
 * Copyright @2004 the original author or authors.
 */

package org.springmodules.cache.provider;

import junit.framework.TestCase;

/**
 * <p>
 * Test Case for factories of Cache Managers.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/04/22 02:19:12 $
 */
public abstract class AbstractCacheManagerFactoryBeanTests extends TestCase {

  /**
   * Constructor.
   */
  public AbstractCacheManagerFactoryBeanTests() {
    super();
  }

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the Test Case.
   */
  public AbstractCacheManagerFactoryBeanTests(String name) {
    super(name);
  }

  /**
   * Returns the name of the package of this class as a file path.
   * 
   * @return the name of the package of this class as a file path.
   */
  protected final String getPackageNameAsPath() {
    String packageName = this.getClass().getPackage().getName();
    String path = packageName.replace('.', '/');
    return path;
  }

  /**
   * Tests the implementation of the method <code>afterPropertiesSet()</code>
   * of the interface
   * <code>org.springframework.beans.factory.InitializingBean</code>.
   */
  public abstract void testAfterPropertiesSet() throws Exception;

  /**
   * Tests the implementation of the method <code>destroy()</code> of the
   * interface <code>org.springframework.beans.factory.FactoryBean</code>.
   */
  public abstract void testDestroy() throws Exception;

  /**
   * Tests the implementation of the method <code>getObjectType()</code> of
   * the interface <code>org.springframework.beans.factory.FactoryBean</code>.
   */
  public abstract void testGetObjectType() throws Exception;

  /**
   * Tests the implementation of the method <code>isSingleton()</code> of the
   * inteface <code>org.springframework.beans.factory.FactoryBean</code>.
   */
  public abstract void testIsSingleton();

}