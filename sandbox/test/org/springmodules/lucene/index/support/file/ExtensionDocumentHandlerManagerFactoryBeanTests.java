/*
 * Copyright 2002-2005 the original author or authors.
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

package org.springmodules.lucene.index.support.file;

import java.util.HashMap;
import java.util.Map;

import org.springmodules.lucene.index.support.file.handler.TextDocumentHandler;

import junit.framework.TestCase;

/**
 * @author Thierry Templier
 */
public class ExtensionDocumentHandlerManagerFactoryBeanTests extends TestCase {

	final public void testDocumentHandlerRegistrations() throws Exception {
		//Configuration of extensions handlers
		ExtensionDocumentHandlerManagerFactoryBean factoryBean=new ExtensionDocumentHandlerManagerFactoryBean();
		Map handlers=new HashMap();
		handlers.put("properties",new TextDocumentHandler());
		handlers.put("ini",new TextDocumentHandler());
		factoryBean.setDocumentHandlers(handlers);
		factoryBean.afterPropertiesSet();

		//Getting the document handler manager
		DocumentHandlerManager manager=(DocumentHandlerManager)factoryBean.getObject();
		assertNotNull(manager);		
		assertNotNull(manager.getDocumentHandler("test.properties"));		
		assertNotNull(manager.getDocumentHandler("test.ini"));		
	}
}
