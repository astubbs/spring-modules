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

package org.springmodules.lucene.index.document.handler.file;

import org.springmodules.lucene.index.document.handler.DocumentMatching;

import sun.net.www.MimeTable;

/**
 * This implementation of the DocumentMatching interface determines
 * when a document handler must be used to index a document thanks
 * to its mime type.
 * 
 * @author Thierry Templier
 */
public class MimeTypeDocumentMatching implements DocumentMatching {

	private String mimeType;
	
	/**
	 * Construct a new MimeTypeDocumentMatching with the mime type
	 * to match.
	 */
	public MimeTypeDocumentMatching(String mimeType) {
		this.mimeType=mimeType;
	}

	/**
	 * This method retrieves the mime type from a name of a file.
	 * @param fileName the name of the file
	 * @return the mime type of the file
	 * 
	 * @see MimeTable
	 */
    public String getMimeType(String fileName) {
        MimeTable mt = MimeTable.getDefaultTable();
        String mimeType = mt.getContentTypeFor(fileName);
        if (mimeType == null)
                mimeType = "application/octet-stream";
        return mimeType;
}
	/**
	 * This method extracts the mime type from the name parameter
	 * which must be a filename and determines if it matches with
	 * the internal mime type property of the instance.
	 * 
	 * @see org.springmodules.lucene.index.object.DocumentMatching#match(java.lang.String)
	 */
	public boolean match(String name) {
		String mimeType=getMimeType(name);
		return mimeType.equals(this.mimeType);
	}

}
