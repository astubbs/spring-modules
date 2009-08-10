/*
 * Copyright 2002-2006 the original author or authors.
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
package org.springmodules.jcr.jackrabbit;

import junit.framework.TestCase;

import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

/**
 * 
 * @author Costin Leau
 */
public class JackrabbitNamespaceHandlerTests extends TestCase {

	private XmlBeanFactory beanFactory;

	@Override
	protected void setUp() throws Exception {
		this.beanFactory = new XmlBeanFactory(new ClassPathResource(
				"/org/springmodules/jcr/jackrabbit/config/jackrabbitNamespaceHandlerTests.xml", getClass()));
	}

	private void assertPropertyValue(final RootBeanDefinition beanDefinition, final String propertyName,
			final Object expectedValue) {
		assertEquals("Property [" + propertyName + "] incorrect.", expectedValue, beanDefinition.getPropertyValues()
				.getPropertyValue(propertyName).getValue());
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testMinimalDefinition() throws Exception {
		final RootBeanDefinition beanDefinition = (RootBeanDefinition) this.beanFactory.getBeanDefinition("minimal");
		assertSame(RepositoryFactoryBean.class, beanDefinition.getBeanClass());
		assertPropertyValue(beanDefinition, "configuration", "classpath:config.xml");
	}

	public void testExtendedDefinition() throws Exception {
		final RootBeanDefinition beanDefinition = (RootBeanDefinition) this.beanFactory.getBeanDefinition("extended");
		assertSame(RepositoryFactoryBean.class, (beanDefinition.getBeanClass()));
		assertPropertyValue(beanDefinition, "configuration", "file:config.xml");
		assertPropertyValue(beanDefinition, "homeDir", "file:///homeDir");
	}

	public void testFullDefinition() throws Exception {
		final RootBeanDefinition beanDefinition = (RootBeanDefinition) this.beanFactory.getBeanDefinition("full");
		assertSame(RepositoryFactoryBean.class, (beanDefinition.getBeanClass()));
		assertPropertyValue(beanDefinition, "homeDir", "file:///homeDir");
		assertPropertyValue(beanDefinition, "repositoryConfig", "repoCfg");
	}

	public void testTransactionManager() throws Exception {
		final RootBeanDefinition beanDefinition = (RootBeanDefinition) this.beanFactory
				.getBeanDefinition("transactionManager");
		assertSame(LocalTransactionManager.class, (beanDefinition.getBeanClass()));
		assertPropertyValue(beanDefinition, "sessionFactory", "jcrSessionFactory");
	}

}
