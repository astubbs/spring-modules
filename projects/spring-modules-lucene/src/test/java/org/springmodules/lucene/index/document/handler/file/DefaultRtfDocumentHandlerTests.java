package org.springmodules.lucene.index.document.handler.file;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springmodules.lucene.util.IOUtils;

import junit.framework.TestCase;

public class DefaultRtfDocumentHandlerTests extends TestCase {
	private Resource getRtfResource() {
		return new ClassPathResource("/org/springmodules/lucene/index/document/handler/file/quick.rtf");
	}
	
	private void checkField(Document document, String fieldKey, boolean notNull) {
		Field field = document.getField(fieldKey);
		if( notNull) {
			assertNotNull(field);
		}
	}
	
	public void testGetDocument() {
		DefaultRtfDocumentHandler documentHandler = new DefaultRtfDocumentHandler();
		
		Resource rtfResource = getRtfResource();
		Map description = new HashMap();
		InputStream inputStream = null;
		try {
			inputStream = rtfResource.getInputStream();
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
