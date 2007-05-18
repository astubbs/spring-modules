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

package org.springmodules.lucene.index.transaction.operation;

import org.springmodules.lucene.index.transaction.LuceneOperation;

/** 
 * @author Thierry Templier
 */
public abstract class LuceneFieldOperation implements LuceneOperation {
	private Object value;
	private String fieldName;
	
	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public long getLongValue() {
		return ((Long)value).longValue();
	}
	
	public void setLongValue(long value) {
		this.value = new Long(value);
	}

	public boolean getBooleanValue() {
		return ((Boolean)value).booleanValue();
	}
	
	public void setBooleanValue(boolean value) {
		this.value = new Boolean(value);
	}

	public int getIntValue() {
		return ((Integer)value).intValue();
	}
	
	public void setIntValue(int value) {
		this.value = new Integer(value);
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
}
