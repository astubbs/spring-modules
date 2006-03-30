package org.springmodules.beans.factory.bfl.parser;

import java.io.IOException;
import java.util.Collection;

import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;

public class BFLUtils {

	private BFLUtils() {
		super();
	}

	public static Collection parse(Resource resource, ApplicationContext parent) {
		try {
			BeanFactoryLanguageParser parser = new BeanFactoryLanguageParser(resource.getInputStream());
			parser.setApplicationContext(parent);
			return parser.parse();
		} catch (IOException e) {
			throw new ParseException(e);
		}
	}
}
