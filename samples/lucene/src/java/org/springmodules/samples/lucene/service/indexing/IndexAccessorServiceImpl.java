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

package org.springmodules.samples.lucene.service.indexing;

import org.springmodules.samples.lucene.bean.indexing.IndexInformations;
import org.springmodules.samples.lucene.dao.indexing.DocumentIdDao;
import org.springmodules.samples.lucene.dao.indexing.IndexAccessorDao;
import org.springmodules.samples.lucene.ui.web.indexing.FileDocumentHolder;

/**
 * @author Thierry Templier
 */
public class IndexAccessorServiceImpl implements IndexAccessorService {

	private IndexAccessorDao indexAccessorDao;
	private DocumentIdDao documentIdDao;

	public IndexInformations getIndexInformations() {
		return indexAccessorDao.getIndexInformations();
	}

	public void addDocument(String title,
							String text,String category) {
		long documentId = documentIdDao.getNextDocumentId();
		indexAccessorDao.addDocument(String.valueOf(documentId), title, text, category);
		documentIdDao.incrementDocumentId();
	}

	public void addDocument(FileDocumentHolder holder) {
		long documentId = documentIdDao.getNextDocumentId();
		holder.setId(String.valueOf(documentId));
		indexAccessorDao.addDocument(holder);
		documentIdDao.incrementDocumentId();
	}

	public void purgeIndex() {
		indexAccessorDao.purgeIndex();
	}

	public IndexAccessorDao getIndexAccessorDao() {
		return indexAccessorDao;
	}

	public void setIndexAccessorDao(IndexAccessorDao dao) {
		indexAccessorDao = dao;
	}

	public DocumentIdDao getDocumentIdDao() {
		return documentIdDao;
	}

	public void setDocumentIdDao(DocumentIdDao dao) {
		documentIdDao = dao;
	}

}
