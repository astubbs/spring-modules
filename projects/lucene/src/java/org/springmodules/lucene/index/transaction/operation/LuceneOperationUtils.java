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

package org.springmodules.lucene.index.transaction.operation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.safehaus.uuid.UUID;
import org.safehaus.uuid.UUIDGenerator;
import org.springmodules.lucene.index.transaction.LuceneOperation;

/** 
 * @author Thierry Templier
 */
public abstract class LuceneOperationUtils {
	
	public static LuceneAddOperation getAddDocumentOperation(Document document, Analyzer analyzer) {
		LuceneAddOperation operation = new LuceneAddOperation();
		operation.setDocuments(new Document[] { document });
		operation.setAnalyzer(analyzer);
		return operation;
	}
	
	public static LuceneDeleteOperation getDeleteDocumentOperation(Document document) {
		LuceneDeleteOperation operation = new LuceneDeleteOperation();
		operation.setDocuments(new Document[] { document });
		return operation;
	}

	public static LuceneDeleteOperation getDeleteDocumentsOperation(Document[] documents, Term term) {
		LuceneDeleteOperation operation = new LuceneDeleteOperation();
		operation.setDocuments(documents);
		operation.setTerm(term);
		return operation;
	}

	public static LuceneSetOperation getSetFieldOperation(String fieldName, Object value) {
		LuceneSetOperation operation = new LuceneSetOperation();
		operation.setFieldName(fieldName);
		operation.setValue(value);
		return operation;
	}

	/*
	 * cf http://mule.codehaus.org/docs/site/mule-core/dependencies.html
	 * and the project Java UUID Generator. http://jug.safehaus.org/
	 */
	public static String createInternalUid() {
		UUIDGenerator generator = UUIDGenerator.getInstance(); 
		UUID uuid = generator.generateTimeBasedUUID(); 
		return uuid.toString();
	}

	public static List getOperationsByType(List operations, Class clazz) {
		List operationsByType = new ArrayList();
		for(Iterator i = operations.iterator(); i.hasNext(); ) {
			LuceneOperation operation = (LuceneOperation)i.next();
			if( operation.getClass()==clazz ) {
				operationsByType.add(operation);
			}
		}
		return operationsByType;
	}

	public static int getOperationNumberByType(List operations, Class clazz) {
		int nb = 0;
		for(Iterator i = operations.iterator(); i.hasNext(); ) {
			LuceneOperation operation = (LuceneOperation)i.next();
			if( operation.getClass()==clazz ) {
				nb++;
			}
		}
		return nb;
	}

}
