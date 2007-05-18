package org.springmodules.lucene.index.document.handler.file;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springmodules.lucene.util.IOUtils;

public class PdfBoxDocumentHandlerTests extends TestCase {

	private Resource getPdfResource() {
		return new ClassPathResource("/org/springmodules/lucene/index/document/handler/file/quick.pdf");
	}
	
	private void checkField(Document document, String fieldKey, boolean notNull) {
		Field field = document.getField(fieldKey);
		if( notNull) {
			assertNotNull(field);
		}
	}
	
	public void testGetDocument() {
		PdfBoxDocumentHandler documentHandler = new PdfBoxDocumentHandler();
		
		Resource pdfResource = getPdfResource();
		Map description = new HashMap();
		InputStream inputStream = null;
		try {
			inputStream = pdfResource.getInputStream();
			Document document = documentHandler.doGetDocumentWithInputStream(description, inputStream);
			checkField(document, "path", false);
			checkField(document, "url", false);
			checkField(document, "contents", true);
			checkField(document, "summary", true);
			checkField(document, "modified", false);
			checkField(document, "uid", false);
			checkField(document, "CreationDate", true);
			checkField(document, "Creator", true);
			checkField(document, "Keywords", false);
			checkField(document, "ModificationDate", false);
			checkField(document, "Producer", true);
			checkField(document, "Subject", false);
			checkField(document, "Trapped", false);
		} catch(Exception ex) {
			ex.printStackTrace();
			fail();
		} finally {
			IOUtils.closeInputStream(inputStream);
		}
	}
}
