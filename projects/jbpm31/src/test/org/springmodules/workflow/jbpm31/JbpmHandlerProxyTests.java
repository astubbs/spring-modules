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
package org.springmodules.workflow.jbpm31;

import junit.framework.TestCase;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

public class JbpmHandlerProxyTests extends TestCase {

	private JbpmHandlerProxy proxy;

	JbpmFactoryLocator locator;

	BeanFactory bf;

	protected void setUp() throws Exception {
		proxy = new JbpmHandlerProxy();
		locator = new JbpmFactoryLocator();
		bf = new DefaultListableBeanFactory();
		locator.setBeanFactory(bf);
		// get one reference (to be destroyed during tearDown)
		locator.useBeanFactory(null).getFactory();
	}

	protected void tearDown() throws Exception {
		proxy = null;
		locator.removeReference(bf);

		// make sure we have no reference leaks
		try {
			assertNull("there are still references inside the locator to the beanFactory", locator.referenceCounter.get(bf));
		}
		finally {
			bf = null;
		}
	}

	public void testCleanBeanFactoryReferences() throws Exception {
		// invoke a reference
		BeanFactory factory = proxy.retrieveBeanFactory();
		assertSame(bf, factory);
	}
}
