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

package org.springmodules.lucene.index.document.handler;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * This factory bean class is used to declaratively configured
 * a DocumentHandlerManager instance which can be used by template
 * or indexers.
 * 
 * <p>The configured DocumentHandlerManager can be based either on file
 * extensions because the factory bean uses the DocumentExtensionMatching
 * class to register the specified DocumentHandlers with the documentHandlers
 * property.
 * 
 * The class internally uses by default the class <code>DefaultDocumentHandlerManager</code>
 * as implementation of <code>DocumentHandlerManager</code> and <code>IdentityDocumentMatching</code>
 * as implementation of <code>DocumentMatching</code>. This can be changed according to
 * their different mutators.
 * In the case of a matching based on file extension, the <code>ExtensionDocumentMatching</code>
 * can be used.
 * 
 * @author Thierry Templier
 * @see org.springmodules.lucene.index.support.file.DocumentHandlerManager
 * @see #setDocumentHandlerManagerClass(Class)
 * @see #setDocumentMatchingClass(Class)
 * @see org.springmodules.lucene.index.support.handler.IdentityDocumentMatching
 * @see org.springmodules.lucene.index.support.handler.ExtensionDocumentMatching
 */
public class DocumentHandlerManagerFactoryBean implements FactoryBean,InitializingBean {

	private DocumentHandlerManager documentHandlerManager;
	private Map documentHandlers;

	private Class documentHandlerManagerClass=DefaultDocumentHandlerManager.class;
	private Class documentMatchingClass=IdentityDocumentMatching.class;
	private Constructor documentMatchingConstructor;
	
	/**
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	public Object getObject() throws Exception {
		return documentHandlerManager;
	}

	/**
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	public Class getObjectType() {
		return DocumentHandlerManager.class;
	}

	/**
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
	public boolean isSingleton() {
		return true;
	}

	private DocumentHandlerManager instanciateDocumentHandlerManager()
						throws InstantiationException, IllegalAccessException {
		return (DocumentHandlerManager)documentHandlerManagerClass.newInstance();
	}
	
	private DocumentMatching instanciateDocumentMatching(String name)
						throws InstantiationException, IllegalAccessException,
						IllegalArgumentException, InvocationTargetException,
						SecurityException, NoSuchMethodException {
		if( documentMatchingConstructor==null ) {
			documentMatchingConstructor=documentMatchingClass.getConstructor(
													new Class[] { String.class });
		}
		return (DocumentMatching)documentMatchingConstructor.newInstance(
													new Object[] { name });
	}

	/**
	 * This method registers all the configured DocumentHandler on the
	 * DocumentHandlerManager using the specified DocumentMatching after
	 * the call of the registerDefaultHandlers on the DocumentHandlerManager.
	 *
	 * @see DocumentHandlerManager#registerDefaultHandlers()
	 */
	public void afterPropertiesSet() throws Exception {
		documentHandlerManager=instanciateDocumentHandlerManager();
		documentHandlerManager.registerDefaultHandlers();
		if( documentHandlers!=null ) {
			Set documentHandlersKeys=documentHandlers.keySet();
			for(Iterator i=documentHandlersKeys.iterator();i.hasNext();) {
				String key=(String)i.next();
				documentHandlerManager.registerDocumentHandler(
								instanciateDocumentMatching(key),
								(DocumentHandler)documentHandlers.get(key));
			}
		}
	}

	public Map getDocumentHandlers() {
		return documentHandlers;
	}

	public void setDocumentHandlers(Map map) {
		documentHandlers = map;
	}

	public Class getDocumentHandlerManagerClass() {
		return documentHandlerManagerClass;
	}

	public void setDocumentHandlerManagerClass(Class documentHandlerManagerClass) {
		this.documentHandlerManagerClass = documentHandlerManagerClass;
	}

	public Class getDocumentMatchingClass() {
		return documentMatchingClass;
	}

	public void setDocumentMatchingClass(Class documentMatchingClass) {
		this.documentMatchingClass = documentMatchingClass;
	}

}
