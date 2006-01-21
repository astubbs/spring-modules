/* 
 * Created on Jan 19, 2006
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

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Element;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceEditor;
import org.springframework.util.StringUtils;

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
 * Handles the parsing of a "cache:config" XML tag if present in the Spring
 * configuration file.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @see #parse(Element, BeanDefinitionRegistry)
 */
public class CacheConfigBeanDefinitionParser implements BeanDefinitionParser {

  private abstract class CacheProvider {

    static final String EHCACHE = "EHCACHE";

    static final String JBOSS_CACHE = "JBOSS_CACHE";

    static final String JCS = "JCS";

    static final String OSCACHE = "OSCACHE";
  }

  private abstract class PropertyName {

    static final String CACHE_MANAGER = "cacheManager";

    static final String CONFIG_LOCATION = "configLocation";

    static final String FAIL_QUIETLY_ENABLED = "failQuietlyEnabled";

    static final String SERIALIZABLE_FACTORY = "serializableFactory";
  }

  private abstract class SerializableFactory {

    static final String NONE = "NONE";

    static final String XSTREAM = "XSTREAM";
  }

  private abstract class XmlAttribute {

    static final String CONFIG_LOCATION = "configLocation";

    static final String FAIL_QUIETLY = "failQuietly";

    static final String ID = "id";

    static final String PROVIDER_NAME = "provider";

    static final String SERIALIZABLE_FACTORY = "serializableFactory";
  }

  /**
   * Configures the cache manager to use. The "cache:config" tag provides the
   * following attributes:
   * <ul>
   * <li><b>id:</b> identifies the cache manager in the Spring application
   * context. The default value is "cacheManager"</li>
   * <li> <b>provider (required):</b> the name of the cache provider to use.
   * Valid values are:
   * <ul>
   * <li><b>EHCACHE:</b> registers a
   * <code>{@link EhCacheManagerFactoryBean}</code> as the cache manager</li>
   * <li><b>JBOSS_CACHE:</b> registers a
   * <code>{@link JbossCacheManagerFactoryBean}</code> as the cache manager</li>
   * <li><b>JCS:</b> registers a <code>{@link JcsManagerFactoryBean}</code>
   * as the cache manager</li>
   * <li><b>OSCACHE:</b> registers a
   * <code>{@link OsCacheManagerFactoryBean}</code> as the cache manager</li>
   * </ul>
   * </li>
   * <li><b>configLocation (optional):</b> the path of the configuration file
   * for the cache manager</li>
   * <li><b>failQuietly (optional):</b> flag that indicates if any run-time
   * caching failure should be propagated stopping the execution of the
   * application. The default value is <code>false</code></li>
   * <li><b>serializableFactory (optional):</b>factory that forces objects to
   * be stored in the cache to be serializable. Valid values are:
   * <ul>
   * <li><b>none (default):</b> no factory is created</li>
   * <li><b>XSTREAM:</b> uses an instance of
   * <code>{@link XStreamSerializableFactory}</code> as the serializable
   * factory</li>
   * </ul>
   * </li>
   * </ul>
   * 
   * @throws IllegalArgumentException
   *           if the value of the XML attribute "provider" is not valid
   * @throws IllegalStateException
   *           if the value of the XML attribute "serializableFactory" is not
   *           valid
   * @see BeanDefinitionParser#parse(Element, BeanDefinitionRegistry)
   */
  public void parse(Element element, BeanDefinitionRegistry registry) {
    Class cacheManagerFactoryBeanClass = null;
    Class cacheProviderFacadeBeanClass = null;

    String providerName = element.getAttribute(XmlAttribute.PROVIDER_NAME);
    if (CacheProvider.EHCACHE.equalsIgnoreCase(providerName)) {
      cacheManagerFactoryBeanClass = EhCacheManagerFactoryBean.class;
      cacheProviderFacadeBeanClass = EhCacheFacade.class;

    } else if (CacheProvider.JBOSS_CACHE.equalsIgnoreCase(providerName)) {
      cacheManagerFactoryBeanClass = JbossCacheManagerFactoryBean.class;
      cacheProviderFacadeBeanClass = JbossCacheFacade.class;

    } else if (CacheProvider.JCS.equalsIgnoreCase(providerName)) {
      cacheManagerFactoryBeanClass = JcsManagerFactoryBean.class;
      cacheProviderFacadeBeanClass = JcsFacade.class;

    } else if (CacheProvider.OSCACHE.equalsIgnoreCase(providerName)) {
      cacheManagerFactoryBeanClass = OsCacheManagerFactoryBean.class;
      cacheProviderFacadeBeanClass = OsCacheFacade.class;

    } else {
      throw new IllegalStateException(StringUtils.quote(providerName)
          + " is not a valid provider. Valid values include "
          + StringUtils.quote(CacheProvider.EHCACHE) + ", "
          + StringUtils.quote(CacheProvider.JBOSS_CACHE) + ", "
          + StringUtils.quote(CacheProvider.JCS) + " and "
          + StringUtils.quote(CacheProvider.OSCACHE));
    }

    // create the cache manager factory bean
    String cacheProviderId = element.getAttribute(XmlAttribute.ID);
    String cacheManagerId = cacheProviderId + ".cacheManager";

    RootBeanDefinition cacheManager = new RootBeanDefinition(
        cacheManagerFactoryBeanClass);
    cacheManager.setPropertyValues(new MutablePropertyValues());
    Map cacheManagerProperties = parseCacheManagerFactoryBeanProperties(element);
    cacheManager.getPropertyValues().addPropertyValues(cacheManagerProperties);
    registry.registerBeanDefinition(cacheManagerId, cacheManager);

    // create the cache provider facade
    RootBeanDefinition cacheProviderFacade = new RootBeanDefinition(
        cacheProviderFacadeBeanClass);
    Map cacheProviderFacadeProperties = parseCacheProviderFacadeProperties(element);
    cacheProviderFacade.getPropertyValues().addPropertyValues(
        cacheProviderFacadeProperties);
    cacheProviderFacade.getPropertyValues().addPropertyValue(
        PropertyName.CACHE_MANAGER, new RuntimeBeanReference(cacheManagerId));
    registry.registerBeanDefinition(cacheProviderId, cacheProviderFacade);
  }

  /**
   * @param element
   *          the XML element containing the properties for the cache manager
   *          factory bean
   * @returns a map containing the properties for the cache manager factory bean
   */
  private Map parseCacheManagerFactoryBeanProperties(Element element) {
    Map properties = new HashMap();

    String configLocation = element.getAttribute(XmlAttribute.CONFIG_LOCATION);
    if (StringUtils.hasText(configLocation)) {
      ResourceEditor resourceEditor = new ResourceEditor();
      resourceEditor.setAsText(configLocation);
      Resource resource = (Resource) resourceEditor.getValue();

      properties.put(PropertyName.CONFIG_LOCATION, resource);
    }

    return properties;
  }

  /**
   * @param element
   *          the XML element containing the properties for the cache provider
   *          facade
   * @returns a map containing the properties for the cache provider facade
   * @throws IllegalStateException
   *           if the value of the XML attribute 'serializableFactory' is not
   *           valid
   */
  private Map parseCacheProviderFacadeProperties(Element element) {
    Map properties = new HashMap();

    String failQuietly = element.getAttribute(XmlAttribute.FAIL_QUIETLY);
    if (StringUtils.hasText(failQuietly)) {
      Boolean value = "true".equalsIgnoreCase(failQuietly) ? Boolean.TRUE
          : Boolean.FALSE;
      properties.put(PropertyName.FAIL_QUIETLY_ENABLED, value);
    }

    String serializableFactory = element
        .getAttribute(XmlAttribute.SERIALIZABLE_FACTORY);
    if (StringUtils.hasText(serializableFactory)) {
      if (!SerializableFactory.NONE.equalsIgnoreCase(serializableFactory)) {
        if (SerializableFactory.XSTREAM.equalsIgnoreCase(serializableFactory)) {
          properties.put(PropertyName.SERIALIZABLE_FACTORY,
              new XStreamSerializableFactory());
        } else {
          throw new IllegalStateException(StringUtils
              .quote(serializableFactory)
              + " is not a serializableFactory. Valid values include "
              + StringUtils.quote(SerializableFactory.NONE)
              + " and "
              + StringUtils.quote(SerializableFactory.XSTREAM));
        }
      }
    }

    return properties;
  }
}
