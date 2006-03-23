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

package org.springmodules.samples.lucene.ui.web.indexing;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springmodules.samples.lucene.bean.indexing.IndexInformations;
import org.springmodules.samples.lucene.service.category.CategoryService;
import org.springmodules.samples.lucene.service.indexing.IndexAccessorService;

/**
 * @author Thierry Templier
 */
public class ShowIndexInformationsController implements Controller {

	private IndexAccessorService indexAccessorService;
	private CategoryService categoryService;

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		IndexInformations infos=indexAccessorService.getIndexInformations();
		List categories=categoryService.getCategories();
		Map model=new HashMap();
		model.put("infos",infos);
		model.put("categories",categories);
		return new ModelAndView("indexing/indexInfos",model);
	}

	public IndexAccessorService getIndexAccessorService() {
		return indexAccessorService;
	}

	public void setIndexAccessorService(IndexAccessorService service) {
		indexAccessorService = service;
	}

	public CategoryService getCategoryService() {
		return categoryService;
	}

	public void setCategoryService(CategoryService service) {
		categoryService = service;
	}
}
