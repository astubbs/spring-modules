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

import org.w3c.dom.Element;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceEditor;
import org.springframework.util.StringUtils;

import org.springmodules.cache.provider.jboss.JbossCacheManagerFactoryBean;
import org.springmodules.cache.provider.jcs.JcsManagerFactoryBean;
import org.springmodules.cache.provider.oscache.OsCacheManagerFactoryBean;

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

  private static final String CONFIG_LOCATION_ATTR = "configLocation";

  private static final String CONFIG_LOCATION_PROPERTY = "configLocation";

  private static final String EHCACHE = "EHCACHE";

  private static final String ID_ATTR = "id";

  private static final String JBOSS_CACHE = "JBOSS_CACHE";

  private static final String JCS = "JCS";

  private static final String OSCACHE = "OSCACHE";

  private static final String PROVIDER_NAME_ATTR = "provider";

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
   * </ul>
   * </li>
   * <li> <b>configLocation (optional):</b> the path of the configuration file
   * for the cache manager</b> </li>
   * </ul>
   * 
   * @throws IllegalArgumentException
   *           if the value of the XML attribute "provider" contains an invalid
   *           value
   * @see BeanDefinitionParser#parse(Element, BeanDefinitionRegistry)
   */
  public void parse(Element element, BeanDefinitionRegistry registry) {
    Class cacheManagerFactoryBeanClass = null;

    String providerName = element.getAttribute(PROVIDER_NAME_ATTR);
    if (EHCACHE.equalsIgnoreCase(providerName)) {
      cacheManagerFactoryBeanClass = EhCacheManagerFactoryBean.class;

    } else if (JBOSS_CACHE.equalsIgnoreCase(providerName)) {
      cacheManagerFactoryBeanClass = JbossCacheManagerFactoryBean.class;

    } else if (JCS.equalsIgnoreCase(providerName)) {
      cacheManagerFactoryBeanClass = JcsManagerFactoryBean.class;

    } else if (OSCACHE.equalsIgnoreCase(providerName)) {
      cacheManagerFactoryBeanClass = OsCacheManagerFactoryBean.class;

    } else {
      throw new IllegalStateException(StringUtils.quote(providerName)
          + " is not a valid provider. Valid values include "
          + StringUtils.quote(EHCACHE) + ", " + StringUtils.quote(JBOSS_CACHE)
          + ", " + StringUtils.quote(JCS) + " and "
          + StringUtils.quote(OSCACHE));
    }

    RootBeanDefinition definition = new RootBeanDefinition(
        cacheManagerFactoryBeanClass);
    definition.setPropertyValues(new MutablePropertyValues());
    setConfigLocation(element, definition);

    String id = element.getAttribute(ID_ATTR);
    registry.registerBeanDefinition(id, definition);
  }

  private void setConfigLocation(Element element, RootBeanDefinition definition) {
    String configLocationPath = element.getAttribute(CONFIG_LOCATION_ATTR);

    if (StringUtils.hasText(configLocationPath)) {
      ResourceEditor resourceEditor = new ResourceEditor();
      resourceEditor.setAsText(configLocationPath);
      Resource resource = (Resource) resourceEditor.getValue();

      definition.getPropertyValues().addPropertyValue(CONFIG_LOCATION_PROPERTY,
          resource);
    }
  }
}
