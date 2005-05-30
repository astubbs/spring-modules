package org.springmodules.web.context;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.ContextLoaderServlet;

public class BeanFactoryLanguageContextLoaderServlet extends
		ContextLoaderServlet {

	public BeanFactoryLanguageContextLoaderServlet() {
		super();
	}

	protected ContextLoader createContextLoader() {
		return new BeanFactoryLanguageContextLoader();
	}
}
