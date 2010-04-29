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
package org.springmodules.jcr.config;

import junit.framework.TestCase;

import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ObjectUtils;
import org.springmodules.jcr.EventListenerDefinition;
import org.springmodules.jcr.JcrSessionFactory;

public class JcrNamespaceHandlerTests extends TestCase {
	private XmlBeanFactory beanFactory;

	protected void setUp() throws Exception {
		this.beanFactory = new XmlBeanFactory(new ClassPathResource(
				"/org/springmodules/jcr/config/jcrNamespaceHandlerTests.xml", getClass()));
	}

	private void assertPropertyValue(RootBeanDefinition beanDefinition, String propertyName, Object expectedValue) {
		assertEquals("Property [" + propertyName + "] incorrect.", expectedValue, getPropertyValue(beanDefinition,
				propertyName));
	}

	private Object getPropertyValue(RootBeanDefinition beanDefinition, String propertyName) {
		return beanDefinition.getPropertyValues().getPropertyValue(propertyName).getValue();
	}

	public void testEventListenerDefinition() throws Exception {
		RootBeanDefinition beanDefinition = (RootBeanDefinition) this.beanFactory.getBeanDefinition("eventListenerFull");
		assertSame(EventListenerDefinition.class, beanDefinition.getBeanClass());
		assertPropertyValue(beanDefinition, "absPath", "/somePath");
		assertPropertyValue(beanDefinition, "isDeep", "true");
		assertPropertyValue(beanDefinition, "noLocal", "false");
		assertPropertyValue(beanDefinition, "eventType", new Integer(17));
		assertTrue(ObjectUtils.nullSafeEquals(new String[] { "123" }, (Object[]) getPropertyValue(beanDefinition,
				"uuid")));
		assertTrue(ObjectUtils.nullSafeEquals(new String[] { "foo", "bar" }, (Object[]) getPropertyValue(
				beanDefinition, "nodeTypeName")));
	}

	public void testSessionFactory() throws Exception {
		RootBeanDefinition beanDefinition = (RootBeanDefinition) this.beanFactory.getBeanDefinition("sessionFactory");
		assertSame(JcrSessionFactory.class, beanDefinition.getBeanClass());

	}
}
