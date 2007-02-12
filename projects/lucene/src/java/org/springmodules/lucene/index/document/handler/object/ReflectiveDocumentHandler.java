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

package org.springmodules.lucene.index.document.handler.object;

import java.lang.reflect.Method;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

/**
 * Implementation of the DocumentHandler interface based on the reflection in
 * order to determine the fields to index.
 * 
 * @author Thierry Templier
 */
public class ReflectiveDocumentHandler extends AbstractObjectDocumentHandler {

	protected Document doGetDocument(Map description, Object object) throws Exception {
		Class clazz=object.getClass();
		Method[] methods=clazz.getDeclaredMethods();

		Document document = new Document();
		for(int cpt=0;cpt<methods.length;cpt++) {
			String name = methods[cpt].getName();
			if( name.startsWith(PREFIX_ACCESSOR) ) {
				String fieldName = constructFieldName(name);
				Object value = methods[cpt].invoke(object,new Object[] {});
				document.add(new Field(fieldName, String.valueOf(value), Field.Store.YES, Field.Index.TOKENIZED));
			}
		}
		return document;
	}

	public boolean supports(Class clazz) {
		return true;
	}

}
