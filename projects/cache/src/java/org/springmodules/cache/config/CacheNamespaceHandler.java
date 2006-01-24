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
import org.springframework.beans.factory.xml.NamespaceHandler;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.springframework.util.ClassUtils;

/**
 * <p>
 * {@link NamespaceHandler} for the <code>cache</code> namespace.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class CacheNamespaceHandler extends NamespaceHandlerSupport {

  private static Log logger = LogFactory.getLog(CacheNamespaceHandler.class);

  /**
   * Constructor.
   */
  public CacheNamespaceHandler() {
    super();
    registerBeanDefinitionParser("config",
        new CacheConfigBeanDefinitionParser());

    try {
      ClassUtils.forName("java.lang.annotation.Annotation");
      
      try {
        Class parserClass = ClassUtils
            .forName("org.springmodules.cache.config.CacheAnnotationsBeanDefinitionParser");

        Object parser = parserClass.newInstance();
        registerBeanDefinitionParser("annotations",
            (BeanDefinitionParser) parser);

      } catch (Exception exception) {
        String errorMessage = "Unable to create instance of "
            + "CacheAnnotationsBeanDefinitionParser. "
            + "Unable to load parser for namespace 'annotations'";
        logger.error(errorMessage, exception);
      }

    } catch (ClassNotFoundException exception) {
      logger.info("No support for JDK 1.5 Annotations. "
          + "Unable to load parser for namespace 'annotations'");
    }

    registerBeanDefinitionParser("beanRef",
        new CacheBeanRefBeanDefinitionParser());

    registerBeanDefinitionParser("commons-attributes",
        new CacheCommonsAttributesBeanDefinitionParser());

    registerBeanDefinitionParser("interceptors",
        new CacheInterceptorsBeanDefinitionParser());
  }

}
