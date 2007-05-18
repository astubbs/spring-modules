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

import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.springmodules.lucene.index.factory.IndexFactory;

/** 
 * @author Thierry Templier
 */
public class DefaultLuceneTransactionProcessor
						extends AbstractLuceneOperationsProcessing
						implements LuceneTransactionProcessor {

	private static final String INTERNAL_UID_FIELD_NAME = "internalUid";

	private boolean optimizeResourcesUsage = false;

	public DefaultLuceneTransactionProcessor(boolean optimizeResourcesUsage) {
		this.optimizeResourcesUsage = optimizeResourcesUsage;
	}
	
	protected void addUidToDocument(Document document) {
		String internalUid = generateUid();
		document.add(new Field(INTERNAL_UID_FIELD_NAME, internalUid,
							Field.Store.YES, Field.Index.UN_TOKENIZED));
	}
	
	private String generateUid() {
		return null;
	}

	
	private void doApplyOperationsTransaction(IndexFactory indexFactory,
						List operations, LuceneRollbackSegment rollbackSegment) {
		doExecuteOperations(indexFactory, operations, rollbackSegment, optimizeResourcesUsage);
	}

	private void doRollbackOperations(IndexFactory indexFactory, LuceneRollbackSegment rollbackSegment) {
		rollbackSegment.compensate(indexFactory);
	}

	public void applyTransaction(IndexFactory indexFactory,
					LuceneTransactionalIndexCache cache, LuceneRollbackSegment rollbackSegment) {
		List operations = cache.getOperations();
		try {
			doApplyOperationsTransaction(indexFactory, operations, rollbackSegment);
		} catch(Exception ex) {
			doRollbackOperations(indexFactory, rollbackSegment);
		}
	}

}
