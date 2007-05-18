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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.springmodules.lucene.index.resource.NameMatchResourceAttributeSource;
import org.springmodules.lucene.index.resource.ResourceInterceptor;
import org.springmodules.lucene.index.resource.RuleBasedResourceAttribute;
import org.w3c.dom.Element;

/**
 * {@link org.springframework.beans.factory.xml.BeanDefinitionParser}
 * for the <code>&lt;lucene:resource-advice&gt;</code> tag.
 * 
 * @author Thierry Templier
 */
class ResourceAdviceBeanDefinitionParser extends AbstractLuceneBeanDefinitionParser implements BeanDefinitionParser {
	private static final String RESOURCE_ATTRIBUTES_SOURCE_ATTRIBUTE = "resourceAttributesSource";
	private static final String READER_WRITING_ENABLED_ATTRIBUTE = "reader-writing-enabled";
	private static final String WRITER_WRITING_ENABLED_ATTRIBUTE = "writer-writing-enabled";
	private static final String READER_OPEN_ATTRIBUTE = "reader-open";
	private static final String WRITER_OPEN_ATTRIBUTE = "writer-open";
	private static final String NAME_ATTRIBUTE = "name";
	private static final String METHOD_TAG = "method";
	private static final String ATTRIBUTES_TAG = "attributes";
	private static final String DEFAULT_RESOURCE_MANAGER_ID = "resourceManager";
	public static final String ID_ATTRIBUTE = "id";
	public static final String RESOURCE_MANAGER_REF_ATTRIBUTE = "resource-manager-ref";
	public static final String RESOURCE_MANAGER_ATTRIBUTE = "resourceManager";
	public static final String TRUE_VALUE = "true";

	public BeanDefinition parse(Element element, ParserContext parserContext) {
		String id = element.getAttribute(ID_ATTRIBUTE);
		String resourceManagerRef = element.getAttribute(RESOURCE_MANAGER_REF_ATTRIBUTE);
		if( resourceManagerRef==null || "".equals(resourceManagerRef) ) {
			resourceManagerRef = DEFAULT_RESOURCE_MANAGER_ID;
		}
		
		BeanDefinitionRegistry registry = parserContext.getRegistry();

		RootBeanDefinition resourceAdviceBeanDefinition = new RootBeanDefinition(
				ResourceInterceptor.class);

		resourceAdviceBeanDefinition.setPropertyValues(new MutablePropertyValues());
		resourceAdviceBeanDefinition.getPropertyValues()
				.addPropertyValue(RESOURCE_MANAGER_ATTRIBUTE, new RuntimeBeanReference(resourceManagerRef));

		configureAttributes(element, resourceAdviceBeanDefinition, parserContext);
		
		registry.registerBeanDefinition(id, resourceAdviceBeanDefinition);
		return null;
	}

	private void configureAttributes(Element element,
			RootBeanDefinition resourceAdviceBeanDefinition, ParserContext parserContext) {
		Element attrsElement = DomUtils.getChildElementByTagName(element, ATTRIBUTES_TAG);
		List methodElements = DomUtils.getChildElementsByTagName(attrsElement, METHOD_TAG);
		NameMatchResourceAttributeSource resourceAttributeSource = new NameMatchResourceAttributeSource();
		Map attributes = new HashMap();
		for(Iterator i = methodElements.iterator(); i.hasNext(); ) {
			Element methodElement = (Element)i.next();
			String methodName = methodElement.getAttribute(NAME_ATTRIBUTE);
			RuleBasedResourceAttribute attr = new RuleBasedResourceAttribute();

			methodName = methodName.trim();
			String writerOpen = methodElement.getAttribute(WRITER_OPEN_ATTRIBUTE);
			writerOpen = writerOpen.trim();
			String readerOpen = methodElement.getAttribute(READER_OPEN_ATTRIBUTE);
			readerOpen = readerOpen.trim();
			String writerWritingEnabled = methodElement.getAttribute(WRITER_WRITING_ENABLED_ATTRIBUTE);
			writerWritingEnabled = writerWritingEnabled.trim();
			String readerWritingEnabled = methodElement.getAttribute(READER_WRITING_ENABLED_ATTRIBUTE);
			readerWritingEnabled = readerWritingEnabled.trim();

			attr.setIndexWriterOpen(TRUE_VALUE.equals(writerOpen));
			attr.setIndexReaderOpen(TRUE_VALUE.equals(readerOpen));
			attr.setWriteOperationsForIndexWriterAuthorized(TRUE_VALUE.equals(writerWritingEnabled));
			attr.setWriteOperationsForIndexReaderAuthorized(TRUE_VALUE.equals(readerWritingEnabled));
			
			attributes.put(methodName, attr);
		}

		resourceAttributeSource.setResourceAttributes(attributes);
		resourceAdviceBeanDefinition.getPropertyValues()
				.addPropertyValue(RESOURCE_ATTRIBUTES_SOURCE_ATTRIBUTE, resourceAttributeSource);
	}

}
