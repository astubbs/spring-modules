/* 
 * Created on Feb 3, 2006
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

import java.io.InputStream;
import java.lang.reflect.Method;

import junit.framework.TestCase;

import org.easymock.classextension.MockClassControl;
import org.w3c.dom.Element;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceEditor;

import org.springmodules.cache.provider.AbstractCacheManagerFactoryBean;
import org.springmodules.cache.provider.CacheProviderFacade;
import org.springmodules.cache.provider.PathUtils;

/**
 * <p>
 * Unit Tests for <code>{@link AbstractCacheManagerAndProviderFacadeParser}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class CacheManagerAndProviderFacadeParserTests extends TestCase {

  private static abstract class BeanName {

    private static final String CACHE_MANAGER = "cacheProvider.cacheManager";

    private static final String CACHE_PROVIDER_FACADE = "cacheProvider";
  }

  private class ConfigStruct {
    String configLocation = "";

    Element toXml() {
      Element element = new DomElementStub("config");
      element.setAttribute("configLocation", configLocation);
      return element;
    }
  }

  private static abstract class PropertyName {

    static final String CACHE_MANAGER = "cacheManager";

    static final String CONFIG_LOCATION = "configLocation";
  }

  private static final String CACHE_CONFIG_LOCATION = "classpath:"
      + PathUtils
          .getPackageNameAsPath(CacheManagerAndProviderFacadeParserTests.class)
      + "/fakeConfigLocation.xml";

  private Class cacheManagerClass;

  private RootBeanDefinition cacheProviderFacade;

  private ConfigStruct config;

  private AbstractCacheManagerAndProviderFacadeParser parser;

  private MockClassControl parserControl;

  private BeanDefinitionRegistry registry;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case
   */
  public CacheManagerAndProviderFacadeParserTests(String name) {
    super(name);
  }

  public void testDoParseWithEmptyConfigLocation() {
    config.configLocation = "";
    Element element = config.toXml();

    expectGetCacheManagerClass();
    parserControl.replay();

    parser.doParse(BeanName.CACHE_PROVIDER_FACADE, element, registry);
    assertNull(
        "Property '" + PropertyName.CONFIG_LOCATION + "' should be null",
        getCacheManagerProperty(PropertyName.CONFIG_LOCATION));

    assertCacheProviderFacadeHasCacheManagerAsProperty();
  }

  public void testDoParseWithExistingConfigLocation() throws Exception {
    config.configLocation = CACHE_CONFIG_LOCATION;
    Element element = config.toXml();

    expectGetCacheManagerClass();
    parserControl.replay();

    parser.doParse(BeanName.CACHE_PROVIDER_FACADE, element, registry);
    Resource configLocation = (Resource) getCacheManagerProperty(PropertyName.CONFIG_LOCATION);

    ResourceEditor editor = new ResourceEditor();
    editor.setAsText(CACHE_CONFIG_LOCATION);
    String expectedContent = getResourceContent((Resource) editor.getValue());
    String actualContent = getResourceContent(configLocation);
    assertEquals("<Config resource content>", expectedContent, actualContent);

    assertCacheProviderFacadeHasCacheManagerAsProperty();
  }

  protected void setUp() throws Exception {
    config = new ConfigStruct();
    cacheManagerClass = AbstractCacheManagerFactoryBean.class;

    Class targetClass = AbstractCacheManagerAndProviderFacadeParser.class;

    Method getCacheManagerClassMethod = targetClass.getDeclaredMethod(
        "getCacheManagerClass", new Class[0]);

    Method[] methodsToMock = { getCacheManagerClassMethod };

    parserControl = MockClassControl.createControl(targetClass, null, null,
        methodsToMock);
    parser = (AbstractCacheManagerAndProviderFacadeParser) parserControl.getMock();
    registry = new DefaultListableBeanFactory();

    cacheProviderFacade = new RootBeanDefinition(CacheProviderFacade.class);
    cacheProviderFacade.setPropertyValues(new MutablePropertyValues());
    registry.registerBeanDefinition(BeanName.CACHE_PROVIDER_FACADE,
        cacheProviderFacade);
  }

  protected void tearDown() {
    parserControl.verify();
  }

  private void assertCacheProviderFacadeHasCacheManagerAsProperty() {
    MutablePropertyValues propertyValues = cacheProviderFacade
        .getPropertyValues();
    PropertyValue propertyValue = propertyValues
        .getPropertyValue(PropertyName.CACHE_MANAGER);
    RuntimeBeanReference cacheManager = (RuntimeBeanReference) propertyValue
        .getValue();
    assertEquals(BeanName.CACHE_MANAGER, cacheManager.getBeanName());
  }

  private void expectGetCacheManagerClass() {
    parser.getCacheManagerClass();
    parserControl.setReturnValue(cacheManagerClass);
  }

  private RootBeanDefinition getCacheManager() {
    RootBeanDefinition cacheManager = (RootBeanDefinition) registry
        .getBeanDefinition(BeanName.CACHE_MANAGER);
    return cacheManager;
  }

  private Object getCacheManagerProperty(String propertyName) {
    RootBeanDefinition cacheManager = getCacheManager();
    MutablePropertyValues propertyValues = cacheManager.getPropertyValues();
    PropertyValue property = propertyValues.getPropertyValue(propertyName);
    assertNotNull("Property '" + propertyName + "' not found", property);
    return property.getValue();
  }

  private String getResourceContent(Resource resource) throws Exception {
    InputStream inputStream = resource.getInputStream();

    StringBuffer out = new StringBuffer();
    byte[] b = new byte[4096];
    for (int n; (n = inputStream.read(b)) != -1;) {
      out.append(new String(b, 0, n));
    }
    return out.toString();
  }

}
