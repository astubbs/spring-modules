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

package org.springmodules.samples.lucene.ui.web.category;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springmodules.samples.lucene.bean.indexing.DocumentCategory;
import org.springmodules.samples.lucene.service.category.CategoryService;

/**
 * @author Thierry Templier
 */
public class CategoryController extends MultiActionController {

	private CategoryService categoryService;

	private int getId(HttpServletRequest request) {
		String tmpId=request.getParameter("id");
		return Integer.parseInt(tmpId);
	}

	public ModelAndView list(HttpServletRequest request,
							  HttpServletResponse response) throws Exception {
		List categories=categoryService.getCategories();
		return new ModelAndView("category/categories","categories",categories);
	}

	public ModelAndView showCategory(HttpServletRequest request,
							  HttpServletResponse response) throws Exception {
		DocumentCategory category=categoryService.getCategory(getId(request));
		return new ModelAndView("category/category","category",category);
	}

	public CategoryService getCategoryService() {
		return categoryService;
	}

	public void setCategoryService(CategoryService service) {
		categoryService = service;
	}

}
