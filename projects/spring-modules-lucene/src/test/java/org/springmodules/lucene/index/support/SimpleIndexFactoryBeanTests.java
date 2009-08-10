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

package org.springmodules.lucene.index.support;

import junit.framework.TestCase;

import org.apache.lucene.store.RAMDirectory;
import org.springmodules.lucene.index.factory.IndexFactory;
import org.springmodules.lucene.index.factory.SimpleIndexFactory;

public class SimpleIndexFactoryBeanTests extends TestCase {

	public void testGetObject() throws Exception {
		SimpleIndexFactoryBean simpleIndexFactoryBean = new SimpleIndexFactoryBean();
		simpleIndexFactoryBean.setDirectory(new RAMDirectory());
		simpleIndexFactoryBean.afterPropertiesSet();

		IndexFactory indexFactory = (IndexFactory)simpleIndexFactoryBean.getObject();
		assertNotNull(indexFactory);
		assertEquals(indexFactory.getClass(), SimpleIndexFactory.class);
	}

	public void testGetObjectWithoutDirectory() throws Exception {
		SimpleIndexFactoryBean simpleIndexFactoryBean = new SimpleIndexFactoryBean();
		try {
			simpleIndexFactoryBean.afterPropertiesSet();
			fail();
		} catch(IllegalArgumentException ex) {}
	}
}
