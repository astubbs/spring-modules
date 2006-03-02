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
   * <li>beanRef</li>
   * <li>commons-attributes</li>
   * <li>config</li>
   * <li>interceptors</li>
   * </ul>
   */
  public AbstractCacheNamespaceHandler() {
    super();
    registerBeanDefinitionParser("config", getCacheProviderFacadeParser());

    try {
      ClassUtils.forName("java.lang.annotation.Annotation");

      String annotationsParserClassName = getAnnotationsParserClassName();
      try {
        Class parserClass = ClassUtils.forName(annotationsParserClassName);

        Object parser = parserClass.newInstance();
        registerBeanDefinitionParser("annotations",
            (BeanDefinitionParser) parser);

      } catch (Exception exception) {
        String errorMessage = "Unable to create instance of "
            + annotationsParserClassName
            + ". Unable to load parser for namespace 'annotations'";
        logger.error(errorMessage, exception);
      }

    } catch (ClassNotFoundException exception) {
      logger.info("No support for JDK 1.5 Annotations. "
          + "Unable to load parser for namespace 'annotations'");
    }

    registerBeanDefinitionParser("beanRef", getCacheProxyFactoryBeanParser());

    registerBeanDefinitionParser("commons-attributes",
        getCommonsAttributeParser());

    registerBeanDefinitionParser("interceptors", getInterceptorsParser());
  }

  /**
   * @return the class name of the parser for the element "annotations". A
   *         string is used instead of a class because annotations are part of
   *         J2SE 5.0 and this handler is compiled against J2SE 1.3.
   */
  protected abstract String getAnnotationsParserClassName();

  /**
   * @return the parser for the element "config".
   */
  protected abstract BeanDefinitionParser getCacheProviderFacadeParser();

  /**
   * @return the parser for the element "beanRef".
   */
  protected abstract BeanDefinitionParser getCacheProxyFactoryBeanParser();

  /**
   * @return the parser for the element "commons-attributes".
   */
  protected abstract BeanDefinitionParser getCommonsAttributeParser();

  /**
   * @return the parser for the element "interceptors".
   */
  protected abstract BeanDefinitionParser getInterceptorsParser();
}
