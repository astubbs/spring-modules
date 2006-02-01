package org.springmodules.lucene.index.support.handler.file;

import java.io.InputStream;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.springmodules.lucene.index.support.handler.AbstractDocumentHandler;

public abstract class AbstractInputStreamDocumentHandler extends AbstractDocumentHandler {

	public final static String FILENAME="filename";

	public boolean supports(Class clazz) {
		return (InputStream.class).isAssignableFrom(clazz);
	}

	protected final Document doGetDocument(Map description, Object object) {
		return doGetDocumentWithInputStream(description,(InputStream)object);
	}

	protected abstract Document doGetDocumentWithInputStream(Map description,InputStream inputStream);
}
