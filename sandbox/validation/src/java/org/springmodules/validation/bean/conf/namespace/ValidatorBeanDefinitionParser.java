/*
 * Copyright 2004-2005 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springmodules.validation.bean.conf.namespace;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springmodules.validation.bean.BeanValidator;
import org.w3c.dom.Element;

/**
 * A {@link org.springframework.beans.factory.xml.BeanDefinitionParser} that defines a BeanValidator in the appilcation
 * context. This handler parses elements of the form:
 * <code>&lt;validator id="" [errorCodeConverter="converter"] [configurationLoader="loader"]/&gt;</code>.
 * where:
 * <ul>
 *  <li>
 *      errorCodeConverter - (optional) a reference to an {@link org.springmodules.validation.bean.converter.ErrorCodeConverter}
 *      in the application context.
 *  </li>
 *  <li>
 *      configurationLoader - (optional) a reference to an {@link org.springmodules.validation.bean.conf.BeanValidationConfigurationLoader}
 *      in the application context.
 *  </li>
 * </ul>
 * If the <code>configurationLoader</code> attribute is not set, the handler expects a
 * <code>{@link ClassValidationConfigurationBeanDefinitionParser.LOADER_NAME}</code> loader to be defined in the
 * application context. <br/>
 * This handler is mainly to be used along with the {@link ClassValidationConfigurationBeanDefinitionParser}.
 *
 * @author Uri Boness
 */
public class ValidatorBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    private final static String ERROR_CODE_CONVERTER_ATTR = "errorConverter";
    private final static String CONF_LOADER_ATTR = "configurationLoader";

    /**
     * Returns the {@link BeanValidator} class.
     *
     * @see AbstractSingleBeanDefinitionParser#doParse(org.w3c.dom.Element, org.springframework.beans.factory.support.BeanDefinitionBuilder)
     */
    protected Class getBeanClass(Element element) {
        return BeanValidator.class;
    }

    /**
     * Creates the {@link BeanValidator} bean definition in the application context.
     *
     * @see AbstractSingleBeanDefinitionParser#doParse(org.w3c.dom.Element, org.springframework.beans.factory.support.BeanDefinitionBuilder)
     */
    protected void doParse(Element element, BeanDefinitionBuilder builder) {

        if (element.hasAttribute(ERROR_CODE_CONVERTER_ATTR)) {
            builder.addPropertyReference("errorCodeConverter", element.getAttribute(ERROR_CODE_CONVERTER_ATTR));
        }

        if (element.hasAttribute(CONF_LOADER_ATTR)) {
            builder.addPropertyReference("configurationLoader", element.getAttribute(CONF_LOADER_ATTR));
        }

        builder.addPropertyReference("configurationLoader", ClassValidationConfigurationBeanDefinitionParser.LOADER_NAME);
    }

}
