/*
 * Created on Mar 14, 2006
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

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.beans.factory.xml.XmlReaderContext;

/**
 * <p/>
 * Factory of instances of <code>{@link ParserContext}</code> to be used for
 * testing.
 * </p>
 *
 * @author Omar Irbouh
 * @author Alex Ruiz
 */
public abstract class ParserContextFactory {

	public static ParserContext create() {
		return create(null);
	}

	public static ParserContext create(BeanDefinitionParserDelegate delegate) {
		DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
		XmlReaderContext readerContext = new XmlReaderContext(null, null, null, null, reader,
				null);
		return new ParserContext(readerContext, delegate);
	}

}