/* 
 * Created on Sep 1, 2005
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

package org.springmodules.cache.provider.jboss;

import junit.framework.TestCase;

import org.jboss.cache.TreeCache;
import org.springframework.core.io.ClassPathResource;
import org.springmodules.cache.provider.PathUtils;

/**
 * <p>
 * Unit Tests for <code>{@link JbossCacheManagerFactoryBean}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public final class JbossCacheManagerFactoryBeanTests extends TestCase {

  private JbossCacheManagerFactoryBean cacheManagerFactoryBean;

  public JbossCacheManagerFactoryBeanTests(String name) {
    super(name);
  }

  private void assertObjectTypeIsCorrect() {
    Class actualObjectType = cacheManagerFactoryBean.getObjectType();
    assertEquals(TreeCache.class, actualObjectType);
  }

  private TreeCache getCacheManager() {
    return (TreeCache) cacheManagerFactoryBean.getObject();
  }

  protected void setUp() {
    cacheManagerFactoryBean = new JbossCacheManagerFactoryBean();
  }

  protected void tearDown() {
    if (cacheManagerFactoryBean != null) {
      try {
        cacheManagerFactoryBean.destroy();
      } catch (Exception exception) {
        // ignore the exception.
      }
    }
  }

  public void testCreateCacheManager() throws Exception {
    cacheManagerFactoryBean.createCacheManager();
    assertNotNull(getCacheManager());
  }

  public void testCreateCacheManagerWithConfigLocation() throws Exception {
    String configLocationPath = PathUtils.getPackageNameAsPath(getClass())
        + "/cache-service.xml";

    cacheManagerFactoryBean.setConfigLocation(new ClassPathResource(
        configLocationPath));

    cacheManagerFactoryBean.createCacheManager();

    TreeCache cacheManager = getCacheManager();
    assertEquals(12345l, cacheManager.getSyncReplTimeout());
  }

  public void testDestroyCacheManager() throws Exception {
    // TODO Implement method.
  }

  public void testGetObjectType() throws Exception {
    cacheManagerFactoryBean.createCacheManager();
    assertObjectTypeIsCorrect();
  }

  public void testGetObjectTypeWhenCacheAdministratorIsNull() {
    assertObjectTypeIsCorrect();
  }

  public void testIsSingleton() {
    assertTrue(cacheManagerFactoryBean.isSingleton());
  }
}