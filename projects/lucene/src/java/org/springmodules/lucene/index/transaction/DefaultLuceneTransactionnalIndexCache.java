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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.springmodules.lucene.index.transaction.operation.LuceneAddOperation;
import org.springmodules.lucene.index.transaction.operation.LuceneDeleteOperation;
import org.springmodules.lucene.index.transaction.operation.LuceneIndexOperation;
import org.springmodules.lucene.index.transaction.operation.LuceneOperationUtils;

/** 
 * @author Thierry Templier
 */
public class DefaultLuceneTransactionnalIndexCache
						extends AbstractLuceneOperationsProcessing
						implements LuceneTransactionalIndexCache {
	private List operations;

	public DefaultLuceneTransactionnalIndexCache() {
		this.operations = new LinkedList();
	}

	public void addOperation(LuceneOperation operation) {
		if( operation!=null ) {
			operations.add(operation);
		}
	}

	public void addOperations(List operations) {
		if( operations!=null ) {
			operations.addAll(operations);
		}
	}

	public void clear() {
		operations.clear();
	}

	public void removeOperation(LuceneOperation operation) {
		if( operation!=null ) {
			operations.remove(operation);
		}
	}

	public void removeAllDeleteOperations() {
		List deleteOperations = getOperationsByType(LuceneDeleteOperation.class);
		for(Iterator i=deleteOperations.iterator(); i.hasNext(); ) {
			LuceneOperation operation = (LuceneOperation)i.next();
			removeOperation(operation);
		}
	}

	public List getOperations() {
		return operations;
	}

	public boolean isDocumentAdded(Document document) {
		return hasDocumentInOperation(document, LuceneAddOperation.class);
	}

	public boolean isDocumentDeleted(Document document) {
		return hasDocumentInOperation(document, LuceneDeleteOperation.class);
	}

	public boolean hasDocumentInOperation(Document document, Class operationType) {
		List operations = getOperationsByType(operationType);
		for(Iterator it = operations.iterator(); it.hasNext(); ) {
			LuceneIndexOperation operation = (LuceneIndexOperation)it.next();
			if( containOperation(document, operation) ) {
				return true;
			}
		}
		return false;
	}

	private boolean containOperation(Document document, LuceneIndexOperation operation) {
		Document[] documents = operation.getDocuments();
		for(int i=0; i<documents.length; i++) {
			if( documents[i].equals(document) ) {
				return true;
			}
		}
		return false;
	}

	public List getOperationsByType(Class operationType) {
		return LuceneOperationUtils.getOperationsByType(operations, operationType);
	}

	public int getOperationNumberByType(Class operationType) {
		return LuceneOperationUtils.getOperationNumberByType(operations, operationType);
	}
}
