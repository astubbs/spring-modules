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

package org.springmodules.samples.lucene.service.category;

import java.util.List;

import org.springmodules.samples.lucene.bean.indexing.DocumentCategory;
import org.springmodules.samples.lucene.dao.category.CategoryDao;

/**
 * @author Thierry Templier
 */
public class CategoryServiceImpl implements CategoryService {
	private CategoryDao categoryDao;

	public List getCategories() {
		return categoryDao.getCategories();
	}

	public DocumentCategory getCategory(int id) {
		return categoryDao.getCategory(id);
	}

	public void addCategory(DocumentCategory category) {
		categoryDao.addCategory(category);
	}

	public void updateCategory(DocumentCategory category) {
		categoryDao.updateCategory(category);
	}

	public void deleteCategory(DocumentCategory category) {
		categoryDao.deleteCategory(category);
	}

	public CategoryDao getCategoryDao() {
		return categoryDao;
	}

	public void setCategoryDao(CategoryDao dao) {
		categoryDao = dao;
	}

}