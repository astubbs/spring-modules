package org.springmodules.lucene.index.support.handler.object;

import org.springmodules.lucene.index.support.handler.AbstractDocumentHandler;

public abstract class AbstractObjectDocumentHandler extends AbstractDocumentHandler {

	public final static String PREFIX_ACCESSOR="get";
	
	protected String constructFieldName(String accessorName) {
		String name=accessorName.substring(PREFIX_ACCESSOR.length());
		return (name.substring(0,1).toLowerCase()+name.substring(1));
	}
	
}
