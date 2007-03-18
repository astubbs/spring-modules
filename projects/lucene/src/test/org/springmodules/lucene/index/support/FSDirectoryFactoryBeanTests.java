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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import junit.framework.TestCase;

import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springmodules.lucene.index.factory.LuceneIndexReader;
import org.springmodules.lucene.index.factory.LuceneIndexWriter;

public class FSDirectoryFactoryBeanTests extends TestCase {

	protected void setUp() throws Exception {
	}

	protected void tearDown() throws Exception {
		Resource resource = new ClassPathResource("/org/springmodules/lucene/index/support/segments");
		try {
			File indexFile = resource.getFile();
			if( indexFile.exists() ) {
				indexFile.delete();
			}
		} catch(FileNotFoundException ex) {}
	}

	private void closeIndexReader(LuceneIndexReader indexReader) {
		if( indexReader!=null ) {
			try {
				indexReader.close();
			} catch (IOException ex) {
				fail();
			}
		}
	}

	private void closeIndexWriter(LuceneIndexWriter indexWriter) {
		if( indexWriter!=null ) {
			try {
				indexWriter.close();
			} catch (IOException ex) {
				fail();
			}
		}
	}

	private void closeIndexReader(IndexReader indexReader) {
		if( indexReader!=null ) {
			try {
				indexReader.close();
			} catch (IOException ex) {
				fail();
			}
		}
	}

	private void closeIndexWriter(IndexWriter indexWriter) {
		if( indexWriter!=null ) {
			try {
				indexWriter.close();
			} catch (IOException ex) {
				fail();
			}
		}
	}

	public void testGetObject() throws Exception {
		FSDirectoryFactoryBean fsDirectoryFactoryBean = new FSDirectoryFactoryBean();
		fsDirectoryFactoryBean.setLocation(new ClassPathResource("/org/springmodules/lucene/index/support"));
		fsDirectoryFactoryBean.afterPropertiesSet();
		Directory fsDirectory = (Directory)fsDirectoryFactoryBean.getObject();
		assertNotNull(fsDirectory);
		assertEquals(fsDirectory.getClass(), FSDirectory.class);
	}

	public void testGetObjectWithoutCreationWriter() throws Exception {
		FSDirectoryFactoryBean fsDirectoryFactoryBean = new FSDirectoryFactoryBean();
		fsDirectoryFactoryBean.setLocation(new ClassPathResource("/org/springmodules/lucene/index/support/"));
		fsDirectoryFactoryBean.setCreate(false);
		fsDirectoryFactoryBean.afterPropertiesSet();
		Directory fsDirectory = (Directory)fsDirectoryFactoryBean.getObject();
		assertNotNull(fsDirectory);
		assertEquals(fsDirectory.getClass(), FSDirectory.class);
		
		IndexWriter writer = null; 
		try {
			writer = new IndexWriter(fsDirectory, new SimpleAnalyzer(), false);
			fail();
		} catch(Exception ex) {
		} finally {
			closeIndexWriter(writer);
		}
	}

	/*public void testGetObjectWithCreationWriter() throws Exception {
		FSDirectoryFactoryBean fsDirectoryFactoryBean = new FSDirectoryFactoryBean();
		fsDirectoryFactoryBean.setLocation(new ClassPathResource("/org/springmodules/lucene/index/support/"));
		fsDirectoryFactoryBean.setCreate(true);
		fsDirectoryFactoryBean.afterPropertiesSet();
		Directory fsDirectory = (Directory)fsDirectoryFactoryBean.getObject();
		assertNotNull(fsDirectory);
		assertEquals(fsDirectory.getClass(), FSDirectory.class);

		IndexWriter writer = null;
		try {
			writer = new IndexWriter(fsDirectory, new SimpleAnalyzer(), true);
		} finally {
			closeIndexWriter(writer);
		}
	}*/

	public void testGetObjectWithoutCreationReader() throws Exception {
		FSDirectoryFactoryBean fsDirectoryFactoryBean = new FSDirectoryFactoryBean();
		fsDirectoryFactoryBean.setLocation(new ClassPathResource("/org/springmodules/lucene/index/support/"));
		fsDirectoryFactoryBean.setCreate(false);
		fsDirectoryFactoryBean.afterPropertiesSet();
		Directory fsDirectory = (Directory)fsDirectoryFactoryBean.getObject();
		assertNotNull(fsDirectory);
		assertEquals(fsDirectory.getClass(), FSDirectory.class);
		
		IndexReader reader = null;
		try {
			reader = IndexReader.open(fsDirectory);
			fail();
		} catch(Exception ex) {
		} finally {
			closeIndexReader(reader);
		}
	}

	/*public void testGetObjectWithCreationReader() throws Exception {
		FSDirectoryFactoryBean fsDirectoryFactoryBean = new FSDirectoryFactoryBean();
		fsDirectoryFactoryBean.setLocation(new ClassPathResource("/org/springmodules/lucene/index/support/"));
		fsDirectoryFactoryBean.setCreate(true);
		fsDirectoryFactoryBean.afterPropertiesSet();
		Directory fsDirectory = (Directory)fsDirectoryFactoryBean.getObject();
		assertNotNull(fsDirectory);
		assertEquals(fsDirectory.getClass(), FSDirectory.class);

		IndexReader reader = null;
		try {
			reader = IndexReader.open(fsDirectory);
			fail();
		} catch(Exception ex) {
		} finally {
			closeIndexReader(reader);
		}
	}*/
}
