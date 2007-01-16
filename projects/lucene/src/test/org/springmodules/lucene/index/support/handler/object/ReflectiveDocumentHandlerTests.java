package org.springmodules.lucene.index.support.handler.object;

import java.util.HashMap;

import junit.framework.TestCase;

import org.apache.lucene.document.Document;
import org.springmodules.lucene.index.support.handler.DocumentHandler;

public class ReflectiveDocumentHandlerTests extends TestCase {
	public void testGetDocument() throws Exception {
		DocumentHandler documentHandler=new ReflectiveDocumentHandler();

		TestBean bean=new TestBean();
		bean.setField1("field1");
		bean.setField2(2);
		bean.setField3("field3");

		Document document = documentHandler.getDocument(new HashMap(), bean);

		assertEquals("field1", document.get("field1"));
		String typeField1 = document.getField("field1").toString();
		assertTrue(typeField1.startsWith("stored/uncompressed,indexed,tokenized<"));

		assertEquals("2", document.get("field2"));
		String typeField2 = document.getField("field2").toString();
		assertTrue(typeField2.startsWith("stored/uncompressed,indexed,tokenized<"));

		assertEquals("field3", document.get("field3"));
		String typeField3 = document.getField("field3").toString();
		assertTrue(typeField3.startsWith("stored/uncompressed,indexed,tokenized<"));
	}
}
