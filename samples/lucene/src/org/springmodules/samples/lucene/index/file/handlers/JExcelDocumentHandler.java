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

package org.springmodules.samples.lucene.index.file.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.springmodules.lucene.index.support.file.DocumentHandler;

/**
 * @author Thierry Templier
 */
public class JExcelDocumentHandler implements DocumentHandler {

	protected String extractText(InputStream inputStream) throws IOException {
		StringBuffer text=new StringBuffer();
		try {
			Workbook workbook=Workbook.getWorkbook(inputStream);
			for(int cpt=0;cpt<workbook.getNumberOfSheets();cpt++) {
				Sheet sheet=workbook.getSheet(cpt);
				extractTextFromSheet(sheet,text);
			}
		} catch(BiffException ex) {
			ex.printStackTrace();
		}
		return text.toString();
	}

	private void appendText(StringBuffer text,String textToAppend) {
		text.append(" ");
		text.append(textToAppend);
	}

	protected void extractTextFromSheet(Sheet sheet,StringBuffer text) throws IOException {
		for(int cptRow=0;cptRow<sheet.getRows();cptRow++) {
			for(int cptColumn=0;cptColumn<sheet.getColumns();cptColumn++) {
				Cell cell=sheet.getCell(cptColumn,cptRow);
				String cellText=cell.getContents();
				if( cellText!=null && cellText.length()>0 ) {
					appendText(text,cellText);
				}
			}
		}
	}

	/**
	 * @see org.springmodules.lucene.index.object.file.FileDocumentHandler#getDocument(java.io.File, java.io.FileReader)
	 */
	public Document getDocument(Map description, InputStream inputStream) throws IOException {
		Document document=new Document();
		String text=extractText(inputStream);
		if( text!=null || text.length()>0 ) {
			//The text is analyzed and indexed but not stored
			document.add(Field.UnStored("contents",text));
		}
		if( description.get(DocumentHandler.FILENAME)!=null ) {
			document.add(Field.Keyword("type", "file"));
			document.add(Field.Keyword("filename", (String)description.get(DocumentHandler.FILENAME)));
		}
		return document;
	}

	//private 

}
