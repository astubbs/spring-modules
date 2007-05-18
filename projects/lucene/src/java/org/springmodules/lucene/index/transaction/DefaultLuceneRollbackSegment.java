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

package org.springmodules.lucene.index.transaction;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.Term;
import org.springmodules.lucene.index.factory.IndexFactory;
import org.springmodules.lucene.index.transaction.operation.LuceneAddOperation;
import org.springmodules.lucene.index.transaction.operation.LuceneDeleteOperation;
import org.springmodules.lucene.index.transaction.operation.LuceneOperationUtils;

/** 
 * @author Thierry Templier
 */
public class DefaultLuceneRollbackSegment extends AbstractLuceneOperationsProcessing
													implements LuceneRollbackSegment {
	private static final String INTERNAL_UID = "internalUid";
	private List operations;
	private Analyzer analyzer;
	private boolean optimizeResourcesUsage;

	public DefaultLuceneRollbackSegment(Analyzer analyzer, boolean optimizeResourcesUsage) {
		this.operations = new LinkedList();
		this.analyzer = analyzer;
		this.optimizeResourcesUsage = optimizeResourcesUsage;
	}
	
	private void addCompensateOperationForAddDocument(LuceneAddOperation operation) {
		Document[] documents = operation.getDocuments();
		for(int i=0; i<documents.length; i++) {
			String internalUid = documents[i].get(INTERNAL_UID);
			LuceneDeleteOperation compensateOperation = new LuceneDeleteOperation();
			compensateOperation.setTerm(new Term(INTERNAL_UID, internalUid));
			operations.add(compensateOperation);
		}
	}

	private void addCompensateOperationForDeleteDocument(LuceneDeleteOperation operation) {
		LuceneAddOperation compensateOperation = new LuceneAddOperation();
		Document[] docs = operation.getDocuments();
		Document[] documents = new Document[docs.length];
		for(int i=0; i<docs.length; i++) {
			documents[i] = new Document();
			for(Enumeration e=docs[i].fields(); e.hasMoreElements(); ) {
				Field field = (Field)e.nextElement();
				System.out.println("  > field.stringValue() = "+field.stringValue());
				System.out.println("  > field.readerValue() = "+field.readerValue());
				//if( !field.isTokenized() ) {
				documents[i].add(new Field(field.name(), field.stringValue(),
						field.isStored() ? Field.Store.YES : Field.Store.NO,
						field.isTokenized() ? Field.Index.TOKENIZED : Field.Index.UN_TOKENIZED));
				//}
			}
		}
		compensateOperation.setDocuments(documents);
		compensateOperation.setAnalyzer(analyzer);
		operations.add(compensateOperation);
	}
	
	public void addCompensateOperation(LuceneOperation operation) {
		if( operation instanceof LuceneAddOperation ) {
			addCompensateOperationForAddDocument((LuceneAddOperation)operation);
		} else if( operation instanceof LuceneDeleteOperation ) {
			addCompensateOperationForDeleteDocument((LuceneDeleteOperation)operation);
		}
	}

	public List getCompensateOperations() {
		return operations;
	}
	
	public void removeCompensateOperationForDeleteDocuments() {
		List addOperations = LuceneOperationUtils.getOperationsByType(operations, LuceneAddOperation.class);
		for(Iterator i=addOperations.iterator(); i.hasNext(); ) {
			LuceneOperation operation = (LuceneOperation)i.next();
			operations.remove(operation);
		}
	}

	public void compensate(IndexFactory indexFactory) {
		System.out.println("-- compensate --------------------------------------");
		doExecuteOperations(indexFactory, operations, null, optimizeResourcesUsage);
	}
}
