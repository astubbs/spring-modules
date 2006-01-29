/* 
 * Created on Jan 21, 2006
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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.TestCase;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

import org.springmodules.cache.provider.PathUtils;
import org.springmodules.cache.provider.ehcache.EhCacheFacade;
import org.springmodules.cache.provider.jboss.JbossCacheFacade;
import org.springmodules.cache.provider.jboss.JbossCacheManagerFactoryBean;
import org.springmodules.cache.provider.jcs.JcsFacade;
import org.springmodules.cache.provider.jcs.JcsManagerFactoryBean;
import org.springmodules.cache.provider.oscache.OsCacheFacade;
import org.springmodules.cache.provider.oscache.OsCacheManagerFactoryBean;
import org.springmodules.cache.serializable.XStreamSerializableFactory;

/**
 * <p>
 * Unit Tests for <code>{@link CacheConfigBeanDefinitionParser}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class CacheConfigBeanDefinitionParserTests extends TestCase {

  private class ConfigElement {

    String configLocation = "";

    boolean failQuietlyEnabled = false;

    String id = "";

    String provider = "";

    String serializableFactory = "";

    Element toXml() {
      Element element = new DomElementStub("config");
      element.setAttribute("id", id);
      element.setAttribute("provider", provider);
      element.setAttribute("configLocation", configLocation);
      element
          .setAttribute("failQuietly", failQuietlyEnabled ? "true" : "false");
      element.setAttribute("serializableFactory", serializableFactory);
      return element;
    }
  }

  private static final String CACHE_MANAGER_ID = "cacheProvider.cacheManager";

  private static final String CACHE_PROVIDER_ID = "cacheProvider";

  private static final String EHCACHE_PROVIDER = "EHCACHE";

  private static final String FAKE_CONFIG_LOCATION = "classpath:"
      + PathUtils
          .getPackageNameAsPath(CacheConfigBeanDefinitionParserTests.class)
      + "/fakeConfigLocation.xml";

  private ConfigElement config;

  private CacheConfigBeanDefinitionParser parser;

  private BeanDefinitionRegistry registry;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the test case.
   */
  public CacheConfigBeanDefinitionParserTests(String name) {
    super(name);
  }

  public void testParseWithFailQuietlyEnabledEqualToFalse() {
    assertParseSetsFailQuietlyEnabled(false);
  }

  public void testParseWithFailQuietlyEnabledEqualToTrue() {
    assertParseSetsFailQuietlyEnabled(true);
  }

  /**
   * Verifies that the method
   * <code>{@link CacheConfigBeanDefinitionParser#parse(Element, BeanDefinitionRegistry)}</code>
   * throws an IllegalStateException if the XML attribute "provider" contains an
   * invalid value.
   */
  public void testParseWithProviderAttributeHavingInvalidValue() {
    config.provider = "MY_OWN{CACHE";
    Element configElement = config.toXml();
    assertParseThrowsExceptionIfAnyAttributeHasInvalidValue(configElement);
  }

  public void testParseWithProviderEqualToEhCache() {
    assertParseCreatesCacheProviderAndCacheManager(EHCACHE_PROVIDER,
        EhCacheFacade.class, EhCacheManagerFactoryBean.class);
  }

  public void testParseWithProviderEqualToJbossCache() {
    assertParseCreatesCacheProviderAndCacheManager("JBOSS_CACHE",
        JbossCacheFacade.class, JbossCacheManagerFactoryBean.class);
  }

  public void testParseWithProviderEqualToJcs() {
    assertParseCreatesCacheProviderAndCacheManager("JCS", JcsFacade.class,
        JcsManagerFactoryBean.class);
  }

  public void testParseWithProviderEqualToOsCache() {
    assertParseCreatesCacheProviderAndCacheManager("OSCACHE",
        OsCacheFacade.class, OsCacheManagerFactoryBean.class);
  }

  public void testParseWithSerializableFactoryEqualToNone() {
    assertParseSetsSerializableFactory("NONE", null);
  }

  public void testParseWithSerializableFactoryEqualToXStream() {
    assertParseSetsSerializableFactory("XSTREAM",
        XStreamSerializableFactory.class);
  }

  /**
   * Verifies that the method
   * <code>{@link CacheConfigBeanDefinitionParser#parse(Element, BeanDefinitionRegistry)}</code>
   * throws an IllegalStateException if the XML attribute "serializableFactory"
   * contains an invalid value.
   */
  public void testParseWithSerializableFactoryHavingInvalidValue() {
    config.serializableFactory = "MY_OWN_FACTORY";
    assertParseThrowsExceptionIfAnyAttributeHasInvalidValue(config.toXml());
  }

  public void testParseWithSerializableFactoryNotSet() {
    assertParseSetsSerializableFactory("", null);
  }

  public void testParseWithSpecifiedConfigLocation() throws Exception {
    config.configLocation = FAKE_CONFIG_LOCATION;
    parser.parse(config.toXml(), registry);

    RootBeanDefinition cacheManager = (RootBeanDefinition) registry
        .getBeanDefinition(CACHE_MANAGER_ID);

    Resource actual = (Resource) getProperty(cacheManager, "configLocation");

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder docBuilder = factory.newDocumentBuilder();
    Document configDocument = docBuilder.parse(actual.getInputStream());
    Element root = configDocument.getDocumentElement();
    assertEquals("configLocation", root.getNodeName());
    assertEquals(0, root.getChildNodes().getLength());
  }

  protected void setUp() {
    parser = new CacheConfigBeanDefinitionParser();
    registry = new DefaultListableBeanFactory();

    config = new ConfigElement();
    config.id = CACHE_PROVIDER_ID;
    config.provider = EHCACHE_PROVIDER;
  }

  private void assertEqualsBeanDefinitionClass(String beanName, Class beanClass) {
    RootBeanDefinition beanDefinition = (RootBeanDefinition) registry
        .getBeanDefinition(beanName);
    assertEquals("<Bean definition " + StringUtils.quote(beanName)
        + " bean class>", beanClass, beanDefinition.getBeanClass());
  }

  private void assertParseCreatesCacheProviderAndCacheManager(String provider,
      Class cacheProviderClass, Class cacheManagerClass) {
    config.provider = provider;
    parser.parse(config.toXml(), registry);

    assertEqualsBeanDefinitionClass(CACHE_PROVIDER_ID, cacheProviderClass);
    assertEqualsBeanDefinitionClass(CACHE_MANAGER_ID, cacheManagerClass);
  }

  private void assertParseSetsFailQuietlyEnabled(boolean failQuietlyEnabled) {
    config.failQuietlyEnabled = failQuietlyEnabled;
    parser.parse(config.toXml(), registry);

    RootBeanDefinition cacheProvider = getCacheProvider();

    Boolean actual = (Boolean) getProperty(cacheProvider, "failQuietlyEnabled");
    assertEquals(failQuietlyEnabled, actual.booleanValue());
  }

  private void assertParseSetsSerializableFactory(String serializableFactory,
      Class serializableFactoryClass) {
    config.serializableFactory = serializableFactory;
    parser.parse(config.toXml(), registry);

    RootBeanDefinition cacheProvider = getCacheProvider();
    PropertyValue propertyValue = cacheProvider.getPropertyValues()
        .getPropertyValue("serializableFactory");

    if (serializableFactoryClass != null) {
      Object serializableFactoryBean = propertyValue.getValue();
      assertEquals(serializableFactoryClass, serializableFactoryBean.getClass());

    } else {
      assertNull(propertyValue);
    }
  }

  private void assertParseThrowsExceptionIfAnyAttributeHasInvalidValue(
      Element configElement) {
    try {
      parser.parse(configElement, registry);
    } catch (IllegalStateException exception) {
      // we are expecting this exception
    }
  }

  private RootBeanDefinition getCacheProvider() {
    RootBeanDefinition cacheProvider = (RootBeanDefinition) registry
        .getBeanDefinition(CACHE_PROVIDER_ID);
    return cacheProvider;
  }

  private Object getProperty(BeanDefinition beanDefinition, String propertyName) {
    return beanDefinition.getPropertyValues().getPropertyValue(propertyName)
        .getValue();
  }
}
