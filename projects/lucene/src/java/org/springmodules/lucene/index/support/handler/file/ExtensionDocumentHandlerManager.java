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

package org.springmodules.lucene.index.support.handler.file;

import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springmodules.lucene.index.support.handler.DefaultDocumentHandlerManager;
import org.springmodules.lucene.index.support.handler.DocumentHandler;
import org.springmodules.lucene.index.support.handler.DocumentHandlerManager;
import org.springmodules.lucene.util.IOUtils;

/**
 * This class is the document handler manager which is based
 * on the mime types of files.
 * 
 * <p>It only adds the text document handler as default handlers.
 * 
 * @author Thierry Templier
 * @see org.springmodules.lucene.index.support.handler.ExtensionDocumentMatching
 * @see org.springmodules.lucene.index.support.handler.file.TextDocumentHandler
 */
public class ExtensionDocumentHandlerManager extends DefaultDocumentHandlerManager implements DocumentHandlerManager {

	private static final String DOCUMENTHANDLER_EXTENSION_PROPERTIES
					= "org/springmodules/lucene/index/support/handler/file/documenthandler-extension.properties";
	private Resource documentHandlerProperties;

	/**
	 * Construct a new ExtensionDocumentHandlerManager.
	 */
	public ExtensionDocumentHandlerManager() {
		super();
		setDocumentHandlerProperties(new ClassPathResource(DOCUMENTHANDLER_EXTENSION_PROPERTIES));
	}

	private Properties loadDocumentHandlerProperties() {
		return IOUtils.loadPropertiesFromResource(documentHandlerProperties);
	}
	
	/**
	 * This method adds the document handlers contained in the property file
	 * as default handlers.
	 * 
	 * @see org.springmodules.lucene.index.object.file.DocumentHandlerManager#registerDefautHandlers()
	 */
	public void registerDefaultHandlers() {
		Properties properties=loadDocumentHandlerProperties();
		Set extensions=properties.keySet();
		for(Iterator i=extensions.iterator();i.hasNext();) {
			String extension=(String)i.next();
			String documentHandlerClassName=(String)properties.get(extension);
			doRegisterDocumentHandler(extension, documentHandlerClassName);
		}
	}

	private void doRegisterDocumentHandler(String extension, String documentHandlerClassName) {
		try {
			Class documentHandlerClass=Class.forName(documentHandlerClassName);
			registerDocumentHandler(new ExtensionDocumentMatching(extension),
						(DocumentHandler)documentHandlerClass.newInstance());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public Resource getDocumentHandlerProperties() {
		return documentHandlerProperties;
	}

	public void setDocumentHandlerProperties(Resource documentHandlerProperties) {
		this.documentHandlerProperties = documentHandlerProperties;
	}

}
