package org.springmodules.lucene.index.support.handler.object;

import java.lang.reflect.Method;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

public class ReflectiveDocumentHandler extends AbstractObjectDocumentHandler {

	protected Document doGetDocument(Map description, Object object) throws Exception {
		Class clazz=object.getClass();
		Method[] methods=clazz.getDeclaredMethods();

		Document document = new Document();
		for(int cpt=0;cpt<methods.length;cpt++) {
			String name=methods[cpt].getName();
			if( name.startsWith(PREFIX_ACCESSOR) ) {
				String fieldName=constructFieldName(name);
				Object value=methods[cpt].invoke(object,new Object[] {});
				document.add(Field.Text(fieldName,String.valueOf(value)));
			}
		}
		return document;
	}

	public boolean supports(Class clazz) {
		return true;
	}

}
