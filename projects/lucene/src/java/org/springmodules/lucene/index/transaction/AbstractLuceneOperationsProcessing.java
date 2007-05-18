package org.springmodules.lucene.index.transaction;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.springmodules.lucene.index.factory.IndexFactory;
import org.springmodules.lucene.index.factory.IndexReaderFactoryUtils;
import org.springmodules.lucene.index.factory.IndexWriterFactoryUtils;
import org.springmodules.lucene.index.factory.LuceneIndexReader;
import org.springmodules.lucene.index.factory.LuceneIndexWriter;
import org.springmodules.lucene.index.transaction.operation.LuceneAddOperation;
import org.springmodules.lucene.index.transaction.operation.LuceneDeleteOperation;
import org.springmodules.lucene.index.transaction.operation.LuceneOperationUtils;

public abstract class AbstractLuceneOperationsProcessing {

	private void executeDeleteOperation(IndexFactory indexFactory,
			LuceneDeleteOperation operation, LuceneRollbackSegment rollbackSegment) {
		LuceneIndexReader indexReader = null;
		try {
			indexReader = indexFactory.getIndexReader();
			doExecuteDeleteOperation(indexReader, operation, rollbackSegment);
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			IndexReaderFactoryUtils.closeIndexReader(indexReader);
		}
	}

	private void executeDeleteOperations(IndexFactory indexFactory,
			List operations, LuceneRollbackSegment rollbackSegment) {
		LuceneIndexReader indexReader = null;
		try {
			indexReader = indexFactory.getIndexReader();
			for(Iterator i=operations.iterator(); i.hasNext(); ) {
				LuceneDeleteOperation operation = (LuceneDeleteOperation)i.next();
				doExecuteDeleteOperation(indexReader, operation, rollbackSegment);
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			IndexReaderFactoryUtils.closeIndexReader(indexReader);
		}
	}

	private void doExecuteDeleteOperation(LuceneIndexReader indexReader, LuceneDeleteOperation operation,
											LuceneRollbackSegment rollbackSegment) throws IOException {
		Term term = operation.getTerm();
		indexReader.deleteDocuments(term);
		if( rollbackSegment!=null ) {
			rollbackSegment.addCompensateOperation(operation);
		}
	}

	private void executeAddOperation(IndexFactory indexFactory,
			LuceneAddOperation operation, LuceneRollbackSegment rollbackSegment) {
		LuceneIndexWriter indexWriter = null;
		try {
			indexWriter = indexFactory.getIndexWriter();
			doExecuteAddOperation(indexWriter, operation, rollbackSegment);
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			IndexWriterFactoryUtils.closeIndexWriter(indexWriter);
		}
	}

	private void executeAddOperations(IndexFactory indexFactory,
			List operations, LuceneRollbackSegment rollbackSegment) {
		LuceneIndexWriter indexWriter = null;
		try {
			indexWriter = indexFactory.getIndexWriter();
			for(Iterator i=operations.iterator(); i.hasNext(); ) {
				LuceneAddOperation operation = (LuceneAddOperation)i.next();
				doExecuteAddOperation(indexWriter, operation, rollbackSegment);
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			IndexWriterFactoryUtils.closeIndexWriter(indexWriter);
		}
	}

	private void doExecuteAddOperation(LuceneIndexWriter indexWriter, LuceneAddOperation operation,
										LuceneRollbackSegment rollbackSegment) throws IOException {
		Document[] documents = operation.getDocuments();
		Analyzer analyzer = operation.getAnalyzer();
		for(int i=0; i<documents.length; i++) {
			if( analyzer!=null ) {
				indexWriter.addDocument(documents[i]);
			} else {
				indexWriter.addDocument(documents[i], analyzer);
			}
		}
		if( rollbackSegment!=null ) {
			rollbackSegment.addCompensateOperation(operation);
		}
	}

	private String firstUpper(String s) {
		if( s.length()>1 ) {
			return s.substring(0, 1).toUpperCase() + s.substring(1);
		} else {
			return s;
		}
	}
	
	private void executeOperation(IndexFactory indexFactory,
					LuceneOperation operation, LuceneRollbackSegment rollbackSegment) {
		if( operation instanceof LuceneAddOperation ) {
			executeAddOperation(indexFactory, (LuceneAddOperation)operation, rollbackSegment);
		} else if( operation instanceof LuceneDeleteOperation ) {
			executeDeleteOperation(indexFactory, (LuceneDeleteOperation)operation, rollbackSegment);
		}
	}
	
	protected void doExecuteOperations(IndexFactory indexFactory, List operations) {
		doExecuteOperations(indexFactory, operations, null, false);
	}

	protected void doExecuteOperations(IndexFactory indexFactory, List operations,
						LuceneRollbackSegment rollbackSegment, boolean optimizeResourcesUsage) {

		if( optimizeResourcesUsage ) {
			List addOperations = LuceneOperationUtils.getOperationsByType(operations, LuceneAddOperation.class);
			executeAddOperations(indexFactory, addOperations, rollbackSegment);
			List deleteOperations = LuceneOperationUtils.getOperationsByType(operations, LuceneDeleteOperation.class);
			executeDeleteOperations(indexFactory, deleteOperations, rollbackSegment);
		} else {
			for(Iterator i = operations.iterator(); i.hasNext(); ) {
				LuceneOperation operation = (LuceneOperation)i.next();
				executeOperation(indexFactory, operation, rollbackSegment);
			}
		}
	}

}
