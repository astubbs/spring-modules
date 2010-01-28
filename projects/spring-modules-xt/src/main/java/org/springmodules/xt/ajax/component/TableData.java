/*
 * Copyright 2006 the original author or authors.
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

package org.springmodules.xt.ajax.component;

/**
 * Component implementing HTML table data inside a {@link TableRow}.
 *
 * @author Sergio Bossa
 */
public class TableData extends AbstractTableData {
    
    private static final long serialVersionUID = 26L;
    
    public TableData(Component data) {
    	super(data);
    }
    
    public TableData(Object dataObject, String property, TextRenderingCallback textRenderingCallback) {
        super(dataObject, property, textRenderingCallback);
    }

	protected String getTagName() {
		return "td";
	}
}
