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

package org.springmodules.samples.lucene.index.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springmodules.lucene.index.FileExtensionNotSupportedException;
import org.springmodules.samples.lucene.index.service.IndexAccessor;

/**
 * @author Thierry Templier
 */
public class AddDocumentUploadController extends SimpleFormController {

	private IndexAccessor indexAccessor;

	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) throws ServletException {

		binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
	}

	public ModelAndView onSubmit(Object command) throws Exception {
		FileDocumentHolder holder=(FileDocumentHolder)command;
		try {
			indexAccessor.addDocument(holder);
		} catch(FileExtensionNotSupportedException ex) {
			return new ModelAndView("indexing/documentNotAdded","filename",holder.getFilename());
		}
		return new ModelAndView("indexing/documentAdded","filename",holder.getFilename());
	}

	public IndexAccessor getIndexAccessor() {
		return indexAccessor;
	}

	public void setIndexAccessor(IndexAccessor accessor) {
		indexAccessor = accessor;
	}

}
