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
 * Test Cases for factories of Cache Managers.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.4 $ $Date: 2005/09/06 01:41:39 $
 */
public abstract class AbstractCacheManagerFactoryBeanTests extends TestCase {

  public AbstractCacheManagerFactoryBeanTests() {
    super();
  }

  public AbstractCacheManagerFactoryBeanTests(String name) {
    super(name);
  }

  /**
   * <p>
   * Returns the name of the package of this class as a file path.
   * </p>
   * <p>
   * For example, the name of the package <code>org.springmodules.cache</code>
   * will be converted to org/springmoudles/cache.
   * </p>
   * 
   * @return the name of the package of this class as a file path.
   */
  protected final String getPackageNameAsPath() {
    String packageName = getClass().getPackage().getName();
    String path = packageName.replace('.', '/');
    return path;
  }

  public abstract void testAfterPropertiesSet() throws Exception;

  public abstract void testDestroy() throws Exception;

  public abstract void testGetObjectType() throws Exception;

  public abstract void testIsSingleton();

}