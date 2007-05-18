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

import junit.framework.TestCase;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springmodules.lucene.index.document.handler.DefaultDocumentHandlerManager;
import org.springmodules.lucene.index.document.handler.DocumentHandlerManager;
import org.springmodules.lucene.index.document.handler.DocumentHandlerManagerFactoryBean;
import org.springmodules.lucene.index.document.handler.IdentityDocumentMatching;
import org.springmodules.lucene.index.document.handler.file.ExtensionDocumentHandlerManager;
import org.springmodules.lucene.index.document.handler.file.ExtensionDocumentMatching;
import org.springmodules.lucene.index.factory.SimpleIndexFactory;
import org.springmodules.lucene.index.factory.concurrent.LockIndexFactory;

/**
 * @author Thierry Templier
 */
public class LuceneNamespaceHandlerTests extends TestCase {

	private String getConfigIndexLocation() {
		return "/org/springmodules/lucene/index/config/applicationContext-index.xml";
	}
	
	public void testLuceneIndexNamespace() throws Exception {
		ApplicationContext context = new ClassPathXmlApplicationContext(getConfigIndexLocation());
		
		Analyzer analyzer = (Analyzer)context.getBean("analyzer");
		
		testRamDirectoryCreation(context, analyzer);
		testFSDirectoryCreation(context, analyzer);
	}

	private void testRamDirectoryCreation(ApplicationContext context, Analyzer analyzer) {
		RAMDirectory directory1 = (RAMDirectory)context.getBean("ramDirectory-ramDirectory1");
		assertNotNull(directory1);
		
		SimpleIndexFactory indexFactory1 = (SimpleIndexFactory)context.getBean("ramDirectory1");
		assertNotNull(indexFactory1);
		assertSame(directory1, indexFactory1.getDirectory());
		assertSame(analyzer, indexFactory1.getAnalyzer());

		RAMDirectory directory2 = (RAMDirectory)context.getBean("ramDirectory-ramDirectory2");
		assertNotNull(directory2);
		
		SimpleIndexFactory indexFactory2 = (SimpleIndexFactory)context.getBean("ramDirectory2");
		assertNotNull(indexFactory2);
		assertNotNull(indexFactory2.getDirectory());
		assertSame(directory2, indexFactory2.getDirectory());
		assertNotNull(indexFactory2.getAnalyzer());
		assertNotSame(analyzer, indexFactory2.getAnalyzer());
	}

	private void testFSDirectoryCreation(ApplicationContext context, Analyzer analyzer) {
		FSDirectory directory1 = (FSDirectory)context.getBean("fsDirectory-fsDirectory1");
		assertNotNull(directory1);
		assertNotNull(directory1.getFile());
		
		SimpleIndexFactory indexFactory1 = (SimpleIndexFactory)context.getBean("fsDirectory1");
		assertNotNull(indexFactory1);
		assertSame(directory1, indexFactory1.getDirectory());
		assertSame(analyzer, indexFactory1.getAnalyzer());

		FSDirectory directory2 = (FSDirectory)context.getBean("fsDirectory-fsDirectory2");
		assertNotNull(directory2);
		assertNotNull(directory2.getFile());

		SimpleIndexFactory indexFactory2 = (SimpleIndexFactory)context.getBean("fsDirectory2");
		assertNotNull(indexFactory2);
		assertNotNull(indexFactory2.getDirectory());
		assertSame(directory2, indexFactory2.getDirectory());
		assertNotNull(indexFactory2.getAnalyzer());
		assertNotSame(analyzer, indexFactory2.getAnalyzer());
	}

	private String getConfigConcurrentIndexLocation() {
		return "/org/springmodules/lucene/index/config/applicationContext-index-concurrent.xml";
	}
	
	public void testLuceneConcurrentIndexNamespace() throws Exception {
		ApplicationContext context = new ClassPathXmlApplicationContext(getConfigConcurrentIndexLocation());
		
		Analyzer analyzer = (Analyzer)context.getBean("analyzer");
		
		testConcurrentFSDirectoryCreation(context, analyzer);
	}

	private void testConcurrentFSDirectoryCreation(ApplicationContext context, Analyzer analyzer) {
		//Directories 1 & 2
		/*FSDirectory directory1 = (FSDirectory)context.getBean("fsDirectory-fsDirectory1");
		assertNotNull(directory1);
		assertNotNull(directory1.getFile());
		
		SimpleIndexFactory indexFactory1 = (SimpleIndexFactory)context.getBean("target-fsDirectory1");
		assertNotNull(indexFactory1);
		assertSame(directory1, indexFactory1.getDirectory());
		assertSame(analyzer, indexFactory1.getAnalyzer());

		ChannelIndexFactory channelIndexFactory1 = (ChannelIndexFactory)context.getBean("fsDirectory1");
		assertNotNull(channelIndexFactory1);
		assertSame(indexFactory1, channelIndexFactory1.getTargetIndexFactory());*/

		FSDirectory directory2 = (FSDirectory)context.getBean("fsDirectory-fsDirectory2");
		assertNotNull(directory2);
		assertNotNull(directory2.getFile());
		
		SimpleIndexFactory indexFactory2 = (SimpleIndexFactory)context.getBean("target-fsDirectory2");
		assertNotNull(indexFactory2);
		assertSame(directory2, indexFactory2.getDirectory());
		assertSame(analyzer, indexFactory2.getAnalyzer());

		LockIndexFactory channelIndexFactory2 = (LockIndexFactory)context.getBean("fsDirectory2");
		assertNotNull(channelIndexFactory2);
		assertSame(indexFactory2, channelIndexFactory2.getTargetIndexFactory());

		//Directories 3 & 4
		/*FSDirectory directory3 = (FSDirectory)context.getBean("fsDirectory-fsDirectory3");
		assertNotNull(directory3);
		assertNotNull(directory3.getFile());

		SimpleIndexFactory indexFactory3 = (SimpleIndexFactory)context.getBean("target-fsDirectory3");
		assertNotNull(indexFactory3);
		assertNotNull(indexFactory3.getDirectory());
		assertSame(directory3, indexFactory3.getDirectory());
		assertNotNull(indexFactory3.getAnalyzer());
		assertNotSame(analyzer, indexFactory3.getAnalyzer());

		ChannelIndexFactory channelIndexFactory3 = (ChannelIndexFactory)context.getBean("fsDirectory3");
		assertNotNull(channelIndexFactory3);
		assertSame(indexFactory3, channelIndexFactory3.getTargetIndexFactory());*/

		FSDirectory directory4 = (FSDirectory)context.getBean("fsDirectory-fsDirectory4");
		assertNotNull(directory4);
		assertNotNull(directory4.getFile());

		SimpleIndexFactory indexFactory4 = (SimpleIndexFactory)context.getBean("target-fsDirectory4");
		assertNotNull(indexFactory4);
		assertNotNull(indexFactory4.getDirectory());
		assertSame(directory4, indexFactory4.getDirectory());
		assertNotNull(indexFactory4.getAnalyzer());
		assertNotSame(analyzer, indexFactory4.getAnalyzer());

		LockIndexFactory channelIndexFactory4 = (LockIndexFactory)context.getBean("fsDirectory4");
		assertNotNull(channelIndexFactory4);
		assertSame(indexFactory4, channelIndexFactory4.getTargetIndexFactory());
	}

	private String getConfigDocumentHandlerLocation() {
		return "/org/springmodules/lucene/index/config/applicationContext-document-handler.xml";
	}
	
	public void testLuceneDocumentHandlerNamespace() throws Exception {
		ApplicationContext context = new ClassPathXmlApplicationContext(getConfigDocumentHandlerLocation());
		
		DocumentHandlerManagerFactoryBean documentHandlerManagerFB1
						= (DocumentHandlerManagerFactoryBean)context.getBean("&documentHandlerManager1");
		assertNotNull(documentHandlerManagerFB1);
		assertEquals(documentHandlerManagerFB1.getDocumentMatchingClass(), ExtensionDocumentMatching.class);
		assertEquals(documentHandlerManagerFB1.getDocumentHandlerManagerClass(), ExtensionDocumentHandlerManager.class);

		DocumentHandlerManager documentHandlerManager1
						= (DocumentHandlerManager)context.getBean("documentHandlerManager1");
		assertNotNull(documentHandlerManager1);
		assertTrue(documentHandlerManager1 instanceof ExtensionDocumentHandlerManager);
		assertNotNull(documentHandlerManager1.getDocumentHandler("test.txt"));
		assertNotNull(documentHandlerManager1.getDocumentHandler("test.properties"));

		DocumentHandlerManagerFactoryBean documentHandlerManagerFB2
						= (DocumentHandlerManagerFactoryBean)context.getBean("&documentHandlerManager2");
		assertNotNull(documentHandlerManagerFB2);
		assertEquals(documentHandlerManagerFB2.getDocumentMatchingClass(), IdentityDocumentMatching.class);
		assertEquals(documentHandlerManagerFB2.getDocumentHandlerManagerClass(), DefaultDocumentHandlerManager.class);

		DocumentHandlerManager documentHandlerManager2
						= (DocumentHandlerManager)context.getBean("documentHandlerManager2");
		assertNotNull(documentHandlerManager2);
	}
}
