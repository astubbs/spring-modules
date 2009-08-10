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

public class JExcelDocumentHandlerTests extends TestCase {
	private Resource getExcelResource() {
		return new ClassPathResource("/org/springmodules/lucene/index/document/handler/file/quick.xls");
	}
	
	private void checkField(Document document, String fieldKey, boolean notNull) {
		Field field = document.getField(fieldKey);
		if( notNull) {
			assertNotNull(field);
		}
	}
	
	public void testGetDocument() {
		JExcelDocumentHandler documentHandler = new JExcelDocumentHandler();
		
		Resource excelResource = getExcelResource();
		Map description = new HashMap();
		InputStream inputStream = null;
		try {
			inputStream = excelResource.getInputStream();
			Document document = documentHandler.doGetDocumentWithInputStream(description, inputStream);
			checkField(document, "contents", true);
			checkField(document, "filename", false);
		} catch(Exception ex) {
			ex.printStackTrace();
			fail();
		} finally {
			IOUtils.closeInputStream(inputStream);
		}
	}

}
