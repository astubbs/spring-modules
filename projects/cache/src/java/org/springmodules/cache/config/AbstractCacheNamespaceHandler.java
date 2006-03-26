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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.springframework.util.ClassUtils;

import org.springmodules.cache.FatalCacheException;
import org.springmodules.cache.util.SystemUtils;

/**
 * <p>
 * Template for handler of caching-related namespaces.
 * </p>
 * 
 * @author Alex Ruiz
 */
public abstract class AbstractCacheNamespaceHandler extends
    NamespaceHandlerSupport {

  /**
   * Logger available to subclasses.
   */
  protected Log logger = LogFactory.getLog(getClass());

  /**
   * Constructor that registers parsers for the elements:
   * <ul>
   * <li>annotations</li>
   * <li>commons-attributes</li>
   * <li>config</li>
   * <li>interceptors</li>
   * <li>proxy</li>
   * </ul>
   */
  public AbstractCacheNamespaceHandler() {
    super();
    init();
    registerAllParsers();
  }

  protected abstract CacheModelParser getCacheModelParser();

  /**
   * @return the parser for the XML elements with name "config"
   */
  protected abstract BeanDefinitionParser getCacheProviderFacadeParser();

  /**
   * Gives subclasses the opportunity to initialize their own infrastructure
   * such as custom bean definition parsers, helpers, etc.
   */
  protected void init() {
    // no implementation.
  }

  private void registerAllParsers() {
    registerBeanDefinitionParser("config", getCacheProviderFacadeParser());
    registerAnnotationsElementParser();
    registerCommonsAttributesElementParser();
    registerProxyElementParser();
    registerInterceptorsElementParser();
  }

  private void registerAnnotationsElementParser() {
    if (SystemUtils.annotationsSupport()) {
      String thisPackage = AbstractCacheNamespaceHandler.class.getPackage()
          .getName();
      String annotationsParserClassName = thisPackage + ".AnnotationsParser";

      try {
        Class parserClass = ClassUtils.forName(annotationsParserClassName);

        Object parser = parserClass.newInstance();
        registerCacheSetupStrategyParser("annotations",
            (AbstractCacheSetupStrategyParser) parser);

      } catch (Exception exception) {
        String errorMessage = "Unable to create instance of "
            + annotationsParserClassName
            + ". Unable to load parser for namespace 'annotations'";
        throw new FatalCacheException(errorMessage, exception);
      }

    } else {
      logger.info("No support for JDK 1.5 Annotations. "
          + "Unable to load parser for namespace 'annotations'");
    }
  }

  private void registerCacheSetupStrategyParser(String elementName,
      AbstractCacheSetupStrategyParser parser) {
    parser.setCacheModelParser(getCacheModelParser());
    registerBeanDefinitionParser(elementName, parser);
  }

  private void registerCommonsAttributesElementParser() {
    CommonsAttributesParser parser = new CommonsAttributesParser();
    registerCacheSetupStrategyParser("commons-attributes", parser);
  }

  private void registerInterceptorsElementParser() {
    InterceptorsParser parser = new InterceptorsParser();
    registerCacheSetupStrategyParser("interceptors", parser);
  }

  private void registerProxyElementParser() {
    CacheProxyFactoryBeanParser parser = new CacheProxyFactoryBeanParser();
    registerCacheSetupStrategyParser("proxy", parser);
  }
}
