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

package org.springmodules.lucene.index.factory.concurrent;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.springmodules.lucene.index.LuceneIndexAccessException;
import org.springmodules.lucene.index.factory.LuceneIndexReader;
import org.springmodules.lucene.index.factory.LuceneIndexWriter;

import edu.emory.mathcs.backport.java.util.concurrent.locks.ReentrantLock;

/**
 * Concurrent {@link org.springmodules.lucene.index.factory.IndexFactory}
 * based on lock.
 * 
 * Wrap the target {@link org.springmodules.lucene.index.factory.IndexFactory}
 * in order to manage the acquisition and the release of locks.
 * 
 * The lock is acquired when an index reader or writer is getting (see {@link #getIndexReader()}
 * and {@link #getIndexWriter()} and is released by the close calls of these
 * wrapped instances.
 * 
 * @author Thierry Templier
 */
public class LockIndexFactory extends AbstractConcurrentIndexFactory {
	private static final String CLOSE_METHOD_NAME = "close";

	private ReentrantLock lock;

	/**
	 * @see org.springmodules.lucene.index.factory.concurrent.ConcurrentIndexFactory#destroyConcurrentResources()
	 */
	public void destroyConcurrentResources() throws Exception {
		lock = null;
	}

	/**
	 * @see org.springmodules.lucene.index.factory.concurrent.ConcurrentIndexFactory#initConcurrentResources()
	 */
	public void initConcurrentResources() throws Exception {
		lock = new ReentrantLock();
	}

	/**
	 * Implement the acquisition of a lock using the ReentrantLock class.
	 * 
	 * @throws InterruptedException
	 * @see edu.emory.mathcs.backport.java.util.concurrent.locks.ReentrantLock#lock()
	 */
	private void acquireLock() throws InterruptedException {
		lock.lock();
	}

	/**
	 * Implement the release of a lock using the ReentrantLock class.
	 * 
	 * @throws InterruptedException
	 * @see edu.emory.mathcs.backport.java.util.concurrent.locks.ReentrantLock#unlock()
	 */
	private void releaseLock() {
		lock.unlock();
	}

	/**
	 * Acquire a lock and return a proxy on a target IndexReader. This proxy
	 * will be able to manage the release of the lock on close calls.
	 * 
	 * @return a proxy on a target IndexReader
	 */
	public LuceneIndexReader getIndexReader() {
		try {
			acquireLock();
			LuceneIndexReader indexReader = getTargetIndexFactory().getIndexReader();
			return (LuceneIndexReader) Proxy.newProxyInstance(
					LuceneIndexReader.class.getClassLoader(),
					new Class[] {LuceneIndexReader.class},
					new LockLuceneIndexReaderInvocationHandler(indexReader));
		} catch(InterruptedException ex) {
			releaseLock();
			throw new LuceneIndexAccessException("Unable to manage lock", ex);
		}
	}

	/**
	 * Acquire a lock and return a proxy on a target IndexWriter. This proxy
	 * will be able to manage the release of the lock on close calls.
	 * 
	 * @return a proxy on a target IndexWriter
	 */
	public LuceneIndexWriter getIndexWriter() {
		try {
			acquireLock();
			LuceneIndexWriter indexWriter = getTargetIndexFactory().getIndexWriter();
			return (LuceneIndexWriter) Proxy.newProxyInstance(
					LuceneIndexWriter.class.getClassLoader(),
					new Class[] {LuceneIndexWriter.class},
					new LockLuceneIndexWriterInvocationHandler(indexWriter));
		} catch(InterruptedException ex) {
			releaseLock();
			throw new LuceneIndexAccessException("Unable to manage lock", ex);
		}
	}

	/**
	 * Invocation handler that releases locks after close calls of IndexReader
	 */
	private class LockLuceneIndexReaderInvocationHandler implements InvocationHandler {

		private final LuceneIndexReader indexReader;

		public LockLuceneIndexReaderInvocationHandler(LuceneIndexReader indexReader) {
			this.indexReader = indexReader;
		}

		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			Object ret = method.invoke(indexReader, args);
			if( CLOSE_METHOD_NAME.equals(method.getName()) ) {
				releaseLock();
			}
			return ret;
		}
	}

	/**
	 * Invocation handler that releases locks after close calls of IndexWriter
	 */
	private class LockLuceneIndexWriterInvocationHandler implements InvocationHandler {

		private final LuceneIndexWriter indexWriter;

		public LockLuceneIndexWriterInvocationHandler(LuceneIndexWriter indexWriter) {
			this.indexWriter = indexWriter;
		}

		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			Object ret = method.invoke(indexWriter, args);
			if( CLOSE_METHOD_NAME.equals(method.getName()) ) {
				releaseLock();
			}
			return ret;
		}
	}
}
