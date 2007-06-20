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

package org.springmodules.lucene.index.transaction.compensation;

import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Method;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.Similarity;
import org.apache.lucene.store.Directory;
import org.springframework.util.ClassUtils;
import org.springmodules.lucene.index.core.WriterCallback;
import org.springmodules.lucene.index.factory.IndexFactory;
import org.springmodules.lucene.index.factory.IndexWriterFactoryUtils;
import org.springmodules.lucene.index.factory.LuceneIndexWriter;
import org.springmodules.lucene.index.resource.ResourceBindingManager;
import org.springmodules.lucene.index.resource.ResourceHolder;
import org.springmodules.lucene.index.transaction.AbstractTransactionalLuceneIndexWriter;
import org.springmodules.lucene.index.transaction.LuceneOperation;
import org.springmodules.lucene.index.transaction.LuceneRollbackSegment;
import org.springmodules.lucene.index.transaction.LuceneTransactionException;
import org.springmodules.lucene.index.transaction.operation.LuceneOperationUtils;

/** 
 * @author Thierry Templier
 */
public class CompensationTransactionalLuceneIndexWriter extends AbstractTransactionalLuceneIndexWriter {
	
	private IndexFactory delegate;
	
	public CompensationTransactionalLuceneIndexWriter(IndexFactory delegate, LuceneRollbackSegment rollbackSegment) {
		setDelegate(delegate);
		setRollbackSegment(rollbackSegment);
	}

	public void forceClose() {
		
	}

	public void close() throws IOException {
		//Skip this operation
	}

	public void addDocument(Document doc) throws IOException {
		addDocument(doc, null);
	}

	public void addDocument(final Document doc, final Analyzer analyzer) throws IOException {
		executeOnWriter(new WriterCallback() {
			public Object doWithWriter(LuceneIndexWriter writer) throws Exception {
				LuceneOperation operation = LuceneOperationUtils.getAddDocumentOperation(doc, analyzer);
				String internalUid = LuceneOperationUtils.createInternalUid();
				System.out.println("!!!!!! internalUid = "+internalUid);
				doc.add(new Field("internalUid", internalUid, Field.Store.YES, Field.Index.UN_TOKENIZED));
				writer.addDocument(doc, analyzer);
				getRollbackSegment().addCompensateOperation(operation);
				return null;
			}
		});
	}

	public void addIndexes(Directory[] dirs) throws IOException {
		//cf http://www.jguru.com/faq/home.jsp?topic=Lucene&page=2
		throw new LuceneTransactionException("Can not be called in a transactional context.");
	}

	public void addIndexes(IndexReader[] readers) throws IOException {
		//cf http://www.jguru.com/faq/home.jsp?topic=Lucene&page=2
		throw new LuceneTransactionException("Can not be called in a transactional context.");
	}

	public int docCount() {
		Integer count = (Integer)executeOnWriter(new WriterCallback() {
			public Object doWithWriter(LuceneIndexWriter writer) throws Exception {
				return new Integer(writer.docCount());
			}
		});
		return count.intValue();
	}

	public Analyzer getAnalyzer() {
		return null;
	}

	private String firstUpper(String s) {
		if( s.length()>1 ) {
			return s.substring(0, 1).toUpperCase() + s.substring(1);
		} else {
			return s;
		}
	}
	
	private Object executeGetField(final String fieldName) {
		final String methodName = "get" + firstUpper(fieldName);
		return executeOnWriter(new WriterCallback() {
			public Object doWithWriter(LuceneIndexWriter writer) throws Exception {
				Method method = ClassUtils.getMethodIfAvailable(writer.getClass(), methodName, new Class[] {});
				return method.invoke(writer, new Object[] {});
			}
		});
	}
	
	private Object executeSetField(final String fieldName, final Object value) {
		final String methodName = "set" + firstUpper(fieldName);
		return executeOnWriter(new WriterCallback() {
			public Object doWithWriter(LuceneIndexWriter writer) throws Exception {
				Method method = ClassUtils.getMethodIfAvailable(writer.getClass(), methodName, new Class[] { value.getClass() });
				return method.invoke(writer, new Object[] { value });
			}
		});
	}
	
	public long getCommitLockTimeout() {
		return ((Long)executeGetField("commitLockTimeout")).longValue();
	}

	public Directory getDirectory() {
		// TODO Auto-generated method stub
		return null;
	}

	public PrintStream getInfoStream() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getMaxBufferedDocs() {
		return ((Integer)executeGetField("maxBufferedDocs")).intValue();
	}

	public int getMaxFieldLength() {
		return ((Integer)executeGetField("maxFieldLength")).intValue();
	}

	public int getMaxMergeDocs() {
		return ((Integer)executeGetField("maxMergeDocs")).intValue();
	}

	public int getMergeFactor() {
		return ((Integer)executeGetField("mergeFactor")).intValue();
	}

	public Similarity getSimilarity() {
		return (Similarity)executeGetField("similarity");
	}

	public int getTermIndexInterval() {
		return ((Integer)executeGetField("termIndexInterval")).intValue();
	}

	public boolean getUseCompoundFile() {
		return ((Boolean)executeGetField("termIndexInterval")).booleanValue();
	}

	public long getWriteLockTimeout() {
		return ((Long)executeGetField("writeLockTimeout")).longValue();
	}

	public void optimize() throws IOException {
		executeOnWriter(new WriterCallback() {
			public Object doWithWriter(LuceneIndexWriter writer) throws Exception {
				writer.optimize();
				return null;
			}
		});
	}

	public void setCommitLockTimeout(final long commitLockTimeout) {
		executeOnWriter(new WriterCallback() {
			public Object doWithWriter(LuceneIndexWriter writer) throws Exception {
				long oldValue = writer.getCommitLockTimeout();
				writer.setCommitLockTimeout(commitLockTimeout);
				LuceneOperation operation = LuceneOperationUtils.getSetFieldOperation(
													"commitLockTimeout", new Long(oldValue));
				getRollbackSegment().addCompensateOperation(operation);
				return null;
			}
		});
	}

	public void setInfoStream(PrintStream infoStream) {
		// TODO Auto-generated method stub
		
	}

	public void setMaxBufferedDocs(final int maxBufferedDocs) {
		executeOnWriter(new WriterCallback() {
			public Object doWithWriter(LuceneIndexWriter writer) throws Exception {
				int oldValue = writer.getMaxBufferedDocs();
				System.out.println("###############################");
				System.out.println("> oldValue: "+oldValue);
				System.out.println("> maxBufferedDocs: "+maxBufferedDocs);
				writer.setMaxBufferedDocs(maxBufferedDocs);
				LuceneOperation operation = LuceneOperationUtils.getSetFieldOperation(
													"maxBufferedDocs", new Integer(oldValue));
				getRollbackSegment().addCompensateOperation(operation);
				System.out.println("###############################");
				return null;
			}
		});
	}

	public void setMaxFieldLength(final int maxFieldLength) {
		executeOnWriter(new WriterCallback() {
			public Object doWithWriter(LuceneIndexWriter writer) throws Exception {
				int oldValue = writer.getMaxFieldLength();
				writer.setMaxFieldLength(maxFieldLength);
				LuceneOperation operation = LuceneOperationUtils.getSetFieldOperation(
													"maxFieldLength", new Integer(oldValue));
				getRollbackSegment().addCompensateOperation(operation);
				return null;
			}
		});
	}

	public void setMaxMergeDocs(final int maxMergeDocs) {
		executeOnWriter(new WriterCallback() {
			public Object doWithWriter(LuceneIndexWriter writer) throws Exception {
				int oldValue = writer.getMaxMergeDocs();
				writer.setMaxMergeDocs(maxMergeDocs);
				LuceneOperation operation = LuceneOperationUtils.getSetFieldOperation(
													"maxMergeDocs", new Integer(oldValue));
				getRollbackSegment().addCompensateOperation(operation);
				return null;
			}
		});
	}

	public void setMergeFactor(final int mergeFactor) {
		executeOnWriter(new WriterCallback() {
			public Object doWithWriter(LuceneIndexWriter writer) throws Exception {
				int oldValue = writer.getMergeFactor();
				writer.setMergeFactor(mergeFactor);
				LuceneOperation operation = LuceneOperationUtils.getSetFieldOperation(
													"mergeFactor", new Integer(oldValue));
				getRollbackSegment().addCompensateOperation(operation);
				return null;
			}
		});
	}

	public void setSimilarity(final Similarity similarity) {
		executeOnWriter(new WriterCallback() {
			public Object doWithWriter(LuceneIndexWriter writer) throws Exception {
				Similarity oldValue = writer.getSimilarity();
				writer.setSimilarity(similarity);
				LuceneOperation operation = LuceneOperationUtils.getSetFieldOperation(
													"similarity", oldValue);
				getRollbackSegment().addCompensateOperation(operation);
				return null;
			}
		});
	}

	public void setTermIndexInterval(final int termIndexInterval) {
		executeOnWriter(new WriterCallback() {
			public Object doWithWriter(LuceneIndexWriter writer) throws Exception {
				int oldValue = writer.getTermIndexInterval();
				writer.setTermIndexInterval(termIndexInterval);
				LuceneOperation operation = LuceneOperationUtils.getSetFieldOperation(
													"termIndexInterval", new Integer(oldValue));
				getRollbackSegment().addCompensateOperation(operation);
				return null;
			}
		});
	}

	public void setUseCompoundFile(final boolean useCompoundFile) {
		executeOnWriter(new WriterCallback() {
			public Object doWithWriter(LuceneIndexWriter writer) throws Exception {
				boolean oldValue = writer.getUseCompoundFile();
				writer.setUseCompoundFile(useCompoundFile);
				LuceneOperation operation = LuceneOperationUtils.getSetFieldOperation(
													"useCompoundFile", new Boolean(oldValue));
				getRollbackSegment().addCompensateOperation(operation);
				return null;
			}
		});
	}

	public void setWriteLockTimeout(final long writeLockTimeout) {
		executeOnWriter(new WriterCallback() {
			public Object doWithWriter(LuceneIndexWriter writer) throws Exception {
				long oldValue = writer.getWriteLockTimeout();
				writer.setWriteLockTimeout(writeLockTimeout);
				LuceneOperation operation = LuceneOperationUtils.getSetFieldOperation(
													"writeLockTimeout", new Long(oldValue));
				getRollbackSegment().addCompensateOperation(operation);
				return null;
			}
		});
	}

}
