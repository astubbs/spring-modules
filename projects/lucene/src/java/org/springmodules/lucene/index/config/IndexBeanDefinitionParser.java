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
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.springmodules.lucene.index.factory.concurrent.LockIndexFactory;
import org.springmodules.lucene.index.support.FSDirectoryFactoryBean;
import org.springmodules.lucene.index.support.RAMDirectoryFactoryBean;
import org.springmodules.lucene.index.support.SimpleIndexFactoryBean;
import org.w3c.dom.Element;

/**
 * {@link org.springframework.beans.factory.xml.BeanDefinitionParser}
 * for the <code>&lt;lucene:index&gt;</code> tag.
 * 
 * @author Thierry Templier
 */
class IndexBeanDefinitionParser extends AbstractLuceneBeanDefinitionParser implements BeanDefinitionParser {
	private static final String LOCK = "lock";
	private static final String CHANNEL = "channel";
	private static final String TARGET_INDEX_FACTORY_ATTRIBUTE = "targetIndexFactory";
	public static final String FS_DIRECTORY_ID_PREFIX = "fsDirectory-";
	public static final String LOCATION_ATTRIBUTE = "location";
	public static final String DIRECTORY_ATTRIBUTE = "directory";
	public static final String RAM_DIRECTORY_ID_PREFIX = "ramDirectory-";
	public static final String TARGET_INDEX_FACTORY_ID_PREFIX = "target-";
	public static final String ID_ATTRIBUTE = "id";
	public static final String CREATE_ATTRIBUTE = "create";
	public static final String ANALYZER_REF_ATTRIBUTE = "analyzer-ref";
	public static final String ANALYZER_ATTRIBUTE = "analyzer";
	public static final String CONCURRENT_ATTRIBUTE = "concurrent";
	public static final String ANALYZER_ELEMENT = "analyzer";
	public static final String BEAN_ELEMENT = "bean";
	public static final String TRUE_VALUE = "true";

	private boolean isFSIndex(Element element) {
		return hasAttributeSet(element, LOCATION_ATTRIBUTE);
	}

	public BeanDefinition parse(Element element, ParserContext parserContext) {
		if( isFSIndex(element) ) {
			createFSIndex(element, parserContext);
		} else {
			createRamIndex(element, parserContext);
		}
		return null;
	}

	private boolean mustCreateIndex(Element element) {
		boolean create = false;
		String createAttr = element.getAttribute(CREATE_ATTRIBUTE);
		if( createAttr!=null ) {
			createAttr = createAttr.trim();
		}
		if( createAttr!=null && TRUE_VALUE.equals(createAttr) ) {
			create = true;
		}
		return create;
	}

	private void configureConcurrentIndexFactory(String id, String concurrent, BeanDefinitionRegistry registry, String idIndexFactory) {
		/*if( CHANNEL.equals(concurrent) ) {
			RootBeanDefinition concurrentIndexFactoryBeanDefinition = new RootBeanDefinition(
																		ChannelIndexFactory.class);
			concurrentIndexFactoryBeanDefinition.setPropertyValues(new MutablePropertyValues());
			concurrentIndexFactoryBeanDefinition.getPropertyValues()
				.addPropertyValue(TARGET_INDEX_FACTORY_ATTRIBUTE, new RuntimeBeanReference(idIndexFactory));

			registry.registerBeanDefinition(id, concurrentIndexFactoryBeanDefinition);
		} else */if( LOCK.equals(concurrent)) {
			RootBeanDefinition concurrentIndexFactoryBeanDefinition = new RootBeanDefinition(
																		LockIndexFactory.class);
			concurrentIndexFactoryBeanDefinition.setPropertyValues(new MutablePropertyValues());
			concurrentIndexFactoryBeanDefinition.getPropertyValues()
				.addPropertyValue(TARGET_INDEX_FACTORY_ATTRIBUTE, new RuntimeBeanReference(idIndexFactory));

			registry.registerBeanDefinition(id, concurrentIndexFactoryBeanDefinition);
		}
	}

	private void createRamIndex(Element element, ParserContext parserContext) {
		String id = element.getAttribute(ID_ATTRIBUTE);
		boolean create = mustCreateIndex(element);
		String concurrent = element.getAttribute(CONCURRENT_ATTRIBUTE);
		boolean concurrentEnabled = !"".equals(concurrent);

		BeanDefinitionRegistry registry = parserContext.getRegistry();

		//Definition of the RAM Directory
		RootBeanDefinition ramDirectoryBeanDefinition = new RootBeanDefinition(
													RAMDirectoryFactoryBean.class);
		registry.registerBeanDefinition(RAM_DIRECTORY_ID_PREFIX + id, ramDirectoryBeanDefinition);

		//Definition of the index factory 
		RootBeanDefinition simpleIndexFactoryBeanDefinition = new RootBeanDefinition(
													SimpleIndexFactoryBean.class);
		simpleIndexFactoryBeanDefinition.setPropertyValues(new MutablePropertyValues());
		simpleIndexFactoryBeanDefinition.getPropertyValues()
				.addPropertyValue(DIRECTORY_ATTRIBUTE, new RuntimeBeanReference(RAM_DIRECTORY_ID_PREFIX + id));
		simpleIndexFactoryBeanDefinition.getPropertyValues()
				.addPropertyValue(CREATE_ATTRIBUTE, new Boolean(create));

		//Configuration of the analyzer of the factory
		configureAnalyzer(element, simpleIndexFactoryBeanDefinition, parserContext);
		
		String idIndexFactory = concurrentEnabled ? TARGET_INDEX_FACTORY_ID_PREFIX + id : id;
		registry.registerBeanDefinition(idIndexFactory, simpleIndexFactoryBeanDefinition);

		if( concurrentEnabled ) {
			configureConcurrentIndexFactory(id, concurrent, registry, idIndexFactory);
		}
	}

	private void createFSIndex(Element element, ParserContext parserContext) {
		String id = element.getAttribute(ID_ATTRIBUTE);
		String location = element.getAttribute(LOCATION_ATTRIBUTE);
		String concurrent = element.getAttribute(CONCURRENT_ATTRIBUTE);
		boolean concurrentEnabled = !"".equals(concurrent);
		System.out.println("concurrent = "+concurrent);
		System.out.println("concurrentEnabled = "+concurrentEnabled);
		boolean create = mustCreateIndex(element);

		BeanDefinitionRegistry registry = parserContext.getRegistry();

		//Definition of the FS Directory
		RootBeanDefinition fsDirectoryBeanDefinition = new RootBeanDefinition(
													FSDirectoryFactoryBean.class);
		fsDirectoryBeanDefinition.setPropertyValues(new MutablePropertyValues());
		fsDirectoryBeanDefinition.getPropertyValues()
				.addPropertyValue(LOCATION_ATTRIBUTE, location);
		registry.registerBeanDefinition(FS_DIRECTORY_ID_PREFIX + id, fsDirectoryBeanDefinition);

		//Definition of the index factory 
		RootBeanDefinition simpleIndexFactoryBeanDefinition = new RootBeanDefinition(
													SimpleIndexFactoryBean.class);
		simpleIndexFactoryBeanDefinition.setPropertyValues(new MutablePropertyValues());
		simpleIndexFactoryBeanDefinition.getPropertyValues()
				.addPropertyValue(DIRECTORY_ATTRIBUTE, new RuntimeBeanReference(FS_DIRECTORY_ID_PREFIX + id));
		simpleIndexFactoryBeanDefinition.getPropertyValues()
				.addPropertyValue(CREATE_ATTRIBUTE, new Boolean(create));

		//Configuration of the analyzer of the factory
		configureAnalyzer(element, simpleIndexFactoryBeanDefinition, parserContext);
		
		String idIndexFactory = concurrentEnabled ? TARGET_INDEX_FACTORY_ID_PREFIX + id : id;
		registry.registerBeanDefinition(idIndexFactory, simpleIndexFactoryBeanDefinition);
		
		if( concurrentEnabled ) {
			configureConcurrentIndexFactory(id, concurrent, registry, idIndexFactory);
		}
	}

	private boolean hasAnalyzerRef(Element element) {
		return hasAttributeSet(element, ANALYZER_REF_ATTRIBUTE);
	}

	private void configureAnalyzerRef(MutablePropertyValues propertyValues, String analyzerRef) {
		//Compute the bean reference of the anaylzer
		propertyValues.addPropertyValue(ANALYZER_ATTRIBUTE, new RuntimeBeanReference(analyzerRef));
	}

	private BeanDefinitionHolder handlerAnalyzerDefinition(Element element, RootBeanDefinition definition, ParserContext parserContext) {
		Element beanElement = DomUtils.getChildElementByTagName(element, BEAN_ELEMENT);
		if( beanElement!=null ) {
			//Compute the inner bean of the anaylzer
			BeanDefinitionParserDelegate delegate = parserContext.getDelegate();
			BeanDefinitionHolder holder = delegate.parseBeanDefinitionElement(beanElement, definition);
			if( holder!=null ) {
				holder = delegate.decorateBeanDefinitionIfRequired(beanElement, holder);
			}
			return holder;
		}

		return null;
	}

	private void configureInnerAnalyzer(Element element, RootBeanDefinition definition, ParserContext parserContext, MutablePropertyValues propertyValues) {
		Element analyzerElement = DomUtils.getChildElementByTagName(element, ANALYZER_ELEMENT);
		if( analyzerElement!=null ) {
			BeanDefinitionHolder analyzer = handlerAnalyzerDefinition(analyzerElement, definition, parserContext);
			if( analyzer!=null ) {
				propertyValues.addPropertyValue(ANALYZER_ATTRIBUTE, analyzer);
			}
		}
	}

	private void configureAnalyzer(Element element, RootBeanDefinition definition, ParserContext parserContext) {
		MutablePropertyValues propertyValues = definition.getPropertyValues();
		if( hasAnalyzerRef(element) ) {
			String analyzerRef = element.getAttribute(ANALYZER_REF_ATTRIBUTE);
			configureAnalyzerRef(propertyValues, analyzerRef);
		} else {
			configureInnerAnalyzer(element, definition, parserContext, propertyValues);
		}
	}

}
