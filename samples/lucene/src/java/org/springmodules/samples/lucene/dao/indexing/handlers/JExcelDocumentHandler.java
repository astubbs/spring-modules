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

package org.springmodules.samples.lucene.dao.indexing.handlers;

import java.io.IOException;
import java.io.InputStream;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 * @author Thierry Templier
 */
public class JExcelDocumentHandler extends AbstractDocumentTypeFileHandler {

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

	private void appendText(StringBuffer text, String textToAppend) {
		text.append(" ");
		text.append(textToAppend);
	}

	protected void extractTextFromSheet(Sheet sheet, StringBuffer text) throws IOException {
		for(int cptRow=0; cptRow<sheet.getRows(); cptRow++) {
			for(int cptColumn=0; cptColumn<sheet.getColumns(); cptColumn++) {
				Cell cell = sheet.getCell(cptColumn,cptRow);
				String cellText = cell.getContents();
				if( cellText!=null && cellText.length()>0 ) {
					appendText(text, cellText);
				}
			}
		}
	}

}
