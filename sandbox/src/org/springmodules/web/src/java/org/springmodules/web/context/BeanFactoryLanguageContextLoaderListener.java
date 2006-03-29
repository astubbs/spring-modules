package org.springmodules.web.context;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.ContextLoaderListener;

public class BeanFactoryLanguageContextLoaderListener extends
		ContextLoaderListener {

	public BeanFactoryLanguageContextLoaderListener() {
		super();
	}

	protected ContextLoader createContextLoader() {
		return new BeanFactoryLanguageContextLoader();
	}
}
