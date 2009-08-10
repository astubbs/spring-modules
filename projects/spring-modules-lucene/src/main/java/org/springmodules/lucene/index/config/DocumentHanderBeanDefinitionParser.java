/*
 * Copyright 2002-2007 the original author or authors.
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

package org.springmodules.lucene.index.config;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springmodules.lucene.index.document.handler.DocumentHandlerManagerFactoryBean;
import org.springmodules.lucene.index.document.handler.file.ExtensionDocumentHandlerManager;
import org.springmodules.lucene.index.document.handler.file.ExtensionDocumentMatching;
import org.w3c.dom.Element;

/**
 * {@link org.springframework.beans.factory.xml.BeanDefinitionParser}
 * for the <code>&lt;lucene:document-handler&gt;</code> tag.
 * 
 * @author Thierry Templier
 */
public class DocumentHanderBeanDefinitionParser
				extends AbstractLuceneBeanDefinitionParser implements BeanDefinitionParser {

	private static final String IDENTITY_TYPE = "identity";
	private static final String EXTENSION_TYPE = "extension";
	public static final String TYPE_ATTRIBUTE = "type";
	public static final String ID_ATTRIBUTE = "id";

	public BeanDefinition parse(Element element, ParserContext parserContext) {
		String type = element.getAttribute(TYPE_ATTRIBUTE);
		if( type==null || "".equals(type) ) {
			type = EXTENSION_TYPE;
		}

		if( EXTENSION_TYPE.equals(type) ) {
			createFileExtensionDocumentHandlerManager(element, parserContext);
		} else if( IDENTITY_TYPE.equals(type) ) {
			createIdentityDocumentHandlerManager(element, parserContext);
		}
		return null;
	}

	private void createIdentityDocumentHandlerManager(Element element, ParserContext parserContext) {
		String id = element.getAttribute(ID_ATTRIBUTE);

		BeanDefinitionRegistry registry = parserContext.getRegistry();

		RootBeanDefinition documentHandlerFactoryBeanDefinition = new RootBeanDefinition(
												DocumentHandlerManagerFactoryBean.class);
		registry.registerBeanDefinition(id, documentHandlerFactoryBeanDefinition);
	}

	private void createFileExtensionDocumentHandlerManager(Element element, ParserContext parserContext) {
		String id = element.getAttribute(ID_ATTRIBUTE);

		BeanDefinitionRegistry registry = parserContext.getRegistry();

		RootBeanDefinition documentHandlerFactoryBeanDefinition = new RootBeanDefinition(
												DocumentHandlerManagerFactoryBean.class);
		documentHandlerFactoryBeanDefinition.setPropertyValues(new MutablePropertyValues());
		documentHandlerFactoryBeanDefinition.getPropertyValues().addPropertyValue(
						"documentHandlerManagerClass", ExtensionDocumentHandlerManager.class);
		documentHandlerFactoryBeanDefinition.getPropertyValues().addPropertyValue(
									"documentMatchingClass", ExtensionDocumentMatching.class);
		registry.registerBeanDefinition(id, documentHandlerFactoryBeanDefinition);
	}

}
