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

package org.springmodules.lucene.index.factory;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

/**
 * @author Thierry Templier
 */
public class RAMBufferedIndexFactoryProxy extends DelegatingIndexFactory {

	/**
	 * Create a new RAMBufferedIndexFactoryProxy.
	 * @see #setTargetIndexFactory
	 */
	public RAMBufferedIndexFactoryProxy() {
	}

	/**
	 * Create a new RAMBufferedIndexFactoryProxy.
	 * @param targetIndexFactory the target IndexFactory
	 */
	public RAMBufferedIndexFactoryProxy(IndexFactory targetIndexFactory) {
		setTargetIndexFactory(targetIndexFactory);
		afterPropertiesSet();
	}

	/**
	 * @see org.springmodules.lucene.index.factory.IndexFactory#getIndexReader()
	 */
	public IndexReader getIndexReader() {
		return getTargetIndexFactory().getIndexReader();
	}

	/**
	 */
	public IndexWriter getIndexWriter() {
		IndexWriter writer =
			IndexWriterFactoryUtils.getIndexWriter(getTargetIndexFactory());
		return getRAMBufferedWriterProxy(writer, getTargetIndexFactory());
	}

	/**
	 */
	protected IndexWriter getRAMBufferedWriterProxy(
		IndexWriter target,
		IndexFactory indexFactory) {
		return (IndexWriter) Proxy.newProxyInstance(
			IndexWriter.class.getClassLoader(),
			new Class[] { IndexWriter.class },
			new RAMBufferedInvocationHandler(target, indexFactory));
	}

	/**
	 * Invocation handler that delegates close calls on CCI Connections
	 * to ConnectionFactoryUtils for being aware of thread-bound transactions.
	 */
	private static class RAMBufferedInvocationHandler
		implements InvocationHandler {

		private static final String CONNECTION_CLOSE_METHOD_NAME = "close";

		private final RAMDirectory tmpDirectory;
		private IndexWriter tmpWriter;

		private final IndexWriter target;
		private final IndexFactory indexFactory;

		public RAMBufferedInvocationHandler(
			IndexWriter target,
			IndexFactory indexFactory) {
			this.target = target;
			this.indexFactory = indexFactory;
			//Create a temporary RAM directory
			this.tmpDirectory = new RAMDirectory();
			//Create a temporary writer on the temporary directory
			this.tmpWriter = createTemporaryWriter();
		}

		private IndexWriter createTemporaryWriter() {
			try {
				return new IndexWriter(tmpDirectory,target.getAnalyzer(),true);
			} catch(IOException ex) {
				ex.printStackTrace();
			}
			return null;
		}

		private void flushTemporaryDirectory() {
			if (this.indexFactory != null) {
				//Add the temporary indexes
				try {
					target.addIndexes(new Directory[] {tmpDirectory} );
				} catch(IOException ex) {
					ex.printStackTrace();
				}

				//Close the target index writer
				try {
					IndexWriterFactoryUtils.doCloseIndexWriterIfNecessary(
						this.indexFactory,
						this.target);
				} catch(IOException ex) {
					ex.printStackTrace();
				}

				//Close the temporary index writer
				try {
					tmpWriter.close();
				} catch(IOException ex) {
					ex.printStackTrace();
				}
			}
		}

		public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
			// Invocation on IndexProxy interface coming in...

			if (method.getName().equals("equals")) {
				// Only consider equal when proxies are identical.
				return (proxy == args[0] ? Boolean.TRUE : Boolean.FALSE);
			} else if (method.getName().equals("hashCode")) {
				// Use hashCode of Connection proxy.
				return new Integer(hashCode());
			} else if (
				method.getName().equals(CONNECTION_CLOSE_METHOD_NAME)) {
				// Handle close method: add temporary directory in the target index at the end
				// of the use of the resource.
				flushTemporaryDirectory();
				return null;
			}

			// Invoke method on target index writer.
			try {
				return method.invoke(this.target, args);
			} catch (InvocationTargetException ex) {
				throw ex.getTargetException();
			}
		}
	}

}
