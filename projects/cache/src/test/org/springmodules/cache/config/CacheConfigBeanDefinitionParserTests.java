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
import org.springframework.beans.factory.config.RuntimeBeanReference;
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

  private static class BeanName {

    static final String CACHE_MANAGER = "cacheProvider.cacheManager";

    static final String CACHE_PROVIDER = "cacheProvider";
  }

  private static class CacheProvider {

    static final String EHCACHE = "EHCACHE";

    static final String INVALID = "INVALID";

    static final String JBOSS_CACHE = "JBOSS_CACHE";

    static final String JCS = "JCS";

    static final String OSCACHE = "OSCACHE";
  }

  private class ConfigStruct {

    String configLocation = "";

    Boolean failQuietlyEnabled = null;

    String id = "";

    String provider = "";

    String serializableFactory = "";

    Element toXml() {
      Element element = new DomElementStub("config");
      element.setAttribute("id", id);
      element.setAttribute("provider", provider);
      element.setAttribute("configLocation", configLocation);
      String failQuietlyProperty = "";
      if (failQuietlyEnabled != null) {
        failQuietlyProperty = failQuietlyEnabled.booleanValue() ? "true"
            : "false";
      }
      element.setAttribute("failQuietly", failQuietlyProperty);
      element.setAttribute("serializableFactory", serializableFactory);
      return element;
    }
  }

  private static class PropertyName {

    static final String CACHE_MANAGER = "cacheManager";

    static final String FAIL_QUIETLY = "failQuietlyEnabled";

    static final String PROVIDER = "provider";

    static final String SERIALIZABLE_FACTORY = "serializableFactory";
  }

  private static class SerializableFactory {

    static final String INVALID = "INVALID";

    static final String NONE = "NONE";

    static final String XSTREAM = "XSTREAM";
  }

  private static final String CONFIG_LOCATION = "classpath:"
      + PathUtils
          .getPackageNameAsPath(CacheConfigBeanDefinitionParserTests.class)
      + "/fakeConfigLocation.xml";

  private ConfigStruct config;

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
    config.failQuietlyEnabled = Boolean.FALSE;
    parser.parse(config.toXml(), registry);

    assertFailQuietlyEnabledPropertyIsCorrect(Boolean.FALSE);
  }

  public void testParseWithFailQuietlyEnabledEqualToTrue() {
    config.failQuietlyEnabled = Boolean.TRUE;
    parser.parse(config.toXml(), registry);

    assertFailQuietlyEnabledPropertyIsCorrect(Boolean.TRUE);
  }

  public void testParseWithFailQuietlyNotSet() {
    config.failQuietlyEnabled = null;
    parser.parse(config.toXml(), registry);

    assertFailQuietlyEnabledPropertyIsCorrect(null);
  }

  public void testParseWithProviderEqualToEhCache() {
    config.provider = CacheProvider.EHCACHE;
    parser.parse(config.toXml(), registry);

    assertCacheProviderClassIsCorrect(EhCacheFacade.class);
    assertCacheProviderHasReferenceToCacheManager();
    assertCacheManagerClassIsCorrect(EhCacheManagerFactoryBean.class);
  }

  public void testParseWithProviderEqualToJbossCache() {
    config.provider = CacheProvider.JBOSS_CACHE;
    parser.parse(config.toXml(), registry);

    assertCacheProviderClassIsCorrect(JbossCacheFacade.class);
    assertCacheProviderHasReferenceToCacheManager();
    assertCacheManagerClassIsCorrect(JbossCacheManagerFactoryBean.class);
  }

  public void testParseWithProviderEqualToJcs() {
    config.provider = CacheProvider.JCS;
    parser.parse(config.toXml(), registry);

    assertCacheProviderClassIsCorrect(JcsFacade.class);
    assertCacheProviderHasReferenceToCacheManager();
    assertCacheManagerClassIsCorrect(JcsManagerFactoryBean.class);
  }

  public void testParseWithProviderEqualToOsCache() {
    config.provider = CacheProvider.OSCACHE;
    parser.parse(config.toXml(), registry);

    assertCacheProviderClassIsCorrect(OsCacheFacade.class);
    assertCacheProviderHasReferenceToCacheManager();
    assertCacheManagerClassIsCorrect(OsCacheManagerFactoryBean.class);
  }

  /**
   * Verifies that the method
   * <code>{@link CacheConfigBeanDefinitionParser#parse(Element, BeanDefinitionRegistry)}</code>
   * throws an IllegalStateException if the XML attribute "provider" contains an
   * invalid value.
   */
  public void testParseWithProviderHavingInvalidValue() {
    config.provider = CacheProvider.INVALID;
    assertParseThrowsExceptionIfAnyAttributeHasInvalidValue();
  }

  public void testParseWithSerializableFactoryEqualToNone() {
    config.serializableFactory = SerializableFactory.NONE;
    parser.parse(config.toXml(), registry);

    assertSerializableFactoryIsNull();
  }

  public void testParseWithSerializableFactoryEqualToXStream() {
    config.serializableFactory = SerializableFactory.XSTREAM;
    parser.parse(config.toXml(), registry);

    assertSerializableFactoryIsCorrect(XStreamSerializableFactory.class);
  }

  /**
   * Verifies that the method
   * <code>{@link CacheConfigBeanDefinitionParser#parse(Element, BeanDefinitionRegistry)}</code>
   * throws an IllegalStateException if the XML attribute "serializableFactory"
   * contains an invalid value.
   */
  public void testParseWithSerializableFactoryHavingInvalidValue() {
    config.serializableFactory = SerializableFactory.INVALID;
    assertParseThrowsExceptionIfAnyAttributeHasInvalidValue();
  }

  public void testParseWithSerializableFactoryNotSet() {
    config.serializableFactory = null;
    parser.parse(config.toXml(), registry);

    assertSerializableFactoryIsNull();
  }

  public void testParseWithSpecifiedConfigLocation() throws Exception {
    config.configLocation = CONFIG_LOCATION;
    parser.parse(config.toXml(), registry);

    RootBeanDefinition cacheManager = getCacheManagerFromRegistry();

    Resource actual = (Resource) getBeanDefinitionProperty(cacheManager,
        "configLocation");

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

    config = new ConfigStruct();
    config.id = BeanName.CACHE_PROVIDER;
    config.provider = CacheProvider.EHCACHE;
  }

  private void assertCacheManagerClassIsCorrect(Class expected) {
    RootBeanDefinition cacheManager = getCacheManagerFromRegistry();
    assertEquals("<Cache manager class>", expected, cacheManager.getBeanClass());
  }

  private void assertCacheProviderClassIsCorrect(Class expected) {
    RootBeanDefinition cacheProvider = getCacheProviderFromRegistry();
    assertEquals("<Cache provider facade class>", expected, cacheProvider
        .getBeanClass());
  }

  private void assertCacheProviderHasReferenceToCacheManager() {
    RuntimeBeanReference reference = (RuntimeBeanReference) getCacheProviderProperty(PropertyName.CACHE_MANAGER);
    assertEquals(BeanName.CACHE_MANAGER, reference.getBeanName());
  }

  private void assertFailQuietlyEnabledPropertyIsCorrect(Boolean expected) {
    Boolean failQuietlyEnabled = (Boolean) getCacheProviderProperty(PropertyName.FAIL_QUIETLY);
    assertEquals("<Property " + StringUtils.quote(PropertyName.FAIL_QUIETLY)
        + ">", expected, failQuietlyEnabled);
  }

  private void assertParseThrowsExceptionIfAnyAttributeHasInvalidValue() {
    try {
      parser.parse(config.toXml(), registry);
    } catch (IllegalStateException exception) {
      // we are expecting this exception
    }
  }

  private void assertSerializableFactoryIsCorrect(Class expected) {
    Object serializableFactory = getCacheProviderProperty(PropertyName.SERIALIZABLE_FACTORY);
    assertEquals(expected, serializableFactory.getClass());
  }

  private void assertSerializableFactoryIsNull() {
    Object serializableFactory = getCacheProviderProperty(PropertyName.SERIALIZABLE_FACTORY);
    assertNull(serializableFactory);
  }

  private RootBeanDefinition getBeanDefinitionFromRegistry(String beanName) {
    RootBeanDefinition beanDefinition = (RootBeanDefinition) registry
        .getBeanDefinition(beanName);
    return beanDefinition;
  }

  private Object getBeanDefinitionProperty(BeanDefinition beanDefinition,
      String propertyName) {
    PropertyValue propertyValue = beanDefinition.getPropertyValues()
        .getPropertyValue(propertyName);
    return propertyValue != null ? propertyValue.getValue() : null;
  }

  private RootBeanDefinition getCacheManagerFromRegistry() {
    return getBeanDefinitionFromRegistry(BeanName.CACHE_MANAGER);
  }

  private RootBeanDefinition getCacheProviderFromRegistry() {
    return getBeanDefinitionFromRegistry(BeanName.CACHE_PROVIDER);
  }

  private Object getCacheProviderProperty(String propertyName) {
    RootBeanDefinition cacheProvider = getCacheProviderFromRegistry();
    return getBeanDefinitionProperty(cacheProvider, propertyName);
  }
}
