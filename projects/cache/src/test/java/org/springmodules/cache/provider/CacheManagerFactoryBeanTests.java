/* 
 * Created on Sep 22, 2005
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
 * Copyright @2005 the original author or authors.
 */
package org.springmodules.cache.provider;

import java.util.Properties;

import junit.framework.TestCase;

import org.springframework.core.io.ClassPathResource;

/**
 * <p>
 * Unit Tests for <code>{@link AbstractCacheManagerFactoryBean}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class CacheManagerFactoryBeanTests extends TestCase {

  protected class MockCacheManagerFactoryBean extends
      AbstractCacheManagerFactoryBean {

    Object cacheManager;

    boolean cacheManagerCreated;

    boolean cacheManagerDestroyed;

    public Object getObject() throws Exception {
      return cacheManager;
    }

    public Class getObjectType() {
      return null;
    }

    protected void createCacheManager() throws Exception {
      cacheManagerCreated = true;
    }

    protected void destroyCacheManager() throws Exception {
      cacheManagerDestroyed = true;
    }

    protected String getCacheProviderName() {
      return null;
    }
  }

  private MockCacheManagerFactoryBean factoryBean;

  public CacheManagerFactoryBeanTests(String name) {
    super(name);
  }

  public void testAfterPropertiesSet() throws Exception {
    factoryBean.afterPropertiesSet();
    assertTrue(factoryBean.cacheManagerCreated);
  }

  public void testDestroyWithCacheManagerEqualToNull() throws Exception {
    factoryBean.cacheManager = null;
    factoryBean.destroy();
    assertFalse(factoryBean.cacheManagerDestroyed);
  }

  public void testDestroyWithCacheManagerNotEqualToNull() throws Exception {
    factoryBean.cacheManager = new Object();
    factoryBean.destroy();
    assertTrue(factoryBean.cacheManagerDestroyed);
  }

  public void testGetConfigProperties() throws Exception {
    String path = PathUtils.getPackageNameAsPath(getClass())
        + "/cacheProvider.properties";

    ClassPathResource resource = new ClassPathResource(path);
    Properties expected = new Properties();
    expected.load(resource.getInputStream());

    factoryBean.setConfigLocation(new ClassPathResource(path));
    assertEquals(expected, factoryBean.getConfigProperties());
  }

  protected void setUp() {
    factoryBean = new MockCacheManagerFactoryBean();
  }

}
