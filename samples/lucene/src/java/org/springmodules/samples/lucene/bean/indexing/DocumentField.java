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

package org.springmodules.samples.lucene.bean.indexing;

/**
 * @author ttemplier
 *
 * Pour changer le modèle de ce commentaire de type généré, allez à :
 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class DocumentField {
	private String name;
	private String value;
	private boolean indexed;
	private boolean stored;

	public DocumentField(String name,String value,boolean indexed,boolean stored) {
		this.name=name;
		this.value=value;
		this.indexed=indexed;
		this.stored=stored;
	}

	/**
	 * @return
	 */
	public boolean isIndexed() {
		return indexed;
	}

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return
	 */
	public boolean isStored() {
		return stored;
	}

	/**
	 * @return
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param b
	 */
	public void setIndexed(boolean b) {
		indexed = b;
	}

	/**
	 * @param string
	 */
	public void setName(String string) {
		name = string;
	}

	/**
	 * @param b
	 */
	public void setStored(boolean b) {
		stored = b;
	}

	/**
	 * @param string
	 */
	public void setValue(String string) {
		value = string;
	}

}
